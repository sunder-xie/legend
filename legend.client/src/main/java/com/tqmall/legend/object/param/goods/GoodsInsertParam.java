package com.tqmall.legend.object.param.goods;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by zsy on 16/11/23.
 */
@Getter
@Setter
public class GoodsInsertParam implements Serializable {
    private static final long serialVersionUID = 8407387919243987976L;

    private Long shopId;        //门店id  必填
    private Long creator;       //用户id  必填
    private String goodsSn;     //商品编号 必填
    private String name;        //商品名称 必填
    private String measureUnit; //最小单位 必填
    private String format;      //型号    必填
    private Long catId;         //分类id  如果是自定义类别则必填
    private Long stdCatId;     //标准配件类别id 如果是标准类别则必填
    private String goodsCat;    //物料分类 必填
    private Long brandId;       //品牌id
    private String brandName;   //品牌
    private BigDecimal price;   //销售价格  必填
    private Long shortageNumber;//缺货临界数量 必填
    private String depot;       //仓位信息
    private String carInfo;     //适用车型，0为通用件，或者goods_car对象列表json字符串
}
