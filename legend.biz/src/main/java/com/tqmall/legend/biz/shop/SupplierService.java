package com.tqmall.legend.biz.shop;

import com.tqmall.legend.entity.shop.Supplier;

import java.util.List;
import java.util.Map;

/**
 * Created by zwb on 14/10/29.
 */
public interface SupplierService {

    List<Supplier> select(Map<String, Object> searchMap);

    void batchSave(List<Supplier> suppliers);

    /**
     * 通过供应商编号，获取供应商列表
     * @param shopId
     * @param supplierSn
     * @return
     */
    List<Supplier> findBySupplierSns(Long shopId,List<String> supplierSn);
}
