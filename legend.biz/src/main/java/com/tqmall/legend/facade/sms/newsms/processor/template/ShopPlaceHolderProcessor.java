package com.tqmall.legend.facade.sms.newsms.processor.template;

import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.entity.shop.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by majian on 16/11/28.
 * 与shop表相关的占位符替换处理器
 * shopName, wechat
 */
@Service
public class ShopPlaceHolderProcessor implements TemplatePreProcessor {
    @Autowired
    private ShopService shopService;

    @Override
    public void process(TemplatePreProcessContext context) {
        Shop shop = shopService.selectById(context.getShopId());
        String shopName = shop.getName();
        context.inflateTemplate("门店名称", shopName);
    }
}
