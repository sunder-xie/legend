package com.tqmall.legend.web.account.converter;

import com.google.common.collect.Lists;
import com.tqmall.legend.entity.account.MemberCardInfo;
import com.tqmall.legend.web.account.vo.CardInfoVo;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

/**
 * Created by majian on 16/10/8.
 */
public class CardInfoListConverter implements Converter<List<MemberCardInfo>, List<CardInfoVo>> {
    @Override
    public List<CardInfoVo> convert(List<MemberCardInfo> source) {
        List<CardInfoVo> target = Lists.newArrayList();
        if (source == null) {
            return target;
        }
        for (MemberCardInfo cardInfo : source) {
            CardInfoVo cardInfoVo = new CardInfoConverter().convert(cardInfo);
            target.add(cardInfoVo);
        }
        return target;
    }
}
