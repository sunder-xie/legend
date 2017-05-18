package com.tqmall.legend.biz.account.impl;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.BizTemplate;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.legend.biz.account.*;
import com.tqmall.legend.biz.account.bo.RechargeComboBo;
import com.tqmall.legend.biz.account.vo.BackCardVo;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.order.OrderInfoService;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.shop.SerialNumberService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.dao.account.AccountSuiteDao;
import com.tqmall.legend.dao.account.AccountTradeFlowDao;
import com.tqmall.legend.dao.account.CouponSuiteDao;
import com.tqmall.legend.entity.account.*;
import com.tqmall.legend.entity.account.vo.AccountCouponVo;
import com.tqmall.legend.entity.account.vo.CouponVo;
import com.tqmall.legend.entity.account.vo.MemberCardChargeVo;
import com.tqmall.legend.entity.order.OrderInfo;
import com.tqmall.legend.entity.order.SerialTypeEnum;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.facade.account.vo.MemberGrantVo;
import com.tqmall.legend.facade.account.vo.MemberUpgradeVo;
import com.tqmall.legend.facade.discount.bo.DiscountSelectedBo;
import com.tqmall.legend.facade.discount.bo.SelectedCardBo;
import com.tqmall.legend.facade.discount.bo.SelectedComboBo;
import com.tqmall.legend.facade.discount.bo.SelectedCouponBo;
import com.tqmall.wheel.lang.Langs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Created by twg on 16/6/7.
 */
@Service
@Slf4j
public class AccountTradeFlowServiceImpl extends BaseServiceImpl implements AccountTradeFlowService {
    @Autowired
    private AccountTradeFlowDao accountTradeFlowDao;
    @Autowired
    private SerialNumberService serialNumberService;
    @Autowired
    private AccountCouponService accountCouponService;
    @Autowired
    private ComboServiceRelService comboServiceRelService;
    @Autowired
    private AccountInfoService accountInfoService;
    @Autowired
    private ComboInfoServiceRelService comboInfoServiceRelService;
    @Autowired
    private AccountCardFlowDetailService accountCardFlowDetailService;
    @Autowired
    private CouponInfoService couponInfoService;
    @Autowired
    private AccountCouponFlowDetailService accountCouponFlowDetailService;
    @Autowired
    private CouponSuiteDao couponSuiteDao;
    @Autowired
    private AccountSuiteDao accountSuiteDao;
    @Autowired
    private ShopManagerService shopManagerService;
    @Autowired
    private MemberCardInfoService memberCardInfoService;
    @Autowired
    private OrderInfoService orderInfoService;
    @Autowired
    private MemberCardService memberCardService;
    @Autowired
    private AccountComboService accountComboService;


    @Override
    public boolean insert(AccountTradeFlow accountTradeFlow) {
        return accountTradeFlowDao.insert(accountTradeFlow) > 0 ? true : false;
    }

    @Override
    public List<AccountTradeFlow> getAccountTradFlows(Long shopId, Long accountId) {
        Map param = Maps.newHashMap();
        param.put("shopId",shopId);
        param.put("accountId",accountId);
        return accountTradeFlowDao.select(param);
    }

    @Override
    public Page<AccountTradeFlow> getAccountTradFlowsByPage(Pageable pageable, Map<String, Object> params) {
        Integer totalSize = accountTradeFlowDao.selectCount(params);

        List<AccountTradeFlow> retList = Lists.newArrayList();

        if (pageable.getSort() != null) {
            Iterator<Sort.Order> iterator = pageable.getSort().iterator();
            List<String> sorts = Lists.newArrayList();
            while (iterator.hasNext()) {
                Sort.Order order = iterator.next();
                sorts.add(order.getProperty() + " " + order.getDirection().name());
            }
            params.put("sorts", sorts);
        }

        PageRequest pageRequest = new PageRequest((pageable.getPageNumber() < 1 ? 1
                : pageable.getPageNumber()) - 1, pageable.getPageSize() < 1 ? 1
                : pageable.getPageSize(), pageable.getSort());
        params.put("offset", pageRequest.getOffset());
        params.put("limit", pageRequest.getPageSize());
        List<AccountTradeFlow> accountTradeFlows = accountTradeFlowDao.select(params);
        for (AccountTradeFlow accountTradeFlow : accountTradeFlows) {
            Integer consumeType = accountTradeFlow.getConsumeType();
            switch (consumeType){
                case 1: accountTradeFlow.setConsumeTypeName(AccountConsumeTypeEnum.CHARGE.getAlias());break;
                case 2: accountTradeFlow.setConsumeTypeName(AccountConsumeTypeEnum.CHARGE_REVERT.getAlias());break;
                case 3: accountTradeFlow.setConsumeTypeName(AccountConsumeTypeEnum.CONSUME.getAlias());break;
                case 4: accountTradeFlow.setConsumeTypeName(AccountConsumeTypeEnum.CONSUME_REVERT.getAlias());break;
                case 5: accountTradeFlow.setConsumeTypeName(AccountConsumeTypeEnum.HANDLE_CARD.getAlias());break;
                case 6: accountTradeFlow.setConsumeTypeName(AccountConsumeTypeEnum.CANCEL_CARD.getAlias());break;
                case 7: accountTradeFlow.setConsumeTypeName(AccountConsumeTypeEnum.CONSUME_MODIFY.getAlias());break;
                case 8: accountTradeFlow.setConsumeTypeName(AccountConsumeTypeEnum.INIT.getAlias());break;
                case 9: accountTradeFlow.setConsumeTypeName(AccountConsumeTypeEnum.GRANT_REVERSE.getAlias());break;
                case 10:accountTradeFlow.setConsumeTypeName(AccountConsumeTypeEnum.UPGRADE_CARD.getAlias());break;
                default:accountTradeFlow.setConsumeTypeName("-");
            }
            Integer tradeType = accountTradeFlow.getTradeType();
            switch (tradeType){
                case 1: accountTradeFlow.setTradeTypeName(AccountTradTypeEnum.COUPON.getAlias());break;
                case 2: accountTradeFlow.setTradeTypeName(AccountTradTypeEnum.COMBO.getAlias());break;
                case 3: accountTradeFlow.setTradeTypeName(AccountTradTypeEnum.MEMBER_CARD.getAlias());break;
                case 4: accountTradeFlow.setTradeTypeName(AccountTradTypeEnum.COMPOUND_TYPE.getAlias());break;
                default:accountTradeFlow.setTradeTypeName("-");
            }
            retList.add(accountTradeFlow);
        }

        Set<Long> orderIdSet = new HashSet<>();
        if (!CollectionUtils.isEmpty(retList)) {
            for (AccountTradeFlow item : retList) {
                if (item.getOrderId() != null) {
                    orderIdSet.add(item.getOrderId());
                }
            }
        }
        List<Long> orderIdList = new ArrayList<>(orderIdSet);

        if (CollectionUtils.isNotEmpty(orderIdList)) {
            List<OrderInfo> orderInfoList = orderInfoService
                    .selectByIdsAndShopId((Long) params.get("shopId"), orderIdList);

            Map<Long, String> orderMap = Maps.newHashMap();
            if (!CollectionUtils.isEmpty(orderIdList)) {
                for (OrderInfo item : orderInfoList) {
                    orderMap.put(item.getId(),item.getOrderSn());
                }
            }

            if (!CollectionUtils.isEmpty(retList)) {
                for (AccountTradeFlow item : retList) {
                    if (item.getOrderId() != null) {
                        item.setOrderSn(orderMap.get(item.getOrderId()));
                    }
                }
            }
        }

        for (AccountTradeFlow item : retList) {
            if (item.getTradeType().equals(AccountTradTypeEnum.MEMBER_CARD.getCode())) {
                String cardExplainRawStr = item.getCardExplain();
                if (cardExplainRawStr == null) {
                    item.setCardExplain(BigDecimal.ZERO.toString());
                } else {

                    String cardExplain = cardExplainRawStr.trim();
                    // if cardexplain is number, then format "xx.00"
                    try {
                        BigDecimal changeAmount = new BigDecimal(cardExplain);
                        item.setCardExplain(changeAmount.setScale(2, BigDecimal.ROUND_HALF_DOWN).toString());
                    } catch (NumberFormatException e) {

                    }
                }

            }
        }

        DefaultPage<AccountTradeFlow> page = new DefaultPage<>(retList, pageRequest, totalSize);
        return page;
    }

