package com.tqmall.legend.web.vo.report.order;

import com.tqmall.common.DateJsonSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yuchengdu on 16/7/11.
 */
public class OrderGoodsVO {
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
     * 配件名称
     */
    private String goodsName;
    /**
     * 零件号
     */
    private String goodsFormat;
    /**
     * 售卖单位
     */
    private String measureUnit;
    /**
     * 成本价
     */
    private BigDecimal inventoryPrice;
    private BigDecimal inventoryAmountTotal;
    /**
     * 销售数量
     */
    private BigDecimal goodsNumber;
    /**
     * 销售价格
     */
    private BigDecimal salePrice;
    /**
     * 销售总金额
     */
    private BigDecimal saleTotalAmount;
    /**
     * 毛利
     */
    private BigDecimal grossProfit;
    /**
     * 领料人ID
     */
    private Long goodsReceiverId;
    /**
     * 物料领料人姓名
     */
    private String goodsReceiverName;
    /**
     * 销售人员名称
     */
    private String saleName;
    /**
     * 销售人员ID
     */
    private Long saleId;
    /**
     * 服务顾问
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
     * 车年份
     */
    private String carYear;
    /**
     * 配件类型名称
     */
    private String catName;
    /**
     * 配件类型ID
     */
    private Long catId;
    /**
     * 品牌ID
     */
    private Long brandId;
    /**
     * 品牌名称
     */
    private String brandName;
    /**
     * 订单状态
     */
    private String orderStatus;

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

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsFormat() {
        return goodsFormat;
    }

    public void setGoodsFormat(String goodsFormat) {
        this.goodsFormat = goodsFormat;
    }

    public String getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit;
    }

    public BigDecimal getInventoryPrice() {
        if (inventoryPrice!=null){
            inventoryPrice=inventoryPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        return inventoryPrice;
    }

    public void setInventoryPrice(BigDecimal inventoryPrice) {
        this.inventoryPrice = inventoryPrice;
    }

    public BigDecimal getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(BigDecimal goodsNumber) {
        this.goodsNumber = goodsNumber;
    }

    public BigDecimal getSalePrice() {
        if (salePrice!=null){
            salePrice=salePrice.setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public Long getGoodsReceiverId() {
        return goodsReceiverId;
    }

    public void setGoodsReceiverId(Long goodsReceiverId) {
        this.goodsReceiverId = goodsReceiverId;
    }

    public String getSaleName() {
        return saleName;
    }

    public void setSaleName(String saleName) {
        this.saleName = saleName;
    }

    public Long getSaleId() {
        return saleId;
    }

    public void setSaleId(Long saleId) {
        this.saleId = saleId;
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

    public String getCarYear() {
        return carYear;
    }

    public void setCarYear(String carYear) {
        this.carYear = carYear;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public Long getCatId() {
        return catId;
    }

    public void setCatId(Long catId) {
        this.catId = catId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public BigDecimal getInventoryAmountTotal() {//成本总金额*销售数量
        if (this.inventoryPrice!=null &&this.goodsNumber !=null) {
            BigDecimal costdecimal=this.inventoryPrice.multiply(this.getGoodsNumber());
            if (costdecimal!=null){
                costdecimal=costdecimal.setScale(2,BigDecimal.ROUND_HALF_UP);
            }
          return costdecimal;
        }
        return new BigDecimal(0);
    }

    public void setInventoryAmountTotal(BigDecimal inventoryAmountTotal) {
        if (this.inventoryPrice!=null &&this.goodsNumber !=null) {
            this.inventoryAmountTotal = this.inventoryPrice.multiply(this.getGoodsNumber());
        }else {
            this.inventoryAmountTotal = inventoryAmountTotal;
        }
    }

    public BigDecimal getSaleTotalAmount() {//销售单价 * 销售数量
        if (this.salePrice!=null && this.goodsNumber != null){
            BigDecimal costSalePrice=this.salePrice.multiply(this.getGoodsNumber());
            if (costSalePrice!=null){
                costSalePrice=costSalePrice.setScale(2,BigDecimal.ROUND_HALF_UP);
            }
            return costSalePrice;
        }
        return new BigDecimal(0);
    }

    public void setSaleTotalAmount(BigDecimal saleTotalAmount) {
        if (this.salePrice!=null && this.goodsNumber != null){
            this.saleTotalAmount = this.salePrice.multiply(this.getGoodsNumber());
        }else {
            this.saleTotalAmount = saleTotalAmount;
        }
    }

    public BigDecimal getGrossProfit() {
        if (this.salePrice!=null && this.goodsNumber != null) {
            if (this.inventoryPrice==null){
                BigDecimal costGrossProfit=this.salePrice.multiply(this.getGoodsNumber());
                if (costGrossProfit!=null){
                    costGrossProfit=costGrossProfit.setScale(2,BigDecimal.ROUND_HALF_UP);
                }
                return costGrossProfit;
            }
            BigDecimal costGrossProfit=this.salePrice.multiply(this.getGoodsNumber())
                    .subtract(
                            this.inventoryPrice.multiply
                                    (this.getGoodsNumber()));
            if (costGrossProfit!=null){
                costGrossProfit=costGrossProfit.setScale(2,BigDecimal.ROUND_HALF_UP);
            }
            return costGrossProfit;
        }
        BigDecimal costGrossProfit=new BigDecimal(0).subtract(this.inventoryAmountTotal==null?new BigDecimal(0):this.inventoryAmountTotal);
        if (costGrossProfit!=null){
            costGrossProfit=costGrossProfit.setScale(2,BigDecimal.ROUND_HALF_UP);
        }
        return costGrossProfit;
    }

    public void setGrossProfit(BigDecimal grossProfit) {
        this.grossProfit = grossProfit;
    }

    public String getGoodsReceiverName() {
        return goodsReceiverName;
    }

    public void setGoodsReceiverName(String goodsReceiverName) {
        this.goodsReceiverName = goodsReceiverName;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
