package com.tqmall.legend.biz.shop.impl;

import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.shop.SupplierService;
import com.tqmall.legend.dao.shop.SupplierDao;
import com.tqmall.legend.entity.shop.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by zwb on 14/10/29.
 */
@Service
public class SupplierServiceImpl extends BaseServiceImpl implements SupplierService {

    @Autowired
    SupplierDao supplierDao;

    public List<Supplier> select(Map<String, Object> searchMap) {
        return supplierDao.select(searchMap);
    }

    @Override
    public void batchSave(List<Supplier> suppliers) {
        super.batchInsert(supplierDao, suppliers, 1000);
    }

    @Override
    public List<Supplier> findBySupplierSns(Long shopId, List<String> supplierSn) {
        return supplierDao.findBySupplierSns(shopId, supplierSn);
    }
}
