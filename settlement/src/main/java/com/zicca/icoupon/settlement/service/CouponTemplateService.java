package com.zicca.icoupon.settlement.service;

import java.util.List;

/**
 * 优惠券模板服务
 *
 * @author zicca
 */
public interface CouponTemplateService {

    /**
     * 判断优惠券是否支持商品
     * @param couponTemplateId 优惠券模板 ID
     * @param shopId 店铺 ID
     * @param goodsId 商品 ID
     * @return 是否支持
     */
    Boolean isSupportGoods(Long couponTemplateId, Long shopId, Long goodsId);

    Boolean isSupportGoods(Long couponTemplateId, List<Long> goodsIds);
}
