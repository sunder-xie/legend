package com.tqmall.legend.biz.order;


import com.google.common.base.Optional;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BusinessCheckedException;
import com.tqmall.legend.biz.order.vo.OrderServicesVo;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.order.OrderCategoryEnum;
import com.tqmall.legend.entity.order.OrderExpenseEntity;
import com.tqmall.legend.entity.order.OrderFormEntityBo;
import com.tqmall.legend.entity.order.OrderGoods;
import com.tqmall.legend.entity.order.OrderInfo;
import com.tqmall.legend.entity.order.OrderPrecheckDetails;
import com.tqmall.legend.entity.order.OrderServices;
import com.tqmall.legend.entity.order.OrderStatusEnum;
import com.tqmall.legend.entity.order.WashCarStats;

import java.util.List;
import java.util.Map;


/**
 * order service interface
 *
 * @see com.tqmall.legend.biz.order.impl.OrderServiceImpl
 */
public interface IOrderService {

    /**
     * calculate order expense
     *
     * @param orderGoodsList    物料列表
     * @param orderServicesList 服务列表
     * @return
     */
    OrderExpenseEntity calculateOrderExpense(List<OrderGoods> orderGoodsList, List<OrderServices> orderServicesList);

    /**
     * calculate virtual order amount
     *
     * @param orderGoodsMapList    选定物料列表
     * @param orderServicesMapList 选定服务列表
     * @return
     */
    Result virtualCalcPrice(List<Map<String, Object>> orderGoodsMapList, List<Map<String, Object>> orderServicesMapList);

    /**
     * calculate order amount
     *
     * @param orderInfo        工单基础信息
     * @param orderServiceList 选定服务列表
     */
    void calculateOrderExpense(OrderInfo orderInfo, List<OrderServices> orderServiceList);

    /**
     * calculate order amount
     *
     * @param orderInfo        工单基础信息
     * @param orderGoodsList   选定物料列表
     * @param orderServiceList 选定服务列表
     */
    void calculateOrderExpense(OrderInfo orderInfo, List<OrderGoods> orderGoodsList, List<OrderServices> orderServiceList);


    /**
     * 工单表单 保存
     *
     * @param orderFormEntityBo 工单表单实体
     * @param userInfo          当前登录用户
     * @return Result
     * @throws BusinessCheckedException
     */
    Result save(OrderFormEntityBo orderFormEntityBo, UserInfo userInfo) throws BusinessCheckedException;


    /**
     * save orderinfo
     *
     * @param orderInfo         工单基本信息
     * @param orderServicesList 工单服务列表
     * @param userInfo
     * @return
     */
    OrderInfo save(OrderInfo orderInfo, List<OrderServices> orderServicesList, UserInfo userInfo);


    /**
     * 工单表单 更新
     *
     * @param orderFormEntityBo 工单表单实体
     * @param userInfo          当前登录用户
     * @return Result
     * @throws BusinessCheckedException
     */
    Result update(OrderFormEntityBo orderFormEntityBo, UserInfo userInfo, boolean isUseNewPrecheck) throws BusinessCheckedException;


    /**
     * update warehouse's order
     *
     * @param orderFormEntityBo 工单表单实体
     * @param userInfo          当前登录用户
     * @return Result
     * @throws BusinessCheckedException
     */
    Result updateOrderOfWareHouse(OrderFormEntityBo orderFormEntityBo, UserInfo userInfo) throws BusinessCheckedException;


    /**
     * save virtual order
     *
     * @param orderFormEntityBo 工单表单实体
     * @param userInfo          当前登录用户
     * @return Result
     * @throws BusinessCheckedException
     */
    Result virtualSave(OrderFormEntityBo orderFormEntityBo, UserInfo userInfo) throws BusinessCheckedException;

    /**
     * update virtual order
     *
     * @param orderFormEntityBo 工单表单实体
     * @param userInfo          当前登录用户
     * @return Result
     * @throws BusinessCheckedException
     */
    Result virtualUpdate(OrderFormEntityBo orderFormEntityBo, UserInfo userInfo) throws BusinessCheckedException;


    /**
     * delete virtual order
     *
     * @param orderId  工单ID
     * @param userInfo 当前登录用户
     * @throws BusinessCheckedException
     */
    void virtualDelete(Long orderId, UserInfo userInfo) throws BusinessCheckedException;


    /**
     * statistics Order‘s Number By orderCategory
     *
     * @param shopId            门店ID
     * @param orderCategoryEnum 工单类别
     * @return int
     * @See OrderCategoryEnum
     */
    int statisticsOrderNumber(long shopId, OrderCategoryEnum orderCategoryEnum);


    /**
     * get order by primary key(id)
     *
     * @param orderId 工单ID
     * @return Optional<OrderInfo>
     */
    Optional<OrderInfo> getOrder(Long orderId);

    /**
     * get order by primary key(id) and shopId
     *
     * @param orderId 工单ID
     * @param shopId  门店ID
     * @return Optional<OrderInfo>
     */
    Optional<OrderInfo> getOrder(Long orderId, Long shopId);


    /**
     * update order's orderStatus
     *
     * @param orderId    工单ID
     * @param statusEnum 工单状态Enum
     * @return {1:success；0：failure}
     * @see OrderStatusEnum
     */
    int updateOrderStatus(long orderId, OrderStatusEnum statusEnum);


    /**
     * update orderInfo
     *
     * @param orderInfo 工单实体
     * @return {1:success；0：failure}
     */
    int updateOrder(OrderInfo orderInfo);


    /**
     * 获取门店最近一次洗车单信息
     *
     * @param shopId
     * @return
     */
    OrderServicesVo getLastCarwash(Long shopId);

    /**
     * 获取洗车单今日统计
     *
     * @param shopId
     * @return
     */
    public WashCarStats getWashCarTodayStats(Long shopId);

    /**
     * 根据orderIds和shopId获取工单列表
     *
     * @param orderIds
     * @param shopId
     * @return
     */
    List<OrderInfo> selectByIdsAndShopId(List<Long> orderIds, Long shopId);

    /**
     * 逻辑删除工单
     *
     * @param orderId
     * @return
     */
    int delete(Long orderId);

    /**
     * 获取客户历史工单列表
     *
     * @param customerCarId 客户车辆ID
     * @param shopId        门店ID
     * @return List<OrderInfo>
     */
    List<OrderInfo> getHistoryOrderByCustomerCarId(Long customerCarId, Long shopId);

    /**
     * TODO 2015-08-16 无意思接口定义,明确入参
     *
     * 查询数量 by dbao
     *
     * @param params
     * @return
     */
    int selectCount(Map<String, Object> params);

    /**
     * TODO 2015-08-16 注释不够完美,缺少参数说明
     *
     * 保存工单基本信息、选购商品、选购服务
     *
     * @param orderInfo
     * @param orderGoodsList
     * @param orderServicesList
     * @param userInfo
     */
    void insertOrder(OrderInfo orderInfo, List<OrderGoods> orderGoodsList, List<OrderServices> orderServicesList, List<OrderPrecheckDetails> orderPrecheckDetailsList, UserInfo userInfo);
}