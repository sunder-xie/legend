package com.tqmall.legend.server.account.converter;

import com.tqmall.legend.entity.account.MemberCard;
import com.tqmall.legend.object.result.account.MemberCardDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;

/**
 * Created by majian on 16/9/7.
 */
public class CardConverter implements Converter<MemberCard,MemberCardDTO> {
    @Override
    public MemberCardDTO convert(MemberCard source) {
        MemberCardDTO destination = new MemberCardDTO();
        BeanUtils.copyProperties(source,destination);
        return destination;
    }
}
