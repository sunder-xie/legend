package com.tqmall.error;

import com.tqmall.zenith.errorcode.ErrorCode;

/**
 * Created by zsy on 16/4/26.
 *
 * 搜索模块
 */
public class LegendSearchErrorCode {
    public static final ErrorCode ORDER_HISTORY_SEARCH_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6001").setMessageFormat("搜索导入工单列表失败").genErrorCode();
    public static final ErrorCode GOODS_SEARCH_ERROR =
            LegendErrorHelper.newLegendError().setDetailCode("6002").setMessageFormat("搜索配件失败").genErrorCode();
}
