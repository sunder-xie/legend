package com.tqmall.legend.facade.sms.newsms.processor.template;

import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.common.entity.Result;
import com.tqmall.dandelion.wechat.client.dto.wechat.ShopDTO;
import com.tqmall.dandelion.wechat.client.wechat.shop.WeChatShopService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by majian on 16/11/30.
 */
@Service
@Slf4j
public class WechatAddressPlaceHolderProcessor implements TemplatePreProcessor {
    @Autowired
    private ShopService shopService;
    @Autowired
    private WeChatShopService weChatShopService;

    @Override
    public void process(TemplatePreProcessContext context) {
        Long userGlobalId = shopService.getUserGlobalId(context.getShopId());
        Result<ShopDTO> shopDTOResult = weChatShopService.selectShopByUcShopId(userGlobalId);
        if (shopDTOResult == null || !shopDTOResult.isSuccess() || shopDTOResult.getData() == null) {
            log.error("获取门店微信公众号名称失败,{}", LogUtils.funToString(userGlobalId,shopDTOResult));
            throw new BizException("获取门店微信公众号名称失败");
        }
        String accountName = shopDTOResult.getData().getAccountName();
        if (StringUtil.isStringEmpty(accountName)) {
            log.error("获取门店微信公众号名称失败,{}", LogUtils.funToString(userGlobalId,shopDTOResult));
            throw new BizException("获取门店微信公众号名称失败");
        }
        context.inflateTemplate("门店微信公众号名称", accountName);
    }
}
