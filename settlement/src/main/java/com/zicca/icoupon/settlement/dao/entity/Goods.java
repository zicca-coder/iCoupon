package com.zicca.icoupon.settlement.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zicca.icoupon.settlement.common.database.BaseDO;
import com.zicca.icoupon.settlement.common.enums.CouponSupportTypeEnum;
import lombok.*;

import java.math.BigDecimal;

/**
 * 商品信息
 *
 * @author zicca
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_goods")
public class Goods extends BaseDO {

    /**
     * 商品ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 商品名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 商品描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 店铺ID
     */
    @TableField(value = "shop_id")
    private Long shopId;

    /**
     * 店铺名称
     */
    @TableField(value = "shop_name")
    private String shopName;

    /**
     * 价格
     */
    @TableField(value = "price")
    private BigDecimal price;

    /**
     * 原价（划线价）
     */
    @TableField(value = "original_price")
    private BigDecimal originalPrice;

    /**
     * 库存
     */
    @TableField(value = "stock")
    private Integer stock;

    /**
     * 销量
     */
    @TableField(value = "sales_count")
    private Integer salesCount;

    /**
     * 优惠券可用状态：0-不可用 1-可用单张 2-可用多张
     */
    @TableField(value = "coupon_support_type")
    private CouponSupportTypeEnum couponSupportType;

}
