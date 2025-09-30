package com.zicca.icoupon.coupon.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zicca.icoupon.coupon.dao.entity.CouponTemplate;
import com.zicca.icoupon.coupon.dao.mapper.CouponTemplateMapper;
import com.zicca.icoupon.coupon.dto.req.*;
import com.zicca.icoupon.coupon.dto.resp.CouponTemplateQueryRespDTO;
import com.zicca.icoupon.coupon.service.CouponTemplateGoodsService;
import com.zicca.icoupon.coupon.service.CouponTemplateService;
import com.zicca.icoupon.coupon.service.basics.cache.CouponTemplateBloomFilterService;
import com.zicca.icoupon.coupon.service.basics.cache.CouponTemplateCaffeineService;
import com.zicca.icoupon.coupon.service.basics.cache.CouponTemplateRedisService;
import com.zicca.icoupon.framework.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.zicca.icoupon.coupon.common.constants.RedisConstant.COUPON_TEMPLATE_LOCK_KEY;
import static com.zicca.icoupon.coupon.common.enums.CouponTemplateStatusEnum.*;
import static com.zicca.icoupon.coupon.common.enums.DiscountTargetEnum.PARTIAL;

/**
 * 优惠券模板服务实现类
 *
 * @author zicca
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CouponTemplateServiceImpl extends ServiceImpl<CouponTemplateMapper, CouponTemplate> implements CouponTemplateService {

    private final CouponTemplateMapper couponTemplateMapper;
    private final CouponTemplateGoodsService couponTemplateGoodsService;
    private final CouponTemplateRedisService redisService;
    private final CouponTemplateCaffeineService caffeineService;
    private final CouponTemplateBloomFilterService bloomFilterService;
    private final RedissonClient redissonClient;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long createCouponTemplate(CouponTemplateCreateReqDTO requestParam) {
        CouponTemplate insertParam = BeanUtil.copyProperties(requestParam, CouponTemplate.class);
        int insert = couponTemplateMapper.insert(insertParam);
        if (insert <= 0) {
            log.error("[优惠券模板服务] 创建优惠券模板失败: insert={}, requestParam={}", insert, requestParam);
            throw new ServiceException("创建优惠券模板失败");
        }
        if (PARTIAL == insertParam.getTarget()) {
            log.info("[优惠券模板服务] 批量关联优惠券模板商品关联信息 - couponTmplateId：{}，goodsIds：{}", insertParam.getId(), requestParam.getGoodIds());
            couponTemplateGoodsService.batchAssociateGoods(insertParam.getId(), requestParam.getShopId(), requestParam.getGoodIds());
        }
        Long id = insertParam.getId();
        // 加入Redis缓存
        CouponTemplateQueryRespDTO cacheBean = BeanUtil.toBean(insertParam, CouponTemplateQueryRespDTO.class);
        cacheBean.setGoodIds(requestParam.getGoodIds());
        redisService.put(id, cacheBean);
        // 布隆过滤器 本地/reids
        bloomFilterService.add(id);
        log.info("[优惠券模板服务] 添加优惠券模板ID至布隆过滤器 - 添加成功: id={}", id);
        log.info("[优惠券模板服务] 创建优惠券模板成功: id={}, requestParam={}", id, requestParam);
        return id;
    }

    @Override
    public void increaseNumberCouponTemplate(CouponTemplateNumberReqDTO requestParam) {
        if (requestParam == null || requestParam.getCouponTemplateId() == null || requestParam.getShopId() == null || requestParam.getNumber() == null) {
            log.warn("[优惠券模板服务] 增加优惠券模板库存参数不完整: requestParam={}", requestParam);
        }
        int update = couponTemplateMapper.increaseNumberCouponTemplate(requestParam.getCouponTemplateId(), requestParam.getShopId(), requestParam.getNumber());
        if (update <= 0) {
            log.error("[优惠券模板服务] 增加优惠券模板库存失败: update={}, requestParam={}", update, requestParam);
        }
        // 领券扣减库存时仅从分布式缓存扣减，因此这里不修改本地缓存库存，本地缓存仅展示优惠券模板基本信息
        redisService.incrStock(requestParam.getCouponTemplateId(), requestParam.getNumber());
        log.info("[优惠券模板服务] 增加优惠券模板库存成功: update={}, requestParam={}", update, requestParam);
    }

    @Override
    public void decreaseNumberCouponTemplate(CouponTemplateNumberReqDTO requestParam) {
        if (requestParam == null || requestParam.getCouponTemplateId() == null || requestParam.getShopId() == null || requestParam.getNumber() == null) {
            log.warn("[优惠券模板服务] 减少优惠券模板库存参数不完整: requestParam={}", requestParam);
        }
        int update = couponTemplateMapper.decreaseNumberCouponTemplate(requestParam.getCouponTemplateId(), requestParam.getShopId(), requestParam.getNumber());
        if (update <= 0) {
            log.error("[优惠券模板服务] 减少优惠券模板库存失败: update={}, requestParam={}", update, requestParam);
        }
        // 领券扣减库存时仅从分布式缓存扣减，因此这里不修改本地缓存库存，本地缓存仅展示优惠券模板基本信息
        redisService.decrStock(requestParam.getCouponTemplateId(), requestParam.getNumber());
        log.info("[优惠券模板服务] 减少优惠券模板库存成功: update={}, requestParam={}", update, requestParam);
    }

    @Override
    public CouponTemplateQueryRespDTO getCouponTemplate(Long id, Long shopId) {
        if (id == null || shopId == null) {
            log.warn("[优惠券模板服务] 查询优惠券模板参数不完整: id={}, shopId={}", id, shopId);
            return null;
        }
        CouponTemplateQueryRespDTO cacheValue = null;
        // 从本地缓存获取
        if ((cacheValue = caffeineService.get(id)) != null) {
            if (caffeineService.isNullCache(cacheValue)) {
                log.warn("[优惠券模板服务] 获取优惠券模板信息 - 本地缓存空值: id={}, shopId={}", id, shopId);
                return null;
            }
            log.info("[优惠券模板服务] 获取优惠券模板信息 - 从本地缓存中获取: id={}, shopId={}", id, shopId);
            return cacheValue;
        }
        // 本地布隆过滤器
        if (bloomFilterService.isNotExistInGuava(id)) {
            log.warn("[优惠券模板服务] 获取优惠券模板信息 - 本地布隆过滤器决策，不存在: id={}, shopId={}", id, shopId);
            caffeineService.putNullToCache(id);
            return null;
        }
        // 从分布式缓存获取
        if ((cacheValue = redisService.get(id)) != null) {
            if (redisService.isNullCache(cacheValue)) {
                log.warn("[优惠券模板服务] 获取优惠券模板信息 - 分布式缓存空值: id={}, shopId={}", id, shopId);
                caffeineService.putNullToCache(id);
                return null;
            }
            log.info("[优惠券模板服务] 获取优惠券模板信息 - 从分布式缓存中获取: id={}, shopId={}", id, shopId);
            caffeineService.put(id, cacheValue);
            return cacheValue;
        }
        // 分布式布隆过滤器
        if (bloomFilterService.isNotExistInRedis(id)) {
            log.warn("[优惠券模板服务] 获取优惠券模板信息 - 分布式布隆过滤器决策，不存在: id={}, shopId={}", id, shopId);
            redisService.putNullToCache(id);
            caffeineService.putNullToCache(id);
            return null;
        }
        // 重建缓存
        RLock lock = redissonClient.getLock(COUPON_TEMPLATE_LOCK_KEY + id);
        try {
            if (lock.tryLock(10, TimeUnit.SECONDS)) {
                log.info("[优惠券模板服务] 获取优惠券模板信息，重建缓存 - 线程:{}, 获取锁成功", Thread.currentThread().getName());
                // 双重判定，是否有其他线程在当前线程等待获取锁期间重建了缓存
                if ((cacheValue = caffeineService.get(id)) != null) {
                    if (caffeineService.isNullCache(cacheValue)) {
                        log.warn("[优惠券模板服务] 获取优惠券模板信息 - 本地缓存空值: id={}, shopId={}", id, shopId);
                        return null;
                    }
                    log.info("[优惠券模板服务] 获取优惠券模板信息 - 从本地缓存中获取: id={}, shopId={}", id, shopId);
                    return cacheValue;
                }
                if ((cacheValue = redisService.get(id)) != null) {
                    if (redisService.isNullCache(cacheValue)) {
                        log.warn("[优惠券模板服务] 获取优惠券模板信息 - 分布式缓存空值: id={}, shopId={}", id, shopId);
                        caffeineService.putNullToCache(id);
                        return null;
                    }
                    log.info("[优惠券模板服务] 获取优惠券模板信息 - 从分布式缓存中获取: id={}, shopId={}", id, shopId);
                    caffeineService.put(id, cacheValue);
                    return cacheValue;
                }
                // 查询数据库
                CouponTemplate couponTemplate = couponTemplateMapper.selectCouponTemplateById(id, shopId);
                if (Objects.isNull(couponTemplate)) {
                    log.warn("[优惠券模板服务] 获取优惠券模板信息 - 数据库不存在: id={}, shopId={}", id, shopId);
                    redisService.putNullToCache(id);
                    caffeineService.putNullToCache(id);
                }
                // 这里需要额外查询商品关联信息，暂时省略
                cacheValue = BeanUtil.copyProperties(couponTemplate, CouponTemplateQueryRespDTO.class);
                caffeineService.put(id, cacheValue);
                redisService.put(id, cacheValue);
                log.info("[优惠券模板服务] 获取优惠券模板信息 - 缓存重建成功: id={}, shopId={}", id, shopId);
                return cacheValue;
            } else {
                log.warn("[优惠券模板服务] 获取优惠券模板信息，重建缓存 - 线程:{}, 获取锁失败", Thread.currentThread().getName());
                return null;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("[优惠券模板服务] 获取优惠券模板信息，重建缓存 - 线程:{}, 获取锁时被中断", Thread.currentThread().getName());
            throw new ServiceException("获取分布式锁被中断");
        } catch (Exception e) {
            log.error("[优惠券模板服务] 获取优惠券模板信息异常");
            throw new ServiceException("获取优惠券模板信息异常");
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.info("[优惠券模板服务] 获取优惠券模板信息，重建缓存 - 线程:{}, 释放锁成功", Thread.currentThread().getName());
            }
        }
    }

    @Override
    public List<CouponTemplate> listCouponTemplateByIds(List<Long> couponTemplateIds, List<Long> shopIds) {
        if (couponTemplateIds == null || couponTemplateIds.isEmpty() || shopIds == null || shopIds.isEmpty()) {
            log.warn("[优惠券模板服务] 查询优惠券模板参数不完整: couponTemplateIds={}, shopIds={}", couponTemplateIds, shopIds);
            return List.of();
        }
        return couponTemplateMapper.selectCouponTemplateByIds(couponTemplateIds, shopIds);
    }

    @Override
    public Boolean isSupportGoods(SupportedGoodsReqDTO requestParam) {
        // 默认实现
        return Boolean.TRUE;
    }

    @Override
    public List<CouponTemplateQueryRespDTO> listCouponTemplateByShopId(Long shopId) {
        try {
            if (Objects.isNull(shopId)) {
                log.warn("[优惠券模板服务] 查询优惠券模板列表 - 店铺ID为空，shopId = {}", shopId);
                return List.of();
            }
            List<CouponTemplate> couponTemplates = couponTemplateMapper.selectCouponTemplateListByShopId(shopId);
            if (CollUtil.isEmpty(couponTemplates)) {
                log.info("[优惠券模板服务] 获取优惠券模板列表 - 优惠券模板列表为空，shopId = {}", shopId);
                return List.of();
            }
            return couponTemplates.stream()
                    .map(couponTemplate -> BeanUtil.copyProperties(couponTemplate, CouponTemplateQueryRespDTO.class))
                    .toList();
        } catch (Exception e) {
            log.error("[优惠券模板服务] 获取优惠券模板列表 - 获取优惠券模板列表失败，shopId = {}", shopId, e);
            throw new ServiceException("获取优惠券模板列表失败");
        }
    }

    @Override
    public List<CouponTemplateQueryRespDTO> listCouponTemplateByStatus(CouponTemplateStatusQueryReqDTO requestParam) {
        try {
            if (ObjectUtil.isNull(requestParam)) {
                log.warn("[优惠券模板服务] 获取特定状态优惠券模板列表 - 请求参数为空，requestParam = {}", requestParam);
                return List.of();
            }
            List<CouponTemplate> couponTemplates = couponTemplateMapper.selectCouponTemplateListByShopIdAndStatus(requestParam.getShopId(), requestParam.getStatus());
            if (CollUtil.isEmpty(couponTemplates)) {
                log.info("[优惠券模板服务] 获取特定状态优惠券模板列表 - 优惠券模板列表为空，status = {}, shopId = {}", requestParam.getStatus(), requestParam.getShopId());
                return List.of();
            }
            return couponTemplates.stream()
                    .map(couponTemplate -> BeanUtil.copyProperties(couponTemplate, CouponTemplateQueryRespDTO.class))
                    .toList();
        } catch (Exception e) {
            log.error("[优惠券模板服务] 获取特定状态优惠券模板列表 - 获取特定状态优惠券模板列表失败，requestParam = {}", requestParam, e);
            throw new ServiceException("获取特定状态优惠券模板列表失败");
        }
    }

    @Override
    public List<CouponTemplateQueryRespDTO> listCouponTemplateByType(CouponTemplateTypeQueryReqDTO requestParam) {
        try {
            if (ObjectUtil.isNull(requestParam)) {
                log.warn("[优惠券模板服务] 获取特定类型优惠券模板列表 - 请求参数为空，requestParam = {}", requestParam);
                return List.of();
            }
            List<CouponTemplate> couponTemplates = couponTemplateMapper.selectCouponTemplateListByShopIdAndType(requestParam.getShopId(), requestParam.getType());
            if (CollUtil.isEmpty(couponTemplates)) {
                log.info("[优惠券模板服务] 获取特定类型优惠券模板列表 - 优惠券模板列表为空，type = {}, shopId = {}", requestParam.getType(), requestParam.getShopId());
                return List.of();
            }
            return couponTemplates.stream()
                    .map(couponTemplate -> BeanUtil.copyProperties(couponTemplate, CouponTemplateQueryRespDTO.class))
                    .toList();
        } catch (Exception e) {
            log.error("[优惠券模板服务] 获取特定类型优惠券模板列表 - 获取特定类型优惠券模板列表失败，requestParam = {}", requestParam, e);
            throw new ServiceException("获取特定类型优惠券模板列表失败");
        }
    }

    @Override
    public List<CouponTemplateQueryRespDTO> listNotStartCouponTemplate(Long shopId) {
        try {
            if (Objects.isNull(shopId)) {
                log.warn("[优惠券模板服务] 获取未开始优惠券模板列表 - 店铺ID为空，shopId = {}", shopId);
                return List.of();
            }
            List<CouponTemplate> couponTemplates = couponTemplateMapper.selectCouponTemplateListByShopIdAndStatus(shopId, NOT_START);
            if (CollUtil.isEmpty(couponTemplates)) {
                log.info("[优惠券模板服务] 获取未开始优惠券模板列表 - 优惠券模板列表为空，shopId = {}", shopId);
                return List.of();
            }
            return couponTemplates.stream().map(couponTemplate -> BeanUtil.copyProperties(couponTemplate, CouponTemplateQueryRespDTO.class)).toList();
        } catch (Exception e) {
            log.error("[优惠券模板服务] 获取未开始优惠券模板列表 - 获取未开始优惠券模板列表失败，shopId = {}", shopId, e);
            throw new ServiceException("获取未开始优惠券模板列表失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteCouponTemplate(Long id, Long shopId) {
        if (Objects.isNull(id) || Objects.isNull(shopId)) {
            log.warn("[优惠券模板服务] 删除优惠券模板 - 优惠券模板ID或店铺ID为空，id = {}, shopId = {}", id, shopId);
            throw new ServiceException("优惠券模板ID或店铺ID为空");
        }
        int i = couponTemplateMapper.deleteCouponTemplateByIdAndShopId(id, shopId);
        if (i <= 0) {
            log.warn("[优惠券模板服务] 删除优惠券模板 - 删除优惠券模板失败，id = {}, shopId = {}", id, shopId);
            throw new ServiceException("删除优惠券模板失败");
        }
        log.info("[优惠券模板服务] 批量接触优惠券模板商品关联信息，id = {}, shopId = {}", id, shopId);
        couponTemplateGoodsService.batchDissociateGoods(id, shopId);
        log.info("[优惠券模板服务] 删除优惠券模板成功，id = {}, shopId = {}", id, shopId);

        // 删除缓存 本地/分布式
        caffeineService.delete(id);
        redisService.delete(id);

    }

    @Override
    public void activeCouponTemplate(Long id, Long shopId) {
        if (Objects.isNull(id) || Objects.isNull(shopId)) {
            log.warn("[优惠券模板服务] 激活优惠券模板 - 优惠券模板ID或店铺ID为空，id = {}, shopId = {}", id, shopId);
            throw new ServiceException("优惠券模板ID或店铺ID为空");
        }
        // 横向越权校验

        // 更新优惠券模板状态为进行中
        int i = couponTemplateMapper.updateCouponTemplateStatusByIdAndShopId(id, shopId, IN_PROGRESS);
        if (i <= 0) {
            log.error("[优惠券模板服务] 激活优惠券模板 - 激活优惠券模板失败，id = {}, shopId = {}", id, shopId);
        }
        // 更新缓存中优惠券模板状态
        CouponTemplateQueryRespDTO value = redisService.updateStatus(id, IN_PROGRESS);
        // 加入优惠券模板到本地缓存中
        caffeineService.put(id, value);
        log.info("[优惠券模板服务] 激活优惠券模板成功，id = {}, shopId = {}", id, shopId);
    }

    @Override
    public void terminateCouponTemplate(Long id, Long shopId) {
        if (Objects.isNull(id) || Objects.isNull(shopId)) {
            log.warn("[优惠券模板服务] 终止优惠券模板 - 优惠券模板ID或店铺ID为空，id = {}, shopId = {}", id, shopId);
            throw new ServiceException("优惠券模板ID或店铺ID为空");
        }
        // 横向越权校验

        int i = couponTemplateMapper.updateCouponTemplateStatusByIdAndShopId(id, shopId, END);
        if (i <= 0) {
            log.error("[优惠券模板服务] 终止优惠券模板 - 终止优惠券模板失败，id = {}, shopId = {}", id, shopId);
        }
        CouponTemplateQueryRespDTO value = redisService.updateStatus(id, END);
        // 终止的优惠券模板从本地缓存中删除
        caffeineService.delete(id);
        log.info("[优惠券模板服务] 终止优惠券模板成功，id = {}, shopId = {}", id, shopId);
    }

    @Override
    public void cancelCouponTemplate(Long id, Long shopId) {
        if (Objects.isNull(id) || Objects.isNull(shopId)) {
            log.warn("[优惠券模板服务] 取消优惠券模板 - 优惠券模板ID或店铺ID为空，id = {}, shopId = {}", id, shopId);
            throw new ServiceException("优惠券模板ID或店铺ID为空");
        }
        int i = couponTemplateMapper.updateCouponTemplateStatusByIdAndShopId(id, shopId, CANCELED);
        if (i <= 0) {
            log.error("[优惠券模板服务] 取消优惠券模板 - 取消优惠券模板失败，id = {}, shopId = {}", id, shopId);
        }
        // 取消后的优惠券对用户不可见，直接从redis中删除
        redisService.delete(id);
        // 删除本地缓存中优惠券模板信息
        caffeineService.delete(id);
        log.info("[优惠券模板服务] 取消优惠券模板成功，id = {}, shopId = {}", id, shopId);
    }
}
