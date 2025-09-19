package com.zicca.icoupon.settlement.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用户优惠券列表查询参数
 *
 * @author zicca
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCouponListReqDTO {

    /**
     * 用户优惠券ID列表
     */
    private List<Long> userCouponIds;

    /**
     * 用户 ID
     */
    private Long userId;

}
