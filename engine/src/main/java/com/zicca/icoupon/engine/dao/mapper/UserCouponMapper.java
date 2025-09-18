package com.zicca.icoupon.engine.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zicca.icoupon.engine.common.enums.UserCouponStatusEnum;
import com.zicca.icoupon.engine.dao.entity.UserCoupon;
import com.zicca.icoupon.engine.dto.req.UserCouponQueryReqDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户优惠券 Mapper
 *
 * @author zicca
 */
public interface UserCouponMapper extends BaseMapper<UserCoupon> {


    /**
     * 插入用户优惠券
     *
     * @param userCoupon 用户优惠券实体
     * @return 插入数量
     */
    int insertUserCoupon(UserCoupon userCoupon);

    /**
     * 批量插入用户优惠券
     *
     * @param userCoupons 用户优惠券实体列表
     * @return 插入数量
     */
    int batchInsertUserCoupon(@Param("userCoupons") List<UserCoupon> userCoupons);

    /**
     * 查询用户优惠券
     *
     * @param id     主键 ID
     * @param userId 用户 ID
     * @return 用户优惠券
     */
    UserCoupon selectUserCouponById(@Param("id") Long id, @Param("userId") Long userId);

    /**
     * 更新用户优惠券状态
     *
     * @param id     主键 ID
     * @param shopId 店铺 ID
     * @param status 状态
     * @return 更新数量
     */
    int updateStatusById(@Param("id") Long id, @Param("shopId") Long shopId, @Param("status") UserCouponStatusEnum status);

    /**
     * 查询用户领取指定优惠券模板的数量
     *
     * @param couponTemplateId 优惠券模板 ID
     * @param userId           用户 ID
     * @return 领取数量
     */
    int selectCountByCouponTemplateIdAndUserId(@Param("couponTemplateId") Long couponTemplateId,
                                               @Param("userId") Long userId);

    /**
     * 查询用户领取指定优惠券模板且未使用的数量
     *
     * @param couponTemplateId 优惠券模板 ID
     * @param userId           用户 ID
     * @return 未使用数量
     */
    int selectUnusedCountByCouponTemplateIdAndUserId(@Param("couponTemplateId") Long couponTemplateId,
                                                     @Param("userId") Long userId);

    /**
     * 根据条件查询用户优惠券列表
     *
     * @param condition 条件
     * @return 用户优惠券列表
     */
    List<UserCoupon> selectUserCouponListByCondition(@Param("condition") UserCouponQueryReqDTO condition);

    List<UserCoupon> selectAvailableUserCouponList(@Param("userId") Long userId);

    /**
     * 更新用户优惠券状态
     *
     * @param id     主键 ID
     * @param userId 用户 ID
     * @param status 状态
     * @return 更新数量
     */
    int updateStatusByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId, @Param("status") UserCouponStatusEnum status);

    /**
     * 批量更新用户优惠券状态
     *
     * @param ids    主键 ID 列表
     * @param userId 用户 ID
     * @param status 状态
     * @return 更新数量
     */
    int updateStatusByIdsAndUserId(@Param("ids") List<Long> ids, @Param("userId") Long userId, @Param("status") UserCouponStatusEnum status);

}
