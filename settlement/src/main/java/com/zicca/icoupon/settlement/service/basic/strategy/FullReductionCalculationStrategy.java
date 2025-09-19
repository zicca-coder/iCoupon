package com.zicca.icoupon.settlement.service.basic.strategy;

import com.zicca.icoupon.settlement.dto.resp.UserCouponQueryRespDTO;
import com.zicca.icoupon.settlement.service.basic.calculation.CalculateSingleParam;
import com.zicca.icoupon.settlement.service.basic.calculation.PricePair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 优惠券计算策略-满减券
 *
 * @author zicca
 */
@Slf4j
@Component("FULL_REDUCTION")
public class FullReductionCalculationStrategy implements CouponCalculationStrategy {
    @Override
    public PricePair calculate(CalculateSingleParam param, UserCouponQueryRespDTO coupon) {
        log.info("优惠券计算 | 满减券");
        // 计算订单总金额（单价 × 数量）
        BigDecimal totalPrice = param.getPrice().multiply(BigDecimal.valueOf(param.getQuantity()));
        // 如果订单金额满足优惠券使用条件
        if (totalPrice.compareTo(coupon.getMinAmount()) >= 0) {
            log.info("使用满减券，优惠金额：{}，优惠后价格：{}", coupon.getFaceValue(), totalPrice.subtract(coupon.getFaceValue()));
            return PricePair.builder()
                    .subtotalAmount(totalPrice)
                    .discountAmount(coupon.getFaceValue())
                    .payAmount(totalPrice.subtract(coupon.getFaceValue()))
                    .build();
        }
        log.info("订单金额不满足优惠券使用条件");
        return PricePair.noDiscount(param.getPrice().multiply(BigDecimal.valueOf(param.getQuantity())));
    }
}
