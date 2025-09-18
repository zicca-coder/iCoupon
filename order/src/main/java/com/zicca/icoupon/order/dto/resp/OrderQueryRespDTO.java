package com.zicca.icoupon.order.dto.resp;

import com.zicca.icoupon.order.common.enums.OrderStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单查询响应参数
 *
 * @author zicca
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "订单查询响应参数")
public class OrderQueryRespDTO {

    @Schema(description = "订单ID", example = "1L")
    private Long id;

    @Schema(description = "订单编号", example = "2023010100000001")
    private String orderNo;

    @Schema(description = "用户ID", example = "1L")
    private Long userId;

    @Schema(description = "店铺ID", example = "1L")
    private Long shopId;

    @Schema(description = "订单总金额", example = "10.00")
    private BigDecimal totalAmount;

    @Schema(description = "优惠金额", example = "10.00")
    private BigDecimal discountAmount;

    @Schema(description = "支付金额", example = "10.00")
    private BigDecimal payAmount;

    @Schema(description = "订单状态：0-待支付 1-已支付 2-已取消 3-已完成 4-待发货 5-已发货 6-待收货 7-已关闭 8-退款中 9-已退款", example = "0")
    private OrderStatusEnum status;

    @Schema(description = "支付时间", example = "2023-01-01 00:00:00")
    private Date payTime;

    @Schema(description = "取消时间", example = "2023-01-01 00:00:00")
    private Date cancelTime;
}
