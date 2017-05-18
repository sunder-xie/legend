package com.tqmall.legend.biz.settlement;

import com.tqmall.legend.entity.settlement.WarehouseInPayment;

import java.util.List;
import java.util.Map;

/**
 * Created by sven on 16/6/19.
 */
public interface WarehouseInPaymentService {
    List<WarehouseInPayment> select(Map<String, Object> map);

    Long insert(WarehouseInPayment warehouseInPayment);

    void batchInsert(List<WarehouseInPayment> warehouseInPaymentList);

    int deleteById(Long id);

    int delete(Map<String,Object> param);
}
