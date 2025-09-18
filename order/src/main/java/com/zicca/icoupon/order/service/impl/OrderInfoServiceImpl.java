package com.zicca.icoupon.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zicca.icoupon.framework.exception.ClientException;
import com.zicca.icoupon.framework.exception.ServiceException;
import com.zicca.icoupon.order.common.context.UserContext;
import com.zicca.icoupon.order.dao.entity.OrderInfo;
import com.zicca.icoupon.order.dao.entity.OrderItem;
import com.zicca.icoupon.order.dao.entity.OrderItemCouponRecord;
import com.zicca.icoupon.order.dao.mapper.OrderInfoMapper;
import com.zicca.icoupon.order.dao.mapper.OrderItemCouponRecordMapper;
import com.zicca.icoupon.order.dao.mapper.OrderItemMapper;
import com.zicca.icoupon.order.dto.req.OrderCalculateReqDTO;
import com.zicca.icoupon.order.dto.req.OrderItemReqDTO;
import com.zicca.icoupon.order.dto.req.OrderGenerateReqDTO;
import com.zicca.icoupon.order.dto.req.UserCouponBathLockReqDTO;
import com.zicca.icoupon.order.dto.resp.OrderCalculateRespDTO;
import com.zicca.icoupon.order.dto.resp.OrderQueryRespDTO;
import com.zicca.icoupon.order.service.OrderCalculateService;
import com.zicca.icoupon.order.service.OrderInfoService;
import com.zicca.icoupon.order.service.UserCouponService;
import com.zicca.icoupon.order.service.basic.calculation.PricePair;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.zicca.icoupon.order.common.enums.OrderStatusEnum.PENDING_PAYMENT;

