package com.zicca.icoupon.settlement.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.zicca.icoupon.settlement.common.enums.DiscountTypeEnum;
import com.zicca.icoupon.settlement.dto.resp.UserCouponQueryRespDTO;
import com.zicca.icoupon.settlement.service.CouponCalculateService;
import com.zicca.icoupon.settlement.service.CouponTemplateService;
import com.zicca.icoupon.settlement.service.UserCouponService;
import com.zicca.icoupon.settlement.service.basic.CouponCalculationStrategyFactory;
import com.zicca.icoupon.settlement.service.basic.calculation.CalculateOverlayParam;
import com.zicca.icoupon.settlement.service.basic.calculation.CalculateSingleParam;
import com.zicca.icoupon.settlement.service.basic.calculation.PricePair;
import com.zicca.icoupon.settlement.service.basic.rule.CouponPriorityHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.zicca.icoupon.settlement.common.enums.DiscountTargetEnum.PARTIAL;
import static com.zicca.icoupon.settlement.common.enums.DiscountTypeEnum.*;
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
    private final CouponCalculationStrategyFactory calculationFactory;

    @Override
    public PricePair calculateSingleAmount(CalculateSingleParam param) {
        log.info("计算优惠券后金额开始，参数: {}", param);
        // 查询用户优惠券信息
        UserCouponQueryRespDTO coupon = userCouponService.getUserCouponById(param.getUserCouponId(), param.getUserId());
        log.info("查询用户优惠券信息：{}", coupon);
        // 优惠券不存在
        if (coupon == null || coupon.getStatus() != NOT_USED) {
            log.warn("[用户优惠券服务] 优惠券不存在或优惠券状态异常");
            return calculateWithNoCoupon(param);
        }
        // 如果优惠券是指定商品可用，则需要判断商品是否在优惠券内
        if (PARTIAL == coupon.getTarget() && !couponTemplateService.isSupportGoods(coupon.getCouponTemplateId(), coupon.getShopId(), param.getGoodsId())) {
            log.warn("[用户优惠券服务] 商品不在优惠券内");
            return calculateWithNoCoupon(param);
        }

        return calculationFactory.calculate(param, coupon);
    }


    @Override
    public PricePair calculateOverlayAmount(CalculateOverlayParam param) {
        log.info("计算优惠券叠加金额开始，参数: {}", param);
        List<UserCouponQueryRespDTO> coupons = userCouponService.getUserCouponList(param.getUserCouponIds(), param.getUserId());
        if (CollectionUtil.isEmpty(coupons)) {
            log.info("[用户优惠券服务] 优惠券不存在");
        }
        BigDecimal totalAmount = calculateTotalAmount(param);
        List<UserCouponQueryRespDTO> bestCoupons = chooseBestCoupons(totalAmount, coupons);
        if (CollectionUtil.isEmpty(bestCoupons)) {
            return calculateWithNoCoupon(converToSingleParam(param));
        }
        return calculateBestCouponsOverlay(param, bestCoupons, totalAmount);
    }

    /**
     * 从优惠券列表中选择最合适的优惠券 | 每种类型优惠券最多选择一个
     *
     * @param totalAmount 金额
     * @param coupons     优惠券列表
     * @return 最合适的优惠券列表
     */
    private List<UserCouponQueryRespDTO> chooseBestCoupons(BigDecimal totalAmount, List<UserCouponQueryRespDTO> coupons) {
        // 按优惠类型分组
        Map<DiscountTypeEnum, List<UserCouponQueryRespDTO>> couponGroups = coupons.stream().collect(Collectors.groupingBy(UserCouponQueryRespDTO::getType));

        List<UserCouponQueryRespDTO> bestCoupons = new ArrayList<>();

        // 处理立减券
        if (couponGroups.containsKey(LITTLE_REDUCTION) && CollectionUtil.isNotEmpty(couponGroups.get(LITTLE_REDUCTION))) {
            List<UserCouponQueryRespDTO> littleReductionCoupons = couponGroups.get(LITTLE_REDUCTION);
            littleReductionCoupons.sort(CouponPriorityHandler.createLittleReductionComparator(totalAmount));
            bestCoupons.add(littleReductionCoupons.get(0)); // 选择最优的立减券
        }
        // 处理折扣券
        if (couponGroups.containsKey(DISCOUNT) && CollectionUtil.isNotEmpty(couponGroups.get(DISCOUNT))) {
            List<UserCouponQueryRespDTO> discountCoupons = couponGroups.get(DISCOUNT);
            discountCoupons.sort(CouponPriorityHandler.createDiscountComparator());
            bestCoupons.add(discountCoupons.get(0)); // 选择最优的折扣券
        }
        // 处理满减券
        if (couponGroups.containsKey(FULL_REDUCTION) && CollectionUtil.isNotEmpty(couponGroups.get(FULL_REDUCTION))) {
            List<UserCouponQueryRespDTO> fullReductionCoupons = couponGroups.get(FULL_REDUCTION);
            fullReductionCoupons.sort(CouponPriorityHandler.createFullReductionComparator(totalAmount));
            bestCoupons.add(fullReductionCoupons.get(0)); // 选择最优的满减券
        }
        // 处理随机券
        if (couponGroups.containsKey(RANDOM) && CollectionUtil.isNotEmpty(couponGroups.get(RANDOM))) {
            List<UserCouponQueryRespDTO> randomCoupons = couponGroups.get(RANDOM);
            randomCoupons.sort(CouponPriorityHandler.createRandomComparator());
            bestCoupons.add(randomCoupons.get(0)); // 选择最优的随机券
        }
        return bestCoupons;
    }


    /**
     * 计算总金额
     */
    private BigDecimal calculateTotalAmount(CalculateOverlayParam requestParam) {
        return requestParam.getPrice().multiply(BigDecimal.valueOf(requestParam.getQuantity()));
    }

    /**
     * 转换叠加参数为单品参数
     */
    private CalculateSingleParam converToSingleParam(CalculateOverlayParam overlayParam) {
        return CalculateSingleParam.builder()
                .userId(overlayParam.getUserId())
                .goodsId(overlayParam.getGoodsId())
                .price(overlayParam.getPrice())
                .quantity(overlayParam.getQuantity())
                .build();
    }

    private PricePair calculateBestCouponsOverlay(CalculateOverlayParam requestParam, List<UserCouponQueryRespDTO> bestCoupons, BigDecimal totalAmount) {
        BigDecimal subtotalAmount = totalAmount;
        BigDecimal discountAmount = BigDecimal.ZERO;
        BigDecimal payAmout = totalAmount;

        // 按照优惠券类型优先级顺序应用优惠券
        // 一般优先级：立减券 > 满减券 > 折扣券 > 随机券
        List<UserCouponQueryRespDTO> sortedCoupons = bestCoupons.stream()
                .sorted(Comparator.comparing(coupon -> CouponPriorityHandler.getCouponPriority(coupon.getType())))
                .collect(Collectors.toList());

        for (UserCouponQueryRespDTO coupon : sortedCoupons) {
            log.info("当前处理优惠券类型：{}", coupon.getType());
            // 创建基于当前支付金额的计算参数
            CalculateSingleParam tempParam = CalculateSingleParam.builder()
                    .userId(requestParam.getUserId())
                    .goodsId(requestParam.getGoodsId())
                    .price(payAmout) // 使用当前支付金额作为单价
                    .quantity(1) // 数量设置为1，因为金额已经计算过
                    .userCouponId(coupon.getId())
                    .build();

            PricePair pricePair = calculationFactory.calculate(tempParam, coupon);
            discountAmount = discountAmount.add(pricePair.getDiscountAmount());
            payAmout = pricePair.getPayAmount();
        }

        return PricePair.builder()
                .subtotalAmount(subtotalAmount)
                .payAmount(payAmout)
                .discountAmount(discountAmount)
                .build();
    }


    /**
     * 优惠券计算 | 无优惠券
     *
     * @param requestParam 优惠券计算参数
     * @return 优惠券计算结果
     */
    private PricePair calculateWithNoCoupon(CalculateSingleParam requestParam) {
        log.info("优惠券计算 | 无优惠券");
        return PricePair.noDiscount(requestParam.getPrice().multiply(BigDecimal.valueOf(requestParam.getQuantity())));
    }


}
