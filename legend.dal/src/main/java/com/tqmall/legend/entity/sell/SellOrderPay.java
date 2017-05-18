package com.tqmall.legend.entity.sell;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by xiangdong.qu on 17/2/22 20:57.
 */
@Data
public class SellOrderPay extends BaseEntity {
    private Long sellOrderId;//售卖订单id
    private String sellOrderSn;//售卖订单号
    private String payOrderSn;//支付商品号
    private String payNo;//支付流水号
    private Long payId;//支付id
    private BigDecimal payAmount;//支付金额(以元为单位)
    private Integer payResult;//支付状态1:支付失败2:支付成功
    private Date payTime;//支付成功日期
}

