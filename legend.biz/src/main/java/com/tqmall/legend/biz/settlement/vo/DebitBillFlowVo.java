package com.tqmall.legend.biz.settlement.vo;

import com.tqmall.common.util.DateUtil;
import com.tqmall.wheel.component.excel.annotation.Excel;
import com.tqmall.wheel.component.excel.annotation.ExcelCol;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by xin on 16/6/5.
 */
@EqualsAndHashCode(callSuper = false)
@Data
@Excel
public class DebitBillFlowVo implements Serializable {

    private static final long serialVersionUID = -8352784082821430396L;

    private Long id;

    private Date gmtCreate;
    private String gmtCreateStr;

    private Long creator;
    @ExcelCol(value = 3, title = "收银人员", width = 10)
    private String creatorName;

    private Long shopId;

    private Long billId;

    private String flowSn;

    private Byte flowType;

    private Byte flowStatus;

    private Date flowTime;
    @ExcelCol(value = 6, title = "收款日期", width = 20)
    private String flowTimeStr;

    private Long paymentId;

    private String paymentName;
    @ExcelCol(value = 2, title = "支付方式", width = 16)
    private String paymentNameStr;

    private String payAccount;
    @ExcelCol(value = 5, title = "收款金额", width = 12)
    private BigDecimal payAmount;

    private String operatorName;

    private String remark;

    // 收款单字段 start
    private Long relId;
    @ExcelCol(value = 1, title = "款项名称", width = 30)
    private String billName;
    private Byte billType;
    private Long debitTypeId;
    @ExcelCol(value = 0, title = "类别", width = 10)
    private String debitTypeName;
    @ExcelCol(value = 4, title = "付款方", width = 10)
    private String payerName;
    // 收款单字段 end

    public String getGmtCreateStr() {
        return DateUtil.convertDateToStr(gmtCreate, "yyyy-MM-dd HH:mm");
    }

    public String getFlowTimeStr () {
        return DateUtil.convertDateToStr(flowTime, "yyyy-MM-dd HH:mm");
    }


    public String getPaymentNameStr() {
        if(paymentName == null || flowStatus == null || flowType == null){
            return "";
        }
        if (flowStatus == 0) {
            if (flowType == 1) {
                return "冲红" + paymentName;
            } else {
                return paymentName;
            }
        } else {
            return "坏账";
        }
    }
}
