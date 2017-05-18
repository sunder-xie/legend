package com.tqmall.legend.facade.account.impl;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.DateUtil;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.legend.biz.account.*;
import com.tqmall.legend.biz.customer.CustomerCarRelService;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.legend.biz.customer.CustomerService;
import com.tqmall.legend.biz.order.OrderInfoService;
import com.tqmall.legend.biz.order.vo.OrderDiscountFlowVo;
import com.tqmall.legend.entity.account.*;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.customer.CustomerCarRel;
import com.tqmall.legend.entity.order.OrderInfo;
import com.tqmall.legend.enums.order.OrderDiscountTypeEnum;
import com.tqmall.legend.facade.account.AccountFacadeService;
import com.tqmall.legend.facade.account.MemberFacadeService;
import com.tqmall.legend.facade.account.vo.MemberCardBo;
import com.tqmall.legend.facade.account.vo.MemberGrantVo;
import com.tqmall.legend.facade.account.vo.MemberInfoVo;
import com.tqmall.legend.facade.order.OrderDiscountFlowFacade;
import com.tqmall.wheel.helper.BeanMapper;
import com.tqmall.wheel.lang.Langs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by wanghui on 6/2/16.
 */
@Service
@Slf4j
public class MemberFacadeServiceImpl implements MemberFacadeService {

    @Autowired
    private MemberCardInfoService memberCardInfoService;
    @Autowired
    private CardServiceRelService cardServiceRelService;
    @Autowired
    private CardGoodsRelService cardGoodsRelService;
    @Autowired
    private MemberCardService memberCardService;
    @Autowired
    private CustomerCarService customerCarService;
    @Autowired
    private AccountInfoService accountInfoService;
    @Autowired
    private AccountFlowService accountFlowService;
    @Autowired
    private CustomerCarRelService customerCarRelService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private AccountTradeFlowService accountTradeFlowService;
    @Autowired
    private AccountCardFlowDetailService accountCardFlowDetailService;
    @Autowired
    private AccountFacadeService accountFacadeService;
    @Autowired
    private OrderDiscountFlowFacade orderDiscountFlowFacade;
    @Autowired
    private OrderInfoService orderInfoService;

    @Override
    public void createMemberCardInfo(MemberInfoVo memberInfoVo) {
        if (log.isInfoEnabled()) {
            log.info("创建会员卡类型.{}", LogUtils.objectToString(memberInfoVo));
        }
        final MemberCardInfo memberCardInfo = memberInfoVo.getMemberCardInfo();
        this.memberCardInfoService.saveMemberCardInfo(memberCardInfo);

        /**
         * 会员卡关联部分服务折扣
         */
        if(Integer.valueOf(2).equals(memberCardInfo.getServiceDiscountType())) {
            if(CollectionUtils.isNotEmpty(memberInfoVo.getCardServiceRelList())) {//服务部分折扣
                /**
                 * 增加服务增加会员卡id
                 */
                List<CardServiceRel> serviceRelList = Lists.transform(memberInfoVo.getCardServiceRelList(), new Function<CardServiceRel, CardServiceRel>() {
                    @Override
                    public CardServiceRel apply(CardServiceRel input) {
                        input.setCardInfoId(memberCardInfo.getId());
                        return input;
                    }
                });

                this.cardServiceRelService.batchSave(serviceRelList);
            }
        }

        /**
         * 会员卡关联部分物料折扣
         */
        if(Integer.valueOf(2).equals(memberCardInfo.getGoodDiscountType())) {
            if (CollectionUtils.isNotEmpty(memberInfoVo.getCardGoodsRelList())) {
                /**
                 * 增加物料-会员卡关联id
                 */
                List<CardGoodsRel> goodsRelList = Lists.transform(memberInfoVo.getCardGoodsRelList(), new Function<CardGoodsRel, CardGoodsRel>() {
                    @Override
                    public CardGoodsRel apply(CardGoodsRel input) {
                        input.setCardInfoId(memberCardInfo.getId());
                        return input;
                    }
                });
                this.cardGoodsRelService.batchSave(goodsRelList);
            }
        }
    }

    @Override
    public void updateMemberCardInfo(MemberInfoVo memberInfoVo) {

    }

