package com.tqmall.legend.biz.order;


import com.tqmall.legend.entity.order.OrderHistory;

import java.util.Collection;
import java.util.List;

/**
 * Created by lilige on 15/11/16.
 */

public interface OrderHistoryService {

    /**
     * 根据工单编号，获取维修历史信息
     *
     * @param shopId
     * @param orderSn
     * @return
     */
    List<OrderHistory> findOrderHistoryByOrderSn(Long shopId, Collection orderSn);

    /**
     * 批量添加
     *
     * @param orderHistories
     */
    void batchSave(List<OrderHistory> orderHistories);

}
