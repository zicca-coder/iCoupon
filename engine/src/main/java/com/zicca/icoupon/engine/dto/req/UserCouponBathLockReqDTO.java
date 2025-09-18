package com.zicca.icoupon.engine.dto.req;

import com.zicca.icoupon.engine.common.enums.UserCouponStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 批量锁定用户优惠券请求参数
 *
 * @author zicca
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCouponBathLockReqDTO {

    private List<Long> userCouponIds;

    private Long userId;

}
