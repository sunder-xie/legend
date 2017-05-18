package com.tqmall.legend.biz.order;

import java.util.Date;

/**
 * Created by zsy on 16/12/9.
 * 工单统计，统计数据走slave库
 */
public interface StatisticsOrderService {
    /**
     * 最近一次到店时间
     * @param customerCarId
     * @param shopId
     * @return
     */
    Date getLastOrderDate(Long shopId, Long customerCarId);
}
