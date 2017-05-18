package com.tqmall.legend.server.account.converter;

import com.google.common.collect.Lists;
import com.tqmall.legend.entity.account.MemberCardInfo;
import com.tqmall.legend.object.result.account.MemberCardInfoDTO;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

/**
 * Created by majian on 16/9/7.
 */
public class CardInfoListConverter implements Converter<List<MemberCardInfo>,List<MemberCardInfoDTO>> {
    @Override
    public List<MemberCardInfoDTO> convert(List<MemberCardInfo> source) {
        List<MemberCardInfoDTO> destination = Lists.newArrayList();
        if (source == null) {
            return destination;
        }
        for (MemberCardInfo cardInfo : source) {
            MemberCardInfoDTO dto = new CardInfoConverter().convert(cardInfo);
            destination.add(dto);
        }
        return destination;
    }
}
