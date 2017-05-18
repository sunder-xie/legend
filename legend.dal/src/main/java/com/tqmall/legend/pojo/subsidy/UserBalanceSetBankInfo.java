package com.tqmall.legend.pojo.subsidy;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.math.BigDecimal;

/**
 * Created by lifeilong on 2016/2/23.
 */
@Data
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserBalanceSetBankInfo {
    private BigDecimal balance;     //账户余额
    private Boolean isSetBank;    //是否已设置了银行卡信息
    private String withdrawNoticeStr;//提现到账时间提醒
}
