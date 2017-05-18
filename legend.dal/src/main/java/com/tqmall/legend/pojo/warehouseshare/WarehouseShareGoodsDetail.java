package com.tqmall.legend.pojo.warehouseshare;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * Created by xin on 2016/11/14.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class WarehouseShareGoodsDetail {
    private Long id;
    private String goodsName;//商品名称
    private String goodsFormat;//型号
    private String carInfo;//适用车型，0为通用件，或者goods_car对象列表json字符串
    private BigDecimal saleNumber;//销售数量
    private String measureUnit;//最小单位
    private BigDecimal goodsPrice;//销售价格
    private String contactName;//联系人名称
    private String contactMobile;//手机号码
    private String provinceName;
    private String cityName;
    private String streetName;
    private Long shopId;// 对方门店id
    private String longitude;// 对方门店经度
    private String latitude; // 对方门店纬度
    private String selfLongitude;// 自己门店经度
    private String selfLatitude; // 自己门店纬度
}
