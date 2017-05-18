package com.tqmall.legend.facade.settlement;

import com.tqmall.common.UserInfo;
import com.tqmall.legend.common.Result;
import com.tqmall.magic.object.result.proxy.ProxyDTO;

/**
 * Created by zsy on 16/5/23.
 */
public interface ShareSettlementFacade {
    /**
     * 共享中心对账，工单结算接口
     * @param proxyDTO 委托单信息
     * @param userInfo
     * @return
     */
    public Result shareOrderSettle(ProxyDTO proxyDTO, UserInfo userInfo);
}
