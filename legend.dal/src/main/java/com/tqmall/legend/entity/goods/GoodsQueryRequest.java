package com.tqmall.legend.entity.goods;

import com.tqmall.legend.entity.statistics.param.PageParam;

/**
 * Created by tanghao on 16/11/11.
 */
public class GoodsQueryRequest extends PageParam {
    private String goodsName;
    private String goodsFormat;
    private String carInfoLike;
    private Integer goodsStatus;
    private Long shopId;
    private String goodsCat;

    public String getGoodsCat() {
        return goodsCat;
    }

    public void setGoodsCat(String goodsCat) {
        this.goodsCat = goodsCat;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsFormat() {
        return goodsFormat;
    }

    public void setGoodsFormat(String goodsFormat) {
        this.goodsFormat = goodsFormat;
    }

    public String getCarInfoLike() {
        return carInfoLike;
    }

    public void setCarInfoLike(String carInfoLike) {
        this.carInfoLike = carInfoLike;
    }

    public Integer getGoodsStatus() {
        return goodsStatus;
    }

    public void setGoodsStatus(Integer goodsStatus) {
        this.goodsStatus = goodsStatus;
    }
}
