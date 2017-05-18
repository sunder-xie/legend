package com.tqmall.legend.facade.setting.impl;

import com.tqmall.legend.biz.settlement.PaymentService;
import com.tqmall.legend.entity.settlement.Payment;
import com.tqmall.legend.enums.settlement.PaymentShowStatusEnum;
import com.tqmall.legend.facade.setting.PaymentFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 16/11/7.
 */
@Service
public class PaymentFacadeImpl implements PaymentFacade {
    @Autowired
    private PaymentService paymentService;

    @Override
    public List<Payment> getPaymentByShopId(Long shopId) {
        //设置门店支付方式
        List<Payment> simpleList = getSimplePayment();
        Map<String, Payment> shopPaymentMap = new HashMap<>();
        //将结算类别设置成标准类别
        for (Payment simple : simpleList) {
            String name = simple.getName();
            shopPaymentMap.put(name, null);
        }
        //添加非固定类型的支付方式
        Map<String, Object> paymentMap = new HashMap<>();
        paymentMap.put("shopId", shopId);
        List<Payment> shopPaymentList = paymentService.select(paymentMap);
        for (Payment shopPayment : shopPaymentList) {
            String name = shopPayment.getName();
            if (!shopPaymentMap.containsKey(name)) {
                shopPaymentMap.put(name, null);
                simpleList.add(shopPayment);
            }
        }
        return simpleList;
    }

    @Override
    public Payment getCachePaymentById(Long id) {
        return paymentService.selectPaymentById(id);
    }

    @Override
    public void updatePaymentById(Payment payment) {
        paymentService.update(payment);
    }

    /**
     * 获取业务类型
     *
     * @return
     */
    private List<Payment> getSimplePayment() {
        Map<String, Object> searchMap = new HashMap<>(1);
        searchMap.put("shopId", 0);
        searchMap.put("showStatus", PaymentShowStatusEnum.SHOW.getCode());
        List<String> sorts = new ArrayList<>();
        sorts.add("payment_tag asc");
        searchMap.put("sorts", sorts);
        List<Payment> simpleList = paymentService.select(searchMap);
        return simpleList;
    }
}
