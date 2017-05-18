package com.tqmall.legend.facade.discount.configuration.handler.init;

import com.tqmall.common.exception.BizException;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.legend.biz.order.OrderInfoService;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.order.OrderInfo;
import com.tqmall.legend.facade.discount.bo.DiscountContext;
import com.tqmall.legend.facade.discount.configuration.handler.InitHandler;
import org.springframework.beans.factory.annotation.Autowired;

import static com.tqmall.wheel.lang.Langs.isBlank;
import static com.tqmall.wheel.lang.Langs.isNotNull;
import static com.tqmall.wheel.lang.Langs.isNull;

/**
 * @Author 辉辉大侠
 * @Date:2:35 PM 02/03/2017
 */
public class CustomerCarInfoinitHandler implements InitHandler {
    @Autowired
    private OrderInfoService orderInfoService;
    @Autowired
    private CustomerCarService customerCarService;

    @Override
    public void init(DiscountContext cxt) {
        if (isNull(cxt.getOrderId())) {
            /**
             * 洗车单初始化车辆、客户信息
             */
            if (isBlank(cxt.getCarLicense())) {
                throw new BizException("洗车单开单时车牌信息未填写.");
            }
            cxt.setCarLicense(cxt.getCarLicense());
            CustomerCar customerCar = this.customerCarService.selectByLicenseAndShopId(cxt.getCarLicense(), cxt.getShopId());
            if (isNotNull(customerCar)) {
                /**
                 * 洗车单开单时车辆不一定存在
                 */
                cxt.setCustomerCarId(customerCar.getId());
                cxt.setCustomerId(customerCar.getCustomerId());
            }
        } else {
            /**
             * 其他工单初始化车辆信息
             */
            OrderInfo orderInfo = this.orderInfoService.selectById(cxt.getOrderId(), cxt.getShopId());
            cxt.setCustomerCarId(orderInfo.getCustomerCarId());
            cxt.setCustomerId(orderInfo.getCustomerId());
            cxt.setCarLicense(orderInfo.getCarLicense());
        }
    }
}
