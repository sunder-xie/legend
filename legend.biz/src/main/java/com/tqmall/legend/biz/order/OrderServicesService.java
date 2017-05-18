package com.tqmall.legend.biz.order;

import com.tqmall.legend.entity.order.OrderServiceTypeEnum;
import com.tqmall.legend.entity.order.OrderServices;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 工单服务service Interface
 */
public interface OrderServicesService {

    public List<OrderServices> select(Map<String, Object> map);

    /**
     * 查询工单服务
     *
     * @param orderId         工单编号
     * @param shopId          门店ID
     * @param serviceTypeEnum {1:基本服务，2:附属服务}
     * @return List&lt;OrderServices&gt;
     */
    List<OrderServices> queryOrderServiceList(Long orderId, Long shopId, OrderServiceTypeEnum serviceTypeEnum);

    /**
     * 查询工单服务
     *
     * @param orderId         工单编号
     * @param shopId          门店ID
     * @param serviceTypeEnum {1:基本服务，2:附属服务}
     */
    List<OrderServices> queryOrderServiceList(Long orderId, Long shopId, OrderServiceTypeEnum serviceTypeEnum,Map map);

    /**
     * 查询工单服务
     *
     * @param orderId 工单编号
     * @param shopId  门店ID
     * @return List&lt;OrderServices&gt;
     */
    List<OrderServices> queryOrderServiceList(Long orderId, Long shopId);

    /**
     * update OrderServices
     *
     * @param newOrderServices
     * @return
     */
    int update(OrderServices newOrderServices);

    /**
     * 根据工单ID获取淘汽一级分类ID
     *
     * @param orderId
     * @return
     */
    List<String> getTaoqiFirstCateIds(Long orderId);

    /**
     * batch save
     *
     * @param orderServicesList
     * @return
     */
    int batchSave(List<OrderServices> orderServicesList);

    /**
     * bacth delete
     *
     * @param ids 主键集合
     * @return
     */
    int batchDel(Object[] ids);

    /**
     * 批量查询工单服务
     * @param shopId
     * @param orderIds
     * @return key为orderId, value为serviceName,用顿号隔开
     */
    Map<Long, String> getServiceNamesByOrderIds(Long shopId, List<Long> orderIds);

    /**
     * 统计服务的转工单数
     * key = serviceId
     * value = orderUsageCount(转工单数)
     * @return
     */
    Map<Long, Integer> getServiceOrderUsageMap(Collection<Long> serviceIds);

    Map<Long, Integer> getServiceConfirmedOrderMap(Collection<Long> serviceIds);

    /**
     * 获取相应工单的基本 服务信息
     *
     * @param shopId
     * @param orderIds
     * @param serviceType
     * @return
     */
    List<OrderServices> selOrderServicesByOrderIds(Long shopId, List<Long> orderIds, int serviceType);
}