    @Override
    public void batchInsert(List<AccountTradeFlow> accountTradeFlowList) {
        super.batchInsert(accountTradeFlowDao, accountTradeFlowList, 1000);
    }

    @Override
    public Integer getTradeType(boolean isCouponUsed, boolean isComboUsed, boolean isCardUsed) {
        Integer tradeType = 0;
        if (isCouponUsed && !isComboUsed && !isCardUsed) {
            tradeType = 1;
        } else if (!isCouponUsed && isComboUsed && !isCardUsed) {
            tradeType = 2;
        } else if (!isCouponUsed && !isComboUsed && isCardUsed) {
            tradeType = 3;
        } else {
            tradeType = 4;
        }
        return tradeType;
    }

    @Override
    public List<AccountTradeFlow> select(Map<String, Object> params) {
        return accountTradeFlowDao.select(params);
    }

    @Override
    public AccountTradeFlow generateAccountTradeBase(Long shopId, Long userId) {
        String flowSn = generateFlowSn(shopId, userId);
        ShopManager shopManager = null;
        if (userId != null) {
            shopManager = shopManagerService.selectByShopIdAndManagerIdDB(shopId, userId);
        }
        AccountTradeFlow accountTradeFlow = new AccountTradeFlow();
        accountTradeFlow.setCreator(userId);
        accountTradeFlow.setModifier(userId);
        accountTradeFlow.setShopId(shopId);
        accountTradeFlow.setFlowSn(flowSn);
        if(shopManager == null){
            accountTradeFlow.setOperatorName("");
        } else {
            accountTradeFlow.setOperatorName(shopManager.getName());
        }
        return  accountTradeFlow;
    }

    /**
     * 生成流水号
     * @param shopId
     * @param userId
     * @return
     */
    private String generateFlowSn(Long shopId, Long userId) {
        Map param = Maps.newHashMap();
        param.put("userId", userId);
        param.put("shopId", shopId);
        param.put("serialType", SerialTypeEnum.FLOW_ID.getCode());
        return serialNumberService.getSerialNumber(param);
    }

    @Override
    public void delete(Long shopId) {
        Map param = Maps.newHashMap();
        if(shopId != null){
            param.put("shopId",shopId);
        }
        accountTradeFlowDao.delete(param);

    }

    @Override
    public List<AccountTradeFlow> selectByOrderId(Long shopId, Long orderId) {
        Map param = Maps.newHashMap();
        param.put("shopId",shopId);
        param.put("orderId",orderId);
        param.put("isReversed", 0);
        return accountTradeFlowDao.select(param);
    }

    @Override
    public AccountTradeFlow findById(Long shopId, Long flowId) {
        Map param = Maps.newHashMap();
        param.put("shopId",shopId);
        param.put("id", flowId);
        List<AccountTradeFlow> accountTradeFlowList = accountTradeFlowDao.select(param);
        if (!CollectionUtils.isEmpty(accountTradeFlowList)) {
            return accountTradeFlowList.get(0);
        }
        return null;
    }

    @Override
    public AccountTradeFlow recordReversFlowForBillPay(Long shopId, Long userId, AccountTradeFlow flow, AccountCardFlowDetail detail, Long accountId,MemberCard memberCard) {
        flow.setIsReversed(1);
        flow.setModifier(userId);
        accountTradeFlowDao.updateById(flow);

        StringBuilder cardExplain = new StringBuilder();
        cardExplain.append("+").append(detail.getChangeAmount().negate().toString());
        AccountTradeFlow reverseFlow =  generateAccountTradeBase(shopId,userId);
        reverseFlow.setOrderId(flow.getOrderId());
        reverseFlow.setTradeType(flow.getTradeType());
        reverseFlow.setReverseId(flow.getId());
        reverseFlow.setConsumeType(4);
        reverseFlow.setCardBalance(memberCard.getBalance());
        reverseFlow.setCardExplain(cardExplain.toString());
        reverseFlow.setCustomerName(flow.getCustomerName());
        reverseFlow.setMobile(flow.getMobile());
        reverseFlow.setAccountId(accountId);
        reverseFlow.setGmtCreate(new Date());
        reverseFlow.setGmtModified(new Date());
        reverseFlow.setAmount(flow.getAmount().negate());
        accountTradeFlowDao.insert(reverseFlow);

        return reverseFlow;
    }

