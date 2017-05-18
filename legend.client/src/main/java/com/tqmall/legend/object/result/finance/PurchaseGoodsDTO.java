package com.tqmall.legend.object.result.finance;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by twg on 16/1/25.
 * 提供给ERP获取采购计划清单信息
 */
@Data
public class PurchaseGoodsDTO implements Serializable {
    private Long id;//
    private String isDeleted;
    private Date gmtCreate;
    private Long creator;
    private Date gmtModified;
    private Long modifier;
    private Integer cityId = Integer.valueOf("0");//   城市id
    private Long shopId = Long.valueOf("0");//   门店id
    private Long purchasePlanId = Long.valueOf("0");//   关联门店采购计划表id
    private Long actId = Long.valueOf("0");//   商品活动id
    private Long groupId = Long.valueOf("0");//   商品组合id
    private Long goodsId = Long.valueOf("0");//   商品id
    private String goodsSn = String.valueOf("");//   商品编码
    private String goodsName = String.valueOf("");//   商品名称
    private String goodsFormat;//   商品规格型号
    private String minMeasureUnit;//  商品单位 最小计量单位
    private BigDecimal goodsPrice = BigDecimal.ZERO;//   商品标价
    private Long goodsNumber = Long.valueOf("1");//   商品数量
    private String goodsImg;//   商品图片地址
    private String carModel;//   适配车型

}
