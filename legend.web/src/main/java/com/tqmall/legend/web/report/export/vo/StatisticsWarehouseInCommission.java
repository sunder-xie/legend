package com.tqmall.legend.web.report.export.vo;

import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.entity.base.BaseEntity;
import com.tqmall.legend.entity.goods.GoodsCar;
import com.tqmall.wheel.component.excel.annotation.ExcelCol;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by tanghao on 17/2/8.
 */
@com.tqmall.wheel.component.excel.annotation.Excel
@Setter
@Getter
public class StatisticsWarehouseInCommission extends BaseEntity {
    @ExcelCol(value = 0, title = "单据号")
    private String warehouseInSn;           //单据号(warehouse_in)
    @ExcelCol(value = 1, title = "单据类型")
    private String status;                  //单据类型(warehouse_in)
    @ExcelCol(value = 12, title = "供应商", width = 16)
    private String supplierName;            //供应商(warehouse_in)
    @ExcelCol(value = 3, title = "零件号", width = 16)
    private String goodsFormat;             //零件号(goods)
    @ExcelCol(value = 4, title = "配件名称", width = 16)
    private String goodsName;               //零件名(goods)
    @ExcelCol(value = 9, title = "类别")
    private String catName;                 //类别(goods_category)
    @ExcelCol(value = 10, title = "仓位")
    private String depot;                   //仓位(goods)
    @ExcelCol(value = 8, title = "单位")
    private String measureUnit;             //单位(warehouse_in_detail)
    @ExcelCol(value = 7, title = "入库数量")
    private Long goodsCount;                //实际入库数量(warehouse_in_detail)
    private Long goodsRealCount;            //可退数量(warehouse_in_detail)
    private Long goodsReturnCount;          //退货数量（等于实际入库数量为负数时的数量，为正数时为0）
    @ExcelCol(value = 5, title = "成本价")
    private BigDecimal purchasePrice;       //成本单价(实际入库数量*成本金额)
    @ExcelCol(value = 6, title = "成本金额")
    private BigDecimal purchaseAmount;      //成本金额(warehouse_in_detail)
    private BigDecimal returnAmount;        //退货成本金额(实际入库数量（为负数时的数量）*成本单价)
    @ExcelCol(value = 13, title = "采购人")
    private String purchaseAgentName;       //采购人(warehouse_in)
    @ExcelCol(value = 14, title = "开单人")
    private String creatorName;             //开单人(warehouse_in)
    private Long shopId;                    //门店id(warehouse_in_detail)
    private Long goodsId;                   //物料id(warehouse_in_detail)
    @ExcelCol(value = 2, title = "入库日期")
    String gmtCreateStr;                    //入库日期

    String carInfoStr;                      //适用车型0；通用配件
    List<GoodsCar> carInfoList;             //json类型适用车型
    Long totalCount;                        //总数量
    BigDecimal totalPurchase;               //总成本金额
    Integer totalSize;                         //总条数
    //add by twg
    private String brandName;               //品牌名称
    private BigDecimal fare;             //运费
    private String goodsSn;                 //物料编号
    private String relSn; //

    public String getGmtCreateStr() {
        return DateUtil.convertDateToYMD(super.getGmtCreate());
    }

    public BigDecimal getPurchaseAmount(){
        if(purchaseAmount==null){
            return BigDecimal.ZERO;
        }
        return purchaseAmount.setScale(2,BigDecimal.ROUND_HALF_UP);
    }
    public BigDecimal getPurchasePrice(){
        if(purchasePrice==null){
            return BigDecimal.ZERO;
        }
        return purchasePrice.setScale(2,BigDecimal.ROUND_HALF_UP);
    }

    @ExcelCol(value = 11, title = "适用车型")
    public String getCarInfos(){
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotEmpty(carInfoStr)) {
            sb.append(carInfoStr).append(" ");
        }
        if (!CollectionUtils.isEmpty(carInfoList)) {
            for (GoodsCar car : carInfoList) {
                sb.append(car.getCarInfo()).append(" ");
            }
        }
        if (sb.length() == 0) {
            return "";
        } else {
            return sb.substring(0, sb.length()-1);
        }
    }
}