    @Override
    public AccountTradeFlow recordReverseFlowForBillConfirm(Long shopId, Long userId, AccountTradeFlow flow, List<AccountCouponFlowDetail> couponDetailList, List<AccountComboFlowDetail> comboDetailList) {

        StringBuilder couponExplain = new StringBuilder();
        if (!CollectionUtils.isEmpty(couponDetailList)) {
            for (AccountCouponFlowDetail item : couponDetailList) {
                couponExplain.append(item.getCouponName())
                        .append("+1; ");
            }
        }
        StringBuilder serviceExplain = new StringBuilder();
        if (!CollectionUtils.isEmpty(comboDetailList)) {
            for (AccountComboFlowDetail item : comboDetailList) {
                //
                serviceExplain.append(item.getServiceName())
                        .append("+")
                        .append(item.getChangeCount())
                        .append("; ");
            }
        }
        AccountTradeFlow reverseFlow =  generateAccountTradeBase(shopId, userId);
        reverseFlow.setOrderId(flow.getOrderId());
        reverseFlow.setTradeType(flow.getTradeType());
        reverseFlow.setReverseId(flow.getId());
        reverseFlow.setConsumeType(4);
        reverseFlow.setCouponExplain(couponExplain.toString());
        reverseFlow.setServiceExplain(serviceExplain.toString());
        reverseFlow.setCustomerName(flow.getCustomerName());
        reverseFlow.setMobile(flow.getMobile());
        reverseFlow.setAccountId(flow.getAccountId());
        accountTradeFlowDao.insert(reverseFlow);
        return reverseFlow;
    }

    @Override
    public void recordFlowForCouponCharge(AccountCouponVo accountCouponVo,AccountTradeFlow accountTradeFlow, Long shopId, Long creator, List<AccountCoupon> result) {
        Long accountId = accountCouponVo.getAccountId();
        Long accountTradeFlowId = accountTradeFlow.getId();
        accountCouponFlowDetailService.recordDetailForCharge(shopId, creator, accountTradeFlowId, result);
        Long couponSuiteId = accountCouponVo.getCouponSuiteId();
        if (couponSuiteId != null) {
            CouponSuite couponSuite = couponSuiteDao.selectById(couponSuiteId);
            /**
             * 储存套餐充值信息
             */
            AccountSuite accountSuite = new AccountSuite();
            accountSuite.setCreator(creator);
            accountSuite.setShopId(shopId);
            accountSuite.setCouponSource(0);
            accountSuite.setAccountId(accountId);
            accountSuite.setCouponSuiteId(couponSuiteId);
            accountSuite.setCouponSuiteName(couponSuite.getSuiteName());
            accountSuite.setFlowId(accountTradeFlowId);
            accountSuite.setFlowSn(accountTradeFlow.getFlowSn());
            accountSuite.setAmount(accountTradeFlow.getPayAmount());
            accountSuiteDao.insert(accountSuite);
        }
    }

