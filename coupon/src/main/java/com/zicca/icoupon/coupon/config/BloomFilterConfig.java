package com.zicca.icoupon.coupon.config;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.zicca.icoupon.coupon.common.constants.BloomFilterConstant.COUPON_TEMPLATE_REDIS_BLOOM_FILTER;

/**
 * 布隆过滤器配置
 *
 * @author zicca
 */
@Slf4j
@Configuration
public class BloomFilterConfig {


    /**
     * 优惠券模板分布式布隆过滤器 | 存储优惠券模板ID
     *
     * @param redissonClient redisson客户端
     * @return 布隆过滤器
     */
    @Bean(value = "couponTemplateRedisBloomFilter")
    public RBloomFilter<Long> couponTemplateRedisBloomFilter(RedissonClient redissonClient) {
        RBloomFilter<Long> bloomFilter = redissonClient.getBloomFilter(COUPON_TEMPLATE_REDIS_BLOOM_FILTER);
        bloomFilter.tryInit(640L, 0.001);
        return bloomFilter;
    }

    /**
     * 优惠券模板本地布隆过滤器 | 存储优惠券模板ID
     *
     * @return 布隆过滤器
     */
    @Bean(value = "couponTemplateGuavaBloomFilter")
    public BloomFilter<Long> couponTemplateGuavaBloomFilter() {
        return BloomFilter.create(Funnels.longFunnel(), 640L, 0.001);
    }


}
