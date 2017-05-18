package com.tqmall.legend.facade.discount.bo;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.tqmall.legend.entity.account.CouponTypeEnum;
import com.tqmall.wheel.utils.DateFormatUtils;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.tqmall.wheel.lang.Langs.isNotEmpty;
import static com.tqmall.wheel.lang.Langs.isNotNull;

/**
 * @Author 辉辉大侠
 * @Date:9:59 AM 02/03/2017
 */
@Data
public class DiscountInfoBo {
    /**
     * 车辆id
     */
    private Long customerCarId;
    /**
     * 车牌信息
     */
    private String carLicense;
    /**
     * 店铺id
     */
    private Long shopId;
    /**
     * 拥有账户折扣信息
     */
    private AccountDiscountBo accountDiscount;
    /**
     * 使用他人账户折扣信息
     */
    private AccountDiscountBo guestAccountDiscount;
    /**
     * 绑定账户折扣信息
     */
    private List<AccountDiscountBo> bindAccountDiscountList;
    /**
     * 工单总的优惠金额
     */
    private BigDecimal discountedAmount;

    /**
     * 个人和关联的会员卡
     */
    public List<AccountCardDiscountBo> getSortedCardList() {
        List<AccountCardDiscountBo> cardList = Lists.newArrayList();
        if (isNotNull(accountDiscount)) {
            if (isNotEmpty(accountDiscount.getCardDiscountList())) {
                cardList.addAll(accountDiscount.getCardDiscountList());
            }
        }
        if (isNotEmpty(bindAccountDiscountList)) {
            for (AccountDiscountBo account : bindAccountDiscountList) {
                if (isNotEmpty(account.getCardDiscountList())) {
                    cardList.addAll(account.getCardDiscountList());
                }
            }
        }
        sortCardList(cardList);
        return cardList;
    }

    /**
     * 会员卡排序：使用他人账户
     *
     * @return
     */
    public List<AccountCardDiscountBo> getSortedGuestCardList() {
        List<AccountCardDiscountBo> cardList = Lists.newArrayList();
        if (isNotNull(guestAccountDiscount)) {
            if (isNotEmpty(guestAccountDiscount.getCardDiscountList())) {
                cardList.addAll(guestAccountDiscount.getCardDiscountList());
            }
        }
        sortCardList(cardList);
        return cardList;
    }

    /**
     * 现金券排序
     *
     * @return
     */
    public List<AccountCouponDiscountBo> getSortedCashCouponList() {
        List<AccountCouponDiscountBo> couponList = getCouponListByType(CouponTypeEnum.CASH_COUPON);
        return couponList;
    }

    /**
     * 现金券排序：使用他人账户
     *
     * @return
     */
    public List<AccountCouponDiscountBo> getSortedGuestCashCouponList() {
        List<AccountCouponDiscountBo> couponList = getGuestCouponListByType(CouponTypeEnum.CASH_COUPON);
        return couponList;
    }

    protected List<AccountCouponDiscountBo> getCouponListByType(final CouponTypeEnum couponType) {
        List<AccountCouponDiscountBo> couponList = Lists.newArrayList();
        if (isNotNull(accountDiscount)) {
            couponList.addAll(filterCoupon(accountDiscount.getCouponDiscountList(), couponType));
        }
        if (isNotEmpty(bindAccountDiscountList)) {
            for (AccountDiscountBo account : bindAccountDiscountList) {
                couponList.addAll(filterCoupon(account.getCouponDiscountList(), couponType));
            }
        }
        return sortCouponList(couponList);
    }

    protected List<AccountCouponDiscountBo> getGuestCouponListByType(final CouponTypeEnum couponType) {
        List<AccountCouponDiscountBo> couponList = Lists.newArrayList();
        if (isNotNull(guestAccountDiscount)) {
            couponList.addAll(filterCoupon(guestAccountDiscount.getCouponDiscountList(), couponType));
        }
        return sortCouponList(couponList);
    }

