package com.tqmall.legend.object.result.balance;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by dingbao on 16/9/26.
 */
@Data
public class UserWithdrawInfoDTO implements Serializable {

    private BigDecimal balance;       //账户余额
    private String userName;          //用户姓名
    private String account;           //提现账户
    private BigDecimal leastAmount;   //最小提现金额
    private Long accountId;           //账户ID
    private Integer accountType;      //账户类型
    private String accountBank;       //银行账户开户行
}
