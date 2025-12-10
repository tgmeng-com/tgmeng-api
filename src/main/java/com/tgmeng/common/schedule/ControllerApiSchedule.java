package com.tgmeng.common.schedule;

import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.common.config.ScheduleRequestConfigManager;
import com.tgmeng.common.enums.system.RequestFromEnum;
import com.tgmeng.common.forest.client.system.ISystemLocalClient;
import com.tgmeng.common.util.CacheUtil;
import com.tgmeng.common.util.SubscriptionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Service
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(
        value = "my-config.schedule.controller-api-top-search.enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class ControllerApiSchedule {

    private final ISystemLocalClient systemLocalClient;

    private final ThreadPoolTaskExecutor executor;

    // 自定义调度线程池（七大参数说明：ScheduledThreadPoolExecutor 内部固定了 队列=DelayedWorkQueue, 最大线程数=Integer.MAX_VALUE）
    private final ScheduledExecutorService timeoutScheduler = new ScheduledThreadPoolExecutor(
            Math.max(2, Runtime.getRuntime().availableProcessors() * 2), // 1. 核心线程数 (corePoolSize)
            r -> { // 6. 线程工厂 (threadFactory)
                Thread t = new Thread(r);
                t.setName("timeout-scheduler-" + t.getId());
                t.setDaemon(true);
                return t;
            },
            new ThreadPoolExecutor.CallerRunsPolicy() // 7. 拒绝策略 (handler)
    );
    // 所有接口的配置
    private final ScheduleRequestConfigManager scheduleRequestConfigManager;
    private final CacheUtil cacheUtil;
    private final SubscriptionUtil subscriptionUtil;

    // 启动后10s执行一次，完成后，每隔1分钟执行一次
    @Scheduled(fixedDelay = 60_000, initialDelay = 5_000)
    public void endpointsOneMinutesRefresh() {
        scanAndInvokeControllers(scheduleRequestConfigManager.getAllEnabledConfigsByRequestCycle(60L));
    }

    @Scheduled(fixedDelay = 300_000, initialDelay = 30_000)
    public void endpointsFiveMinutesRefresh() {
        scanAndInvokeControllers(scheduleRequestConfigManager.getAllEnabledConfigsByRequestCycle(300L));
    }

    @Scheduled(fixedDelay = 1_200_000, initialDelay = 30_000)
    public void endpointsTwentyMinutesRefresh() {
        scanAndInvokeControllers(scheduleRequestConfigManager.getAllEnabledConfigsByRequestCycle(1200L));
    }

    public void scanAndInvokeControllers(Map<String, ScheduleRequestConfigManager.PlatformConfig> configs) {
        long globalStart = System.currentTimeMillis();
        log.info("🤖 开始系统定时任务缓存数据，共 {} 个接口", configs.size());
        List<CompletableFuture<Void>> futures = configs.entrySet().stream()
                .map(entry -> {
                    String endpointKey = entry.getKey();
                    ScheduleRequestConfigManager.PlatformConfig config = entry.getValue();

                    long timeoutSeconds = config.getTimeout();
                    long delayMillis = config.getRequestDelay();

                    // 异步执行任务
                    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                        long start = System.currentTimeMillis();

                        try {
                            // 执行前延迟
                            if (delayMillis > 0) {
                                Thread.sleep(delayMillis);
                            }

                            // 调用接口
                            ResultTemplateBean result = systemLocalClient.systemLocalClient(
                                    RequestFromEnum.INTERNAL.getValue(),
                                    endpointKey
                            );

                            if (result.getData() != null) {
                                cacheUtil.put(endpointKey, result.getData());
                            } else {
                                log.warn("🚨🚨🚨 接口 {} 返回异常，data = null", endpointKey);
                            }

                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            log.warn("⛔⛔⛔ 接口 {} 被强制中断（超时可能触发）", endpointKey);

                        } catch (Exception e) {
                            log.error("🚨🚨🚨 接口 {} 执行异常", endpointKey, e);

                        } finally {
                            long cost = System.currentTimeMillis() - start;
                            log.info("🕒🕒🕒 接口 {} 执行结束，耗时 {} ms", endpointKey, cost);

                            if (cost > 60_000) {
                                log.warn("⚠️⚠️⚠️ 接口 {} 执行超过 1 分钟，用时 {} ms", endpointKey, cost);
                            }
                        }

                    }, executor);

                    // 【关键修复】使用独立调度线程池执行“超时中断”
                    timeoutScheduler.schedule(() -> {
                        if (!future.isDone()) {
                            log.warn("⛔⛔⛔ 接口 {} 超时（{} 秒），强制中断线程", endpointKey, timeoutSeconds);
                            future.cancel(true);
                        }
                    }, timeoutSeconds, TimeUnit.SECONDS);

                    return future;
                })
                .toList();


        try {
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            log.info("✅ 所有接口执行完成，耗时 {} ms", System.currentTimeMillis() - globalStart);

            long subStart = System.currentTimeMillis();
            subscriptionUtil.subscriptionOption();
            log.info("✅ 订阅操作完成，耗时 {} ms", System.currentTimeMillis() - subStart);

        } catch (Exception ex) {
            log.error("🚨🚨🚨 任务执行异常", ex);
        }

        log.info("🎉 本次定时任务全部完成，总耗时 {} ms", System.currentTimeMillis() - globalStart);

    }
}