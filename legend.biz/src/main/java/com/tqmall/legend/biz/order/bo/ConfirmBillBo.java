package com.tqmall.legend.biz.order.bo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by zsy on 16/6/4.
 * 确认账单bo对象
 */
@Data
public class ConfirmBillBo {
    /**
     * 收款单bo对象
     */
    private DebitBillBo debitBillBo;

    /**
     * 券:优惠券、折扣券、计次卡、转赠券
     */
    private List<OrderDiscountFlowBo> orderDiscountFlowBoList;//工单优惠流水

    /**
     * 折扣优惠（如果没有会员卡id，则工单流水discount_type为2优惠）
     * 注：折扣、会员卡优惠、单纯优惠互斥
     */
    private BigDecimal discountRate;//折扣（单纯的折扣，会员卡目前无需传此字段）
    private Long memberCardId;//会员卡id
    private Long accountId;//账户id
    private String cardNumber;//会员卡编号
    private String cardDiscountReason;//会员卡优惠原因
    private BigDecimal discountAmount;//优惠金额
    /**
     * 淘汽优惠券
     */
    private String taoqiCouponSn;
    private BigDecimal taoqiCouponAmount;

    /**
     * 工单字段
     * 费用
     */
    private BigDecimal taxAmount;
}
