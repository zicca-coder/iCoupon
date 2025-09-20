package com.zicca.icoupon.order.service.impl;

import com.zicca.icoupon.order.api.CouponServiceApi;
import com.zicca.icoupon.order.dto.req.UserCouponBathLockReqDTO;
import com.zicca.icoupon.order.service.UserCouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author zicca
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserCouponServiceImpl implements UserCouponService {

    private final CouponServiceApi couponServiceApi;


    @Override
    public void lockUserCoupon(Long id, Long userId) {
        couponServiceApi.lockUserCoupon(id, userId);
    }

    @Override
    public void batchLockUserCoupon(UserCouponBathLockReqDTO requestParam) {
        couponServiceApi.batchLockUserCoupon(requestParam);
    }

}
