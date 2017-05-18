package com.tqmall.legend.facade.sms.newsms.processor.template;

import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.common.entity.Result;
import com.tqmall.dandelion.wechat.client.wechat.coupon.WeChatCouponConfigService;
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
public class CouponUrlPlaceHolderProcessor implements TemplatePreProcessor {
    @Autowired
    private ShopService shopService;
    @Autowired
    private WeChatCouponConfigService weChatCouponConfigService;

    @Override
    public void process(TemplatePreProcessContext context) {

        Long userGlobalId = shopService.getUserGlobalId(context.getShopId());
        Result<String> couponUrlResult = weChatCouponConfigService.getCouponUrl(userGlobalId);
        if (couponUrlResult == null || !couponUrlResult.isSuccess() || couponUrlResult.getData() == null) {
            log.error("获取领劵链接失败,{}", LogUtils.funToString(userGlobalId,couponUrlResult));
            throw new BizException("获取领劵链接失败");
        }

        String couponUrl = couponUrlResult.getData();
        if (StringUtil.isStringEmpty(couponUrl)) {
            log.error("获取领劵链接失败,couponUrl is empty String,{}", LogUtils.funToString(userGlobalId,couponUrlResult));
            throw new BizException("获取领劵链接失败");
        }
        context.inflateTemplate("领劵链接", couponUrl);
    }
}
