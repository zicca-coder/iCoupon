package com.zicca.icoupon.distribution.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import com.zicca.icoupon.framework.exception.ServiceException;

import java.util.Arrays;

/**
 * 分发任务状态枚举
 * <p> 0：未开始
 * <p> 1：进行中
 * <p> 2：已完成
 * <p> 3：已取消
 * <p> 4：失败
 *
 * @author zicca
 */
public enum DistributionTaskStatusEnum implements IEnum<Integer> {

    NOT_STARTED(0, "未开始"),

    IN_PROGRESS(1, "进行中"),

    COMPLETED(2, "已完成"),

    CANCELED(3, "已取消"),

    FAILED(4, "失败"),
    ;
    @EnumValue
    private Integer code;
    @JsonValue
    private String desc;

    DistributionTaskStatusEnum(Integer code, String desc) {
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

    public static DistributionTaskStatusEnum getByCode(Integer code) {
        return Arrays.stream(values())
                .filter(item -> item.code.equals(code))
                .findFirst().orElseThrow(() -> new ServiceException("分发任务状态码"));
    }
}
