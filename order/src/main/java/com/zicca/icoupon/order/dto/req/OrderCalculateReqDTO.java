package com.zicca.icoupon.order.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 订单计算请求参数
 *
 * @author zicca
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "订单生成请求参数")
public class OrderCalculateReqDTO {

    @Schema(description = "用户 ID", example = "1L")
    private Long userId;

    @Schema(description = "店铺 ID", example = "1L")
    private Long shopId;

    @Schema(description = "订单项列表")
    List<OrderItemReqDTO> orderItemList;

}
