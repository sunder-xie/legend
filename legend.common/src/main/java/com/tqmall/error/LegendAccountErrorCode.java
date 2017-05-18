package com.tqmall.error;

import com.tqmall.zenith.errorcode.ErrorCode;

/**
 * Created by wanghui on 6/8/16.
 */
public interface LegendAccountErrorCode {
    ErrorCode PARAMS_ERROR = LegendErrorHelper.newLegendError()
            .setDetailCode("4000").setMessageFormat("参数错误.{}").genErrorCode();
    ErrorCode COMMON_ERROR = LegendErrorHelper.newLegendError()
            .setDetailCode("4001").setMessageFormat("失败.{}").genErrorCode();
    ErrorCode NUllPOINTER_ERROR = LegendErrorHelper.newLegendError()
            .setDetailCode("4002").setMessageFormat("对象为空.{}").genErrorCode();
}