    private List<AccountCouponDiscountBo> filterCoupon(List<AccountCouponDiscountBo> couponList, final CouponTypeEnum couponType) {
        if (isNotEmpty(couponList)) {
            return FluentIterable.from(couponList)
                    .filter(new Predicate<AccountCouponDiscountBo>() {
                        @Override
                        public boolean apply(AccountCouponDiscountBo input) {
                            return input.getCouponType() == couponType;
                        }
                    }).toList();
        } else {
            return Lists.newArrayList();
        }

    }

    /**
     * 通用券排序
     *
     * @return
     */
    public List<AccountCouponDiscountBo> getSortedUniversalCouponList() {
        List<AccountCouponDiscountBo> couponList = getCouponListByType(CouponTypeEnum.UNIVERSAL_COUPON);
        return couponList;
    }

    /**
     * 通用券排序：使用他人账户
     *
     * @return
     */
    public List<AccountCouponDiscountBo> getSortedGuestUniversalCouponList() {
        List<AccountCouponDiscountBo> couponList = getGuestCouponListByType(CouponTypeEnum.UNIVERSAL_COUPON);
        return couponList;
    }

    /**
     * 个人的和绑定的计次卡
     */
    public List<AccountComboDiscountBo> getSortedComboList() {
        List<AccountComboDiscountBo> comboList = Lists.newArrayList();
        if (isNotNull(accountDiscount)) {
            if (isNotEmpty(accountDiscount.getComboDiscountList())) {
                comboList.addAll(accountDiscount.getComboDiscountList());
            }
        }
        if (isNotEmpty(bindAccountDiscountList)) {
            for (AccountDiscountBo account : bindAccountDiscountList) {
                if (isNotEmpty(account.getComboDiscountList())) {
                    comboList.addAll(account.getComboDiscountList());
                }
            }
        }
        return sortComboList(comboList);
    }

    /**
     * 他人的计次卡
     */
    public List<AccountComboDiscountBo> getSortedGuestComboList() {
        List<AccountComboDiscountBo> comboList = Lists.newArrayList();
        if (isNotNull(guestAccountDiscount)) {
            if (isNotEmpty(guestAccountDiscount.getComboDiscountList())) {
                comboList.addAll(guestAccountDiscount.getComboDiscountList());
            }
        }
        return sortComboList(comboList);
    }

    /**
     * 得到所有优惠券
     */
    public List<AccountCouponDiscountBo> getSortedAllCouponList() {
        List<AccountCouponDiscountBo> couponList = Lists.newArrayList();
        if (isNotNull(accountDiscount)) {
            couponList.addAll(accountDiscount.getCouponDiscountList());
        }
        if (isNotEmpty(bindAccountDiscountList)) {
            for (AccountDiscountBo account : bindAccountDiscountList) {
                couponList.addAll(account.getCouponDiscountList());
            }
        }
        if (isNotNull(guestAccountDiscount)) {
            couponList.addAll(guestAccountDiscount.getCouponDiscountList());
        }
        return sortCouponList(couponList);
    }

    /**
     * 个人和关联的优惠券
     */
    public List<AccountCouponDiscountBo> getSortedCouponList() {
        List<AccountCouponDiscountBo> couponList = Lists.newArrayList();
        if (isNotNull(accountDiscount)) {
            couponList.addAll(accountDiscount.getCouponDiscountList());
        }
        if (isNotEmpty(bindAccountDiscountList)) {
            for (AccountDiscountBo account : bindAccountDiscountList) {
                couponList.addAll(account.getCouponDiscountList());
            }
        }
        return sortCouponList(couponList);
    }

    /**
     * 他人的优惠券
     */
    public List<AccountCouponDiscountBo> getSortedGuestCouponList() {
        List<AccountCouponDiscountBo> couponList = Lists.newArrayList();
        if (isNotNull(guestAccountDiscount)) {
            couponList.addAll(guestAccountDiscount.getCouponDiscountList());
        }
        return sortCouponList(couponList);
    }

