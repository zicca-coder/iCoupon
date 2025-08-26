package com.zicca.icoupon.admin.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zicca.icoupon.admin.merchant.dao.entity.CouponTemplateGoods;

import java.util.List;

/**
 * 优惠券模板商品关联表 服务类
 *
 * @author zicca
 */
public interface CouponTemplateGoodsService extends IService<CouponTemplateGoods> {


    /**
     * 为优惠券模板批量关联商品
     *
     * @param couponTemplateId 优惠券模板 ID
     * @param goodsIds         商品 IDs
     */
    void batchAssociateGoods(Long couponTemplateId, List<Long> goodsIds);

    /**
     * 为优惠券模板批量解除商品关联
     *
     * @param couponTemplateId 优惠券模板 ID
     * @param goodsIds         商品 IDs
     */
    void batchDissociateGoods(Long couponTemplateId);


}
