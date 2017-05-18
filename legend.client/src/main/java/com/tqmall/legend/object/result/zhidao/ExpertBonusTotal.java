package com.tqmall.legend.object.result.zhidao;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by macx on 16/3/30.
 */
@Data
public class ExpertBonusTotal implements Serializable{
    private static final long serialVersionUID = -3296218658623118480L;
    private BigDecimal noTXAmount = new BigDecimal(0);//未提现金额
    private BigDecimal txAmount = new BigDecimal(0);//总答题金额
}
