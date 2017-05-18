package com.tqmall.legend.object.result.settlement;

import com.tqmall.legend.object.result.order.OrderInfoDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by xin on 16/6/28.
 */
@Data
@EqualsAndHashCode
@ToString
public class OrderDebitBillDTO implements Serializable {

    private static final long serialVersionUID = -627410976218514574L;

    // 收款单id
    private Long billId;
    //工单信息
    private OrderInfoDTO orderInfoDTO;

    // 工单总计
    private BigDecimal totalAmount;
    // 应收金额
    private BigDecimal receivableAmount;
    // 实收金额
    private BigDecimal paidAmount;
    // 挂账金额
    private BigDecimal signAmount;

    // 收支历史记录(包括优惠流水和收款流水)
    List<HistoryFlowDTO> historyFlowList;

}
