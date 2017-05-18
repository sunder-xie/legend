package com.tqmall.legend.facade.onlinepay.bo;

import com.tqmall.legend.facade.onlinepay.enums.OnlinePayMethodEnum;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Created by sven on 2017/2/27.
 */
@Getter
@Setter
public class OnlinePayBo {
    private String OrderSn;//订单编号

    private BigDecimal totalFee;//支付费用

    private String noAgree;//签约协议号

    private String cardNo;//银行卡号

    private String acctName;//银行账户姓名

    private String idNo;//证件号码

    private Boolean isEncrypt; //是否加密

    private Integer payMethod;  //支付方式 信用卡或借记卡

    private Integer source;  //支付来源

    private Integer ucShopId; //userGlobalId 门店ID

    private Integer paymentMethod; //连连支付,银联支付,支付宝支付

    private String returnUrl;  //返回URL

}
