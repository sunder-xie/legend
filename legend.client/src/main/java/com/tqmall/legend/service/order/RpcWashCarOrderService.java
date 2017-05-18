package com.tqmall.legend.service.order;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.object.param.activity.PageParam;
import com.tqmall.legend.object.param.order.CustomerCompletionFormEntityParam;
import com.tqmall.legend.object.result.base.PageEntityDTO;
import com.tqmall.legend.object.result.order.OrderInfoDTO;
import com.tqmall.legend.object.result.order.WashCarOrderStatsDTO;

/**
 * Created by xiangdong.qu on 17/2/10 10:59.
 */
public interface RpcWashCarOrderService {

    /**
     * 获取店铺洗车单当天统计
     *
     * @param shopId
     *
     * @return
     */
    public Result<WashCarOrderStatsDTO> getShopWashCarTodayStats(Long shopId);

    /**
     * 获取店铺当天洗车单列表
     *
     * @param shopId
     *
     * @return
     */
    public Result<PageEntityDTO<OrderInfoDTO>> getShopWashCarOrderTodayPage(Long shopId, PageParam pageParam);

    /**
     * 洗车单完善客户资料
     *
     * @param customerCompletionFormEntityParam
     *
     * @return
     */
    public Result perfectCustomerInfo(CustomerCompletionFormEntityParam customerCompletionFormEntityParam);
}
