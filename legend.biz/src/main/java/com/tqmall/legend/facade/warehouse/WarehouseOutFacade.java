package com.tqmall.legend.facade.warehouse;

import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.entity.order.OrderGoods;
import com.tqmall.legend.entity.warehouseout.WarehouseOut;
import com.tqmall.legend.entity.warehouseout.WarehouseOutDetail;
import com.tqmall.legend.facade.order.vo.SearchOrderVo;
import com.tqmall.legend.facade.warehouse.vo.LegendWarehouseOutDTOVo;
import com.tqmall.legend.facade.warehouse.vo.WarehouseOutRefundVo;
import com.tqmall.legend.facade.warehouse.vo.WarehouseOutVo;
import com.tqmall.search.common.data.PageableRequest;
import com.tqmall.search.dubbo.client.legend.order.param.LegendOrderRequest;
import com.tqmall.search.dubbo.client.legend.warehouseout.param.LegendWarehouseOutRequest;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * Created by sven on 16/8/23.
 */
public interface WarehouseOutFacade {
    /**
     * 出库
     *
     * @param warehouseOut
     * @param
     * @return
     * @throws BizException
     */
    Long stockOut(WarehouseOut warehouseOut, List<WarehouseOutDetail> detailList, UserInfo userInfo) throws BizException;

    /**
     * 退货(红字出库)
     *
     * @param warehouseOut
     * @param
     * @return
     * @throws BizException
     */
    Long stockOutRefund(WarehouseOut warehouseOut, List<WarehouseOutDetail> detailList, UserInfo userInfo) throws BizException;

    /**
     * 出库作废(红字、蓝字作废)
     *
     * @param id
     * @param userInfo
     * @return
     * @throws BizException
     */
    void abolishStockOut(Long id, UserInfo userInfo) throws BizException;

    /**
     * 查询封装后的出库单详情(出库单,出库单详情,库存数,开单人,领料人)
     *
     * @param id
     * @param shopId
     * @return
     */
    WarehouseOutVo select(Long id, Long shopId);

    /**
     * 获取出库单Sn
     *
     * @param shopId
     * @param prefix
     * @return
     */
    String getSn(Long shopId, String prefix);

    /**
     * 查询出库单列表(搜索)
     *
     * @param param
     * @param pageableRequest
     * @param shopId
     * @return
     */
    DefaultPage<LegendWarehouseOutDTOVo> select(LegendWarehouseOutRequest param, PageableRequest pageableRequest, Long shopId);

    /**
     * 查询未出库的工单物料
     *
     * @param orderId
     * @param shopId
     * @return
     */
    List<OrderGoods> selectTraceOrderGoods(Long orderId, Long shopId);

    /**
     * 查询待报价工单列表
     *
     * @param orderRequest
     * @param pageable
     * @return
     */

    DefaultPage<SearchOrderVo> selectOrderQuoteList(LegendOrderRequest orderRequest, PageableRequest pageable);

    /**
     * 查询工单出库列表(搜索)
     *
     * @param orderRequest
     * @param pageable
     * @return
     */
    DefaultPage<SearchOrderVo> selectOrderOutList(LegendOrderRequest orderRequest, PageableRequest pageable);

    /**
     * 根据工单ID查询所有出库物料(蓝字出库)
     *
     * @param orderId
     * @param shopId
     * @return
     */
    List<WarehouseOutDetail> selectByOrderIdAndShopId(Long orderId, Long shopId);

    /**
     * 批量更新出库单详情
     *
     * @param detailList
     * @param userInfo
     */
    void batchUpdateWarehouseDetail(List<WarehouseOutDetail> detailList, UserInfo userInfo);

    /**
     * 删除出库记录(仅适用作废单)
     *
     * @param shopId
     * @param id
     */
    void delete(Long shopId, Long id);

    /**
     * 根据车辆查询退货记录
     *
     * @param customerCarId
     * @param shopId
     * @return
     */
    DefaultPage<WarehouseOutRefundVo> selectOutRefund(Long customerCarId, Long shopId, PageRequest pageRequest);
}
