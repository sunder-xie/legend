package com.tqmall.legend.web.account.converter;

import com.google.common.collect.Lists;
import com.tqmall.legend.biz.account.vo.MemberCardVo;
import com.tqmall.legend.entity.account.MemberCard;
import com.tqmall.wheel.lang.Langs;

import java.util.Collections;
import java.util.List;

/**
 * Created by xin on 2017/3/6.
 */
public class MemberCardConverter {

    public static MemberCardVo convertVo(MemberCard memberCard) {
        if (memberCard == null) {
            return null;
        }
        MemberCardVo memberCardVo = new MemberCardVo();
        memberCardVo.setAccountId(memberCard.getAccountId());
        memberCardVo.setMemberCardId(memberCard.getId());
        memberCardVo.setCardTypeId(memberCard.getCardTypeId());
        memberCardVo.setCardTypeName(memberCard.getCardTypeName());
        memberCardVo.setCardNum(memberCard.getCardNumber());
        memberCardVo.setDepositAmount(memberCard.getDepositAmount());
        memberCardVo.setBalance(memberCard.getBalance());
        memberCardVo.setGrantTime(memberCard.getGmtCreate());
        memberCardVo.setExpireDate(memberCard.getExpireDate());
        return memberCardVo;
    }

    public static List<MemberCardVo> convertVoList(List<MemberCard> memberCardList) {
        if (Langs.isEmpty(memberCardList)) {
            return Collections.emptyList();
        }
        List<MemberCardVo> memberCardVoList = Lists.newArrayList();
        for (MemberCard memberCard : memberCardList) {
            MemberCardVo memberCardVo = convertVo(memberCard);
            if (memberCardVo != null) {
                memberCardVoList.add(memberCardVo);
            }
        }
        return memberCardVoList;
    }
}
