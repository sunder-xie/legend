package com.tqmall.legend.entity.warehouseshare;

/**
 * Created by tanghao on 16/11/15.
 */
public class WarehouseShareCountVO {
    private Integer onsaleNum = 0;//销售中
    private Integer waitForSaleNum = 0;//待销售
    private Integer hasSensitiveWordsNum = 0;//含有敏感词

    public Integer getAllSaleNum() {
        return allSaleNum;
    }

    public void setAllSaleNum(Integer allSaleNum) {
        this.allSaleNum = allSaleNum;
    }

    private Integer allSaleNum;

    public Integer getOnsaleNum() {
        return onsaleNum;
    }

    public void setOnsaleNum(Integer onsaleNum) {
        this.onsaleNum = onsaleNum;
    }

    public Integer getWaitForSaleNum() {
        return waitForSaleNum;
    }

    public void setWaitForSaleNum(Integer waitForSaleNum) {
        this.waitForSaleNum = waitForSaleNum;
    }

    public Integer getHasSensitiveWordsNum() {
        return hasSensitiveWordsNum;
    }

    public void setHasSensitiveWordsNum(Integer hasSensitiveWordsNum) {
        this.hasSensitiveWordsNum = hasSensitiveWordsNum;
    }
}
