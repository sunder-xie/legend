package com.tqmall.legend.facade.marketing.gather.param;

/**
 * Created by xin on 2016/12/16.
 */
public class FeedbackByPhoneParam {
    private Long customerCarId;
    private Long accountId;
    private Long noteInfoId;
    private Long couponInfoId;
    private String content;
    private String nextVisitTime;

    public Long getCustomerCarId() {
        return customerCarId;
    }

    public void setCustomerCarId(Long customerCarId) {
        this.customerCarId = customerCarId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getNoteInfoId() {
        return noteInfoId;
    }

    public void setNoteInfoId(Long noteInfoId) {
        this.noteInfoId = noteInfoId;
    }

    public Long getCouponInfoId() {
        return couponInfoId;
    }

    public void setCouponInfoId(Long couponInfoId) {
        this.couponInfoId = couponInfoId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNextVisitTime() {
        return nextVisitTime;
    }

    public void setNextVisitTime(String nextVisitTime) {
        this.nextVisitTime = nextVisitTime;
    }
}
