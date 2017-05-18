package com.tqmall.legend.object.result.account;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by majian on 16/9/6.
 */
@Data
public class MemberCardInfoDTO implements Serializable {
    private Long id;
    private String deleted;
    private Date gmtCreate;
    private Date gmtModifier;
    private Long creator;
    private Long modifier;
    private Long shopId;//门店id
    private String typeName;//会员卡类型名
    private String cardInfoExplain;//描述
    private Integer serviceDiscountType;//服务折扣类型:0.无折扣;1.全部服务享受折扣;2部分服务享受折扣
    private BigDecimal serviceDiscount;//服务折扣
    private Integer goodDiscountType;//配件折扣类型:0.无折扣;1.全部配件享用折扣;2.部分配件享受折扣
    private BigDecimal goodDiscount;//配件折扣
    private Long effectivePeriodDays;//有效期
    private Integer compatibleWithCoupon;//0:不允许与优惠券共同使用;1:允许与其他优惠券共同使用
    private Integer generalUse;//0:不允许其他人非该账户下的车辆使用;1.允许其他人非该账户下的车辆使用
    private BigDecimal initBalance;//初始余额
    private BigDecimal salePrice;//售卖金额
    private Integer discountType;//折扣类型:0.无折扣;1.全部工单折扣;2.服务折扣(见service_discount_type);3.配件折扣(见goods_discount_type);4.多种类型折扣（服务折扣类型、配件折扣类型）
    private String discountTypeDescription;//折扣类型描述:0.无折扣;1.全部工单折扣;2.服务折扣(见service_discount_type);3.配件折扣(见goods_discount_type);4.多种类型折扣（服务折扣类型、配件折扣类型）
    private String discountTypeDescriptionDetail;//折扣类型详情描述:部分服务类型或者配件类型折扣时将列出每类的折扣
    private BigDecimal orderDiscount;//工单折扣
    private Integer cardInfoStatus;//状态1启用2停用
}
