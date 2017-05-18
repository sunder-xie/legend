package com.tqmall.legend.object.param.settlement;

import com.tqmall.legend.object.param.BaseRpcParam;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by xin on 16/6/18.
 */
@Data
public class DebitBillFlowSaveParam extends BaseRpcParam {

    private static final long serialVersionUID = 6824805833376261210L;

    private Long shopId;
    private Long orderId;
    private String remark;
    private Date flowTime;
    // 会员卡
    private CardMemberParam cardMember;
    // 支付方式
    private List<DebitBillFlowParam> flowList;
}
