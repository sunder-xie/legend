package com.tqmall.legend.biz.api;

import com.tqmall.legend.api.entity.OrderCountSearchVO;
import com.tqmall.legend.object.param.order.OrderSearchParam;
import com.tqmall.zenith.errorcode.support.Result;

import java.util.List;
import java.util.Map;

/**
 * 工单对外API接口
 */
public interface IOrderApiService {


    /**
     * 从搜索或本地获取工单列表
     *
     * @param orderSearchParam
     * @return
     */
    Result getOrderInfoListFromSearchOrLocal(OrderSearchParam orderSearchParam);


    /**
     * 从搜索获取对应的工单数
     *
     * @param searchParam
     * @return
     */
    Result<Map<String, Long>> getOrderCount(List<OrderCountSearchVO> searchParam);

    /**
     * 获取基本工单信息
     *
     * @param orderId 工单ID
     * @param shopId  门店ID
     * @return
     */
    Result getOrder(Long orderId, Long shopId);


    /**
     * 工单无效
     *
     * @param orderId 工单ID
     * @param userId  当前操作人ID
     * @return
     */
    Result invalidOrder(Long orderId, Long userId);

}
