package com.tqmall.legend.biz.order;

import com.tqmall.common.UserInfo;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.order.OrderInfo;

/**
 * Created by lixiao on 15/9/8.
 */
public interface OrderExtService {

    /**
     * 异步保存工单无关紧要信息
     *
     * @param customerCar
     * @param orderInfo
     * @param userInfo
     * @param orderGoodsJson
     * @param orderServiceJson
     */
    public void saveOtherDataFuture(CustomerCar customerCar,
                                    OrderInfo orderInfo,
                                    UserInfo userInfo,
                                    String orderGoodsJson,
                                    String orderServiceJson,
                                    Long proxyId);


    /**
     * 异步更新工单无关紧要信息
     *
     * @param customerCar
     * @param orderInfo
     * @param userInfo
     */
    public void updateOtherDataFuture(CustomerCar customerCar,
                                      OrderInfo orderInfo,
                                      OrderInfo orderLatestData,
                                      UserInfo userInfo);


}
