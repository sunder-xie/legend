package com.tqmall.legend.web.vo.report.order.requestpara;

/**
 * Created by yuchengdu on 16/7/10.
 * 工单服务销售明细报表
 */
public class OrderServicesReportRequest {
    private String orderCreateStartDate;//订单创建查询的开始时间
    private String orderCreateEndDate;//订单创建查询的结束时间
    private String orderSettleStartDate;//订单结算的开始时间
    private String orderSettleEndDate;//订单结算的结束时间
    private Long orderReceiverId;//订单服务顾问
    private Long orderWorkerId;//订单维修工
    private String carType;//车型
    private Long serviceCateId;//服务类型ID
    private String customerName;//车主名称
    private String serviceName;//服务名称
    private String orderNumber;//工单号
    private String carLicense;//车牌
    private String orderStatus;
    private Integer page;
    private Integer pageSize;

    public String getOrderCreateStartDate() {
        return orderCreateStartDate;
    }

    public void setOrderCreateStartDate(String orderCreateStartDate) {
        this.orderCreateStartDate = orderCreateStartDate;
    }

    public String getOrderCreateEndDate() {
        return orderCreateEndDate;
    }

    public void setOrderCreateEndDate(String orderCreateEndDate) {
        this.orderCreateEndDate = orderCreateEndDate;
    }

    public String getOrderSettleStartDate() {
        return orderSettleStartDate;
    }

    public void setOrderSettleStartDate(String orderSettleStartDate) {
        this.orderSettleStartDate = orderSettleStartDate;
    }

    public String getOrderSettleEndDate() {
        return orderSettleEndDate;
    }

    public void setOrderSettleEndDate(String orderSettleEndDate) {
        this.orderSettleEndDate = orderSettleEndDate;
    }

    public Long getOrderReceiverId() {
        return orderReceiverId;
    }

    public void setOrderReceiverId(Long orderReceiverId) {
        this.orderReceiverId = orderReceiverId;
    }

    public Long getOrderWorkerId() {
        return orderWorkerId;
    }

    public void setOrderWorkerId(Long orderWorkerId) {
        this.orderWorkerId = orderWorkerId;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public Long getServiceCateId() {
        return serviceCateId;
    }

    public void setServiceCateId(Long serviceCateId) {
        this.serviceCateId = serviceCateId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCarLicense() {
        return carLicense;
    }

    public void setCarLicense(String carLicense) {
        this.carLicense = carLicense;
    }


    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
