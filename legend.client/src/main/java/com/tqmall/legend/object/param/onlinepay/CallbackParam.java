package com.tqmall.legend.object.param.onlinepay;

import com.tqmall.legend.object.param.BaseRpcParam;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Created by wanghui on 3/16/16.
 */
@Data
@ToString
public class CallbackParam extends BaseRpcParam {
    /**
     * 支付费用
     */
    private BigDecimal payFee;
    /**
     * 支付订单号
     */
    private String payNo;
    /**
     * 支付回调订单号
     */
    private String orderSn;
    /**
     * 支付成功与否
     */
    private Boolean paySuccess;
    /**
     * 支付信息
     */
    private String remark;
    /**
     * 业务类型 null 或者1表示短信充值, 2表示微信公众号注册费用支付
     */
    private Integer busType;
}
