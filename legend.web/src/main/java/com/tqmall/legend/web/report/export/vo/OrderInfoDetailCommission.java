package com.tqmall.legend.web.report.export.vo;

import com.tqmall.wheel.component.excel.annotation.ExcelCol;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by tanghao on 17/2/7.
 */
@Setter
@Getter
@com.tqmall.wheel.component.excel.annotation.Excel
public class OrderInfoDetailCommission implements Serializable{
    private Long shopId;
    @ExcelCol(value = 0, title = "工单编号")
    private String orderSn;//工单编号
    private Long orderId;//工单id
    @ExcelCol(value = 1, title = "工单类型")
    private String orderTag;//工单类型(洗车单..)
    @ExcelCol(value = 2, title = "业务类型")
    private String orderType;//业务类型(大维修..)
    @ExcelCol(value = 3, title = "车牌")
    private String carLicense;//车牌
    @ExcelCol(value = 4, title = "车主")
    private String customerName;//车主
    @ExcelCol(value = 5, title = "开单时间")
    private String createTime;//开单时间
    @ExcelCol(value = 6, title = "结算时间")
    private String payTime;//结算时间
    @ExcelCol(value = 7, title = "工单状态")
    private String orderStatus;//工单状态
    @ExcelCol(value = 8, title = "销售员")
    private String salerName;//销售员
    @ExcelCol(value = 9, title = "服务顾问")
    private String receiverName;//服务顾问
    @ExcelCol(value = 10, title = "维修工")
    private String workers;//维修工
    @ExcelCol(value = 11, title = "工时")
    private BigDecimal serviceHour;//工时
    @ExcelCol(value = 12, title = "总计金额")
    private BigDecimal allTotalAmount;//总计

    private BigDecimal totalAmount;//应收金额(pre_total_amount)

    private BigDecimal extraAmount;//附加费
    @ExcelCol(value = 15, title = "物料成本")
    private BigDecimal goodsAmount;//物料成本

    private BigDecimal grossProfitAmount;//毛利

    private BigDecimal grossProfitRate;//毛利率

    @ExcelCol(value = 13, title = "应收金额")
    public String getTotalAmountStr(){
        if (!"已结算".equals(orderStatus)){
            return "--";
        } else {
          return totalAmount + "";
        }
    }

    @ExcelCol(value = 14, title = "附加费用")
    public String getExtraAmountStr(){
        if (!"综合维修".equals(orderTag)){
            return "--";
        } else if (null == extraAmount) {
            return "--";
        }else {
            return extraAmount + "";
        }
    }
    @ExcelCol(value = 16, title = "毛利")
    public String getGrossProfitAmountStr(){
        if (!"已结算".equals(orderStatus)){
            return "--";
        } else {
            return grossProfitAmount + "";
        }
    }
    @ExcelCol(value = 17, title = "毛利率")
    public String getGrossProfitRateStr(){
        if (!"已结算".equals(orderStatus)){
            return "--";
        } else {
            return grossProfitRate + "%";
        }
    }
}
