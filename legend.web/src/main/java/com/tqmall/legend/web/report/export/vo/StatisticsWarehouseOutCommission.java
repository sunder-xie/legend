package com.tqmall.legend.web.report.export.vo;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.entity.base.BaseEntity;
import com.tqmall.wheel.component.excel.annotation.ExcelCol;

import java.math.BigDecimal;

/**
 * Created by tanghao on 17/2/8.
 */
@com.tqmall.wheel.component.excel.annotation.Excel
public class StatisticsWarehouseOutCommission extends BaseEntity {
    @ExcelCol(value = 2, title = "出库日期")
    String gmtCreateStr;
    Integer totalSize;                      //总记录
    BigDecimal totalCount;                     //总数量
    BigDecimal totalSalePriceAmount;        //总成本金额
    BigDecimal totalInventoryPriceAmount;   //总出库金额
    @ExcelCol(value = 0, title = "单据号")
    private String warehouseOutSn;          //单据号（warehouse_out）
    @ExcelCol(value = 1, title = "单据类型")
    private String status;                 //单据类型（warehouse_out）
    @ExcelCol(value = 17, title = "维修接待")
    private String receiverName;            //维修接待（order_info）
    @ExcelCol(value = 3, title = "车牌")
    private String carLicense;              //车辆牌照号（warehouse_out）
    @ExcelCol(value = 4, title = "车主")
    private String customerName;            //客户名（warehouse_out）
    @ExcelCol(value = 5, title = "零件号")
    private String goodsFormat;             //零件号（warehouse_out_detail）
    @ExcelCol(value = 6, title = "配件名称")
    private String goodsName;               //零件名（warehouse_out_detail）
    @ExcelCol(value = 13, title = "类别")
    private String catName;                 //类别（goods_category）
    @ExcelCol(value = 14, title = "仓位")
    private String depot;                   //仓位（goods）
    @ExcelCol(value = 12, title = "单位")
    private String measureUnit;             //单位（goods）
    @ExcelCol(value = 11, title = "出库数量")
    private BigDecimal goodsCount;             //实际出库数量（warehouse_out_detail）
    private BigDecimal goodsRealCount;            //可退数量（warehouse_out_detail）
    private BigDecimal goodsReturnCount;          //退库数量（出库数量-可退数量）
    @ExcelCol(value = 7, title = "出库价")
    private BigDecimal salePrice;           //出库价（warehouse_out_detail）
    @ExcelCol(value = 8, title = "出库金额")
    private BigDecimal salePriceAmount;     //出库金额（出库数量*出库价）
    private BigDecimal returnAmount;        //退库金额（退库数量*出库价）
    @ExcelCol(value = 9, title = "成本价")
    private BigDecimal inventoryPrice;      //成本价（warehouse_out_detail）
    @ExcelCol(value = 10, title = "成本金额")
    private BigDecimal inventoryPriceAmount;//成本金额（可退数量*成本价）
    @ExcelCol(value = 18, title = "领料人")
    private String goodsReceiverName;       //领料人（shop_manager）
    @ExcelCol(value = 19, title = "开单人")
    private String operatorName;            //开单人（order_info）
    private Long shopId;                    //门店id（warehouse_out_detail）
    private Long goodsId;                   //物料id（warehouse_out_detail）
    private Long orderId;                   //工单id（warehouse_out_detail）
    private Long warehouseOutId;            //出库单id（warehouse_out_detail）
    private String orderTypeName;           //工单业务类型名
    @ExcelCol(value = 15, title = "工单号")
    private String orderSn;//工单编码

    //商品销售统计
    private String carInfo;                 //适配车型（warehouse_out_detail）
    private Long catId;                     //类别ID（goods_category）
    private BigDecimal relPayAmount;        //实付金额：配件的材料费-材料优惠
    private BigDecimal profitAmount;        //利润：材料费-材料优惠-成本
    private String customerMobile;          //客户手机号

    public String getGmtCreateStr() {
        return DateUtil.convertDateToYMD(super.getGmtCreate());
    }

    public void setGmtCreateStr(String gmtCreateStr) {
        this.gmtCreateStr = gmtCreateStr;
    }

