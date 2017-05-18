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
public class UserBalanceVO {
    private BigDecimal balance;           //余额
    private String noticeStr;             //余额提现提示
    private Boolean withdrawStatus;       //提现开启或关闭
}
