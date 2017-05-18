package com.tqmall.legend.biz.order;

import com.tqmall.legend.entity.order.OrderPrecheckDetails;

import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 16/7/9.
 */
public interface OrderPrecheckDetailsService {
    /**
     * 批量添加
     * @param orderPrecheckDetailsList
     * @return
     */
    int batchInsert(List<OrderPrecheckDetails> orderPrecheckDetailsList);

    /**
     * 删除
     * @param ids
     * @return
     */
    int deleteByIds(Object[] ids);

    /**
     * 条件查询
     *
     * @param searchParams
     * @return
     */
    List<OrderPrecheckDetails> select(Map<String,Object> searchParams);

    /**
     * 更新
     * @param orderPrecheckDetails
     * @return
     */
    int updateById(OrderPrecheckDetails orderPrecheckDetails);
}
