package com.tqmall.legend.facade.order.impl;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.tqmall.legend.biz.order.IOrderService;
import com.tqmall.legend.biz.order.OrderPrecheckDetailsService;
import com.tqmall.legend.biz.precheck.PrechecksService;
import com.tqmall.legend.entity.order.OrderInfo;
import com.tqmall.legend.entity.order.OrderPrecheckDetails;
import com.tqmall.legend.entity.precheck.PrecheckItemTypeEnum;
import com.tqmall.legend.entity.precheck.PrecheckValue;
import com.tqmall.legend.facade.order.OrderPrecheckDetailsFacade;
import com.tqmall.legend.facade.order.vo.OrderPrecheckDetailsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 16/7/10.
 */
@Service
public class OrderPrecheckDetailsFacadeImpl implements OrderPrecheckDetailsFacade {

    @Autowired
    private PrechecksService prechecksService;
    @Autowired
    private OrderPrecheckDetailsService orderPrecheckDetailsService;
    @Autowired
    private IOrderService orderService;
    /**
     * 根据orderId获取工单预检详情
     *
     * @param orderId
     * @return
     */
    @Override
    public OrderPrecheckDetailsVo getOrderPrecheckDetailsByOrderId(Long orderId) {
        OrderPrecheckDetailsVo orderPrecheckDetailsVo = new OrderPrecheckDetailsVo();
        //设置工单外观检查内容
        List<PrecheckValue> precheckValueList = prechecksService.getItemValuesByType(PrecheckItemTypeEnum.OUTLINECODE.getIndex());
        orderPrecheckDetailsVo.setPrecheckValueList(precheckValueList);
        //获取工单预检详情
        Optional<OrderInfo>  orderInfoOptional = orderService.getOrder(orderId);
        if(orderInfoOptional.isPresent()){
            Map<String,Object> searchMap = Maps.newHashMap();
            searchMap.put("orderId", orderId);
            List<OrderPrecheckDetails> orderPrecheckDetailsList = orderPrecheckDetailsService.select(searchMap);
            for (OrderPrecheckDetails orderPrecheckDetails : orderPrecheckDetailsList) {
                orderPrecheckDetails.setFtlId(com.tqmall.legend.common.Constants.ftlItemMapping.get(orderPrecheckDetails.getPrecheckItemId()));
            }
            orderPrecheckDetailsVo.setOrderPrecheckDetailsList(orderPrecheckDetailsList);
            return orderPrecheckDetailsVo;
        }
        return orderPrecheckDetailsVo;
    }

    @Override
    public boolean setOrderPrecheckDetailsModelByOrderId(Model model, Long orderId) {
        if(model != null && orderId != null){
            OrderPrecheckDetailsVo orderPrecheckDetailsVo = getOrderPrecheckDetailsByOrderId(orderId);
            model.addAttribute("outlineValues",orderPrecheckDetailsVo.getPrecheckValueList());
            model.addAttribute("precheckDetailsList",orderPrecheckDetailsVo.getOrderPrecheckDetailsList());
            return true;
        }
        return false;
    }

}
