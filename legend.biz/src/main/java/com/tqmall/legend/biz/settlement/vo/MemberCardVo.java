package com.tqmall.legend.biz.settlement.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by xin on 16/6/8.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class MemberCardVo implements Serializable {
    private static final long serialVersionUID = -6951179351280178723L;

    private Long id;
    private BigDecimal payAmount;
    private BigDecimal balance;
}
