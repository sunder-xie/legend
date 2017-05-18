package com.tqmall.legend.biz.account;

import com.tqmall.legend.biz.account.bo.RechargeComboBo;
import com.tqmall.legend.biz.account.vo.BackCardVo;
import com.tqmall.legend.entity.account.*;
import com.tqmall.legend.entity.account.vo.AccountCouponVo;
import com.tqmall.legend.entity.account.vo.MemberCardChargeVo;
import com.tqmall.legend.facade.account.vo.MemberGrantVo;
import com.tqmall.legend.facade.account.vo.MemberUpgradeVo;
import com.tqmall.legend.facade.discount.bo.DiscountSelectedBo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by twg on 16/6/7.
 */
public interface AccountTradeFlowService {
    boolean insert(AccountTradeFlow accountTradeFlow);

    List<AccountTradeFlow> getAccountTradFlows(Long shopId,Long accountId);

    Page<AccountTradeFlow> getAccountTradFlowsByPage(Pageable pageable,Map<String, Object> params);

    void batchInsert(List<AccountTradeFlow> accountTradeFlowList);

    Integer getTradeType(boolean isCouponUsed, boolean isComboUsed, boolean isCardUsed);

    List<AccountTradeFlow> select(Map<String, Object> params);

//    AccountTradeFlow generateAccountTradeBase(Long shopId, Long userId);

    void delete(Long shopId);

    List<AccountTradeFlow> selectByOrderId(Long shopId, Long orderId);

    AccountTradeFlow findById(Long shopId, Long flowId);

    AccountTradeFlow recordFlowForBillPay(
            Long shopId,
            Long userId,
            Long orderId,
            BigDecimal cardPayAmount,
            AccountInfo accountInfo,
            MemberCard memberCard);

    Map<Long, Long> recordFlowForBillConfirm(
            Long shopId,
            Long userId,
            Long orderId,
            DiscountSelectedBo discountSelectedBo);

    AccountTradeFlow recordReversFlowForBillPay(
            Long shopId,
            Long userId,
            AccountTradeFlow flow,
            AccountCardFlowDetail detail,
            Long accountId,
            MemberCard memberCard);

    AccountTradeFlow recordReverseFlowForBillConfirm(
            Long shopId,
            Long userId,
            AccountTradeFlow flow,
            List<AccountCouponFlowDetail> couponDetailList,
            List<AccountComboFlowDetail> comboDetailList);

    void recordFlowForCouponCharge(AccountCouponVo accountCouponVo, AccountTradeFlow accountTradeFlow, Long shopId, Long creator, List<AccountCoupon> result);

    AccountTradeFlow recordTradeFlowForCouponCharge(AccountCouponVo accountCouponVo, Long shopId, Long creator, Long accountId);

    AccountTradeFlow recordFlowForComboCharge(RechargeComboBo rechargeComboBo, Long shopId, Long userId, AccountCombo combo);

    AccountTradeFlow recordFlowForCardCharge(Long userId, Long shopId, MemberCardChargeVo memberCardChargeVo, AccountInfo accountInfo, MemberCard memberCard);

    AccountTradeFlow recordRevertFlowForCardCharge(Long shopId, Long userId, AccountTradeFlow flow, AccountCardFlowDetail detail, MemberCard memberCard);


    /**
     * 记录会员卡办理的流水
     * @param memberGrantVo 参数
     * @param accountInfo 账户
     * @param shopId
     *@param userId @return
     */
    AccountTradeFlow recordFlowForHandleMemberCard(MemberGrantVo memberGrantVo, AccountInfo accountInfo, MemberCard memberCard, Long shopId, Long userId);
    /**
     * 记录会员卡升级流水
     * @return
     */
    AccountTradeFlow recordFlowForUpgradeMemberCard(Long shopId, Long operatorId, MemberUpgradeVo memberUpgradeVo);

    AccountTradeFlow recordFlowForCardBack(BackCardVo backCardVo, MemberCard memberCard);

    /**
     * 查询充值列表分页接口
     * @param pageable
     * @param shopId
     * @param date1
     * @param date2
     * @param mobile
     * @return
     */
    Page<AccountTradeFlow> getPageFlowForCardRecharge(Pageable pageable, Long shopId, Date date1, Date date2,String mobile);


    void update(AccountTradeFlow accountTradeFlow);

    /**
     * 优惠券发放撤销
     * @param shopId
     * @param userId
     * @param userName
     * @param flow
     * @param couponExpalin
     * @return
     */
    AccountTradeFlow recordReverseFlowForCouponRecharge(Long shopId, Long userId, String userName, AccountTradeFlow flow, String couponExpalin);

    /**
     * 计次卡办理撤销
     * @param shopId
     * @param userId
     * @param userName
     * @param flow
     * @param comboExplain
     * @return
     */
    AccountTradeFlow recordReverseFlowForComboRecharge(Long shopId, Long userId, String userName, AccountTradeFlow flow, String comboExplain);

    /**
     * 获取会员卡消费流水
     *
     * @param shopId
     * @param accountId
     * @return
     */
    List<AccountTradeFlow> getMemberCardConsumeFlow(Long shopId, Long accountId);


    List<AccountTradeFlow> getMemberCardGrantFlowByAccountIds(Long shopId,List accountIds);

    /**
     * 会员卡办理撤销
     * @param shopId
     * @param userId
     * @param userName
     * @param flow
     * @param cardExpalin
     * @param balance
     * @return
     */
    AccountTradeFlow recordReverseFlowForCardGrant(Long shopId, Long userId, String userName, AccountTradeFlow flow, String cardExpalin, BigDecimal balance);

    /**
     * 记入会员卡导入流水
     * @param memberCardList
     * @param shopId
     *@param userId @return
     */
    void recordFlowFowCardInit(List< ? extends MemberCard> memberCardList, Long shopId, Long userId);

    AccountTradeFlow recordFlowForCouponImport(AccountCoupon coupon, Long shopId, Long userId);

    AccountTradeFlow recordFlowForComboImport(AccountCombo combo, Long shopId, Long userId);

    List<AccountTradeFlow> getAccountTradeFlowByIds(Long... flowIds);

    /**
     * 批量查流水
     * @param shopID
     * @param flowIds
     * @return
     */
    List<AccountTradeFlow> listByIds(Long shopID, Collection<Long> flowIds);

    /**
     * 生成账户交易流水号
     * @param shopId
     * @param userId
     * @return
     */
    AccountTradeFlow generateAccountTradeBase(Long shopId, Long userId);

    /**
     * 根据账户ids 和 消费类型
     * @param shopId
     * @param accountIds
     * @param consumeType
     * @return
     */
    List<AccountTradeFlow> findFlowByAccountIdsAndConsumeType(Long shopId,List accountIds,int consumeType);

    /**
     * 获取某个时间段批量添加的账户交易流水
     * @param shopId
     * @param accountIds 账户ids
     * @param consumeType
     * @return
     */
    List<AccountTradeFlow> findFlowByAccountIdsMobilesAndConsumeType(Long shopId,List<Long> accountIds,int consumeType,String importFlag);

    /**
     * 根据工单获取
     * @param shopId
     * @param orderId
     * @return
     */
    List<AccountTradeFlow> findComboDetail(Long shopId , Long orderId);

    /**
     * 获取账户的累计充值金额
     * @param shopId
     * @return
     */
    BigDecimal getTotalChargeAmountByCardId(Long shopId, Long cardId);

    /**
     * 查询会员卡累计收款金额
     * @param shopId
     * @param memberCardId
     * @return
     */
    BigDecimal getCardTotalPayAmount(Long shopId, Long memberCardId);
}
