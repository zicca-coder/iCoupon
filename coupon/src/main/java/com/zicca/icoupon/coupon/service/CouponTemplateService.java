package com.zicca.icoupon.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zicca.icoupon.coupon.dao.entity.CouponTemplate;
import com.zicca.icoupon.coupon.dto.req.SupportedGoodsReqDTO;
import com.zicca.icoupon.coupon.dto.resp.CouponTemplateQueryRespDTO;

import java.util.List;

/**
 * 优惠券模板服务
 *
 * @author zicca
 */
public interface CouponTemplateService extends IService<CouponTemplate> {


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

}
