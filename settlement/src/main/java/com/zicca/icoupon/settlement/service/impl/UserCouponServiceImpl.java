package com.zicca.icoupon.settlement.service.impl;

import com.zicca.icoupon.framework.exception.RemoteException;
import com.zicca.icoupon.framework.result.Result;
import com.zicca.icoupon.settlement.api.EngineServiceApi;
import com.zicca.icoupon.settlement.dto.req.UserCouponListReqDTO;
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
        try {
            Result<UserCouponQueryRespDTO> result = engineServiceApi.getUserCouponById(id, userId);
            if (result == null) {
                log.error("[结算服务] | 远程调用引擎服务返回空结果，id: {}, userId: {}", id, userId);
                throw new RemoteException("引擎服务调用返回空结果");
            }

            if (result.isSuccess()) {
                return result.getData();
            }

            log.warn("[结算服务] | 远程调用引擎服务失败，id: {}, userId: {}, code: {}, message: {}",
                    id, userId, result.getCode(), result.getMessage());
            throw new RemoteException("引擎服务调用失败: " + result.getMessage());
        } catch (Exception e) {
            log.error("[结算服务] | 远程调用引擎服务异常，id: {}, userId: {}", id, userId, e);
            throw new RemoteException("引擎服务调用异常");
        }
    }

    @Override
    public List<UserCouponQueryRespDTO> getUserCouponList(UserCouponQueryReqDTO requestParam) {
        return List.of();
    }

    @Override
    public List<UserCouponQueryRespDTO> getUserCouponList(List<Long> userCouponIds, Long userId) {
        try {
            UserCouponListReqDTO requestParam = new UserCouponListReqDTO(userCouponIds, userId);
            Result<List<UserCouponQueryRespDTO>> result = engineServiceApi.batchGetUserCouponList(requestParam);

            if (result == null) {
                log.error("[结算服务] | 远程调用引擎服务批量获取用户优惠券列表返回空结果，userId: {}, couponIds: {}", userId, userCouponIds);
                throw new RemoteException("引擎服务调用返回空结果");
            }

            if (result.isSuccess()) {
                return result.getData();
            }

            log.warn("[结算服务] | 远程调用引擎服务批量获取用户优惠券列表失败，userId: {}, couponIds: {}, code: {}, message: {}",
                    userId, userCouponIds, result.getCode(), result.getMessage());
            throw new RemoteException("引擎服务调用失败: " + result.getMessage());
        } catch (Exception e) {
            log.error("[结算服务] | 远程调用引擎服务批量获取用户优惠券列表异常，userId: {}, couponIds: {}", userId, userCouponIds, e);
            throw new RemoteException("引擎服务调用异常");
        }
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
