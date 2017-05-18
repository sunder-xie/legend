package com.tqmall.legend.entity.shop;

import com.tqmall.legend.entity.base.BaseEntity;
import com.tqmall.legend.entity.goods.Goods;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class ServiceGoodsSuiteVo extends BaseEntity {

    private String suitName;
    private Long serviceId;
    private String goodsInfo;
    private BigDecimal suitePrice;
    private Long shopId;
    private List<Goods> goodsList;

}

