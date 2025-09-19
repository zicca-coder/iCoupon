package com.zicca.icoupon.settlement.service.impl;

import com.zicca.icoupon.settlement.dto.req.OrderCalculateReqDTO;
import com.zicca.icoupon.settlement.dto.req.OrderItemReqDTO;
import com.zicca.icoupon.settlement.dto.resp.OrderCalculateRespDTO;
import com.zicca.icoupon.settlement.service.OrderItemCalculateService;
import com.zicca.icoupon.settlement.service.OrderCalculateService;
import com.zicca.icoupon.settlement.service.basic.calculation.PricePair;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zicca.icoupon.settlement.common.enums.CouponSupportTypeEnum.NOT_AVAILABLE;

/**
 * 订单计算服务实现类
 *
 * @author zicca
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderCalculateServiceImpl implements OrderCalculateService {

    private final OrderItemCalculateService orderItemCalculateService;


    @Override
    public OrderCalculateRespDTO calculateOrder(OrderCalculateReqDTO requestParam) {
        log.info("订单计算服务开始计算订单价格和优惠，参数：{}", requestParam);
        OrderCalculateRespDTO orderInfo = new OrderCalculateRespDTO();
        List<OrderItemReqDTO> orderItemList = requestParam.getOrderItemList();
        // 计算每个订单项价格和优惠
        // 首先计算不能使用优惠券的商品
        List<OrderItemReqDTO> noDiscountItems = orderItemList.stream().filter(item -> NOT_AVAILABLE == item.getCouponSupportType()).toList();
        log.info("不能使用优惠券的商品：{}", noDiscountItems);
        List<OrderItemReqDTO> discountItems = orderItemList.stream().filter(item -> NOT_AVAILABLE != item.getCouponSupportType()).toList();
        log.info("可以使用优惠券的商品：{}", discountItems);
        Map<Long, PricePair> itemPriceMap = new HashMap<>();
        Map<Long, List<Long>> itemCouponMap = new HashMap<>();
        noDiscountItems.forEach(item -> {
            itemPriceMap.put(item.getGoodsId(), PricePair.builder()
                    .subtotalAmount(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .payAmount(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .discountAmount(BigDecimal.ZERO)
                    .build());
        });
        // 目前暂定一个商品项只能用一张优惠券
        discountItems.forEach(item -> {
            PricePair pricePair = orderItemCalculateService.calculateItemWithSingleCoupon(item);
            if (pricePair.isDiscount()) {
                // 由于目前暂定一个商品项只能用一张优惠券，所以默认优惠券列表中只有一张优惠券，因此直接添加即可
                itemCouponMap.put(item.getGoodsId(), item.getUserCouponIds());
            }
            itemPriceMap.put(item.getGoodsId(), pricePair);
        });

        // 计算订单总的优惠金额和支付金额
        // 计算所有订单项的总优惠金额
        BigDecimal totalDiscountAmount = itemPriceMap.values().stream()
                .map(PricePair::getDiscountAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        log.info("订单总优惠金额：{}", totalDiscountAmount);
        
        // 计算所有订单项的总支付金额
        BigDecimal totalPayAmount = itemPriceMap.values().stream()
                .map(PricePair::getPayAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        log.info("订单总支付金额：{}", totalPayAmount);

        // 计算订单总金额
        BigDecimal totalAmount = itemPriceMap.values().stream()
                .map(PricePair::getSubtotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        log.info("订单总金额：{}", totalAmount);

        return OrderCalculateRespDTO.builder()
                .totalAmount(totalAmount)
                .payAmount(totalPayAmount)
                .discountAmount(totalDiscountAmount)
                .orderItemPriceInfo(itemPriceMap)
                .orderItemCouponInfo(itemCouponMap)
                .build();
    }


}
