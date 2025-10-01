package com.zicca.icoupon.coupon.dto.req;

import com.zicca.icoupon.coupon.common.enums.DiscountTargetEnum;
import com.zicca.icoupon.coupon.common.enums.DiscountTypeEnum;
import com.zicca.icoupon.coupon.common.enums.UserCouponStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 创建用户优惠券请求参数
 *
 * @author zicca
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCouponCreateReqDTO {

    /**
     * 用户 ID
     */
    @Schema(description = "用户 ID", example = "1")
    private Long userId;

    /**
     * 优惠券模板 ID
     */
    @Schema(description = "优惠券模板 ID", example = "1")
    private Long couponTemplateId;

    /**
     * 店铺编号
     * 如果是店铺券：存储当前店铺编号
     */
    @Schema(description = "店铺编号", example = "1")
    private Long shopId;

    /**
     * 优惠对象:
     * 0：指定商品可用，1：全店通用
     */
    @Schema(description = "优惠对象: 0：指定商品可用，1：全店通用", example = "0")
    private DiscountTargetEnum target;

    /**
     * 优惠类型：
     * 0：立减券 1：满减券 2：折扣券 3：随机券
     */
    @Schema(description = "优惠类型： 0：立减券 1：满减券 2：折扣券 3：随机券", example = "0")
    private DiscountTypeEnum type;

    /**
     * 立减券：立减金额
     * 满减券：满足条件后立减的金额
     * 折扣券：折扣率
     * 随机券：随机金额
     */
    @Schema(description = "立减券：立减金额 满减券：满足条件后立减的金额 折扣券：折扣率 随机券：随机金额", example = "10")
    private BigDecimal faceValue;

    /**
     * 满减券门槛金额（仅满减券有效）
     */
    @Schema(description = "满减券门槛金额（仅满减券有效）", example = "10")
    private BigDecimal minAmount;

    /**
     * 领取时间
     */
    @Schema(description = "领取时间", example = "2021-01-01 00:00:00")
    private Date receiveTime;

    /**
     * 使用时间
     */
    @Schema(description = "使用时间", example = "2021-01-01 00:00:00")
    private Date useTime;

    /**
     * 有效期开始时间
     */
    @Schema(description = "有效期开始时间", example = "2021-01-01 00:00:00")
    private Date validStartTime;

    /**
     * 有效期结束时间
     */
    @Schema(description = "有效期结束时间", example = "2021-01-01 00:00:00")
    private Date validEndTime;

    /**
     * 优惠券使用状态：
     * 0-未使用 1-锁定 2-已使用 3-已过期 4-已撤回
     */
    @Schema(description = "优惠券使用状态： 0-未使用 1-锁定 2-已使用 3-已过期 4-已撤回", example = "0")
    private UserCouponStatusEnum status;

}
