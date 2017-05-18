package com.tqmall.legend.facade.discount.configuration.handler.init;

import com.google.common.collect.Lists;
import com.tqmall.legend.biz.account.AccountComboService;
import com.tqmall.legend.biz.account.AccountCouponService;
import com.tqmall.legend.biz.account.MemberCardService;
import com.tqmall.legend.entity.account.AccountCombo;
import com.tqmall.legend.entity.account.AccountComboServiceRel;
import com.tqmall.legend.entity.account.AccountCoupon;
import com.tqmall.legend.entity.account.MemberCard;
import com.tqmall.legend.facade.discount.bo.AccountCardDiscountBo;
import com.tqmall.legend.facade.discount.bo.AccountComboDiscountBo;
import com.tqmall.legend.facade.discount.bo.AccountCouponDiscountBo;
import com.tqmall.legend.facade.discount.bo.AccountDiscountBo;
import com.tqmall.legend.facade.discount.configuration.handler.InitHandler;
import com.tqmall.legend.facade.discount.configuration.handler.init.converter.AccountCardDiscountConverter;
import com.tqmall.legend.facade.discount.configuration.handler.init.converter.AccountCouponDiscountConverter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.tqmall.wheel.lang.Langs.isNotEmpty;
import static com.tqmall.wheel.lang.Langs.isNotNull;

/**
 * @Author 辉辉大侠
 * @Date:3:37 PM 02/03/2017
 */

public abstract class AbstractAccountInfoInitHandler implements InitHandler {
    @Autowired
    private MemberCardService memberCardService;
    @Autowired
    private AccountComboService accountComboService;
    @Autowired
    private AccountCouponService accountCouponService;

    /**
     * 初始化账户下关联的会员卡信息
     *
     * @param accountDiscount
     */
    protected void initAccountCardsInfo(Long shopId, AccountDiscountBo accountDiscount) {
        if (isNotNull(accountDiscount)) {
            List<AccountCardDiscountBo> cardDiscountList = Lists.newArrayList();
            List<MemberCard> memberCardList = this.memberCardService.getUnExpiredMemberCardListByAccountId(shopId, accountDiscount.getAccountId());
            if (isNotEmpty(memberCardList)) {
                for (MemberCard memberCard : memberCardList) {
                    AccountCardDiscountBo cardDiscount = new AccountCardDiscountConverter().apply(memberCard, new AccountCardDiscountBo());
                    cardDiscount.setCustomerName(accountDiscount.getCustomerName());
                    cardDiscount.setMobile(accountDiscount.getCustomerMobile());
                    cardDiscount.setAccountId(accountDiscount.getAccountId());
                    cardDiscountList.add(cardDiscount);
                }
            }
            accountDiscount.setCardDiscountList(cardDiscountList);
        }
    }

    /**
     * 初始化账户下关联的计次卡信息
     */
    protected void initAccountComboInfo(Long shopId, AccountDiscountBo accountDiscount) {
        if (isNotNull(accountDiscount)) {
            List<AccountComboDiscountBo> comboDiscountList = Lists.newArrayList();
            List<AccountCombo> comboList = this.accountComboService.listAvailableCombo(shopId, accountDiscount.getAccountId());
            if (isNotEmpty(comboList)) {
                for (AccountCombo combo : comboList) {
                    if (isNotEmpty(combo.getServiceList())) {
                        for (AccountComboServiceRel item : combo.getServiceList()) {
                            Integer count = item.getTotalServiceCount() - item.getUsedServiceCount();
                            if (count > 0) {
                                AccountComboDiscountBo comboDiscount = new AccountComboDiscountBo();
                                comboDiscount.setComboId(combo.getId());
                                comboDiscount.setComboName(combo.getComboName());
                                comboDiscount.setComboTypeId(combo.getComboInfoId());
                                comboDiscount.setComboServiceId(item.getId());
                                comboDiscount.setServiceId(item.getServiceId());
                                comboDiscount.setServiceName(item.getServiceName());
                                comboDiscount.setCount(count);
                                comboDiscount.setEffectiveDate(combo.getEffectiveDate());
                                comboDiscount.setExpireDate(combo.getExpireDate());
                                comboDiscount.setCustomerName(accountDiscount.getCustomerName());
                                comboDiscount.setMobile(accountDiscount.getCustomerMobile());
                                comboDiscount.setAccountId(accountDiscount.getAccountId());
                                comboDiscountList.add(comboDiscount);
                            }
                        }
                    }
                }
            }
            accountDiscount.setComboDiscountList(comboDiscountList);
        }
    }

    /**
     * 初始化账户下关联的优惠券信息
     */
    protected void initAccountCouponInfo(Long shopId, AccountDiscountBo accountDiscount) {
        if (isNotNull(accountDiscount)) {
            List<AccountCouponDiscountBo> couponDiscountList = Lists.newArrayList();
            List<AccountCoupon> couponList = this.accountCouponService.findAvailableAccountCoupon(shopId, accountDiscount.getAccountId());
            if (isNotEmpty(couponList)) {
                for (AccountCoupon coupon : couponList) {
                    AccountCouponDiscountBo couponDiscount = new AccountCouponDiscountConverter().apply(coupon, new AccountCouponDiscountBo());
                    couponDiscount.setCustomerName(accountDiscount.getCustomerName());
                    couponDiscount.setMobile(accountDiscount.getCustomerMobile());
                    couponDiscount.setAccountId(accountDiscount.getAccountId());
                    couponDiscountList.add(couponDiscount);
                }
            }
            accountDiscount.setCouponDiscountList(couponDiscountList);
        }
    }

}
