package com.tqmall.legend.biz.order;

import com.google.common.base.Optional;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.order.OrderType;

import java.util.List;
import java.util.Map;

/**
 * Created by lixiao on 15-1-20.
 */
public interface OrderTypeService {

    public List<OrderType> select(Map map);

    public List<OrderType> selectNoCache(Map map);

    public OrderType selectById(Long id);


    /**
     * 批量插入工单类型
     *
     * @param orderTypeList
     * @return
     */
    public Result batchInsert(List<OrderType> orderTypeList);

    /**
     * get orderType by primary key(id)
     *
     * @param id 工单类型主键ID
     * @return Optional<OrderType>
     */
    Optional<OrderType> getOrderType(Long id);

    /**
     * 根据ids更新业务类型状态
     *
     * @param showStatus
     * @param ids
     * @param userId 修改人
     * @return
     */
    int updateShowStatusByIds(Integer showStatus, List<Long> ids, Long userId);

    /**
     * 获取工单类型
     *
     * @return
     */
    OrderType selectByIdAndShopId(Long shopId, Long id);

    /**
     * 获取门店所有工单类型
     *
     * @return
     */
    List<OrderType> selectByShopId(Long shopId);
}
