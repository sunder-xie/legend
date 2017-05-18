package com.tqmall.legend.web.account.converter;

import com.google.common.collect.Lists;
import com.tqmall.legend.entity.account.CardGoodsRel;
import com.tqmall.legend.entity.account.MemberCardInfo;
import com.tqmall.legend.web.account.vo.CardInfoEditVo;
import com.tqmall.legend.web.account.vo.GoodsCatParam;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

/**
 * Created by majian on 16/10/8.
 */
public class CardInfoEditVoConverter implements Converter<MemberCardInfo, CardInfoEditVo> {
    @Override
    public CardInfoEditVo convert(MemberCardInfo source) {
        CardInfoEditVo taget = new CardInfoEditVo();
        BeanUtils.copyProperties(source,taget);
        List<GoodsCatParam> goodsCatParamList = convertGoodsCatList(source);
        taget.setCardGoodsRels(goodsCatParamList);
        taget.setCardServiceRels(source.getCardServiceRels());
        taget.setCompatibleWithCoupon(source.getCompatibleWithCoupon().equals(1));
        taget.setGeneralUse(source.getGeneralUse().equals(1));
        return taget;
    }

    private List<GoodsCatParam> convertGoodsCatList(MemberCardInfo source) {
        List<GoodsCatParam> goodsCatParamList = Lists.newArrayList();
        if (source.getCardGoodsRels() == null) {
            return goodsCatParamList;
        }
        for (CardGoodsRel cardGoodsRel : source.getCardGoodsRels()) {
            GoodsCatParam goodsCatParam = new GoodsCatParam();
            goodsCatParam.setCatId(cardGoodsRel.getGoodsCatId());
            goodsCatParam.setCatName(cardGoodsRel.getGoodsCatName());
            goodsCatParam.setDiscount(cardGoodsRel.getDiscount());
            goodsCatParam.setCustomCat(cardGoodsRel.getGoodsCatSource().equals(2));
            goodsCatParamList.add(goodsCatParam);
        }
        return goodsCatParamList;
    }
}
