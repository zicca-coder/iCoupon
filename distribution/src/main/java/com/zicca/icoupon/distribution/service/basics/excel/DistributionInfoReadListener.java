package com.zicca.icoupon.distribution.service.basics.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.zicca.icoupon.distribution.dao.entity.CouponTemplate;
import com.zicca.icoupon.distribution.dao.entity.DistributionRecord;
import com.zicca.icoupon.distribution.dao.entity.DistributionTask;
import com.zicca.icoupon.distribution.dao.entity.UserCoupon;
import com.zicca.icoupon.distribution.dao.mapper.CouponTemplateMapper;
import com.zicca.icoupon.distribution.dao.mapper.DistributionRecordMapper;
import com.zicca.icoupon.distribution.dao.mapper.UserCouponMapper;
import com.zicca.icoupon.distribution.service.UserCouponService;
import com.zicca.icoupon.framework.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Date;

import static com.zicca.icoupon.distribution.common.enums.UserCouponStatusEnum.NOT_USED;

/**
 * 分发信息读取监听器
 *
 * @author zicca
 */
@Slf4j
@RequiredArgsConstructor
public class DistributionInfoReadListener extends AnalysisEventListener<DistributionInfoObject> {

    private final CouponTemplate couponTemplate;
    private final DistributionTask distributionTask;
    private final CouponTemplateMapper couponTemplateMapper;
    private final UserCouponMapper userCouponMapper;
    private final DistributionRecordMapper distributionRecordMapper;
    private final PlatformTransactionManager transactionManager;

    private int rowCount = 1;


    @Override
    public void invoke(DistributionInfoObject data, AnalysisContext context) {
        log.info("读取分发信息第{}行数据，给用户：{}分发优惠券", rowCount, data.getUserId());
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        try {
            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    processUserCoupon(data, status);
                }
            });
        } catch (Exception e) {
            log.error("用户：{}事务执行失败", data.getUserId(), e);
        } finally {
            rowCount++;
        }
    }

    private void processUserCoupon(DistributionInfoObject data, TransactionStatus status) {
        try {
            // 扣减库存
            int decreased = couponTemplateMapper.decreaseNumberCouponTemplate(
                    couponTemplate.getId(), couponTemplate.getShopId(), 1);

            if (decreased <= 0) {
                log.warn("[消费者] 优惠券分发任务正式执行 - 优惠券库存不足，用户ID：{}，模板ID：{}",
                        data.getUserId(), couponTemplate.getId());
                status.setRollbackOnly();
                return;
            }

            // 插入分发记录
            try {
                DistributionRecord record = new DistributionRecord(data.getUserId(), distributionTask.getId());
                distributionRecordMapper.insert(record);
            } catch (DuplicateKeyException e) {
                log.warn("用户：{}已领取过该优惠券，忽略本次分发", data.getUserId());
                status.setRollbackOnly();
                return;
            }

            // 插入用户领取优惠券记录
            UserCoupon userCoupon = buildUserCoupon(data);
            userCouponMapper.insert(userCoupon);

            log.info("用户：{}优惠券分发成功", data.getUserId());

        } catch (DuplicateKeyException e) {
            log.warn("用户：{}优惠券记录重复，已存在", data.getUserId());
            status.setRollbackOnly();
        } catch (DataAccessException e) {
            log.error("用户：{}数据库操作失败", data.getUserId(), e);
            status.setRollbackOnly();
        } catch (Exception e) {
            log.error("用户：{}分发优惠券时发生未知错误", data.getUserId(), e);
            status.setRollbackOnly();
            throw new ServiceException("处理用户优惠券失败");
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("Excel文件读取完成，总共处理{}行数据", rowCount - 1);
    }

    private UserCoupon buildUserCoupon(DistributionInfoObject data) {
        UserCoupon userCoupon = UserCoupon.builder()
                .userId(data.getUserId())
                .couponTemplateId(couponTemplate.getId())
                .shopId(couponTemplate.getShopId())
                .target(couponTemplate.getTarget())
                .type(couponTemplate.getType())
                .faceValue(couponTemplate.getFaceValue())
                .minAmount(couponTemplate.getMinAmount())
                .receiveTime(new Date())
                .validStartTime(couponTemplate.getValidStartTime())
                .validEndTime(couponTemplate.getValidEndTime())
                .status(NOT_USED)
                .build();
        // 分发模块无web请求，UserConfig配置的拦截器实现，无法存入当前用户ID
        userCoupon.setCreateBy(couponTemplate.getCreateBy());
        userCoupon.setUpdateBy(couponTemplate.getUpdateBy());
        return userCoupon;
    }
}
