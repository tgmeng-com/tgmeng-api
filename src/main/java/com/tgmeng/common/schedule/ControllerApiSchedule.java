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

        log.info("🤖开始:系统定时任务缓存数据，共{}个接口，👈👈", configs.size());
        // 使用自定义线程池进行并行处理
        // 将每个任务提交到线程池
        List<CompletableFuture<Void>> futures = configs.entrySet().stream()
                .map(endpoint -> CompletableFuture.runAsync(() -> {
                    try {
                        // 模拟延迟
                        Thread.sleep(endpoint.getValue().getRequestDelay());

                        // 调用远程接口获取数据
                        ResultTemplateBean resultTemplateBean = systemLocalClient.systemLocalClient(RequestFromEnum.INTERNAL.getValue(), endpoint.getKey());

                        // 处理返回的结果
                        if (resultTemplateBean.getData() != null) {
                            cacheUtil.put(endpoint.getKey(), resultTemplateBean.getData());
                            log.info("🤖成功缓存数据: {}", endpoint.getKey());
                        } else {
                            log.info("🤖❌定时任务，接口数据异常: {}，数据：{}", endpoint.getKey(), resultTemplateBean.getData());
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        log.error("任务被中断: {}", endpoint.getKey(), e);
                    } catch (Exception e) {
                        log.error("🤖❌定时任务，接口数据异常: {}", endpoint.getKey(), e);
                    }
                }, executor)) // 提交任务到线程池
                .toList();
        // 等待所有任务完成，设置超时
        try {
            // 使用 CompletableFuture.allOf() 等待所有任务完成
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                    .orTimeout(600, TimeUnit.SECONDS) // 60秒超时
                    .join(); // 等待任务完成

            // 如果需要的话，执行后续操作
        } catch (CompletionException e) {
            // 判断是否超时
            if (e.getCause() instanceof TimeoutException) {
                log.warn("🤖任务超时，未能完成所有任务");
                // 超时后取消未完成的任务
                futures.stream()
                        .filter(future -> !future.isDone())
                        .forEach(future -> future.cancel(true));
            } else {
                log.error("🤖执行异常: {}", e.getMessage(), e);
            }
        } catch (Exception e) {
            log.error("🤖执行异常: {}", e.getMessage(), e);
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