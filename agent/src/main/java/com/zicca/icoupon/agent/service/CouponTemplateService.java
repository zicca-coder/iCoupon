package com.zicca.icoupon.agent.service;

import com.zicca.icoupon.agent.dto.req.CouponTemplateStatusQueryReqDTO;
import com.zicca.icoupon.agent.dto.req.CouponTemplateTypeQueryReqDTO;
import com.zicca.icoupon.agent.dto.resp.CouponTemplateQueryRespDTO;

import java.util.List;

/**
 * 优惠券模板服务
 *
 * @author zicca
 */
public interface CouponTemplateService {

    /**
     * 查询优惠券模板
     *
     * @param id     优惠券模板 id
     * @param shopId 店铺 id
     * @return 优惠券模板
     */
    CouponTemplateQueryRespDTO getCouponTemplate(Long id, Long shopId);

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
}
