package com.zicca.icoupon.engine.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zicca.icoupon.engine.dao.entity.ReservationReminder;
import com.zicca.icoupon.engine.dto.req.ReservationReminderCancelReqDTO;
import com.zicca.icoupon.engine.dto.req.ReservationReminderCreateReqDTO;
import com.zicca.icoupon.engine.dto.resp.ReservationReminderQueryRespDTO;
import com.zicca.icoupon.engine.service.basics.reminder.ReservationReminderDTO;

import java.util.List;

/**
 * 预约提醒服务
 *
 * @author zicca
 */
public interface ReservationReminderService extends IService<ReservationReminder> {

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

    /**
     * 判断预约提醒是否存在
     *
     * @param requestParam 预约提醒数据传输对象
     * @return 预约提醒是否存在
     */
    boolean isReservationReminderExist(ReservationReminderDTO requestParam);


}
