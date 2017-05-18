package com.tqmall.legend.entity.warehousein;

import com.tqmall.legend.common.CommonUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * Created by guangxue on 15/1/14.
 */

@EqualsAndHashCode(callSuper = false)
@Data
public class WarehouseInPrintVO {
    private Long id;
    private String format;
    private String goodsName;
    private String carInfo;
    private Long catId;
    private String depot;
    private Integer goodsCount;
    private BigDecimal purchasePrice;
    private String purchasePriceStr;

    private BigDecimal purchaseAmount;
    private String purchaseAmountStr;
    private BigDecimal stock;

    public String getPurchasePriceStr() {
        return CommonUtils.convertMoney(purchasePrice);
    }

    public String getPurchaseAmountStr() {
        return CommonUtils.convertMoney(purchaseAmount);
    }
}
