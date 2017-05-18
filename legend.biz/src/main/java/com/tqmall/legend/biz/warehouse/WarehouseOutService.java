package com.tqmall.legend.biz.warehouse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tqmall.common.UserInfo;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.order.OrderInfo;
import com.tqmall.legend.entity.warehouseout.WarehouseOut;
import com.tqmall.legend.entity.warehouseout.WarehouseOutDetail;
import com.tqmall.legend.pojo.warehouseOut.WarehouseOutDetailVO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by zwb on 14/10/29.
 */
public interface WarehouseOutService {


    /**
     *
     * @param goodsId
     * @param shopId
     * @param orderId
     * @param orderGoodsId
     * @return
     */
    public BigDecimal countOutGoods(Long goodsId, Long shopId, Long orderId, Long orderGoodsId);

    /**
     * @param goodsId
     * @param shopId
     * @param orderId
     * @param orderGoodsId
     * @return
     */
    public List<WarehouseOutDetail> getWarehouseDetail(Long goodsId, Long shopId, Long orderId, Long orderGoodsId);


    /**
     * @param searchParams
     * @return
     */
    public Result blueInvalid(Map<String, Object> searchParams, UserInfo user);

    /**
     * @param searchParams
     * @return
     */
    public Result redInvalid(Map<String, Object> searchParams, Long userId);



    /**
     * 获取某条物料出库记录的信息
     *
     * @param goodsId
     * @param shopId
     * @param orderId
     * @param orderGoodsId
     * @return
     */
    public WarehouseOutDetailVO getWarehouseDetailForGoods(Long goodsId, Long shopId, Long orderId, Long orderGoodsId);


    /**
     * 出库单表通用查询
     *
     * @param searchMap
     * @return
     */
    public List<WarehouseOut> select(Map<String, Object> searchMap);


    /**
     * 工单物料出库
     *
     * @param orderInfo 工单实体
     * @param userInfo  当前操作人
     * @return {true:出库成功;false:出库失败}
     */
    Result stackOut(OrderInfo orderInfo, UserInfo userInfo);

    /**
     * 物料是否全部出库
     *
     * @param orderInfo 工单实体
     * @param userInfo  当前操作人
     * @return {true:全部出库;false:未全部出库}
     */
    boolean isAllStackOut(OrderInfo orderInfo, UserInfo userInfo);

    /**
     * 判断工单物料是否缺货
     *
     * @param orderInfo 工单实体
     * @param userInfo  当前操作人
     * @return {true:缺货;false：不缺货}
     */
    boolean isStockOut(OrderInfo orderInfo, UserInfo userInfo);


    /**
     * 保存出库单
     *
     * @param warehouseOut 出库单实体
     * @return
     */
    int save(WarehouseOut warehouseOut);

    /**
     * 物料出库单作废
     *
     * @param orderId
     * @param userInfo
     * @return
     */
    Result invalid(Long orderId, UserInfo userInfo);

    int insert(WarehouseOut warehouseOut);

    WarehouseOut selectByIdAndShopId(Long id, Long shopId);

    int selectCount(Map<String, Object> param);

    int updateById(WarehouseOut warehouseOut);

    int deleteByShopIdAndId(Long shopId,Long id);

    Map<Long,BigDecimal> mapOrderId2realInventoryAmount(Long shopId, List<Long> orderIds);
}
