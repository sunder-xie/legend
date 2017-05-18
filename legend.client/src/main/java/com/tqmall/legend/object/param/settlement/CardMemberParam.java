package com.tqmall.legend.object.param.settlement;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by xin on 16/6/28.
 */
@Data
public class CardMemberParam implements Serializable {

    private static final long serialVersionUID = -950633440556623940L;

    private Long id;
    private String cardSn;
    private BigDecimal memberPayAmount;
    private BigDecimal balance;
}
