package com.tqmall.legend.facade.warehouse;

import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.entity.warehousein.WarehouseIn;
import com.tqmall.legend.entity.warehousein.WarehouseInDetail;
import com.tqmall.legend.facade.supplier.bo.SupplierBo;
import com.tqmall.legend.facade.warehouse.bo.WarehouseInDetailBo;
import com.tqmall.legend.facade.warehouse.vo.LegendWarehouseInDTOVo;
import com.tqmall.legend.facade.warehouse.vo.WarehouseInVo;
import com.tqmall.search.common.data.PageableRequest;
import com.tqmall.search.dubbo.client.legend.warehousein.param.LegendWarehouseInParam;

import java.util.List;

/**
 * Created by sven on 16/7/29.
 */
public interface WarehouseInFacade {
    /**
     * 保存草稿
     *
     * @param warehouseIn
     * @return
     */
    Long saveDraft(WarehouseIn warehouseIn, List<WarehouseInDetail> warehouseInDetailList, UserInfo userInfo);

    /**
     * 编辑草稿
     *
     * @param warehouseIn
     * @param warehouseInDetailList
     */
    void updateDraft(WarehouseIn warehouseIn, List<WarehouseInDetail> warehouseInDetailList, UserInfo userInfo);

    /**
     * 红字入库(退货)
     *
     * @param warehouseIn
     * @param warehouseInDetailList
     * @return
     */
    Long stockRefund(WarehouseIn warehouseIn, List<WarehouseInDetail> warehouseInDetailList, UserInfo userInfo);

    /**
     * 蓝字入库
     *
     * @param warehouseIn
     * @param warehouseInDetailList
     * @return
     */
    Long stock(WarehouseIn warehouseIn, List<WarehouseInDetail> warehouseInDetailList, UserInfo userInfo);


    /**
     * 作废(红字、蓝字入库)
     *
     * @param id 入库单ID
     * @param
     * @return
     */
    void abolishStock(Long id, UserInfo userInfo);

    /**
     * 根据入库单ID查询(包括详情)
     *
     * @param id     入库单ID
     * @param shopId
     * @return
     */
    WarehouseInVo select(Long id, Long shopId);

    /**
     * 获取入库单sn
     *
     * @param shopId
     * @param prefix
     * @return
     */
    String getSn(Long shopId, String prefix);

    /**
     * 详情页草稿转入库
     *
     * @param id
     * @param userInfo
     * @param push
     */
    void draftToStock(Long id, UserInfo userInfo, boolean push);

    /**
     * 编辑草稿转入库
     *
     * @param warehouseIn
     * @param warehouseInDetailList
     * @param userInfo
     * @throws BizException
     */
    void draftToStock(WarehouseIn warehouseIn, List<WarehouseInDetail> warehouseInDetailList, UserInfo userInfo);

    /**
     * 删除草稿(删除作废单子,暂时不删除付款记录、付款流水)
     *
     * @param id
     * @throws Exception
     */
    boolean delete(Long id, Long shopId) throws BizException;

    /**
     * 快修快保批量入库
     *
     * @param warehouseInDetailVoList
     */
    void batchStock(List<WarehouseInDetailBo> warehouseInDetailVoList, UserInfo userInfo);

    /**
     * 淘汽采购入库信息组装
     *
     * @param orderSn  淘汽采购订单号
     * @param uid      用户id
     * @param userInfo
     * @return
     */
    WarehouseInVo getTqmallStockInfo(String orderSn, Long uid, UserInfo userInfo);

    /**
     * 查询入库单及其详情(搜索)
     *
     * @param param           查询条件
     * @param pageableRequest 分页
     * @return
     */
    DefaultPage<LegendWarehouseInDTOVo> select(LegendWarehouseInParam param, PageableRequest pageableRequest);

    /**
     * 更新入库单供应商(仅更新供应商ID以及供应商名称)
     *
     * @param supplierBo
     */
    void updateSupplier(SupplierBo supplierBo);

    /**
     * 判断是否存在该供应商的入库记录
     *
     * @param shopId
     * @param supplierId
     * @return
     */
    boolean isSupplierExist(Long shopId, Long supplierId);
}
