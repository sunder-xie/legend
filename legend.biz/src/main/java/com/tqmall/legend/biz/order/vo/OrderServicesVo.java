package com.tqmall.legend.biz.order.vo;

import com.tqmall.legend.entity.order.OrderGoods;
import com.tqmall.legend.entity.order.OrderServices;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by xin on 16/4/7.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class OrderServicesVo implements Serializable {

    private static final long serialVersionUID = -5659110321911605884L;

    private Long id;
    private Long shopId;
    private Long orderId;
    private Long serviceId;
    private BigDecimal soldPrice;
    private BigDecimal serviceHour;
    private String orderSn;
    private BigDecimal servicePrice;
    private BigDecimal serviceAmount;
    private BigDecimal discount;
    private Integer type;
    private BigDecimal soldAmount;
    private String serviceName;
    private String serviceSn;
    private Long serviceCatId;
    private String serviceCatName;
    private String flags;
    private String serviceNote;
    // 服务父ID，用于子服务指向服务套餐
    private Long parentServiceId;

    private String flagsLike;

    //管理费比率
    private BigDecimal manageRate;

    private List<OrderGoods> orderGoodsList;

    // 多个维修工id（用逗号隔开）
    private String workerIds;
    // 多个维修工名称（用逗号隔开）
    private String workerNames;

    /**
     * 转换工单服务VO
     * @param orderServices
     * @return
     */
    public static OrderServicesVo getVo(OrderServices orderServices) {
        OrderServicesVo orderServicesVo = new OrderServicesVo();
        orderServicesVo.setId(orderServices.getId());
        orderServicesVo.setShopId(orderServices.getShopId());
        orderServicesVo.setOrderId(orderServices.getOrderId());
        orderServicesVo.setServiceId(orderServices.getServiceId());
        orderServicesVo.setSoldPrice(orderServices.getSoldPrice());
        orderServicesVo.setServiceHour(orderServices.getServiceHour());
        orderServicesVo.setOrderSn(orderServices.getOrderSn());
        orderServicesVo.setServicePrice(orderServices.getServicePrice());
        orderServicesVo.setServiceAmount(orderServices.getServiceAmount());
        orderServicesVo.setDiscount(orderServices.getDiscount());
        orderServicesVo.setType(orderServices.getType());
        orderServicesVo.setSoldAmount(orderServices.getSoldAmount());
        orderServicesVo.setServiceName(orderServices.getServiceName());
        orderServicesVo.setServiceSn(orderServices.getServiceSn());
        orderServicesVo.setServiceCatId(orderServices.getServiceCatId());
        orderServicesVo.setServiceCatName(orderServices.getServiceCatName());
        orderServicesVo.setFlags(orderServices.getFlags());
        orderServicesVo.setServiceNote(orderServices.getServiceNote());
        orderServicesVo.setParentServiceId(orderServices.getParentServiceId());
        orderServicesVo.setFlagsLike(orderServices.getFlagsLike());
        orderServicesVo.setManageRate(orderServices.getManageRate());
        orderServicesVo.setOrderGoodsList(orderServices.getOrderGoodsList());
        orderServicesVo.setWorkerIds(orderServices.getWorkerIds());
        return orderServicesVo;
    }
}
