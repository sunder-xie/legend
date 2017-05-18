package com.tqmall.legend.service.onlinepay;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.object.param.onlinepay.CallbackParam;

/**
 * Created by wanghui on 3/16/16.
 */
public interface RpcOnlinePayCallbackService {
    /**
     * 支付成功之后的回调函数
     * @param param
     * @return
     */
    Result<String> callback(CallbackParam param);

    /**
     * 通用的支付回写的方法
     * @param param
     * @return
     */
    Result<String> callbackCommon(CallbackParam param);
}
