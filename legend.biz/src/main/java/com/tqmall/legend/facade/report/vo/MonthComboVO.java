package com.tqmall.legend.facade.report.vo;

/**
 * Created by tanghao on 16/9/22.
 */
public class MonthComboVO {
    private String comboName;//计次卡名
    private Integer handOutNum;//发放数量
    private String reciveAmount;//收款金额

    public String getComboName() {
        return comboName;
    }

    public void setComboName(String comboName) {
        this.comboName = comboName;
    }

    public Integer getHandOutNum() {
        return handOutNum;
    }

    public void setHandOutNum(Integer handOutNum) {
        this.handOutNum = handOutNum;
    }

    public String getReciveAmount() {
        return reciveAmount;
    }

    public void setReciveAmount(String reciveAmount) {
        this.reciveAmount = reciveAmount;
    }
}
