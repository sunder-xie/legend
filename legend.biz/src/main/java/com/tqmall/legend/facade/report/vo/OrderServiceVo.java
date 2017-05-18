package com.tqmall.legend.facade.report.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by majian on 16/9/6.
 */
@Data
public class OrderServiceVo {
    private Integer rank;//排名
    private String serviceName;//服务名
    private String serviceCatName;//服务类别名
    private Integer saleNum;//销量
    private BigDecimal saleAmount;//销售额
}
