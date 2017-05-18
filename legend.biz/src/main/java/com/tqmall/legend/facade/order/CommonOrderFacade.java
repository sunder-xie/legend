package com.tqmall.legend.facade.order;

import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.order.OrderInfo;
import com.tqmall.legend.facade.order.vo.OrderToProxyVo;

import java.util.List;

/**
 * 综合维修单 场景业务Service
 */
public interface CommonOrderFacade {

    /**
     * 工单转委托单获取工单详情接口
     *
     * @param orderId
     * @param shopId
     * @return
     */
    Result<OrderToProxyVo> getProxyOrderInfo(Long orderId, Long shopId);

    /**
     * 开委托单，单纯更新工单委托类型接口
     *
     * @param orderId
     * @return
     */
    Result updateOrderInfoProxyType(Long orderId, Integer proxyType);

    /**
     * 获取工单信息
     *
     * @param orderId
     * @param shopId
     * @return
     */
    OrderInfo getOrderInfoById(Long orderId, Long shopId);
}
