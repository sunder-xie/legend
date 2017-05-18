package com.tqmall.legend.facade.discount.configuration.handler.init;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.tqmall.common.exception.BizException;
import com.tqmall.legend.facade.discount.bo.AccountCardDiscountBo;
import com.tqmall.legend.facade.discount.bo.AccountCouponDiscountBo;
import com.tqmall.legend.facade.discount.bo.DiscountContext;
import com.tqmall.legend.facade.discount.configuration.handler.InitHandler;
import com.tqmall.wheel.lang.Objects;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.tqmall.wheel.lang.Langs.isNotEmpty;
import static com.tqmall.wheel.lang.Langs.isNotNull;

/**
 * @Author 辉辉大侠
 * @Date:10:30 PM 05/03/2017
 */
@Slf4j
public class CardCompatibleWithCouponLimit implements InitHandler {

    @Override
    public void init(DiscountContext cxt) {
        List<AccountCouponDiscountBo> couponList = cxt.getAllCouponList();
        if (isNotEmpty(couponList)) {
            /**
             * 被选中并且不能与会员卡一同使用的优惠券
             */
            ImmutableList<AccountCouponDiscountBo> selectedNotCompatibleWithCardCouponList = FluentIterable.from(couponList).filter(new Predicate<AccountCouponDiscountBo>() {
                @Override
                public boolean apply(AccountCouponDiscountBo input) {
                    return !input.isCompatibleWithCard();
                }
            }).filter(new Predicate<AccountCouponDiscountBo>() {
                @Override
                public boolean apply(AccountCouponDiscountBo input) {
                    return input.isSelected();
                }
            }).toList();

            if (isNotEmpty(selectedNotCompatibleWithCardCouponList)) {
                if (isNotNull(cxt.getSelected().getSelectedCard())) {
                    log.error("优惠券不能与会员卡共同使用,选中的优惠券:{}", Objects.toJSON(selectedNotCompatibleWithCardCouponList));
                    throw new BizException("选中的优惠券不能与会员卡共同使用,因为会员卡已选中.");
                } else {
                    List<AccountCardDiscountBo> cardList = cxt.getAllCardList();
                    if (isNotEmpty(cardList)) {
                        for (AccountCardDiscountBo card : cardList) {
                            if (card.isAvailable()) {
                                card.setAvailable(false);
                                card.setMessage("选中的优惠券不能与会员卡共同使用,会员卡不可用.");
                            }
                        }
                    }
                }
            }
        }
    }
}
