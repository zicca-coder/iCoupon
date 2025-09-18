package com.zicca.icoupon.order.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zicca.icoupon.order.common.enums.OrderStatusEnum;
import com.zicca.icoupon.order.dao.entity.OrderInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单信息表 Mapper 接口
 *
 * @author zicca
 */
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    /**
     * 根据订单id查询订单信息
     *
     * @param id 订单id
     * @return 订单信息
     */
    OrderInfo selectOrderInfoById(@Param("id") Long id);

    /**
     * 根据订单编号查询订单信息
     *
     * @param orderNo 订单编号
     * @return 订单信息
     */
    OrderInfo selectOrderInfoByOrderNo(@Param("orderNo") String orderNo);

    /**
     * 根据用户id和订单编号查询订单信息
     *
     * @param userId  用户id
     * @param orderNo 订单编号
     * @return 订单信息
     */
    OrderInfo selectOrderInfoByUserIdAndOrderNo(@Param("userId") Long userId, @Param("orderNo") String orderNo);

    /**
     * 根据id和用户id查询订单信息
     *
     * @param id     订单id
     * @param userId 用户id
     * @return 订单信息
     */
    OrderInfo selectOrderInfoByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    /**
     * 根据id列表查询订单信息
     *
     * @param ids id列表
     * @return 订单信息
     */
    List<OrderInfo> selectOrderInfoListByIds(@Param("ids") List<Long> ids);

    /**
     * 根据id列表和用户id查询订单信息
     *
     * @param ids    id列表
     * @param userId 用户id
     * @return 订单信息
     */
    List<OrderInfo> selectOrderInfoListByIdsAndUserId(@Param("ids") List<Long> ids, @Param("userId") Long userId);

    /**
     * 根据用户id查询订单信息
     *
     * @param userId 用户id
     * @return 订单信息
     */
    List<OrderInfo> selectOrderInfoListByUserId(@Param("userId") Long userId);

    /**
     * 根据用户id和订单状态查询订单信息
     *
     * @param userId 用户id
     * @param status 订单状态
     * @return 订单信息
     */
    List<OrderInfo> selectOrderInfoListByUserIdAndStatus(@Param("userId") Long userId, @Param("status") OrderStatusEnum status);

    /**
     * 根据用户id和订单状态查询订单数量
     *
     * @param userId 用户id
     * @param status 订单状态
     * @return 订单数量
     */
    List<OrderInfo> selectCountByUserIdAndStatus(@Param("userId") Long userId, @Param("status") OrderStatusEnum status);

    /**
     * 插入订单信息
     *
     * @param orderInfo 订单信息
     * @return 影响行数
     */
    int insertOrderInfo(OrderInfo orderInfo);

    /**
     * 更新订单信息
     *
     * @param id        订单id
     * @param orderInfo 订单信息
     * @return 影响行数
     */
    int updateOrderInfoById(@Param("id") Long id, @Param("orderInfo") OrderInfo orderInfo);

    /**
     * 更新订单信息
     *
     * @param orderNo   订单编号
     * @param orderInfo 订单信息
     * @return 影响行数
     */
    int updateOrderInfoByOrderNo(@Param("orderNo") String orderNo, @Param("orderInfo") OrderInfo orderInfo);

    /**
     * 更新订单信息
     *
     * @param userId    用户id
     * @param orderNo   订单编号
     * @param orderInfo 订单信息
     * @return 影响行数
     */
    int updateOrderInfoByUserIdAndOrderNo(@Param("userId") Long userId, @Param("orderNo") String orderNo, @Param("orderInfo") OrderInfo orderInfo);

    /**
     * 更新订单信息
     *
     * @param id        订单id
     * @param userId    用户id
     * @param orderInfo 订单信息
     * @return 影响行数
     */
    int updateOrderInfoByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId, @Param("orderInfo") OrderInfo orderInfo);

    /**
     * 删除订单信息
     *
     * @param id 订单id
     * @return 影响行数
     */
    int deleteOrderInfoById(@Param("id") Long id);

    /**
     * 删除订单信息
     *
     * @param orderNo 订单编号
     * @return 影响行数
     */
    int deleteOrderInfoByOrderNo(@Param("orderNo") String orderNo);

    /**
     * 删除订单信息
     *
     * @param id     订单id
     * @param userId 用户id
     * @return 影响行数
     */
    int deleteOrderInfoByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    /**
     * 批量删除订单信息
     *
     * @param ids id列表
     * @return 影响行数
     */
    int deleteOrderInfoByIds(@Param("ids") List<Long> ids);

    /**
     * 批量删除订单信息
     *
     * @param ids    id列表
     * @param userId 用户id
     * @return 影响行数
     */
    int deleteOrderInfoByIdsAndUserId(@Param("ids") List<Long> ids, @Param("userId") Long userId);

    /**
     * 更新订单状态
     *
     * @param id     订单id
     * @param status 订单状态
     * @return 影响行数
     */
    int updateStatusById(@Param("id") Long id, @Param("status") OrderStatusEnum status);

    /**
     * 更新订单状态
     *
     * @param orderNo 订单编号
     * @param status  订单状态
     * @return 影响行数
     */
    int updateStatusByOrderNo(@Param("orderNo") String orderNo, @Param("status") OrderStatusEnum status);

    /**
     * 更新订单状态
     *
     * @param id     订单id
     * @param userId 用户id
     * @param status 订单状态
     * @return 影响行数
     */
    int updateStatusByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId, @Param("status") OrderStatusEnum status);

}
