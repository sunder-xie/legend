package com.tqmall.legend.biz.order.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by zsy on 16/6/8.
 * 收款单bo对象
 */
@Data
public class DebitBillBo{
    private Long relId;//关联id
    private BigDecimal totalAmount;//总计金额
    private BigDecimal receivableAmount;//应收金额
    private String remark;//备注
}
