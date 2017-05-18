package com.tqmall.legend.dao.account.param;

import java.util.Date;

import org.springframework.data.domain.Pageable;

/**
 * Created by majian on 17/1/5.
 */
public class MemberExportPagedParam extends BasePagedParam{

    private MemberExportParam condition;

    public MemberExportPagedParam() {
    }

    public MemberExportPagedParam(Pageable pageable, MemberExportParam condition) {
        super(pageable);
        this.condition = condition;
    }

    public Date getNow() {
        return condition.getNow();
    }

    public void setNow(Date now) {
        condition.setNow(now);
    }

    public Long getShopId() {
        return condition.getShopId();
    }

    public void setCardInfoId(Long cardInfoId) {
        condition.setCardInfoId(cardInfoId);
    }

    public void setCardNum(String cardNum) {
        condition.setCardNum(cardNum);
    }

    public Long getCardInfoId() {
        return condition.getCardInfoId();
    }

    public String getMobile() {
        return condition.getMobile();
    }

    public void setMobile(String mobile) {
        condition.setMobile(mobile);
    }

    public String getCardNum() {
        return condition.getCardNum();
    }

    public String getCustomerName() {
        return condition.getCustomerName();
    }

    public void setShopId(Long shopId) {
        condition.setShopId(shopId);
    }

    public void setCustomerName(String customerName) {
        condition.setCustomerName(customerName);
    }
}
