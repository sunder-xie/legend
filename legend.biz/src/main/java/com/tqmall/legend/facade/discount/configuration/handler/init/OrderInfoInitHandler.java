package com.tqmall.legend.facade.discount.configuration.handler.init;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.legend.biz.goods.GoodsService;
import com.tqmall.legend.biz.order.OrderGoodsService;
import com.tqmall.legend.biz.order.OrderInfoService;
import com.tqmall.legend.biz.order.OrderServicesService;
import com.tqmall.legend.entity.goods.Goods;
import com.tqmall.legend.entity.order.OrderGoods;
import com.tqmall.legend.entity.order.OrderInfo;
import com.tqmall.legend.entity.order.OrderServices;
import com.tqmall.legend.facade.discount.bo.DiscountContext;
import com.tqmall.legend.facade.discount.bo.DiscountGoodsBo;
import com.tqmall.legend.facade.discount.bo.DiscountServiceBo;
import com.tqmall.legend.facade.discount.configuration.handler.InitHandler;
import com.tqmall.legend.facade.discount.configuration.handler.init.converter.DiscountGoodsConverter;
import com.tqmall.legend.facade.discount.configuration.handler.init.converter.DiscountServiceConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static com.tqmall.wheel.lang.Langs.isNotEmpty;
import static com.tqmall.wheel.lang.Langs.isNotNull;

/**
 * 工单信息初始化
 *
 * @Author 辉辉大侠
 * @Date:10:59 AM 02/03/2017
 */
@Slf4j
public class OrderInfoInitHandler implements InitHandler {
    @Autowired
    private OrderInfoService orderInfoService;
    @Autowired
    private OrderServicesService orderServicesService;
    @Autowired
    private OrderGoodsService orderGoodsService;
    @Autowired
    private GoodsService goodsService;

    @Override
    public void init(DiscountContext cxt) {
        /**
         * 若存在工单id,则不为洗车单,初始化上下文信息
         */
        if (isNotNull(cxt.getOrderId())) {
            initOrderBaseInfo(cxt);
            initOrderService(cxt);
            initOrderGoods(cxt);
        }
    }

    private void initOrderBaseInfo(DiscountContext cxt) {
        /**
         * 初始化上下文中的工单信息
         */
        OrderInfo orderInfo = this.orderInfoService.selectById(cxt.getOrderId(), cxt.getShopId());
        cxt.setOrderAmount(orderInfo.getOrderAmount());
        cxt.setOrderServiceAmount(orderInfo.getServiceAmount().subtract(orderInfo.getServiceDiscount()));
        cxt.setOrderGoodsAmount(orderInfo.getGoodsAmount().subtract(orderInfo.getGoodsDiscount()));
        cxt.setOrderFeeAmount(orderInfo.getFeeAmount().subtract(orderInfo.getFeeDiscount()));
    }

    private void initOrderGoods(DiscountContext cxt) {
        /**
         * 初始化上下文中的物料信息
         */
        List<DiscountGoodsBo> discountGoodsList = Lists.newArrayList();
        List<OrderGoods> orderGoodsList = this.orderGoodsService.queryOrderGoodList(cxt.getOrderId(), cxt.getShopId());
        if (isNotEmpty(orderGoodsList)) {
            List<Goods> goodsList = this.goodsService.selectByIdsAndShopId(Lists.transform(orderGoodsList, new Function<OrderGoods, Long>() {
                @Override
                public Long apply(OrderGoods input) {
                    return input.getGoodsId();
                }
            }), cxt.getShopId());
            Map<Long, Goods> goodsMap = Maps.uniqueIndex(goodsList, new Function<Goods, Long>() {
                @Override
                public Long apply(Goods input) {
                    return input.getId();
                }
            });

            for (OrderGoods orderGoods : orderGoodsList) {
                DiscountGoodsBo discountGoods = new DiscountGoodsConverter().apply(orderGoods, new DiscountGoodsBo());
                Goods goods = goodsMap.get(discountGoods.getGoodsId());
                if (isNotNull(goods)) {
                    if (isNotNull(goods.getStdCatId()) && !Integer.valueOf(0).equals(goods.getStdCatId())) {
                        discountGoods.setGoodsStdCatId(Long.valueOf(goods.getStdCatId()));
                    } else {
                        discountGoods.setGoodsCustomCatId(goods.getCatId());
                    }
                } else {
                    log.error("根据id获取个物料信息失败,goodsId:{}", discountGoods.getGoodsId());
                }
                discountGoodsList.add(discountGoods);
            }
        }
        cxt.setDiscountGoodsList(discountGoodsList);
    }

    /**
     * 初始化上下文中的工单服务信息
     *
     * @param cxt
     */
    private void initOrderService(DiscountContext cxt) {
        List<DiscountServiceBo> discountServiceList = Lists.newArrayList();
        List<OrderServices> orderServicesList = this.orderServicesService.queryOrderServiceList(cxt.getOrderId(), cxt.getShopId());
        if (isNotEmpty(orderServicesList)) {
            for (OrderServices orderServices : orderServicesList) {
                discountServiceList.add(new DiscountServiceConverter().apply(orderServices, new DiscountServiceBo()));
            }
        }
        cxt.setDiscountServiceList(discountServiceList);
    }
}
