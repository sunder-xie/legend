package com.tqmall.legend.biz.order;

import com.tqmall.legend.entity.order.OrderServicesWorker;

import java.util.List;

/**
 * Created by lixiao on 17/3/10.
 */
public interface OrderServicesWorkerService {

    public int batchInsert(List<OrderServicesWorker> OrderServicesWorkerList);

    public int deletebByOrderId(Long orderId);
}
