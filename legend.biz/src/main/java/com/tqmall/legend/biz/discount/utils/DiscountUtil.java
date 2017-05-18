package com.tqmall.legend.biz.discount.utils;


import org.springframework.util.Assert;

import java.math.BigDecimal;

/**
 * Created by wanghui on 6/16/16.
 */
public final class DiscountUtil {
    /**
     * 获取折扣信息
     * @param discount
     * @return
     */
    public final static BigDecimal trimDiscount(BigDecimal discount) {
        Assert.notNull(discount, "折扣额度不能为空.");
        Assert.state(discount.compareTo(BigDecimal.ZERO) >= 0, "折扣金额不能小于0");
        Assert.state(discount.compareTo(BigDecimal.TEN) <= 0, "折扣金额不能大于10");
        return discount.divide(BigDecimal.TEN);
    }

    /**
     * 检查折扣数值是否正确,折扣必须位于0~10之间
     * @param discount
     * @return
     */
    public final static boolean checkDiscountValue(BigDecimal discount) {
        return discount != null
                && discount.compareTo(BigDecimal.ZERO) >= 0
                && discount.compareTo(BigDecimal.TEN) <= 0;
    }
}
