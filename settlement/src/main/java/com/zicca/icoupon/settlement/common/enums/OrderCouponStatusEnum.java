package com.zicca.icoupon.settlement.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import com.zicca.icoupon.framework.exception.ServiceException;

import java.util.Arrays;

/**
 * 订单优惠券状态枚举
 * <p> 0：锁定
 * <p> 1：已取消
 * <p> 2：已支付
 * <p> 3：已退款
 *
 * @author zicca
 */
public enum OrderCouponStatusEnum implements IEnum<Integer> {

    /**
     * 已锁定 - 订单创建时临时锁定优惠券
     * <p> 订单创建时临时锁定优惠券，此时优惠券对用户不可见，不能被其他订单使用
     */
    LOCKED(0, "已锁定"),

    /**
     * 已使用 - 订单支付成功后优惠券被正式使用
     * <p> 订单支付成功后优惠券被正式使用，优惠券状态变为已使用，从用户可用列表中移除
     */
    USED(1, "已使用"),

    /**
     * 已退还 - 订单取消或退款后优惠券被退还给用户
     * <p> 订单取消或退款后优惠券被退还给用户，优惠券重新变为可用状态
     */
    REFUNDED(2, "已退还"),

    /**
     * 已过期 - 锁定的优惠券超过有效期自动释放
     * <p> 锁定的优惠券超过预设时间未支付自动释放，优惠券重新变为可用状态
     */
    EXPIRED(3, "已过期");

    @EnumValue
    private Integer code;
    @JsonValue
    private String desc;

    OrderCouponStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    @Override
    public Integer getValue() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static OrderCouponStatusEnum getByCode(Integer code) {
        return Arrays.stream(values())
                .filter(item -> item.code.equals(code))
                .findFirst().orElseThrow(() -> new ServiceException("无效的订单状态码"));
    }
}
