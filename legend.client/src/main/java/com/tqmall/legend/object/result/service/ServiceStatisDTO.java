package com.tqmall.legend.object.result.service;

import java.io.Serializable;

/**
 * Created by majian on 16/10/17.
 */
public class ServiceStatisDTO implements Serializable{
    private Long topCatId; //类id
    private String catName;//类名
    private Integer publisherCount;//发布门店数
    private Integer publishCount;//发布总数
    private Integer appointUsages;//预约数
    private Integer orderUsages;//转工单数
    private Integer confirmedOrderUsage;//工单结算数

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public Long getTopCatId() {
        return topCatId;
    }

    public void setTopCatId(Long topCatId) {
        this.topCatId = topCatId;
    }

    public Integer getPublisherCount() {
        return publisherCount;
    }

    public void setPublisherCount(Integer publisherCount) {
        this.publisherCount = publisherCount;
    }

    public Integer getPublishCount() {
        return publishCount;
    }

    public void setPublishCount(Integer publishCount) {
        this.publishCount = publishCount;
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

    public void addAppointUsages(Integer usageInPoints) {
        this.appointUsages += usageInPoints;
    }

    public void addPublishCount(int i) {
        this.publishCount += i;
    }

    public void addOrderUsages(Integer usageInOrders) {
        this.orderUsages += usageInOrders;
    }

    public void addConfirmedOrderUsages(Integer usageInConfirmOrders) {
        this.confirmedOrderUsage += usageInConfirmOrders;
    }
}
