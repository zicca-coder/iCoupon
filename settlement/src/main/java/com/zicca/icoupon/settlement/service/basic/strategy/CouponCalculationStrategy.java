package com.zicca.icoupon.settlement.service.basic.strategy;

import com.zicca.icoupon.settlement.dto.resp.UserCouponQueryRespDTO;
import com.zicca.icoupon.settlement.service.basic.calculation.CalculateSingleParam;
import com.zicca.icoupon.settlement.service.basic.calculation.PricePair;

/**
 * 优惠券计算策略
 *
 * @author zicca
 */
public interface CouponCalculationStrategy {

    /**
     * 计算
     *
     * @param param  计算参数
     * @param coupon 优惠券
     * @return 优惠金额
     */
    PricePair calculate(CalculateSingleParam param, UserCouponQueryRespDTO coupon);

}
