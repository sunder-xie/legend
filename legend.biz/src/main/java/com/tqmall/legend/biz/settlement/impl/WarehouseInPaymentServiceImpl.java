package com.tqmall.legend.biz.settlement.impl;

import com.tqmall.legend.biz.settlement.WarehouseInPaymentService;
import com.tqmall.legend.dao.settlement.WarehouseInPaymentDao;
import com.tqmall.legend.entity.settlement.WarehouseInPayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by sven on 16/6/19.
 */
@Service
public class WarehouseInPaymentServiceImpl implements WarehouseInPaymentService {
    @Autowired
    private WarehouseInPaymentDao warehouseInPaymentDao;

    @Override
    public Long insert(WarehouseInPayment warehouseInPayment) {
        warehouseInPaymentDao.insert(warehouseInPayment);
        return warehouseInPayment.getId();
    }

    @Override
    public void batchInsert(List<WarehouseInPayment> warehouseInPaymentList) {
        warehouseInPaymentDao.batchInsert(warehouseInPaymentList);
    }

    @Override
    public int deleteById(Long id) {
        return warehouseInPaymentDao.deleteById(id);
    }

    @Override
    public int delete(Map<String, Object> param) {
        return warehouseInPaymentDao.delete(param);
    }

    @Override
    public List<WarehouseInPayment> select(Map<String, Object> map) {
        return warehouseInPaymentDao.select(map);
    }
}
