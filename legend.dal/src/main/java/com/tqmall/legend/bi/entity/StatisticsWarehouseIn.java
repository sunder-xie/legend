package com.tqmall.legend.bi.entity;

/**
 * Created by zsy on 2015/1/27.
 */

import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.entity.base.BaseEntity;
import com.tqmall.legend.entity.goods.GoodsCar;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class StatisticsWarehouseIn extends BaseEntity {
    private String warehouseInSn;           //单据号(warehouse_in)
    private String status;                  //单据类型(warehouse_in)
    private String supplierName;            //供应商(warehouse_in)
    private String goodsFormat;             //零件号(goods)
    private String goodsName;               //零件名(goods)
    private String catName;                 //类别(goods_category)
    private String depot;                   //仓位(goods)
    private String measureUnit;             //单位(warehouse_in_detail)
    private Long goodsCount;                //实际入库数量(warehouse_in_detail)
    private Long goodsRealCount;            //可退数量(warehouse_in_detail)
    private Long goodsReturnCount;          //退货数量（等于实际入库数量为负数时的数量，为正数时为0）
    private BigDecimal purchasePrice;       //成本单价(实际入库数量*成本金额)
    private BigDecimal purchaseAmount;      //成本金额(warehouse_in_detail)
    private BigDecimal returnAmount;        //退货成本金额(实际入库数量（为负数时的数量）*成本单价)
    private String purchaseAgentName;       //采购人(warehouse_in)
    private String creatorName;             //开单人(warehouse_in)
    private Long shopId;                    //门店id(warehouse_in_detail)
    private Long goodsId;                   //物料id(warehouse_in_detail)
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

