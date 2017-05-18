package com.tqmall.error;

import com.tqmall.zenith.errorcode.ErrorCodeBuilder;
import com.tqmall.zenith.errorcode.ErrorLevel;
import com.tqmall.zenith.errorcode.PlatformErrorCode;

/**
 * Created by wanghui on 1/11/16.
 *
 * 避免冲突，模块错误码分开
 *
 * 搜索模块：60XX
 *
 */
public class LegendErrorHelper {

    public static ErrorCodeBuilder newLegendError() {
        return ErrorCodeBuilder.newError(PlatformErrorCode.YUNXIU_LEGEND, "00").setErrorLevel(ErrorLevel.ERROR);
    }
    public static ErrorCodeBuilder newLegendFatal() {
        return ErrorCodeBuilder.newError(PlatformErrorCode.YUNXIU_LEGEND, "00").setErrorLevel(ErrorLevel.FATAL);
    }
    public static ErrorCodeBuilder newLegendWarn() {
        return ErrorCodeBuilder.newError(PlatformErrorCode.YUNXIU_LEGEND, "00").setErrorLevel(ErrorLevel.WARN);
    }
}