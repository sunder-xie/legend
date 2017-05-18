package com.tqmall.legend.web.account.param;

/**
 * Created by majian on 17/1/5.
 */
public class MemberExportConditions {
    private Long cardInfoId;
    private String cardNum;
    private String mobile;
    private String customerName;

    public Long getCardInfoId() {
        return cardInfoId;
    }

    public void setCardInfoId(Long cardInfoId) {
        this.cardInfoId = cardInfoId;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
