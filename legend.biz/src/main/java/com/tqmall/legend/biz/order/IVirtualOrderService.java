package com.tqmall.legend.biz.order;

import com.google.common.base.Optional;
import com.tqmall.legend.entity.order.VirtualOrder;

/**
 * Created by dongc on 15/9/11.
 */
public interface IVirtualOrderService {

    /**
     * save virtualOrder
     *
     * @param orderInfo
     * @return
     */
    int save(VirtualOrder orderInfo);

    /**
     * update virtualOrder
     *
     * @param virtualOrder
     * @return
     */
    int update(VirtualOrder virtualOrder);

    /**
     * get VirtualOrder
     *
     * @param orderId 工单ID
     * @return
     */
    Optional<VirtualOrder> getOrderById(Long orderId);

    /**
     * is Exist virtualOrder
     *
     * @param parentId
     * @return
     */
    Long isExistVirtualOrder(Long parentId);

    /**
     * delete virtual's order
     *
     * @param orderId
     * @return
     */
    int delete(Long orderId);
}
