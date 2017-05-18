package com.tqmall.legend.object.param.settlement;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by xin on 16/6/18.
 */
@Data
public class DebitBillFlowParam implements Serializable {

    private static final long serialVersionUID = 4544432680842560642L;

    private Long paymentId;
    private String paymentName;
    private BigDecimal payAmount;
}
