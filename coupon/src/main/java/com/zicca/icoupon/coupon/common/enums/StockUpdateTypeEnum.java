package com.zicca.icoupon.coupon.common.enums;

import com.zicca.icoupon.framework.exception.ServiceException;

import java.util.Arrays;

/**
 * 库存更新类型枚举
 *
 * @author zicca
 */
public enum StockUpdateTypeEnum {

    USER_RECEIVE_COUPON(1, "用户领取优惠券事件"),
    MERCHANT_UPDATE_STOCK(2, "商户/后台更新优惠券库存事件"),
    ;
    private Integer code;

    private String desc;

    StockUpdateTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static StockUpdateTypeEnum fromCode(Integer code) {
        return Arrays.stream(values())
                .filter(item -> item.code.equals(code))
                .findFirst().orElseThrow(() -> new ServiceException("无效的库存变更事件类型码"));
    }



}
