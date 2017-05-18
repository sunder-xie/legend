package com.tqmall.legend.biz.account.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.legend.biz.account.*;
import com.tqmall.legend.biz.account.bo.CardGrantReverseBo;
import com.tqmall.legend.biz.account.bo.ComboRechargeReverseBo;
import com.tqmall.legend.biz.account.bo.CouponRechargeReverseBo;
import com.tqmall.legend.biz.account.bo.RechargeComboBo;
import com.tqmall.legend.biz.account.vo.BackCardVo;
import com.tqmall.legend.biz.order.OrderInfoService;
import com.tqmall.legend.entity.account.*;
import com.tqmall.legend.entity.account.vo.AccountCouponVo;
import com.tqmall.legend.entity.account.vo.MemberCardChargeVo;
import com.tqmall.legend.entity.order.OrderInfo;
import com.tqmall.legend.facade.account.vo.MemberGrantVo;
import com.tqmall.legend.facade.discount.bo.DiscountSelectedBo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by majian on 16/7/13.
 */
@Service
@Slf4j
public class AccountFlowServiceImpl implements AccountFlowService {
    @Autowired
    private AccountTradeFlowService accountTradeFlowService;
    @Autowired
    private MemberCardService memberCardService;
    @Autowired
    private AccountCardFlowDetailService accountCardFlowDetailService;
    @Autowired
    private AccountComboFlowDetailService accountComboFlowDetailService;
    @Autowired
    private AccountCouponFlowDetailService accountCouponFlowDetailService;
    @Autowired
    private MemberCardInfoService memberCardInfoService;
    @Autowired
    private AccountSuiteService accountSuiteService;
    @Autowired
    private AccountCouponService accountCouponService;
    @Autowired
    private OrderInfoService orderInfoService;


    @Override
    public AccountTradeFlow recordFlowForBillPay(Long shopId, Long userId, Long orderId, BigDecimal cardPayAmount, AccountInfo accountInfo, MemberCard memberCard) {
        AccountTradeFlow flow = accountTradeFlowService.recordFlowForBillPay(shopId, userId, orderId, cardPayAmount, accountInfo, memberCard);
        Long tradeFlowId = flow.getId();
        accountCardFlowDetailService.recordDetailForConume(shopId, userId, cardPayAmount, memberCard, tradeFlowId);
        return flow;

    }

    @Override
    public Map<Long, Long> recordFlowForBillConfirm(Long shopId, Long userId, Long orderId, DiscountSelectedBo discountSelectedBo) {
        return accountTradeFlowService.recordFlowForBillConfirm(shopId, userId, orderId, discountSelectedBo);
    }

    @Override
    public AccountTradeFlow recordReversFlowForBillPay(Long shopId, Long userId, AccountTradeFlow flow, AccountCardFlowDetail detail, Long cardId) {
        MemberCard memberCard = memberCardService.findByIdContainDeleted(shopId, cardId);
        AccountTradeFlow reverseFlow = accountTradeFlowService.recordReversFlowForBillPay(shopId, userId, flow, detail, flow.getAccountId(),memberCard);
        Long reverseFlowId = reverseFlow.getId();
        accountCardFlowDetailService.recordReverseDetailForConsume(shopId, userId, detail, reverseFlowId, memberCard);
        return reverseFlow;
    }

    @Override
    public AccountTradeFlow recordReverseFlowForBillConfirm(Long shopId, Long userId, AccountTradeFlow flow, List<AccountCouponFlowDetail> couponDetailList, List<AccountComboFlowDetail> comboDetailList) {
        AccountTradeFlow reverseFlow = accountTradeFlowService.recordReverseFlowForBillConfirm(shopId, userId, flow, couponDetailList, comboDetailList);

        return reverseFlow;
    }

    @Override
    public AccountTradeFlow recordFlowForCouponCharge(AccountCouponVo accountCouponVo, Long shopId, Long creator, Long accountId) {
        AccountTradeFlow flow = accountTradeFlowService.recordTradeFlowForCouponCharge(accountCouponVo, shopId, creator, accountId);
        return flow;
    }

    @Override
    public AccountTradeFlow recordFlowForComboCharge(RechargeComboBo rechargeComboBo, Long shopId, Long userId, AccountCombo combo) {

        AccountTradeFlow flow = accountTradeFlowService.recordFlowForComboCharge(rechargeComboBo, shopId, userId, combo);
        Long accountTradeFlowId = flow.getId();
        accountComboFlowDetailService.recordDetailForCharge(shopId, userId, accountTradeFlowId, combo);
        return flow;
    }

    @Override
    public AccountTradeFlow recordFlowForCardCharge(Long shopId, Long userId, MemberCardChargeVo memberCardChargeVo, AccountInfo accountInfo, MemberCard memberCard) {
        AccountTradeFlow accountTradeFlow = accountTradeFlowService.recordFlowForCardCharge(userId,shopId, memberCardChargeVo, accountInfo, memberCard);
        Long accountTradeFlowId = accountTradeFlow.getId();
        accountCardFlowDetailService.recordDetailForCharge(memberCardChargeVo.getAmount(), shopId, accountInfo, memberCard, accountTradeFlowId,userId);
        return accountTradeFlow;
    }

