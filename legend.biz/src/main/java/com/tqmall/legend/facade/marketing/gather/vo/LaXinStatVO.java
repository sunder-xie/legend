package com.tqmall.legend.facade.marketing.gather.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Created by xin on 2016/12/22.
 */
@Getter
@Setter
public class LaXinStatVO {
    // 操作方式
    private Integer operateType;
    // 操作方式名称
    private String operateTypeName;
    // 发放优惠券老客户数
    private Integer grantOldCustomerNum = 0;
    // 领券老客户数
    private Integer receiveOldCustomerNum = 0;
    // 领券新客户数
    private Integer receiveNewCustomerNum = 0;
    // 到店老客户数
    private Integer toStoreOldCustomerNum = 0;
    // 到店新客户数
    private Integer toStoreNewCustomerNum = 0;
    // 增收
    private BigDecimal income = BigDecimal.ZERO;
    // 转化率
    private String conversionRate;
}
