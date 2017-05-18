package com.tqmall.legend.object.param.settlement;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by xiangDong.qu on 16/3/22.
 */
@Data
public class SettlementSaveParam implements Serializable {

    private static final long serialVersionUID = 4937577555404420205L;
    private Long userId;
    private Long shopId;
    private Integer orderTag;
    private Integer payStatus;

    /**
     * {
     * "orderId": "工单ID", Long
     * "orderSn": "工单编号",String
     * "payStatus": "工单支付状态",String
     * "totalAmount": "工单总金额",BigDecimal
     * "payAmount": "实际应收金额",BigDecimal
     * "goodsAmount": "物料总金额",BigDecimal
     * "serviceAmount": "服务总金额",BigDecimal
     * "taxAmount": "费用",BigDecimal
     * "discountRate": "折扣",BigDecimal
     * "preferentiaAmount": "优惠",BigDecimal
     * "memberPreAmount": "会员优惠",BigDecimal
     * "couponAmount": "代金券总金额",BigDecimal
     * "couponDetail": "[{\"couponSn\":代金券码,\"couponValue\":代金券金额}]",
     * "postscript": "备注",
     * "paymentJson": "[{\"paymentId\":\"1\",\"paymentName\":\"现金\",\"paymentValue\":100}]",
     * "signedAmount": "挂账金额", BigDecimal
     * "taoqiCouponAmount": "淘汽优惠券金额",BigDecimal
     * "taoqiCouponSn": "淘汽优惠券码",String
     * "otherCouponName": "其他优惠券名",String
     * "otherCouponAmount": "其他优惠券金额",BigDecimal
     * "otherCouponSn": "其他优惠券编码" String
     * }
     */
    private String paymentBillJson;

    /**
     * 会员卡信息
     * {
     * "id": "会员卡ID",Long
     * "cardSn": "会员卡编号",String
     * "memberPayAmount": "支付金额",BigDecimal
     * "balance": "剩余余额" BigDecimal
     * }
     */
    private String cardMemberJson;

    /**
     * 会员优惠券列表
     * [
     * {
     * "id": "优惠券编号", String
     * "serviceCount": "使用张数", String
     * "serviceValue": "优惠券价值" String
     * },
     * {
     * "id": "20429",
     * "serviceCount": "1",
     * "serviceValue": ""
     * }
     * ]
     */
    private String discountCouponJson;
}
