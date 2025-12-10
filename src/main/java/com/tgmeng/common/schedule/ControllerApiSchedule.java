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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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

    // 使用自定义线程池，避免使用ForkJoinPool
    //private final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
    private final ThreadPoolTaskExecutor executor;
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

        log.info("🤖开始:系统定时任务缓存数据，共{}个接口", configs.size());

        // 提交任务
        List<CompletableFuture<Void>> futures = configs.entrySet().stream()
                .map(entry -> {
                    String endpointKey = entry.getKey();
                    ScheduleRequestConfigManager.PlatformConfig config = entry.getValue();
                    long timeoutSeconds = config.getTimeout(); // 每个接口独立超时

                    return CompletableFuture.runAsync(() -> {
                                long startTime = System.currentTimeMillis();
                                try {
                                    Thread.sleep(config.getRequestDelay());

                                    ResultTemplateBean result = systemLocalClient.systemLocalClient(
                                            RequestFromEnum.INTERNAL.getValue(), endpointKey);

                                    if (result.getData() != null) {
                                        cacheUtil.put(endpointKey, result.getData());
                                    } else {
                                        log.warn("🚨🚨🚨 接口数据异常: {}，数据：{}", endpointKey, result.getData());
                                    }

                                    long elapsed = System.currentTimeMillis() - startTime;
                                    log.info("🕒 接口 {} 执行结束，耗时 {}ms", endpointKey, elapsed);
                                    if (elapsed > 60_000) {
                                        log.warn("⚠️⚠️⚠️ 接口 {} 执行结束，超过1分钟: {}ms", endpointKey, elapsed);
                                    }

                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                    log.error("🚨🚨🚨 任务被中断: {}", endpointKey, e);
                                } catch (Exception e) {
                                    log.error("🚨🚨🚨 接口异常: {}", endpointKey, e);
                                }
                            }, executor)
                            .orTimeout(timeoutSeconds, TimeUnit.SECONDS)
                            .exceptionally(ex -> {
                                if (ex instanceof TimeoutException) {
                                    log.warn("🚨🚨🚨 接口 {} 超时（{}秒）", endpointKey, timeoutSeconds);
                                } else {
                                    log.error("🚨🚨🚨 接口执行异常: {}", endpointKey, ex);
                                }
                                return null;
                            });
                })
                .toList();

        // 全局等待所有任务完成
        try {
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                    .join(); // 等待全部完成
        } catch (CompletionException e) {
            log.error("🤖全局任务异常: {}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("🤖全局任务异常: {}", e.getMessage(), e);
        } finally {
            // 确保订阅操作始终执行
            try {
                subscriptionUtil.subscriptionOption();
            } catch (Exception e) {
                log.error("订阅操作执行失败: {}", e.getMessage(), e);
            }
        }
    }
}