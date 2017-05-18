package com.tqmall.legend.facade.report.vo;


import com.tqmall.common.template.PagedSearch;

/**
 * Created by majian on 16/9/21.
 */
public class GoodsRankParam extends PagedSearch{
    private String month;//月份
    private String keyword;//关键字

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
