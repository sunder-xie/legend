package com.tqmall.legend.object.result.account;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by wanghui on 6/16/16.
 */
@Data
public class CardMemberDTO implements Serializable {
    /**
     * 会员卡id
     */
    private Long memberCardId;
    /**
     * 会员卡号
     */
    private String cardNumber;
    /**
     * 会员卡余额
     */
    private BigDecimal balance;
    /**
     * 会员卡类型名
     */
    private String cardTypeName;
    /**
     * 会员历史消费金额
     */
    private BigDecimal expenseAmount;
    /**
     * 会员累计充值金额
     */
    private BigDecimal depositAmount;
    /**
     * 会员积分
     */
    private BigDecimal cardPoints;
    /**
     * 折扣类型
     */
    private Integer discountType;
    /**
     * 折扣描述
     */
    private String discountDescription;
    /**
     * 折扣额度
     */
    private BigDecimal discount;
    /**
     * 服务折扣类型:0.无折扣;1.全部服务享受折扣;2部分服务享受折扣
     */
    private Integer serviceDiscountType;
    /**
     * serviceDiscountType=1时，全部服务折扣
     */
    private BigDecimal serviceDiscount;
    /**
     * 配件折扣类型:0.无折扣;1.全部配件享用折扣;2.部分配件享受折扣
     */
    private Integer goodsDiscountType;
    /**
     * goodsDiscountType=1时，全部配件折扣
     */
    private BigDecimal goodsDiscount;
    /**
     * 会员卡失效时间
     */
    private Date expireDate;
    /**
     * 0:不允许与优惠券共同使用;1:允许与其他优惠券共同使用
     */
    private Integer compatibleWithCoupon;
    /**
     * 0:不允许其他人非该账户下的车辆使用;1.允许其他人非该账户下的车辆使用
     */
    private Integer generalUse;

    /**
     * 会员卡描述
     *
     * @return
     */
    private String limitDescription;

    /**
     * 会员卡生效时间
     *
     * @return
     */
    private Date createDate;
    /**
     * 会员卡特定服务折扣
     */
    private List<CardServiceRelDTO> cardServiceRels;
    /**
     * 会员卡特定物料折扣
     */
    private List<CardGoodsRelDTO> cardGoodsRels;

    /**
     * 是否属于他人（不是归属和关联账户下的卡）
     */
    private boolean belongOther;

    /**
     * 持有该卡的车主
     */
    private String customerName;

    /**
     * 持有该卡的车主手机号
     */
    private String mobile;
}
