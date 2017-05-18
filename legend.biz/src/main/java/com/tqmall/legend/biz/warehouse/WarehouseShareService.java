package com.tqmall.legend.biz.warehouse;

import com.tqmall.legend.entity.warehouseshare.WarehouseShare;
import com.tqmall.legend.entity.warehouseshare.WarehouseShareCountVO;
import com.tqmall.legend.pojo.warehouseshare.WarehouseShareGoodsDetail;
import com.tqmall.legend.pojo.warehouseshare.WarehouseShareVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by xin on 2016/11/23.
 */
public interface WarehouseShareService {
    /**
     * 批量插入方法
     * @param warehouseShares
     * @return
     */
    Integer batchInsert(List<WarehouseShare> warehouseShares);

    /**
     * 查询库存共享配件列表
     * @param goodsCate 配件类型
     * @param goodsName 配件名称
     * @return
     */
    Page<WarehouseShareVO> getWarehouseShareGoodsListByCondition(Long shopId,
                                                                 String goodsCate,
                                                                 String goodsName,
                                                                 int region,
                                                                 Pageable pageable);

    /**
     * 更新库存信息
     * @param warehouseShare
     * @return
     */
    Integer update(WarehouseShare warehouseShare);

    /**
     * 根据门店id和goodsStatus查询出售库存列表
     * @param shopId
     * @param goodsStatus
     * @return
     */
    List<WarehouseShare> querySaleListByGoodsStatus(Long shopId,Integer goodsStatus,Integer offset,Integer pageSize);

    /**
     * 根据门店id和出售物料状态获取数量
     * @param shopId
     * @param goodsStatus
     * @return
     */
    Integer queryCountForSaleListByGoodsStatus(Long shopId,Integer goodsStatus);

    /**
     * 查询库存共享配件详情
     * @param id
     * @return
     */
    WarehouseShareGoodsDetail getWarehouseShareGoodsDetail(Long id, Long shopId);

    /**
     * 查询门店出售列表数量
     * @param shopId
     * @return
     */
    WarehouseShareCountVO querySaleCount(Long shopId);

    /**
     * 根据门店ID查询已经存在的列表
     * @param shopId
     * @return
     */
    List<Long> queryExistGoodsId(Long shopId);

    /**
     * 查询库存共享配件列表
     * @param goodsCate 配件类型
     * @param goodsName 配件名称
     * @param status 审核状态
     * @return
     */
    Page<WarehouseShare> searchWarehouseSharePage(String goodsCate,
                                                  String goodsName,
                                                  Long shopId,
                                                  Integer status,
                                                  Integer pageNum,
                                                  Integer pageSize);

    boolean checkPass(Long id);

    boolean checkNotPass(Long id, String remark);
}
