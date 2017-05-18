package com.tqmall.legend.facade.report.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by 辉辉大侠 on 9/5/16.
 */
@Data
public class OrderServiceCatForPie {
    /**
     * 服务分类名称
     */
    private String serviceCatName;
    /**
     * 服务分类id
     */
    private Long serviceCatId;
    /**
     * 服务分类对应销量
     */
    private Integer saleNum;
    /**
     * 服务分类对应销售额
     */
    private BigDecimal saleAmount;
}
