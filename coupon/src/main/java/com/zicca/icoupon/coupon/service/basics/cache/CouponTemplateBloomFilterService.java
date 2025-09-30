package com.zicca.icoupon.coupon.service.basics.cache;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.zicca.icoupon.coupon.dao.mapper.CouponTemplateMapper;
import com.zicca.icoupon.framework.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

import static com.zicca.icoupon.coupon.common.constants.BloomFilterConstant.COUPON_TEMPLATE_REDIS_BLOOM_FILTER;
import static com.zicca.icoupon.coupon.common.constants.RedisConstant.BLOOM_FILTER_LOCK_KEY;
import static com.zicca.icoupon.coupon.common.constants.RedisConstant.BLOOM_FILTER_VERSION_KEY;

/**
 * 优惠券模板布隆过滤器服务
 *
 * @author zicca
 */
@Slf4j
@Service
public class CouponTemplateBloomFilterService implements ApplicationRunner {


    private final AtomicReference<RBloomFilter<Long>> redisBloomFilterRef;
    private final AtomicReference<BloomFilter<Long>> guavaBloomFilterRef;
    private CouponTemplateMapper couponTemplateMapper;
    private ThreadPoolExecutor bloomFilterInitExecutor;
    private RedissonClient redissonClient;
    private StringRedisTemplate stringRedisTemplate;


    public CouponTemplateBloomFilterService(@Autowired RBloomFilter<Long> redisBloomFilter,
                                            @Autowired BloomFilter<Long> guavaBloomFilter,
                                            @Autowired CouponTemplateMapper couponTemplateMapper,
                                            @Autowired ThreadPoolExecutor bloomFilterInitExecutor,
                                            @Autowired RedissonClient redissonClient,
                                            @Autowired StringRedisTemplate stringRedisTemplate) {
        this.redisBloomFilterRef = new AtomicReference<>(redisBloomFilter);
        this.guavaBloomFilterRef = new AtomicReference<>(guavaBloomFilter);
        this.couponTemplateMapper = couponTemplateMapper;
        this.bloomFilterInitExecutor = bloomFilterInitExecutor;
        this.redissonClient = redissonClient;
        this.stringRedisTemplate = stringRedisTemplate;
    }


    /**
     * 添加元素
     *
     * @param couponTemplateId 优惠券模板id
     */
    public void add(Long couponTemplateId) {
        if (ObjectUtil.isNull(couponTemplateId)) {
            log.warn("[优惠券模板布隆过滤器服务] 添加元素 | 添加至布隆过滤器 - 添加元素参数异常: couponTemplateId={}", couponTemplateId);
            return;
        }
        addToRedis(couponTemplateId);
        addToGuava(couponTemplateId);
    }


    /**
     * 添加元素至分布式布隆过滤器
     *
     * @param couponTemplateId 优惠券模板id
     */
    public void addToRedis(Long couponTemplateId) {
        if (ObjectUtil.isNull(couponTemplateId)) {
            log.warn("[优惠券模板布隆过滤器服务] 添加元素 | 添加至分布式布隆过滤器 - 添加元素参数异常: couponTemplateId={}", couponTemplateId);
            return;
        }
        redisBloomFilterRef.get().add(couponTemplateId);
        log.debug("[优惠券模板布隆过滤器服务] 添加元素 | 添加至分布式布隆过滤器 - 添加元素成功: couponTemplateId={}", couponTemplateId);
    }

    /**
     * 添加元素至本地布隆过滤器
     *
     * @param couponTemplateId 优惠券模板id
     */
    public void addToGuava(Long couponTemplateId) {
        if (ObjectUtil.isNull(couponTemplateId)) {
            log.warn("[优惠券模板布隆过滤器服务] 添加元素 | 添加至本地布隆过滤器 - 添加元素参数异常: couponTemplateId={}", couponTemplateId);
            return;
        }
        guavaBloomFilterRef.get().put(couponTemplateId);
        log.debug("[优惠券模板布隆过滤器服务] 添加元素 | 添加至本地布隆过滤器 - 添加元素成功: couponTemplateId={}", couponTemplateId);
    }

    /**
     * 检测元素是否存在于分布式布隆过滤器
     *
     * @param couponTemplateId 优惠券模板id
     * @return 存在返回true，不存在返回false
     */
    public boolean mightContainInRedis(Long couponTemplateId) {
        if (ObjectUtil.isNull(couponTemplateId)) {
            log.warn("[优惠券模板布隆过滤器服务] 检测元素是否存在 | 检查元素是否存在于分布式布隆过滤器 - 检查元素参数异常: couponTemplateId={}", couponTemplateId);
            throw new ServiceException("参数异常");
        }
        return redisBloomFilterRef.get().contains(couponTemplateId);
    }

    /**
     * 检测元素是否存在于本地布隆过滤器
     *
     * @param couponTemplateId 优惠券模板id
     * @return 存在返回true，不存在返回false
     */
    public boolean mightContainInGuava(Long couponTemplateId) {
        if (ObjectUtil.isNull(couponTemplateId)) {
            log.warn("[优惠券模板布隆过滤器服务] 检测元素是否存在 | 检查元素是否存在于本地布隆过滤器 - 检查元素参数异常: couponTemplateId={}", couponTemplateId);
            throw new ServiceException("参数异常");
        }
        return guavaBloomFilterRef.get().mightContain(couponTemplateId);
    }

