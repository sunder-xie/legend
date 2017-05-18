package com.tqmall.legend.biz.account.impl;

import java.math.BigDecimal;
import java.util.*;

import com.tqmall.legend.bi.entity.CommonPair;
import com.tqmall.legend.biz.account.MemberCardInfoService;
import com.tqmall.legend.biz.account.MemberCardService;
import com.tqmall.wheel.lang.Langs;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.legend.biz.account.AccountCardFlowDetailService;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.dao.account.AccountCardFlowDetailDao;
import com.tqmall.legend.entity.account.*;

/**
 * Created by twg on 16/6/13.
 */
@Slf4j
@Service
public class AccountCardFlowDetailServiceImpl extends BaseServiceImpl implements AccountCardFlowDetailService {
    @Autowired
    private AccountCardFlowDetailDao accountCardFlowDetailDao;
    @Autowired
    private MemberCardService memberCardService;
    @Autowired
    private MemberCardInfoService memberCardInfoService;

    @Override
    public boolean insert(AccountCardFlowDetail cardFlowDetail) {
        return accountCardFlowDetailDao.insert(cardFlowDetail) > 0 ? true : false;
    }

    @Override
    public void delete(Long shopId) {
        Map param = Maps.newHashMap();
        if(shopId != null){
            param.put("shopId",shopId);
        }
        accountCardFlowDetailDao.delete(param);

    }

    private AccountCardFlowDetail generateAccountCardFlowDetailBase(Long shopId, Long userId) {
        AccountCardFlowDetail cardFlowDetail = new AccountCardFlowDetail();
        cardFlowDetail.setCreator(userId);
        cardFlowDetail.setModifier(userId);
        cardFlowDetail.setShopId(shopId);
        return cardFlowDetail;
    }

    @Override
    public AccountCardFlowDetail findByFlowId(Long shopId, Long flowId) {
        Map param = Maps.newHashMap();
        param.put("shopId",shopId);
        param.put("accountTradeFlowId",flowId);
        List<AccountCardFlowDetail> detailList = accountCardFlowDetailDao.select(param);
        if (detailList != null && detailList.size() == 1) {
            return detailList.get(0);
        }
        return null;
    }

    @Override
    public void recordReverseDetailForConsume(Long shopId, Long userId, AccountCardFlowDetail detail, Long reverseFlowId, MemberCard memberCard) {
        AccountCardFlowDetail reverseDetail = generateAccountCardFlowDetailBase(shopId,userId);
        reverseDetail.setCardId(memberCard.getId());
        reverseDetail.setChangeAmount(detail.getChangeAmount().negate());
        reverseDetail.setConsumeType(AccountCardFlowDetail.ConsumeTypeEnum.CONSUME_REVERT.getCode());
        reverseDetail.setAccountTradeFlowId(reverseFlowId);
        reverseDetail.setCardBalance(memberCard.getBalance());
        accountCardFlowDetailDao.insert(reverseDetail);
    }

    @Override
    public void recordDetailForConume(Long shopId, Long userId, BigDecimal cardPayAmount, MemberCard memberCard, Long tradeFlowId) {
        AccountCardFlowDetail cardFlowDetail = generateAccountCardFlowDetailBase(shopId, userId);
        cardFlowDetail.setCardId(memberCard.getId());
        cardFlowDetail.setChangeAmount(cardPayAmount.negate());
        cardFlowDetail.setCardBalance(memberCard.getBalance());
        cardFlowDetail.setConsumeType(AccountCardFlowDetail.ConsumeTypeEnum.CONSUME.getCode());//消费
        cardFlowDetail.setAccountTradeFlowId(tradeFlowId);
        accountCardFlowDetailDao.insert(cardFlowDetail);
    }

    @Override
    public void recordDetailForCharge(BigDecimal changeAmount,
                                      Long shopId,
                                      AccountInfo accountInfo,
                                      MemberCard memberCard,
                                      Long accountTradeFlowId,
                                      Long userId) {
        AccountCardFlowDetail detail = generateAccountCardFlowDetailBase(shopId, userId);
        detail.setCardId(memberCard.getId());
        detail.setChangeAmount(changeAmount);
        detail.setCardBalance(memberCard.getBalance());
        detail.setConsumeType(AccountCardFlowDetail.ConsumeTypeEnum.CHARGE.getCode());
        detail.setAccountTradeFlowId(accountTradeFlowId);
        accountCardFlowDetailDao.insert(detail);


    }

