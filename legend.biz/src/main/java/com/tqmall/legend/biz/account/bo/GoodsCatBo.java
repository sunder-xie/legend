package com.tqmall.legend.biz.account.bo;


import java.math.BigDecimal;

/**
 * Created by majian on 16/9/28.
 */
public class GoodsCatBo {
    private Long catId;
    private String catName;
    private BigDecimal discount;
    private Integer catSource;

    public Integer getCatSource() {
        return catSource;
    }

    public void setCatSource(Integer catSource) {
        this.catSource = catSource;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Long getCatId() {
        return catId;
    }

    public void setCatId(Long catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }
}
