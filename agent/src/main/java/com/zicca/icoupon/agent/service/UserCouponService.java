package com.zicca.icoupon.agent.service;

import com.zicca.icoupon.agent.dto.req.UserCouponReceiveReqDTO;
import com.zicca.icoupon.agent.dto.resp.UserCouponQueryRespDTO;

import java.util.List;

/**
 * 用户优惠券服务
 *
 * @author zicca
 */
public interface UserCouponService {


    /**
     * 用户领取优惠券
     *
     * @param requestParam 用户领取优惠券请求参数
     */
    void receiveCoupon(UserCouponReceiveReqDTO requestParam);


    /**
     * 根据ID查询用户优惠券
     *
     * @param id     用户优惠券 ID
     * @param userId 用户 ID
     * @return 用户优惠券
     */
    UserCouponQueryRespDTO getUserCouponById(Long id, Long userId);


    /**
     * 获取用户可用优惠券
     *
     * @param userId 用户ID
     * @return 可用优惠券列表
     */
    List<UserCouponQueryRespDTO> getAvailableUserCouponList(Long userId);


}
