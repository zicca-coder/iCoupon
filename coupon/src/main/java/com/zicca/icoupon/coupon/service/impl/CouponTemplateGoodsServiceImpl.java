package com.zicca.icoupon.coupon.service.impl;

import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zicca.icoupon.coupon.dao.entity.CouponTemplateGoods;
import com.zicca.icoupon.coupon.dao.mapper.CouponTemplateGoodsMapper;
import com.zicca.icoupon.coupon.service.CouponTemplateGoodsService;
import com.zicca.icoupon.framework.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 优惠券模板商品关联表 服务实现类
 *
 * @author zicca
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CouponTemplateGoodsServiceImpl extends ServiceImpl<CouponTemplateGoodsMapper, CouponTemplateGoods> implements CouponTemplateGoodsService {

    private final CouponTemplateGoodsMapper couponTemplateGoodsMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchAssociateGoods(Long couponTemplateId, Long shopId, List<Long> goodsIds) {
        if (couponTemplateId == null || shopId == null) {
            throw new IllegalArgumentException("优惠券模板ID和店铺ID不能为空");
        }
        if (CollectionUtils.isEmpty(goodsIds)) {
            log.warn("商品列表为空");
            return;
        }
        // 去重处理
        List<Long> distinctGoodsIds = goodsIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        if (distinctGoodsIds.isEmpty()) {
            return;
        }
        List<CouponTemplateGoods> records = goodsIds.stream()
                .map(goodsId -> CouponTemplateGoods.builder()
                .couponTemplateId(couponTemplateId)
                .shopId(shopId)
                .goodsId(goodsId)
                .build()).toList();
        int insertedCount = couponTemplateGoodsMapper.batchInsertCouponGoods(records);
        if (insertedCount != records.size()) {
            throw new ServiceException("批量插入优惠券商品关联关系失败，期望插入" + records.size() + "条，实际插入" + insertedCount + "条");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchDissociateGoods(Long couponTemplateId, Long shopId) {
        // 参数校验
        if (couponTemplateId == null || shopId == null) {
            throw new IllegalArgumentException("优惠券模板ID和店铺ID不能为空");
        }

        // 执行删除操作
        int deletedCount = couponTemplateGoodsMapper.deleteCouponGoods(couponTemplateId, null, shopId);

        // 检查删除结果
        if (deletedCount < 0) {
            throw new ServiceException("批量解除优惠券商品关联关系失败，受影响行数: " + deletedCount);
        }

        // 可选：添加操作日志
        log.info("成功解除优惠券模板{}与店铺{}的商品关联关系，共删除{}条记录",
                couponTemplateId, shopId, deletedCount);
    }
}
