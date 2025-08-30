package com.zicca.icoupon.distribution.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zicca.icoupon.distribution.common.enums.DistributionTaskStatusEnum;
import com.zicca.icoupon.distribution.dao.entity.DistributionTask;
import org.apache.ibatis.annotations.Param;


/**
 * 优惠券任务表 Mapper 接口
 *
 * @author zicca
 */
public interface DistributionTaskMapper extends BaseMapper<DistributionTask> {


    /**
     * 更新任务状态
     *
     * @param id     任务 id
     * @param shopId 店铺 id
     * @param status 任务状态
     */
    void updateStatusById(@Param("id") Long id,
                          @Param("shop_id") Long shopId,
                          @Param("status") DistributionTaskStatusEnum status);

}
