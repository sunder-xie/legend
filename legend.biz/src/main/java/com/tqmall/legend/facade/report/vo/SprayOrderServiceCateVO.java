package com.tqmall.legend.facade.report.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by xin on 2016/12/13.
 */
@Data
public class SprayOrderServiceCateVO {
    private Integer rank;//排名
    private String serviceCatName;//服务类别名
    private Long serviceCatId;//服务类别id
    private Integer saleNum;//销量
    private BigDecimal saleAmount;//销售额
    private BigDecimal surfaceNum;//面漆数
}
