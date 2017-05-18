package com.tqmall.legend.facade.order;

import com.tqmall.legend.facade.order.vo.OrderPrecheckDetailsVo;
import org.springframework.ui.Model;

/**
 * Created by zsy on 16/7/10.
 */
public interface OrderPrecheckDetailsFacade {
    /**
     * 根据orderId获取工单预检详情
     *
     * @param orderId
     * @return
     */
    OrderPrecheckDetailsVo getOrderPrecheckDetailsByOrderId(Long orderId);

    /**
     * 设置预检单model
     *
     * @param model
     * @param orderId
     */
    boolean setOrderPrecheckDetailsModelByOrderId(Model model, Long orderId);
}
