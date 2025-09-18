package com.zicca.icoupon.order.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zicca.icoupon.order.common.database.BaseDO;
import lombok.*;

import java.math.BigDecimal;

/**
 * 订单商品实体
 *
 * @author zicca
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_order_item")
public class OrderItem extends BaseDO {

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
     * 用户 ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 商品 ID
     */
    @TableField(value = "goods_id")
    private Long goodsId;

    /**
     * 商品名称
     */
    @TableField(value = "goods_name")
    private String goodsName;

    /**
     * 商品单价
     */
    @TableField(value = "price")
    private BigDecimal price;

    /**
     * 商品原价
     */
    @TableField(value = "original_price")
    private BigDecimal originalPrice;

    /**
     * 商品数量
     */
    @TableField(value = "quantity")
    private Integer quantity;

    /**
     * 小计金额（商品单价 * 商品数量）
     */
    @TableField(value = "subtotal_amount")
    private BigDecimal subtotalAmount;

    /**
     * 实际支付金额
     */
    @TableField(value = "pay_amount")
    private BigDecimal payAmount;

    /**
     * 优惠金额
     */
    @TableField(value = "discount_amount")
    private BigDecimal discountAmount;

}
