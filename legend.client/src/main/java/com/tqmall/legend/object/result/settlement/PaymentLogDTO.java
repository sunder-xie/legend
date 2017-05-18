package com.tqmall.legend.object.result.settlement;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by xiangDong.qu on 16/3/19.
 */
@Data
@EqualsAndHashCode
@ToString
@Deprecated
public class PaymentLogDTO implements Serializable{
    private static final long serialVersionUID = 5331814650250836777L;
    private BigDecimal payAmount;
    private Long paymentId;
    private String paymentName;
    private String paymentUserName;
}
