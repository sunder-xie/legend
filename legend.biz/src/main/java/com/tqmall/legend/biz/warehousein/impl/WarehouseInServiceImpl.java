package com.tqmall.legend.biz.warehousein.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.warehousein.WarehouseInService;
import com.tqmall.legend.dao.warehousein.WarehouseInDao;
import com.tqmall.legend.entity.shop.SupplierSettlementVO;
import com.tqmall.legend.entity.warehousein.WarehouseIn;
import com.tqmall.legend.facade.supplier.bo.SupplierBo;
import com.tqmall.wheel.lang.Langs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guangxue on 14/11/7.
 * updated by sven on 15/08/16.
 */
@Service
@Slf4j
public class WarehouseInServiceImpl extends BaseServiceImpl implements WarehouseInService {
    @Autowired
    private WarehouseInDao warehouseInDao;

    @Override
    public int delete(Map<String, Object> param) {
        return warehouseInDao.delete(param);
    }

    @Override
    public void deleteById(Long id, Long shopId) {
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        param.put("shopId", shopId);
        warehouseInDao.delete(param);
    }

    @Override
    public int insert(WarehouseIn warehouseIn) {
        return warehouseInDao.insert(warehouseIn);
    }

    @Override
    public int updateById(WarehouseIn warehouseIn) {
        return warehouseInDao.updateById(warehouseIn);
    }

    @Override
    public WarehouseIn selectByIdAndShopId(Long id, Long shopId) {
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        param.put("shopId", shopId);
        List<WarehouseIn> warehouseIns = warehouseInDao.select(param);
        if (CollectionUtils.isEmpty(warehouseIns)) {
            return null;
        }
        return warehouseIns.get(0);
    }

    @Override
    public List<WarehouseIn> select(Map<String, Object> map) {
        return warehouseInDao.select(map);
    }

    @Override
    public int selectCount(Map<String, Object> map) {
        return warehouseInDao.selectCount(map);
    }

    @Override
    public List<SupplierSettlementVO> statsSuppliersAmount(Map<String, Object> params) {
        return warehouseInDao.statsSuppliersAmount(params);
    }


    @Override
    public Integer getSupplierCountWarehouseIn(Map<String, Object> params) {
        return warehouseInDao.getSupplierCountWarehouseIn(params);
    }

    @Override
    public Integer update(Map<String, Object> map) {
        return warehouseInDao.update(map);
    }

    @Override
    public int updateSupplier(SupplierBo supplierBo) {
        Map<String, Object> param = Maps.newConcurrentMap();
        param.put("supplierId", supplierBo.getSupplierId());
        param.put("supplierName", supplierBo.getSupplierName());
        param.put("reqSupplierId", supplierBo.getReqSupplierId());
        param.put("shopId", supplierBo.getShopId());
        param.put("modifier", supplierBo.getOperator());
        return warehouseInDao.updateSupplier(param);
    }

    @Override
    public List<WarehouseIn> selectByPurchaseSnList(Long shopId, List<String> purchaseSnList) {
        Assert.notNull(shopId, "店铺id不能为空");
        if (Langs.isEmpty(purchaseSnList)) {
            return Lists.newArrayList();
        }
        return this.warehouseInDao.selectByPurchaseSnList(shopId, purchaseSnList);
    }

}
