package com.tqmall.legend.biz.order;

import com.google.common.base.Optional;
import com.tqmall.legend.entity.order.OrderGoodTypeEnum;
import com.tqmall.legend.entity.order.OrderGoods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * TODO refactor
 * <p/>
 * orderGood Service Interface
 */
public interface OrderGoodsService {
    OrderGoods selectById(Long id);

    List<OrderGoods> select(Map<String, Object> map);

    List<OrderGoods> selectAndGoods(Map<String, Object> map);

    /**
     * 查询工单物料
     * TODO 对于接口入参的值可以枚举的，进行参数入参优化
     *
     * @param orderId           工单编号
     * @param shopId            门店ID
     * @param orderGoodTypeEnum 物料类型{0：实际物料，1：虚开物料}
     * @return List<OrderGoods>
     */
    List<OrderGoods> queryOrderGoodList(Long orderId, Long shopId, OrderGoodTypeEnum orderGoodTypeEnum);


    /**
     * 查询工单物料
     *
     * @param orderId 工单编号
     * @param shopId  门店ID
     * @return List<OrderGoods>
     */
    List<OrderGoods> queryOrderGoodList(Long orderId, Long shopId);


    /**
     * 查询配件关联的工单物料
     *
     * @param goodsId 配件ID
     * @param shopId  门店ID
     * @return List<OrderGoods>
     */
    List<OrderGoods> queryOrderGoodesByGoodsId(Long goodsId, Long shopId);

    /**
     * save orderGoods
     *
     * @param orderGoods
     * @return
     */
    int save(OrderGoods orderGoods);

    /**
     * update orderGoods
     *
     * @param orderGood
     * @return
     */
    int update(OrderGoods orderGood);

    /**
     * batch insert
     *
     * @param orderGoodsList
     */
    int batchInsert(List<OrderGoods> orderGoodsList);


    /**
     * batch delete
     *
     * @param ids 主键集合
     * @return
     */
    int batchDel(Object[] ids);


    /**
     * 获取工单的物料
     *
     * @param orderId 工单
     * @param shopId  门店ID
     * @return
     */
    Optional<List<OrderGoods>> getOrderGoodList(long orderId, long shopId);


    /**
     * 根据客户ID
     * 分页查询选购过的物料列表
     *
     * @param pageable
     * @param customercarid 客户ID
     * @param shopId        店铺ID
     * @return
     */
    Page<OrderGoods> getHistoryGoodList(Pageable pageable, Long customercarid, Long shopId);

    public List<OrderGoods> selectByOrderIds(Long ...orderIds);

    List<OrderGoods> selectActual(List<Long> orderIds,Long shopId);
}
