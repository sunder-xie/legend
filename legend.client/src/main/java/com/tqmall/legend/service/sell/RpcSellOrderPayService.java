package com.tqmall.legend.service.sell;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.object.param.sell.RpcSellNoticeParam;

/**
 * Created by xiangdong.qu on 17/2/23 09:41.
 */
public interface RpcSellOrderPayService {

    /**
     * finance 支付成功通知
     *
     * @param rpcSellNoticeParam
     *
     * @return
     */
    public Result<Boolean> sellOrderPaySuccessNotice(RpcSellNoticeParam rpcSellNoticeParam);
}
