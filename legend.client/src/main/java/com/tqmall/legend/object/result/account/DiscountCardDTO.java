package com.tqmall.legend.object.result.account;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by wanghui on 6/17/16.
 */
@Data
public class DiscountCardDTO implements Serializable {
    /**
     * 会员卡id
     */
    private Long cardId;
    /**
     * 会员卡号
     */
    private String cardNumber;
    /**
     * 是否选择会员折扣
     */
    private boolean selected;
    /**
     * 会员卡名称
     */
    private String cardTypeName;
    /**
     * 会员卡余额
     */
    private BigDecimal balance;
    /**
     * 会员卡折扣类型
     * 0.无折扣;1.全部工单折扣;2.全部服务折扣;3.全部配件折扣;4.多种类型 折扣（服务折扣类型、配件折扣类型）
     */
    private Integer discountType;
    /**
     * 服务折扣类型:0.无折扣;1.全部服务享受折扣;2部分服务享受折扣
     */
    private Integer serviceDiscountType;
    /**
     * 全部服务折扣
     */
    private BigDecimal serviceDiscountRate;
    /**
     * 配件折扣类型:0.无折扣;1.全部配件享用折扣;2.部分配件享受折扣
     */
    private Integer goodsDiscountType;
    /**
     * 全部配件折扣
     */
    private BigDecimal goodsDiscountRate;
    /**
     * 会员卡折扣(仅用于全部工单折扣)
     */
    private BigDecimal discountRate;
    /**
     * 折扣金额
     */
    private BigDecimal discountAmount;
    /**
     * 是否可用
     */
    private boolean available;
    /**
     * 会员卡折扣信息描述
     */
    private String discountDescription;

    /**
     * 会员卡特定服务折扣
     */
    private List<CardServiceRelDTO> cardServiceRels;
    /**
     * 会员卡特定物料折扣
     */
    private List<CardGoodsRelDTO> cardGoodsRels;

    /**
     * 持卡人账户id
     */
    private Long accountId;

    /**
     * 持卡人姓名
     */
    private String customerName;

    /**
     * 持卡人手机号
     */
    private String mobile;
}
