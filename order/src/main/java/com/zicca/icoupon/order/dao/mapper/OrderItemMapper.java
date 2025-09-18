package com.zicca.icoupon.order.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zicca.icoupon.order.dao.entity.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单项 Mapper 接口
 *
 * @author zicca
 */
public interface OrderItemMapper extends BaseMapper<OrderItem> {

    /**
     * 根据 ID 查询订单项
     *
     * @param id 订单项 ID
     * @return 订单项
     */
    OrderItem selectOrderItemById(@Param("id") Long id);

    /**
     * 根据 ID 和用户 ID 查询订单项
     *
     * @param id     订单项 ID
     * @param userId 用户 ID
     * @return 订单项
     */
    OrderItem selectOrderItemByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    /**
     * 根据 ID 列表查询订单项
     *
     * @param ids 订单项 ID 列表
     * @return 订单项列表
     */
    List<OrderItem> selectOrderItemListByIds(@Param("ids") List<Long> ids);

    /**
     * 根据 ID 列表和用户 ID 查询订单项
     *
     * @param ids    订单项 ID 列表
     * @param userId 用户 ID
     * @return 订单项列表
     */
    List<OrderItem> selectOrderItemListByIdsAndUserId(@Param("ids") List<Long> ids, @Param("userId") Long userId);

    /**
     * 插入订单项
     *
     * @param orderItem 订单项
     * @return 插入数量
     */
    int insertOrderItem(@Param("orderItem") OrderItem orderItem);

    /**
     * 批量插入订单项
     *
     * @param orderItemList 订单项列表
     * @return 插入数量
     */
    int batchInsertOrderItem(@Param("orderItemList") List<OrderItem> orderItemList);

    /**
     * 删除订单项
     *
     * @param id 订单项 ID
     * @return 删除数量
     */
    int deleteOrderItem(@Param("id") Long id);

    /**
     * 删除订单项
     *
     * @param id     订单项 ID
     * @param userId 用户 ID
     * @return 删除数量
     */
    int deleteOrderItemByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    /**
     * 批量删除订单项
     *
     * @param ids 订单项 ID 列表
     * @return 删除数量
     */
    int batchDeleteOrderItem(@Param("ids") List<Long> ids);

    /**
     * 批量删除订单项
     *
     * @param ids    订单项 ID 列表
     * @param userId 用户 ID
     * @return 删除数量
     */
    int batchDeleteOrderItemByIdsAndUserId(@Param("ids") List<Long> ids, @Param("userId") Long userId);

}
