package com.tqmall.legend.facade.warehouse.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sven on 2017/1/20.
 */
@Setter
@Getter
public class WarehouseOutRefundVo {
    private Long orderId;//工单id
    private String goodsFormat;//零件号
    private String goodsName;//零件名称
    private Long warehouseOutId;//退货单ID
    private String warehouseOutSn;//退货单号
    private String orderSn;//工单编号
    private String remark;//备注
    private BigDecimal salePrice; //销售价
    private Date outTime; //退货时间
    private BigDecimal goodsCount; //退货数量


    public BigDecimal getGoodsCount(){
        //空，展示0
        if(goodsCount == null){
            return BigDecimal.ZERO;
        }
        return goodsCount.abs();
    }

    public String getOutTimeStr(){
        if (outTime == null) {
            return null;
        }
        SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd");
        return f.format(outTime);
    }
}
