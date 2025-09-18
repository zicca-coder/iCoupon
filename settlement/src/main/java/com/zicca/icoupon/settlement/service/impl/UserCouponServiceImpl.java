package com.zicca.icoupon.settlement.service.impl;

import com.zicca.icoupon.settlement.api.EngineServiceApi;
import com.zicca.icoupon.settlement.dto.req.UserCouponQueryReqDTO;
import com.zicca.icoupon.settlement.dto.resp.UserCouponQueryRespDTO;
import com.zicca.icoupon.settlement.service.UserCouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户优惠券服务实现类
 *
 * @author zicca
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserCouponServiceImpl implements UserCouponService {

    private final EngineServiceApi engineServiceApi;

    @Override
    public UserCouponQueryRespDTO getUserCouponById(Long id, Long userId) {
        return engineServiceApi.getUserCouponById(id, userId).getData();
    }

    @Override
    public List<UserCouponQueryRespDTO> getUserCouponList(UserCouponQueryReqDTO requestParam) {
        return List.of();
    }

    @Override
    public List<UserCouponQueryRespDTO> getUserCouponListByStatus(UserCouponQueryReqDTO requestParam) {
        return List.of();
    }

    @Override
    public List<UserCouponQueryRespDTO> getAvailableUserCouponList(Long userId) {
        return List.of();
    }
}
