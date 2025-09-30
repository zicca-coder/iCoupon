package com.zicca.icoupon.agent.service.impl;

import com.zicca.icoupon.agent.api.CouponServiceApi;
import com.zicca.icoupon.agent.dto.req.UserCouponReceiveReqDTO;
import com.zicca.icoupon.agent.dto.resp.UserCouponQueryRespDTO;
import com.zicca.icoupon.agent.service.UserCouponService;
import com.zicca.icoupon.framework.exception.RemoteException;
import com.zicca.icoupon.framework.result.Result;
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

    private final CouponServiceApi couponServiceApi;

    @Override
    public void receiveCoupon(UserCouponReceiveReqDTO requestParam) {
        try {
            Result<Void> result = couponServiceApi.receiveCoupon(requestParam);

            if (result == null) {
                log.error("[智能体服务] | 远程调用优惠券服务领取优惠券返回空结果，" + "requestParam: {}", requestParam);
                throw new RemoteException("优惠券服务调用返回空结果");
            }

            if (result.isSuccess()) {
                return;
            }

            log.warn("[智能体服务] | 远程调用优惠券服务领取优惠券失败，" + "requestParam: {}, code: {}, message: {}",
                    requestParam, result.getCode(), result.getMessage());
            throw new RemoteException("优惠券服务调用失败: " + result.getMessage());
        } catch (Exception e) {
            log.error("[智能体服务] | 远程调用优惠券服务领取优惠券异常，" + "requestParam: {}", requestParam, e);
            throw new RemoteException("优惠券服务调用异常");
        }
    }

    @Override
    public UserCouponQueryRespDTO getUserCouponById(Long id, Long userId) {
        try {
            Result<UserCouponQueryRespDTO> result = couponServiceApi.getUserCouponById(id, userId);

            if (result == null) {
                log.error("[智能体服务] | 远程调用优惠券服务查询用户优惠券返回空结果，" + "id: {}, userId: {}", id, userId);
                throw new RemoteException("优惠券服务调用返回空结果");
            }

            if (result.isSuccess()) {
                return result.getData();
            }

            log.warn("[智能体服务] | 远程调用优惠券服务查询用户优惠券失败，" + "id: {}, userId: {}, code: {}, message: {}",
                    id, userId, result.getCode(), result.getMessage());
            throw new RemoteException("优惠券服务调用失败: " + result.getMessage());
        } catch (Exception e) {
            log.error("[智能体服务] | 远程调用优惠券服务查询用户优惠券异常，" + "id: {}, userId: {}", id, userId, e);
            throw new RemoteException("优惠券服务调用异常");
        }
    }

    @Override
    public List<UserCouponQueryRespDTO> getAvailableUserCouponList(Long userId) {
        try {
            Result<List<UserCouponQueryRespDTO>> result = couponServiceApi.getAvailableUserCouponList(1961811779001393153L);

            if (result == null) {
                log.error("[智能体服务] | 远程调用优惠券服务查询用户可用优惠券返回空结果，" + "userId: {}", userId);
                throw new RemoteException("优惠券服务调用返回空结果");
            }

            if (result.isSuccess()) {
                return result.getData();
            }

            log.warn("[智能体服务] | 远程调用优惠券服务查询用户可用优惠券失败，" + "userId: {}, code: {}, message: {}",
                    userId, result.getCode(), result.getMessage());
            throw new RemoteException("优惠券服务调用失败: " + result.getMessage());
        } catch (Exception e) {
            log.error("[智能体服务] | 远程调用优惠券服务查询用户可用优惠券异常，" + "userId: {}", userId, e);
            throw new RemoteException("优惠券服务调用异常");
        }
    }
}
