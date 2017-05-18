package com.tqmall.legend.biz.settlement.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by xin on 16/6/13.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class BatchDebitVo implements Serializable{

    private static final long serialVersionUID = 8472973321751236860L;
    private Long shopId;
    private Long userId;
    private List<Long> orderIdList;
    private Long paymentId;
    private String paymentName;
    private BigDecimal payAmount;
    private String remark;
    private Date flowTime;
}
