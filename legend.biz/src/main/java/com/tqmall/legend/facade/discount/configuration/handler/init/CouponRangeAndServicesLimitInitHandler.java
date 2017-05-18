package com.tqmall.legend.facade.discount.configuration.handler.init;

import com.tqmall.legend.entity.account.CouponTypeEnum;
import com.tqmall.legend.enums.coupon.CouponInfoUseRangeEnum;
import com.tqmall.legend.facade.discount.bo.AccountCouponDiscountBo;
import com.tqmall.legend.facade.discount.bo.AccountDiscountBo;
import com.tqmall.legend.facade.discount.bo.DiscountContext;
import com.tqmall.legend.facade.discount.bo.DiscountServiceBo;
import com.tqmall.legend.facade.discount.configuration.handler.InitHandler;

import java.util.List;

import static com.tqmall.wheel.lang.Langs.*;

public class CouponRangeAndServicesLimitInitHandler implements InitHandler {

    @Override
    public void init(DiscountContext cxt) {
        /**
         * 本账户下的优惠券使用限制
         */
        if (isNotNull(cxt.getAccountDiscount())) {
            doLimit(cxt, cxt.getAccountDiscount().getCouponDiscountList());
        }
        /**
         * 绑定账户下的优惠券使用限制
         */
        if (isNotEmpty(cxt.getBindAccountDiscountList())) {
            for (AccountDiscountBo account : cxt.getBindAccountDiscountList()) {
                this.doLimit(cxt, account.getCouponDiscountList());
            }
        }
        /**
         * 他人账户下的优惠券使用限制
         */
        if (isNotNull(cxt.getGuestAccountDiscount())) {
            this.doLimit(cxt, cxt.getGuestAccountDiscount().getCouponDiscountList());
        }
    }

    private void doLimit(DiscountContext cxt, List<AccountCouponDiscountBo> couponList) {
        if (isNotEmpty(couponList)) {
            for (AccountCouponDiscountBo coupon : couponList) {
                /**
                 * 使用金额限制
                 */
                if (coupon.isAvailable() && coupon.isAmountLimit()
                        && coupon.getMinAmountLimit().compareTo(cxt.getOrderAmount()) > 0) {
                    coupon.setAvailable(false);
                    coupon.setMessage("优惠券不可用,工单金额[" + cxt.getOrderAmount() + "]小于优惠券最低使用金额[" + coupon.getMinAmountLimit() + "]");
                }
                /**
                 * 现金券有使用范围的限制
                 */
                if (coupon.isAvailable()
                        && coupon.getCouponType() == CouponTypeEnum.CASH_COUPON) {
                    if (coupon.getRange() == CouponInfoUseRangeEnum.ZXFUGS) {
                        if (isEmpty(cxt.getDiscountServiceList())) {
                            coupon.setAvailable(false);
                            coupon.setMessage("现金券限工单全部服务使用,但工单无服务,本券不可使用.");
                        }
                    } else if (coupon.getRange() == CouponInfoUseRangeEnum.ZXZDFWXMDZ) {
                        boolean containSpecifiedService = false;
                        for (DiscountServiceBo service : cxt.getDiscountServiceList()) {
                            if (coupon.getRangeServiceIds().contains(service.getServiceId())) {
                                containSpecifiedService = true;
                                break;
                            }
                        }
                        if (!containSpecifiedService) {
                            coupon.setAvailable(false);
                            coupon.setMessage("现金券只能用于特定服务,但工单不含特定服务,本券不可使用.");
                        }
                    }
                }
            }
        }
    }
}
