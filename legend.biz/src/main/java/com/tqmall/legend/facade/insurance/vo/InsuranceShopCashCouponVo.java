package com.tqmall.legend.facade.insurance.vo;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.insurance.domain.result.cashcoupon.CashCouponDetailDTO;
import lombok.Data;

/**
 * Created by zhouheng on 17/3/11.
 */
@Data
public class InsuranceShopCashCouponVo {

    /**
     * 有效的优惠券
     */
    private int cashCouponEffetive;
    /**
     * 已使用的优惠券
     */
    private int cashCouponConsumed;
    /**
     * 十天内过期
     */
    private int expiredTenDay;
    /**
     * 已过期
     */
    private int expired;
    /**
     * 今日可用
     */
    private int effectToday;
    /**
     * 累计获赠券额
     */
    private int totalCashCouponFee;


}
