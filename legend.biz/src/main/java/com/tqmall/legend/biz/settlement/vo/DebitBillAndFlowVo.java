package com.tqmall.legend.biz.settlement.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xin on 16/6/7.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class DebitBillAndFlowVo implements Serializable {

    private static final long serialVersionUID = 8555623831074978513L;

    private DebitBillVo debitBillVo;
    // 冲红单
    private List<DebitBillVo> redBillVoList;

    private List<DebitBillFlowVo> debitBillFlowVoList;

}
