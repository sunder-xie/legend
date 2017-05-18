package com.tqmall.legend.biz.settlement.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by sven on 16/6/8.
 */
@Data
public class PayBillVo {
    //类型
    private Long payTypeId;
    private String payTypeName;
    //付款方式
    private Long paymentId;
    private String paymentName;
    //付款金额
    private BigDecimal amount;
    //款项名称
    private String billName;
    //收银
    private String operatorName;
    private String payeeName;
    private String billTime;
    private String remark;
    private String ids;
}
