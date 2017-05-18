package com.tqmall.error;

import com.tqmall.zenith.errorcode.ErrorCode;

/**
 * Created by zsy on 16/5/23.
 * 共享中心错误码
 */
public class LegendShareErrorCode {
    public static final ErrorCode PARAMS_ERROR = LegendErrorHelper.newLegendError()
            .setDetailCode("6100").setMessageFormat("参数有误").genErrorCode();
    public static final ErrorCode ORDER_STATUS_ERROR = LegendErrorHelper.newLegendError()
            .setDetailCode("6101").setMessageFormat("委托单未交车，无法结算").genErrorCode();
    public static final ErrorCode PRICE_NOT_MATCH_ERROR = LegendErrorHelper.newLegendError()
            .setDetailCode("6102").setMessageFormat("委托单和工单实际应收金额不匹配，无法结算，工单id为{}").genErrorCode();
    public static final ErrorCode PAYMENT_ERROR = LegendErrorHelper.newLegendError()
            .setDetailCode("6103").setMessageFormat("现金的支付方式不存在，无法结算").genErrorCode();
    public static final ErrorCode ORDER_CONFIRM_ERROR = LegendErrorHelper.newLegendError()
            .setDetailCode("6104").setMessageFormat("账单确认失败").genErrorCode();
}
