package com.tqmall.legend.web.vo.report.order;

import com.tqmall.wheel.component.excel.annotation.Excel;
import com.tqmall.wheel.component.excel.annotation.ExcelCol;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by 辉辉大侠 on 23/11/2016.
 */
@Excel(datePattern = "yyyy-MM-dd HH:mm:ss")
@Data
public class OrderServiceExcelVo {
    @ExcelCol(value = 0, title = "开单时间", width = 20)
    private Date orderCreateDate;
    @ExcelCol(value = 1, title = "结算时间", width = 20)
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
    @ExcelCol(value = 7, title = "服务名称", width = 20)
    private String serviceName;
    @ExcelCol(value = 8, title = "服务类别")
    private String serviceCatName;
    @ExcelCol(value = 9, title = "工时费")
    private BigDecimal servicePrice;
    @ExcelCol(value = 10, title = "工时")
    private BigDecimal serviceHour;
    @ExcelCol(value = 11, title = "金额")
    private BigDecimal serviceAmount;
    @ExcelCol(value = 12, title = "优惠")
    private BigDecimal discount;
    @ExcelCol(value = 13, title = "维修工")
    private String workerName;
    @ExcelCol(value = 14, title = "服务顾问")
    private String receiverName;
}
