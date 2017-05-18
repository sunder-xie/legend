package com.tqmall.legend.web.vo.report.order.requestpara;

/**
 * Created by yuchengdu on 16/7/10.
 * 配件销售明细 请求参数封装
 */
public class OrderGoodsReportRequest {

    private String orderCreateStartDate;//订单创建查询的开始时间
    private String orderCreateEndDate;//订单创建查询的结束时间
    private String orderSettleStartDate;//订单结算的开始时间
    private String orderSettleEndDate;//订单结算的结束时间
    private String orderGoodsName;//订单配件名称
    private String orderGoodsNumber;//零件号
    private String carType;//车型
    private String orderNumber;//工单号
    private String carLicense;//车牌
    private Long orderGoodSaleId;//订单销售员
    private Long orderReceiverId;//订单服务顾问
    private Long orderWorkerId;//订单维修工
    private Long orderGoodsReceiverId;//无聊配件的领料人
    private Long firstCatId;//配件类型一级分类ID
    private Long secondCatId;//配件类型二级分类ID
    private Long thirdCatId;//配件类型三级分类ID
    private Long orderGoodsBrandId;//配件品牌
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

    public String getOrderGoodsName() {
        return orderGoodsName;
    }

    public void setOrderGoodsName(String orderGoodsName) {
        this.orderGoodsName = orderGoodsName;
    }

    public String getOrderGoodsNumber() {
        return orderGoodsNumber;
    }

    public void setOrderGoodsNumber(String orderGoodsNumber) {
        this.orderGoodsNumber = orderGoodsNumber;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
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

    public Long getOrderGoodSaleId() {
        return orderGoodSaleId;
    }

    public void setOrderGoodSaleId(Long orderGoodSaleId) {
        this.orderGoodSaleId = orderGoodSaleId;
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

    public Long getOrderGoodsReceiverId() {
        return orderGoodsReceiverId;
    }

    public void setOrderGoodsReceiverId(Long orderGoodsReceiverId) {
        this.orderGoodsReceiverId = orderGoodsReceiverId;
    }

    public Long getFirstCatId() {
        return firstCatId;
    }

    public void setFirstCatId(Long firstCatId) {
        this.firstCatId = firstCatId;
    }

    public Long getSecondCatId() {
        return secondCatId;
    }

    public void setSecondCatId(Long secondCatId) {
        this.secondCatId = secondCatId;
    }

    public Long getThirdCatId() {
        return thirdCatId;
    }

    public void setThirdCatId(Long thirdCatId) {
        this.thirdCatId = thirdCatId;
    }

    public Long getOrderGoodsBrandId() {
        return orderGoodsBrandId;
    }

    public void setOrderGoodsBrandId(Long orderGoodsBrandId) {
        this.orderGoodsBrandId = orderGoodsBrandId;
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
