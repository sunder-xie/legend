package com.tqmall.legend.object.result.settlement;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Created by xiangDong.qu on 16/3/15.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PaymentDTO implements Serializable{
    private static final long serialVersionUID = -8611383816181198382L;

    private Long id;
    private Long shopId;
    private String name;
    private Integer paymentTag;//标准结算类目1现金2刷卡3会员卡4第三方支付5转账6支票7其它，1优先级最高
}
