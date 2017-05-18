package com.tqmall.legend.web.account.vo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by majian on 16/9/28.
 */
public class CardCreateParam {
    private Long id;
    private String name;
    private String descript;
    private Integer discountType;//折扣类型:0.无折扣;1.全部工单折扣;2.服务折扣;3.配件折扣;4.多种类型折扣（服务折扣类型、配件折扣类型）
    private Integer serviceDiscountType;//服务折扣类型:0.无折扣;1.全部折扣;2部分折扣
    private Integer goodsDiscountType;//配件折扣类型:0.无折扣;1.全部折扣;2部分折扣
    private BigDecimal orderDiscount;//工单折扣 discountType = 1 时使用
    private BigDecimal serviceDiscount;//服务折扣 discountType = 2  serviceDiscountType = 1时使用
    private BigDecimal goodsDiscount;//配件折扣 discountType = 3 goodsDiscountType = 1时使用
    private Integer effectivePeriodDays;//有效期(天)
    private boolean compatibleWithCoupon;//能否与优惠券共同使用?
    private boolean generalUse;//是否允许其他人使用
    private BigDecimal initBalance;
    private BigDecimal salePrice;
    private Map<Long,BigDecimal> serviceRels;//<serviceCatId,discount>
    private List<GoodsCatParam> goodsCats;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Map<Long, BigDecimal> getServiceRels() {
        return serviceRels;
    }

    public void setServiceRels(Map<Long, BigDecimal> serviceRels) {
        this.serviceRels = serviceRels;
    }

    public List<GoodsCatParam> getGoodsCats() {
        return goodsCats;
    }

    public void setGoodsCats(List<GoodsCatParam> goodsCats) {
        this.goodsCats = goodsCats;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public Integer getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }

    public Integer getServiceDiscountType() {
        return serviceDiscountType;
    }

    public void setServiceDiscountType(Integer serviceDiscountType) {
        this.serviceDiscountType = serviceDiscountType;
    }

    public Integer getGoodsDiscountType() {
        return goodsDiscountType;
    }

    public void setGoodsDiscountType(Integer goodsDiscountType) {
        this.goodsDiscountType = goodsDiscountType;
    }

    public BigDecimal getOrderDiscount() {
        return orderDiscount;
    }

    public void setOrderDiscount(BigDecimal orderDiscount) {
        this.orderDiscount = orderDiscount;
    }

    public BigDecimal getServiceDiscount() {
        return serviceDiscount;
    }

    public void setServiceDiscount(BigDecimal serviceDiscount) {
        this.serviceDiscount = serviceDiscount;
    }

    public BigDecimal getGoodsDiscount() {
        return goodsDiscount;
    }

    public void setGoodsDiscount(BigDecimal goodsDiscount) {
        this.goodsDiscount = goodsDiscount;
    }

    public Integer getEffectivePeriodDays() {
        return effectivePeriodDays;
    }

    public void setEffectivePeriodDays(Integer effectivePeriodDays) {
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
}
