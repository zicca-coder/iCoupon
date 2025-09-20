package com.zicca.icoupon.coupon.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import com.zicca.icoupon.framework.exception.ServiceException;

import java.util.Arrays;

/**
 * 通知类型枚举
 * <p> 0：弹框推送
 * <p> 1：邮件
 * <p> 2：短信
 * <p> 3：站内信
 * <p> 4：微信
 *
 * @author zicca
 */
public enum NotifyTypeEnum implements IEnum<Integer> {

    POPUP(0, "弹框推送"),
    EMAIL(1, "邮件"),
    SMS(2, "短信"),
    INBOX(3, "站内信"),
    WECHAT(4, "微信"),
    ;
    @EnumValue
    private Integer code;
    @JsonValue
    private String desc;

    NotifyTypeEnum(Integer code, String desc) {
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

    public static NotifyTypeEnum getByCode(Integer code) {
        return Arrays.stream(values())
                .filter(item -> item.code.equals(code))
                .findFirst().orElseThrow(() -> new ServiceException("无效的通知类型码"));
    }

}
