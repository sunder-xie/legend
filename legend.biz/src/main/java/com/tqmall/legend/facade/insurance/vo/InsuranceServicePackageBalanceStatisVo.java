package com.tqmall.legend.facade.insurance.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by twg on 16/9/19.
 */
@Data
public class InsuranceServicePackageBalanceStatisVo {
    /**************现金*******************/
    private BigDecimal cashTotalAmount = BigDecimal.ZERO;//累计已结算金额
    private BigDecimal cashMonthAmount = BigDecimal.ZERO;//本月可结算金额
    private BigDecimal cashMonthWithdrawAmount = BigDecimal.ZERO;//本月提现中金额
    /***************服务包******************/
    private Integer packageTotalCount = 0;//累计赠送
    private Integer packageMonthCount = 0;//本月赠送
    private Integer packageDsxCount = 0;//待生效数量
    private Integer packageDfhCount = 0;//待发货数量
    private Integer packagePszCount = 0;//配送中数量
    private Integer packageYqsCount = 0;//已签收数量
    /***************保单****************/
    private Integer orderTotalCount = 0;//累计赠送
    private Integer orderMonthCount = 0;//本月赠送
    private Integer orderDsxCount = 0;//待生效数量
    private Integer orderYsxCount = 0;//已生效数量
}