    @Override
    public void disableMemberCardType(Long shopId, Long memberTypeId) {

    }

    @Override
    public void enableMemberCardType(Long shopId, Long memberTypeId) {

    }

    @Override
    public List<MemberCard> getMemberCardByCarLicense(Long shopId, String carLicense) {
        Assert.notNull(shopId, "门店id不能为空.");
        Assert.hasText(carLicense, "车牌号不能为空.");
        CustomerCar customerCar = this.customerCarService.selectByLicenseAndShopId(carLicense, shopId);

        if (customerCar == null) {
            if (log.isWarnEnabled()) {
                log.warn("根据车牌查找不到对应的车辆信息,门店id:{},车牌:{}", shopId, carLicense);
            }
            return null;
        }
        return getAvailableListByCarId(shopId, customerCar.getId());
    }

    @Override
    public AccountTradeFlow grantMemberCard(MemberGrantVo grantVo, Long shopId, Long userId, String userName) throws BizException {

        if (Strings.isNullOrEmpty(grantVo.getCardNumber())) {
            throw new BizException("会员卡号不能为空");
        }

        if(this.memberCardService.isExistCardNumber(shopId, grantVo.getCardNumber(),null)) {
            throw new BizException("会员卡号已存在.");
        }

        if (!this.memberCardService.grantMemberCardAble(shopId, grantVo.getAccountId(),grantVo.getMemberCardInfoId())) {
            throw new BizException("该账户已绑定会员卡已达最大限额或已经办了该类型理会员卡.");
        }

        MemberCardInfo memberCardInfo = this.memberCardInfoService.findById(grantVo.getMemberCardInfoId());

        if (memberCardInfo == null) {
            log.error("查找不到对应的会员卡类型.param:{}", LogUtils.objectToString(grantVo));
            throw new RuntimeException("查找不到对应的会员卡类型");
        }

        MemberCard memberCard = new MemberCard();
        memberCard.setCreator(userId);
        memberCard.setGmtCreate(new Date());
        memberCard.setShopId(shopId);
        memberCard.setAccountId(grantVo.getAccountId());
        memberCard.setBalance(memberCardInfo.getInitBalance());
        /**
         * 初始化会员卡基本信息
         */
        memberCard.setCardTypeId(memberCardInfo.getId());
        memberCard.setCardTypeName(memberCardInfo.getTypeName());
        memberCard.setCardNumber(grantVo.getCardNumber());
        memberCard.setExpenseAmount(BigDecimal.ZERO);
        memberCard.setExpenseCount(0);
        memberCard.setReceiver(grantVo.getReceiver());
        memberCard.setReceiverName(grantVo.getReceiverName());
        memberCard.setPublisher(userId);
        memberCard.setPublisherName(userName);

        /**
         * 设置会员卡过期时间
         */
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, memberCardInfo.getEffectivePeriodDays().intValue());
        memberCard.setExpireDate(calendar.getTime());

        /**
         * 会员卡内余额信息
         */
        if (memberCardInfo.getInitBalance() != null && memberCardInfo.getInitBalance().compareTo(BigDecimal.ZERO) > 0) {
            memberCard.setBalance(memberCardInfo.getInitBalance());
            memberCard.setDepositAmount(memberCardInfo.getInitBalance());
            memberCard.setDepositCount(1);
        } else {
            memberCard.setBalance(BigDecimal.ZERO);//会员卡余额
            memberCard.setDepositAmount(BigDecimal.ZERO);
            memberCard.setDepositCount(0);
        }
        AccountInfo accountInfo = accountInfoService.getAccountInfoById(grantVo.getAccountId());
        if(accountInfo == null){
            throw new RuntimeException("accountInfo == null");
        }

