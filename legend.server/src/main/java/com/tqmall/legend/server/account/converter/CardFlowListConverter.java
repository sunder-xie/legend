package com.tqmall.legend.server.account.converter;

import com.google.common.collect.Lists;
import com.tqmall.legend.entity.account.AccountTradeFlow;
import com.tqmall.legend.object.result.account.MemberCardFlowDTO;
import com.tqmall.legend.object.result.account.MemberCardInfoDTO;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

/**
 * Created by majian on 16/9/7.
 */
public class CardFlowListConverter implements Converter<List<AccountTradeFlow>,List<MemberCardFlowDTO>> {
    @Override
    public List<MemberCardFlowDTO> convert(List<AccountTradeFlow> source) {
        List<MemberCardFlowDTO> destination = Lists.newArrayList();
        if (source == null) {
            return destination;
        }
        for (AccountTradeFlow flow : source) {
            MemberCardFlowDTO dto = new CardFlowConverter().convert(flow);
            destination.add(dto);
        }
        return destination;
    }
}
