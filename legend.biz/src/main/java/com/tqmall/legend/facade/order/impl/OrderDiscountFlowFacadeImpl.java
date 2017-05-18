package com.tqmall.legend.facade.order.impl;

import com.google.common.collect.Maps;
import com.tqmall.legend.biz.order.OrderDiscountFlowService;
import com.tqmall.legend.biz.order.vo.OrderDiscountFlowVo;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.settlement.vo.DebitBillFlowVo;
import com.tqmall.legend.entity.order.OrderDiscountFlow;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.facade.order.OrderDiscountFlowFacade;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Created by zsy on 16/6/12.
 */
@Service
public class OrderDiscountFlowFacadeImpl implements OrderDiscountFlowFacade {
    @Autowired
    private OrderDiscountFlowService orderDiscountFlowService;
    @Autowired
    private ShopManagerService shopManagerService;

    /**
     * 根据orderId、shopId获取工单流水
     *
     * @param orderId
     * @return
     */
    @Override
    public List<OrderDiscountFlowVo> getOrderDiscountFlow(Long orderId, Long shopId) {
        List<OrderDiscountFlowVo> flowVoList = null;
        if (orderId != null && shopId != null) {
            Map<String, Object> searchMap = Maps.newHashMap();
            searchMap.put("orderId", orderId);
            searchMap.put("shopId", shopId);
            List<OrderDiscountFlow> flowList = orderDiscountFlowService.select(searchMap);
            Set<Long> managerIdSet = new HashSet<>();
            if (!CollectionUtils.isEmpty(flowList)) {
                flowVoList = new ArrayList<>(flowList.size());
                for(OrderDiscountFlow flow : flowList) {
                    OrderDiscountFlowVo flowVo = new OrderDiscountFlowVo();
                    BeanUtils.copyProperties(flow, flowVo);
                    flowVoList.add(flowVo);

                    managerIdSet.add(flowVo.getCreator());
                }
            }

            int size = managerIdSet.size();
            if (size > 0) {
                // 查询门店员工
                List<ShopManager> shopManagerList = shopManagerService.selectByIdsWithDeleted(managerIdSet.toArray(new Long[size]));
                Map<Long, String> shopManagerMap = new HashMap<>();
                if (!CollectionUtils.isEmpty(shopManagerList)) {
                    for (ShopManager shopManager : shopManagerList) {
                        shopManagerMap.put(shopManager.getId(), shopManager.getName());
                    }
                }
                for (OrderDiscountFlowVo flowVo : flowVoList) {
                    // 收银人
                    String name = shopManagerMap.get(flowVo.getCreator());
                    flowVo.setOperatorName(name);
                }
            }
        }
        return flowVoList;
    }
}
