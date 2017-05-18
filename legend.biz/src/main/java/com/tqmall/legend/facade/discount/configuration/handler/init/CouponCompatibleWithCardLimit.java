package com.tqmall.legend.facade.discount.configuration.handler.init;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.tqmall.common.exception.BizException;
import com.tqmall.legend.facade.discount.bo.AccountCouponDiscountBo;
import com.tqmall.legend.facade.discount.bo.DiscountContext;
import com.tqmall.legend.facade.discount.configuration.handler.InitHandler;

import java.util.List;

import static com.tqmall.wheel.lang.Langs.isNotEmpty;
import static com.tqmall.wheel.lang.Langs.isNotNull;

/**
 * @Author 辉辉大侠
 * @Date:11:00 AM 06/03/2017
 */
public class CouponCompatibleWithCardLimit implements InitHandler {

    @Override
    public void init(DiscountContext cxt) {
        List<AccountCouponDiscountBo> couponList = cxt.getAllCouponList();
        if (isNotEmpty(couponList) && isNotNull(cxt.getSelected().getSelectedCard())) {
            /**
             * 与会员卡不能共同使用的优惠券列表
             */
            ImmutableList<AccountCouponDiscountBo> notCompatibleWithCardCouponList = FluentIterable.from(couponList)
                    .filter(new Predicate<AccountCouponDiscountBo>() {
                        @Override
                        public boolean apply(AccountCouponDiscountBo input) {
                            return !input.isCompatibleWithCard() && input.isAvailable();
                        }
                    }).toList();
            if (isNotEmpty(notCompatibleWithCardCouponList)) {
                for (AccountCouponDiscountBo coupon : notCompatibleWithCardCouponList) {
                    if (coupon.isSelected()) {
                        throw new BizException("会员卡已选中,选中的优惠券不能与会员卡共同使用.");
                    }
                    coupon.setAvailable(false);
                    coupon.setMessage("优惠券不能与会员卡共同使用,会员卡已选中,此优惠券不能使用.");
                }
            }
        }
    }
}
