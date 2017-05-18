package com.tqmall.legend.biz.warehousein;


import com.tqmall.legend.entity.shop.SupplierSettlementVO;
import com.tqmall.legend.entity.warehousein.WarehouseIn;
import com.tqmall.legend.entity.warehousein.WarehouseInDetail;
import com.tqmall.legend.facade.supplier.bo.SupplierBo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by guangxue on 14/11/7.
 */
public interface WarehouseInService {

    List<WarehouseIn> select(Map<String, Object> map);

    int selectCount(Map<String, Object> map);

    List<SupplierSettlementVO> statsSuppliersAmount(Map<String, Object> params);

    Integer getSupplierCountWarehouseIn(Map<String, Object> params);


    Integer update(Map<String, Object> map);

    /**
     * 根据ID查询
     */
    WarehouseIn selectByIdAndShopId(Long id, Long shopId);

    /**
     * 根据id更新
     */
    int updateById(WarehouseIn warehouseIn);

    int insert(WarehouseIn warehouseIn);

    void deleteById(Long id, Long shopId);

    int delete(Map<String, Object> param);

    int updateSupplier(SupplierBo supplierBo);

    List<WarehouseIn> selectByPurchaseSnList(Long shopId, List<String> purchaseSnList);
}