    public Integer getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Integer totalSize) {
        this.totalSize = totalSize;
    }

    public BigDecimal getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(BigDecimal totalCount) {
        this.totalCount = totalCount;
    }

    public BigDecimal getTotalSalePriceAmount() {
        return totalSalePriceAmount;
    }

    public void setTotalSalePriceAmount(BigDecimal totalSalePriceAmount) {
        this.totalSalePriceAmount = totalSalePriceAmount;
    }

    public BigDecimal getTotalInventoryPriceAmount() {
        return totalInventoryPriceAmount;
    }

    public void setTotalInventoryPriceAmount(BigDecimal totalInventoryPriceAmount) {
        this.totalInventoryPriceAmount = totalInventoryPriceAmount;
    }

    public String getWarehouseOutSn() {
        return warehouseOutSn;
    }

    public void setWarehouseOutSn(String warehouseOutSn) {
        this.warehouseOutSn = warehouseOutSn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
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

    public String getGoodsFormat() {
        return goodsFormat;
    }

    public void setGoodsFormat(String goodsFormat) {
        this.goodsFormat = goodsFormat;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getDepot() {
        return depot;
    }

    public void setDepot(String depot) {
        this.depot = depot;
    }

    public String getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit;
    }

    public BigDecimal getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(BigDecimal goodsCount) {
        this.goodsCount = goodsCount;
    }

    public BigDecimal getGoodsRealCount() {
        return goodsRealCount;
    }

    public void setGoodsRealCount(BigDecimal goodsRealCount) {
        this.goodsRealCount = goodsRealCount;
    }

    public BigDecimal getGoodsReturnCount() {
        return goodsReturnCount;
    }

    public void setGoodsReturnCount(BigDecimal goodsReturnCount) {
        this.goodsReturnCount = goodsReturnCount;
    }

    public BigDecimal getSalePrice() {
        if(salePrice==null){
            return BigDecimal.ZERO;
        }
        return salePrice.setScale(2,BigDecimal.ROUND_HALF_UP);
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public BigDecimal getSalePriceAmount() {
        return salePriceAmount;
    }

    public void setSalePriceAmount(BigDecimal salePriceAmount) {
        this.salePriceAmount = salePriceAmount;
    }

    public BigDecimal getReturnAmount() {
        return returnAmount;
    }

    public void setReturnAmount(BigDecimal returnAmount) {
        this.returnAmount = returnAmount;
    }

    public BigDecimal getInventoryPrice() {
        if(null == inventoryPrice){
            return BigDecimal.ZERO;
        }
        return inventoryPrice.setScale(2,BigDecimal.ROUND_HALF_UP);
    }

    public void setInventoryPrice(BigDecimal inventoryPrice) {
        this.inventoryPrice = inventoryPrice;
    }

    public BigDecimal getInventoryPriceAmount() {
        if(null == inventoryPriceAmount){
            return BigDecimal.ZERO;
        }
        return inventoryPriceAmount.setScale(2,BigDecimal.ROUND_HALF_UP);
    }

    public void setInventoryPriceAmount(BigDecimal inventoryPriceAmount) {
        this.inventoryPriceAmount = inventoryPriceAmount;
    }

    public String getGoodsReceiverName() {
        return goodsReceiverName;
    }

    public void setGoodsReceiverName(String goodsReceiverName) {
        this.goodsReceiverName = goodsReceiverName;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getWarehouseOutId() {
        return warehouseOutId;
    }

    public void setWarehouseOutId(Long warehouseOutId) {
        this.warehouseOutId = warehouseOutId;
    }

    @ExcelCol(value = 16, title = "业务类型")
    public String getOrderTypeName() {
        if(StringUtils.isEmpty(orderTypeName)){
            return "--";
        }
        return orderTypeName;
    }

    public void setOrderTypeName(String orderTypeName) {
        this.orderTypeName = orderTypeName;
    }

    public String getCarInfo() {
        return carInfo;
    }

    public void setCarInfo(String carInfo) {
        this.carInfo = carInfo;
    }

    public Long getCatId() {
        return catId;
    }

    public void setCatId(Long catId) {
        this.catId = catId;
    }

    public BigDecimal getRelPayAmount() {
        return relPayAmount;
    }

    public void setRelPayAmount(BigDecimal relPayAmount) {
        this.relPayAmount = relPayAmount;
    }

    public BigDecimal getProfitAmount() {
        return profitAmount;
    }

    public void setProfitAmount(BigDecimal profitAmount) {
        this.profitAmount = profitAmount;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }
}
