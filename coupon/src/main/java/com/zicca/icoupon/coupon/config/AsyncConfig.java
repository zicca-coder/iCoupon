package com.zicca.icoupon.coupon.config;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * 异步配置
 *
 * @author zicca
 */
@Configuration
public class AsyncConfig {

    /**
     * 布隆过滤器初始化线程池
     */
    @Bean("bloomFilterInitExecutor")
    public ThreadPoolExecutor bloomFilterInitExecutor() {
        return new ThreadPoolExecutor(
                1,
                1,
                3L,
                TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(10),
                new ThreadFactoryBuilder().setNamePrefix("coupon-template-bloom-filter-init-%d").build(),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    /**
     * 预约提醒推送线程池
     */
    @Bean("reminderPushExecutor")
    public ThreadPoolExecutor reminderPushExecutor() {
        return new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors() << 1,
                Runtime.getRuntime().availableProcessors() << 2,
                60L,
                TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                new ThreadFactoryBuilder().setNamePrefix("coupon-reservation-reminder-push-%d").build(),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

}
