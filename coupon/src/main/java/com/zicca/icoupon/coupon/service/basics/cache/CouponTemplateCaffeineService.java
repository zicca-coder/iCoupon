package com.zicca.icoupon.coupon.service.basics.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.zicca.icoupon.coupon.dto.resp.CouponTemplateQueryRespDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 优惠券模板缓存服务
 *
 * @author zicca
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CouponTemplateCaffeineService {

    private final Cache<String, CouponTemplateQueryRespDTO> couponTemplateCache;
    private final String CACHE_PREFIX = "coupon_template:";
    private final Long NULL_VALUE_MARKER = -1L;

    /**
     * 缓存数据
     *
     * @param key   键
     * @param value 值
     */
    public void put(Long key, CouponTemplateQueryRespDTO value) {
        if (value == null || key == null) {
            log.warn("[优惠券模板本地缓存服务] 缓存数据失败: key={}, value={}", key, value);
            return;
        }
        String cacheKey = CACHE_PREFIX + key;
        couponTemplateCache.put(cacheKey, value);
        log.info("[优惠券模板本地缓存服务] 缓存数据成功: key={}, value={}", key, value);
    }

    /**
     * 缓存空值
     *
     * @param key 键
     */
    public void putNullToCache(Long key) {
        if (key == null) {
            log.warn("[优惠券模板本地缓存服务] 缓存空值失败: key=null");
            return;
        }
        String cacheKey = CACHE_PREFIX + key;
        CouponTemplateQueryRespDTO nullCache = CouponTemplateQueryRespDTO.builder().id(NULL_VALUE_MARKER).build();
        couponTemplateCache.put(cacheKey, nullCache);
        log.info("[优惠券模板本地缓存服务] 缓存空值成功: key={}", key);
    }


    public boolean isNullCache(CouponTemplateQueryRespDTO value) {
        if (value == null) {
            log.warn("[优惠券模板本地缓存服务] 判断是否缓存空值参数异常: value=null");
            return false;
        }
        return NULL_VALUE_MARKER.equals(value.getId());
    }

    /**
     * 获取缓存数据
     *
     * @param key 键
     * @return 值
     */
    public CouponTemplateQueryRespDTO get(Long key) {
        if (key == null) {
            log.warn("[优惠券模板本地缓存服务] 获取缓存数据失败: key=null");
            return null;
        }
        String cacheKey = CACHE_PREFIX + key;
        CouponTemplateQueryRespDTO value = couponTemplateCache.getIfPresent(cacheKey);
        if (value == null) {
            log.warn("[优惠券模板本地缓存服务] 缓存未命中: key={}", key);
            return null;
        }
        log.info("[优惠券模板本地缓存服务] 获取缓存数据成功: key={}, value={}", key, value);
        return value;
    }

    /**
     * 删除缓存数据
     *
     * @param key 键
     */
    public void delete(Long key) {
        if (key == null) {
            log.warn("[优惠券模板本地缓存服务] 删除缓存数据失败: key=null");
            return;
        }
        String cacheKey = CACHE_PREFIX + key;
        couponTemplateCache.invalidate(cacheKey);
        log.info("[优惠券模板本地缓存服务] 删除缓存数据成功: key={}", key);
    }

}
