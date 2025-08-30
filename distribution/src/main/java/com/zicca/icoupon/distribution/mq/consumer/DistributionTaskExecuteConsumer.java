package com.zicca.icoupon.distribution.mq.consumer;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson2.JSON;
import com.zicca.icoupon.distribution.common.enums.DistributionTaskTypeEnum;
import com.zicca.icoupon.distribution.dao.entity.CouponTemplate;
import com.zicca.icoupon.distribution.dao.entity.DistributionTask;
import com.zicca.icoupon.distribution.dao.mapper.CouponTemplateMapper;
import com.zicca.icoupon.distribution.dao.mapper.DistributionRecordMapper;
import com.zicca.icoupon.distribution.dao.mapper.DistributionTaskMapper;
import com.zicca.icoupon.distribution.dao.mapper.UserCouponMapper;
import com.zicca.icoupon.distribution.mq.base.MessageWrapper;
import com.zicca.icoupon.distribution.mq.event.DistributionTaskExecuteEvent;
import com.zicca.icoupon.distribution.service.basics.excel.DistributionInfoObject;
import com.zicca.icoupon.distribution.service.basics.excel.DistributionInfoReadListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import static com.zicca.icoupon.distribution.common.constants.MQConstants.DISTRIBUTION_TASK_EXECUTE_CG;
import static com.zicca.icoupon.distribution.common.constants.MQConstants.DISTRIBUTION_TASK_EXECUTE_TOPIC;
import static com.zicca.icoupon.distribution.common.enums.CouponTemplateStatusEnum.CANCELED;
import static com.zicca.icoupon.distribution.common.enums.CouponTemplateStatusEnum.END;
import static com.zicca.icoupon.distribution.common.enums.DistributionTaskStatusEnum.IN_PROGRESS;
import static com.zicca.icoupon.distribution.common.enums.DistributionTaskStatusEnum.NOT_STARTED;
import static com.zicca.icoupon.distribution.common.enums.DistributionTaskTypeEnum.IMMEDIATELY;
import static com.zicca.icoupon.distribution.common.enums.DistributionTaskTypeEnum.TIMING;

/**
 * 分发任务执行消息消费者
 * {@link com.zicca.icoupon.admin.merchant.mq.producer.DistributionTaskExecuteProducer}
 *
 * @author zicca
 */
@Slf4j
@Component
@RequiredArgsConstructor
@RocketMQMessageListener(
        topic = DISTRIBUTION_TASK_EXECUTE_TOPIC,
        consumerGroup = DISTRIBUTION_TASK_EXECUTE_CG
)
public class DistributionTaskExecuteConsumer implements RocketMQListener<MessageWrapper<DistributionTaskExecuteEvent>> {

    private final DistributionTaskMapper distributionTaskMapper;
    private final CouponTemplateMapper couponTemplateMapper;
    private final UserCouponMapper userCouponMapper;
    private final DistributionRecordMapper distributionRecordMapper;
    private final PlatformTransactionManager transactionManager;

    @Override
    public void onMessage(MessageWrapper<DistributionTaskExecuteEvent> messageWrapper) {
        log.info("[消费者] 优惠券分发任务正式执行 - 执行消费逻辑，消息体：{}", JSON.toJSONString(messageWrapper));
        long start = System.currentTimeMillis();
        try {
            DistributionTaskExecuteEvent event = messageWrapper.getMessage();
            Long distributionTaskId = event.getDistributionTaskId();

            // 1. 验证分发任务有效性
            DistributionTask distributionTask = validateDistributionTask(distributionTaskId, event.getType());
            if (distributionTask == null) {
                return;
            }

            // 2. 验证优惠券模板有效性
            CouponTemplate template = validateCouponTemplate(distributionTask);
            if (template == null) {
                return;
            }

            // 3. 更新任务状态（仅针对定时任务）
            if (TIMING == event.getType()) {
                distributionTaskMapper.updateStatusById(distributionTaskId, distributionTask.getShopId(), IN_PROGRESS);
            }

            // 4. 执行分发任务
            executeDistributionTask(distributionTask, template);
            long end = System.currentTimeMillis();
            log.info("[消费者] 优惠券分发任务执行完成 - 任务ID：{} - 共耗时：{} s", distributionTaskId, (end - start) / 1000);
        } catch (Exception e) {
            log.error("[消费者] 优惠券分发任务执行异常 - 消息体：{}", JSON.toJSONString(messageWrapper), e);
            // 可以根据业务需要决定是否重新入队或记录失败状态
        }
    }

    /**
     * 验证分发任务
     *
     * @param distributionTaskId 分发任务 ID
     * @param taskType           任务类型
     * @return 分发任务
     */
    private DistributionTask validateDistributionTask(Long distributionTaskId, DistributionTaskTypeEnum taskType) {
        DistributionTask distributionTask = distributionTaskMapper.selectById(distributionTaskId);
        if (distributionTask == null) {
            log.warn("[消费者] 优惠券分发任务正式执行 - 分发任务不存在，已终止优惠券分发");
            return null;
        }

        // 状态校验
        boolean statusValid = switch (taskType) {
            case IMMEDIATELY -> IN_PROGRESS == distributionTask.getStatus();
            case TIMING -> NOT_STARTED == distributionTask.getStatus();
            default -> false;
        };

        if (!statusValid) {
            log.warn("[消费者] 优惠券分发任务正式执行 - 分发任务状态异常：{}，已终止优惠券分发", distributionTask.getStatus());
            return null;
        }

        return distributionTask;
    }

    /**
     * 验证优惠券模板
     *
     * @param distributionTask 分发任务
     * @return 优惠券模板
     */
    private CouponTemplate validateCouponTemplate(DistributionTask distributionTask) {
        CouponTemplate template = couponTemplateMapper.getCouponTemplateById(
                distributionTask.getCouponTemplateId(), distributionTask.getShopId());

        if (template == null) {
            log.warn("[消费者] 优惠券分发任务正式执行 - 优惠券模板不存在，已终止优惠券分发");
            return null;
        }

        // 已结束/作废的优惠券模板不可进行分发
        if (END == template.getStatus() || CANCELED == template.getStatus()) {
            log.warn("[消费者] 优惠券分发任务正式执行 - 优惠券模板状态异常：{}，已终止优惠券分发", template.getStatus());
            return null;
        }

        return template;
    }

    /**
     * 执行分发任务
     *
     * @param distributionTask 分发任务
     */
    private void executeDistributionTask(DistributionTask distributionTask, CouponTemplate couponTemplate) {
        EasyExcel.read(
                        distributionTask.getFileAddress(),
                        DistributionInfoObject.class,
                        new DistributionInfoReadListener(
                                couponTemplate,
                                distributionTask,
                                couponTemplateMapper,
                                userCouponMapper,
                                distributionRecordMapper,
                                transactionManager
                        ))
                .sheet()
                .doRead();
    }


}
