package com.tqmall.legend.web.vo.report.order;

import com.tqmall.common.DateJsonSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yuchengdu on 16/7/11.
 */
public class OrderServicesVO {
    /**
     * 店铺ID
     */
    private Long shopId;
    /**
     * 订单创建时间
     */
    @JsonSerialize(using = DateJsonSerializer.class)
    private Date orderCreateDate;
    /**
     * 订单结算时间
     */
    @JsonSerialize(using = DateJsonSerializer.class)
    private Date orderConfirmTime;
    /**
     * 订单SN
     */
    private String orderSn;
    /**
     * 车牌号
     */
    private String carLicense;
    /**
     * 车主姓名
     */
    private String customerName;
    /**
     * 服务名称
     */
    private String serviceName;
    /**
     * 服务类型ID
     */
    private Long  serviceCatId;
    /**
     * 服务类型名称
     */
    private String serviceCatName;
    /**
     * 服务价格
     */
    private BigDecimal servicePrice;
    /**
     * 服务工时
     */
    private BigDecimal serviceHour;
    /**
     * 工时费
     */
    private BigDecimal serviceAmount;
    /**
     * 折扣
     */
    private BigDecimal discount;
    /**
     * 维修工ID
     */
    private Long workerId;
    /**
     * 维修工名称
     */
    private String workerName;
    /**
     * 服务顾问名称
     */
    private String receiverName;
    /**
     * 服务顾问ID
     */
    private Long receiverId;
    /**
     * 车别名
     */
    private String carAlias;
    /**
     * 车品牌
     */
    private String carBrand;
    /**
     * 车款式
     */
    private String carModels;
    /**
     * 车排量
     */
    private String carPower;
    /**
     * 订单状态
     */
    private String orderStatus;

    public String getCarYear() {
        return carYear;
    }

    public void setCarYear(String carYear) {
        this.carYear = carYear;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Date getOrderCreateDate() {
        return orderCreateDate;
    }

    public void setOrderCreateDate(Date orderCreateDate) {
        this.orderCreateDate = orderCreateDate;
    }

    public Date getOrderConfirmTime() {
        return orderConfirmTime;
    }

    public void setOrderConfirmTime(Date orderConfirmTime) {
        this.orderConfirmTime = orderConfirmTime;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public String getCarLicense() {
        return carLicense;
    }

    public void setCarLicense(String carLicense) {
        this.carLicense = carLicense;
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

    public Long getServiceCatId() {
        return serviceCatId;
    }

    public void setServiceCatId(Long serviceCatId) {
        this.serviceCatId = serviceCatId;
    }

    public String getServiceCatName() {
        return serviceCatName;
    }

    public void setServiceCatName(String serviceCatName) {
        this.serviceCatName = serviceCatName;
    }

    public BigDecimal getServicePrice() {
        if (servicePrice!=null){
            servicePrice=servicePrice.setScale(2,BigDecimal.ROUND_HALF_UP);
        }
        return servicePrice;
    }

    public void setServicePrice(BigDecimal servicePrice) {
        this.servicePrice = servicePrice;
    }

    public BigDecimal getServiceHour() {
        return serviceHour;
    }

    public void setServiceHour(BigDecimal serviceHour) {
        this.serviceHour = serviceHour;
    }

    public BigDecimal getServiceAmount() {
        if (serviceAmount!=null){
            serviceAmount=serviceAmount.setScale(2,BigDecimal.ROUND_HALF_UP);
        }
        return serviceAmount;
    }

    public void setServiceAmount(BigDecimal serviceAmount) {
        this.serviceAmount = serviceAmount;
    }

    public BigDecimal getDiscount() {
        if (discount!=null){
            discount=discount.setScale(2,BigDecimal.ROUND_HALF_UP);
        }
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Long workerId) {
        this.workerId = workerId;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getCarAlias() {
        return carAlias;
    }

    public void setCarAlias(String carAlias) {
        this.carAlias = carAlias;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarModels() {
        return carModels;
    }

    public void setCarModels(String carModels) {
        this.carModels = carModels;
    }

    public String getCarPower() {
        return carPower;
    }

    public void setCarPower(String carPower) {
        this.carPower = carPower;
    }

    /**
     * 车年份

     */
    private String carYear;

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
