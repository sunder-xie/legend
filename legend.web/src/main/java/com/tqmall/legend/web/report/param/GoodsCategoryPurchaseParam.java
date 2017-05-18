package com.tqmall.legend.web.report.param;

/**
 * Created by majian on 16/11/8.
 */
public class GoodsCategoryPurchaseParam {
    private String month;
    private int page;
    private int size;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
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
