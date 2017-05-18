package com.tqmall.legend.object.result.balance;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by dingbao on 16/9/26.
 */
@Data
public class UserBalanceSetBankInfoDTO implements Serializable {

    private BigDecimal balance;     //账户余额
    private Boolean isSetBank;    //是否已设置了银行卡信息
    private String withdrawNoticeStr;//提现到账时间提醒

}
