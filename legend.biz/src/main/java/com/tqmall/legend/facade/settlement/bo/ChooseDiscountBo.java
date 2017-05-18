package com.tqmall.legend.facade.settlement.bo;

import com.tqmall.legend.facade.discount.bo.DiscountSelectedBo;
import lombok.Data;

/**
 * Created by zsy on 16/6/13.
 */
@Data
public class ChooseDiscountBo {
    private Long orderId;
    private DiscountSelectedBo selectedItem;
}
