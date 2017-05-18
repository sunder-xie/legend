package com.tqmall.legend.object.param.account;

import java.io.Serializable;

/**
 * Created by wushuai on 2016/08/17.
 */
public class QryAccountComboParam implements Serializable {
    private static final long serialVersionUID = 4776319033166482193L;
    private Long userGlobalId;
    private String mobile;//车主电话
    private Integer comboStatus;//0.正常,1.用完
    private Integer expireStautus;//1.未过期,2.已过期
    private Long offset;
    private Integer limit;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getUserGlobalId() {
        return userGlobalId;
    }

    public void setUserGlobalId(Long userGlobalId) {
        this.userGlobalId = userGlobalId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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
        return offset == null ? 0l : offset;
    }

    public void setOffset(Long offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit == null ? 10 : limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}