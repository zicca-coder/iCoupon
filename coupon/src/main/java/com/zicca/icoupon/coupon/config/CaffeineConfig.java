package com.zicca.icoupon.coupon.config;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.zicca.icoupon.coupon.dto.resp.CouponTemplateQueryRespDTO;
import org.checkerframework.checker.index.qual.NonNegative;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * caffeine缓存配置
 *
 * @author zicca
 */
@Configuration
public class CaffeineConfig {

    @Bean(value = "couponTemplateCache")
    public Cache<String, CouponTemplateQueryRespDTO> couponTemplateCache() {
        Long nullValueMarker = -1L;
        return Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfter(new Expiry<String, CouponTemplateQueryRespDTO>() {
                    @Override
                    public long expireAfterCreate(String key, CouponTemplateQueryRespDTO value, long currentTime) {
                        if (ObjectUtil.isNotNull(value) && nullValueMarker.equals(value.getId())) {
                            return TimeUnit.MINUTES.toNanos(5); // 空值设置5分钟过期
                        }
                        return TimeUnit.MINUTES.toNanos(30);
                    }

                    @Override
                    public long expireAfterUpdate(String key, CouponTemplateQueryRespDTO value, long currentTime, @NonNegative long currentDuration) {
                        return currentDuration;
                    }

                    @Override
                    public long expireAfterRead(String key, CouponTemplateQueryRespDTO value, long currentTime, @NonNegative long currentDuration) {
                        return currentDuration;
                    }
                })
                .build();
    }



}
