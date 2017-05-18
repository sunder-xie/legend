package com.tqmall.legend.web.account.converter;

import com.tqmall.legend.entity.account.MemberCardInfo;
import com.tqmall.legend.web.account.vo.CardInfoVo;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;

/**
 * Created by majian on 16/10/8.
 */
public class CardInfoConverter implements Converter<MemberCardInfo, CardInfoVo> {
    @Override
    public CardInfoVo convert(MemberCardInfo source) {
        CardInfoVo target = new CardInfoVo();
        BeanUtils.copyProperties(source,target);

        String s = getDiscountDescript(source);
        target.setDiscountDescript(s);

        return target;
    }

    private String getDiscountDescript(MemberCardInfo source) {
        return source.getDiscountDescription();
    }
}
