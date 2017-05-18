package com.tqmall.legend.biz.settlement.vo;

import com.tqmall.common.util.DateUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by xin on 16/6/4.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class DebitBillVo implements Serializable {

    private static final long serialVersionUID = 5078346892355088953L;

    private Long id;

    private Date gmtCreate;
    private String gmtCreateStr;

    private Long creator;

    private Long shopId;

    private Long relId;

    private String billSn;

    private String billName;

    private Byte billType;

    private Long debitTypeId;
    private String debitTypeName;

    private Date billTime;
    private String billTimeStr;

    private Date settleTime;
    private String settleTimeStr;

    private BigDecimal totalAmount;

    private BigDecimal receivableAmount;

    private BigDecimal paidAmount;

    private BigDecimal signAmount;

    private BigDecimal badAmount;

    private Long paymentId;

    private String paymentName;

    private String payAccount;

    private BigDecimal payAmount;

    private String operatorName;

    private String payerName;

    private String remark;

    public String getGmtCreateStr() {
        return DateUtil.convertDateToStr(gmtCreate, "yyyy-MM-dd HH:mm");
    }

    public String getBillTimeStr() {
        return DateUtil.convertDateToStr(billTime, "yyyy-MM-dd HH:mm");
    }

    public String getSettleTimeStr() {
        return DateUtil.convertDateToStr(settleTime, "yyyy-MM-dd HH:mm");
    }
}
