package com.tqmall.legend.biz.account.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Created by xin on 2017/1/4.
 */
@Getter
@Setter
public class BackCardVo {
    private Long shopId;
    private Long userId;
    private Long cardId;
    private Long paymentId;
    private String paymentName;
    private BigDecimal payAmount;
    private String remark;
}
