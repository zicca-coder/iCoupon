package com.zicca.icoupon.settlement.service;

import com.zicca.icoupon.settlement.service.basic.calculation.CalculateOverlayParam;
import com.zicca.icoupon.settlement.service.basic.calculation.CalculateSingleParam;
import com.zicca.icoupon.settlement.service.basic.calculation.PricePair;

/**
 * 优惠券计算服务
 *
 * @author zicca
 */
public interface CouponCalculateService {

    /**
     * 计算金额 | 单个订单商品项 + 单个优惠券
     *
     * @param param 请求参数
     * @return 结果
     */
    PricePair calculateSingleAmount(CalculateSingleParam param);


    /**
     * 计算金额 | 单个订单商品项 + 多个优惠券
     *
     * @param param 请求参数
     * @return 结果
     */
    PricePair calculateOverlayAmount(CalculateOverlayParam param);
}
