package com.tqmall.legend.web.account.vo;

import java.math.BigDecimal;

/**
 * Created by majian on 16/10/8.
 */
public class CardInfoVo {
    private Long id;
    private String typeName;//会员卡类型名
    private String cardInfoExplain;//描述
    private String discountDescript;//折扣
    private BigDecimal initBalance;//初始余额
    private BigDecimal salePrice;//售卖金额
    private Integer cardInfoStatus;//状态1启用2停用
    private Long effectivePeriodDays;//有效期
    private String gmtCreateStr;//创建日期
    private Integer grantedCount;//已发放数量

    public Integer getGrantedCount() {
        return grantedCount;
    }

    public void setGrantedCount(Integer grantedCount) {
        this.grantedCount = grantedCount;
    }

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

    public String getDiscountDescript() {
        return discountDescript;
    }

    public void setDiscountDescript(String discountDescript) {
        this.discountDescript = discountDescript;
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

    public Long getEffectivePeriodDays() {
        return effectivePeriodDays;
    }

    public void setEffectivePeriodDays(Long effectivePeriodDays) {
        this.effectivePeriodDays = effectivePeriodDays;
    }

    public String getGmtCreateStr() {
        return gmtCreateStr;
    }

    public void setGmtCreateStr(String gmtCreateStr) {
        this.gmtCreateStr = gmtCreateStr;
    }
}
