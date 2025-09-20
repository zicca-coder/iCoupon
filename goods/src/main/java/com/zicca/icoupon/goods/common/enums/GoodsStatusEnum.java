package com.zicca.icoupon.goods.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 商品状态枚举
 * <p> 0：在售
 * <p> 1：售罄
 * <p> 2：下架
 *
 * @author zicca
 */
public enum GoodsStatusEnum implements IEnum<Integer> {

    /**
     * 在售
     */
    ON_SALE(0, "在售"),

    /**
     * 售罄
     */
    SOLD_OUT(1, "售罄"),

    /**
     * 下架
     */
    OFF_SHELF(2, "下架"),
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
