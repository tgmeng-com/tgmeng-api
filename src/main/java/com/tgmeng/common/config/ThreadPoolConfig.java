package com.tgmeng.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ThreadPoolConfig {
    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 核心线程数，保持的最小线程数
        executor.setCorePoolSize(10);

        // 最大线程数
        executor.setMaxPoolSize(100);

        // 队列容量
        executor.setQueueCapacity(1000);

        // 线程空闲时间，超过该时间的空闲线程将被销毁
        executor.setKeepAliveSeconds(60);

        // 线程工厂：可以自定义线程名称等
        executor.setThreadNamePrefix("taskExecutor-");

        // 拒绝策略：当线程池和队列都满时的策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        executor.initialize();
        return executor;
    }
}
