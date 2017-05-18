package com.tqmall.legend.biz.warehousein.impl;

import com.tqmall.legend.biz.warehousein.WarehouseInDetailService;
import com.tqmall.legend.dao.warehousein.WarehouseInDetailDao;
import com.tqmall.legend.entity.warehousein.WarehouseInDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lixiao on 16/4/8.
 */
@Slf4j
@Service
public class WarehouseInDetailServiceImpl implements WarehouseInDetailService {

    @Autowired
    private WarehouseInDetailDao warehouseInDetailDao;

    @Override
    public List<WarehouseInDetail> select(Map<String, Object> searchMap) {
        return warehouseInDetailDao.select(searchMap);
    }

    @Override
    public Integer selectCount(Map<String, Object> searchMap) {
        return warehouseInDetailDao.selectCount(searchMap);
    }

    @Override
    public int updateById(WarehouseInDetail warehouseInDetail) {
        return warehouseInDetailDao.updateById(warehouseInDetail);
    }

    @Override
    public int update(Map<String, Object> param) {
        return warehouseInDetailDao.update(param);
    }

    @Override
    public List<WarehouseInDetail> queryInDetail(@NotNull Long goodsId, Long shopId) {
        Map<String, Object> searchMap = new HashMap<String, Object>(2);
        searchMap.put("shopId", shopId);
        searchMap.put("goodsId", goodsId);
        List<WarehouseInDetail> warehouseInDetails = null;
        try {
            warehouseInDetails = warehouseInDetailDao.select(searchMap);
        } catch (Exception e) {
            log.error("[DB]获取配件入库明细异常 配件ID:{} 异常信息:{}", goodsId, e);
            return new ArrayList<WarehouseInDetail>();
        }

        if (warehouseInDetails == null) {
            warehouseInDetails = new ArrayList<WarehouseInDetail>();
        }

        return warehouseInDetails;
    }

    @Override
    public int delete(Map<String, Object> parm) {
        return warehouseInDetailDao.delete(parm);
    }

    @Override
    public int batchInsert(List<WarehouseInDetail> warehouseInDetails) {
        return warehouseInDetailDao.batchInsert(warehouseInDetails);
    }

    @Override
    public Integer selectGoodsCount(Map<String, Object> searchParams) {
        return warehouseInDetailDao.selectGoodsCount(searchParams);
    }
}
