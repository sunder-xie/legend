package com.tqmall.legend.biz.order.impl;

import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.order.IOrderSnapshotService;
import com.tqmall.legend.dao.order.OrderSnapshotDao;
import com.tqmall.legend.entity.order.OrderSnapshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * OrderSnapshot Service implement
 *
 * Created by dongc on 15/7/22.
 */
@Service
public class OrderSnapshotServiceImpl extends BaseServiceImpl implements IOrderSnapshotService{

    @Autowired
    private OrderSnapshotDao orderSnapshotDao;

    @Override
    public int save(OrderSnapshot orderSnapshot) {
        return orderSnapshotDao.insert(orderSnapshot);
    }
}
