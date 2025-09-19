package com.zicca.icoupon.settlement.service.basic;

import com.zicca.icoupon.settlement.common.enums.DiscountTypeEnum;
import com.zicca.icoupon.settlement.dto.resp.UserCouponQueryRespDTO;
import com.zicca.icoupon.settlement.service.basic.calculation.CalculateSingleParam;
import com.zicca.icoupon.settlement.service.basic.calculation.PricePair;
import com.zicca.icoupon.settlement.service.basic.strategy.CouponCalculationStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author zicca
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CouponCalculationStrategyFactory implements CouponCalculationStrategy{

    private final Map<String, CouponCalculationStrategy> strategies;

    private CouponCalculationStrategy getStrategy(DiscountTypeEnum type) {
        return strategies.get(type.name());
    }

    @Override
    public PricePair calculate(CalculateSingleParam param, UserCouponQueryRespDTO coupon) {
        CouponCalculationStrategy couponCalculationStrategy = getStrategy(coupon.getType());
        if (couponCalculationStrategy == null) {
            log.info("优惠券计算 | 无优惠券");
            return PricePair.noDiscount(param.getPrice().multiply(BigDecimal.valueOf(param.getQuantity())));
        }
        return couponCalculationStrategy.calculate(param, coupon);
    }
}
