package com.tqmall.legend.facade.marketing.gather.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by xin on 2016/12/24.
 */
@Getter
@Setter
public class PerformanceStatVO {
    // 总计
    private DataStatVO total;
    // 新客户到店奖励
    private DataStatVO toStoreNewCustomerReward;
    // 业绩归属奖励
    private DataStatVO performanceBelongReward;
    // 业绩之星
    private DataStatVO performanceStar;
    // 维修业绩提成
    private DataStatVO repairPerformance;
    // 销售业绩提成
    private DataStatVO salePerformance;
    // 服务顾问业绩提成
    private DataStatVO serviceAdvisorPerformance;
}
