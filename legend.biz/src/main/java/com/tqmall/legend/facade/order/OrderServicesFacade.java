package com.tqmall.legend.facade.order;

import com.tqmall.legend.api.entity.OrderCountSearchVO;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.entity.base.CountOrderEntity;
import com.tqmall.legend.facade.order.vo.OrderInfoVo;
import com.tqmall.legend.entity.order.OrderFormEntityBo;
import com.tqmall.legend.facade.order.vo.OrderWorkerVo;
import com.tqmall.search.common.data.PageableRequest;
import com.tqmall.search.dubbo.client.legend.order.param.LegendOrderRequest;

import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 16/4/14.
 */
public interface OrderServicesFacade {
    /**
     * 车间看板接口
     *
     * @param shopId
     * @param size
     * @return
     */
    public List<OrderWorkerVo> getOrderWorkerList(Long shopId, Integer size);



    /**
     * 从搜索获取工单数
     * @param countOrderEntity
     * @return
     */
    Map<String, Long> getOrderCountFromSearch(CountOrderEntity countOrderEntity);


    Map<String, Long> getOrderCountFromSearchToApp(List<OrderCountSearchVO> countOrderEntity);

    /**
     * 从搜索获取工单列表
     * @param pageableRequest
     * @param orderRequest
     * @return
     */
    DefaultPage<OrderInfoVo> getOrderListFromSearch(PageableRequest pageableRequest,LegendOrderRequest orderRequest);

    DefaultPage<OrderInfoVo> getOrderInfoPage(PageableRequest pageableRequest,LegendOrderRequest orderRequest);

    /**
     * app新建快修快保单
     * @param orderFormEntityBo
     * @return 工单id
     */
    Long createSpeedilyOrderForApp(OrderFormEntityBo orderFormEntityBo);

    /**
     * app更新快修快保单
     * @param orderFormEntityBo
     * @return
     */
    Long updateSpeedilyOrderForApp(OrderFormEntityBo orderFormEntityBo);
}
