package com.tqmall.legend.object.result.service;

import java.io.Serializable;

/**
 * Created by majian on 16/10/17.
 */
public class ServiceShopStatisVO implements Serializable{
    private Long serviceId;//服务id
    private String serviceName; //服务名
    private Integer appointUsages; // 预约总数
    private Integer orderUsages; //转工单数
    private Integer confirmedOrderUsage;//工单结算数

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Integer getAppointUsages() {
        return appointUsages;
    }

    public void setAppointUsages(Integer appointUsages) {
        this.appointUsages = appointUsages;
    }

    public Integer getOrderUsages() {
        return orderUsages;
    }

    public void setOrderUsages(Integer orderUsages) {
        this.orderUsages = orderUsages;
    }

    public Integer getConfirmedOrderUsage() {
        return confirmedOrderUsage;
    }

    public void setConfirmedOrderUsage(Integer confirmedOrderUsage) {
        this.confirmedOrderUsage = confirmedOrderUsage;
    }
}