    /**
     * 优惠券排序
     *
     * @param couponList
     * @return
     */
    private List<AccountCouponDiscountBo> sortCouponList(List<AccountCouponDiscountBo> couponList) {
        if (isNotEmpty(couponList)) {
            Collections.sort(couponList, new Comparator<AccountCouponDiscountBo>() {
                @Override
                public int compare(AccountCouponDiscountBo o1, AccountCouponDiscountBo o2) {
                    /**
                     * 一张可用,一张不可用
                     */
                    if (o1.isAvailable() != o2.isAvailable()) {
                        /**
                         * 可用的券排在前面
                         */
                        if (o1.isAvailable()) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }

                    /**
                     * 二张均可用或均不可用
                     */
                    if (DateFormatUtils.toYMD(o1.getExpireDate()).equals(DateFormatUtils.toYMD(o2.getExpireDate()))) {
                        /**
                         * 过期时间相等的情况下,优惠金额大的显示在前面
                         */
                        if (o1.getDiscount() != null && o2.getDiscount() != null) {
                            return o2.getDiscount().compareTo(o1.getDiscount());
                        } else {
                            return 0;
                        }
                    } else {
                        /**
                         * 过期时间不等的情况下,过期时间近的排在前面
                         */
                        return o1.getExpireDate().compareTo(o2.getExpireDate());
                    }

                }
            });
        }
        return couponList;
    }

    /**
     * 会员卡排序
     *
     * @param cardList
     */
    private List<AccountCardDiscountBo> sortCardList(List<AccountCardDiscountBo> cardList) {
        if (isNotEmpty(cardList)) {
            Collections.sort(cardList, new Comparator<AccountCardDiscountBo>() {
                @Override
                public int compare(AccountCardDiscountBo o1, AccountCardDiscountBo o2) {
                    if (o1.isAvailable() != o2.isAvailable()) {
                        if (o1.isAvailable()) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                    //系统默认取该车牌对应账户下有效会员卡的本单优惠金额最大者，如果优惠金额相等，次选会员卡余额较大者，再次取会员卡过期时间最近，最次随机选择；
                    if ((o1.isAvailable() && o2.isAvailable()) && (o1.getFinalDiscount().compareTo(o2.getFinalDiscount()) != 0)) {
                        return o2.getFinalDiscount().compareTo(o1.getFinalDiscount());
                    } else {
                        if (o1.getBalance().compareTo(o2.getBalance()) != 0) {
                            return o2.getBalance().compareTo(o1.getBalance());
                        } else {
                            if (!DateFormatUtils.toYMD(o1.getExpireDate()).equals(DateFormatUtils.toYMD(o2.getExpireDate()))){
                                return o1.getExpireDate().compareTo(o2.getExpireDate());
                            }
                        }
                    }
                    return o1.getCardId().compareTo(o2.getCardId());
                }
            });
        }
        return cardList;
    }

    /**
     * 计次卡排序
     *
     * @param comboList
     * @return
     */
    private List<AccountComboDiscountBo> sortComboList(List<AccountComboDiscountBo> comboList) {
        if (isNotEmpty(comboList)) {
            Collections.sort(comboList, new Comparator<AccountComboDiscountBo>() {
                @Override
                public int compare(AccountComboDiscountBo o1, AccountComboDiscountBo o2) {
                    if (o1.isAvailable() != o2.isAvailable()) {
                        if (o1.isAvailable()) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                    if (!DateFormatUtils.toYMD(o1.getExpireDate()).equals(DateFormatUtils.toYMD(o2.getExpireDate()))){
                        return o1.getExpireDate().compareTo(o2.getExpireDate());
                    }
                    return o1.getComboId().intValue() - o2.getComboId().intValue();
                }
            });
        }
        return comboList;
    }
}
