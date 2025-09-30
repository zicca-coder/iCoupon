package com.zicca.icoupon.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zicca.icoupon.coupon.common.enums.DiscountTypeEnum;
import com.zicca.icoupon.coupon.dao.entity.CouponTemplate;
import com.zicca.icoupon.coupon.dto.req.*;
import com.zicca.icoupon.coupon.dto.resp.CouponTemplateQueryRespDTO;

import java.util.List;

/**
 * 优惠券模板服务
 *
 * @author zicca
 */
public interface CouponTemplateService extends IService<CouponTemplate> {

    /**
     * 创建优惠券模版
     *
     * @param requestParam 请求参数
     */
    Long createCouponTemplate(CouponTemplateCreateReqDTO requestParam);

    /**
     * 增加优惠券模版发行量
     *
     * @param requestParam 请求参数
     */
    void increaseNumberCouponTemplate(CouponTemplateNumberReqDTO requestParam);

    /**
     * 减少优惠券模版发行量
     *
     * @param requestParam 请求参数
     */
    void decreaseNumberCouponTemplate(CouponTemplateNumberReqDTO requestParam);

    /**
     * 查询优惠券模板
     *
     * @param id     优惠券模板 id
     * @param shopId 店铺 id
     * @return 优惠券模板
     */
    CouponTemplateQueryRespDTO getCouponTemplate(Long id, Long shopId);

    /**
     * 根据优惠券模板ID集合批量查询优惠券信息
     *
     * @param couponTemplateIds 优惠券ID集合
     * @param shopIds           店铺ID集合
     * @return 优惠券信息
     */
    List<CouponTemplate> listCouponTemplateByIds(List<Long> couponTemplateIds, List<Long> shopIds);


    /**
     * 查询优惠券是否支持该商品
     *
     * @param requestParam 请求参数
     * @return 是否支持
     */
    Boolean isSupportGoods(SupportedGoodsReqDTO requestParam);


    /**
     * 根据店铺ID查询优惠券
     *
     * @param shopId 店铺ID
     * @return 优惠券列表
     */
    List<CouponTemplateQueryRespDTO> listCouponTemplateByShopId(Long shopId);

    /**
     * 根据优惠券状态查询优惠券
     *
     * @param requestParam 请求参数
     * @return 优惠券列表
     */
    List<CouponTemplateQueryRespDTO> listCouponTemplateByStatus(CouponTemplateStatusQueryReqDTO requestParam);

    /**
     * 根据优惠券类型查询优惠券
     *
     * @param requestParam 请求参数
     * @return 优惠券列表
     */
    List<CouponTemplateQueryRespDTO> listCouponTemplateByType(CouponTemplateTypeQueryReqDTO requestParam);

    /**
     * 获取未开始优惠券 | 获取可预约的优惠券
     *
     * @return 优惠券列表
     */
    List<CouponTemplateQueryRespDTO> listNotStartCouponTemplate(Long shopId);

    /**
     * 删除优惠券模版
     *
     * @param id     优惠券模版 id
     * @param shopId 店铺 id
     */
    void deleteCouponTemplate(Long id, Long shopId);



    /**
     * 激活优惠券模板
     *
     * @param couponTemplateId 优惠券模板ID
     * @param shopId           店铺ID
     */
    void activeCouponTemplate(Long couponTemplateId, Long shopId);


    /**
     * 终止优惠券模板
     *
     * @param couponTemplateId 优惠券模板ID
     * @param shopId           店铺ID
     */
    void terminateCouponTemplate(Long couponTemplateId, Long shopId);



    /**
     * 取消优惠券模板
     *
     * @param couponTemplateId 优惠券模板ID
     * @param shopId           店铺ID
     */
    void cancelCouponTemplate(Long couponTemplateId, Long shopId);




}
