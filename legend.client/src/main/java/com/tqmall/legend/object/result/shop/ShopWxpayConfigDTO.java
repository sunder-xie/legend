package com.tqmall.legend.object.result.shop;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by feilong.li on 16/10/17.
 */
@Data
public class ShopWxpayConfigDTO implements Serializable {

    private static final long serialVersionUID = -1963999676957777055L;

    private Long id;
    private Long shopId;    //门店id
    private Long userGlobalId;  //UC用户id
    private Long applyRecordId; //申请记录id
    private Integer payMode;    //微信支付模式 0 商户模式 1 受理模式
    private String appId;       //商户模式:公众号appid 受理模式:子公众号appid
    private String appSecret;       //公众号app_secret
    private String mchId;       //商户模式:商户号 受理模式:子商户号
    private String apiKey;      //api密钥

    private Integer applyStatus;   //申请状态 0 申请中 1 对接中 2 中断 3 已开通 4 测试通过

}
