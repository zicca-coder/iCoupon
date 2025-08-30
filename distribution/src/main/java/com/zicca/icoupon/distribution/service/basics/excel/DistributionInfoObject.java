package com.zicca.icoupon.distribution.service.basics.excel;

import lombok.Data;

/**
 * 分发信息对象 | 分发任务 Excel 元数据实体
 *
 * @author zicca
 */
@Data
public class DistributionInfoObject {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String mail;

//    /**
//     * 微信号
//     */
//    private String wechat;


}
