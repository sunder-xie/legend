package com.tqmall.legend.facade.discount.configuration.handler.calc;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.tqmall.common.exception.BizException;
import com.tqmall.legend.entity.account.CouponTypeEnum;
import com.tqmall.legend.enums.coupon.CouponInfoUseRangeEnum;
import com.tqmall.legend.facade.discount.bo.AccountCouponDiscountBo;
import com.tqmall.legend.facade.discount.bo.DiscountContext;
import com.tqmall.legend.facade.discount.bo.DiscountServiceBo;
import com.tqmall.legend.facade.discount.configuration.handler.CalcHandler;
import com.tqmall.wheel.lang.Casts;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.*;

import static com.tqmall.wheel.lang.Langs.isNotEmpty;

/**
 * @Author 辉辉大侠
 * @Date:4:35 PM 03/03/2017
 */
@Slf4j
public class CouponCalcHandler implements CalcHandler {
    @Override
    public void doDiscount(DiscountContext cxt) {

        if (isNotEmpty(cxt.getSelected().getSelectedCouponList())) {
            List<AccountCouponDiscountBo> selectCouponList = new ArrayList(FluentIterable.from(cxt.getAllCouponList()).filter(new Predicate<AccountCouponDiscountBo>() {
                @Override
                public boolean apply(AccountCouponDiscountBo input) {
                    return input.isSelected();
                }
            }).toList());
            /**
             * 按优惠券的特殊性进行排序
             */
            this.sort(selectCouponList);

            for (AccountCouponDiscountBo coupon : selectCouponList) {
                if (coupon.getCouponType() == CouponTypeEnum.CASH_COUPON) {
                    if (coupon.getRange() == CouponInfoUseRangeEnum.ZXZDFWXMDZ) {
                        /**
                         * 限指定服务抵扣
                         */
                        final Set<Long> rangeServiceIds = coupon.getRangeServiceIds();
                        ImmutableList<DiscountServiceBo> discountServiceBos = FluentIterable.from(cxt.getDiscountServiceList())
                                .filter(new Predicate<DiscountServiceBo>() {
                                    @Override
                                    public boolean apply(DiscountServiceBo input) {
                                        return rangeServiceIds.contains(input.getServiceId());
                                    }
                                }).toList();
                        BigDecimal discountAmount = coupon.getDiscount();
                        for (DiscountServiceBo discountServiceBo : discountServiceBos) {
                            if (discountAmount.equals(BigDecimal.ZERO)) {
                                break;
                            }
                            if (discountServiceBo.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                                if (discountServiceBo.getAmount().compareTo(discountAmount) >= 0) {
                                    discountServiceBo.setAmount(discountServiceBo.getAmount().subtract(discountAmount));
                                    discountAmount = BigDecimal.ZERO;
                                    break;
                                } else {
                                    discountAmount = discountAmount.subtract(discountServiceBo.getAmount());
                                    discountServiceBo.setAmount(BigDecimal.ZERO);
                                }
                            }
                        }
                        coupon.setFinalDiscount(coupon.getDiscount().subtract(discountAmount));
                        cxt.setDiscountAmount(cxt.getDiscountAmount().add(coupon.getFinalDiscount()));
                    } else if (coupon.getRange() == CouponInfoUseRangeEnum.ZXFUGS) {
                        /**
                         * 只限服务抵扣
                         */
                        BigDecimal discountAmount = coupon.getDiscount();
                        for (DiscountServiceBo discountServiceBo : cxt.getDiscountServiceList()) {
                            if (discountServiceBo.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                                if (discountAmount.compareTo(BigDecimal.ZERO) == 0) {
                                    break;
                                }
                                if (discountServiceBo.getAmount().compareTo(discountAmount) > 0) {
                                    discountServiceBo.setAmount(discountServiceBo.getAmount().subtract(discountAmount));
                                    discountAmount = BigDecimal.ZERO;
                                    break;
                                } else {
                                    discountAmount = discountAmount.subtract(discountServiceBo.getAmount());
                                    discountServiceBo.setAmount(BigDecimal.ZERO);
                                }
                            }
                        }
                        coupon.setFinalDiscount(coupon.getDiscount().subtract(discountAmount));
                        cxt.setDiscountAmount(cxt.getDiscountAmount().add(coupon.getFinalDiscount()));
                    } else if (coupon.getRange() == CouponInfoUseRangeEnum.QCTY) {
                        /**
                         * 全场通用
                         */
                        BigDecimal remainAmount = cxt.getOrderAmount().subtract(cxt.getDiscountAmount());
                        if (coupon.getDiscount().compareTo(remainAmount) >= 0) {
                            coupon.setFinalDiscount(remainAmount);
                            cxt.setDiscountAmount(cxt.getDiscountAmount().add(remainAmount));
                        } else {
                            coupon.setFinalDiscount(coupon.getDiscount());
                            cxt.setDiscountAmount(cxt.getDiscountAmount().add(coupon.getDiscount()));
                        }
                    }
                } else if (coupon.getCouponType() == CouponTypeEnum.UNIVERSAL_COUPON) {
                    BigDecimal remainAmount = cxt.getOrderAmount().subtract(cxt.getDiscountAmount());
                    if (coupon.getFinalDiscount().compareTo(remainAmount) > 0) {
                        if (log.isErrorEnabled()) {
                            log.error("通用券[{}]的设置金额[{}]不能大于工单折扣剩余的金额:[{}]", coupon.getCouponId(), coupon.getFinalDiscount(), remainAmount);
                        }
                        throw new BizException("通用券输入金额不能大于工单折扣后的金额");
                    } else {
                        cxt.setDiscountAmount(cxt.getDiscountAmount().add(coupon.getFinalDiscount()));
                    }
                } else {
                    throw new BizException("未知类型的优惠券.");
                }
            }
        }
    }

    protected void sort(List<AccountCouponDiscountBo> couponList) {
        Collections.sort(couponList, new Comparator<AccountCouponDiscountBo>() {
            @Override
            public int compare(AccountCouponDiscountBo o1, AccountCouponDiscountBo o2) {
                if (o1.getCouponType() == o2.getCouponType()) {
                    if (o1.getCouponType() == CouponTypeEnum.UNIVERSAL_COUPON) {
                        return Casts.castInteger(o1.getCouponId() - o2.getCouponId());
                    } else {
                        if (o1.getRange() != o2.getRange()) {
                            return -o1.getRange().getValue() + o2.getRange().getValue();
                        } else {
                            return Casts.castInteger(o1.getCouponId() - o2.getCouponId());
                        }
                    }
                } else {
                    return o1.getCouponType().getCode() - o2.getCouponType().getCode();
                }
            }
        });
    }
}
