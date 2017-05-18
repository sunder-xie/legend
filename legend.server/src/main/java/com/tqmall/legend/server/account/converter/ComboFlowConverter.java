package com.tqmall.legend.server.account.converter;

import com.tqmall.legend.entity.account.AccountTradeFlow;
import com.tqmall.legend.object.result.account.ComboFlowDTO;
import org.springframework.core.convert.converter.Converter;

/**
 * Created by majian on 16/9/7.
 */
public class ComboFlowConverter implements Converter<AccountTradeFlow,ComboFlowDTO> {
    @Override
    public ComboFlowDTO convert(AccountTradeFlow source) {
        ComboFlowDTO destination = new ComboFlowDTO();
        destination.setConsumeContent(source.getServiceExplain());
        destination.setConsumeTime(source.getGmtCreate());
        destination.setConsumeType(source.getConsumeType());
        return destination;
    }
}
