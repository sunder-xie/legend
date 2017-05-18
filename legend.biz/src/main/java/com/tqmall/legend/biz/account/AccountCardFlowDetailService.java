package com.tqmall.legend.biz.account;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.tqmall.legend.entity.account.*;

/**
 * Created by twg on 16/6/13.
 */
public interface AccountCardFlowDetailService {

    boolean insert(AccountCardFlowDetail cardFlowDetail);

    void delete(Long shopId);

//    AccountCardFlowDetail generateAccountCardFlowDetailBase(Long shopId, Long userId);

    AccountCardFlowDetail findByFlowId(Long shopId, Long flowId);

    void recordReverseDetailForConsume(Long shopId, Long userId, AccountCardFlowDetail detail, Long reverseFlowId, MemberCard memberCard);

    void recordDetailForConume(Long shopId, Long userId, BigDecimal cardPayAmount, MemberCard memberCard, Long tradeFlowId);

    void recordDetailForCharge(BigDecimal changeAmount,
                               Long shopId,
                               AccountInfo accountInfo,
                               MemberCard memberCard,
                               Long accountTradeFlowId, Long userId);

    void recordReverseDetailForCharge(Long shopId, Long userId, AccountTradeFlow reversFlow, MemberCard memberCard);

    /**
     * 获取门店一段时间内的充值金额总和
     *
     * @param shopId
     * @param date1
     * @param date2
     * @return
     */
    BigDecimal getRechargeSummaryAmount(Long shopId, Date date1, Date date2);

    /**
     * 获取门店一段时间内的充值客户总数
     *
     * @param shopId
     * @param date1
     * @param date2
     * @return
     */
    Integer getRechargeCustomerCount(Long shopId, Date date1, Date date2);

    /**
     * 记录办卡流水明细
     *
     * @param initBalance
     * @param shopId
     * @param accountInfo
     * @param flowId
     * @param userId
     */
    void recordDetailForHandle(BigDecimal initBalance, Long shopId, AccountInfo accountInfo, MemberCard memberCard, Long flowId, Long userId);

    /**
     * 记录会员卡升级明细
     *
     * @param shopId
     * @param balance
     * @param cardId
     * @param accountInfo
     * @param flowId
     * @param userId
     */
    void recordDetailForUpgrade(Long shopId, BigDecimal balance, Long cardId, AccountInfo accountInfo, Long flowId, Long userId);

    /**
     * 记录办卡撤销流水明细
     *
     * @param shopId
     * @param userId
     * @param reverseFlowId
     * @param balance
     * @param detail
     */
    void recordReverseDetailForGrant(Long shopId, Long userId, Long reverseFlowId, BigDecimal balance, AccountCardFlowDetail detail);

    List<AccountCardFlowDetail> getCardFlowDetailByFlowIds(Long shopId, List flowIds);

    /**
     * 批量记录导入流水
     *
     * @param shopId
     * @param userId
     * @param memberCardList
     * @param flowMap
     */
    void recordListForInit(Long shopId, Long userId, List<? extends MemberCard> memberCardList, Map<Long, Long> flowMap);


    List<AccountCardFlowDetail> getAccountCardFlowDetails(Map param);

    /**
     * 查询会员卡交易流水
     *
     * @param shopID
     * @param memberCardId
     * @return
     */
    List<AccountCardFlowDetail> listByCardId(Long shopID, Long memberCardId);

    /**
     * 查询会员卡的累计消费情况
     *
     * @param shopID
     * @param cardId
     * @return
     */
    MemberCardUsedSummay sumUsedInfo(Long shopID, Long cardId);

    void recordDetailForBack(Long shopId, Long userId, MemberCard memberCard, Long flowId);

    void batchSave(List<AccountCardFlowDetail> accountCardFlowDetails);

    /**
     * 查询会员卡充值流水id集合
     *
     * @param shopId
     * @param memberCardIdList
     * @return
     */
    List<Long> getRechargeTradeFlowIds(Long shopId, List<Long> memberCardIdList);

    void recordDetailForBillConfirm(Long shopId, Long userId, Long flowId, MemberCard memberCard);

    int getCardConsumeNum(Long shopId, List<Long> cardIds);

    Map<Long, String> getFlowIdAndCardTypeNameRel(Long shopId, List<Long> flowIdList);
}
