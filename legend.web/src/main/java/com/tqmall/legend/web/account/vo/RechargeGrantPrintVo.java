package com.tqmall.legend.web.account.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by majian on 16/6/22.
 */
@Data
public class RechargeGrantPrintVo {
    private String flowSn;
    private String dateStr;
    private String cardNumber;
    private String customerName;
    private String carLicenses;
    private String phone;
    private BigDecimal payAmount;
    private String shopName;
    private String shopTele;
    private String shopAddress;
    private String tips;

    private String remark;
    private String name;
    private Integer number = 1;
    private BigDecimal amount;
    private BigDecimal payableAmount;//应付金额

}
