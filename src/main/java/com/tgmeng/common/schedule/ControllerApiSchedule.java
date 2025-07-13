package com.tgmeng.common.schedule;

import com.tgmeng.common.forest.client.system.ISystemLocalClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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

    // 将endpoints提取为常量，便于维护
    private static final List<String> ENDPOINTS = Arrays.asList(
            // 国内热搜
            "/api/topsearch/baidu",
            "/api/topsearch/bilibili",
            "/api/topsearch/weibo",
            "/api/topsearch/douyin",
            "/api/topsearch/douban",
            "/api/topsearch/tencent",
            "/api/topsearch/toutiao",
            "/api/topsearch/wangyi",
            "/api/topsearch/yunwangyi",
            "/api/topsearch/tiebabaidu",
            "/api/topsearch/shaoshupai",
            "/api/topsearch/dianshijubaidu",
            "/api/topsearch/xiaoshuobaidu",
            "/api/topsearch/dianyingbaidu",
            "/api/topsearch/youxibaidu",
            "/api/topsearch/qichebaidu",
            "/api/topsearch/regengbaidu",
            "/api/topsearch/caijingbaidu",
            "/api/topsearch/minshengbaidu",
            "/api/topsearch/zhihu",

            // GitHub 热搜
            "/api/topsearch/github/allstars",
            "/api/topsearch/github/daystars",
            "/api/topsearch/github/weekstars",
            "/api/topsearch/github/monthstars",
            "/api/topsearch/github/yearstars",
            "/api/topsearch/github/threeyearstars",
            "/api/topsearch/github/fiveyearstars",
            "/api/topsearch/github/tenyearstars",

            // 国际热搜
            "/api/topsearch/global/youtube",
            "/api/topsearch/global/huggingfacespacestrending",
            "/api/topsearch/global/huggingfacespaceslikes",
            "/api/topsearch/global/huggingfacemodelstrending",
            "/api/topsearch/global/huggingfacemodellikes",
            "/api/topsearch/global/huggingfacedatasetstrending",
            "/api/topsearch/global/huggingfacedatasetslikes"
    );

    @Scheduled(cron = "${my-config.schedule.controller-api-top-search.schedule-rate}")
    public void scanAndInvokeControllers() {
        long startTime = System.currentTimeMillis();
        log.info("🤖🤖开始 系统定时缓存所有数据，共{}个接口👈👈", ENDPOINTS.size());

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        try {
            // 使用自定义线程池进行并行处理
            CompletableFuture<?>[] futures = ENDPOINTS.stream()
                    .map(endpoint -> CompletableFuture.runAsync(() -> {
                        try {
                            systemLocalClient.systemLocalClient(endpoint);
                            successCount.incrementAndGet();
                            log.info("🤖系统定时任务成功缓存: {}", endpoint);
                        } catch (Exception e) {
                            failureCount.incrementAndGet();
                            log.error("🤖系统定时任务缓存失败: {}, 错误: {}", endpoint, e.getMessage());
                        }
                    }, executor))
                    .toArray(CompletableFuture[]::new);

            // 等待所有任务完成，设置超时时间
            CompletableFuture.allOf(futures)
                    .orTimeout(300, TimeUnit.SECONDS) // 5分钟超时
                    .join();

        } catch (Exception e) {
            log.error("🤖系统定时任务执行异常: {}", e.getMessage(), e);
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        log.info("🤖🤖完成 系统定时缓存所有数据👈👈 " +
                        "成功: {}, 失败: {}, 总耗时: {}ms",
                successCount.get(), failureCount.get(), duration);
    }
}