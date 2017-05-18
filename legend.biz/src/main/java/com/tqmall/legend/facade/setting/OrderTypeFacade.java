package com.tqmall.legend.facade.setting;

import com.tqmall.legend.entity.order.OrderType;

import java.util.List;

/**
 * Created by zsy on 16/11/7.
 */
public interface OrderTypeFacade {
    /**
     * 根据shopId查询门店已有的业务类型（含5个公共的）
     *
     * @param shopId
     * @return
     */
    List<OrderType> getOrderTypeByShopId(Long shopId);

    /**
     * id存在则，更新状态，否则插入数据
     *
     * @param orderType
     */
    void updateOrderType(OrderType orderType, Long userId);

    /**
     * 初始化门店业务类型
     *
     * @param shopId
     */
    void initOrderTypeByShopId(Long shopId);

    /**
     * 获取业务类型
     *
     * @param shopId
     * @param name
     * @return
     */
    OrderType getOrderTypeNoCacheByShopIdAndName(Long shopId, String name);
}
