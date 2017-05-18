package com.tqmall.legend.facade.settlement.bo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by xin on 16/6/18.
 */
@Data
public class DebitBillFlowSaveBo {
    private Long shopId;
    private Long userId;
    private Long orderId;
    private Date flowTime;
    private String remark;
    private List<DebitBillFlowBo> flowList;
    // 会员卡消费金额
    private BigDecimal memberPayAmount;
    // 会员卡id
    private Long memberCardId;
}
