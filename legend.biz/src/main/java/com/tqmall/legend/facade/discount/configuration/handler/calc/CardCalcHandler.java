package com.tqmall.legend.facade.discount.configuration.handler.calc;

import com.tqmall.common.util.NumUtil;
import com.tqmall.legend.biz.discount.utils.DiscountUtil;
import com.tqmall.legend.enums.account.CardDiscountTypeEnum;
import com.tqmall.legend.enums.account.CardGoodsDiscountTypeEnum;
import com.tqmall.legend.enums.account.CardServiceDiscountTypeEnum;
import com.tqmall.legend.facade.discount.bo.AccountCardDiscountBo;
import com.tqmall.legend.facade.discount.bo.DiscountContext;
import com.tqmall.legend.facade.discount.bo.DiscountGoodsBo;
import com.tqmall.legend.facade.discount.bo.DiscountServiceBo;
import com.tqmall.legend.facade.discount.configuration.handler.CalcHandler;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.tqmall.wheel.lang.Langs.*;

@Slf4j
public class CardCalcHandler implements CalcHandler {
    @Override
    public void doDiscount(DiscountContext cxt) {
        List<AccountCardDiscountBo> cardList = cxt.getAllCardList();
        cardList = new ArrayList<>(cardList);
        Collections.sort(cardList, new Comparator<AccountCardDiscountBo>() {
            @Override
            public int compare(AccountCardDiscountBo o1, AccountCardDiscountBo o2) {
                if (o1.isSelected()) {
                    return 1;
                }
                if (o2.isSelected()) {
                    return -1;
                }
                return o1.getCardId().intValue() - o2.getCardId().intValue();
            }
        });
        if (isNotEmpty(cardList)) {
            for (AccountCardDiscountBo card : cardList) {
                if (card.isAvailable()) {
                    /**
                     *对于选中的会员卡进行会员优惠计算
                     */
                    if (card.getCardDiscountType() == CardDiscountTypeEnum.NO) {
                        //会员卡无折扣的情况
                        card.setDiscount(BigDecimal.ZERO);
                    } else if (card.getCardDiscountType() == CardDiscountTypeEnum.ALL) {
                        /**
                         * 整单折扣
                         */
                        /**
                         * 对每一项服务进行折扣计算
                         */
                        BigDecimal serviceDiscountAmount = this.discountServiceList(cxt.getDiscountServiceList(), card.getOrderDiscountRate(), card.isSelected());
                        /**
                         * 对每一项配件进行折扣计算
                         */
                        BigDecimal goodsDiscountAmount = this.discountGoodsList(cxt.getDiscountGoodsList(), card.getOrderDiscountRate(), card.isSelected());
                        /**
                         * 服务+配件折扣金额总和为工单的优惠金额
                         */
                        card.setDiscount(serviceDiscountAmount.add(goodsDiscountAmount));
                    } else if (card.getCardDiscountType() == CardDiscountTypeEnum.SERVICE) {
                        card.setDiscount(doDiscountService(cxt, card, card.isSelected()));
                    } else if (card.getCardDiscountType() == CardDiscountTypeEnum.GOODS) {
                        card.setDiscount(doDiscountGoods(cxt, card, card.isSelected()));
                    } else if (card.getCardDiscountType() == CardDiscountTypeEnum.MULTI) {
                        /**
                         * 服务和配件同时折扣
                         */
                        BigDecimal serviceDiscountAmount = doDiscountService(cxt, card, card.isSelected());
                        BigDecimal goodsDiscountAmount = doDiscountGoods(cxt, card, card.isSelected());
                        card.setDiscount(serviceDiscountAmount.add(goodsDiscountAmount));
                    }

                    card.setFinalDiscount(card.getDiscount());
                    if (card.isSelected()) {
                        /**
                         * 会员卡被选中的情况下设置会员卡的最终优惠金额
                         */
                        if (isNotNull(cxt.getSelected().getSelectedCard().getCardDiscountAmount())) {
                            card.setFinalDiscount(cxt.getSelected().getSelectedCard().getCardDiscountAmount());
                        }
                        if (isNotNull(card.getFinalDiscount())) {
                            cxt.setDiscountAmount(cxt.getDiscountAmount().add(card.getFinalDiscount()));
                        }
                    }
                }
            }
        }

    }

