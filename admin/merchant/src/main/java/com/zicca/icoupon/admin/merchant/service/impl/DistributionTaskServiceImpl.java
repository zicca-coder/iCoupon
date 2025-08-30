package com.zicca.icoupon.admin.merchant.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zicca.icoupon.admin.merchant.common.context.UserContext;
import com.zicca.icoupon.admin.merchant.dao.entity.DistributionTask;
import com.zicca.icoupon.admin.merchant.dao.mapper.DistributionTaskMapper;
import com.zicca.icoupon.admin.merchant.dto.req.DistributionTaskCreateReqDTO;
import com.zicca.icoupon.admin.merchant.dto.resp.CouponTemplateQueryRespDTO;
import com.zicca.icoupon.admin.merchant.dto.resp.DistributionTaskQueryRespDTO;
import com.zicca.icoupon.admin.merchant.mq.event.DistributionTaskExecuteEvent;
import com.zicca.icoupon.admin.merchant.mq.producer.DistributionTaskExecuteProducer;
import com.zicca.icoupon.admin.merchant.service.CouponTemplateService;
import com.zicca.icoupon.admin.merchant.service.DistributionTaskService;
import com.zicca.icoupon.admin.merchant.service.basics.excel.RowCountListener;
import com.zicca.icoupon.framework.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.zicca.icoupon.admin.merchant.common.enums.DistributionTaskStatusEnum.IN_PROGRESS;
import static com.zicca.icoupon.admin.merchant.common.enums.DistributionTaskStatusEnum.NOT_STARTED;
import static com.zicca.icoupon.admin.merchant.common.enums.DistributionTaskTypeEnum.IMMEDIATELY;
import static com.zicca.icoupon.admin.merchant.common.enums.DistributionTaskTypeEnum.TIMING;

/**
 * 分发任务服务实现类
 *
 * @author zicca
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DistributionTaskServiceImpl extends ServiceImpl<DistributionTaskMapper, DistributionTask> implements DistributionTaskService {

    private final CouponTemplateService couponTemplateService;
    private final DistributionTaskExecuteProducer distributionTaskExecuteProducer;


    @Override
    public void createDistributionTask(DistributionTaskCreateReqDTO requestParam) {
        long start = System.currentTimeMillis();
        CouponTemplateQueryRespDTO template = couponTemplateService.findCouponTemplateById(requestParam.getCouponTemplateId());
        if (template == null) {
            throw new ServiceException("优惠券模板不存在");
        }
        DistributionTask distributionTask = BeanUtil.copyProperties(requestParam, DistributionTask.class);
        log.info(requestParam.getType().getDesc());
        distributionTask.setShopId(UserContext.getShopId());
        // 根据分发任务类型设置任务状态
        distributionTask.setStatus(
                Objects.equals(requestParam.getType(), TIMING)
                        ? NOT_STARTED
                        : IN_PROGRESS
        );
        RowCountListener listener = new RowCountListener();
        EasyExcel.read(requestParam.getFileAddress(), listener).sheet().doRead();
        distributionTask.setSendNum(listener.getRowCount());

        boolean saved = save(distributionTask);
        if (!saved) {
            throw new ServiceException("新增分发任务失败");
        }
        // 根据分发任务类型：0-立即发送 1-定时发送
        DistributionTaskExecuteEvent event = DistributionTaskExecuteEvent.builder()
                .distributionTaskId(distributionTask.getId())
                .type(requestParam.getType())
                .sendTime(requestParam.getType() == TIMING ? distributionTask.getSendTime().getTime() : null)
                .build();
        distributionTaskExecuteProducer.sendMessage(event);
        long end = System.currentTimeMillis();
        log.info("createDistributionTask: {} ms", (end - start));
    }

    @Override
    public List<DistributionTaskQueryRespDTO> getAllDistributionTasks() {
        List<DistributionTask> tasks = lambdaQuery().eq(DistributionTask::getShopId, UserContext.getShopId()).list();
        if (tasks == null || tasks.isEmpty()) {
            return List.of();
        }
        return tasks.stream()
                .map(task -> BeanUtil.copyProperties(task, DistributionTaskQueryRespDTO.class))
                .toList();
    }

    @Override
    public DistributionTaskQueryRespDTO getDistributionTaskById(Long id) {
        DistributionTask task = lambdaQuery()
                .eq(DistributionTask::getId, id)
                .eq(DistributionTask::getShopId, UserContext.getShopId())
                .one();
        return task == null ? null : BeanUtil.copyProperties(task, DistributionTaskQueryRespDTO.class);
    }

    @Override
    public void deleteDistributionTaskById(Long id) {
        boolean deleted = remove(new LambdaQueryWrapper<DistributionTask>()
                .eq(DistributionTask::getId, id)
                .eq(DistributionTask::getShopId, UserContext.getShopId())
                .ne(DistributionTask::getStatus, IN_PROGRESS));
        if (!deleted) {
            throw new ServiceException("删除任务失败");
        }
    }


}
