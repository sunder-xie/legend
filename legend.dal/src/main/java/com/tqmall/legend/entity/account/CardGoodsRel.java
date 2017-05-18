package com.tqmall.legend.entity.account;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = false)
@Data
public class CardGoodsRel extends BaseEntity {

    private Long shopId;//门店id
    private Integer goodsCatSource;//1:标准分类2:自定义分类
    private Long goodsCatId;//配件id
    private String goodsCatName;//配件名
    private BigDecimal discount;//配件折扣
    private Long cardInfoId;//会员卡类型关联id

}