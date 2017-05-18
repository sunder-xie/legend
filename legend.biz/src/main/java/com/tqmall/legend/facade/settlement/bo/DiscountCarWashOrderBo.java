package com.tqmall.legend.facade.settlement.bo;

import com.tqmall.legend.facade.discount.bo.DiscountSelectedBo;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Created by zsy on 17/3/6.
 */
@Getter
@Setter
public class DiscountCarWashOrderBo {
    // 车牌
    private String carLicense;
    // 洗车服务ID
    private Long carWashServiceId;
    // 洗车金额
    private BigDecimal carWashAmount;
    // 已选择的优惠券
    private DiscountSelectedBo discountSelectedBo;
}
