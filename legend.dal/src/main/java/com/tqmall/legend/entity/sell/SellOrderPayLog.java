package com.tqmall.legend.entity.sell;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by xiangdong.qu on 17/2/22 20:59.
 */
@Data
public class SellOrderPayLog extends BaseEntity {
    private Long sellOrderPayId;//支付记录id
    private Integer msgSource;//支付通知来源0:同步通知1:异步通知(异步通知只是成功的)
    private Integer checkStatus;//校验状态1:校验失败2:校验成功
    private String payOrderSn;//支付商品号
    private String payNo;//支付流水号
    private Long payId;//支付id
    private BigDecimal payAmount;//支付金额(以元为单位)
    private Integer payResult;//支付状态1:支付失败2:支付成功
    private Date payTime;//支付日期
}


