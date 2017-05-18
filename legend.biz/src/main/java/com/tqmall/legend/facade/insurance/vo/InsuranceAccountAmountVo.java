package com.tqmall.legend.facade.insurance.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Created by zwb on 16/8/22.
 */
@Setter
@Getter
public class InsuranceAccountAmountVo {

    /**资产账户id**/
    private Integer financeAccountId;

    /**资产类型id,冗余字段**/
    private Integer amountTypeId;

    /**总余额，amount＋frozen_amount**/
    private BigDecimal totalAmount;

    /**可用余额**/
    private BigDecimal amount;

    /**已用余额**/
    private BigDecimal usedAmount;

    /**冻结余额**/
    private BigDecimal frozenAmount;

    /**－1:冻结，0:失效，1:生效**/
    private Integer amountStatus;
}
