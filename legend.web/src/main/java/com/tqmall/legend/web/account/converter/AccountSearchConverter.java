package com.tqmall.legend.web.account.converter;

import com.google.common.collect.Lists;
import com.tqmall.legend.web.account.vo.AccountSearchVo;
import com.tqmall.search.dubbo.client.legend.account.result.LegendAccountDTO;
import com.tqmall.search.dubbo.client.legend.account.result.LegendMemberCardDTO;
import com.tqmall.wheel.lang.Langs;
import com.tqmall.wheel.utils.DateFormatUtils;

import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by xin on 2017/3/6.
 */
public class AccountSearchConverter {
    public static AccountSearchVo convert(LegendAccountDTO legendAccountDTO) {
        if (legendAccountDTO == null) {
            return null;
        }
        AccountSearchVo accountSearchVo = new AccountSearchVo();
        accountSearchVo.setAccountId(legendAccountDTO.getAccountId());
        accountSearchVo.setCustomerId(legendAccountDTO.getCustomerId());
        accountSearchVo.setCustomerName(legendAccountDTO.getCustomerName());
        accountSearchVo.setMobile(legendAccountDTO.getMobile());
        accountSearchVo.setShopId(legendAccountDTO.getShopId());
        accountSearchVo.setMemberCars(legendAccountDTO.getMemberCars());
        accountSearchVo.setMemberCards(legendAccountDTO.getMemberCards());
        return accountSearchVo;
    }

    public static List<AccountSearchVo> convertList(List<LegendAccountDTO> legendAccountDTOList) {
        if (Langs.isEmpty(legendAccountDTOList)) {
            return Collections.emptyList();
        }
        List<AccountSearchVo> accountSearchVoList = Lists.newArrayList();
        for (LegendAccountDTO legendAccountDTO : legendAccountDTOList) {
            AccountSearchVo accountSearchVo = convert(legendAccountDTO);
            if (accountSearchVo != null) {
                accountSearchVoList.add(accountSearchVo);
            }
        }
        return accountSearchVoList;
    }
}
