package com.tqmall.legend.web.account.vo;

/**
 * Created by majian on 17/1/5.
 */
public class ComboExportSummaryVO {
    private Integer comboNumberSummary;//有效计次卡总数
    private Integer serviceNumberSummary;//服务项目总数
    private Integer comboServiceRemainSummary;//服务项目剩余次数

    public Integer getComboNumberSummary() {
        return comboNumberSummary;
    }

    public void setComboNumberSummary(Integer comboNumberSummary) {
        this.comboNumberSummary = comboNumberSummary;
    }

    public Integer getServiceNumberSummary() {
        return serviceNumberSummary;
    }

    public void setServiceNumberSummary(Integer serviceNumberSummary) {
        this.serviceNumberSummary = serviceNumberSummary;
    }

    public Integer getComboServiceRemainSummary() {
        return comboServiceRemainSummary;
    }

    public void setComboServiceRemainSummary(Integer comboServiceRemainSummary) {
        this.comboServiceRemainSummary = comboServiceRemainSummary;
    }
}
