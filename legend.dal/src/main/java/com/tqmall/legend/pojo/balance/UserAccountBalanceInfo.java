package com.tqmall.legend.pojo.balance;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.math.BigDecimal;

/**
 * Created by lifeilong on 2016/2/24.
 */
@Data
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserAccountBalanceInfo {
    private BigDecimal withdrewAmount; //已提现成功金额
    private BigDecimal withdrawingAmount;   //账户余额
    private BigDecimal auditingAmount;  //在审核中金额
    private BigDecimal withdrawingAmountFoTq;  //提现中金额
}
