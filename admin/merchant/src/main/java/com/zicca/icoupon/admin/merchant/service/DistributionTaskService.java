package com.zicca.icoupon.admin.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zicca.icoupon.admin.merchant.dao.entity.DistributionTask;
import com.zicca.icoupon.admin.merchant.dto.req.DistributionTaskCreateReqDTO;
import com.zicca.icoupon.admin.merchant.dto.resp.DistributionTaskQueryRespDTO;

import java.util.List;

/**
 * 分发任务服务
 *
 * @author zicca
 */
public interface DistributionTaskService extends IService<DistributionTask> {

    /**
     * 创建分发任务
     *
     * @param requestParam 请求参数
     */
    void createDistributionTask(DistributionTaskCreateReqDTO requestParam);

    /**
     * 查询所有分发任务
     *
     * @return 分发任务列表
     */
    List<DistributionTaskQueryRespDTO> getAllDistributionTasks();

    /**
     * 查询分发任务
     *
     * @param id 分发任务ID
     * @return 分发任务
     */
    DistributionTaskQueryRespDTO getDistributionTaskById(Long id);

    /**
     * 删除分发任务
     *
     * @param id 分发任务 ID
     */
    void deleteDistributionTaskById(Long id);

}
