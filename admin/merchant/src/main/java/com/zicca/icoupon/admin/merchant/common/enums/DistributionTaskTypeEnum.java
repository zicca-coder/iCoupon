package com.zicca.icoupon.admin.merchant.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import com.zicca.icoupon.framework.exception.ServiceException;

import java.util.Arrays;

/**
 * 分发任务类型枚举
 * <p> 0：立即发送
 * <p> 1：定时发送
 *
 * @author zicca
 */
public enum DistributionTaskTypeEnum implements IEnum<Integer> {

    IMMEDIATELY(0, "立即发送"),
    TIMING(1, "定时发送");
    ;
    @EnumValue
    private Integer code;
    @JsonValue
    private String desc;

    DistributionTaskTypeEnum(Integer code, String desc) {
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

    public static DistributionTaskTypeEnum getByCode(Integer code) {
        return Arrays.stream(values())
                .filter(item -> item.code.equals(code))
                .findFirst().orElseThrow(() -> new ServiceException("无效的分发任务类型码"));
    }
}