    @Override
    public AccountTradeFlow recordRevertFlowForCardCharge(Long shopId, Long userId, AccountTradeFlow flow, AccountCardFlowDetail detail) {
        MemberCard memberCard = memberCardService.findById(detail.getCardId());
        AccountTradeFlow reversFlow = accountTradeFlowService.recordRevertFlowForCardCharge(shopId, userId, flow, detail,memberCard );
        accountCardFlowDetailService.recordReverseDetailForCharge(shopId,userId,reversFlow,memberCard);
        return reversFlow;
    }

    @Override
    public AccountTradeFlow recordFlowForHandleMemberCard(MemberGrantVo memberGrantVo, AccountInfo accountInfo, MemberCard memberCard, Long shopId, Long userId) {

        MemberCardInfo cardInfo = memberCardInfoService.findById(memberGrantVo.getMemberCardInfoId());
        memberCard.setMemberCardInfo(cardInfo);

        AccountTradeFlow accountTradeFlow = accountTradeFlowService.recordFlowForHandleMemberCard(memberGrantVo, accountInfo, memberCard,shopId ,userId );
        Long flowId = accountTradeFlow.getId();
        accountCardFlowDetailService.recordDetailForHandle(cardInfo.getInitBalance(),
                shopId,accountInfo, memberCard, flowId,userId);
        return accountTradeFlow;
    }

    @Override
    public AccountTradeFlow recordFlowForCardBack(BackCardVo backCardVo, MemberCard memberCard) {
        Long shopId = backCardVo.getShopId();
        Long userId = backCardVo.getUserId();
        AccountTradeFlow flow = accountTradeFlowService.recordFlowForCardBack(backCardVo, memberCard);
        accountCardFlowDetailService.recordDetailForBack(shopId, userId, memberCard, flow.getId());
        return flow;
    }

    @Override
    public AccountTradeFlow recordReverseFlowForComboRecharge(ComboRechargeReverseBo comboRechargeReverseBo) {
        Long shopId = comboRechargeReverseBo.getShopId();
        Long userId = comboRechargeReverseBo.getUserId();
        String userName = comboRechargeReverseBo.getUserName();
        AccountTradeFlow flow = comboRechargeReverseBo.getFlow();
        //1.查详情
        Long flowId = flow.getId();
        List<AccountComboFlowDetail> detailList = accountComboFlowDetailService.findByFlowId(shopId, flowId);
        //2.回滚主流水
        StringBuilder comboExplainSB = new StringBuilder();
        if (!CollectionUtils.isEmpty(detailList)) {
            Iterator<AccountComboFlowDetail> iterator = detailList.iterator();
            while (iterator.hasNext()) {
                AccountComboFlowDetail detail = iterator.next();
                comboExplainSB.append(detail.getServiceName()).append("-").append(detail.getChangeCount());
                if (iterator.hasNext()) {
                    comboExplainSB.append(",");
                }
            }
        }
        String comboExplain = comboExplainSB.toString();
        AccountTradeFlow reverseFlow = accountTradeFlowService.recordReverseFlowForComboRecharge(shopId, userId, userName, flow, comboExplain);
        //3.回滚详情
        if (!CollectionUtils.isEmpty(detailList)) {
            accountComboFlowDetailService.recordRevertDetailForRecharge(userId,shopId,detailList,reverseFlow.getId());
        }
        return reverseFlow;
    }

