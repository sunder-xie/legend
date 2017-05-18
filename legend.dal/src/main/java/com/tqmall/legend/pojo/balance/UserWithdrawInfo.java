package com.tqmall.legend.pojo.balance;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.math.BigDecimal;

/**
 * Created by xiangDong.qu on 15/12/17.
 */
@Data
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserWithdrawInfo {
    private BigDecimal balance;       //账户余额
    private String userName;          //用户姓名
    private String account;           //提现账户
    private BigDecimal leastAmount;   //最小提现金额
    private Long accountId;           //账户ID
    private Integer accountType;      //账户类型
    private String accountBank;       //银行账户开户行
}
