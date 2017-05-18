package com.tqmall.legend.facade.discount.configuration.handler.init.converter;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Sets;
import com.tqmall.legend.entity.account.AccountCoupon;
import com.tqmall.legend.entity.account.CouponInfo;
import com.tqmall.legend.entity.account.CouponServiceRel;
import com.tqmall.legend.entity.account.CouponTypeEnum;
import com.tqmall.legend.enums.coupon.CouponInfoUseRangeEnum;
import com.tqmall.legend.facade.discount.bo.AccountCouponDiscountBo;
import com.tqmall.wheel.lang.Converter;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

import static com.tqmall.wheel.lang.Langs.isNotEmpty;
import static com.tqmall.wheel.lang.Langs.isNotNull;

/**
 * @Author 辉辉大侠
 * @Date:3:27 PM 02/03/2017
 */
@Data
public class AccountCouponDiscountConverter implements Converter<AccountCoupon, AccountCouponDiscountBo> {
    @Override
    public AccountCouponDiscountBo apply(AccountCoupon accountCoupon, AccountCouponDiscountBo accountCouponDiscount) {
        if (isNotNull(accountCoupon) && isNotNull(accountCouponDiscount)) {
            accountCouponDiscount.setCouponId(accountCoupon.getId());
            accountCouponDiscount.setCouponName(accountCoupon.getCouponName());
            accountCouponDiscount.setCouponType(CouponTypeEnum.code(accountCoupon.getCouponType()));
            accountCouponDiscount.setCouponSn(accountCoupon.getCouponCode());
            accountCouponDiscount.setCouponTypeId(accountCoupon.getCouponInfoId());
            accountCouponDiscount.setEffectiveDate(accountCoupon.getEffectiveDate());
            accountCouponDiscount.setExpireDate(accountCoupon.getExpireDate());
            /**
             * 初始化优惠券的最小金额限制
             */
            CouponInfo couponInfo = accountCoupon.getCouponInfo();
            accountCouponDiscount.setRuleDesc(couponInfo.getRuleStr());
            /**
             * 设置抵扣金额
             */
            accountCouponDiscount.setDiscount(couponInfo.getDiscountAmount());
            BigDecimal amountLimit = couponInfo.getAmountLimit();
            if (amountLimit.compareTo(BigDecimal.ZERO) > 0) {
                accountCouponDiscount.setAmountLimit(true);
                accountCouponDiscount.setMinAmountLimit(amountLimit);
                accountCouponDiscount.setRuleDesc(accountCouponDiscount.getRuleDesc() + ";满" + amountLimit +"元可使用");
            } else {
                accountCouponDiscount.setAmountLimit(false);
            }

            /**
             * 优惠券使用范围
             */
            if (accountCouponDiscount.getCouponType() == CouponTypeEnum.CASH_COUPON) {
                /**
                 * 若为现金券,则有使用范围限制
                 */
                accountCouponDiscount.setRange(CouponInfoUseRangeEnum.value(couponInfo.getUseRange()));

                if (accountCouponDiscount.getRange() == CouponInfoUseRangeEnum.ZXZDFWXMDZ) {
                    Set<Long> rangServiceIds = Sets.newHashSet();
                    if (isNotEmpty(couponInfo.getCouponServiceList())) {
                        rangServiceIds = FluentIterable.from(couponInfo.getCouponServiceList()).transform(new Function<CouponServiceRel, Long>() {
                            @Override
                            public Long apply(CouponServiceRel input) {
                                return input.getServiceId();
                            }
                        }).toSet();
                    }
                    accountCouponDiscount.setRangeServiceIds(rangServiceIds);
                }
            }
            /**
             * 初始化优惠券使用限制
             */
            accountCouponDiscount.setSingleUse(!Integer.valueOf(0).equals(couponInfo.getSingleUse()));
            accountCouponDiscount.setCompatibleWithCard(Integer.valueOf(1).equals(couponInfo.getCompatibleWithCard()));
        }
        return accountCouponDiscount;
    }
}
