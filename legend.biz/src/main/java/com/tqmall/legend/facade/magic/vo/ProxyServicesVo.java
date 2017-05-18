package com.tqmall.legend.facade.magic.vo;

import java.math.BigDecimal;

import lombok.Data;

/**
 *
 * Created by shulin on 16/5/17.
 */
@Data
public class ProxyServicesVo {

    private Long id;
    private Long shopId;//门店id
    private Long proxyId;//委托单id
    private String proxySn;//委托单编号
    private Long serviceId;//服务id
    private String serviceType;//服务类型
    private String serviceName;//服务名称
    private String serviceSn;//服务编号
    private BigDecimal serviceAmount;//订单金额
    private BigDecimal proxyAmount;//委托金额
    private BigDecimal sharePrice;//'成本价（toB）
    private BigDecimal serviceHour; //服务工时
    private String serviceNote; //备注
}
