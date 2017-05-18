package com.tqmall.legend.facade.insurance.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by sven on 16/9/21.
 */
@Data
public class PayFinanceBo {
    private String OrderSn;//淘汽保单sn

    private BigDecimal totalFee;//支付费用

    private String noAgree;//签约协议号

    private String cardNo;//银行卡号

    private String acctName;//银行账户姓名

    private String idNo;//证件号码

    private Boolean isEncrypt; //是否加密

    private Integer payMethod;  //支付方式 信用卡或借记卡

}
