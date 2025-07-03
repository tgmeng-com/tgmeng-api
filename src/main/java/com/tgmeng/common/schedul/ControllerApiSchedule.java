package com.tgmeng.common.schedul;

import com.tgmeng.common.forest.client.system.ISystemLocalClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ControllerApiSchedule {

    private final ISystemLocalClient systemLocalClient;

    @Scheduled(cron = "${my-config.schedule.controller-api-schedule-rate}") // 每60秒执行一次
    public void scanAndInvokeControllers() {
        log.info("👉👉开始 系统定时缓存所有数据👈👈");
        // 国内
        systemLocalClient.systemLocalClient("/api/topsearch/baidu");
        systemLocalClient.systemLocalClient("/api/topsearch/bilibili");
        systemLocalClient.systemLocalClient("/api/topsearch/weibo");
        systemLocalClient.systemLocalClient("/api/topsearch/douyin");
        systemLocalClient.systemLocalClient("/api/topsearch/douban");
        systemLocalClient.systemLocalClient("/api/topsearch/tencent");
        systemLocalClient.systemLocalClient("/api/topsearch/toutiao");
        systemLocalClient.systemLocalClient("/api/topsearch/wangyi");
        systemLocalClient.systemLocalClient("/api/topsearch/yunwangyi");
        systemLocalClient.systemLocalClient("/api/topsearch/tiebabaidu");
        systemLocalClient.systemLocalClient("/api/topsearch/shaoshupai");
        // Github
        systemLocalClient.systemLocalClient("/api/topsearch/github/allstars");
        systemLocalClient.systemLocalClient("/api/topsearch/github/daystars");
        systemLocalClient.systemLocalClient("/api/topsearch/github/weekstars");
        systemLocalClient.systemLocalClient("/api/topsearch/github/monthstars");
        systemLocalClient.systemLocalClient("/api/topsearch/github/yearstars");
        systemLocalClient.systemLocalClient("/api/topsearch/github/threeyearstars");
        systemLocalClient.systemLocalClient("/api/topsearch/github/fiveyearstars");
        systemLocalClient.systemLocalClient("/api/topsearch/github/tenyearstars");
        // 国际
        systemLocalClient.systemLocalClient("/api/topsearch/global/youtube");

        log.info("👉👉完成 系统定时缓存所有数据👈👈");
    }
}
