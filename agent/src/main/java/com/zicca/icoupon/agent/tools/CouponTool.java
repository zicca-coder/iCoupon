package com.zicca.icoupon.agent.tools;

import com.zicca.icoupon.agent.common.context.UserContext;
import com.zicca.icoupon.agent.common.enums.CouponTemplateStatusEnum;
import com.zicca.icoupon.agent.common.enums.DiscountTypeEnum;
import com.zicca.icoupon.agent.dto.req.CouponTemplateStatusQueryReqDTO;
import com.zicca.icoupon.agent.dto.req.CouponTemplateTypeQueryReqDTO;
import com.zicca.icoupon.agent.dto.resp.CouponTemplateQueryRespDTO;
import com.zicca.icoupon.agent.dto.resp.ReservationReminderQueryRespDTO;
import com.zicca.icoupon.agent.dto.resp.UserCouponQueryRespDTO;
import com.zicca.icoupon.agent.service.CouponTemplateService;
import com.zicca.icoupon.agent.service.ReservationReminderService;
import com.zicca.icoupon.agent.service.UserCouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 代理工具类
 *
 * @author zicca
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CouponTool {

    private final UserCouponService userCouponService;
    private final CouponTemplateService couponTemplateService;
    private final ReservationReminderService reservationReminderService;

    @Tool(description = "用户询问：我有哪些可用的优惠券？/查看我的可用优惠券。/我有可用的优惠券吗？")
    public List<UserCouponQueryRespDTO> getAvailableUserCouponList() {
        Long userId = UserContext.getUserId();
        log.info("[智能体服务] | 查询用户可可用优惠券列表，userId: {}", userId);
        return userCouponService.getAvailableUserCouponList(userId);
    }


    @Tool(description = "用户询问：查看我的预约提醒。/我预约了哪些抢券提醒？/查询我的领券预约信息。")
    public List<ReservationReminderQueryRespDTO> listReservationReminder() {
        Long userId = UserContext.getUserId();
        log.info("[智能体服务] | 查询用户预约提醒列表，userId: {}", userId);
        return reservationReminderService.listReservationReminder();
    }

    @Tool(description = "用户询问：查看这家店有哪些可以领取的优惠券？/查询这家店有哪些优惠券。/根据店铺ID查询这家店的优惠券信息")
    public List<CouponTemplateQueryRespDTO> listCouponTemplateByShopId(@ToolParam(description = "店铺ID，例如：1810714735922956666") Long shopId) {
        log.info("[智能体服务] | 查询店铺优惠券列表，shopId: {}", shopId);
        return couponTemplateService.listCouponTemplateByShopId(shopId);
    }

    @Tool(description = "用户询问：查看哪家店有哪些特定状态的优惠券？/查询哪家店有哪些（未开始/进行中/已结束）优惠券。/根据优惠券类型（未开始/进行中/已结束）查询这家店的优惠券信息")
    public List<CouponTemplateQueryRespDTO> listCouponTemplateByStatus(@ToolParam(description = "店铺ID，例如：1810714735922956666") Long shopId,
                                                                       @ToolParam(description = "优惠券状态：未开始/进行中/已结束/已作废") String status) {
        CouponTemplateStatusEnum statusEnum;
        try {
            statusEnum = CouponTemplateStatusEnum.fromDesc(status);
        } catch (Exception e) {
            return List.of();
        }
        CouponTemplateStatusQueryReqDTO requestParam = new CouponTemplateStatusQueryReqDTO(shopId, statusEnum);
        log.info("[智能体服务] | 筛选优惠券列表，requestParam: {}", requestParam);
        return couponTemplateService.listCouponTemplateByStatus(requestParam);
    }

    @Tool(description = "根据店铺Id查询有哪几种优惠券类型/这家店铺支持哪几种类型的优惠券？")
    public List<String> getDiscountType(@ToolParam(description = "根据店铺ID和优惠券状态查询优惠券列表请求参数") Long shopId) {
        log.info("[智能体服务] | 获取优惠券类型枚举，shopId: {}", shopId);
        return List.of("立减券", "折扣券", "满减券", "随机券");
    }

    @Tool(description = "根据店铺Id查询有哪几种优惠券状态/这家店铺有哪几种状态的优惠券？")
    public List<String> getCouponTemplateStatus(@ToolParam(description = "根据店铺ID和优惠券状态查询优惠券列表请求参数") Long shopId) {
        log.info("[智能体服务] | 获取优惠券状态枚举，shopId: {}", shopId);
        return List.of("未开始", "进行中", "已结束", "已作废");
    }

    @Tool(description = "用户询问：查看哪家店有哪些特定类型的优惠券？/查询哪家店有哪些（立减券/折扣券/满减券/随机券）优惠券。/根据优惠券类型（立减券/折扣券/满减券/随机券）查询这家店的优惠券信息")
    public List<CouponTemplateQueryRespDTO> listCouponTemplateByType(@ToolParam(description = "店铺ID，例如：1810714735922956666") Long shopId,
                                                                     @ToolParam(description = "优惠券类型：立减券/折扣券/满减券/随机券") String type) {
        DiscountTypeEnum typeEnum;
        try {
            typeEnum= DiscountTypeEnum.getByDesc(type);
        } catch (Exception e) {
            return List.of();
        }
        CouponTemplateTypeQueryReqDTO  param = new CouponTemplateTypeQueryReqDTO(shopId, typeEnum);
        log.info("[智能体服务] | 筛选优惠券列表，requestParam: {}", param);
        return couponTemplateService.listCouponTemplateByType(param);
    }

    @Tool(description = "用户询问：查看哪家店有哪些可以预约的优惠券?")
    public List<CouponTemplateQueryRespDTO> listNotStartCouponTemplate(@ToolParam Long shopId) {
        log.info("[智能体服务] | 筛选可预约优惠券列表，shopId: {}", shopId);
        return couponTemplateService.listNotStartCouponTemplate(shopId);
    }



    // 查询我本周领取的优惠券

    // 帮我查看这张优惠券的详细信息

    // 帮我解释这张优惠券的使用规则



}
