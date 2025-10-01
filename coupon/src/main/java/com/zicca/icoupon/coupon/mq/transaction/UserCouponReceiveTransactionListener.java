package com.zicca.icoupon.coupon.mq.transaction;

import com.zicca.icoupon.coupon.service.basics.cache.UserCouponRedisService;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.messaging.Message;

/**
 * 用户领取优惠券事务监听
 *
 * @author zicca
 */
@RequiredArgsConstructor
@RocketMQTransactionListener
public class UserCouponReceiveTransactionListener implements RocketMQLocalTransactionListener {


    /**
     * 执行本地事务
     *
     * @param msg 消息
     * @param arg 参数
     * @return 状态
     */
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        try {


            // 成功则返回提交状态
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {

            // 失败则返回回滚状态
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    /**
     * 检查本地事务状态
     *
     * @param msg 消息
     * @return 状态
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {



        return null;
    }
}
