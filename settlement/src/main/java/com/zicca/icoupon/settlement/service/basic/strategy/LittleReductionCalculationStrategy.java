package com.zicca.icoupon.settlement.service.basic.strategy;

import com.zicca.icoupon.settlement.dto.resp.UserCouponQueryRespDTO;
import com.zicca.icoupon.settlement.service.basic.calculation.CalculateSingleParam;
import com.zicca.icoupon.settlement.service.basic.calculation.PricePair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 优惠券计算策略-立减券
 *
 * @author zicca
 */
@Slf4j
@Component("LITTLE_REDUCTION")
public class LittleReductionCalculationStrategy implements CouponCalculationStrategy {
    @Override
    public PricePair calculate(CalculateSingleParam param, UserCouponQueryRespDTO coupon) {
        log.info("优惠券计算 | 立减券");
        // 计算订单总金额（单价 × 数量）
        BigDecimal totalPrice = param.getPrice().multiply(BigDecimal.valueOf(param.getQuantity()));

        // 立减后的金额 = 订单总金额 - 优惠券面值
        BigDecimal discountedPrice = totalPrice.subtract(coupon.getFaceValue());

        // 确保最终价格不低于0
        if (discountedPrice.compareTo(BigDecimal.ZERO) < 0) {
            discountedPrice = BigDecimal.ZERO;
        }

        log.info("使用立减券，优惠金额：{}，优惠后金额：{}", coupon.getFaceValue(), discountedPrice);
        return new PricePair(totalPrice, discountedPrice, totalPrice.subtract(discountedPrice));
    }
}
