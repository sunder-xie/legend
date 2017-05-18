package com.tqmall.legend.web.account.vo;

import java.math.BigDecimal;

/**
 * Created by majian on 16/9/28.
 */
public class GoodsCatParam {
    private Long catId;
    private String catName;
    private BigDecimal discount;
    private boolean customCat;

    public boolean isCustomCat() {
        return customCat;
    }

    public void setCustomCat(boolean customCat) {
        this.customCat = customCat;
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
