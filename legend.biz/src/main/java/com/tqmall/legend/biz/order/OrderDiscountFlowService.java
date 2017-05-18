package com.tqmall.legend.biz.order;

import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.order.OrderDiscountFlow;

import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 16/6/4.
 * 工单收款流水
 */
public interface OrderDiscountFlowService {
    /**
     * 根据id查询工单流水
     * @param id
     * @return
     */
    OrderDiscountFlow selectById(Long id);

    /**
     * 条件查询
     * @param searchParams
     * @return
     */
    List<OrderDiscountFlow> select(Map<String,Object> searchParams);

    /**
     * 批量添加工单收款流水
     *
     * @param orderDiscountFlowList
     * @return
     */
    Result batchInsert(List<OrderDiscountFlow> orderDiscountFlowList);

    /**
     * 根据orderId批量更新优惠流水状态
     *
     * @param orderId
     * @return
     */
    int batchUpdateAuditStatusByOrderId(Long orderId);
}
