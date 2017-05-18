package com.tqmall.legend.biz.order;

import com.tqmall.legend.entity.order.OrderSnapshot;

/**
 * Order's Snapshot Service Interface
 *
 * Created by dongc on 15/7/22.
 */
public interface IOrderSnapshotService {

    /**
     * save orderSnapshot
     *
     * @param orderSnapshot
     * @return
     */
    int save(OrderSnapshot orderSnapshot);
}
