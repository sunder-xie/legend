package com.tqmall.legend.facade.marketing.gather.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by xin on 2016/12/16.
 */
@Getter
@Setter
public class GatherCustomerVO {
    private Long customerCarId;
    private Long customerId;
    private String carLicense; // 车牌
    private String carModel; // 车型
    private String customerName; // 车主姓名
    private String customerMobile; // 车主电话
    private Long userId; // 归属SA id
    private String userName; // 归属SA
    private String lastConsumeTime; // 最近消费时间
    private BigDecimal totalConsumeAmount; // 累计消费金额
    private int totalConsumeCount; // 累计消费次数
    private Long noteInfoId; // 提醒id
    private String noteTime; // 提醒时间
    private Long gatherCouponConfigId;//老客户带新送券配置id
}