        if(!this.memberCardService.saveMemberCard(memberCard) ) {
            log.error("持久化会员卡信息失败.");
            throw new RuntimeException("持久化会员卡信息失败");
        }
        AccountTradeFlow flow = accountFlowService.recordFlowForHandleMemberCard(grantVo, accountInfo, memberCard, shopId,userId );
        flow.setMemberCard(memberCard);
        return flow;
    }

    @Override
    public List<MemberCard> getMemberCardByCustomerId(Long shopId, Long customerId) {
        AccountInfo accountInfo = this.accountInfoService.getAccountInfoByCustomerIdAndShopId(shopId, customerId);
        if (accountInfo == null) {
            if (log.isWarnEnabled()) {
                log.warn("根据客户未能找到对应的账户信息.门店id:{},客户id:{}", shopId,customerId);
            }
            return null;
        }
        return this.memberCardService.getUnExpiredMemberCardListByAccountId(shopId, accountInfo.getId());
    }

    /**
     * 根据客户手机号获取关联的会员卡信息
     *
     * @param shopId
     * @param mobile
     * @return
     */
    @Override
    public List<MemberCard> getMemberCardByMobile(Long shopId, String mobile) {
        Assert.isTrue(Langs.isNotBlank(mobile));

        List<Customer> customerList = customerService.getCustomerByMobile(mobile, shopId);
        if (Langs.isEmpty(customerList)) {
            return Collections.emptyList();
        }
        if (customerList.size() > 1) {
            log.error("手机号：{}对应{}个客户", mobile, customerList.size());
            throw new BizException("手机号对应多个客户，手机号：" + mobile);
        }
        return getMemberCardByCustomerId(shopId, customerList.get(0).getId());
    }

    /**
     * 不要使用NPE去做流程扭转
     * @param shopId
     * @param userId
     * @param flowId
     */
    @Override
    public void reverseRecharge(Long shopId, Long userId, Long flowId) {
        AccountTradeFlow accountTradeFlow = null;
        AccountCardFlowDetail accountCardFlowDetail=null;
        try{
            accountTradeFlow = memberCardService.findFlowByFlowId(shopId,flowId);
            Date gmtCreate = accountTradeFlow.getGmtCreate();
            if (DateUtil.addDate(gmtCreate,3).before(new Date())) {
                throw new BizException("超过3天,不能撤销");
            }
        }catch (NullPointerException e){
            log.error("充值撤销时未找到主流水中的flowId:{}",flowId);
            throw new BizException("充值撤销时未找到主流水中的flowId");
        }
        try{
            accountCardFlowDetail = memberCardService.findCardFlowByFlowId(shopId,flowId);
        }catch (NullPointerException e){
            log.error("充值撤销时未找到会员卡流水中的flowId:{}",flowId);
            throw new BizException("充值撤销时未找到会员卡流水中的flowId");
        }

        MemberCard card = memberCardService.getUpgradedMemberCard(shopId, accountCardFlowDetail.getCardId());
        if("Y".equals(card.getIsDeleted())) {
            throw new BizException("会员卡已退卡，无法撤销");
        }

        memberCardService.revertRecharge(shopId,userId,accountCardFlowDetail.getCardId(),accountCardFlowDetail.getChangeAmount());

        accountFlowService.recordRevertFlowForCardCharge(shopId,userId,accountTradeFlow,accountCardFlowDetail );

    }

    /**
     * 查询可用的会员卡列表包括自己的和关联别人的
     *
     * @param shopId
     * @param carId
     * @return
     */
    @Override
    public List<MemberCard> getAvailableListByCarId(Long shopId, Long carId) {
        Assert.isTrue(shopId != null && shopId > 0);
        Assert.isTrue(carId != null && carId > 0);

        List<Long> accountIds = Lists.newArrayList();
        AccountInfo accountInfo = accountInfoService.getAccountInfoByCarIdAndShopId(shopId, carId);
        if (accountInfo != null) {
            accountIds.add(accountInfo.getId());
        }
        List<CustomerCarRel> customerCarRels = customerCarRelService.listByCarId(shopId, carId);
        if (Langs.isNotEmpty(customerCarRels)) {
            for (CustomerCarRel customerCarRel : customerCarRels) {
                accountIds.add(customerCarRel.getAccountId());
            }
        }
        return memberCardService.getUnExpiredMemberCardListByAccountIds(shopId, accountIds);
    }

    /**
     * 根据工单id查询上一次结算使用的会员卡信息
     *
     * @param shopId
     * @param orderId
     * @return
     */
    @Override
    public MemberCardBo getUsedForOrderLastSettle(Long shopId, Long orderId) {
        Assert.isTrue(shopId != null && shopId > 0);
        Assert.isTrue(orderId != null && orderId > 0);
        MemberCardBo memberCardBo = null;
        // 查询工单折扣使用的会员卡
        List<OrderDiscountFlowVo> discountFlowVoList = orderDiscountFlowFacade.getOrderDiscountFlow(orderId, shopId);
        Long memberCardId = null;
        if (Langs.isNotEmpty(discountFlowVoList)) {
            for (OrderDiscountFlowVo orderDiscountFlowVo : discountFlowVoList) {
                if (OrderDiscountTypeEnum.MEMBERCOUPON.getCode() == orderDiscountFlowVo.getDiscountType()) {
                    memberCardId = orderDiscountFlowVo.getRelId();
                }
            }
        }
        if (memberCardId == null || memberCardId <= 0) {
            // 查询工单结算使用的会员卡
            List<AccountTradeFlow> accountTradeFlows = accountTradeFlowService.selectByOrderId(shopId, orderId);
            if (Langs.isNotEmpty(accountTradeFlows)) {
                List<Long> flowIds = Lists.newArrayList();
                for (AccountTradeFlow accountTradeFlow : accountTradeFlows) {
                    Integer tradeType = accountTradeFlow.getTradeType();
                    if (tradeType != null && AccountTradTypeEnum.MEMBER_CARD.getCode() == tradeType) {
                        flowIds.add(accountTradeFlow.getId());
                    }
                }
                if (Langs.isNotEmpty(flowIds)) {
                    AccountCardFlowDetail accountCardFlowDetail = accountCardFlowDetailService.findByFlowId(shopId, flowIds.get(0));
                    if (accountCardFlowDetail != null) {
                        memberCardId = accountCardFlowDetail.getCardId();
                    }
                }
            }
        }
        if (memberCardId != null && memberCardId > 0) {
            MemberCard memberCard = memberCardService.findByIdContainDeleted(shopId, memberCardId);
            if (memberCard != null) {
                MemberCardInfo memberCardInfo = memberCardInfoService.findById(memberCard.getShopId(), memberCard.getCardTypeId());
                if (memberCardInfo != null) {
                    memberCard.setCardTypeName(memberCardInfo.getTypeName());
                    memberCard.setMemberCardInfo(memberCardInfo);
                }

                AccountInfo accountInfo = accountInfoService.getAccountInfoById(memberCard.getAccountId());
                if (accountInfo != null) {
                    memberCard.setCustomerName(accountInfo.getCustomerName());
                    memberCard.setMobile(accountInfo.getMobile());
                }

                boolean isBelongOther = true;
                OrderInfo orderInfo = orderInfoService.selectById(orderId);
                if (orderInfo != null) {
                    Map<Long, Long> accountIdAndCustomerIdMap = accountFacadeService.getAccountIdAndCustomerIdByCarId(shopId, orderInfo.getCustomerCarId());
                    if (!org.springframework.util.CollectionUtils.isEmpty(accountIdAndCustomerIdMap)) {
                        Set<Long> accountIds = accountIdAndCustomerIdMap.keySet();
                        isBelongOther = !accountIds.contains(memberCard.getAccountId());
                    }
                    memberCardBo = BeanMapper.mapIfPossible(memberCard, MemberCardBo.class);
                    if (memberCardBo != null) {
                        memberCardBo.setBelongOther(isBelongOther);
                    }
                }
            }
        }
        return memberCardBo;
    }

    /**
     * 判断会员卡是否属于其他人（不是归属和关联账户下的）
     *
     * @param shopId
     * @param memberCardId
     * @param customerCarId
     * @return
     */
    @Override
    public boolean isBelongOther(Long shopId, Long memberCardId, Long customerCarId) {
        MemberCard memberCard = memberCardService.findById(memberCardId);
        if (memberCard == null) {
            log.error("会员卡不存在，id = {}", memberCardId);
            throw new BizException("会员卡不存在");
        }

        Map<Long, Long> accountIdAndCustomerIdMap = accountFacadeService.getAccountIdAndCustomerIdByCarId(shopId, customerCarId);
        if (org.springframework.util.CollectionUtils.isEmpty(accountIdAndCustomerIdMap)) {
            return true;
        }
        Set<Long> accountIds = accountIdAndCustomerIdMap.keySet();
        return !accountIds.contains(memberCard.getAccountId());
    }

}
