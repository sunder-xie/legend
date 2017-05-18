package com.tqmall.legend.web.report.export.vo;

import com.tqmall.wheel.component.excel.annotation.Excel;
import com.tqmall.wheel.component.excel.annotation.ExcelCol;

import java.math.BigDecimal;

/**
 * Created by majian on 16/12/23.
 */
@Excel
public class SaleStarCommission {
    @ExcelCol(value = 0, title = "员工")
    private String userName;
    @ExcelCol(value = 1, title = "排名时间")
    private String weekRange;
    @ExcelCol(value = 3, title = "销售之星奖励")
    private BigDecimal rewardAmount;
    private Integer rank;
    @ExcelCol(value = 2, title = "销售排名")
    public String getRankStr() {
        if (rank.equals(0)) {
            return "-";
        }
        return "第"+rank+"名";
    }


    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getWeekRange() {
        return weekRange;
    }

    public void setWeekRange(String weekRange) {
        this.weekRange = weekRange;
    }

    public BigDecimal getRewardAmount() {
        return rewardAmount;
    }

    public void setRewardAmount(BigDecimal rewardAmount) {
        this.rewardAmount = rewardAmount;
    }
}
