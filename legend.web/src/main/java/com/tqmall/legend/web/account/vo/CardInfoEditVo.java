package com.tqmall.legend.web.account.vo;

import com.tqmall.legend.entity.account.CardGoodsRel;
import com.tqmall.legend.entity.account.CardServiceRel;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by majian on 16/10/8.
 */
public class CardInfoEditVo {
    private Long id;
    private String typeName;//会员卡类型名
    private String cardInfoExplain;//描述
    private Integer discountType;//折扣类型:0.无折扣;1.全部工单折扣;2.全部服务折扣;3.全部配件折扣;4.多种类型 折扣（服务折扣类型、配件折扣类型）
    private BigDecimal orderDiscount;//工单折扣
    private Integer serviceDiscountType;//服务折扣类型:0.无折扣;1.全部服务享受折扣;2部分服务享受折扣
    private BigDecimal serviceDiscount;//服务折扣
    private Integer goodDiscountType;//配件折扣类型:0.无折扣;1.全部配件享用折扣;2.部分配件享受折扣
    private BigDecimal goodDiscount;//配件折扣
    private Long effectivePeriodDays;//有效期
    private boolean compatibleWithCoupon;//0:不允许与优惠券共同使用;1:允许与其他优惠券共同使用
    private boolean generalUse;//0:不允许其他人非该账户下的车辆使用;1.允许其他人非该账户下的车辆使用
    private BigDecimal initBalance;//初始余额
    private BigDecimal salePrice;//售卖金额
    private Integer cardInfoStatus;//状态1启用2停用
    private List<CardServiceRel> cardServiceRels;
    private List<GoodsCatParam> cardGoodsRels;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getCardInfoExplain() {
        return cardInfoExplain;
    }

    public void setCardInfoExplain(String cardInfoExplain) {
        this.cardInfoExplain = cardInfoExplain;
    }

    public Integer getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getOrderDiscount() {
        return orderDiscount;
    }

    public void setOrderDiscount(BigDecimal orderDiscount) {
        this.orderDiscount = orderDiscount;
    }

    public Integer getServiceDiscountType() {
        return serviceDiscountType;
    }

    public void setServiceDiscountType(Integer serviceDiscountType) {
        this.serviceDiscountType = serviceDiscountType;
    }

    public BigDecimal getServiceDiscount() {
        return serviceDiscount;
    }

    public void setServiceDiscount(BigDecimal serviceDiscount) {
        this.serviceDiscount = serviceDiscount;
    }

    public Integer getGoodDiscountType() {
        return goodDiscountType;
    }

    public void setGoodDiscountType(Integer goodDiscountType) {
        this.goodDiscountType = goodDiscountType;
    }

    public BigDecimal getGoodDiscount() {
        return goodDiscount;
    }

    public void setGoodDiscount(BigDecimal goodDiscount) {
        this.goodDiscount = goodDiscount;
    }

    public Long getEffectivePeriodDays() {
        return effectivePeriodDays;
    }

    public void setEffectivePeriodDays(Long effectivePeriodDays) {
        this.effectivePeriodDays = effectivePeriodDays;
    }

    public boolean isCompatibleWithCoupon() {
        return compatibleWithCoupon;
    }

    public void setCompatibleWithCoupon(boolean compatibleWithCoupon) {
        this.compatibleWithCoupon = compatibleWithCoupon;
    }

    public boolean isGeneralUse() {
        return generalUse;
    }

    public void setGeneralUse(boolean generalUse) {
        this.generalUse = generalUse;
    }

    public BigDecimal getInitBalance() {
        return initBalance;
    }

    public void setInitBalance(BigDecimal initBalance) {
        this.initBalance = initBalance;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public Integer getCardInfoStatus() {
        return cardInfoStatus;
    }

    public void setCardInfoStatus(Integer cardInfoStatus) {
        this.cardInfoStatus = cardInfoStatus;
    }

    public List<CardServiceRel> getCardServiceRels() {
        return cardServiceRels;
    }

    public void setCardServiceRels(List<CardServiceRel> cardServiceRels) {
        this.cardServiceRels = cardServiceRels;
    }

    public List<GoodsCatParam> getCardGoodsRels() {
        return cardGoodsRels;
    }

    public void setCardGoodsRels(List<GoodsCatParam> cardGoodsRels) {
        this.cardGoodsRels = cardGoodsRels;
    }
}
