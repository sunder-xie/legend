package com.tqmall.legend.facade.magic.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 业务场景：用于委托单对账单“确认对账”功能传参
 * Created by shulin on 16/10/17.
 */
@Getter
@Setter
public class ProxyConfirmParamVo {
    private BigDecimal surfaceNum;      //委托面数
    private BigDecimal surfacePrice;    //面漆单价，如果不填，传null
    private BigDecimal discountAmount;  //优惠金额
    private String confirmRemark;       //备注
    private String proxyIds;            //委托单ID，用逗号间隔
    private BigDecimal proxyAmount;     //委托总计金额
    private BigDecimal proxyRealAmount;  //应收金额
}
