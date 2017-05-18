package com.tqmall.legend.object.param.sell;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by xiangdong.qu on 17/2/23 09:45.
 */
@Data
public class RpcSellNoticeParam implements Serializable{
    private String sellOrderSn;  //购买订单号(非连连支付商品号)
    private String payNo;        //支付流水号
    private Integer payId;       //支付id
    private BigDecimal payAmount;//支付金额
    private Date payDate;        //支付日期
}
