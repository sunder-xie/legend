package com.tqmall.legend.dao.inventory;


import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.inventory.InventoryRecord;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface InventoryRecordDao extends BaseDao<InventoryRecord> {


    /**
     * 库存分页查询
     *
     * @param searchParams
     * @return
     */
    Integer selectCountInventoryPage(Map<String, Object> searchParams);

    /**
     * 库存分页查询总数
     *
     * @param searchParams
     * @return
     */
    List<InventoryRecord> selectInventoryPage(Map<String, Object> searchParams);
}
