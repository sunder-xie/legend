package com.tqmall.legend.biz.warehouse;

import com.tqmall.legend.entity.warehouseout.WarehouseOutDetail;

import java.util.List;
import java.util.Map;

/**
 * 出库单明细Service
 */
public interface IWarehouseOutDetailService {

    /**
     * batchInsert warehouseOutDetail
     *
     * @param toInsertedWarehouseOutDetail 待插入明细集合
     * @return
     */
    int batchInsert(List<WarehouseOutDetail> toInsertedWarehouseOutDetail);

    /**
     * @param searchMap
     * @return
     */
    List<WarehouseOutDetail> select(Map<String, Object> searchMap);


    Integer selectCount(Map<String, Object> searchMap);

    /**
     * 查询物料数
     *
     * @param searchParams
     * @return
     */
    Integer selectGoodsCount(Map<String, Object> searchParams);

    int updateById(WarehouseOutDetail warehouseOutDetail);

    int update(Map<String, Object> param);

    int delete(Map<String, Object> param);


    /**
     * 查询配件入库记录
     *
     * @param goodsId 配件ID
     * @param shopId  门店ID
     * @return
     */
    List<WarehouseOutDetail> queryOutDetail(Long goodsId, Long shopId);
}
