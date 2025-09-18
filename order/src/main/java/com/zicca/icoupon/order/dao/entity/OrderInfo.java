package com.zicca.icoupon.order.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zicca.icoupon.order.common.database.BaseDO;
import com.zicca.icoupon.order.common.enums.OrderStatusEnum;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单实体
 *
 * @author zicca
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_order_info")
public class OrderInfo extends BaseDO {

    /**
     * 订单 ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 订单编号
     */
    @TableField(value = "order_no")
    private String orderNo;

    /**
     * 用户 ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 店铺 ID
     */
    @TableField(value = "shop_id")
    private Long shopId;

    /**
     * 订单总金额
     */
    @TableField(value = "total_amount")
    private BigDecimal totalAmount;

    /**
     * 优惠金额
     */
    @TableField(value = "discount_amount")
    private BigDecimal discountAmount;

    /**
     * 实际支付金额
     */
    @TableField(value = "pay_amount")
    private BigDecimal payAmount;

    /**
     * 订单状态：0：待支付，1：已支付，2：已取消，3：已完成，4：待发货，5：已发货，6：待收货，7：已关闭，8：退款中，9：已退款
     */
    @TableField(value = "status")
    private OrderStatusEnum status;

    /**
     * 支付时间
     */
    @TableField(value = "pay_time")
    private Date payTime;

    /**
     * 取消时间
     */
    @TableField(value = "cancel_time")
    private Date cancelTime;
}
