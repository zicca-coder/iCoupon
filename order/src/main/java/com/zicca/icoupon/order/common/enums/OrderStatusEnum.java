package com.zicca.icoupon.order.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import com.zicca.icoupon.framework.exception.ServiceException;

import java.util.Arrays;

/**
 * 订单状态枚举
 * <p> 0：待支付 - 用户已提交订单但尚未支付
 * <p> 1：已支付 - 用户已完成支付
 * <p> 2：已取消 - 用户或系统取消了订单
 * <p> 3：已完成 - 订单已成功完成（已收货）
 * <p> 4：待发货 - 已支付但尚未发货
 * <p> 5：已发货 - 商家已发货
 * <p> 6：待收货 - 已发货但用户尚未确认收货
 * <p> 7：已关闭 - 订单超时未支付被系统关闭
 * <p> 8：退款中 - 用户申请退款
 * <p> 9：已退款 - 退款已完成
 *
 * @author zicca
 */
public enum OrderStatusEnum implements IEnum<Integer> {

    /**
     * 待支付 - 用户已提交订单但尚未支付
     */
    PENDING_PAYMENT(0, "待支付"),

    /**
     * 已支付 - 用户已完成支付
     */
    PAID(1, "已支付"),

    /**
     * 已取消 - 用户或系统取消了订单
     */
    CANCELLED(2, "已取消"),

    /**
     * 已完成 - 订单已成功完成（已收货）
     */
    COMPLETED(3, "已完成"),

    /**
     * 待发货 - 已支付但尚未发货
     */
    PENDING_SHIPMENT(4, "待发货"),

    /**
     * 已发货 - 商家已发货
     */
    SHIPPED(5, "已发货"),

    /**
     * 待收货 - 已发货但用户尚未确认收货
     */
    PENDING_RECEIPT(6, "待收货"),

    /**
     * 已关闭 - 订单超时未支付被系统关闭
     */
    CLOSED(7, "已关闭"),

    /**
     * 退款中 - 用户申请退款
     */
    REFUNDING(8, "退款中"),

    /**
     * 已退款 - 退款已完成
     */
    REFUNDED(9, "已退款");

    @EnumValue
    private Integer code;
    @JsonValue
    private String desc;

    OrderStatusEnum(Integer code, String desc) {
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

    public static OrderStatusEnum getByCode(Integer code) {
        return Arrays.stream(values())
                .filter(item -> item.code.equals(code))
                .findFirst().orElseThrow(() -> new ServiceException("无效的订单状态码"));
    }
}
