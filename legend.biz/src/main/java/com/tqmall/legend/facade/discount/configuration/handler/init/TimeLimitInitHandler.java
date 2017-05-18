package com.tqmall.legend.facade.discount.configuration.handler.init;

import com.tqmall.legend.facade.discount.bo.*;
import com.tqmall.legend.facade.discount.configuration.handler.InitHandler;
import com.tqmall.wheel.lang.Dates;
import com.tqmall.wheel.utils.DateFormatUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.tqmall.wheel.lang.Langs.isNotEmpty;
import static com.tqmall.wheel.lang.Langs.isNotNull;

@Slf4j
public class TimeLimitInitHandler implements InitHandler {

    @Override
    public void init(DiscountContext cxt) {
        /**
         * 处理拥有账户
         */
        if (isNotNull(cxt.getAccountDiscount())) {
            this.doCardTimeLimit(cxt.getAccountDiscount().getCardDiscountList());
            this.doComoTimeLimit(cxt.getAccountDiscount().getComboDiscountList());
            this.doCouponTimeLimit(cxt.getAccountDiscount().getCouponDiscountList());
        }
        /**
         * 处理绑定账户
         */
        if (isNotEmpty(cxt.getBindAccountDiscountList())) {
            for (AccountDiscountBo bindAccount : cxt.getBindAccountDiscountList()) {
                this.doCardTimeLimit(bindAccount.getCardDiscountList());
                this.doComoTimeLimit(bindAccount.getComboDiscountList());
                this.doCouponTimeLimit(bindAccount.getCouponDiscountList());
            }
        }
        /**
         * 处理他人账户
         */
        if (isNotNull(cxt.getGuestAccountDiscount())) {
            this.doCardTimeLimit(cxt.getGuestAccountDiscount().getCardDiscountList());
            this.doComoTimeLimit(cxt.getGuestAccountDiscount().getComboDiscountList());
            this.doCouponTimeLimit(cxt.getGuestAccountDiscount().getCouponDiscountList());
        }
    }

    private void doCouponTimeLimit(List<AccountCouponDiscountBo> couponList) {
        if (isNotEmpty(couponList)) {
            for (AccountCouponDiscountBo coupon : couponList) {
                if (coupon.getEffectiveDate().compareTo(Dates.now()) >= 0) {
                    coupon.setAvailable(false);
                    coupon.setMessage("优惠券未生效,生效时间为:" + DateFormatUtils.toYMD(coupon.getEffectiveDate()));
                } else if (coupon.getExpireDate().compareTo(Dates.now()) <= 0) {
                    coupon.setAvailable(false);
                    coupon.setMessage("优惠券已过期,过期时间为:" + DateFormatUtils.toYMD(coupon.getExpireDate()));
                }
            }
        }
    }

    private void doComoTimeLimit(List<AccountComboDiscountBo> comboList) {
        if (isNotEmpty(comboList)) {
            for (AccountComboDiscountBo combo : comboList) {
                if (combo.getEffectiveDate().compareTo(Dates.now()) >= 0) {
                    combo.setAvailable(false);
                    combo.setMessage("计次卡未生效,生效时间为:" + DateFormatUtils.toYMD(combo.getEffectiveDate()));
                } else if (combo.getExpireDate().compareTo(Dates.now()) <= 0) {
                    combo.setAvailable(false);
                    combo.setMessage("计次卡已过期,过期时间为:" + DateFormatUtils.toYMD(combo.getExpireDate()));
                }
            }
        }
    }

    private void doCardTimeLimit(List<AccountCardDiscountBo> cardList) {
        if (isNotEmpty(cardList)) {
            for (AccountCardDiscountBo card : cardList) {
                if (card.getExpireDate().compareTo(Dates.now()) <= 0) {
                    card.setAvailable(false);
                    card.setMessage("会员卡已过期,过期时间为:" + DateFormatUtils.toYMD(card.getExpireDate()));
                }
            }
        }
    }
}
