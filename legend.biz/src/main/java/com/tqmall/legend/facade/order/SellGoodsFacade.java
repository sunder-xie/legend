package com.tqmall.legend.facade.order;

import com.tqmall.common.UserInfo;
import com.tqmall.legend.entity.order.OrderFormEntityBo;

/**
 * 物料销售单 场景业务Service
 */
public interface SellGoodsFacade {


    /**
     * 包装表单实体
     *
     * @param customerCarId 客户车辆ID
     * @param orderId       工单ID
     * @param userInfo      当前操作用户
     * @return
     */
    OrderFormEntityBo wrapperFormBody(Long customerCarId, Long orderId, UserInfo userInfo);



    /**
     * 复制销售单
     *
     * @param copyOrderId 被copy的工单
     * @param userInfo    当前操作用户
     * @return
     */
    Object wrapperFormBody(Long copyOrderId, UserInfo userInfo);
}
