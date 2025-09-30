package com.zicca.icoupon.coupon.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zicca.icoupon.coupon.dao.entity.CouponTemplateGoods;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 优惠券模板商品关联表 Mapper 接口
 *
 * @author zicca
 */
public interface CouponTemplateGoodsMapper extends BaseMapper<CouponTemplateGoods> {

    /**
     * 添加优惠券模板商品关联记录
     *
     * @param couponTemplateId 优惠券模板 ID
     * @param goodsId          商品 ID
     * @param shopId           店铺 ID
     * @return 影响行数
     */
    int insertCouponGoods(@Param("couponTemplateId") Long couponTemplateId, @Param("goodsId") Long goodsId, @Param("shopId") Long shopId);


    /**
     * 批量添加优惠券模板商品关联记录
     *
     * @param couponTemplateGoods 优惠券模板商品关联记录
     * @return 影响行数
     */
    int batchInsertCouponGoods(@Param("couponTemplateGoodsList") List<CouponTemplateGoods> couponTemplateGoodsList);


    /**
     * 删除优惠券模板商品关联记录
     *
     * @param couponTemplateId 优惠券模板 ID
     * @param goodsId          商品 ID
     * @param shopId           店铺 ID
     * @return
     */
    int deleteCouponGoods(@Param("couponTemplateId") Long couponTemplateId, @Param("goodsId") Long goodsId, @Param("shopId") Long shopId);


}