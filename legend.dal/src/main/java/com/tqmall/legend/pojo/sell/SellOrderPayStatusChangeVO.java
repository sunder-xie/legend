package com.tqmall.legend.pojo.sell;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by xiangdong.qu on 17/2/23 10:34.
 */
@Data
public class SellOrderPayStatusChangeVO {
    public String payOrderSn;  //支付商品单号
    public String payNo;       //支付流水号
    public BigDecimal payAmount; //支付金额
    public Date payTime;       //支付时间
    public Integer checkStatus;//校验状态 1:校验失败,2:校验成功
}
