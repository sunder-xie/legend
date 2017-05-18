package com.tqmall.legend.biz.warehousein;

import com.tqmall.legend.entity.warehousein.WarehouseInDetail;

import java.util.List;
import java.util.Map;

/**
 * Created by lixiao on 16/4/8.
 */
public interface WarehouseInDetailService {

    /**
     * common query
     *
     * @param searchMap
     * @return
     */
    List<WarehouseInDetail> select(Map<String, Object> searchMap);

    Integer selectCount(Map<String, Object> searchMap);

    /**
     * 查询物料数
     *
     * @param searchParams
     * @return
     */
    Integer selectGoodsCount(Map<String, Object> searchParams);

    int updateById(WarehouseInDetail warehouseInDetail);

    /**
     * 批量插入
     *
     * @param warehouseInDetails
     * @return
     */
    int batchInsert(List<WarehouseInDetail> warehouseInDetails);

    int delete(Map<String, Object> parm);

    int update(Map<String, Object> param);

    /**
     * 查询配件入库记录
     *
     * @param goodsId 配件ID
     * @param shopId  门店ID
     * @return
     */
    List<WarehouseInDetail> queryInDetail(Long goodsId, Long shopId);
}
