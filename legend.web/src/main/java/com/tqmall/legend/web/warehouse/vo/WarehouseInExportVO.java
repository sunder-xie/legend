package com.tqmall.legend.web.warehouse.vo;

import com.tqmall.wheel.component.excel.annotation.Excel;
import com.tqmall.wheel.component.excel.annotation.ExcelCol;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Created by lilige on 17/2/8.
 */
@Excel
@Getter
@Setter
public class WarehouseInExportVO {
    @ExcelCol(value = 0, title = "入库单号", width = 20)
    private String warehouseInSn;
    @ExcelCol(value = 1, title = "类型")
    private String status;
    @ExcelCol(value = 2, title = "入库日期", width = 16)
    private String inTime;
    @ExcelCol(value = 3, title = "零件号", width = 20)
    private String goodsFormat;
    @ExcelCol(value = 4, title = "零件名", width = 20)
    private String goodsName;
    @ExcelCol(value = 5, title = "类别", width = 16)
    private String catName;
    @ExcelCol(value = 6, title = "单价", width = 10)
    private BigDecimal purchasePrice;
    @ExcelCol(value = 7, title = "入库数量", width = 10)
    private BigDecimal goodsCount;
    @ExcelCol(value = 8, title = "单位")
    private String measureUnit;
    @ExcelCol(value = 9, title = "总金额", width = 12)
    private BigDecimal purchaseAmount;
    @ExcelCol(value = 10, title = "仓位", width = 16)
    private String depot;
    @ExcelCol(value = 11, title = "适用车型", width = 20)
    private String carInfoStr;
    @ExcelCol(value = 12, title = "供应商", width = 20)
    private String supplierName;
    @ExcelCol(value = 13, title = "采购人", width = 16)
    private String purchaseAgentName;
    @ExcelCol(value = 14, title = "开单人", width = 16)
    private String operatorName;
    @ExcelCol(value = 15, title = "备注", width = 20)
    private String comment;
}

