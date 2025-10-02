package com.zicca.icoupon.coupon.mq.consumer;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zicca.icoupon.coupon.common.exception.EntityInsertException;
import com.zicca.icoupon.coupon.common.exception.StockUpdateException;
import com.zicca.icoupon.coupon.dao.entity.UserCoupon;
import com.zicca.icoupon.coupon.dao.mapper.CouponTemplateMapper;
import com.zicca.icoupon.coupon.dao.mapper.UserCouponMapper;
import com.zicca.icoupon.coupon.mq.base.MessageWrapper;
import com.zicca.icoupon.coupon.mq.event.StockUpdateEvent;
import com.zicca.icoupon.coupon.service.basics.cache.UserCouponRedisService;
import com.zicca.icoupon.framework.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Date;

import static com.zicca.icoupon.coupon.common.constants.MQConstants.COUPON_TEMPLATE_STOCK_UPDATE_CG;
import static com.zicca.icoupon.coupon.common.constants.MQConstants.COUPON_TEMPLATE_STOCK_UPDATE_TOPIC;
import static com.zicca.icoupon.coupon.common.constants.RedisConstant.COUPON_TEMPLATE_KEY;
import static com.zicca.icoupon.coupon.common.enums.UserCouponStatusEnum.NOT_USED;

/**
 * 库存更新消息消费者
 *
 * @author zicca
 */
@Slf4j
@Component
@RequiredArgsConstructor
@RocketMQMessageListener(
        topic = COUPON_TEMPLATE_STOCK_UPDATE_TOPIC,
        consumerGroup = COUPON_TEMPLATE_STOCK_UPDATE_CG
)
public class StockUpdateConsumer implements RocketMQListener<MessageWrapper<StockUpdateEvent>> {

    private final CouponTemplateMapper couponTemplateMapper;

    private final UserCouponMapper userCouponMapper;

    private final StringRedisTemplate stringRedisTemplate;

    private final UserCouponRedisService userCouponRedisService;

    private final TransactionTemplate transactionTemplate;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void onMessage(MessageWrapper<StockUpdateEvent> messageWrapper) {
        log.debug("[消费者] 优惠券模板库存更新 - 执行消费逻辑，消息体：{}", JSON.toJSONString(messageWrapper));
        StockUpdateEvent message = messageWrapper.getMessage();
        switch (message.getType()) {
            case USER_RECEIVE_COUPON -> handleUserCouponReceiveEvent(message.getUserCouponReceiveEvent());
            case MERCHANT_UPDATE_STOCK -> handleStockUpdateEvent(message.getCouponTemplateStockUpdateEvent());
            default -> {
                return;
            }
        }
        log.info("[消费者] 优惠券模板库存更新 - 执行消费逻辑，消费完成");
    }


    private void handleStockUpdateEvent(StockUpdateEvent.CouponTemplateStockUpdateEvent event) {
        log.info("[消费者] 优惠券模板库存更新 - 商家/后台更新优惠券库存，执行消费逻辑");
        String key = COUPON_TEMPLATE_KEY + event.getCouponTemplateId();
        if (event.isAdd()) {
            couponTemplateMapper.increaseNumberCouponTemplate(event.getCouponTemplateId(), event.getShopId(), event.getNumber());
            log.debug("[消费者] 优惠券模板库存更新 - 执行消费逻辑，增加DB库存成功，key={}, number={}", key, event.getNumber());
            stringRedisTemplate.opsForHash().increment(key, "stock", event.getNumber());
            log.debug("[消费者] 优惠券模板库存更新 - 执行消费逻辑，增加缓存库存成功，key={}, number={}", key, event.getNumber());
            return;
        }
        couponTemplateMapper.decreaseNumberCouponTemplate(event.getCouponTemplateId(), event.getShopId(), event.getNumber());
        log.debug("[消费者] 优惠券模板库存更新 - 执行消费逻辑，减少DB库存成功，key={}, number={}", key, event.getNumber());
        stringRedisTemplate.opsForHash().increment(key, "stock", -event.getNumber());
        log.debug("[消费者] 优惠券模板库存更新 - 执行消费逻辑，减少缓存库存成功，key={}, number={}", key, event.getNumber());
    }

    private void handleUserCouponReceiveEvent(StockUpdateEvent.UserCouponReceiveEvent event) {
        log.info("[消费者] 优惠券模板库存更新 - 用户领取优惠券，执行消费逻辑");
        UserCoupon userCoupon = bulidUserCoupon(event);
        String key = COUPON_TEMPLATE_KEY + event.getCouponTemplateId();

        transactionTemplate.executeWithoutResult(status -> {
            try {
                // 扣减库存
                int decremented = couponTemplateMapper.decreaseNumberCouponTemplate(event.getCouponTemplateId(), event.getShopId(), 1);
                if (!SqlHelper.retBool(decremented)) {
                    log.warn("[消费者] 用户领取优惠券，用户领取优惠券事件 - 执行消费逻辑，扣减优惠券数据库库存失败，消息体：{}", JSON.toJSONString(event));
                    throw new StockUpdateException("用户领取优惠券，数据库扣减库存异常");
                }
                int stock = couponTemplateMapper.selectStockById(event.getCouponTemplateId(), event.getShopId());
                stringRedisTemplate.opsForHash().put(key, "stock", String.valueOf(stock));

                int inserted = userCouponMapper.insertUserCoupon(userCoupon);
                if (!SqlHelper.retBool(inserted)) {
                    log.warn("[消费者] 用户领取优惠券，用户领取优惠券事件 - 执行消费逻辑，插入用户优惠券数据库失败，消息体：{}", JSON.toJSONString(event));
                    throw new EntityInsertException("用户领取优惠券，数据库插入用户优惠券异常");
                }
                userCouponRedisService.addUserCoupon(userCoupon);
            } catch (StockUpdateException | EntityInsertException ignored) {
                log.error("[消费者] 用户领取优惠券，用户领取优惠券事件 - 执行消费逻辑，数据库操作异常，消息体：{}", JSON.toJSONString(event));
            } catch (Exception e) {
                status.setRollbackOnly();
                // 回滚redis
                userCouponRedisService.decrementAndSaveUserReceiveRollback(event.getUserId(), event.getCouponTemplate());
                log.warn("[消费者] 用户领取优惠券，用户领取优惠券事件 - 执行消费逻辑，数据库操作失败，消息体：{}", JSON.toJSONString(event));
                // 这里抛出异常暂时注释掉，避免服务重启后消费端一直尝试重复消费
                // 注：这里取消注释后，服务端重复消费，但是redis已经回滚库存和用户领取限制了，需要额外处理
//                throw new ServiceException("优惠券领取异常，请稍后再试");
            }
        });
    }


    private UserCoupon bulidUserCoupon(StockUpdateEvent.UserCouponReceiveEvent event) {
        return UserCoupon.builder()
                .userId(event.getUserId())
                .couponTemplateId(event.getCouponTemplateId())
                .shopId(event.getShopId())
                .target(event.getCouponTemplate().getTarget())
                .type(event.getCouponTemplate().getType())
                .faceValue(event.getCouponTemplate().getFaceValue())
                .minAmount(event.getCouponTemplate().getMinAmount())
                .receiveTime(new Date())
                .validStartTime(event.getCouponTemplate().getValidStartTime())
                .validEndTime(event.getCouponTemplate().getValidEndTime())
                .status(NOT_USED)
                .build();
    }


}
