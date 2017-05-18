package com.tqmall.legend.facade.discount.configuration.handler.init;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tqmall.legend.facade.discount.bo.AccountCouponDiscountBo;
import com.tqmall.legend.facade.discount.bo.DiscountContext;
import com.tqmall.legend.facade.discount.bo.SelectedCouponBo;
import com.tqmall.legend.facade.discount.configuration.handler.InitHandler;

import java.util.List;

import static com.tqmall.wheel.lang.Langs.isNotEmpty;

/**
 * @Author 辉辉大侠
 * @Date:2:01 PM 06/03/2017
 */
public class CouponSelectedInitHandler implements InitHandler {

    @Override
    public void init(DiscountContext cxt) {
        if (isNotEmpty(cxt.getSelected().getSelectedCouponList())) {
            /**
             * 将所有已选中的优惠券id置入Map中
             */
            ImmutableMap<Long, SelectedCouponBo> selectedCoupons = Maps.uniqueIndex(cxt.getSelected().getSelectedCouponList(), new Function<SelectedCouponBo, Long>() {
                @Override
                public Long apply(SelectedCouponBo input) {
                    return input.getCouponId();
                }
            });

            List<AccountCouponDiscountBo> couponList = cxt.getAllCouponList();
            if (isNotEmpty(couponList)) {
                for (AccountCouponDiscountBo coupon : couponList) {
                    if (selectedCoupons.containsKey(coupon.getCouponId())) {
                        coupon.setSelected(true);
                        coupon.setFinalDiscount(selectedCoupons.get(coupon.getCouponId()).getCouponDiscountAmount());

                    }
                }

            }
        }
    }
}