    @Override
    public AccountTradeFlow recordTradeFlowForCouponCharge(AccountCouponVo accountCouponVo, Long shopId, Long creator, Long accountId) {
        List<CouponVo> couponVos = accountCouponVo.getCouponVos();
        List<Long> ids = new LinkedList<>();
        for (CouponVo couponVo : couponVos) {
            ids.add(couponVo.getId());
        }
        if (CollectionUtils.isEmpty(ids)) {
            throw new RuntimeException("ids is empty");
        }
        Long[] idss = new Long[0];
        idss =ids.toArray(idss);
        List<CouponInfo> couponInfos = couponInfoService.findByIds(shopId, idss);
        Map<Long, CouponInfo> couponInfoMap = new HashMap<>();
        for (CouponInfo couponInfo : couponInfos) {
            couponInfoMap.put(couponInfo.getId(), couponInfo);
        }
        AccountInfo accountInfo = accountInfoService.getAccountInfoById(accountId);
        AccountTradeFlow accountTradeFlow = generateAccountTradeBase(shopId,creator);
        accountTradeFlow.setTradeType(AccountTradTypeEnum.COUPON.getCode());
        accountTradeFlow.setPayAmount(accountCouponVo.getPayAmount());
        accountTradeFlow.setPaymentName(accountCouponVo.getPaymentName());
        accountTradeFlow.setPaymentId(accountCouponVo.getPaymentId());
        accountTradeFlow.setConsumeType(AccountConsumeTypeEnum.CHARGE.getCode());
        accountTradeFlow.setAccountId(accountId);
        accountTradeFlow.setCustomerName(accountInfo.getCustomerName());
        accountTradeFlow.setMobile(accountInfo.getMobile());
        Long couponSuiteId = accountCouponVo.getCouponSuiteId();
        if (couponSuiteId != null) {
            CouponSuite couponSuite = couponSuiteDao.selectById(couponSuiteId);
            accountTradeFlow.setAmount(couponSuite.getSalePrice());

        }
        /**
         * 组装优惠券信息
         */
        StringBuilder sb = new StringBuilder();
        for (CouponVo couponVo : couponVos) {
            CouponInfo couponInfo = couponInfoMap.get(couponVo.getId());
            sb.append(couponInfo.getCouponName()).append("+").append(couponVo.getNum()).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        accountTradeFlow.setCouponExplain(sb.toString());
        accountTradeFlowDao.insert(accountTradeFlow);
        return accountTradeFlow;
    }

    @Override
    public AccountTradeFlow recordFlowForComboCharge(RechargeComboBo rechargeComboBo, Long shopId, Long userId, AccountCombo combo) {
        AccountInfo accountInfo = accountInfoService
                .getAccountInfoById(rechargeComboBo.getAccountId());
        List<ComboInfoServiceRel> comboInfoServiceRelList = comboInfoServiceRelService
                .findByComboInfoId(shopId,rechargeComboBo.getComboInfoId());

        StringBuilder serviceExplain = new StringBuilder();
        if (!org.springframework.util.CollectionUtils.isEmpty(comboInfoServiceRelList)) {
            for (ComboInfoServiceRel item : comboInfoServiceRelList) {
                serviceExplain.append(item.getServiceName())
                        .append("+")
                        .append(item.getServiceCount().toString()).append("; ");
            }
            serviceExplain.substring(0, serviceExplain.length() - 1);
        }

        AccountTradeFlow accountTradeFlow = generateAccountTradeBase(shopId, userId);
        accountTradeFlow.setAmount(rechargeComboBo.getAmount());
        accountTradeFlow.setPayAmount(rechargeComboBo.getPayAmount());
        accountTradeFlow.setPaymentId(rechargeComboBo.getPaymentId());
        accountTradeFlow.setPaymentName(rechargeComboBo.getPaymentName());
        accountTradeFlow.setCustomerName(accountInfo.getCustomerName());
        accountTradeFlow.setMobile(accountInfo.getMobile());
        accountTradeFlow.setIsReversed(0);
        accountTradeFlow.setConsumeType(1);
        accountTradeFlow.setAccountId(rechargeComboBo.getAccountId());
        accountTradeFlow.setTradeType(2);
        accountTradeFlow.setServiceExplain(serviceExplain.toString());
        accountTradeFlow.setReceiver(rechargeComboBo.getRecieverId());
        accountTradeFlow.setReceiverName(rechargeComboBo.getRecieverName());
        accountTradeFlow.setGmtCreate(new Date());
        accountTradeFlow.setGmtModified(new Date());
        accountTradeFlowDao.insert(accountTradeFlow);

        return accountTradeFlow;
    }

    @Override
    public AccountTradeFlow recordFlowForCardCharge(Long userId, Long shopId, MemberCardChargeVo memberCardChargeVo, AccountInfo accountInfo, MemberCard memberCard) {
        AccountTradeFlow accountTradeFlow = generateAccountTradeBase(shopId,userId);
        accountTradeFlow.setRemark(memberCardChargeVo.getRemark());
        accountTradeFlow.setMobile(accountInfo.getMobile());
        accountTradeFlow.setAccountId(accountInfo.getId());
        accountTradeFlow.setPaymentId(memberCardChargeVo.getPaymentId());
        accountTradeFlow.setConsumeType(AccountConsumeTypeEnum.CHARGE.getCode());
        accountTradeFlow.setPaymentName(memberCardChargeVo.getPaymentName());
        accountTradeFlow.setCardExplain(memberCardChargeVo.getAmount().toString());
        accountTradeFlow.setCardBalance(memberCard.getBalance());
        accountTradeFlow.setCustomerName(accountInfo.getCustomerName());
        accountTradeFlow.setTradeType(AccountTradTypeEnum.MEMBER_CARD.getCode());
        accountTradeFlow.setAmount(memberCardChargeVo.getAmount());
        accountTradeFlow.setPayAmount(memberCardChargeVo.getPayAmount());
        accountTradeFlow.setReceiver(memberCard.getReceiver());
        accountTradeFlow.setReceiverName(memberCard.getReceiverName());
        accountTradeFlowDao.insert(accountTradeFlow);
        return  accountTradeFlow;
    }

    @Override
    public AccountTradeFlow recordRevertFlowForCardCharge(Long shopId, Long userId, AccountTradeFlow flow, AccountCardFlowDetail detail, MemberCard memberCard) {
        flow.setIsReversed(1);
        accountTradeFlowDao.updateById(flow);
        AccountTradeFlow reversFlow = generateAccountTradeBase(shopId,userId);
        reversFlow.setRemark(flow.getRemark());
        reversFlow.setMobile(flow.getMobile());
        reversFlow.setAccountId(flow.getAccountId());
        reversFlow.setPaymentId(flow.getPaymentId());
        reversFlow.setConsumeType(AccountConsumeTypeEnum.CHARGE_REVERT.getCode());
        reversFlow.setPaymentName(flow.getPaymentName());
        reversFlow.setCardExplain("-"+detail.getChangeAmount());
        reversFlow.setCardBalance(memberCard.getBalance());
        reversFlow.setCustomerName(flow.getCustomerName());
        reversFlow.setTradeType(AccountTradTypeEnum.MEMBER_CARD.getCode());
        reversFlow.setAmount(flow.getAmount().negate());
        reversFlow.setPayAmount(flow.getPayAmount().negate());
        reversFlow.setReverseId(flow.getId());
        reversFlow.setGmtCreate(new Date());
        reversFlow.setGmtModified(new Date());
        accountTradeFlowDao.insert(reversFlow);

        return reversFlow;
    }

    @Override
    public AccountTradeFlow recordFlowForBillPay(Long shopId, Long userId, Long orderId, BigDecimal cardPayAmount, AccountInfo accountInfo, MemberCard memberCard) {
        AccountTradeFlow accountTradeFlow = generateAccountTradeBase(shopId, userId);
        accountTradeFlow.setCustomerName(accountInfo.getCustomerName());
        accountTradeFlow.setMobile(accountInfo.getMobile());
        accountTradeFlow.setAccountId(accountInfo.getId());
        accountTradeFlow.setOrderId(orderId);
        accountTradeFlow.setTradeType(getTradeType(false,false,true));
        accountTradeFlow.setIsReversed(0);
        accountTradeFlow.setConsumeType(3);
        accountTradeFlow.setCardBalance(memberCard.getBalance());
        accountTradeFlow.setAmount(cardPayAmount.negate());
        accountTradeFlow.setCardExplain(cardPayAmount.negate().toString());
        accountTradeFlow.setGmtCreate(new Date());
        accountTradeFlow.setGmtModified(new Date());
        accountTradeFlowDao.insert(accountTradeFlow);

        Long tradeFlowId = accountTradeFlow.getId();
        accountCardFlowDetailService.recordDetailForConume(shopId, userId, cardPayAmount, memberCard, tradeFlowId);
        return accountTradeFlow;
    }

    @Override
    public Map<Long, Long> recordFlowForBillConfirm(Long shopId, Long userId, Long orderId, DiscountSelectedBo discountSelectedBo) {

        SelectedCardBo selectedCard = discountSelectedBo.getSelectedCard();
        boolean isMemberCardUsed = selectedCard != null;

        List<SelectedComboBo> selectedComboList = discountSelectedBo.getSelectedComboList();
        boolean isComboUsed = Langs.isNotEmpty(selectedComboList);

        List<SelectedCouponBo> selectedCouponList = discountSelectedBo.getSelectedCouponList();
        boolean isCouponUsed = Langs.isNotEmpty(selectedCouponList);

        Set<Long> accountIdSet = Sets.newHashSet();
        Map<Long, String> couponExplainMap = Maps.newHashMap();
        if (isCouponUsed) {
            couponExplainMap = getCouponExplainMap(selectedCouponList);
            accountIdSet.addAll(couponExplainMap.keySet());
        }
        Map<Long, String> serviceExplainMap = Maps.newHashMap();
        if (isComboUsed) {
            serviceExplainMap = getServiceExplainMap(selectedComboList);
            accountIdSet.addAll(serviceExplainMap.keySet());
        }

        String cardExplain = "";
        BigDecimal cardBalance = BigDecimal.ZERO;
        MemberCard memberCard = null;
        if (isMemberCardUsed) {
            cardExplain = "行使会员特权";
            memberCard = memberCardService.findById(selectedCard.getCardId());
            if (memberCard != null) {
                if (memberCard.isExpired()) {
                    throw new BizException("会员卡已过期");
                }
                cardBalance = memberCard.getBalance();
                accountIdSet.add(memberCard.getAccountId());
            }
        }
        List<AccountInfo> accountInfoList = accountInfoService.getInfoByIds(accountIdSet);
        if (Langs.isEmpty(accountInfoList)) {
            return Collections.emptyMap();
        }
        Map<Long, Long> flowIdMap = Maps.newHashMap();
        String flowSn = generateFlowSn(shopId, userId);
        ShopManager shopManager = null;
        if (userId != null) {
            shopManager = shopManagerService.selectByShopIdAndManagerIdDB(shopId, userId);
        }
        for (AccountInfo accountInfo : accountInfoList) {
            Long accountId = accountInfo.getId();
            if (isComboUsed || isCouponUsed || isMemberCardUsed) {
                Integer tradeType = getTradeType(isCouponUsed, isComboUsed, isMemberCardUsed);
                AccountTradeFlow accountTradeFlow = new AccountTradeFlow();
                accountTradeFlow.setCreator(userId);
                accountTradeFlow.setModifier(userId);
                accountTradeFlow.setShopId(shopId);
                accountTradeFlow.setFlowSn(flowSn);
                if(shopManager == null){
                    accountTradeFlow.setOperatorName("");
                } else {
                    accountTradeFlow.setOperatorName(shopManager.getName());
                }
                accountTradeFlow.setCustomerName(accountInfo.getCustomerName());
                accountTradeFlow.setMobile(accountInfo.getMobile());
                accountTradeFlow.setAccountId(accountId);
                accountTradeFlow.setOrderId(orderId);
                accountTradeFlow.setTradeType(tradeType);
                accountTradeFlow.setConsumeType(3);
                accountTradeFlow.setCouponExplain(couponExplainMap.get(accountId));
                accountTradeFlow.setServiceExplain(serviceExplainMap.get(accountId));
                if (memberCard != null && memberCard.getAccountId().equals(accountId)) {
                    accountTradeFlow.setCardExplain(cardExplain);
                    accountTradeFlow.setCardBalance(cardBalance);
                }
                accountTradeFlowDao.insert(accountTradeFlow);

                flowIdMap.put(accountId, accountTradeFlow.getId());
            }
        }
        return flowIdMap;
    }

    /**
     * 优惠券变更说明
     * @param selectedCouponList
     * @return
     */
    private Map<Long, String> getCouponExplainMap(List<SelectedCouponBo> selectedCouponList) {
        Map<Long, String> couponExplainMap = Maps.newHashMap();
        if (Langs.isNotEmpty(selectedCouponList)) {
            List<Long> couponIds = Lists.transform(selectedCouponList, new Function<SelectedCouponBo, Long>() {
                @Override
                public Long apply(SelectedCouponBo selectedCouponBo) {
                    return selectedCouponBo.getCouponId();
                }
            });
            List<AccountCoupon> accountCouponList = accountCouponService.listByIds(couponIds);
            if (Langs.isEmpty(accountCouponList)) {
                return couponExplainMap;
            }
            // 校验券是否过期或已使用
            Collection<AccountCoupon> invalidCouponList = Collections2.filter(accountCouponList, new Predicate<AccountCoupon>() {
                @Override
                public boolean apply(AccountCoupon accountCoupon) {
                    return accountCoupon.isExpired() || AccountCoupon.StateEnum.USED.getCode() == accountCoupon.getUsedStatus();
                }
            });
            if (Langs.isNotEmpty(invalidCouponList)) {
                List<Long> invalidAccountCouponIds = Lists.newArrayListWithCapacity(invalidCouponList.size());
                StringBuilder sb = new StringBuilder("优惠券：");
                for (AccountCoupon accountCoupon : invalidCouponList) {
                    sb.append(accountCoupon.getCouponName()).append("、");
                    invalidAccountCouponIds.add(accountCoupon.getId());
                }
                log.error("优惠券已失效，accountCouponIdList = {}", JSONUtil.object2Json(invalidAccountCouponIds));
                throw new BizException(sb.substring(0, sb.length() - 1) + "已失效");
            }
            for (AccountCoupon accountCoupon : accountCouponList) {
                Long accountId = accountCoupon.getAccountId();
                String couponName = accountCoupon.getCouponName();
                couponName = couponName == null ? "" : couponName;
                String couponExplain = couponName + "-1; ";
                appendMapValue(couponExplainMap, accountId, couponExplain);
            }
        }
        return couponExplainMap;
    }

    /**
     * 服务项目变更说明
     * @param selectedComboList
     * @return
     */
    private Map<Long, String> getServiceExplainMap(List<SelectedComboBo> selectedComboList) {
        Map<Long, String> serviceExplainMap = Maps.newHashMap();
        if (Langs.isNotEmpty(selectedComboList)) {
            List<Long> comboServiceIds = Lists.newArrayList();
            Map<Long, Integer> useCountMap = Maps.newHashMap();
            for (SelectedComboBo selectedComboBo : selectedComboList) {
                Long comboServiceId = selectedComboBo.getComboServiceId();
                comboServiceIds.add(comboServiceId);
                useCountMap.put(comboServiceId, selectedComboBo.getCount());
            }

            List<AccountComboServiceRel> accountComboServiceRels = comboServiceRelService.listByIds(comboServiceIds);
            if (Langs.isNotEmpty(accountComboServiceRels)) {
                List<Long> accountComboIds = Lists.transform(accountComboServiceRels, new Function<AccountComboServiceRel, Long>() {
                    @Override
                    public Long apply(AccountComboServiceRel accountComboServiceRel) {
                        return accountComboServiceRel.getComboId();
                    }
                });
                List<AccountCombo> accountComboList = accountComboService.listByIds(accountComboIds);
                Map<Long, Long> comboIdAndAccountIdRel = Maps.newHashMap();
                if (Langs.isEmpty(accountComboList)) {
                    return serviceExplainMap;
                }
                // 校验计次卡是否已失效
                Collection<AccountCombo> invalidComboList = Collections2.filter(accountComboList, new Predicate<AccountCombo>() {
                    @Override
                    public boolean apply(AccountCombo accountCombo) {
                        return accountCombo.isExpired() || AccountCombo.EXHAUSTED.equals(accountCombo.getComboStatus());
                    }
                });
                if (Langs.isNotEmpty(invalidComboList)) {
                    List<Long> invalidComboIdList = Lists.newArrayListWithCapacity(invalidComboList.size());
                    StringBuilder sb = new StringBuilder("计次卡：");
                    for (AccountCombo accountCombo : invalidComboList) {
                        sb.append(accountCombo.getComboName()).append("、");
                        invalidComboIdList.add(accountCombo.getId());
                    }
                    log.error("计次卡已失效，accountComboIdList = {}", JSONUtil.object2Json(invalidComboIdList));
                    throw new BizException(sb.substring(0, sb.length() - 1) + "已失效");
                }

                for (AccountCombo accountCombo : accountComboList) {
                    Long id = accountCombo.getId();
                    Long accountId = accountCombo.getAccountId();
                    comboIdAndAccountIdRel.put(id, accountId);
                }
                for (AccountComboServiceRel accountComboServiceRel : accountComboServiceRels) {
                    Long accountId = comboIdAndAccountIdRel.get(accountComboServiceRel.getComboId());
                    if (accountId != null) {
                        Integer useCount = useCountMap.get(accountComboServiceRel.getId());
                        StringBuilder serviceExplainSb = new StringBuilder();
                        serviceExplainSb.append(accountComboServiceRel.getServiceName()).append("-").append(useCount).append("; ");
                        appendMapValue(serviceExplainMap, accountId, serviceExplainSb.toString());
                    }
                }
            }
        }
        return serviceExplainMap;
    }

    private void appendMapValue(Map<Long, String> map, Long key, String value) {
        if (map.containsKey(key)) {
            String tempValue = map.get(key);
            tempValue = tempValue == null ? "" : tempValue;
            map.put(key, tempValue + value);
        } else {
            map.put(key, value);
        }
    }


    @Override
    public AccountTradeFlow recordFlowForHandleMemberCard(MemberGrantVo memberGrantVo, AccountInfo accountInfo, MemberCard memberCard, Long shopId, Long userId) {
        MemberCardInfo cardInfo = memberCard.getMemberCardInfo();
        AccountTradeFlow accountTradeFlow = generateAccountTradeBase(shopId, userId);
        accountTradeFlow.setMobile(accountInfo.getMobile());
        accountTradeFlow.setAccountId(accountInfo.getId());
        accountTradeFlow.setPaymentId(memberGrantVo.getPaymentId());
        accountTradeFlow.setConsumeType(AccountConsumeTypeEnum.HANDLE_CARD.getCode());
        accountTradeFlow.setPaymentName(memberGrantVo.getPaymentName());
        accountTradeFlow.setCardExplain(cardInfo.getInitBalance().toString());
        accountTradeFlow.setCardBalance(memberCard.getBalance());
        accountTradeFlow.setCustomerName(accountInfo.getCustomerName());
        accountTradeFlow.setTradeType(AccountTradTypeEnum.MEMBER_CARD.getCode());
        BigDecimal amount = memberGrantVo.getAmount();
        if (amount == null) {
            amount = BigDecimal.ZERO;
        }
        amount = amount.setScale(2, RoundingMode.HALF_UP);
        accountTradeFlow.setPayAmount(amount);
        accountTradeFlow.setReceiver(memberCard.getReceiver());
        accountTradeFlow.setReceiverName(memberCard.getReceiverName());
        accountTradeFlow.setGmtCreate(new Date());
        accountTradeFlow.setGmtModified(new Date());
        accountTradeFlow.setAmount(cardInfo.getSalePrice());
        accountTradeFlowDao.insert(accountTradeFlow);

        return  accountTradeFlow;
    }

    @Override
    public AccountTradeFlow recordFlowForUpgradeMemberCard(Long shopId, Long operatorId, MemberUpgradeVo memberUpgradeVo) {

        AccountTradeFlow accountTradeFlow = generateAccountTradeBase(shopId, operatorId);
        accountTradeFlow.setMobile(memberUpgradeVo.getMobile());
        accountTradeFlow.setAccountId(memberUpgradeVo.getAccountId());
        accountTradeFlow.setTradeType(AccountTradTypeEnum.MEMBER_CARD.getCode());
        accountTradeFlow.setConsumeType(AccountConsumeTypeEnum.UPGRADE_CARD.getCode());

        accountTradeFlow.setCardExplain(memberUpgradeVo.getOldCardTypeName()+"升级为"+memberUpgradeVo.getNewCardTypeName());
        accountTradeFlow.setCardBalance(memberUpgradeVo.getCardBalance());
        accountTradeFlow.setCustomerName(memberUpgradeVo.getCustomerName());
        accountTradeFlow.setReceiver(memberUpgradeVo.getReceiver());
        accountTradeFlow.setReceiverName(memberUpgradeVo.getReceiverName());
        accountTradeFlow.setGmtCreate(new Date());
        accountTradeFlow.setGmtModified(new Date());
        accountTradeFlow.setAmount(new BigDecimal(0));
        accountTradeFlowDao.insert(accountTradeFlow);

        return accountTradeFlow;
    }

    @Override
    public Page<AccountTradeFlow> getPageFlowForCardRecharge(Pageable pageable, Long shopId, Date date1, Date date2,String mobile) {
        Map param = Maps.newHashMap();
        param.put("shopId",shopId);
        param.put("consumeType",AccountConsumeTypeEnum.CHARGE.getCode());
        param.put("tradeType",AccountTradTypeEnum.MEMBER_CARD.getCode());
        if (date1 != null) {
            param.put("startTime", date1);
        }
        if (date2 != null) {
            param.put("endTime", date2);
        }
        if(!StringUtil.isStringEmpty(mobile)){
            param.put("mobile",mobile);
        }
        Page<AccountTradeFlow> page = super.getPage(accountTradeFlowDao, pageable, param);
        if (page != null) {
            List<AccountTradeFlow> content = page.getContent();
            if (Langs.isNotEmpty(content)) {
                List<Long> accountTradeFlowIdList = Lists.transform(content, new Function<AccountTradeFlow, Long>() {
                    @Override
                    public Long apply(AccountTradeFlow accountTradeFlow) {
                        return accountTradeFlow.getId();
                    }
                });
                Map<Long, String> flowIdAndCardTypeNameRel = accountCardFlowDetailService.getFlowIdAndCardTypeNameRel(shopId, accountTradeFlowIdList);
                for (AccountTradeFlow flow : content) {
                    flow.setCardTypeName(flowIdAndCardTypeNameRel.get(flow.getId()));
                }
            }
        }
        return page;
    }

    @Override
    public void update(AccountTradeFlow accountTradeFlow) {
        accountTradeFlowDao.updateById(accountTradeFlow);
    }

    @Override
    public AccountTradeFlow recordReverseFlowForCouponRecharge(Long shopId, Long userId, String userName, AccountTradeFlow flow, String couponExpalin) {

        AccountTradeFlow reverseFlow = reverseRechargeBase(shopId, userId, userName, flow);

        reverseFlow.setConsumeType(AccountConsumeTypeEnum.CHARGE_REVERT.getCode());
        reverseFlow.setTradeType(AccountTradTypeEnum.COUPON.getCode());
        reverseFlow.setCouponExplain(couponExpalin);
        accountTradeFlowDao.insert(reverseFlow);
        return reverseFlow;
    }

    @Override
    public AccountTradeFlow recordReverseFlowForComboRecharge(Long shopId, Long userId, String userName, AccountTradeFlow flow, String comboExplain) {

        AccountTradeFlow reverseFlow = reverseRechargeBase(shopId, userId, userName, flow);

        reverseFlow.setConsumeType(AccountConsumeTypeEnum.CHARGE_REVERT.getCode());
        reverseFlow.setTradeType(AccountTradTypeEnum.COMBO.getCode());
        reverseFlow.setServiceExplain(comboExplain);
        accountTradeFlowDao.insert(reverseFlow);
        return reverseFlow;
    }

    @Override
    public List<AccountTradeFlow> getMemberCardConsumeFlow(Long shopId, Long accountId) {
        Map param = Maps.newHashMap();
        param.put("shopId",shopId);
        param.put("accountId",accountId);
        param.put("tradeType",AccountTradTypeEnum.MEMBER_CARD.getCode());
        param.put("consumeType", AccountCardFlowDetail.ConsumeTypeEnum.CONSUME.getCode());
        param.put("isReversed",0);
        return accountTradeFlowDao.select(param);
    }

    @Override
    public List<AccountTradeFlow> getMemberCardGrantFlowByAccountIds(Long shopId, List accountIds) {
        Map param = Maps.newHashMap();
        param.put("shopId",shopId);
        param.put("accountIds",accountIds);
        param.put("tradeType",AccountTradTypeEnum.MEMBER_CARD.getCode());
        param.put("consumeType", AccountCardFlowDetail.ConsumeTypeEnum.HANDLE_CARD.getCode());
        return accountTradeFlowDao.select(param);
    }
    @Override
    public AccountTradeFlow recordReverseFlowForCardGrant(Long shopId, Long userId, String userName, AccountTradeFlow flow, String cardExpalin, BigDecimal balance) {
        AccountTradeFlow reverseFlow = reverseRechargeBase(shopId, userId, userName, flow);
        reverseFlow.setConsumeType(AccountConsumeTypeEnum.GRANT_REVERSE.getCode());
        reverseFlow.setTradeType(AccountTradTypeEnum.MEMBER_CARD.getCode());
        reverseFlow.setCardBalance(balance);
        reverseFlow.setCardExplain(cardExpalin);
        accountTradeFlowDao.insert(reverseFlow);
        return reverseFlow;
    }

    private AccountTradeFlow reverseRechargeBase(Long shopId, Long userId, String userName, AccountTradeFlow flow) {
        flow.setIsReversed(1);
        accountTradeFlowDao.updateById(flow);
        AccountTradeFlow reverseFlow = generateAccountTradeBase(shopId, userId);
        reverseFlow.setAmount(flow.getAmount().negate());
        reverseFlow.setPayAmount(flow.getPayAmount().negate());
        reverseFlow.setPaymentId(flow.getPaymentId());
        reverseFlow.setPaymentName(flow.getPaymentName());
        reverseFlow.setReverseId(flow.getId());
        reverseFlow.setIsReversed(0);
        reverseFlow.setCustomerName(flow.getCustomerName());
        reverseFlow.setMobile(flow.getMobile());
        reverseFlow.setAccountId(flow.getAccountId());
        reverseFlow.setReceiver(flow.getReceiver());
        reverseFlow.setReceiverName(flow.getReceiverName());
        reverseFlow.setOperatorName(userName);
        return reverseFlow;
    }

    @Override
    public AccountTradeFlow recordFlowForCardBack(BackCardVo backCardVo, MemberCard memberCard) {
        Long shopId = backCardVo.getShopId();
        Long userId = backCardVo.getUserId();
        Long accountId = memberCard.getAccountId();
        AccountInfo accountInfo = accountInfoService.getAccountInfoById(accountId);

        AccountTradeFlow flow = generateAccountTradeBase(shopId, userId);
        flow.setAmount(BigDecimal.ZERO);
        flow.setPaymentName(backCardVo.getPaymentName());
        flow.setPaymentId(backCardVo.getPaymentId());
        BigDecimal payAmount = backCardVo.getPayAmount();
        if (payAmount == null) {
            payAmount = BigDecimal.ZERO;
        }
        payAmount = payAmount.setScale(2, RoundingMode.HALF_UP);
        flow.setPayAmount(payAmount.negate());
        flow.setTradeType(AccountTradTypeEnum.MEMBER_CARD.getCode());
        flow.setIsReversed(0);
        flow.setConsumeType(AccountConsumeTypeEnum.CANCEL_CARD.getCode());
        flow.setCardBalance(memberCard.getBalance());
        flow.setCustomerName(accountInfo.getCustomerName());
        flow.setMobile(accountInfo.getMobile());
        flow.setAccountId(accountId);
        String typeName = memberCardInfoService.getTypeNameById(shopId, memberCard.getCardTypeId());
        flow.setCardExplain(typeName + "退卡");
        flow.setRemark(backCardVo.getRemark());
        accountTradeFlowDao.insert(flow);

        return flow;
    }

    @Override
    public void recordFlowFowCardInit(List<? extends MemberCard> memberCardList, Long shopId, Long userId) {
        if (CollectionUtils.isEmpty(memberCardList)) {
            return;
        }

        Set<Long> accountIds = Sets.newHashSet();
        for (MemberCard item : memberCardList) {
            accountIds.add(item.getAccountId());
        }

        List<Long> accountIdList = new ArrayList<>(accountIds);
        List<AccountInfo> infoList = accountInfoService.getInfoByIds(accountIdList);
        Map<Long,String> infoMap = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(infoList)) {
            for (AccountInfo item : infoList) {
                infoMap.put(item.getId(),item.getCustomerName());

            }
        }

        for (MemberCard item : memberCardList) {
            item.setCustomerName(infoMap.get(item.getAccountId()));
        }

        Map<Long,Long> flowMap = Maps.newHashMap();
        for (MemberCard item : memberCardList) {
            AccountTradeFlow flow = generateAccountTradeBase(shopId, userId);
            flow.setMobile(item.getMobile());
            flow.setAccountId(item.getAccountId());
            flow.setConsumeType(AccountConsumeTypeEnum.INIT.getCode());
            flow.setCardExplain(item.getBalance().toString());
            flow.setCardBalance(item.getBalance());
            flow.setCustomerName(item.getCustomerName());
            flow.setTradeType(AccountTradTypeEnum.MEMBER_CARD.getCode());
            flow.setGmtCreate(new Date());
            flow.setGmtModified(new Date());
            accountTradeFlowDao.insert(flow);
            flowMap.put(flow.getAccountId(),flow.getId());
        }

        accountCardFlowDetailService.recordListForInit(shopId,userId,memberCardList,flowMap);

    }

