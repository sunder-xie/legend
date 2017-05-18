package com.tqmall.legend.biz.pay;

import com.tqmall.common.UserInfo;
import com.tqmall.legend.common.Result;

import java.math.BigDecimal;

/**
 * Created by wanghui on 3/16/16.
 */
public interface OnlinePayService {
    /**
     * 通过支付宝支付
     * @param userInfo
     * @param payFee 支付费用
     * @param  smsSum 充值条数
     * @param remark  支付备注
     * @param backUrl 支付回调 url
     * @param operator 操作人
     * @return
     */
    String payByZhifubao(UserInfo userInfo,BigDecimal payFee,Integer smsSum, String remark, String backUrl);

    /**
     * 通用的支付宝支付接口
     * @return 提交支付宝的form表单字符串
     */
    public Result<String> payByZhifubaoCommon(String orderSn, BigDecimal payFee, String backUrl, String subject,Integer busType);
}
