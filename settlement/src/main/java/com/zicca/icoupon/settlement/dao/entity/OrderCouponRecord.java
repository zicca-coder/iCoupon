package com.zicca.icoupon.settlement.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zicca.icoupon.settlement.common.database.BaseDO;
import com.zicca.icoupon.settlement.common.enums.OrderCouponStatusEnum;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单优惠券使用记录实体
 *
 * @author zicca
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_order_coupon_record")
public class OrderCouponRecord extends BaseDO {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 订单 ID
     */
    @TableField(value = "order_id")
    private Long orderId;

    /**
     * 用户优惠券 ID
     */
    @TableField(value = "user_coupon_id")
    private Long userCouponId;

    /**
     * 优惠券模板名称
     */
    @TableField(value = "coupon_template_name")
    private String couponTemplateName;

    /**
     * 优惠金额
     */
    @TableField(value = "discount_amount")
    private BigDecimal discountAmount;

    /**
     * 结算单状态
     */
    @TableField(value = "status")
    private OrderCouponStatusEnum status;

    /**
     * 使用时间
     */
    @TableField(value = "used_time")
    private Date usedTime;

    /**
     * 退还时间
     */
    @TableField(value = "refound_time")
    private Date refoundTime;

}
