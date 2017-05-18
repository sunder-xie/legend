package com.tqmall.legend.facade.order;

import com.tqmall.common.UserInfo;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.order.OrderInfo;

/**
 * Created by xin on 16/4/25.
 */
public interface OrderInvalidFacade {
    /**
     * 工单无效
     * 步骤:
     * 1.工单重新结算
     * 2.判断工单是否有物料,如果有则退回
     * 3.工单状态流转
     * @param orderInfo
     * @param userInfo
     * @return
     */
    Result invalid(OrderInfo orderInfo, UserInfo userInfo);

    /**
     * 委托单无效工单
     *
     * @param orderId
     * @return
     */
    boolean proxyInvalid(Long orderId);
}