    @Override
    public AccountTradeFlow recordReverseFlowForCouponRecharge(CouponRechargeReverseBo couponRechargeReverseBo) {
        Long shopId = couponRechargeReverseBo.getShopId();
        Long userId = couponRechargeReverseBo.getUserId();
        AccountTradeFlow flow = couponRechargeReverseBo.getFlow();
        Long flowId = flow.getId();
        String userName = couponRechargeReverseBo.getUserName();
        //1.查详情
        List<AccountCouponFlowDetail> detailList = accountCouponFlowDetailService.findByFlowId(shopId, flowId);
        //2.回滚主流水
        Map<String,Integer> detailMap = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(detailList)) {
            Iterator<AccountCouponFlowDetail> iterator = detailList.iterator();
            while (iterator.hasNext()) {
                AccountCouponFlowDetail detail = iterator.next();
                String couponName = detail.getCouponName();
                if (detailMap.containsKey(couponName)) {
                    int newValue = detailMap.get(couponName) + 1;
                    detailMap.put(couponName,newValue);
                }else {
                    detailMap.put(couponName,1);
                }
            }

        }
        StringBuilder couponExplainSb = new StringBuilder();
        if (!MapUtils.isEmpty(detailMap)) {
            Set<Map.Entry<String, Integer>> entries = detailMap.entrySet();
            Iterator<Map.Entry<String, Integer>> iterator = entries.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Integer> item = iterator.next();
                couponExplainSb.append(item.getKey()).append("-").append(item.getValue());
                if (iterator.hasNext()) {
                    couponExplainSb.append(",");
                }
            }

        }
        String couponExpalin = couponExplainSb.toString();
        AccountTradeFlow reverseFlow = accountTradeFlowService.recordReverseFlowForCouponRecharge(shopId, userId, userName, flow, couponExpalin);
        //3.回滚详情
        if (!CollectionUtils.isEmpty(detailList)) {
            accountCouponFlowDetailService.recordRevertDetailForRecharge(userId,shopId,detailList,reverseFlow.getId());
        }
        //4.回滚套餐流水
        accountSuiteService.recordReverseSuiteForRecharge(shopId, userId, flowId, reverseFlow);
        return reverseFlow;
    }

    @Override
    public AccountTradeFlow recordReverseFlowForCardGrant(CardGrantReverseBo cardGrantReverseBo) {
        Long shopId = cardGrantReverseBo.getShopId();
        Long userId = cardGrantReverseBo.getUserId();
        String userName = cardGrantReverseBo.getUserName();
        AccountTradeFlow flow = cardGrantReverseBo.getFlow();
        //1.查明细
        Long flowId = flow.getId();
        AccountCardFlowDetail flowDetail = accountCardFlowDetailService.findByFlowId(shopId, flowId);
        BigDecimal changeAmount = flowDetail.getChangeAmount();
        BigDecimal balance = BigDecimal.ZERO;
        //2.回滚主流水
        String cardExpalin = changeAmount.negate().toString();
        AccountTradeFlow reverseFlow = accountTradeFlowService.recordReverseFlowForCardGrant(shopId, userId, userName, flow, cardExpalin,balance);
        //3.回滚明细
        Long reverseFlowId = reverseFlow.getId();
        accountCardFlowDetailService.recordReverseDetailForGrant(shopId,userId, reverseFlowId, balance, flowDetail);


        return null;
    }

    @Override
    public void recordFlowForCouponImport(List<AccountCoupon> couponList, Long shopId, Long userId) {
        if (!CollectionUtils.isEmpty(couponList)) {
            for (AccountCoupon coupon : couponList) {
                AccountTradeFlow flow = accountTradeFlowService.recordFlowForCouponImport(coupon, shopId, userId);
                accountCouponFlowDetailService.recordDetailForCouponImport(coupon, shopId, userId, flow.getId());
                coupon.setFlowId(flow.getId());
                coupon.setFlowSn(flow.getFlowSn());
                coupon.setOperatorName(flow.getOperatorName());
                accountCouponService.update(coupon);
            }
        }
    }

    @Override
    public void recordFlowForComboImport(AccountCombo combo, Long shopId, Long userId) {
        AccountTradeFlow flow = accountTradeFlowService.recordFlowForComboImport(combo, shopId, userId);
        accountComboFlowDetailService.recordFlowForComboImport(combo, shopId, userId, flow.getId());
    }

    @Override
    public List<AccountTradeFlow> listCardFlowByCardId(Long shopID, Long memberCardId) {
        List<AccountCardFlowDetail> details = accountCardFlowDetailService.listByCardId(shopID, memberCardId);
        List<Long> flowIds = Lists.transform(details, new Function<AccountCardFlowDetail, Long>() {
            @Override
            public Long apply(AccountCardFlowDetail input) {
                return input.getAccountTradeFlowId();
            }
        });

        List<AccountTradeFlow> flowList = accountTradeFlowService.listByIds(shopID, flowIds);
        //增加工单流水id by 辉辉大侠
        List<Long> orderIds = Lists.newArrayList();
        for(AccountTradeFlow flow:flowList) {
            if (isOrderIdValid(flow)) {
                orderIds.add(flow.getOrderId());
            }
        }
        if (orderIds.size() > 0) {
            List<OrderInfo> orderInfos = this.orderInfoService.selectByIdsAndShopId(shopID, orderIds);
            Map<Long, String> orderIdSnMap = Maps.newHashMap();
            for(OrderInfo info:orderInfos) {
                orderIdSnMap.put(info.getId(), info.getOrderSn());
            }
            for(AccountTradeFlow flow:flowList) {
                if (isOrderIdValid(flow)) {
                    flow.setOrderSn(orderIdSnMap.get(flow.getOrderId()));
                }
            }
        }

        return flowList;

    }

    private boolean isOrderIdValid(AccountTradeFlow flow) {
        return flow.getOrderId() != null && !Long.valueOf(0).equals(flow.getOrderId());
    }

    @Override
    public List<AccountTradeFlow> listComboFlowByComboId(Long shopID, Long comboId) {
        List<AccountComboFlowDetail> details = accountComboFlowDetailService.listByComboId(shopID, comboId);
        List<Long> flowIds = Lists.transform(details, new Function<AccountComboFlowDetail, Long>() {
            @Override
            public Long apply(AccountComboFlowDetail input) {
                return input.getAccountTradeFlowId();
            }
        });
        Set<Long> distinctFlowIds = new HashSet<>(flowIds);
        List<AccountTradeFlow> flowList = accountTradeFlowService.listByIds(shopID, distinctFlowIds);
        return flowList;
    }
}
