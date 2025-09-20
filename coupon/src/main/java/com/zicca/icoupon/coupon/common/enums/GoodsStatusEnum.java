package com.zicca.icoupon.coupon.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 商品状态枚举
 *
 * @author zicca
 */
public enum GoodsStatusEnum implements IEnum<Integer> {



    ;
    @EnumValue
    private Integer code;
    @JsonValue
    private String desc;

    GoodsStatusEnum(Integer code, String desc) {
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
}
