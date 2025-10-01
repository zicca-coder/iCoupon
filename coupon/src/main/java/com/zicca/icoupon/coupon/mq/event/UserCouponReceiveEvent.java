package com.zicca.icoupon.coupon.mq.event;

import com.zicca.icoupon.coupon.dto.req.UserCouponReceiveReqDTO;
import com.zicca.icoupon.coupon.dto.resp.CouponTemplateQueryRespDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 优惠券领取事件
 *
 * @author zicca
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCouponReceiveEvent {

    private UserCouponReceiveReqDTO requestParam;

    private Integer receiveCount;

    private CouponTemplateQueryRespDTO couponTemplate;

    private Long userId;

}
