package com.zicca.icoupon.engine.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zicca.icoupon.engine.dao.entity.CouponTemplate;
import org.apache.ibatis.annotations.Param;

/**
 * 优惠券模板 Mapper
 *
 * @author zicca
 */
public interface CouponTemplateMapper extends BaseMapper<CouponTemplate> {

    /**
     * 增加优惠券模板发行量
     *
     * @param couponTemplateId 优惠券模板 id
     * @param shopId           店铺 id
     * @param number           增加的数量
     */
    int increaseNumberCouponTemplate(@Param("couponTemplateId") Long couponTemplateId,
                                     @Param("shopId") Long shopId,
                                     @Param("number") Integer number);

    /**
     * 减少优惠券模板发行量
     *
     * @param couponTemplateId 优惠券模板 id
     * @param shopId           店铺 id
     * @param number           减少的数量
     */
    int decreaseNumberCouponTemplate(@Param("couponTemplateId") Long couponTemplateId,
                                     @Param("shopId") Long shopId,
                                     @Param("number") Integer number);

    /**
     * 根据优惠券模板 id 获取优惠券模板
     *
     * @param couponTemplateId 优惠券模板 id
     * @param shopId           店铺 id
     * @return 优惠券模板
     */
    CouponTemplate selectCouponTemplateById(@Param("id") Long couponTemplateId,
                                         @Param("shopId") Long shopId);

}
