package com.zicca.icoupon.agent.service.impl;

import com.zicca.icoupon.agent.api.CouponServiceApi;
import com.zicca.icoupon.agent.dto.req.ReservationReminderCancelReqDTO;
import com.zicca.icoupon.agent.dto.req.ReservationReminderCreateReqDTO;
import com.zicca.icoupon.agent.dto.resp.CouponTemplateQueryRespDTO;
import com.zicca.icoupon.agent.dto.resp.ReservationReminderQueryRespDTO;
import com.zicca.icoupon.agent.service.ReservationReminderService;
import com.zicca.icoupon.framework.exception.RemoteException;
import com.zicca.icoupon.framework.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 预约提醒服务实现类
 * @author zicca
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationReminderServiceImpl implements ReservationReminderService {

    private final CouponServiceApi couponServiceApi;

    @Override
    public void createReservationReminder(ReservationReminderCreateReqDTO requestParam) {
        try {
            Result<CouponTemplateQueryRespDTO> result = couponServiceApi.getCouponTemplateById(requestParam.getCouponTemplateId(), requestParam.getShopId());

            if (result == null) {
                log.error("[智能体服务] | 远程调用优惠券服务创建优惠券预约提醒返回空结果，" + "requestParam：{}", requestParam);
                throw new RemoteException("优惠券服务调用返回空结果");
            }

            if (result.isSuccess()) {
                return;
            }

            log.warn("[智能体服务] | 优惠券服务创建优惠券预约提醒失败，" + "requestParam：{}", requestParam);
            throw new RemoteException("优惠券服务调用失败: " + result.getMessage());
        } catch (Exception e) {
            log.error("[智能体服务] | 优惠券服务创建优惠券预约提醒异常，" + "requestParam：{}", requestParam, e);
            throw new RemoteException("优惠券服务调用异常");
        }
    }

    @Override
    public List<ReservationReminderQueryRespDTO> listReservationReminder() {
        try {
            Result<List<ReservationReminderQueryRespDTO>> result = couponServiceApi.listReservationReminder();

            if (result == null) {
                log.error("[智能体服务] | 远程调用优惠券服务查询优惠券预约提醒返回空结果");
                throw new RemoteException("优惠券服务调用返回空结果");
            }

            if (result.isSuccess()) {
                return result.getData();
            }

            log.warn("[智能体服务] | 优惠券服务查询优惠券预约提醒失败");
            throw new RemoteException("优惠券服务调用失败: " + result.getMessage());
        } catch (Exception e) {
            log.error("[智能体服务] | 优惠券服务查询优惠券预约提醒异常", e);
            throw new RemoteException("优惠券服务调用异常");
        }
    }

    @Override
    public void cancelReservationReminder(ReservationReminderCancelReqDTO requestParam) {
        try {
            Result<Void> result = couponServiceApi.cancelReservationReminder(requestParam);

            if (result == null) {
                log.error("[智能体服务] | 远程调用优惠券服务取消优惠券预约提醒返回空结果，" + "requestParam：{}", requestParam);
                throw new RemoteException("优惠券服务调用返回空结果");
            }

            if (result.isSuccess()) {
                return;
            }

            log.warn("[智能体服务] | 优惠券服务取消优惠券预约提醒失败，" + "requestParam：{}", requestParam);
            throw new RemoteException("优惠券服务调用失败: " + result.getMessage());
        } catch (Exception e) {
            log.error("[智能体服务] | 优惠券服务取消优惠券预约提醒异常，" + "requestParam：{}", requestParam, e);
            throw new RemoteException("优惠券服务调用异常");
        }
    }
}
