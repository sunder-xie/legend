package com.tqmall.legend.object.result.service;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * ddl-wechat端查询工单列表内部关联的服务对象
 * Created by wushuai on 16/9/19.
 */
@Data
public class WechatOrdServiceDTO implements Serializable {
    private Long serviceId;
    private String serviceName;
    private BigDecimal serviceHour;
    private BigDecimal servicePrice;
    private Long priceType;//服务价格类型 0 正常价格数值显示 1 到店洽谈 2 免费
    private String categoryName;        //一级分类名称
    private Long categoryId;         //一级分类id
}
