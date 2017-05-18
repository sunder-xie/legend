package com.tqmall.legend.biz.order.impl;

import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.order.OrderDiscountFlowService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.dao.order.OrderDiscountFlowDao;
import com.tqmall.legend.entity.order.OrderDiscountFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 16/6/4.
 * 工单收款流水
 */
@Service
public class OrderDiscountFlowServiceImpl extends BaseServiceImpl implements OrderDiscountFlowService {
    @Autowired
    private OrderDiscountFlowDao orderDiscountFlowDao;

    /**
     * 根据id查询工单流水
     * @param id
     * @return
     */
    @Override
    public OrderDiscountFlow selectById(Long id) {
        OrderDiscountFlow orderDiscountFlow = orderDiscountFlowDao.selectById(id);
        return orderDiscountFlow;
    }

    /**
     * 条件查询
     * @param searchParams
     * @return
     */
    @Override
    public List<OrderDiscountFlow> select(Map<String, Object> searchParams) {
        List<OrderDiscountFlow> orderDiscountFlowList = orderDiscountFlowDao.select(searchParams);
        return orderDiscountFlowList;
    }

    /**
     * 批量添加工单收款流水
     *
     * @param orderDiscountFlowList
     * @return
     */
    @Override
    @Transactional
    public Result batchInsert(List<OrderDiscountFlow> orderDiscountFlowList) {
        Integer size = super.batchInsert(orderDiscountFlowDao,orderDiscountFlowList,300);
        return Result.wrapSuccessfulResult(size);
    }

    @Override
    public int batchUpdateAuditStatusByOrderId(Long orderId) {
        if(orderId != null){
            return orderDiscountFlowDao.batchUpdateAuditStatusByOrderId(orderId);
        }
        return 0;
    }
}
