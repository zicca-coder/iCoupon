package com.zicca.icoupon.engine.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zicca.icoupon.engine.common.context.UserContext;
import com.zicca.icoupon.engine.dao.entity.ReservationReminder;
import com.zicca.icoupon.engine.dao.mapper.ReservationReminderMapper;
import com.zicca.icoupon.engine.dto.req.ReservationReminderCancelReqDTO;
import com.zicca.icoupon.engine.dto.req.ReservationReminderCreateReqDTO;
import com.zicca.icoupon.engine.dto.resp.CouponTemplateQueryRespDTO;
import com.zicca.icoupon.engine.dto.resp.ReservationReminderQueryRespDTO;
import com.zicca.icoupon.engine.mq.event.ReservationReminderPushEvent;
import com.zicca.icoupon.engine.mq.producer.ReservationReminderPushProducer;
import com.zicca.icoupon.engine.service.CouponTemplateService;
import com.zicca.icoupon.engine.service.ReservationReminderService;
import com.zicca.icoupon.engine.service.basics.reminder.ReservationReminderDTO;
import com.zicca.icoupon.engine.toolkit.ReservationReminderUtil;
import com.zicca.icoupon.framework.exception.ClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static com.zicca.icoupon.engine.common.enums.CouponTemplateStatusEnum.IN_PROGRESS;

/**
 * 预约提醒服务实现类
 * @author zicca
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationReminderServiceImpl extends ServiceImpl<ReservationReminderMapper, ReservationReminder> implements ReservationReminderService {

    private final CouponTemplateService couponTemplateService;
    private final ReservationReminderMapper reservationReminderMapper;
    private final ReservationReminderPushProducer reservationReminderPushProducer;

    @Override
    public void createReservationReminder(ReservationReminderCreateReqDTO requestParam) {
        // 查询优惠券是否有效
        CouponTemplateQueryRespDTO couponTemplate = couponTemplateService.getCouponTemplate(requestParam.getCouponTemplateId(), requestParam.getShopId());
        if (couponTemplate == null) {
            throw new ClientException("优惠券模板不存在");
        }
        if (couponTemplate.getValidEndTime().before(new Date())) {
            throw new ClientException("无法预约已经开始领取的优惠券");
        }
        // 查询是否已经创建过预约信息
        ReservationReminder reminder = lambdaQuery().eq(ReservationReminder::getCouponTemplateId, requestParam.getCouponTemplateId())
                .eq(ReservationReminder::getUserId, Long.parseLong(UserContext.getUserId())).one();
        if (reminder != null){ // 如果创建过预约信息，则更新
            Long information = reminder.getInformation();
            Long bitMap = ReservationReminderUtil.calculateBitMap(requestParam.getRemindTime(), requestParam.getType());
            if ((information & bitMap) != 0L) {
                throw new ClientException("已经创建过该提醒了");
            }
            reminder.setInformation(information ^ bitMap);
            lambdaUpdate().eq(ReservationReminder::getCouponTemplateId, requestParam.getCouponTemplateId())
                    .eq(ReservationReminder::getUserId, Long.parseLong(UserContext.getUserId()))
                    .set(ReservationReminder::getInformation, reminder.getInformation())
                    .update();
        } else { // 如果没有创建过预约信息，则创建
            reminder= BeanUtil.toBean(requestParam, ReservationReminder.class);
            reminder.setStartTime(couponTemplate.getValidStartTime());
            reminder.setUserId(Long.parseLong(UserContext.getUserId()));
            reminder.setInformation(ReservationReminderUtil.calculateBitMap(requestParam.getRemindTime(), requestParam.getType()));
            save(reminder);
        }
        ReservationReminderPushEvent event = ReservationReminderPushEvent.builder()
                .couponTemplateId(requestParam.getCouponTemplateId())
                .shopId(requestParam.getShopId())
                .userId(Long.parseLong(UserContext.getUserId()))
                .remindTime(requestParam.getRemindTime())
                .type(requestParam.getType())
                .startTime(couponTemplate.getValidStartTime())
                .delayTime(DateUtil.offsetMinute(couponTemplate.getValidStartTime(), -requestParam.getRemindTime()).getTime())
                .build();
        reservationReminderPushProducer.sendMessage(event);
    }

    @Override
    public List<ReservationReminderQueryRespDTO> listReservationReminder() {
        List<ReservationReminder> reminders = lambdaQuery().eq(ReservationReminder::getUserId, Long.parseLong(UserContext.getUserId()))
                .list();
        if (CollectionUtil.isEmpty(reminders)) {
            return List.of();
        }
        return reminders.stream()
                .map(reminder -> BeanUtil.copyProperties(reminder, ReservationReminderQueryRespDTO.class))
                .toList();
    }

    @Override
    public void cancelReservationReminder(ReservationReminderCancelReqDTO requestParam) {
        // 查询优惠券是否有效
        CouponTemplateQueryRespDTO couponTemplate = couponTemplateService.getCouponTemplate(requestParam.getCouponTemplateId(), requestParam.getShopId());
        if (couponTemplate == null) {
            throw new ClientException("优惠券模板不存在");
        }
        if (couponTemplate.getValidEndTime().before(new Date())) {
            throw new ClientException("无法取消已经开始优惠券预约");
        }
        ReservationReminder reminder = lambdaQuery().eq(ReservationReminder::getCouponTemplateId, requestParam.getCouponTemplateId())
                .eq(ReservationReminder::getUserId, Long.parseLong(UserContext.getUserId()))
                .one();
        if (reminder == null) {
            throw new ClientException("没有该预约信息");
        }
        Long bitMap = ReservationReminderUtil.calculateBitMap(requestParam.getRemindTime(), requestParam.getType());
        if ((bitMap & reminder.getInformation()) == 0L) {
            throw new ClientException("没有该预约该时间点的提醒");
        }
        bitMap ^= reminder.getInformation();
        lambdaUpdate().eq(ReservationReminder::getCouponTemplateId, requestParam.getCouponTemplateId())
                .eq(ReservationReminder::getUserId, Long.parseLong(UserContext.getUserId()))
                .set(ReservationReminder::getInformation, bitMap)
                .update();
    }

    @Override
    public boolean isReservationReminderExist(ReservationReminderDTO requestParam) {
        ReservationReminder reminder = lambdaQuery().eq(ReservationReminder::getCouponTemplateId, requestParam.getCouponTemplateId())
                .eq(ReservationReminder::getUserId, Long.parseLong(UserContext.getUserId()))
                .one();
        if (reminder == null) {
            // 数据库中没有该预约信息
            return false;
        }

        Long information = reminder.getInformation();
        Long bitMap = ReservationReminderUtil.calculateBitMap(requestParam.getRemindTime(), requestParam.getType());
        // 按位与等于 0 说明该时间点的预约信息不存在
        return (information & bitMap) != 0L;
    }
}
