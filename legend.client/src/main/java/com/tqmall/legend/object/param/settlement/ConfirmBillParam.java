package com.tqmall.legend.object.param.settlement;

import com.tqmall.legend.object.param.BaseRpcParam;
import com.tqmall.legend.object.param.account.DiscountSelectedComboParam;
import com.tqmall.legend.object.param.account.DiscountSelectedCouponParam;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by zsy on 16/6/18.
 */
@Data
public class ConfirmBillParam extends BaseRpcParam {
    private static final long serialVersionUID = -9060270651787425219L;

    private Long userId;//用户id
    private Long shopId;//门店id

    /**
     * 收款单信息
     */
    private Long orderId;//工单id
    private BigDecimal receivableAmount;//应收金额
    private String remark;//备注
    private BigDecimal taxAmount;//费用

    /**
     * 选择计次卡
     */
    private List<DiscountSelectedComboParam> discountSelectedComboParamList;

    /**
     * 选择的现金券和通用券
     */
    private List<DiscountSelectedCouponParam> discountSelectedCouponParamList;
    /**
     * 使用其他车主的会员卡、优惠券，需要传手机号和验证码
     */
    private String guestMobile;//其他车主的手机号

    /**
     * 会员卡优惠
     */
    private Long memberCardId;//会员卡id
    private BigDecimal discountAmount;//优惠金额

    /**
     * 淘汽优惠券
     */
    private String taoqiCouponSn;
}
