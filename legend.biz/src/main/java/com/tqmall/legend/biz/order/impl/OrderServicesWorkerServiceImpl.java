package com.tqmall.legend.biz.order.impl;

import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.order.OrderServicesWorkerService;
import com.tqmall.legend.dao.order.OrderServicesWorkerDao;
import com.tqmall.legend.entity.order.OrderServicesWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lixiao on 17/3/10.
 */
@Slf4j
@Service
public class OrderServicesWorkerServiceImpl extends BaseServiceImpl implements OrderServicesWorkerService {

    @Autowired
    private OrderServicesWorkerDao orderServicesWorkerDao;

    public int batchInsert(List<OrderServicesWorker> OrderServicesWorkerList) {
        int totalSize = super.batchInsert(orderServicesWorkerDao, OrderServicesWorkerList, 300);
        return totalSize;
    }

    @Override
    public int deletebByOrderId(Long orderId) {
        Map<String,Object> deleteMap = new HashMap<>();
        deleteMap.put("orderId" , orderId);
        return orderServicesWorkerDao.delete(deleteMap);
    }

}
