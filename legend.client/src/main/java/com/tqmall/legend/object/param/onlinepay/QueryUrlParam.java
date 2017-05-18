package com.tqmall.legend.object.param.onlinepay;

import com.tqmall.legend.object.param.BaseRpcParam;

/**
 * Created by tanghao on 16/5/16.
 */
public class QueryUrlParam extends BaseRpcParam {
    /**
     * 支付回调订单号
     */
    private String orderSn;

    /**
     * 支付成功与否
     */
    private Boolean paySuccess;

}
