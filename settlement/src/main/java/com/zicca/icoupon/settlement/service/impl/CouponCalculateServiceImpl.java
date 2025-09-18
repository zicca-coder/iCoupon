package com.zicca.icoupon.settlement.service.impl;

import com.zicca.icoupon.settlement.dto.resp.UserCouponQueryRespDTO;
import com.zicca.icoupon.settlement.service.CouponCalculateService;
import com.zicca.icoupon.settlement.service.CouponTemplateService;
import com.zicca.icoupon.settlement.service.UserCouponService;
import com.zicca.icoupon.settlement.service.basic.calculation.CouponCalculatePair;
import com.zicca.icoupon.settlement.service.basic.calculation.PricePair;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Random;

import static com.zicca.icoupon.settlement.common.enums.DiscountTargetEnum.PARTIAL;
import static com.zicca.icoupon.settlement.common.enums.UserCouponStatusEnum.NOT_USED;

/**
 * 优惠券计算服务实现类
 *
 * @author zicca
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CouponCalculateServiceImpl implements CouponCalculateService {

    private final UserCouponService userCouponService;
    private final CouponTemplateService couponTemplateService;

    @Override
    public PricePair calculateAmount(CouponCalculatePair requestParam) {
        log.info("计算优惠券后金额开始，参数: {}", requestParam);
        // 查询用户优惠券信息
        UserCouponQueryRespDTO coupon = userCouponService.getUserCouponById(requestParam.getUserCouponId(), requestParam.getUserId());
        log.info("查询用户优惠券信息：{}", coupon);
        // 优惠券不存在
        if (coupon == null) {
            log.warn("[用户优惠券服务] 优惠券不存在");
            return calculateWithNoCoupon(requestParam);
        }
        // 优惠券状态不是未使用
        if (coupon.getStatus() != NOT_USED) {
            log.warn("[用户优惠券服务] 优惠券状态不是未使用");
            return calculateWithNoCoupon(requestParam);
        }
        // 如果优惠券是指定商品可用，则需要判断商品是否在优惠券内
        if (PARTIAL == coupon.getTarget() && !couponTemplateService.isSupportGoods(coupon.getCouponTemplateId(), coupon.getShopId(), requestParam.getGoodsId())) {
            log.warn("[用户优惠券服务] 商品不在优惠券内");
            return calculateWithNoCoupon(requestParam);
        }
        // 根据优惠券类型计算金额
        switch (coupon.getType()) {
            case LITTLE_REDUCTION -> {
                return calculateWithLittleReduction(requestParam, coupon);
            }
            case DISCOUNT -> {
                return calculateWithDiscount(requestParam, coupon);
            }
            case FULL_REDUCTION -> {
                return calculateWithFullReduction(requestParam, coupon);
            }
            case RANDOM -> {
                return calculateWithRandom(requestParam, coupon);
            }
            default -> {
                return calculateWithNoCoupon(requestParam);
            }
        }
    }


    /**
     * 优惠券计算 | 立减券
     *
     * @param requestParam 优惠券计算参数
     * @param coupon       优惠券信息
     * @return 优惠券计算结果
     */
    private PricePair calculateWithLittleReduction(CouponCalculatePair requestParam, UserCouponQueryRespDTO coupon) {
        log.info("优惠券计算 | 立减券");
        // 计算订单总金额（单价 × 数量）
        BigDecimal totalPrice = requestParam.getPrice().multiply(BigDecimal.valueOf(requestParam.getQuantity()));

        // 立减后的金额 = 订单总金额 - 优惠券面值
        BigDecimal discountedPrice = totalPrice.subtract(coupon.getFaceValue());

        // 确保最终价格不低于0
        if (discountedPrice.compareTo(BigDecimal.ZERO) < 0) {
            discountedPrice = BigDecimal.ZERO;
        }

        log.info("使用立减券，优惠金额：{}，优惠后金额：{}", coupon.getFaceValue(), discountedPrice);
        return new PricePair(totalPrice, discountedPrice, totalPrice.subtract(discountedPrice));
    }


    /**
     * 优惠券计算 | 折扣券
     *
     * @param requestParam 优惠券计算参数
     * @param coupon       优惠券
     * @return 优惠券计算结果
     */
    private PricePair calculateWithDiscount(CouponCalculatePair requestParam, UserCouponQueryRespDTO coupon) {
        log.info("优惠券计算 | 折扣券");
        // 计算订单总金额（单价 × 数量）
        BigDecimal totalPrice = requestParam.getPrice().multiply(BigDecimal.valueOf(requestParam.getQuantity()));

        // 折扣后的金额 = 订单总金额 × 折扣率
        BigDecimal discountedPrice = totalPrice.multiply(coupon.getFaceValue());

        log.info("使用折扣券，优惠金额：{}，优惠后价格：{}", totalPrice.subtract(discountedPrice), discountedPrice);
        return new PricePair(totalPrice, discountedPrice);
    }


    /**
     * 优惠券计算 | 满减券
     *
     * @param requestParam 优惠券计算参数
     * @param coupon       优惠券
     * @return 优惠券计算结果
     */
    private PricePair calculateWithFullReduction(CouponCalculatePair requestParam, UserCouponQueryRespDTO coupon) {
        log.info("优惠券计算 | 满减券");
        // 计算订单总金额（单价 × 数量）
        BigDecimal totalPrice = requestParam.getPrice().multiply(BigDecimal.valueOf(requestParam.getQuantity()));
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
        return calculateWithNoCoupon(requestParam);
    }


    /**
     * 优惠券计算 | 折扣券
     *
     * @param requestParam 优惠券计算参数
     * @param coupon       优惠券信息
     * @return 优惠券计算结果
     */
    private PricePair calculateWithRandom(CouponCalculatePair requestParam, UserCouponQueryRespDTO coupon) {
        log.info("优惠券计算 | 折扣券");
        // 计算订单总金额（单价 × 数量）
        BigDecimal totalPrice = requestParam.getPrice().multiply(BigDecimal.valueOf(requestParam.getQuantity()));
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


    /**
     * 优惠券计算 | 无优惠券
     *
     * @param requestParam 优惠券计算参数
     * @return 优惠券计算结果
     */
    private PricePair calculateWithNoCoupon(CouponCalculatePair requestParam) {
        log.info("优惠券计算 | 无优惠券");
        return PricePair.noDiscount(requestParam.getPrice().multiply(BigDecimal.valueOf(requestParam.getQuantity())));
    }


}
