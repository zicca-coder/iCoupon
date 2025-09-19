package com.zicca.icoupon.settlement.service.basic.strategy;

import com.zicca.icoupon.settlement.dto.resp.UserCouponQueryRespDTO;
import com.zicca.icoupon.settlement.service.basic.calculation.CalculateSingleParam;
import com.zicca.icoupon.settlement.service.basic.calculation.PricePair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Random;

/**
 * 优惠券计算策略-随机券
 *
 * @author zicca
 */
@Slf4j
@Component("RANDOM")
public class RandomCalculationStrategy implements CouponCalculationStrategy {
    @Override
    public PricePair calculate(CalculateSingleParam param, UserCouponQueryRespDTO coupon) {
        log.info("优惠券计算 | 折扣券");
        // 计算订单总金额（单价 × 数量）
        BigDecimal totalPrice = param.getPrice().multiply(BigDecimal.valueOf(param.getQuantity()));
        Random random = new Random();
        BigDecimal max = coupon.getFaceValue();
        BigDecimal min = coupon.getMinAmount();
        // 在[min, max)区间内随机生成一个数，用于计算随机优惠金额
        BigDecimal randomAmount = BigDecimal.valueOf(random.nextDouble() * (max.subtract(min).doubleValue()) + min.doubleValue());
        // 减去随机金额
        BigDecimal discountedPrice = totalPrice.subtract(randomAmount);

        if (discountedPrice.compareTo(BigDecimal.ZERO) < 0) {
            discountedPrice = BigDecimal.ZERO;
        }

        log.info("使用随机券，优惠金额：{}，优惠后价格：{}", randomAmount, discountedPrice);
        return new PricePair(totalPrice, discountedPrice, totalPrice.subtract(discountedPrice));
    }
}
