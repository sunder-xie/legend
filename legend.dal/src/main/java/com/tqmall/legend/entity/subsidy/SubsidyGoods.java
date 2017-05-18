package com.tqmall.legend.entity.subsidy;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Data
public class SubsidyGoods extends BaseEntity {

    private Long actId;//活动id
    private Long subsidyActId;//补贴包id
    private Long catId;//商品类目id
    private String catName;//商品类目名称
    private String goodBrand;//商品品牌
    private Long goodId;//商品id
    private String goodName;//商品名称
    private BigDecimal subsidyAmount;//补贴金额
    private Integer leastUnit;//最小单位
    private Date orderStartTime;//订单开始时间
    private Date orderEndTime;//订单结束时间

}


