package com.tqmall.legend.server.account.converter;

import com.tqmall.legend.entity.account.AccountTradeFlow;
import com.tqmall.legend.object.result.account.MemberCardFlowDTO;
import com.tqmall.wheel.lang.Langs;
import org.springframework.core.convert.converter.Converter;

import java.math.BigDecimal;

/**
 * Created by majian on 16/9/7.
 */
public class CardFlowConverter implements Converter<AccountTradeFlow,MemberCardFlowDTO> {
    @Override
    public MemberCardFlowDTO convert(AccountTradeFlow source) {
        MemberCardFlowDTO destination = new MemberCardFlowDTO();
        destination.setOrderSn(source.getOrderSn());
        destination.setPayment(source.getPaymentName());

        if (Langs.isNotNull(source.getAmount())) {
            if(BigDecimal.ZERO.compareTo(source.getAmount()) > 0) {
                destination.setConsumeAmount(source.getAmount());
            } else {
                destination.setRechargeAmount(source.getAmount());
            }
        }

        destination.setConsumeType(source.getConsumeType());
        destination.setGmtCreate(source.getGmtCreate());
        return destination;
    }
}
