package com.tqmall.legend.facade.insurance.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Created by zwb on 16/8/22.
 */
@Setter
@Getter
public class InsuranceAccountPayRecordVo {
    /**创建时间**/
    private java.util.Date gmtCreate;

    /**来源方资产账户id**/
    private Integer fromAccountId;

    /**来源方账户名称**/
    private String fromAccountName;

    private BigDecimal fromAccountCurrentAmount;

    /**接收资产账户id**/
    private Integer toAccountId;

    /**接收资产账户名称**/
    private String toAccountName;

    private BigDecimal toAccountCurrentAmount;

    /**操作账号，根据此字段查询流水**/
    private Integer operateAccountId;

    /**操作方**/
    private String operateAccountName;

    /**资产类型应用表字典表id**/
    private Integer amountTypeId;

    /**支出类型：1:收入，2:支出**/
    private Integer payType;

    /**操作金额**/
    private BigDecimal amount;

    /**关联的外部业务编号,，比如订单指的就是orderSn**/
    private String outOrderSn;

    private String businessSource;

    private String recordReason;

    private String recordAction;
}
