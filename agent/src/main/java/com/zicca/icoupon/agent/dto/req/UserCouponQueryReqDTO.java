package com.zicca.icoupon.agent.dto.req;

import com.zicca.icoupon.agent.common.enums.DiscountTargetEnum;
import com.zicca.icoupon.agent.common.enums.DiscountTypeEnum;
import com.zicca.icoupon.agent.common.enums.UserCouponStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户优惠券查询请求参数
 *
 * @author zicca
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户优惠券查询请求参数")
public class UserCouponQueryReqDTO {

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    @Schema(description = "用户ID", example = "1961811779001393153")
    private Long userId;

    /**
     * 优惠券模板ID（可选，用于筛选指定模板的优惠券）
     */
    @Schema(description = "优惠券模板ID", example = "1960313844890882050")
    private Long couponTemplateId;

    /**
     * 店铺ID（可选，用于筛选指定店铺的优惠券）
     */
    @Schema(description = "店铺ID", example = "1810714735922956666")
    private Long shopId;

    /**
     * 优惠券状态（可选，用于筛选指定状态的优惠券）
     */
    @Schema(description = "优惠券状态", example = "0")
    private UserCouponStatusEnum status;

    /**
     * 优惠券目标（可选，用于筛选指定目标类型的优惠券）
     */
    @Schema(description = "优惠券目标", example = "0")
    private DiscountTargetEnum target;

    /**
     * 优惠券类型（可选，用于筛选指定类型的优惠券）
     */
    @Schema(description = "优惠券类型", example = "0")
    private DiscountTypeEnum type;


}
