package com.zicca.icoupon.order.service.impl;

import com.zicca.icoupon.order.api.EngineServiceApi;
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

    private final EngineServiceApi engineServiceApi;


    @Override
    public void lockUserCoupon(Long id, Long userId) {
        engineServiceApi.lockUserCoupon(id, userId);
    }

    @Override
    public void batchLockUserCoupon(UserCouponBathLockReqDTO requestParam) {
        engineServiceApi.batchLockUserCoupon(requestParam);
    }

}
