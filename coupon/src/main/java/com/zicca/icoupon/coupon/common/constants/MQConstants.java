package com.zicca.icoupon.coupon.common.constants;

/**
 * MQ常量
 *
 * @author zicca
 */
public final class MQConstants {

    /**
     * 预约提醒推送 Topic
     */
    public static final String RESERVATION_REMINDER_PUSH_TOPIC = "iCoupon_reservation-service_reminder-push_execute";

    /**
     * 预约提醒推送 Consumer Group
     */
    public static final String RESERVATION_REMINDER_PUSH_CG = "iCoupon_reservation-service_reminder-push_execute_CG";


    public static final String USER_COUPON_RECEIVE_TOPIC = "iCoupon_user-service_coupon-receive_execute";

    public static final String USER_COUPON_RECEIVE_CG = "iCoupon_user-service_coupon-receive_execute_CG";


    public static final String USER_COUPON_EXPIRE_TOPIC = "iCoupon_user-service_coupon-expire_execute";

    public static final String USER_COUPON_EXPIRE_CG = "iCoupon_user-service_coupon-expire_execute_CG";


}
