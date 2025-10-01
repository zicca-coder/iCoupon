package com.zicca.icoupon.coupon.common.constants;

/**
 * redis常量
 *
 * @author zicca
 */
public class RedisConstant {

    /**
     * 优惠券模板缓存 Key
     */
    public static final String COUPON_TEMPLATE_KEY = "icoupon:coupon:cache:template:exist:";


    /**
     * 优惠券模板锁 Key | 查询优惠券模板分布式锁，避免多个线程同时查询数据库造成压力
     */
    public static final String COUPON_TEMPLATE_LOCK_KEY = "icoupon:coupon:lock:template:select";

    /**
     * 布隆过滤器版本号 Key
     */
    public static final String BLOOM_FILTER_VERSION_KEY = "icoupon:coupon:cache:bloomfilter:version";

    /**
     * 布隆过滤器锁 Key | 构建布隆过滤器分布式锁，避免多个线程同时构建
     */
    public static final String BLOOM_FILTER_LOCK_KEY = "icoupon:coupon:lock:bloomfilter:rebuild";


    /**
     * 用户优惠券列表缓存 Key | 缓存用户领取的优惠券列表
     */
    public static final String USER_COUPON_LIST_KEY = "icoupon:coupon:cache:usercoupon:list:";

    /**
     * 用户优惠券领取限制缓存 Key | 缓存用户领取优惠券的次数限制
     */
    public static final String USER_COUPON_LIMIT_KEY = "icoupon:coupon:cache:usercoupon:limit:";

}
