package com.zicca.icoupon.settlement.service.basic.strategy;

import com.zicca.icoupon.settlement.dto.resp.UserCouponQueryRespDTO;
import com.zicca.icoupon.settlement.service.basic.calculation.CalculateSingleParam;
import com.zicca.icoupon.settlement.service.basic.calculation.PricePair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 优惠券计算策略-折扣券
 * @author zicca
 */
@Slf4j
@Component("DISCOUNT")
public class DiscountCalculationStrategy implements CouponCalculationStrategy{
    @Override
    public PricePair calculate(CalculateSingleParam param, UserCouponQueryRespDTO coupon) {
        log.info("优惠券计算 | 折扣券");
        // 计算订单总金额（单价 × 数量）
        BigDecimal totalPrice = param.getPrice().multiply(BigDecimal.valueOf(param.getQuantity()));

        // 折扣后的金额 = 订单总金额 × 折扣率
        BigDecimal discountedPrice = totalPrice.multiply(coupon.getFaceValue());

        log.info("使用折扣券，优惠金额：{}，优惠后价格：{}", totalPrice.subtract(discountedPrice), discountedPrice);
        return new PricePair(totalPrice, discountedPrice);
    }
}