    private BigDecimal doDiscountGoods(DiscountContext cxt, AccountCardDiscountBo card, boolean selected) {
        BigDecimal discountAmount = BigDecimal.ZERO;
        /**
         * 配件折扣
         */
        if (card.getCardGoodsDiscountType() == CardGoodsDiscountTypeEnum.ALL) {
            /**
             * 全部配件折扣
             * 全部配件的折扣金额即为会员卡的折扣金额
             */
            discountAmount = this.discountGoodsList(cxt.getDiscountGoodsList(), card.getGoodsDiscountRate(), selected);
        } else if (card.getCardGoodsDiscountType() == CardGoodsDiscountTypeEnum.PART) {
            if (isNotEmpty(cxt.getDiscountGoodsList())) {
                for (DiscountGoodsBo goods : cxt.getDiscountGoodsList()) {
                    if (isNotNull(goods.getGoodsStdCatId()) && !Long.valueOf(0).equals(goods.getGoodsStdCatId())) {
                        /**
                         * 标准分类
                         */
                        BigDecimal rate = card.getGoodsStdCatDiscountRateMap().get(goods.getGoodsStdCatId());
                        if (isNotNull(rate)) {
                            discountAmount = discountAmount.add(discountGoods(goods, rate, selected));
                        }
                    } else {
                        /**
                         * 自定义分类
                         */
                        BigDecimal rate = card.getGoodsCustomCatDiscountRateMap().get(goods.getGoodsCustomCatId());
                        if (isNotNull(rate)) {
                            discountAmount = discountAmount.add(discountGoods(goods, rate, selected));
                        }
                    }
                }
            }
        }
        return discountAmount;
    }

    private BigDecimal doDiscountService(DiscountContext cxt, AccountCardDiscountBo card, boolean selected) {
        BigDecimal discountAmount = BigDecimal.ZERO;
        /**
         * 服务折扣
         */
        if (card.getCardServiceDiscountType() == CardServiceDiscountTypeEnum.ALL) {
            /**
             * 全部服务享受折扣
             * 全部服务的折扣金额即为会员卡的折扣金额
             */
            discountAmount = this.discountServiceList(cxt.getDiscountServiceList(), card.getServiceDiscountRate(), selected);
        } else if (card.getCardServiceDiscountType() == CardServiceDiscountTypeEnum.PART) {

            if (isNotEmpty(cxt.getDiscountServiceList())) {
                for (DiscountServiceBo service : cxt.getDiscountServiceList()) {
                    BigDecimal rate = card.getServiceCatDiscountRateMap().get(service.getServiceCatId());
                    if (isNotNull(rate)) {
                        discountAmount = discountAmount.add(discountService(service, rate, selected));
                    }
                }
            }
        }
        return discountAmount;
    }

    private BigDecimal discountServiceList(List<DiscountServiceBo> serviceList, BigDecimal discount, boolean selected) {
        BigDecimal discountAmount = BigDecimal.ZERO;
        if (isNotEmpty(serviceList)) {
            for (DiscountServiceBo service : serviceList) {
                discountAmount = discountAmount.add(this.discountService(service, discount, selected));
            }
        }
        return discountAmount;
    }

    private BigDecimal discountService(DiscountServiceBo service, BigDecimal discount, boolean selected) {
        BigDecimal discountAmount = BigDecimal.ZERO;
        if (isNotNull(service)) {
            BigDecimal discountedAmount = service.getAmount().multiply(DiscountUtil.trimDiscount(discount));
            discountedAmount = NumUtil.roundedBigdecimal(discountedAmount);
            discountAmount = service.getAmount().subtract(discountedAmount);
            if (selected) service.setAmount(discountedAmount);
        }
        return discountAmount;
    }

    private BigDecimal discountGoodsList(List<DiscountGoodsBo> goodsList, BigDecimal discount, boolean selected) {
        BigDecimal discountAmount = BigDecimal.ZERO;
        if (isNotEmpty(goodsList)) {
            for (DiscountGoodsBo goods : goodsList) {
                discountAmount = discountAmount.add(this.discountGoods(goods, discount, selected));
            }
        }
        return discountAmount;
    }

    private BigDecimal discountGoods(DiscountGoodsBo goods, BigDecimal discount, boolean selected) {
        BigDecimal discountAmount = BigDecimal.ZERO;
        if (isNotNull(goods)) {
            BigDecimal discountedAmount = goods.getAmount().multiply(DiscountUtil.trimDiscount(discount));
            discountedAmount = NumUtil.roundedBigdecimal(discountedAmount);
            discountAmount = goods.getAmount().subtract(discountedAmount);
            if (selected) goods.setAmount(discountedAmount);
        }
        return discountAmount;
    }
}
