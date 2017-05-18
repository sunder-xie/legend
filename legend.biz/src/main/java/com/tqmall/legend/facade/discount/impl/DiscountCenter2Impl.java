package com.tqmall.legend.facade.discount.impl;

import com.google.common.collect.Lists;
import com.tqmall.legend.biz.shop.ShopServiceInfoService;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import com.tqmall.legend.facade.discount.DiscountCenter2;
import com.tqmall.legend.facade.discount.bo.*;
import com.tqmall.legend.facade.discount.configuration.handler.CalcHandler;
import com.tqmall.legend.facade.discount.configuration.handler.InitHandler;
import com.tqmall.wheel.lang.Langs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author 辉辉大侠
 * @Date:2:07 PM 02/03/2017
 */
@Service
public class DiscountCenter2Impl implements DiscountCenter2 {
    @Resource(name = "init-handler-list")
    private List<InitHandler> initHandlerList;

    @Resource(name = "calc-handler-list")
    private List<CalcHandler> calcHandlerList;
    @Autowired
    private ShopServiceInfoService shopServiceInfoService;

    @Override
    public DiscountInfoBo discountOrder(Long shopId, Long orderId, DiscountSelectedBo selectedItem) {
        DiscountContext cxt = new DiscountContext();
        cxt.setShopId(shopId);
        cxt.setOrderId(orderId);
        cxt.setSelected(selectedItem);
        cxt.setGuestMobile(selectedItem.getGuestMobile());
        return this._doDiscount(cxt);
    }

    @Override
    public DiscountInfoBo discountCarWashOrder(Long shopId, String carLicense, Long carWashServiceId, BigDecimal amount, DiscountSelectedBo selectedItem) {
        DiscountContext cxt = new DiscountContext();
        cxt.setShopId(shopId);
        cxt.setCarLicense(carLicense);
        cxt.setSelected(selectedItem);

        cxt.setGuestMobile(selectedItem.getGuestMobile());
        /**
         * 初始化工单信息
         */
        cxt.setOrderAmount(amount);
        cxt.setOrderServiceAmount(amount);
        cxt.setOrderGoodsAmount(BigDecimal.ZERO);
        cxt.setOrderFeeAmount(BigDecimal.ZERO);

        /**
         * 初始化服务信息
         */
        DiscountServiceBo discountService = new DiscountServiceBo();
        discountService.setServiceId(-1L);
        discountService.setOrderServiceId(-1L);
        discountService.setServiceHour(BigDecimal.ONE);
        discountService.setAmount(amount);
        discountService.setServiceCatId(-1L);
        if (Langs.isNotNull(carWashServiceId)) {
            ShopServiceInfo shopServiceInfo = shopServiceInfoService.selectById(carWashServiceId, shopId);
            if (Langs.isNotNull(shopServiceInfo)) {
                discountService.setServiceId(carWashServiceId);
                discountService.setAmount(amount);
                discountService.setServiceCatId(shopServiceInfo.getCategoryId());
            }
        }
        cxt.setDiscountServiceList(Lists.newArrayList(discountService));
        cxt.setDiscountGoodsList(Lists.<DiscountGoodsBo>newArrayList());
        return this._doDiscount(cxt);
    }

    public DiscountInfoBo _doDiscount(DiscountContext cxt) {
        for (InitHandler initHandler : this.initHandlerList) {
            initHandler.init(cxt);
        }
        for (CalcHandler calcHandler : this.calcHandlerList) {
            calcHandler.doDiscount(cxt);
        }
        DiscountInfoBo discountInfo = new DiscountInfoBo();
        discountInfo.setCustomerCarId(cxt.getCustomerCarId());
        discountInfo.setCarLicense(cxt.getCarLicense());
        discountInfo.setShopId(cxt.getShopId());
        discountInfo.setDiscountedAmount(cxt.getDiscountAmount());
        discountInfo.setAccountDiscount(cxt.getAccountDiscount());
        discountInfo.setBindAccountDiscountList(cxt.getBindAccountDiscountList());
        discountInfo.setGuestAccountDiscount(cxt.getGuestAccountDiscount());
        return discountInfo;
    }
}
