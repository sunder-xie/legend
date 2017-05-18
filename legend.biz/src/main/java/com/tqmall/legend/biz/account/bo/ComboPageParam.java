package com.tqmall.legend.biz.account.bo;

import java.util.List;

/**
 * Created by majian on 16/11/10.
 */
public class ComboPageParam {
    private Long shopId;
    private List<Long> accountIds;
    private Integer comboStatus;//0.正常,1.用完
    private Integer expireStautus;//1.未过期,2.已过期
    private Long offset;
    private Integer limit;
    private List<String> sorts;

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public List<Long> getAccountIds() {
        return accountIds;
    }

    public void setAccountIds(List<Long> accountIds) {
        this.accountIds = accountIds;
    }

    public Integer getComboStatus() {
        return comboStatus;
    }

    public void setComboStatus(Integer comboStatus) {
        this.comboStatus = comboStatus;
    }

    public Integer getExpireStautus() {
        return expireStautus;
    }

    public void setExpireStautus(Integer expireStautus) {
        this.expireStautus = expireStautus;
    }

    public Long getOffset() {
        return offset;
    }

    public void setOffset(Long offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public List<String> getSorts() {
        return sorts;
    }

    public void setSorts(List<String> sorts) {
        this.sorts = sorts;
    }
}
