package com.tqmall.legend.facade.discount.configuration.handler.init.converter;

import com.google.common.collect.Maps;
import com.tqmall.legend.entity.account.CardGoodsRel;
import com.tqmall.legend.entity.account.CardServiceRel;
import com.tqmall.legend.entity.account.MemberCard;
import com.tqmall.legend.entity.account.MemberCardInfo;
import com.tqmall.legend.enums.account.CardDiscountTypeEnum;
import com.tqmall.legend.enums.account.CardGoodsDiscountTypeEnum;
import com.tqmall.legend.enums.account.CardServiceDiscountTypeEnum;
import com.tqmall.legend.facade.discount.bo.AccountCardDiscountBo;
import com.tqmall.wheel.lang.Converter;

import java.math.BigDecimal;
import java.util.Map;

import static com.tqmall.wheel.lang.Langs.isNotEmpty;
import static com.tqmall.wheel.lang.Langs.isNotNull;

/**
 * @Author 辉辉大侠
 * @Date:2:59 PM 02/03/2017
 */
public class AccountCardDiscountConverter implements Converter<MemberCard, AccountCardDiscountBo> {
    @Override
    public AccountCardDiscountBo apply(MemberCard memberCard, AccountCardDiscountBo accountCardDiscount) {
        if (isNotNull(memberCard) && isNotNull(accountCardDiscount)) {
            accountCardDiscount.setCardId(memberCard.getId());
            accountCardDiscount.setCardNumber(memberCard.getCardNumber());
            accountCardDiscount.setCardTypeId(memberCard.getCardTypeId());
            accountCardDiscount.setExpireDate(memberCard.getExpireDate());

            MemberCardInfo cardInfo = memberCard.getMemberCardInfo();
            accountCardDiscount.setCardDiscountType(CardDiscountTypeEnum.type(cardInfo.getDiscountType()));
            accountCardDiscount.setBalance(memberCard.getBalance());
            accountCardDiscount.setCardName(cardInfo.getTypeName());
            if (accountCardDiscount.getCardDiscountType() == CardDiscountTypeEnum.ALL) {
                /**
                 * 整单折扣
                 */
                accountCardDiscount.setOrderDiscountRate(cardInfo.getOrderDiscount());
            } else if (accountCardDiscount.getCardDiscountType() == CardDiscountTypeEnum.SERVICE) {
                /**
                 * 服务类型折扣
                 */
                accountCardDiscount.setCardServiceDiscountType(CardServiceDiscountTypeEnum.type(cardInfo.getServiceDiscountType()));
                this.assemblyServiceDiscount(accountCardDiscount, cardInfo);
            } else if (accountCardDiscount.getCardDiscountType() == CardDiscountTypeEnum.GOODS) {
                /**
                 * 配件折扣
                 */
                accountCardDiscount.setCardGoodsDiscountType(CardGoodsDiscountTypeEnum.type(cardInfo.getGoodDiscountType()));
                this.assemblyGoodsDiscount(accountCardDiscount, cardInfo);
            } else if (accountCardDiscount.getCardDiscountType() == CardDiscountTypeEnum.MULTI) {

                /**
                 * 服务和配件同时折扣
                 */
                accountCardDiscount.setCardServiceDiscountType(CardServiceDiscountTypeEnum.type(cardInfo.getServiceDiscountType()));
                this.assemblyServiceDiscount(accountCardDiscount, cardInfo);

                accountCardDiscount.setCardGoodsDiscountType(CardGoodsDiscountTypeEnum.type(cardInfo.getGoodDiscountType()));
                this.assemblyGoodsDiscount(accountCardDiscount, cardInfo);
            }
            accountCardDiscount.setDiscountDesc(cardInfo.getDiscountDescription());
        }
        return accountCardDiscount;
    }

    private void assemblyGoodsDiscount(AccountCardDiscountBo accountCardDiscount, MemberCardInfo cardInfo) {
        if (accountCardDiscount.getCardGoodsDiscountType() == CardGoodsDiscountTypeEnum.ALL) {
            accountCardDiscount.setGoodsDiscountRate(cardInfo.getGoodDiscount());
        } else if (accountCardDiscount.getCardGoodsDiscountType() == CardGoodsDiscountTypeEnum.PART) {
            Map<Long, BigDecimal> goodsStdCatDiscountRateMap = Maps.newHashMap();
            Map<Long, BigDecimal> goodsCustomCatDiscountRateMap = Maps.newHashMap();

            if (isNotEmpty(cardInfo.getCardGoodsRels())) {
                for (CardGoodsRel rel : cardInfo.getCardGoodsRels()) {
                    if (rel.getGoodsCatSource() == 1) {
                        //标准分类
                        goodsStdCatDiscountRateMap.put(rel.getGoodsCatId(), rel.getDiscount());
                    } else {
                        //自定义分类
                        goodsCustomCatDiscountRateMap.put(rel.getGoodsCatId(), rel.getDiscount());
                    }
                }
            }
            accountCardDiscount.setGoodsStdCatDiscountRateMap(goodsStdCatDiscountRateMap);
            accountCardDiscount.setGoodsCustomCatDiscountRateMap(goodsCustomCatDiscountRateMap);
        }
    }

    private void assemblyServiceDiscount(AccountCardDiscountBo accountCardDiscount, MemberCardInfo cardInfo) {
        if (accountCardDiscount.getCardServiceDiscountType() == CardServiceDiscountTypeEnum.ALL) {
            accountCardDiscount.setServiceDiscountRate(cardInfo.getServiceDiscount());
        } else if (accountCardDiscount.getCardServiceDiscountType() == CardServiceDiscountTypeEnum.PART) {
            Map<Long, BigDecimal> serviceCatDiscountRateMap = Maps.newHashMap();
            if (isNotEmpty(cardInfo.getCardServiceRels())) {
                for (CardServiceRel rel : cardInfo.getCardServiceRels()) {
                    serviceCatDiscountRateMap.put(rel.getServiceCatId(), rel.getDiscount());
                }
            }
            accountCardDiscount.setServiceCatDiscountRateMap(serviceCatDiscountRateMap);
        }
    }
}