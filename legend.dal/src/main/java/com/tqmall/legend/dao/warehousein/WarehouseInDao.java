package com.tqmall.legend.dao.warehousein;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.shop.SupplierSettlementVO;
import com.tqmall.legend.entity.warehousein.PagedWarehouseInDetail;
import com.tqmall.legend.entity.warehousein.WarehouseIn;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface WarehouseInDao extends BaseDao<WarehouseIn> {

    int update(Map<String, Object> param);


    List<SupplierSettlementVO> statsSuppliersAmount(Map<String, Object> params);

    Integer getSupplierCountWarehouseIn(Map<String, Object> params);

    /**
     * 更新入库单供应商
     *
     * @param param
     * @return
     */
    int updateSupplier(Map<String, Object> param);

    List<WarehouseIn> selectByPurchaseSnList(@Param("shopId") Long shopId, @Param("PurchaseSnList") Iterable<String> purchaseSnList);

}
