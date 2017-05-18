package com.tqmall.legend.biz.settlement.vo;

import com.tqmall.wheel.component.excel.annotation.Excel;
import com.tqmall.wheel.component.excel.annotation.ExcelCol;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by lixiao on 17/1/6.
 */
@Getter
@Setter
@Excel
public class PayResultVo {

    private Integer id;
    private Integer billId;
    @ExcelCol(value = 6, title = "付款日期", width = 20)
    private String flowTime;
    @ExcelCol(value = 2, title = "支付方式", width = 12)
    private String paymentName;
    @ExcelCol(value = 5, title = "付款金额", width = 12)
    private Double payAmount;
    @ExcelCol(value = 7, title = "备注", width = 20)
    private String remark;
    @ExcelCol(value = 0, title = "业务类型", width = 16)
    private String typeName;
    private Integer relId;
    private Integer shopId;
    @ExcelCol(value = 1, title = "款项名称", width = 30)
    private String billName;
    @ExcelCol(value = 4, title = "收款方", width = 10)
    private String payeeName;
    private String operatorName;
    private String billTime;
    private Long creator;
    @ExcelCol(value = 3, title = "收银人员", width = 10)
    private String creatorName;
}
