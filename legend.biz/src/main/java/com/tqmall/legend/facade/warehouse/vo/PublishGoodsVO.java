package com.tqmall.legend.facade.warehouse.vo;

/**
 * 发布库存商品返回对象
 * Created by tanghao on 16/11/12.
 */
public class PublishGoodsVO {
    private Integer publishNum;//发布成功数
    private Integer failNum;//发布失败数

    public Integer getPublishNum() {
        return publishNum;
    }

    public void setPublishNum(Integer publishNum) {
        this.publishNum = publishNum;
    }

    public Integer getFailNum() {
        return failNum;
    }

    public void setFailNum(Integer failNum) {
        this.failNum = failNum;
    }
}
