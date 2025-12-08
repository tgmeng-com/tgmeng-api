package com.tgmeng.common.schedule;

import cn.hutool.core.util.ObjectUtil;
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
import org.springframework.stereotype.Service;

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

    // 使用自定义线程池，避免使用ForkJoinPool
    private final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

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
        CompletableFuture<?>[] futures = configs.entrySet().stream()
                .map(endpoint -> CompletableFuture.runAsync(() -> {
                    try {
                        Thread.sleep(endpoint.getValue().getRequestDelay());
                        ResultTemplateBean resultTemplateBean = systemLocalClient.systemLocalClient(RequestFromEnum.INTERNAL.getValue(), endpoint.getKey());
                        if (ObjectUtil.isNotEmpty(resultTemplateBean.getData())) {
                            cacheUtil.put(endpoint.getKey(), resultTemplateBean.getData());
                            log.info("🤖成功缓存数据: {}", endpoint.getKey());
                        } else {
                            log.info("🤖❌定时任务，接口数据异常: {}，数据：{}", endpoint.getKey(), resultTemplateBean.getData());
                        }
                    } catch (Exception e) {
                        log.error("🤖❌定时任务，接口数据异常: {}", endpoint.getKey());
                    }
                }, executor))
                .toArray(CompletableFuture[]::new);
        //
        try {
            // 等待所有任务完成，设置超时时间
            CompletableFuture.allOf(futures)
                    .orTimeout(600, TimeUnit.SECONDS) // 10分钟超时
                    .join();

        } catch (CompletionException e) {
            if (e.getCause() instanceof TimeoutException) {
                // 处理超时情况
                log.warn("🤖执行超时:，取消未完成的任务");
                // 取消所有未完成的任务
                for (CompletableFuture<?> future : futures) {
                    if (!future.isDone()) {
                        future.cancel(true);
                    }
                }
            } else {
                // 其他异常
                log.error("🤖执行异常: {}", e.getMessage(), e);
            }
        } catch (Exception e) {
            log.error("🤖系执行异常: {}", e.getMessage(), e);
        }
        subscriptionUtil.subscriptionOption();
    }
}