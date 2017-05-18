package com.tqmall.legend.biz.order;

import com.tqmall.legend.entity.order.VirtualOrderservice;

import java.util.List;
import java.util.Set;

/**
 * Created by dongc on 15/9/11.
 */
public interface IVirtualOrderserviceService {

    /**
     * save virtual orderService
     *
     * @param orderServices
     * @return
     */
    int save(VirtualOrderservice orderServices);

    /**
     * update virtual orderService
     *
     * @param virtualOrderservice
     * @return
     */
    int update(VirtualOrderservice virtualOrderservice);


    /**
     * 查询工单基本服务
     *
     * @param orderId     工单编号
     * @param serviceType {1:基本服务，2:附属服务}
     * @param shopId      门店ID
     * @return List&lt;OrderServices&gt;
     */
    List<VirtualOrderservice> queryOrderServices(Long orderId, int serviceType, Long shopId);


    /**
     * 查询工单所有服务
     *
     * @param orderId     工单编号
     * @param shopId      门店ID
     * @return List&lt;OrderServices&gt;
     */
    List<VirtualOrderservice> queryOrderServices(Long orderId, Long shopId);

    /**
     * 删除工单所有服务
     * @param serviceIdSet
     * @return
     */
    int deleteByIds(Set<Long> serviceIdSet);

}
