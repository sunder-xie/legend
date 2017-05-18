package com.tqmall.legend.biz.order.impl;

import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.order.OrderHistoryService;
import com.tqmall.legend.dao.order.OrderHistoryDao;
import com.tqmall.legend.entity.order.OrderHistory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * Created by lilige on 15/11/16.
 */
@Slf4j
@Service
public class OrderHistoryServiceImpl extends BaseServiceImpl implements OrderHistoryService {
    @Autowired
    private OrderHistoryDao orderHistoryDao;

    @Override
    public List<OrderHistory> findOrderHistoryByOrderSn(Long shopId, Collection orderSn) {
        return orderHistoryDao.findOrderHistoryByOrderSn(shopId, orderSn);
    }

    @Override
    public void batchSave(List<OrderHistory> orderHistories) {
        super.batchInsert(orderHistoryDao,orderHistories,1000);
    }
}
