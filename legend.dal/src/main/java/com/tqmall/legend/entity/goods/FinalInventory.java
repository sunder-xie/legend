package com.tqmall.legend.entity.goods;




import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Data
public class FinalInventory extends BaseEntity {

    private Long shopId;
    private Long goodsId;
    private BigDecimal goodsCount;
    private BigDecimal goodsFinalPrice;
    private Date finalDate;
    private Long orderId;
    private String inventoryType;

}

