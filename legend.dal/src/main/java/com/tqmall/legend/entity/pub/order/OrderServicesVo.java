package com.tqmall.legend.entity.pub.order;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = false)
@Data
public class OrderServicesVo {

    private String serviceName;//服务名称
    private String categoryName;//服务类别名称
    private Long categoryId;//服务类别ID
    private BigDecimal serviceHour;//工时
    private BigDecimal servicePrice;//单价
    private Integer priceType;          //服务价格类型 1 正常价格数值显示 2 到店洽谈 3 免费

}

