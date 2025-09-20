package com.zicca.icoupon.coupon.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zicca.icoupon.coupon.common.database.BaseDO;
import com.zicca.icoupon.coupon.common.enums.DiscountTargetEnum;
import com.zicca.icoupon.coupon.common.enums.DiscountTypeEnum;
import com.zicca.icoupon.coupon.common.enums.UserCouponStatusEnum;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户优惠券实体 | 店铺优惠券
 *
 * @author zicca
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_user_coupon")
public class UserCoupon extends BaseDO {

    /**
     * 主键 ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户 ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 优惠券模板 ID
     */
    @TableField(value = "coupon_template_id")
    private Long couponTemplateId;

    /**
     * 店铺编号
     * 如果是店铺券：存储当前店铺编号
     */
    @TableField(value = "shop_id")
    private Long shopId;

    /**
     * 优惠对象:
     * 0：指定商品可用，1：全店通用
     */
    @TableField(value = "target")
    private DiscountTargetEnum target;

    /**
     * 优惠类型：
     * 0：立减券 1：满减券 2：折扣券 3：随机券
     */
    @TableField(value = "type")
    private DiscountTypeEnum type;

    /**
     * 立减券：立减金额
     * 满减券：满足条件后立减的金额
     * 折扣券：折扣率
     * 随机券：随机金额
     */
    @TableField(value = "face_value")
    private BigDecimal faceValue;

    /**
     * 满减券门槛金额（仅满减券有效）
     */
    @TableField(value = "min_amount")
    private BigDecimal minAmount;

    /**
     * 领取时间
     */
    @TableField(value = "receive_time")
    private Date receiveTime;

    /**
     * 使用时间
     */
    @TableField(value = "use_time")
    private Date useTime;

    /**
     * 有效期开始时间
     */
    @TableField(value = "valid_start_time")
    private Date validStartTime;

    /**
     * 有效期结束时间
     */
    @TableField(value = "valid_end_time")
    private Date validEndTime;

    /**
     * 优惠券使用状态：
     * 0-未使用 1-锁定 2-已使用 3-已过期 4-已撤回
     */
    @TableField(value = "status")
    private UserCouponStatusEnum status;

}
