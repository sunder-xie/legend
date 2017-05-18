package com.tqmall.legend.facade.marketing.gather.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Created by xin on 2016/12/21.
 */
@Getter
@Setter
public class PanHuoStatVO {

    // 操作方式
    private Integer operateType;
    // 操作方式名称
    private String operateTypeName;
    // 操作客户数
    private Integer operateCustomerNum = 0;
    // 到店客户数
    private Integer toStoreCustomerNum = 0;
    // 增收
    private BigDecimal income = BigDecimal.ZERO;
    // 转化率
    private String conversionRate;
}
