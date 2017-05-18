package com.tqmall.legend.facade.magic.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 业务场景：初始化委托对账单页面数据
 * Created by shulin on 16/10/17.
 */
@Getter
@Setter
public class ProxyConfirmVo {
    private String serviceType;  //服务类型
    private BigDecimal proxyAmount; //委托金额
    private BigDecimal surfaceNum;  //委托面数
    private String opteratorName; //收银员
    private Long shopId;  //委托方门店ID
    private String shopName; //委托方门店名称
}
