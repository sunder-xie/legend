package com.tqmall.legend.web.account.converter;

import com.google.common.collect.Lists;
import com.tqmall.legend.biz.account.bo.CardCreateBo;
import com.tqmall.legend.biz.account.bo.GoodsCatBo;
import com.tqmall.legend.web.account.vo.CardCreateParam;
import com.tqmall.legend.web.account.vo.GoodsCatParam;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

/**
 * Created by majian on 16/9/28.
 */
public class CardCreateConverter implements Converter<CardCreateParam, CardCreateBo> {
    @Override
    public CardCreateBo convert(CardCreateParam source) {
        CardCreateBo destination = new CardCreateBo();
        BeanUtils.copyProperties(source,destination);
        destination.setServiceCatRels(source.getServiceRels());
        if (source.getGoodsCats() != null) {
            List<GoodsCatBo> goodsCatBos = Lists.newArrayList();
            for (GoodsCatParam catParam : source.getGoodsCats()) {
                GoodsCatBo goodsCatBo = new GoodsCatBo();
                goodsCatBo.setCatId(catParam.getCatId());
                goodsCatBo.setCatName(catParam.getCatName());
                goodsCatBo.setDiscount(catParam.getDiscount());
                goodsCatBo.setCatSource(catParam.isCustomCat() ? 2 : 1);
                goodsCatBos.add(goodsCatBo);

            }
            destination.setGoodsCats(goodsCatBos);
        }
        return destination;
    }
}
