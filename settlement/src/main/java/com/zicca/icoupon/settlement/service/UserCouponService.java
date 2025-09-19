package com.zicca.icoupon.settlement.service;

import com.zicca.icoupon.settlement.dto.req.UserCouponListReqDTO;
import com.zicca.icoupon.settlement.dto.req.UserCouponQueryReqDTO;
import com.zicca.icoupon.settlement.dto.resp.UserCouponQueryRespDTO;

import java.util.List;

/**
 * 用户优惠券模板服务
 *
 * @author zicca
 */
public interface UserCouponService {

    /**
     * 根据ID查询用户优惠券
     *
     * @param id     用户优惠券 ID
     * @param userId 用户 ID
     * @return 用户优惠券
     */
    UserCouponQueryRespDTO getUserCouponById(Long id, Long userId);

    /**
     * 查询用户优惠券列表
     *
     * @param requestParam 查询参数
     * @return 优惠券列表
     */
    List<UserCouponQueryRespDTO> getUserCouponList(UserCouponQueryReqDTO requestParam);


    /**
     * 批量查询用户优惠券列表
     *
     * @param requestParam 查询参数
     * @return 优惠券列表
     */
    List<UserCouponQueryRespDTO> getUserCouponList(List<Long> userCouponIds, Long userId);

    /**
     * 根据状态查询用户优惠券列表
     *
     * @param requestParam 查询参数
     * @return 优惠券列表
     */
    List<UserCouponQueryRespDTO> getUserCouponListByStatus(UserCouponQueryReqDTO requestParam);

    /**
     * 获取用户可用优惠券
     *
     * @param userId 用户ID
     * @return 可用优惠券列表
     */
    List<UserCouponQueryRespDTO> getAvailableUserCouponList(Long userId);
}
