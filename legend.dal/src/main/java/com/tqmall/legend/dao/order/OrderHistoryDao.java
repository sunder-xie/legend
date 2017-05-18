package com.tqmall.legend.dao.order;

/**
 * Created by lilige on 15/11/16.
 */

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.order.OrderHistory;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

@MyBatisRepository
public interface OrderHistoryDao extends BaseDao<OrderHistory> {

    /**
     * 根据工单编号，获取维修历史信息
     * @param shopId
     * @param orderSn
     * @return
     */
    List<OrderHistory> findOrderHistoryByOrderSn(@Param("shopId")Long shopId, @Param("orderSn")Collection orderSn);

}

