package com.tqmall.legend.facade.settlement.bo;

import com.tqmall.legend.facade.discount.bo.DiscountSelectedBo;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by zsy on 17/3/6.
 */
@Getter
@Setter
public class DiscountOrderBo {
    private Long orderId;
    private DiscountSelectedBo discountSelectedBo;
}
