package com.zicca.icoupon.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zicca.icoupon.order.dao.entity.OrderInfo;
import com.zicca.icoupon.order.dto.req.OrderGenerateReqDTO;
import com.zicca.icoupon.order.dto.resp.OrderQueryRespDTO;

import java.util.List;

/**
 * 订单服务
 *
 * @author zicca
 */
public interface OrderInfoService extends IService<OrderInfo> {


    /**
     * 获取订单
     *
     * @param id 订单 ID
     * @return 订单信息
     */
    OrderQueryRespDTO getOrderById(Long id);

    /**
     * 获取订单列表
     *
     * @return 订单列表
     */
    List<OrderQueryRespDTO> getOrderList();

    /**
     * 删除订单
     *
     * @param id 订单 ID
     */
    void deleteOrderById(Long id);

    /**
     * 批量删除订单
     *
     * @param ids 订单 ID 列表
     */
    void deleteOrderList(List<Long> ids);

    /**
     * 生成订单
     */
    void generateOrder(OrderGenerateReqDTO requestParam);

    /**
     * 支付订单
     */
    void payOrder();

    /**
     * 取消订单
     */
    void cancelOrder();

    /**
     * 关闭订单
     */
    void closeOrder();

    /**
     * 完成订单
     */
    void completeOrder();

}
