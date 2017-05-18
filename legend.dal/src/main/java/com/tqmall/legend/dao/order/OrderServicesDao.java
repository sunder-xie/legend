package com.tqmall.legend.dao.order;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.order.OrderServices;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

@MyBatisRepository
public interface OrderServicesDao extends BaseDao<OrderServices> {
    /*
    * 获取相应工单的基本 服务信息
    * */
    public List<OrderServices> selOrderServicesByOrderIds(@Param("shopId") Long shopId, @Param("orderIds") List<Long> orderIds, @Param("serviceType") int serviceType);

    /**
     * 批量查询工单服务
     * @param shopId
     * @param orderIds
     * @return orderId, serviceName
     */
    List<OrderServices> getServiceNamesByOrderIds(@Param("shopId") Long shopId, @Param("orderIds") List<Long> orderIds);

    List<OrderServices> selectByServiceIds(@Param("serviceIds") Collection<Long> serviceIds);
}
