package com.tqmall.legend.biz.warehouse.impl;

import com.tqmall.legend.biz.warehouse.IWarehouseOutDetailService;
import com.tqmall.legend.dao.warehouseout.WarehouseOutDetailDao;
import com.tqmall.legend.entity.warehouseout.WarehouseOutDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 配件出库单明细
 */
@Slf4j
@Service
public class WarehouseOutDetailServiceImpl implements IWarehouseOutDetailService {

    @Autowired
    private WarehouseOutDetailDao warehouseOutDetailDao;

    @Override
    public int batchInsert(List<WarehouseOutDetail> warehouseOutDetailList) {
        return warehouseOutDetailDao.batchInsert(warehouseOutDetailList);
    }

    @Override
    public List<WarehouseOutDetail> select(Map<String, Object> searchMap) {
        return warehouseOutDetailDao.select(searchMap);
    }

    @Override
    public Integer selectCount(Map<String, Object> searchMap) {
        return warehouseOutDetailDao.selectCount(searchMap);
    }

    @Override
    public int updateById(WarehouseOutDetail warehouseOutDetail) {
        return warehouseOutDetailDao.updateById(warehouseOutDetail);
    }

    @Override
    public Integer selectGoodsCount(Map<String, Object> searchParams) {
        return warehouseOutDetailDao.selectGoodsCount(searchParams);
    }

    @Override
    public int update(Map<String, Object> param) {
        return warehouseOutDetailDao.update(param);
    }

    @Override
    public int delete(Map<String, Object> param) {
        return warehouseOutDetailDao.delete(param);
    }

    @Override
    public List<WarehouseOutDetail> queryOutDetail(Long goodsId, Long shopId) {
        Map<String, Object> searchMap = new HashMap<String, Object>(2);
        searchMap.put("shopId", shopId);
        searchMap.put("goodsId", goodsId);
        List<WarehouseOutDetail> warehouseOutDetails = null;
        try {
            warehouseOutDetails = warehouseOutDetailDao.select(searchMap);
        } catch (Exception e) {
            log.error("[DB]获取配件出库明细异常 配件ID:{} 异常信息:{}", goodsId, e);
            return new ArrayList<WarehouseOutDetail>();
        }

        if (warehouseOutDetails == null) {
            warehouseOutDetails = new ArrayList<WarehouseOutDetail>();
        }

        return warehouseOutDetails;
    }

}
