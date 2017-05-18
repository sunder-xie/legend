package com.tqmall.legend.biz.manager.thread;

import com.tqmall.common.UserInfo;
import com.tqmall.legend.biz.order.OrderExtService;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.order.OrderInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lixiao on 15/9/7.
 */
@Component
public class OrderInfoExtUpdateThread implements Runnable {

    CustomerCar customerCar;
    OrderInfo orderInfo;
    OrderInfo orderLatestData;
    UserInfo userInfo;

    @Autowired
    OrderExtService orderExtService;

    public void init(CustomerCar customerCar,
                           OrderInfo orderInfo,
                           OrderInfo orderLatestData,
                           UserInfo userInfo) {
        this.customerCar = customerCar;
        this.orderInfo = orderInfo;
        this.orderLatestData = orderLatestData;
        this.userInfo = userInfo;

    }

    @Override
    public void run() {
        orderExtService.updateOtherDataFuture(customerCar, orderInfo, orderLatestData,userInfo);
    }



}
