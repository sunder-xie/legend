package com.tqmall.legend.biz.manager.thread;

import com.tqmall.common.UserInfo;
import com.tqmall.legend.biz.order.*;
import com.tqmall.legend.entity.customer.*;
import com.tqmall.legend.entity.order.OrderInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lixiao on 15/9/7.
 */
@Component
public class OrderInfoExtSaveThread implements Runnable {


    CustomerCar customerCar;
    OrderInfo orderInfo;
    UserInfo userInfo;
    String orderGoodsJson;
    String orderServiceJson;
    Long proxyId;//委托单id
    @Autowired
    OrderExtService orderExtService;

    public void init(CustomerCar customerCar,
                           OrderInfo orderInfo,
                           UserInfo userInfo,
                           String orderGoodsJson,
                           String orderServiceJson,Long proxyId) {
        this.customerCar = customerCar;
        this.orderInfo = orderInfo;
        this.userInfo = userInfo;
        this.orderGoodsJson = orderGoodsJson;
        this.orderServiceJson = orderServiceJson;
        this.proxyId = proxyId;
    }

    @Override
    public void run() {
        orderExtService.saveOtherDataFuture(customerCar, orderInfo, userInfo, orderGoodsJson, orderServiceJson, proxyId);
    }



}
