package com.tqmall.legend.biz.shop.bo;

/**
 * Created by majian on 16/10/17.
 */
public class ServiceStatisBo {
    private Long shopId;
    private Long topCatId;
    private Long catId;
    private Long serviceId;
    private String serviceName;
    private Integer usageInPoints = 0;//预约总数
    private Integer usageInOrders = 0;//转成工单数
    private Integer usageInConfirmOrders = 0;//工单结算总数

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getTopCatId() {
        return topCatId;
    }

    public void setTopCatId(Long topCatId) {
        this.topCatId = topCatId;
    }

    public Long getCatId() {
        return catId;
    }

    public void setCatId(Long catId) {
        this.catId = catId;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getUsageInPoints() {
        return usageInPoints;
    }

    public void setUsageInPoints(Integer usageInPoints) {
        this.usageInPoints = usageInPoints;
    }

    public Integer getUsageInOrders() {
        return usageInOrders;
    }

    public void setUsageInOrders(Integer usageInOrders) {
        this.usageInOrders = usageInOrders;
    }

    public Integer getUsageInConfirmOrders() {
        return usageInConfirmOrders;
    }

    public void setUsageInConfirmOrders(Integer usageInConfirmOrders) {
        this.usageInConfirmOrders = usageInConfirmOrders;
    }
}
