package com.tqmall.legend.entity.account.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by majian on 16/6/2.
 */
@Data
public class RechargeComboFlowBo {
    private Long accountId;
    private Long flowId;
    private Date date;
    private String customerName;
    private String phone;
    private String comboName;
    private BigDecimal payAmount;//收款金额
    private String recieverName;//服务顾问
    private String creatorName;//发卡人
    private Integer isReversed;

    public String getDateStr() {
        SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd");
        return df.format(date);
    }
}