    @Override
    public void recordReverseDetailForCharge(Long shopId, Long userId, AccountTradeFlow reversFlow, MemberCard memberCard) {
        Long flowId = reversFlow.getReverseId();
        AccountCardFlowDetail detail = this.findByFlowId(shopId, flowId);
        AccountCardFlowDetail reverseDetail = generateAccountCardFlowDetailBase(shopId, userId);
        reverseDetail.setCardId(detail.getCardId());
        reverseDetail.setChangeAmount(detail.getChangeAmount().negate());
        reverseDetail.setCardBalance(memberCard.getBalance());
        reverseDetail.setConsumeType(AccountCardFlowDetail.ConsumeTypeEnum.CHARGE_REVERT.getCode());
        reverseDetail.setAccountTradeFlowId(reversFlow.getId());
        accountCardFlowDetailDao.insert(reverseDetail);

    }

    @Override
    public BigDecimal getRechargeSummaryAmount(Long shopId, Date date1, Date date2) {
        return accountCardFlowDetailDao.getRechargeSummaryAmount(shopId,date1,date2);
    }

    @Override
    public Integer getRechargeCustomerCount(Long shopId, Date date1, Date date2) {
        return accountCardFlowDetailDao.getRechargeCustomerCount(shopId,date1,date2);
    }

    @Override
    public void recordDetailForHandle(BigDecimal initBalance, Long shopId, AccountInfo accountInfo, MemberCard memberCard, Long flowId, Long userId) {
        AccountCardFlowDetail detail = generateAccountCardFlowDetailBase(shopId, userId);
        detail.setCardId(memberCard.getId());
        detail.setChangeAmount(initBalance);
        detail.setCardBalance(memberCard.getBalance());
        detail.setConsumeType(AccountCardFlowDetail.ConsumeTypeEnum.HANDLE_CARD.getCode());
        detail.setAccountTradeFlowId(flowId);
        accountCardFlowDetailDao.insert(detail);
    }

    @Override
    public void recordDetailForUpgrade(Long shopId, BigDecimal balance, Long cardId, AccountInfo accountInfo, Long flowId, Long userId) {
        AccountCardFlowDetail detail = generateAccountCardFlowDetailBase(shopId, userId);
        detail.setCardId(cardId);
        detail.setChangeAmount(new BigDecimal(0));
        detail.setCardBalance(balance);
        detail.setConsumeType(AccountCardFlowDetail.ConsumeTypeEnum.UPGRADE.getCode());
        detail.setAccountTradeFlowId(flowId);
        accountCardFlowDetailDao.insert(detail);
    }

    @Override
    public void recordReverseDetailForGrant(Long shopId, Long userId, Long reverseFlowId, BigDecimal balance, AccountCardFlowDetail detail) {
        AccountCardFlowDetail reverseDetail = generateAccountCardFlowDetailBase(shopId, userId);
        reverseDetail.setCardId(detail.getCardId());
        reverseDetail.setChangeAmount(detail.getChangeAmount().negate());
        reverseDetail.setCardBalance(balance);
        reverseDetail.setConsumeType(AccountCardFlowDetail.ConsumeTypeEnum.GRANT_REVERSE.getCode());
        reverseDetail.setAccountTradeFlowId(reverseFlowId);
        accountCardFlowDetailDao.insert(reverseDetail);
    }

    @Override
    public List<AccountCardFlowDetail> getCardFlowDetailByFlowIds(Long shopId, List flowIds) {
        if (CollectionUtils.isEmpty(flowIds)) {
            return Lists.newArrayList();
        }
        return accountCardFlowDetailDao.getCardFlowDetailByFlowIds(shopId,flowIds);
    }

    @Override
    public void recordListForInit(Long shopId, Long userId, List<? extends MemberCard> memberCardList, Map<Long, Long> flowMap) {
        if (CollectionUtils.isEmpty(memberCardList)) {
            return;
        }
        List<AccountCardFlowDetail> detailList = Lists.newArrayList();
        for (MemberCard item : memberCardList) {
            AccountCardFlowDetail detail = generateAccountCardFlowDetailBase(shopId, userId);
            detail.setCardId(item.getId());
            detail.setChangeAmount(item.getBalance());
            detail.setCardBalance(item.getBalance());
            detail.setConsumeType(6);
            detail.setAccountTradeFlowId(flowMap.get(item.getAccountId()));
            detailList.add(detail);
        }
        if (!CollectionUtils.isEmpty(detailList)) {
            accountCardFlowDetailDao.batchInsert(detailList);
        }
    }

    @Override
    public List<AccountCardFlowDetail> getAccountCardFlowDetails(Map param) {
        return accountCardFlowDetailDao.select(param);
    }

    @Override
    public List<AccountCardFlowDetail> listByCardId(Long shopID, Long memberCardId) {
        Map param = Maps.newHashMap();
        param.put("shopId",shopID);
        param.put("cardId",memberCardId);

        return accountCardFlowDetailDao.select(param);
    }

