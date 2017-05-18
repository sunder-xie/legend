package com.tqmall.legend.entity.warehouseshare;
import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.entity.base.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

public class WarehouseShare extends BaseEntity {

    private Long goodsId;//关联库存表物料id
    private String goodsName;//商品名称
    private String measureUnit;//最小单位
    private String goodsOrigin;//产地
    private String goodsFormat;//型号
    private String catName;//分类名称
    private String brandName;//品牌名称
    private BigDecimal goodsPrice;//销售价格
    private BigDecimal goodsStock;//库存
    private BigDecimal saleNumber;//销售数量
    private Long shopId;//店铺id
    private Date lastSaleTime;//最后一次开始出售时间
    private Integer goodsStatus;//0：待出售，1：出售中，9：审核未通过
    private BigDecimal inventoryPrice;//结存价格
    private String imgUrl;//商品图片地址
    private String carInfo;//适用车型，0为通用件，或者goods_car对象列表json字符串
    private String partUsedTo;//适用部位
    private String goodsRemark;//审核情况
    private Date lastInTime;//最后入库时间
    private String lastInTimeStr;

    public String getLastInTimeStr() {
        if(null != lastInTime){
            return DateUtil.convertDateToYMD(lastInTime);
        }
        return "";
    }

    public void setLastInTimeStr(String lastInTimeStr) {
        this.lastInTimeStr = lastInTimeStr;
    }

    public Date getLastInTime() {
        return lastInTime;
    }

    public void setLastInTime(Date lastInTime) {
        this.lastInTime = lastInTime;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit;
    }

    public String getGoodsOrigin() {
        return goodsOrigin;
    }

    public void setGoodsOrigin(String goodsOrigin) {
        this.goodsOrigin = goodsOrigin;
    }

    public String getGoodsFormat() {
        return goodsFormat;
    }

    public void setGoodsFormat(String goodsFormat) {
        this.goodsFormat = goodsFormat;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public BigDecimal getGoodsPrice() {
        if(null != goodsPrice){
            return goodsPrice.setScale(2,BigDecimal.ROUND_HALF_UP);
        }
        return goodsPrice;
    }

    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public BigDecimal getGoodsStock() {
        if(null != goodsStock){
            return goodsStock.setScale(2,BigDecimal.ROUND_HALF_UP);
        }
        return goodsStock;
    }

    public void setGoodsStock(BigDecimal goodsStock) {
        this.goodsStock = goodsStock;
    }

    public BigDecimal getSaleNumber() {
        if(null != saleNumber){
            return saleNumber.setScale(2,BigDecimal.ROUND_HALF_UP);
        }
        return saleNumber;
    }

    public void setSaleNumber(BigDecimal saleNumber) {
        this.saleNumber = saleNumber;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Date getLastSaleTime() {
        return lastSaleTime;
    }

    public void setLastSaleTime(Date lastSaleTime) {
        this.lastSaleTime = lastSaleTime;
    }

    public Integer getGoodsStatus() {
        return goodsStatus;
    }

    public void setGoodsStatus(Integer goodsStatus) {
        this.goodsStatus = goodsStatus;
    }

    public BigDecimal getInventoryPrice() {
        if(null != inventoryPrice){
            return inventoryPrice.setScale(2,BigDecimal.ROUND_HALF_UP);
        }
        return inventoryPrice;
    }

    public void setInventoryPrice(BigDecimal inventoryPrice) {
        this.inventoryPrice = inventoryPrice;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getCarInfo() {
        return carInfo;
    }

    public void setCarInfo(String carInfo) {
        this.carInfo = carInfo;
    }

    public String getPartUsedTo() {
        return partUsedTo;
    }

    public void setPartUsedTo(String partUsedTo) {
        this.partUsedTo = partUsedTo;
    }

    public String getGoodsRemark() {
        return goodsRemark;
    }

    public void setGoodsRemark(String goodsRemark) {
        this.goodsRemark = goodsRemark;
    }
}

