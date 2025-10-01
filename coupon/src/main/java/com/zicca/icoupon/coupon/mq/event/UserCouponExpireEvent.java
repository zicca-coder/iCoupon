package com.zicca.icoupon.coupon.mq.event;

import com.zicca.icoupon.coupon.dao.entity.UserCoupon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户优惠券过期事件 | 过期自动修改优惠券状态，删除缓存
 *
 * @author zicca
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCouponExpireEvent {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户优惠券ID
     */
    private Long userCouponId;

    /**
     * 用户优惠券
     */
    private UserCoupon userCoupon;

    /**
     * 延迟时间
     */
    private Long delayTime;


}
