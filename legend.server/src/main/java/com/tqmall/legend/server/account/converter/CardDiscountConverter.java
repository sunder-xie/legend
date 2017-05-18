package com.tqmall.legend.server.account.converter;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tqmall.legend.facade.discount.bo.AccountCardDiscountBo;
import com.tqmall.legend.object.result.account.CardGoodsRelDTO;
import com.tqmall.legend.object.result.account.CardServiceRelDTO;
import com.tqmall.legend.object.result.account.DiscountCardDTO;
import com.tqmall.wheel.lang.Langs;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by xin on 2017/3/8.
 */
public class CardDiscountConverter {

    public static List<DiscountCardDTO> convertList(List<AccountCardDiscountBo> accountCardDiscountBoList) {
        if (Langs.isEmpty(accountCardDiscountBoList)) {
            return Collections.emptyList();
        }
        return Lists.transform(accountCardDiscountBoList, new Function<AccountCardDiscountBo, DiscountCardDTO>() {
            @Override
            public DiscountCardDTO apply(AccountCardDiscountBo accountCardDiscountBo) {
                return convert(accountCardDiscountBo);
            }
        });
    }

    public static DiscountCardDTO convert(AccountCardDiscountBo accountCardDiscountBo) {
        if (accountCardDiscountBo == null) {
            return null;
        }
        DiscountCardDTO discountCardDTO = new DiscountCardDTO();
        discountCardDTO.setCardId(accountCardDiscountBo.getCardId());
        discountCardDTO.setBalance(accountCardDiscountBo.getBalance());
        discountCardDTO.setSelected(accountCardDiscountBo.isSelected());
        discountCardDTO.setAvailable(accountCardDiscountBo.isAvailable());
        discountCardDTO.setCardTypeName(accountCardDiscountBo.getCardName());
        discountCardDTO.setDiscountType(accountCardDiscountBo.getCardDiscountType().getType());
        discountCardDTO.setDiscountDescription(accountCardDiscountBo.getDiscountDesc());
        discountCardDTO.setCardNumber(accountCardDiscountBo.getCardNumber());
        discountCardDTO.setDiscountAmount(accountCardDiscountBo.getDiscount());
        discountCardDTO.setAccountId(accountCardDiscountBo.getAccountId());
        discountCardDTO.setCustomerName(accountCardDiscountBo.getCustomerName());
        discountCardDTO.setMobile(accountCardDiscountBo.getMobile());
        switch (accountCardDiscountBo.getCardDiscountType()) {
            case NO:
                break;//无折扣
            case ALL:
                discountCardDTO.setDiscountRate(accountCardDiscountBo.getOrderDiscountRate());
                break;//全部工单折扣
            case SERVICE:
                _assemblyDiscountCardService(discountCardDTO, accountCardDiscountBo);
                break;
            case GOODS:
                _assemblyDiscountCardGoods(discountCardDTO, accountCardDiscountBo);
                break;
            case MULTI:
                _assemblyDiscountCardService(discountCardDTO, accountCardDiscountBo);
                _assemblyDiscountCardGoods(discountCardDTO, accountCardDiscountBo);
                break;
            default:
                return discountCardDTO;
        }
        return discountCardDTO;
    }

    private static void _assemblyDiscountCardService(DiscountCardDTO discountCardDTO, AccountCardDiscountBo accountCardDiscountBo) {
        discountCardDTO.setServiceDiscountType(accountCardDiscountBo.getCardServiceDiscountType().getType());
        if (discountCardDTO.getServiceDiscountType() == 1) { //全部服务折扣
            discountCardDTO.setServiceDiscountRate(accountCardDiscountBo.getServiceDiscountRate());
        } else if (discountCardDTO.getServiceDiscountType() == 2) {//部分服务折扣
            Map<Long, BigDecimal> serviceCatDiscountRateMap = accountCardDiscountBo.getServiceCatDiscountRateMap();
            List<CardServiceRelDTO> cardServiceRelDTOList = Lists.newArrayList();
            if (serviceCatDiscountRateMap != null) {
                for (Map.Entry<Long, BigDecimal> entry : serviceCatDiscountRateMap.entrySet()) {
                    CardServiceRelDTO cardServiceRelDTO = new CardServiceRelDTO();
                    cardServiceRelDTO.setServiceCatId(entry.getKey());
                    cardServiceRelDTO.setDiscount(entry.getValue());
                    cardServiceRelDTOList.add(cardServiceRelDTO);
                }
            }
            discountCardDTO.setCardServiceRels(cardServiceRelDTOList);
        }
    }

    private static void _assemblyDiscountCardGoods(DiscountCardDTO discountCardDTO, AccountCardDiscountBo accountCardDiscountBo) {
        discountCardDTO.setGoodsDiscountType(accountCardDiscountBo.getCardGoodsDiscountType().getType());
        if (discountCardDTO.getGoodsDiscountType() == 1) {//全部物料折扣
            discountCardDTO.setGoodsDiscountRate(accountCardDiscountBo.getGoodsDiscountRate());
        } else if (discountCardDTO.getGoodsDiscountType() == 2) { //部分物料折扣
            Map<Long, BigDecimal> goodsStdCatDiscountRateMap = accountCardDiscountBo.getGoodsStdCatDiscountRateMap();
            List<CardGoodsRelDTO> cardGoodsRelDTOList = Lists.newArrayList();
            if (goodsStdCatDiscountRateMap != null) {
                for (Map.Entry<Long, BigDecimal> entry : goodsStdCatDiscountRateMap.entrySet()) {
                    CardGoodsRelDTO cardGoodsRelDTO = new CardGoodsRelDTO();
                    cardGoodsRelDTO.setGoodsCatId(entry.getKey());
                    cardGoodsRelDTO.setDiscount(entry.getValue());
                    cardGoodsRelDTOList.add(cardGoodsRelDTO);
                }
            }
            discountCardDTO.setCardGoodsRels(cardGoodsRelDTOList);
        }
    }
}
