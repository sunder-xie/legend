package com.tqmall.error;

import com.tqmall.zenith.errorcode.ErrorCode;

/**
 * Created by changqiang.ke on 16/5/5.
 */
public class LegendThirdPartyErrorCode {
    public static final ErrorCode APLLY_ACCOUNT_FAIL = LegendErrorHelper.newLegendError()
            .setDetailCode("9001").setMessageFormat("申请打款失败").genErrorCode();
    public static final ErrorCode GET_SHOP_INFO_FAIL = LegendErrorHelper.newLegendError()
            .setDetailCode("9002").setMessageFormat("根据shopId取门店信息失败").genErrorCode();
    public static final ErrorCode RECEIVER_MOBILE_ERRO = LegendErrorHelper.newLegendError()
            .setDetailCode("9003").setMessageFormat("收票人手机号错误").genErrorCode();
    public static final ErrorCode NO_CAR_FUND_AUDIT = LegendErrorHelper.newLegendError()
            .setDetailCode("9004").setMessageFormat("没有对应的资金记录").genErrorCode();
    public static final ErrorCode NO_APPLY_FUND_INFO = LegendErrorHelper.newLegendError()
            .setDetailCode("9005").setMessageFormat("申请打款信息为空").genErrorCode();

    //返利接口
    public static final ErrorCode RETURNS_EXCEED_LIMIT = LegendErrorHelper.newLegendError()
            .setDetailCode("9006").setMessageFormat("退货数量超过限额").genErrorCode();
}