/**
 * 订单信息服务实现类
 *
 * @author zicca
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {

    private final OrderInfoMapper orderInfoMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderItemCouponRecordMapper orderItemCouponRecordMapper;
    private final OrderCalculateService orderCalculateService;
    private final ConversionService conversionService;
    private final UserCouponService userCouponService;

    @Override
    public OrderQueryRespDTO getOrderById(Long id) {
        if (id == null) {
            throw new ClientException("订单ID不能为空");
        }
        OrderInfo orderInfo = orderInfoMapper.selectOrderInfoByIdAndUserId(id, Long.parseLong(UserContext.getUserId()));
        return BeanUtil.copyProperties(orderInfo, OrderQueryRespDTO.class);
    }

    @Override
    public List<OrderQueryRespDTO> getOrderList() {
        List<OrderInfo> orderInfos = orderInfoMapper.selectOrderInfoListByUserId(Long.parseLong(UserContext.getUserId()));
        if (CollectionUtil.isEmpty(orderInfos)) {
            return List.of();
        }
        return orderInfos.stream()
                .map(orderInfo -> BeanUtil.copyProperties(orderInfo, OrderQueryRespDTO.class))
                .toList();
    }

    @Override
    public void deleteOrderById(Long id) {
        if (id == null) {
            throw new ClientException("订单ID不能为空");
        }
        int i = orderInfoMapper.deleteOrderInfoByIdAndUserId(id, Long.parseLong(UserContext.getUserId()));
        if (i < 1) {
            throw new ServiceException("删除订单失败");
        }
    }

    @Override
    public void deleteOrderList(List<Long> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            throw new ClientException("订单ID不能为空");
        }
        int i = orderInfoMapper.deleteOrderInfoByIdsAndUserId(ids, Long.parseLong(UserContext.getUserId()));
        if (i < ids.size()) {
            throw new ServiceException("删除订单失败");
        }
        log.info("删除订单成功，删除数量：{}", i);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateOrder(OrderGenerateReqDTO requestParam) {
        log.debug("生成订单开始，参数：{}", requestParam);
        // 验证商品信息和库存
        List<OrderItemReqDTO> orderItemList = requestParam.getOrderItemList();
        List<Long> goodsIds = orderItemList.stream().map(OrderItemReqDTO::getGoodsId).toList();
        // 查询商品库存是否充足，暂时不实现，假设商品库存充足

        OrderCalculateReqDTO calculateParam = BeanUtil.copyProperties(requestParam, OrderCalculateReqDTO.class);

        OrderInfo orderInfo = OrderInfo.builder()
                .orderNo(IdUtil.getSnowflake().nextIdStr())
                .userId(requestParam.getUserId())
                .shopId(requestParam.getShopId())
                .status(PENDING_PAYMENT)
                .build();
        // 调用结算服务计算订单价格和优惠
        OrderCalculateRespDTO calculateResult = orderCalculateService.calculateOrder(calculateParam);
        log.debug("订单价格计算结果：{}", calculateResult);
        orderInfo.setTotalAmount(calculateResult.getTotalAmount());
        orderInfo.setPayAmount(calculateResult.getPayAmount());
        orderInfo.setDiscountAmount(calculateResult.getDiscountAmount());

        try {
            int i = orderInfoMapper.insert(orderInfo);
            log.info("创建订单成功{}", orderInfo);
        } catch (Exception e) {
            log.error("创建订单失败", e);
            throw new ServiceException("创建订单失败");
        }
        Long orderId = orderInfo.getId();
        // 构建订单商品项实体
        List<OrderItem> orderItems = new ArrayList<>();
        // 构建订单优惠券记录实体
        List<OrderItemCouponRecord> records = new ArrayList<>();

        orderItemList.forEach(item -> {
            Long orderItemId = IdUtil.getSnowflakeNextId();
            PricePair pair = calculateResult.getOrderItemPriceInfo().get(item.getGoodsId());
            Map<Long, List<Long>> itemCouponPari = calculateResult.getOrderItemCouponInfo();
            OrderItem orderItem = OrderItem.builder()
                    .id(orderItemId)
                    .orderId(orderId)
                    .userId(requestParam.getUserId())
                    .goodsId(item.getGoodsId())
                    .goodsName(item.getGoodsName())
                    .price(item.getPrice())
                    .originalPrice(item.getPrice())
                    .quantity(item.getQuantity())
                    .subtotalAmount(pair.getSubtotalAmount())
                    .payAmount(pair.getPayAmount())
                    .discountAmount(pair.getDiscountAmount())
                    .build();
            log.debug("订单项：{}", orderItem);
            orderItems.add(orderItem);
            List<Long> coupons = itemCouponPari.get(item.getGoodsId());
            if (CollectionUtil.isEmpty(coupons)) {
                return;
            }
            coupons.forEach(userCouponId -> {
                OrderItemCouponRecord record = OrderItemCouponRecord.builder()
                        .orderItemId(orderItemId)
                        .goodsId(item.getGoodsId())
                        .userCouponId(userCouponId)
                        .userId(requestParam.getUserId())
                        .build();
                records.add(record);
            });

        });

        if (CollectionUtil.isNotEmpty(orderItems)) {
            orderItemMapper.batchInsertOrderItem(orderItems);
            log.debug("订单项保存成功：{}", orderItems);
        }
        if (!records.isEmpty()) {
            orderItemCouponRecordMapper.batchInsertRecord(records);
            log.info(records.toString());
        }

        // 调用优惠券引擎服务锁定用户优惠券
        List<Long> userCouponIds = records.stream().map(OrderItemCouponRecord::getUserCouponId).toList();
        UserCouponBathLockReqDTO lockParam = UserCouponBathLockReqDTO.builder().userCouponIds(userCouponIds).userId(requestParam.getUserId()).build();
        userCouponService.batchLockUserCoupon(lockParam);

        // 调用库存服务锁定库存
        // 发送延时消息，30分钟未付款订单取消
    }

    @Override
    public void payOrder() {

    }

    @Override
    public void cancelOrder() {

    }

    @Override
    public void closeOrder() {

    }

    @Override
    public void completeOrder() {

    }
}
