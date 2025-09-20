package com.zicca.icoupon.admin.merchant.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import com.zicca.icoupon.framework.exception.ServiceException;

import java.util.Arrays;

/**
 * 商品是否可用优惠券类型枚举
 * <P>0：不可使用优惠券
 * <P>1：可使用单张优惠券
 * <P>2：可叠加使用优惠券
 *
 * @author zicca
 */
public enum CouponSupportTypeEnum implements IEnum<Integer> {


    NOT_AVAILABLE(0, "不可使用优惠券"),
    SINGLE_AVAILABLE(1, "可使用单张优惠券"),
    OVERLAY_AVAILABLE(2, "可叠加使用优惠券")
    ;
    @EnumValue
    private Integer code;
    @JsonValue
    private String desc;

    CouponSupportTypeEnum(Integer code, String desc) {
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

    public static CouponSupportTypeEnum fromCode(Integer code) {
        return Arrays.stream(values())
                .filter(item -> item.code.equals(code))
                .findFirst().orElseThrow(() -> new ServiceException("无效的商品是否可用优惠券类型码"));
    }
}
