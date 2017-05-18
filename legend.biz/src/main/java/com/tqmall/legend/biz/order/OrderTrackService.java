package com.tqmall.legend.biz.order;

import com.tqmall.common.UserInfo;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.order.OrderInfo;
import com.tqmall.legend.entity.order.OrderStatusEnum;
import com.tqmall.legend.entity.order.OrderTrack;

/**
 * TODO 合并到iorderservice
 */
public interface OrderTrackService {

    public Result tasking(Long orderId, String orderSn, String orderStatus, UserInfo userInfo);

    public Result finish(Long orderId, String orderSn, String orderStatus, UserInfo userInfo);

    public Result pricing(Long orderId, String orderSn, String orderStatus, UserInfo userInfo);

    public Result track(OrderInfo orderInfo, UserInfo userInfo);

    /**
     * save orderTrack
     *
     * @param orderTrack
     * @return
     */
    int save(OrderTrack orderTrack);

    /**
     * 记录工单流水
     *
     * @param orderId  工单ID
     * @param shopId   门店ID
     * @param ddsg     工单状态
     * @param userInfo 当前操作用户
     * @return
     */
    int record(Long orderId, Long shopId, OrderStatusEnum ddsg, UserInfo userInfo);

    int insert(OrderTrack orderTrack);
}
