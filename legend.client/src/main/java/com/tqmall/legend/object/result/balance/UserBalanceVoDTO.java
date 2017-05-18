package com.tqmall.legend.object.result.balance;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by dingbao on 16/9/26.
 */
@Data
public class UserBalanceVoDTO implements Serializable {

    private BigDecimal balance;           //余额
    private String noticeStr;             //余额提现提示
    private Boolean withdrawStatus;       //提现开启或关闭

}
