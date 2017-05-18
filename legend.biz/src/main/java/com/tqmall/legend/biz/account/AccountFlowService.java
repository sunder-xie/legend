package com.tqmall.legend.biz.account;

import com.tqmall.legend.biz.account.bo.CardGrantReverseBo;
import com.tqmall.legend.biz.account.bo.ComboRechargeReverseBo;
import com.tqmall.legend.biz.account.bo.CouponRechargeReverseBo;
import com.tqmall.legend.biz.account.bo.RechargeComboBo;
import com.tqmall.legend.biz.account.vo.BackCardVo;
import com.tqmall.legend.entity.account.*;
import com.tqmall.legend.entity.account.vo.AccountCouponVo;
import com.tqmall.legend.entity.account.vo.MemberCardChargeVo;
import com.tqmall.legend.facade.account.vo.MemberGrantVo;
import com.tqmall.legend.facade.discount.bo.DiscountSelectedBo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by majian on 16/7/13.
 */
public interface AccountFlowService {

    /**
     * 账单支付
     * @param shopId
     * @param userId
     * @param orderId
     * @param cardPayAmount
     * @param accountInfo
     * @param memberCard
     * @return
     */
    AccountTradeFlow recordFlowForBillPay(
            Long shopId,
            Long userId,
            Long orderId,
            BigDecimal cardPayAmount,
            AccountInfo accountInfo,
            MemberCard memberCard);

    /**
     * 账单确认
     * @param shopId
     * @param userId
     * @param orderId
     * @return key为accountId，value为flowId
     */
    Map<Long, Long> recordFlowForBillConfirm(
            Long shopId,
            Long userId,
            Long orderId,
            DiscountSelectedBo discountSelectedBo);

    /**
     * 账单支付撤销(工单冲红)
     * @param shopId
     * @param userId
     * @param flow
     * @param detail
     * @param cardId
     * @return
     */
    AccountTradeFlow recordReversFlowForBillPay(
            Long shopId,
            Long userId,
            AccountTradeFlow flow,
            AccountCardFlowDetail detail,
            Long cardId);

    /**
     * 账单确认撤销(工单冲红)
     * @param shopId
     * @param userId
     * @param flow
     * @param couponDetailList
     * @param comboDetailList
     * @return
     */
    AccountTradeFlow recordReverseFlowForBillConfirm(
            Long shopId,
            Long userId,
            AccountTradeFlow flow,
            List<AccountCouponFlowDetail> couponDetailList,
            List<AccountComboFlowDetail> comboDetailList);

    /**
     * 优惠券发放
     * @param accountCouponVo
     * @param shopId
     * @param creator
     * @param accountId
     * @return
     */
    AccountTradeFlow recordFlowForCouponCharge(AccountCouponVo accountCouponVo, Long shopId, Long creator, Long accountId);

    /**
     * 计次卡办理
     * @param rechargeComboBo
     * @param shopId
     * @param userId
     * @param combo
     * @return
     */
    AccountTradeFlow recordFlowForComboCharge(RechargeComboBo rechargeComboBo, Long shopId, Long userId, AccountCombo combo);

    /**
     * 会员卡充值
     * @param shopId
     * @param userId
     * @param memberCardChargeVo
     * @param accountInfo
     * @return
     */
    AccountTradeFlow recordFlowForCardCharge(Long shopId, Long userId, MemberCardChargeVo memberCardChargeVo, AccountInfo accountInfo, MemberCard memberCard);

    /**
     * 会员卡充值撤销
     * @param shopId
     * @param userId
     * @param flow
     * @param detail
     * @return
     */
    AccountTradeFlow recordRevertFlowForCardCharge(Long shopId, Long userId, AccountTradeFlow flow, AccountCardFlowDetail detail);


    /**
     * 记录会员卡办理的流水
     * @param memberGrantVo 参数
     * @param accountInfo 账户
     * @param shopId
     *@param userId @return
     */
    AccountTradeFlow recordFlowForHandleMemberCard(MemberGrantVo memberGrantVo, AccountInfo accountInfo, MemberCard memberCard, Long shopId, Long userId);

    /**
     * 会员卡退卡
     * @param memberCard
     * @return
     */
    AccountTradeFlow recordFlowForCardBack(BackCardVo backCardVo, MemberCard memberCard);

    /**
     * 计次卡办理撤销
     * @param comboRechargeReverseBo
     * @return
     */
    AccountTradeFlow recordReverseFlowForComboRecharge(ComboRechargeReverseBo comboRechargeReverseBo);

    /**
     * 优惠券发放撤销
     * @param couponRechargeReverseBo
     * @return
     */
    AccountTradeFlow recordReverseFlowForCouponRecharge(CouponRechargeReverseBo couponRechargeReverseBo);

    /**
     * 会员卡办理撤销
     * @param cardGrantReverseBo
     * @return
     */
    AccountTradeFlow recordReverseFlowForCardGrant(CardGrantReverseBo cardGrantReverseBo);

    void recordFlowForCouponImport(List<AccountCoupon> couponList, Long shopId, Long userId);

    void recordFlowForComboImport(AccountCombo combo, Long shopId, Long userId);

    /**
     * 查询会员卡交易流水
     * @param shopID
     * @param memberCardId
     * @return
     */
    List<AccountTradeFlow> listCardFlowByCardId(Long shopID, Long memberCardId);

    /**
     * 查询计次卡交易流水
     * @param shopID
     * @param comboId
     * @return
     */
    List<AccountTradeFlow> listComboFlowByComboId(Long shopID, Long comboId);
}
