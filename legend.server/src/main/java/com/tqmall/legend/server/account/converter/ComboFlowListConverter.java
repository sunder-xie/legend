package com.tqmall.legend.server.account.converter;

import com.google.common.collect.Lists;
import com.tqmall.legend.entity.account.AccountTradeFlow;
import com.tqmall.legend.object.result.account.ComboFlowDTO;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

/**
 * Created by majian on 16/9/7.
 */
public class ComboFlowListConverter implements Converter<List<AccountTradeFlow>,List<ComboFlowDTO>> {
    @Override
    public List<ComboFlowDTO> convert(List<AccountTradeFlow> source) {
        List<ComboFlowDTO> destination = Lists.newArrayList();
        if (source == null) {
            return destination;
        }
        for (AccountTradeFlow flow : source) {
            ComboFlowDTO dto = new ComboFlowConverter().convert(flow);
            destination.add(dto);
        }
        return destination;
    }
}