    @Override
    public AccountTradeFlow recordFlowForCouponImport(AccountCoupon coupon, Long shopId, Long userId) {

        Long accountId = coupon.getAccountId();
        AccountInfo accountInfo = accountInfoService.getAccountInfoAllById(shopId,accountId);
        String mobile = accountInfo.getMobile();
        String customerName = accountInfo.getCustomerName();
        AccountTradeFlow tradeFlow = generateAccountTradeBase(shopId, userId);
        tradeFlow.setTradeType(AccountTradTypeEnum.COUPON.getCode());
        tradeFlow.setIsReversed(0);
        tradeFlow.setConsumeType(AccountConsumeTypeEnum.INIT.getCode());
        tradeFlow.setCouponExplain(coupon.getCouponName()+"+1");
        tradeFlow.setCustomerName(customerName);
        tradeFlow.setMobile(mobile);
        tradeFlow.setAccountId(accountId);
        accountTradeFlowDao.insert(tradeFlow);
        return tradeFlow;
    }

    @Override
    public AccountTradeFlow recordFlowForComboImport(AccountCombo combo, Long shopId, Long userId) {
        StringBuilder serviceExplain = new StringBuilder();
        List<AccountComboServiceRel> serviceList = combo.getServiceList();
        if (CollectionUtils.isNotEmpty(serviceList)) {
            for (AccountComboServiceRel service : serviceList) {
                String serviceName = service.getServiceName();
                int changeCount = service.getTotalServiceCount() - service.getUsedServiceCount();
                serviceExplain.append(serviceName).append("+").append(changeCount).append(",");
            }
            serviceExplain.substring(0,serviceExplain.length()-1);
        }
        Long accountId = combo.getAccountId();
        AccountInfo accountInfo = accountInfoService.getAccountInfoAllById(shopId, accountId);
        AccountTradeFlow flow = generateAccountTradeBase(shopId, userId);
        flow.setAccountId(accountId);
        flow.setTradeType(AccountTradTypeEnum.COMBO.getCode());
        flow.setIsReversed(0);
        flow.setConsumeType(AccountConsumeTypeEnum.INIT.getCode());
        flow.setServiceExplain(serviceExplain.toString());
        flow.setCustomerName(accountInfo.getCustomerName());
        flow.setMobile(accountInfo.getMobile());
        accountTradeFlowDao.insert(flow);
        return flow;
    }

