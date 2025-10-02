package com.zicca.icoupon.coupon.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zicca.icoupon.coupon.common.enums.CouponTemplateStatusEnum;
import com.zicca.icoupon.coupon.common.enums.DiscountTypeEnum;
import com.zicca.icoupon.coupon.dao.entity.CouponTemplate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 优惠券模板 Mapper
 *
 * @author zicca
 */
public interface CouponTemplateMapper extends BaseMapper<CouponTemplate> {

    /**
     * 插入优惠券模板
     *
     * @param couponTemplate 优惠券模板
     * @return 插入数量
     */
    int insertCouponTemplate(@Param("couponTemplate") CouponTemplate couponTemplate);

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
     * 根据优惠券模板 id 获取库存数量
     *
     * @param id     id
     * @param shopId 店铺 id
     * @return 库存数量
     */
    Integer selectStockById(@Param("id") Long id, @Param("shopId") Long shopId);

    /**
     * 减少优惠券模板库存
     *
     * @param couponTemplateId 优惠券模板 id
     * @param shopId           店铺 id
     * @param number           减少的数量
     * @return 库存
     */
    Integer decreaseNumberCouponTemplateWithResult(@Param("couponTemplateId") Long couponTemplateId,
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

    /**
     * 根据优惠券模板 id 列表获取优惠券模板
     *
     * @param couponTemplateIds 优惠券模板 id 列表
     * @param shopIds           店铺 id 列表
     * @return 优惠券模板列表
     */
    List<CouponTemplate> selectCouponTemplateByIds(@Param("couponTemplateIds") List<Long> couponTemplateIds,
                                                   @Param("shopIds") List<Long> shopIds);


    /**
     * 根据店铺 id 获取优惠券模板列表
     *
     * @param shopId 店铺 id
     * @return 优惠券模板列表
     */
    List<CouponTemplate> selectCouponTemplateListByShopId(@Param("shopId") Long shopId);

    /**
     * 根据店铺 id 和优惠券模板状态获取优惠券模板列表
     *
     * @param shopId 店铺 id
     * @param status 优惠券模板状态
     * @return 优惠券模板列表
     */
    List<CouponTemplate> selectCouponTemplateListByShopIdAndStatus(@Param("shopId") Long shopId, @Param("status") CouponTemplateStatusEnum status);

    /**
     * 根据店铺 id 和优惠券模板类型获取优惠券模板列表
     *
     * @param shopId 店铺 id
     * @param type   优惠券模板类型
     * @return 优惠券模板列表
     */
    List<CouponTemplate> selectCouponTemplateListByShopIdAndType(@Param("shopId") Long shopId, @Param("type") DiscountTypeEnum type);


    /**
     * 拉取优惠券模板ID列表
     * <p>只拉取有效的ID：未删除、未开始/进行中、未过期的优惠券模板</p>
     *
     * @return 优惠券模板ID列表
     */
    List<Long> fetchCouponTemplateIds();


    /**
     * 删除优惠券模板
     *
     * @param id     优惠券模板ID
     * @param shopId 店铺ID
     * @return 删除数量
     */
    int deleteCouponTemplateByIdAndShopId(@Param("id") Long id, @Param("shopId") Long shopId);

    /**
     * 更新优惠券模板状态
     *
     * @param id     优惠券模板ID
     * @param shopId 店铺ID
     * @param status 优惠券模板状态
     * @return 更新数量
     */
    int updateCouponTemplateStatusByIdAndShopId(@Param("id") Long id, @Param("shopId") Long shopId, @Param("status") CouponTemplateStatusEnum status);


}
