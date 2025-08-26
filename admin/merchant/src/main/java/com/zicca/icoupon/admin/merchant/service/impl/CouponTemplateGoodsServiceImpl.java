package com.zicca.icoupon.admin.merchant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zicca.icoupon.admin.merchant.common.context.UserContext;
import com.zicca.icoupon.admin.merchant.dao.entity.CouponTemplateGoods;
import com.zicca.icoupon.admin.merchant.dao.mapper.CouponTemplateGoodsMapper;
import com.zicca.icoupon.admin.merchant.service.CouponTemplateGoodsService;
import com.zicca.icoupon.framework.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 优惠券模板商品服务实现类
 *
 * @author zicca
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CouponTemplateGoodsServiceImpl extends ServiceImpl<CouponTemplateGoodsMapper, CouponTemplateGoods> implements CouponTemplateGoodsService {


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchAssociateGoods(Long couponTemplateId, List<Long> goodsIds) {
        if (CollectionUtils.isEmpty(goodsIds)) {
            return;
        }
        List<CouponTemplateGoods> records = goodsIds.stream().map(goodsId -> CouponTemplateGoods.builder()
                .couponTemplateId(couponTemplateId)
                .shopId(UserContext.getShopId())
                .goodsId(goodsId)
                .build()).toList();
        boolean success = saveBatch(records);
        if (!success) {
            throw new ServiceException("批量插入优惠券商品关联关系失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchDissociateGoods(Long couponTemplateId) {
        try {
            boolean deleted = remove(new LambdaQueryWrapper<CouponTemplateGoods>()
                    .eq(CouponTemplateGoods::getCouponTemplateId, couponTemplateId)
                    .eq(CouponTemplateGoods::getShopId, UserContext.getShopId()));
        } catch (Exception e) {
            throw new ServiceException("批量解除优惠券商品关联关系失败");
        }
    }


}
