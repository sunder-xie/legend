package com.tqmall.legend.facade.discount.configuration.handler.init.converter;

import com.tqmall.legend.entity.order.OrderGoods;
import com.tqmall.legend.facade.discount.bo.DiscountGoodsBo;
import com.tqmall.wheel.lang.Converter;

import static com.tqmall.wheel.lang.Langs.isNotNull;

/**
 * @Author 辉辉大侠
 * @Date:1:50 PM 02/03/2017
 */
public class DiscountGoodsConverter implements Converter<OrderGoods, DiscountGoodsBo> {
    @Override
    public DiscountGoodsBo apply(OrderGoods orderGoods, DiscountGoodsBo discountGoods) {
        if (isNotNull(orderGoods) && isNotNull(discountGoods)) {
            discountGoods.setOrderGoodsId(orderGoods.getId());
            discountGoods.setGoodsId(orderGoods.getGoodsId());
            discountGoods.setAmount(orderGoods.getSoldAmount());
            discountGoods.setGoodsNumber(orderGoods.getGoodsNumber());
        }
        return discountGoods;
    }
}
