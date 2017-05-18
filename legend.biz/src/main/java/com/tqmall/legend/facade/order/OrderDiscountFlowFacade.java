package com.tqmall.legend.facade.order;

import com.tqmall.legend.biz.order.vo.OrderDiscountFlowVo;

import java.util.List;

/**
 * Created by zsy on 16/6/12.
 */
public interface OrderDiscountFlowFacade {
    /**
     * 根据orderId、shopId获取工单流水
     *
     * @param orderId
     * @return
     */
    List<OrderDiscountFlowVo> getOrderDiscountFlow(Long orderId, Long shopId);
}
