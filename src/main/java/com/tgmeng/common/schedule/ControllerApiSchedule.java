package com.tgmeng.common.schedule;

import cn.hutool.core.date.StopWatch;
import com.tgmeng.common.bean.ResultTemplateBean;
import com.tgmeng.common.config.ScheduleRequestConfigManager;
import com.tgmeng.common.enums.system.RequestFromEnum;
import com.tgmeng.common.forest.client.system.ISystemLocalClient;
import com.tgmeng.common.util.CacheUtil;
import com.tgmeng.common.util.HotPointDataParquetUtil;
import com.tgmeng.common.util.SubscriptionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Map;

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

    // 所有接口的配置
    private final ScheduleRequestConfigManager scheduleRequestConfigManager;
    private final CacheUtil cacheUtil;
    private final SubscriptionUtil subscriptionUtil;
    private final HotPointDataParquetUtil hotPointDataParquetUtil;

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

    // 定时处理订阅
    @Scheduled(fixedDelay = 60_000, initialDelay = 60_000)
    public void subscriptionSchedule() {
        subscriptionUtil.subscriptionOption();
    }

    // 热点数据定时存储
    @Scheduled(fixedDelay = 60_000, initialDelay = 60_000)
    public void hotPointDataParquetUtilSchedule() {
        hotPointDataParquetUtil.saveToParquet();
    }

    // 每天2点，把昨天的热点数据合并到天级别
    @Scheduled(cron = "0 0 2 * * ?")
    public void mergeYesterdaySchedule() {
        hotPointDataParquetUtil.mergeYesterdaySchedule();
    }

    // 定时清理历史热点数据，每天3点清理数天前的历史数据，可以在yml中配置
    @Scheduled(cron = "0 0 3 * * ?")
    public void hotPointDataParquetCleanSchedule() {
        hotPointDataParquetUtil.cleanForParquet();
    }

    public void scanAndInvokeControllers(Map<String, ScheduleRequestConfigManager.PlatformConfig> configs) {
        log.info("🤖 开始系统定时任务缓存数据，共 {} 个接口", configs.size());
        // 所有接口异步并行执行，互不阻塞
        configs.forEach((endpointKey, config) -> {
            long delayMillis = config.getRequestDelay();

            executor.execute(() -> {
                // 如果配置了延迟，先 sleep
                if (delayMillis > 0) {
                    try {
                        Thread.sleep(delayMillis);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        log.warn("接口 {} 延迟被中断", endpointKey);
                        return;
                    }
                }
                StopWatch stopWatch = new StopWatch(endpointKey);
                stopWatch.start();
                try {
                    ResultTemplateBean result = systemLocalClient.systemLocalClient(
                            RequestFromEnum.INTERNAL.getValue(),
                            endpointKey
                    );

                    if (result.getData() != null) {
                        cacheUtil.put(endpointKey, result.getData());
                    } else {
                        log.warn("🚨 接口 {} 返回异常，data = null", endpointKey);
                    }
                } catch (Exception e) {
                    log.error("🚨 接口 {} 执行异常", endpointKey, e);
                } finally {
                    stopWatch.stop();
                    long cost = stopWatch.getTotalTimeMillis();
                    log.info("🕒 接口 {} 执行结束，耗时 {} ms", endpointKey, cost);

                    if (cost > 60_000) {
                        log.warn("⚠️ 接口 {} 执行超过 1 分钟，用时 {} ms", endpointKey, cost);
                    }
                }
            });
        });
    }
}