package com.tqmall.legend.object.result.service;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by zsy on 15/12/15.
 */
@Data
public class AppServiceInfoDTO implements Serializable {
    private static final long serialVersionUID = 7139870698219320723L;
    private Long serviceId;//服务id
    private String serviceName;
    private String serviceSn;
    private BigDecimal servicePrice;//服务价格(若为套餐时则为套餐价)
    private BigDecimal marketPrice;//服务市场价格，仅做车主展示,不可低于servicePrice
    private String flags;                               //“BZFW”：标准服务,“TQFW”：淘汽服务,CZFU:车主服务
    private String serviceNote;
    private String url;
    private Long sort;
    private Integer priceType;//服务价格类型 1 正常价格数值显示 2 到店洽谈 3 免费
    private String serviceInfo;
}
