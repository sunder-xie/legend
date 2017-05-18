package com.tqmall.legend.facade.report.vo;


import com.tqmall.common.template.PagedSearch;

/**
 * Created by majian on 16/9/21.
 */
public class GoodsCatRankParam extends PagedSearch{
    private String month;
    private String tag;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
