package com.tqmall.legend.entity.pub.order;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = false)
@Data
public class OrderGoodsVo {

    private BigDecimal goodsNumber;//物料数量
    private BigDecimal goodsPrice;//物料单价
    private String goodsName;//物料名称

}

