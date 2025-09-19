package com.zicca.icoupon.settlement.service.basic.rule;

import com.zicca.icoupon.settlement.common.enums.DiscountTypeEnum;
import com.zicca.icoupon.settlement.dto.resp.UserCouponQueryRespDTO;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Random;

/**
 * 优惠券优先级处理
 *
 * @author zicca
 */
public final class CouponPriorityHandler {

    private static final int LITTLE_REDUCTION_PRIORITY = 1;
    private static final int FULL_REDUCTION_PRIORITY = 2;
    private static final int DISCOUNT_PRIORITY = 3;
    private static final int RANDOM_PRIORITY = 4;
    private static final int NO_COUPON_PRIORITY = 5;


    /**
     * 获取优惠券的优先级
     *
     * @param type 优惠券优惠类型
     * @return 优惠券的优先级
     */
    public static int getCouponPriority(DiscountTypeEnum type) {
        switch (type) {
            case LITTLE_REDUCTION: // 立减券优先级最高
                return LITTLE_REDUCTION_PRIORITY;
            case FULL_REDUCTION: // 满减券
                return FULL_REDUCTION_PRIORITY;
            case DISCOUNT: // 折扣券
                return DISCOUNT_PRIORITY;
            case RANDOM: // 随机券
                return RANDOM_PRIORITY;
            default: // 无优惠券
                return NO_COUPON_PRIORITY;
        }
    }


    /**
     * 创建立减券的比较规则
     *
     * @param orderAmount 订单金额
     * @return 比较器
     */
    public static Comparator<UserCouponQueryRespDTO> createLittleReductionComparator(BigDecimal orderAmount) {
        return (coupon1, coupon2) -> {
            BigDecimal discount1 = coupon1.getFaceValue();
            BigDecimal discount2 = coupon2.getFaceValue();

            boolean discount1ExceedOrder = discount1.compareTo(orderAmount) > 0;
            boolean discount2ExceedOrder = discount2.compareTo(orderAmount) > 0;

            // 两个立减金额都比订单大，立减金额小的优先（节省优惠券）
            if (discount1ExceedOrder && discount2ExceedOrder) {
                return discount1.compareTo(discount2);
            }

            // 一个立减金额比订单大，一个立减金额比订单小，立减金额大的优先，即优惠后金额小的优先
            if (discount1ExceedOrder != discount2ExceedOrder) {
                return discount1ExceedOrder ? 1 : -1;
            }

            // 两个立减金额都比订单小，立减金额大的优先
            return discount2.compareTo(discount1);
        };
    }

    /**
     * 创建一个折扣券的比较规则
     *
     * @return
     */
    public static Comparator<UserCouponQueryRespDTO> createDiscountComparator() {
        // 折扣率越小，折扣力度越大
        // 例如：0.8折(8折)比0.9折(9折)更优惠
        return Comparator.comparing(UserCouponQueryRespDTO::getFaceValue);
    }


    /**
     * 创建一个满减券的比较规则
     *
     * @param orderAmount 订单金额
     * @return 比较器
     */
    public static Comparator<UserCouponQueryRespDTO> createFullReductionComparator(BigDecimal orderAmount) {
        return (coupon1, coupon2) -> {
            boolean coupon1Meets = orderAmount.compareTo(coupon1.getMinAmount()) >= 0;
            boolean coupon2Meets = orderAmount.compareTo(coupon2.getMinAmount()) >= 0;

            // 一个达到门槛，一个未达到门槛，优先选择达到门槛的
            if (coupon1Meets && !coupon2Meets) return -1;
            if (!coupon1Meets && coupon2Meets) return 1;

            // 两个都达到门槛或都达不到门槛，选择优惠金额大的
            return coupon2.getFaceValue().compareTo(coupon1.getFaceValue());
        };
    }


    /**
     * 创建一个随机券的比较规则
     *
     * @return 随机券的比较器
     */
    public static Comparator<UserCouponQueryRespDTO> createRandomComparator() {
        Random random = new Random();
        return (coupon1, coupon2) -> {
            BigDecimal min1 = coupon1.getMinAmount();
            BigDecimal min2 = coupon2.getMinAmount();
            BigDecimal max1 = coupon1.getFaceValue();
            BigDecimal max2 = coupon2.getFaceValue();
            // 1. 比较最小值，最小值大的优先
            int minCompare = min2.compareTo(min1);
            if (minCompare != 0) {
                return minCompare;
            }
            // 2. 最小值相同，比较最大值，最大值大的优先
            int maxCompare = max2.compareTo(max1);
            if (maxCompare != 0) {
                return maxCompare;
            }
            // 3. 区间相同，随机选择
            return random.nextBoolean() ? -1 : 1;
        };
    }

}