    @Override
    public List<AccountTradeFlow> getAccountTradeFlowByIds(Long... flowIds) {
        return accountTradeFlowDao.selectByIds(flowIds);
    }

    @Override
    public List<AccountTradeFlow> listByIds(Long shopID, Collection<Long> flowIds) {

        if (CollectionUtils.isNotEmpty(flowIds)) {
            return accountTradeFlowDao.selectByIds(shopID,flowIds);
        }
        return Lists.newArrayList();
    }

    @Override
    public List<AccountTradeFlow> findFlowByAccountIdsAndConsumeType(Long shopId, List accountIds, int consumeType) {
        Map param = Maps.newHashMap();
        param.put("shopId",shopId);
        param.put("accountIds",accountIds);
        param.put("tradeType",AccountTradTypeEnum.MEMBER_CARD.getCode());
        param.put("consumeType", consumeType);
        return accountTradeFlowDao.select(param);
    }

    @Override
    public List<AccountTradeFlow> findFlowByAccountIdsMobilesAndConsumeType(Long shopId, List<Long> accountIds, int consumeType, String importFlag) {
        return accountTradeFlowDao.findFlowByAccountIdsMobilesAndConsumeType(shopId, accountIds, consumeType, importFlag);
    }

    /**
     * 根据工单获取
     *
     * @param shopId
     * @param orderId
     * @return
     */
    @Override
    public List<AccountTradeFlow> findComboDetail(Long shopId, Long orderId) {
        Map map = Maps.newHashMap();
        map.put("shopId",shopId);
        map.put("orderId",orderId);
        return accountTradeFlowDao.select(map);
    }