    /**
     * 检测元素是否不存在于分布式布隆过滤器
     *
     * @param couponTemplateId 优惠券模板id
     * @return 不存在返回true，存在返回false
     */
    public boolean isNotExistInRedis(Long couponTemplateId) {
        if (ObjectUtil.isNull(couponTemplateId)) {
            log.warn("[优惠券模板布隆过滤器服务] 检测元素是否不存在 | 检查元素是否不存在于分布式布隆过滤器 - 检查元素参数异常: couponTemplateId={}", couponTemplateId);
            throw new ServiceException("参数异常");
        }
        return !redisBloomFilterRef.get().contains(couponTemplateId);
    }

    /**
     * 检测元素是否不存在于本地布隆过滤器
     *
     * @param couponTemplateId 优惠券模板id
     * @return 不存在返回true，存在返回false
     */
    public boolean isNotExistInGuava(Long couponTemplateId) {
        if (ObjectUtil.isNull(couponTemplateId)) {
            log.warn("[优惠券模板布隆过滤器服务] 检测元素是否不存在 | 检查元素是否不存在于本地布隆过滤器 - 检查元素参数异常: couponTemplateId={}", couponTemplateId);
            throw new ServiceException("参数异常");
        }
        return !guavaBloomFilterRef.get().mightContain(couponTemplateId);
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        CompletableFuture.runAsync(this::initializeBloomFilter, bloomFilterInitExecutor);
    }

    private String getCurrentMonthVersion() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        return sdf.format(new Date());
    }


    // 使用xxl-job可以避免集群部署存在的重复执行问题
    @Scheduled(cron = "0 0 2 1 * ?") // 每月1日凌晨2点执行
    public void rebuildBloomFilterScheduled() {
        String currentVersion = getCurrentMonthVersion();
        String storedVersion = (String) stringRedisTemplate.opsForValue().get(BLOOM_FILTER_VERSION_KEY);

        if (currentVersion.equals(storedVersion)) {
            log.info("[优惠券模板布隆过滤器服务] 当前版本已存在，跳过重建: {}", currentVersion);
            return;
        }
        RLock lock = redissonClient.getLock(BLOOM_FILTER_LOCK_KEY);
        try {
            if (lock.tryLock(3, TimeUnit.SECONDS)) {
                try {
                    // 再次检查版本，防止重复执行
                    String latestVersion = (String) stringRedisTemplate.opsForValue().get(BLOOM_FILTER_VERSION_KEY);
                    if (currentVersion.equals(latestVersion)) {
                        log.info("[优惠券模板布隆过滤器服务] 获取锁后发现版本已存在，跳过重建: {}", currentVersion);
                        return;
                    }
                    // 执行重建
                    rebuildBloomFilter();
                    // 更新版本号
                    stringRedisTemplate.opsForValue().set(BLOOM_FILTER_VERSION_KEY, currentVersion);
                    log.info("[优惠券模板布隆过滤器服务] 布隆过滤器重建完成，版本更新为: {}", currentVersion);
                } finally {
                    lock.unlock();
                }
            } else {
                log.info("[优惠券模板布隆过滤器服务] 未能获取到重建锁，其他节点正在执行重建");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("[优惠券模板布隆过滤器服务] 获取重建锁时被中断");
        } catch (Exception e) {
            log.error("[优惠券模板布隆过滤器服务] 重建布隆过滤器失败", e);
        }
    }

    private void rebuildBloomFilter() {
        try {
            log.info("[优惠券模板布隆过滤器服务] 开始重建布隆过滤器");
            RBloomFilter<Long> redisBloomFilter = redissonClient.getBloomFilter(COUPON_TEMPLATE_REDIS_BLOOM_FILTER);
            redisBloomFilter.tryInit(640L, 0.001);
            BloomFilter<Long> guavaBloomFilter = BloomFilter.create(Funnels.longFunnel(), 640L, 0.001);
            List<Long> ids = couponTemplateMapper.fetchCouponTemplateIds();
            if (CollUtil.isEmpty(ids)) {
                log.info("[优惠券模板布隆过滤器服务] 获取优惠券模板id列表为空");
                return;
            }
            // 批量添加到布隆过滤器
            ids.forEach(id -> {
                redisBloomFilter.add(id);
                guavaBloomFilter.put(id);
            });
            log.info("[优惠券模板布隆过滤器服务] 布隆过滤器重建完成，处理了 {} 个优惠券模板", ids.size());
            // 直接替换，保证最终一致性
            redisBloomFilterRef.set(redisBloomFilter);
            guavaBloomFilterRef.set(guavaBloomFilter);
            log.info("[优惠券模板布隆过滤器服务] 布隆过滤器重建完成");
        } catch (Exception e) {
            log.error("[优惠券模板布隆过滤器服务] 布隆过滤器重建失败", e);
            throw new RuntimeException("布隆过滤器重建失败", e);
        }
    }


    /**
     * 初始化布隆过滤器 | 将数据库中的有效优惠券模板id添加至布隆过滤器
     */
    private void initializeBloomFilter() {
        try {
            log.info("[优惠券模板布隆过滤器服务] 应用启动后异步初始化开始");
            List<Long> ids = couponTemplateMapper.fetchCouponTemplateIds();
            if (CollUtil.isEmpty(ids)) {
                log.info("[优惠券模板布隆过滤器服务] 获取优惠券模板id列表为空，数据库信息同步完成");
                return;
            }
            ids.forEach(this::add);
            String currentVersion = getCurrentMonthVersion();
            stringRedisTemplate.opsForValue().set(BLOOM_FILTER_VERSION_KEY, currentVersion);
            log.info("[优惠券模板布隆过滤器服务] 应用启动后异步初始化完成，处理了 {} 个优惠券模板", ids.size());
        } catch (Exception e) {
            log.error("[优惠券模板布隆过滤器服务] 应用启动后异步初始化失败", e);
        }
    }


}
