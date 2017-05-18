package com.tqmall.legend.facade.discount.configuration.handler.init.converter;

import com.tqmall.legend.entity.order.OrderServices;
import com.tqmall.legend.facade.discount.bo.DiscountServiceBo;
import com.tqmall.wheel.lang.Converter;

import static com.tqmall.wheel.lang.Langs.isNotNull;

/**
 * @Author 辉辉大侠
 * @Date:11:33 AM 02/03/2017
 */
public class DiscountServiceConverter implements Converter<OrderServices, DiscountServiceBo> {

    @Override
    public DiscountServiceBo apply(OrderServices orderServices, DiscountServiceBo discountServiceBo) {
        if (isNotNull(orderServices) || isNotNull(discountServiceBo)) {
            discountServiceBo.setServiceId(orderServices.getServiceId());
            discountServiceBo.setOrderServiceId(orderServices.getId());
            discountServiceBo.setServiceCatId(orderServices.getServiceCatId());
            discountServiceBo.setServiceHour(orderServices.getServiceHour());
            discountServiceBo.setAmount(orderServices.getSoldAmount());
        }
        return discountServiceBo;
    }
}
