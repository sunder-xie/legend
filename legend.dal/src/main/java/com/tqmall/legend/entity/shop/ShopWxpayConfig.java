package com.tqmall.legend.entity.shop;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by feilong.li on 16/10/17.
 */
@Setter
@Getter
public class ShopWxpayConfig extends BaseEntity{

    private Long shopId;    //门店id
    private Long userGlobalId;  //UC用户id
    private Long applyRecordId; //申请记录id
    private Integer payMode;    //微信支付模式 0 非受理模式 1 受理模式
    private String appId;       //非受理模式:公众号appid 受理模式:子公众号appid
    private String appSecret;       //公众号app_secret
    private String mchId;       //非受理模式:商户号 受理模式:子商户号
    private String apiKey;      //api密钥


}
