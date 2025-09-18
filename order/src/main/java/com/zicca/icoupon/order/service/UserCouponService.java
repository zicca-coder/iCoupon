package com.zicca.icoupon.order.service;

import com.zicca.icoupon.order.dto.req.UserCouponBathLockReqDTO;

/**
 * @author zicca
 */
public interface UserCouponService {

    /**
     * 锁定用户优惠券
     *
     * @param id     用户优惠券 ID
     * @param userId 用户 ID
     */
    void lockUserCoupon(Long id, Long userId);

    /**
     * 批量锁定用户优惠券
     *
     * @param requestParam 批量锁定用户优惠券参数
     */
    void batchLockUserCoupon(UserCouponBathLockReqDTO requestParam);


}
