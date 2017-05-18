package com.tqmall.legend.biz.sell;

import com.tqmall.legend.biz.base.BaseService;
import com.tqmall.legend.entity.sell.SellOrderPayLog;

/**
 * Created by xiangdong.qu on 17/2/23 09:31.
 */
public interface SellOrderPayLogService extends BaseService {
    /**
     * 新建记录
     *
     * @param sellOrderPayLog
     *
     * @return
     */
    public SellOrderPayLog insertLog(SellOrderPayLog sellOrderPayLog);
}
