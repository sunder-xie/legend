package com.tqmall.legend.biz.settlement;

import com.tqmall.core.common.entity.Result;

/**
 * Created by feilong.li on 16/10/18.
 */
public interface OnlinePaymentService {

    public Result<String> applyOnlinePayment(Long shopId);

}
