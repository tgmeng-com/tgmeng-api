package com.tgmeng.common.config;

import com.google.common.util.concurrent.RateLimiter;
import com.tgmeng.common.exception.ServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class LicenseRateLimiter {

    // QPM 限速器
    private final ConcurrentHashMap<String, RateLimiter> qpmLimiters = new ConcurrentHashMap<>();

    // QPD 计数器
    private final ConcurrentHashMap<String, AtomicInteger> qpdCounters = new ConcurrentHashMap<>();

    // 每天零点清空 QPD
    @Scheduled(cron = "0 0 0 * * ?")
    public void resetDailyCounters() {
        qpdCounters.clear();
        log.info("License QPD counters reset");
    }

    public void checkQPM(String licenseCode, int limitPerMinute) {
        RateLimiter limiter = qpmLimiters.computeIfAbsent(licenseCode, k -> RateLimiter.create(limitPerMinute / 10.0));
        if (!limiter.tryAcquire()) {
            throw new ServerException("每分钟调用次数已达上限");
        }
    }

    public void checkQPD(String licenseCode, int limitPerDay) {
        AtomicInteger counter = qpdCounters.computeIfAbsent(licenseCode, k -> new AtomicInteger(0));

        int used = counter.incrementAndGet();
        if (used > limitPerDay) {
            throw new ServerException("每日调用次数已达上限");
        }
    }
}