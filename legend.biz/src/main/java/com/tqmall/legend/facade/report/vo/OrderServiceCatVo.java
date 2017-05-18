package com.tqmall.legend.facade.report.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by 辉辉大侠 on 9/5/16.
 */
@Data
public class OrderServiceCatVo {
    /**
     * 排名
     */
    private Integer rank;
    /**
     * 服务类型名称
     */
    private String serviceCatName;
    /**
     * 销量
     */
    private Integer saleNum;
    /**
     * 销售额
     */
    private BigDecimal saleAmount;

}
