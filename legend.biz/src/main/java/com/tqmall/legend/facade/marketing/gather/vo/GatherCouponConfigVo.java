package com.tqmall.legend.facade.marketing.gather.vo;

import com.tqmall.legend.entity.marketing.gather.GatherCouponConfig;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by wushuai on 16/12/19.
 */
@Getter
@Setter
public class GatherCouponConfigVo extends GatherCouponConfig {
    private String shopName;//门店名称
    private String shopAddress;//门店地址
    private String qrCode;//门店微信公众号二维码图片链接(未开通的门店此值为空)
    private String couponName;//优惠券名称
    private String validityPeriodStr;//优惠券有效期文本
}
