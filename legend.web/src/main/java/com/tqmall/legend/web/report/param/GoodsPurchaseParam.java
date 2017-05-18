package com.tqmall.legend.web.report.param;

/**
 * Created by majian on 16/11/8.
 */
public class GoodsPurchaseParam {
    private String month;
    private String keyword;
    private int page;
    private int size;

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

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
