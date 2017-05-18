package com.tqmall.error;

import com.tqmall.zenith.errorcode.ErrorCode;

/**
 * Created by zsy on 16/6/4.
 */
public class LegendSettlementErrorCode {
    public static final ErrorCode PARAMS_ERROR = LegendErrorHelper.newLegendError()
            .setDetailCode("3000").setMessageFormat("参数为空").genErrorCode();
    public static final ErrorCode ORDER_NOTEXSIT_ERROR = LegendErrorHelper.newLegendError()
            .setDetailCode("3001").setMessageFormat("工单不存在").genErrorCode();
    public static final ErrorCode ORDER_STATUS_ERROR = LegendErrorHelper.newLegendError()
            .setDetailCode("3002").setMessageFormat("工单不是待结算状态").genErrorCode();
    public static final ErrorCode CONFIRM_BILL_ERROR = LegendErrorHelper.newLegendError()
            .setDetailCode("3003").setMessageFormat("账单确认失败").genErrorCode();
    public static final ErrorCode DEBIT_BILL_INSERT_ERROR = LegendErrorHelper.newLegendError()
            .setDetailCode("3004").setMessageFormat("收款单添加失败").genErrorCode();
    public static final ErrorCode COUPON_ERROR = LegendErrorHelper.newLegendError()
            .setDetailCode("3005").setMessageFormat("优惠券数据不完整").genErrorCode();
    public static final ErrorCode MONNY_ERROR = LegendErrorHelper.newLegendError()
            .setDetailCode("3006").setMessageFormat("金额有误").genErrorCode();
    public static final ErrorCode CONFIRM_ERROR = LegendErrorHelper.newLegendError()
            .setDetailCode("3007").setMessageFormat("受托工单无需确认账单").genErrorCode();
    public static final ErrorCode PROXY_NOT_EXIST_ERROR = LegendErrorHelper.newLegendError()
            .setDetailCode("3008").setMessageFormat("委托单不存在").genErrorCode();
    public static final ErrorCode PROXY_SEARCH_ERROR = LegendErrorHelper.newLegendError()
            .setDetailCode("3009").setMessageFormat("委托单查询失败").genErrorCode();
}
