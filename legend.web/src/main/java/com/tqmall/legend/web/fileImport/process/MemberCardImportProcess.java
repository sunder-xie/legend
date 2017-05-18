package com.tqmall.legend.web.fileImport.process;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.legend.biz.account.AccountCardFlowDetailService;
import com.tqmall.legend.biz.account.AccountTradeFlowService;
import com.tqmall.legend.biz.account.MemberCardService;
import com.tqmall.legend.common.fileImport.CommonFileImportContext;
import com.tqmall.legend.common.fileImport.FileImportProcess;
import com.tqmall.legend.entity.account.AccountCardFlowDetail;
import com.tqmall.legend.entity.account.AccountConsumeTypeEnum;
import com.tqmall.legend.entity.account.AccountTradTypeEnum;
import com.tqmall.legend.entity.account.AccountTradeFlow;
import com.tqmall.legend.entity.account.MemberCard;
import com.tqmall.legend.web.fileImport.vo.MemberCardImportContext;
import com.tqmall.legend.web.fileImport.vo.MemberCardTypeInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by twg on 16/12/7.
 */
@Slf4j
@Component
public class MemberCardImportProcess implements FileImportProcess {
    @Autowired
    private MemberCardService memberCardService;
    @Autowired
    private AccountTradeFlowService accountTradeFlowService;
    @Autowired
    private AccountCardFlowDetailService accountCardFlowDetailService;

    @Override
    @Transactional
    public void process(CommonFileImportContext fileImportContext) {
        log.debug("this is MemberCardImportProcess");

        MemberCardImportContext memberCardImportContext = (MemberCardImportContext) fileImportContext;

        Long shopId = memberCardImportContext.getShopId();
        Long userId = memberCardImportContext.getUserId();
        Map<String, Integer> typeNumMap = Maps.newTreeMap();
        List<MemberCardTypeInfo> memberCardTypeInfos = Lists.newArrayList();
        List<MemberCard> memberCardList = memberCardImportContext.getExcelContents();

        for (MemberCard memberCard : memberCardList) {
            memberCardService.insert(memberCard);
            AccountTradeFlow flow = getAccountTradeFlow(shopId, userId, memberCard);
            /*按会员卡类型，统计导入个数*/
            setTypeNum(typeNumMap, memberCard);
            insertAccountCardFlowDetail(shopId, userId, memberCard, flow);
        }

        for (Map.Entry<String, Integer> entry : typeNumMap.entrySet()) {
            MemberCardTypeInfo memberCardTypeInfo = new MemberCardTypeInfo();
            memberCardTypeInfo.setTypeName(entry.getKey());
            memberCardTypeInfo.setTypeNum(entry.getValue());
            memberCardTypeInfos.add(memberCardTypeInfo);
        }
        memberCardImportContext.setSuccessNum(memberCardList.size());
        memberCardImportContext.setMemberCardTypeInfos(memberCardTypeInfos);
    }

    private void insertAccountCardFlowDetail(Long shopId, Long userId, MemberCard memberCard, AccountTradeFlow flow) {
        AccountCardFlowDetail accountCardFlowDetail = new AccountCardFlowDetail();
        accountCardFlowDetail.setShopId(shopId);
        accountCardFlowDetail.setCreator(userId);
        accountCardFlowDetail.setCardBalance(memberCard.getBalance());
        accountCardFlowDetail.setChangeAmount(memberCard.getBalance());
        accountCardFlowDetail.setCardId(memberCard.getId());
        accountCardFlowDetail.setAccountTradeFlowId(flow.getId());
        accountCardFlowDetail.setConsumeType(AccountCardFlowDetail.ConsumeTypeEnum.INIT.getCode());//6办卡
        accountCardFlowDetailService.insert(accountCardFlowDetail);
    }

    private AccountTradeFlow getAccountTradeFlow(Long shopId, Long userId, MemberCard memberCard) {
        AccountTradeFlow flow = accountTradeFlowService.generateAccountTradeBase(shopId, userId);
        flow.setMobile(memberCard.getMobile());
        flow.setAccountId(memberCard.getAccountId());
        flow.setConsumeType(AccountConsumeTypeEnum.INIT.getCode());
        if (memberCard.getBalance() != null) {
            flow.setCardExplain(memberCard.getBalance().toString());
            flow.setCardBalance(memberCard.getBalance());
        }
        flow.setCustomerName(memberCard.getCustomerName());
        flow.setTradeType(AccountTradTypeEnum.MEMBER_CARD.getCode());
        accountTradeFlowService.insert(flow);
        return flow;
    }

    private void setTypeNum(Map<String, Integer> typeNumMap, MemberCard memberCard) {
        String cardTypeName = memberCard.getCardTypeName();
        if (typeNumMap.containsKey(cardTypeName)) {
            Integer typeNum = typeNumMap.get(cardTypeName);
            typeNumMap.put(cardTypeName, ++typeNum);
        } else {
            typeNumMap.put(cardTypeName, 1);
        }
    }
}
