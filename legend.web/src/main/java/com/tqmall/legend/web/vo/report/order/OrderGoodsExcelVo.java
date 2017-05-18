package com.tqmall.legend.web.vo.report.order;

import com.tqmall.wheel.component.excel.annotation.Excel;
import com.tqmall.wheel.component.excel.annotation.ExcelCol;
import com.tqmall.wheel.utils.DateFormatUtils;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by 辉辉大侠 on 22/11/2016.
 */
@Excel
@Data
public class OrderGoodsExcelVo {
    private Date orderCreateDate;
    private Date orderConfirmTime;
    @ExcelCol(value = 2, title = "工单状态")
    private String orderStatus;
    @ExcelCol(value = 3, title = "工单号", width = 20)
    private String orderSn;
    @ExcelCol(value = 4, title = "车牌", width = 16)
    private String carLicense;
    @ExcelCol(value = 5, title = "车型", width = 16)
    private String carModels;
    @ExcelCol(value = 6, title = "车主")
    private String customerName;
    @ExcelCol(value = 7, title = "配件名称", width = 60)
    private String goodsName;
    @ExcelCol(value = 8, title = "零件号", width = 30)
    private String goodsFormat;
    @ExcelCol(value = 9, title = "成本单价")
    private BigDecimal inventoryPrice;
    @ExcelCol(value = 10, title = "销售单价")
    private BigDecimal salePrice;
    @ExcelCol(value = 11, title = "销售数量")
    private BigDecimal goodsNumber;
    @ExcelCol(value = 12, title = "单位")
    private String measureUnit;
    @ExcelCol(value = 13, title = "成本总额")
    private BigDecimal inventoryAmountTotal;
    @ExcelCol(value = 14, title = "销售总额")
    private BigDecimal saleTotalAmount;

    private BigDecimal grossProfit;
    @ExcelCol(value = 16, title = "销售员")
    private String saleName;
    @ExcelCol(value = 17, title = "服务顾问")
    private String receiverName;
    @ExcelCol(value = 18, title = "配件类型", width = 20)
    private String catName;
    @ExcelCol(value = 19, title = "配件品牌", width = 20)
    private String brandName;

    @ExcelCol(value = 15, title = "毛利")
    public BigDecimal getGrossProfitForExcel() {
        if ("已结算".equals(this.orderStatus)) {
            return this.grossProfit;
        } else {
            return null;
        }
    }

    @ExcelCol(value = 0, title = "开单时间", width = 20)
    public String getOrderCreateDateStr() {
        if (orderCreateDate == null) {
            return "";
        }
        return DateFormatUtils.toYMDHMS(orderCreateDate);
    }

    @ExcelCol(value = 1, title = "结算时间", width = 20)
    public String getOrderConfirmTimeStr() {
        if (orderConfirmTime == null) {
            return "";
        }
        return DateFormatUtils.toYMDHMS(orderConfirmTime);
    }
}
