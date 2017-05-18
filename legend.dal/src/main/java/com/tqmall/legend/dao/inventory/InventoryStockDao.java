package com.tqmall.legend.dao.inventory;



import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.inventory.InventoryStock;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface InventoryStockDao extends BaseDao<InventoryStock> {

}

