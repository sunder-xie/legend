package com.tqmall.legend.dao.order;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.order.OrderDiscountFlow;
import org.apache.ibatis.annotations.Param;

/**
 * Created by zsy on 16/6/2.
 */
@MyBatisRepository
public interface OrderDiscountFlowDao extends BaseDao<OrderDiscountFlow> {
    /**
     * 根据orderId批量更新收款流水状态
     *
     * @param orderId
     * @return
     */
    int batchUpdateAuditStatusByOrderId(@Param("orderId") Long orderId);
}