    @Override
    public BigDecimal getTotalChargeAmountByCardId(final Long shopId, final Long cardId) {
        return new BizTemplate<BigDecimal>(){
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(shopId, "店铺id不能为空.");
                Assert.notNull(cardId, "账户id不能为空.");
            }

            @Override
            protected BigDecimal process() throws BizException {
                List<MemberCard> cardList = memberCardService.getUpgradedCardsContainDeletedById(shopId, cardId);
                if (Langs.isEmpty(cardList)) {
                    return BigDecimal.ZERO;
                } else {
                    List<Long> cardIds = Lists.transform(cardList, new Function<MemberCard, Long>() {
                        @Override
                        public Long apply(MemberCard input) {
                            return input.getId();
                        }
                    });
                    return accountTradeFlowDao.getTotalChargeAmountByCardIds(shopId, cardIds);
                }
            }
        }.execute();

    }

    /**
     * 查询会员卡累计收款金额
     *
     * @param shopId
     * @param memberCardId
     * @return
     */
    @Override
    public BigDecimal getCardTotalPayAmount(Long shopId, Long memberCardId) {
        Assert.notNull(shopId, "店铺id不能为空.");
        Assert.notNull(memberCardId, "会员卡id不能为空.");

        // 查询会员卡升级链集合
        List<MemberCard> cardList = memberCardService.getUpgradedCardsContainDeletedById(shopId, memberCardId);
        if (CollectionUtils.isEmpty(cardList)) {
            return BigDecimal.ZERO;
        }

        List<Long> cardIds = Lists.transform(cardList, new Function<MemberCard, Long>() {
            @Override
            public Long apply(MemberCard memberCard) {
                return memberCard.getId();
            }
        });

        List<Long> rechargeTradeFlowIds = accountCardFlowDetailService.getRechargeTradeFlowIds(shopId, cardIds);
        if (CollectionUtils.isEmpty(rechargeTradeFlowIds)) {
            return BigDecimal.ZERO;
        }
        return accountTradeFlowDao.getTotalPayAmount(shopId, rechargeTradeFlowIds);
    }
}
