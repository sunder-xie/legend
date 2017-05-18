package com.tqmall.legend.entity.statistics;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 物料毛利明细报表
 * @author wjc
 *
 * 2015年9月15日下午4:49:22
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class GrossProfitDetail {
    private Date payTime;
    private String payTimeStr;
    private String customerName;
    private String carLicense;
    private String carBrand;
    private String carSeries;
    private String carPower;
    private String carYear;
    private String carModels;
    private String catName;
    private String brandName;
    private String name;
    private Long goodsNumber;
    private String measureUnit;
    private BigDecimal soldPrice;
    private BigDecimal inventoryPrice;
    private BigDecimal soldAmount;
    private BigDecimal grossProfit;
    private BigDecimal grossProfitMargin;
    private String goodsNote;
    private String receiverName;
    private String workerName;
    private String orderSn;
    private Long id;
    
    public String getPayTimeStr(){
        if (payTimeStr != null) {
            return payTimeStr;
        }
        SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        if (payTime != null) {
            return df.format(payTime);
        }
        return null;
    }
}
