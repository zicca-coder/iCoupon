package com.zicca.icoupon.agent.service;

import com.zicca.icoupon.agent.dto.req.ReservationReminderCancelReqDTO;
import com.zicca.icoupon.agent.dto.req.ReservationReminderCreateReqDTO;
import com.zicca.icoupon.agent.dto.resp.ReservationReminderQueryRespDTO;

import java.util.List;

/**
 * 预约提醒服务
 *
 * @author zicca
 */
public interface ReservationReminderService {

    /**
     * 创建预约提醒
     *
     * @param requestParam 创建预约提醒请求参数
     */
    void createReservationReminder(ReservationReminderCreateReqDTO requestParam);

    /**
     * 查询预约提醒
     *
     * @return 预约提醒列表
     */
    List<ReservationReminderQueryRespDTO> listReservationReminder();

    /**
     * 取消预约提醒
     *
     * @param requestParam 取消预约提醒请求参数
     */
    void cancelReservationReminder(ReservationReminderCancelReqDTO requestParam);

}