    @Override
    public MemberCardUsedSummay sumUsedInfo(Long shopID, Long cardId) {
        return accountCardFlowDetailDao.sumUsedInfo(shopID, cardId);
    }

    @Override
    public void recordDetailForBack(Long shopId, Long userId, MemberCard memberCard, Long flowId) {
        AccountCardFlowDetail detail = generateAccountCardFlowDetailBase(shopId, userId);
        detail.setCardId(memberCard.getId());
        detail.setChangeAmount(BigDecimal.ZERO);
        detail.setCardBalance(memberCard.getBalance());
        detail.setConsumeType(AccountCardFlowDetail.ConsumeTypeEnum.BACK.getCode());
        detail.setAccountTradeFlowId(flowId);
        accountCardFlowDetailDao.insert(detail);
    }

    @Override
    public void batchSave(List<AccountCardFlowDetail> accountCardFlowDetails) {
        super.batchInsert(accountCardFlowDetailDao,accountCardFlowDetails,1000);
    }

    /**
     * 查询会员卡充值流水id集合
     *
     * @param shopId
     * @param memberCardIdList
     * @return
     */
    @Override
    public List<Long> getRechargeTradeFlowIds(Long shopId, List<Long> memberCardIdList) {
        Assert.notNull(shopId, "店铺id不能为空.");
        Assert.notEmpty(memberCardIdList, "会员卡id不能为空.");

        List<Long> rechargeTradeFlowIds = accountCardFlowDetailDao.getRechargeTradeFlowIds(shopId, memberCardIdList);
        if (CollectionUtils.isEmpty(rechargeTradeFlowIds)) {
            return Collections.emptyList();
        }
        return rechargeTradeFlowIds;
    }

    @Override
    public void recordDetailForBillConfirm(Long shopId, Long userId, Long flowId, MemberCard memberCard) {
        AccountCardFlowDetail detail = generateAccountCardFlowDetailBase(shopId, userId);
        detail.setCardId(memberCard.getId());
        detail.setChangeAmount(BigDecimal.ZERO);
        detail.setCardBalance(memberCard.getBalance());
        detail.setConsumeType(AccountCardFlowDetail.ConsumeTypeEnum.CONSUME.getCode());
        detail.setAccountTradeFlowId(flowId);
        accountCardFlowDetailDao.insert(detail);
    }

    @Override
    public int getCardConsumeNum(Long shopId, List<Long> cardIds) {
        if (CollectionUtils.isEmpty(cardIds)) {
            return 0;
        }
        return accountCardFlowDetailDao.getCardConsumeNum(shopId, cardIds);
    }

    @Override
    public Map<Long, String> getFlowIdAndCardTypeNameRel(Long shopId, List<Long> flowIdList) {
        if (Langs.isEmpty(flowIdList)) {
            return Collections.emptyMap();
        }
        List<CommonPair<Long, Long>> flowIdAndCardIdRelList = accountCardFlowDetailDao.getFlowIdAndCardIdRel(shopId, flowIdList);
        if (Langs.isEmpty(flowIdAndCardIdRelList)) {
            return Collections.emptyMap();
        }
        Map<Long, Long> flowIdAndCardIdRel = new HashMap<>(flowIdAndCardIdRelList.size());
        for (CommonPair<Long, Long> commonPair : flowIdAndCardIdRelList) {
            Long flowId = commonPair.getDataF();
            Long cardId = commonPair.getDataS();
            flowIdAndCardIdRel.put(flowId, cardId);
        }
        List<MemberCard> memberCardList = memberCardService.getMemberCardWithDeletedByIds(shopId, new ArrayList<>(flowIdAndCardIdRel.values()));
        if (Langs.isEmpty(memberCardList)) {
            return Collections.emptyMap();
        }
        Map<Long, Long> cardIdAndCardTypeIdMap = new HashMap<>();
        for (MemberCard memberCard : memberCardList) {
            cardIdAndCardTypeIdMap.put(memberCard.getId(), memberCard.getCardTypeId());
        }
        Map<Long, String> cardTypeIdNameMap = memberCardInfoService.getIdNameMap(shopId, new ArrayList<>(cardIdAndCardTypeIdMap.values()));

        Map<Long, String> flowIdAndCardTypeNameMap = new HashMap<>();
        for (Map.Entry<Long, Long> entry : flowIdAndCardIdRel.entrySet()) {
            Long flowId = entry.getKey();
            Long cardId = entry.getValue();
            Long cardTypeId = cardIdAndCardTypeIdMap.get(cardId);
            if (cardTypeId != null) {
                String cardTypeName = cardTypeIdNameMap.get(cardTypeId);
                flowIdAndCardTypeNameMap.put(flowId, cardTypeName);
            }
        }
        return flowIdAndCardTypeNameMap;
    }
}
