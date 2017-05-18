package com.tqmall.legend.facade.account.impl;

import com.google.common.base.Function;
import com.google.common.collect.*;
import com.tqmall.common.exception.BizException;
import com.tqmall.legend.biz.account.*;
import com.tqmall.legend.biz.account.bo.ConsumeComboBo;
import com.tqmall.legend.biz.customer.CustomerCarRelService;
import com.tqmall.legend.biz.customer.CustomerService;
import com.tqmall.legend.entity.account.*;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.entity.customer.CustomerCarRel;
import com.tqmall.legend.facade.account.AccountFacadeService;
import com.tqmall.legend.facade.account.vo.*;
import com.tqmall.legend.facade.discount.DiscountCenter2;
import com.tqmall.legend.facade.discount.bo.*;
import com.tqmall.wheel.lang.Langs;
import com.tqmall.wheel.utils.DateFormatUtils;
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
public class AccountFacadeServiceImpl implements AccountFacadeService {

    @Autowired
    private DiscountCenter2 discountCenter;
    @Autowired
    private AccountInfoService accountInfoService;
    @Autowired
    private AccountCouponService accountCouponService;
    @Autowired
    private AccountComboService accountComboService;
    @Autowired
    private AccountTradeFlowService accountTradeFlowService;
    @Autowired
    private AccountCardFlowDetailService accountCardFlowDetailService;
    @Autowired
    private MemberCardService memberCardService;
    @Autowired
    private AccountCouponFlowDetailService accountCouponFlowDetailService;
    @Autowired
    private AccountComboFlowDetailService accountComboFlowDetailService;
    @Autowired
    private AccountFlowService accountFlowService;
    @Autowired
    private ComboInfoService comboInfoService;
    @Autowired
    private MemberCardInfoService cardInfoService;
    @Autowired
    private CouponInfoService couponInfoService;
    @Autowired
    private AccountSuiteService accountSuiteService;
    @Autowired
    private CustomerCarRelService customerCarRelService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private AccountComboServiceRelService accountComboServiceRelService;

    @Override
    public DiscountInfoBo getDiscountInfoByOrderId(Long shopId, Long orderId) {
        return this.getDiscountInfoByOrderId(shopId, orderId, null);
    }


    @Override
    public DiscountInfoBo getDiscountInfoByOrderId(Long shopId, Long orderId, DiscountSelectedBo selectedItem) {
        Assert.notNull(shopId, "店铺id不能为空");
        Assert.notNull(orderId, "工单id不能为空");
        return this.discountCenter.discountOrder(shopId, orderId, selectedItem);
    }


    @Override
    public DiscountInfoBo getCarWashDiscountInfo(Long shopId, String carLicense, Long serviceId,
                                                 BigDecimal amount,
                                                 DiscountSelectedBo selectedItem) {
        /**
         * 接口初始数据校验
         */
        Assert.notNull(shopId, "店铺id不能为空.");
        Assert.hasText(carLicense, "车牌信息不能为空.");
        Assert.notNull(amount, "洗车单金额不能为空.");

        return this.discountCenter.discountCarWashOrder(shopId, carLicense, serviceId, amount, selectedItem);
    }

    @Override
    public DiscountInfoBo getCarWashDiscountInfo(Long shopId, String carLicense, Long serviceId, BigDecimal amount) {
        return this.getCarWashDiscountInfo(shopId, carLicense, serviceId, amount, null);
    }

    @Override
    public void settlementAccount(Long shopId, Long userId, Long orderId, DiscountSelectedBo discountSelectedBo) {
        /**
         *
         * if (discountUsed){
         *      2.写流水
         *      3.扣减优惠券和计次卡
         * }
         */
        /**
         * 这里调用接口的唯一目的是进行数据校验
         */
        //this.discountCenter.discountOrder(shopId, orderId, selectedItem);

        Map<Long, Long> flowIdMap = accountFlowService.recordFlowForBillConfirm(shopId, userId, orderId, discountSelectedBo);
        if (org.springframework.util.CollectionUtils.isEmpty(flowIdMap)) {
            return;
        }

        //1.扣减优惠券和计次卡
        List<SelectedCouponBo> selectedCouponList = discountSelectedBo.getSelectedCouponList();
        if (Langs.isNotEmpty(selectedCouponList)) {
            List<Long> idList = new ArrayList<>();
            for (SelectedCouponBo selectedCouponBo : selectedCouponList) {
                idList.add(selectedCouponBo.getCouponId());
            }
            accountCouponService.consume(shopId, userId, idList, flowIdMap);
        }
        List<SelectedComboBo> selectedComboList = discountSelectedBo.getSelectedComboList();
        if (Langs.isNotEmpty(selectedComboList)) {
            List<ConsumeComboBo> consumeComboBoList = new ArrayList<>();
            for (SelectedComboBo selectedComboBo : selectedComboList) {
                ConsumeComboBo consumeComboBo = new ConsumeComboBo();
                consumeComboBo.setComboServiceRelId(selectedComboBo.getComboServiceId());
                consumeComboBo.setServiceUsedCount(selectedComboBo.getCount());
                consumeComboBoList.add(consumeComboBo);
            }
            accountComboService.consumeCombo(shopId, userId, consumeComboBoList, flowIdMap);
        }

        SelectedCardBo selectedCard = discountSelectedBo.getSelectedCard();
        if (selectedCard != null) {
            MemberCard memberCard = memberCardService.findById(selectedCard.getCardId());
            if (memberCard != null) {
                memberCardService.addExpenseCount(memberCard);
                Long flowId = flowIdMap.get(memberCard.getAccountId());
                if (flowId != null) {
                    accountCardFlowDetailService.recordDetailForBillConfirm(shopId, userId, flowId, memberCard);
                }
            }
        }
    }




    @Override
    public void debitAccount(Long shopId, Long userId, Long orderId, Long memberCardId, BigDecimal cardPayAmount) {
        /**
         * 1.扣消費總金額
         * 2.扣会员卡余额
         * 2.记流水
         * 3.变更累计消费次数
         */

        if (cardPayAmount == null || cardPayAmount.compareTo(BigDecimal.ZERO) == 0) {
            return;
        }

        if (memberCardId == null || memberCardId <= 0) {
            return;
        }
        MemberCard memberCard = memberCardService.findById(memberCardId);
        if (memberCard == null) {
            return;
        }
        if (memberCard.isExpired()) {
            throw new BizException("会员卡已过期");
        }
        AccountInfo accountInfo = accountInfoService.getAccountInfoById(memberCard.getAccountId());
        if (accountInfo == null) {
            return;
        }
        memberCard = memberCardService.consume(shopId, userId, memberCard, cardPayAmount);
        addExpenseAmount(shopId, orderId, memberCard);
        accountTradeFlowService.recordFlowForBillPay(shopId, userId, orderId, cardPayAmount, accountInfo, memberCard);
    }

    private void addExpenseAmount(Long shopId, Long orderId, MemberCard memberCard) {
        List<AccountTradeFlow> tradeFlows = accountTradeFlowService.selectByOrderId(shopId, orderId);
        if (Langs.isEmpty(tradeFlows)) {
            memberCardService.addExpenseCount(memberCard);
        } else {
            List<Long> flowIds = Lists.transform(tradeFlows, new Function<AccountTradeFlow, Long>() {
                @Override
                public Long apply(AccountTradeFlow input) {
                    return input.getId();
                }
            });
            List<AccountCardFlowDetail> flowDetails = accountCardFlowDetailService.getCardFlowDetailByFlowIds(shopId, flowIds);
            if (CollectionUtils.isEmpty(flowDetails)) {
                memberCardService.addExpenseCount(memberCard);
            }
        }
    }


    @Override
    public void revertSettlement(Long shopId, Long operator, Long orderId) {
        Long userId = operator;

        List<AccountTradeFlow> accountTradeFlowList = accountTradeFlowService
                .selectByOrderId(shopId, orderId);
        if (!CollectionUtils.isEmpty(accountTradeFlowList)) {
            boolean isMemberUsed = false;
            MemberCard memberCard = null;
            for (AccountTradeFlow flow : accountTradeFlowList) {
                if (flow.getTradeType().equals(AccountTradTypeEnum.MEMBER_CARD.getCode())) {
                    // 获取流水详情
                    AccountCardFlowDetail detail =
                            accountCardFlowDetailService.findByFlowId(shopId, flow.getId());

                    if (detail != null) {
                        isMemberUsed = true;
                        MemberCard upgradedMemberCard = memberCardService.getUpgradedMemberCard(shopId, detail.getCardId());

                        // 回滚会员卡余额
                        memberCard = memberCardService.revertConsume(shopId, userId, upgradedMemberCard.getId(), detail.getChangeAmount());
                        // 记冲红流水
                        accountFlowService.recordReversFlowForBillPay(shopId, userId, flow, detail, upgradedMemberCard.getId());
                    }
                } else {
                    // 获取流水详情
                    List<AccountCouponFlowDetail> couponDetailList =
                            accountCouponFlowDetailService.findByFlowId(shopId, flow.getId());
                    List<AccountComboFlowDetail> comboDetailList =
                            accountComboFlowDetailService.findByFlowId(shopId, flow.getId());

                    //记反流水
                    AccountTradeFlow reverseFlow = accountFlowService
                            .recordReverseFlowForBillConfirm(shopId, userId, flow, couponDetailList, comboDetailList);

                    // 遍历详情回滚coupon,combo
                    Long reverseFlowId = reverseFlow.getId();
                    if (!CollectionUtils.isEmpty(couponDetailList)) {
                        for (AccountCouponFlowDetail item : couponDetailList) {
                            accountCouponService.reverseConsume(userId,shopId,reverseFlowId, item);
                        }
                    }

                    if (!CollectionUtils.isEmpty(comboDetailList)) {
                        for (AccountComboFlowDetail item : comboDetailList) {
                            accountComboService.reverseConsume(userId,shopId,reverseFlowId, item);
                        }
                    }
                }
            }
            if (isMemberUsed) {
                memberCardService.subExpenseCount(memberCard);
            }
        }

    }

    @Override
    public Long createSimpleComboType(Long shopId, Long operatorId, String comboName, Long effectivePeriodDays, BigDecimal salePrice, String remark, Long serviceId, Integer serviceCount) throws BizException {
        ComboInfo comboInfo = new ComboInfo();
        comboInfo.setComboName(comboName);
        comboInfo.setEffectivePeriodDays(effectivePeriodDays);
        comboInfo.setSalePrice(salePrice);
        comboInfo.setRemark(remark);
        comboInfo.setCustomizeTime(ComboInfo.NON_CUSTOM_TIME);
        List<ComboInfoServiceRel> serviceRelList = Lists.newArrayList();
        ComboInfoServiceRel serviceRel = new ComboInfoServiceRel();
        serviceRel.setServiceId(serviceId);
        serviceRel.setServiceCount(serviceCount);
        serviceRelList.add(serviceRel);
        comboInfo.setContent(serviceRelList);
        Long comboInfoId = comboInfoService.addComboInfo(comboInfo, shopId, operatorId);
        return comboInfoId;
    }

    @Override
    public ComboInfo getComboInfo(Long shopId, Long comboInfoId) {
        if (shopId == null) {
            throw new BizException("shopId为空");
        }
        if (comboInfoId == null) {
            throw new BizException("comboInfoId为空");
        }
        return comboInfoService.getComboInfo(comboInfoId,shopId);
    }

    @Override
    public List<MemberCardInfo> listFreeMemberCardInfo(Long shopId) {
        if (shopId == null) {
            throw new BizException("shopId为空");
        }
        List<MemberCardInfo> validInfos = cardInfoService.findAllByShopId(shopId, 1);
        List<MemberCardInfo> data = Lists.newArrayList();
        for (MemberCardInfo info : validInfos) {
            boolean isSalePriceZero = info.getSalePrice().compareTo(BigDecimal.ZERO) == 0;
            boolean isInitBalanceZero = info.getInitBalance().compareTo(BigDecimal.ZERO) == 0;
            if (isSalePriceZero && isInitBalanceZero) {
                data.add(info);
            }
        }
        return data;
    }

    @Override
    public List<AccountCoupon> listFreeCoupons(Long shopID, Long accountId) {

        List<AccountCoupon> freeCoupons = accountCouponService.listFreeCoupon(shopID, accountId);
        attachInfo(shopID, freeCoupons);
        return freeCoupons;
    }

    @Override
    public List<AccountSuite> listBundleCoupons(Long shopID, Long accountId) {
        List<AccountCoupon> bundleCoupons = accountCouponService.listBundleCoupon(shopID, accountId);
        attachInfo(shopID, bundleCoupons);
        List<AccountSuite> suites = attachToSuite(shopID, bundleCoupons);
        return suites;
    }

    @Override
    public AccountCoupon getCouponDetail(Long shopID, Long couponId) {
        AccountCoupon coupon = accountCouponService.findById(couponId, shopID);
        if (coupon == null) {
            throw new BizException("找不到couponId="+couponId+"的优惠券");
        }
        attachInfo(shopID, coupon);
        return coupon;
    }

    /**
     * 查询客户优惠信息
     *
     * @param shopId
     * @param customerCarId
     * @return
     */
    @Override
    public List<CustomerDiscountInfo> getCustomerDiscountInfo(Long shopId, Long customerCarId) {
        List<CustomerDiscountInfo> customerDiscountInfoList = new ArrayList<>();
        Map<Long, Long> accountIdCustomerIdRel = getAccountIdAndCustomerIdByCarId(shopId, customerCarId);
        if (org.springframework.util.CollectionUtils.isEmpty(accountIdCustomerIdRel)) {
            return customerDiscountInfoList;
        }

        Set<Long> accountIds = accountIdCustomerIdRel.keySet();
        Collection<Long> customerIds = accountIdCustomerIdRel.values();

        Map<Long, Customer> customerMap = getCustomerMap(shopId, customerIds);
        Map<Long, List<MemberCardDiscount>> memberCardDiscountMap = getMemberCardDiscountMap(shopId, accountIds);
        Map<Long, Long> sumComboCouponNumMap = getSumComboCouponNum(shopId, accountIds);
        Map<Long, List<ComboCouponDiscount>> comboCouponDiscountMap = getComboCouponDiscount(shopId, accountIds);

        for (Map.Entry<Long, Long> entry : accountIdCustomerIdRel.entrySet()) {
            Long accountId = entry.getKey();
            Long customerId = entry.getValue();
            Customer customer = customerMap.get(customerId);
            if (customer != null) {
                CustomerDiscountInfo customerDiscountInfo = new CustomerDiscountInfo();
                customerDiscountInfo.setCustomerId(customerId);
                customerDiscountInfo.setCustomerName(customer.getCustomerName());
                customerDiscountInfo.setMobile(customer.getMobile());
                customerDiscountInfo.setMemberCardDiscountList(memberCardDiscountMap.get(accountId));
                customerDiscountInfo.setSumComboCouponNum(sumComboCouponNumMap.get(accountId));
                customerDiscountInfo.setComboCouponDiscountList(comboCouponDiscountMap.get(accountId));
                customerDiscountInfoList.add(customerDiscountInfo);
            }
        }
        return customerDiscountInfoList;
    }

    private void attachInfo(Long shopID, AccountCoupon coupon) {
        CouponInfo couponInfo = couponInfoService.selectById(coupon.getCouponInfoId(), shopID);
        if (couponInfo == null) {
            throw new BizException("找不到优惠券类型");
        }
        String useRangeDescript = couponInfoService.composeUseRangeDescript(couponInfo);
        couponInfo.setUseRangeDescript(useRangeDescript);
        coupon.setCouponInfo(couponInfo);
    }

    private List<AccountSuite> attachToSuite(Long shopID, List<AccountCoupon> bundleCoupons) {
        List<Long> flowIds = Lists.transform(bundleCoupons, new Function<AccountCoupon, Long>() {
            @Override
            public Long apply(AccountCoupon input) {
                return input.getFlowId();
            }
        });
        Set<Long> flowIdSet = new HashSet<>(flowIds);
        List<AccountSuite> suites = accountSuiteService.listByFlowIds(shopID, flowIdSet);

        ImmutableMap<Long, AccountSuite> flowIdSuiteMap = Maps.uniqueIndex(suites, new Function<AccountSuite, Long>() {
            @Override
            public Long apply(AccountSuite input) {
                return input.getFlowId();
            }
        });

        for (AccountCoupon coupon : bundleCoupons) {
            Long flowId = coupon.getFlowId();
            AccountSuite suite = flowIdSuiteMap.get(flowId);
            if (suite != null) {
                suite.getCoupons().add(coupon);
            }
        }
        return suites;
    }

    private void attachInfo(Long shopID, List<AccountCoupon> freeCoupons) {
        List<Long> couponInfoIdList = Lists.transform(freeCoupons, new Function<AccountCoupon, Long>() {
            @Override
            public Long apply(AccountCoupon input) {
                return input.getCouponInfoId();
            }
        });
        Set<Long> couponInfoIdSet = Sets.newHashSet(couponInfoIdList);
        if (couponInfoIdSet.isEmpty()) {
            return;
        }
        List<CouponInfo> couponInfos = couponInfoService.findByIds(shopID, couponInfoIdSet);
        couponInfoService.attachUseRange(couponInfos);
        ImmutableMap<Long, CouponInfo> infoMap = Maps.uniqueIndex(couponInfos, new Function<CouponInfo, Long>() {
            @Override
            public Long apply(CouponInfo input) {
                return input.getId();
            }
        });
        for (AccountCoupon freeCoupon : freeCoupons) {
            freeCoupon.setCouponInfo(infoMap.get(freeCoupon.getCouponInfoId()));
        }
    }

    private Map<Long, Customer> getCustomerMap(Long shopId, Collection<Long> customerIds) {
        if (Langs.isEmpty(customerIds)) {
            return Collections.emptyMap();
        }
        Map<Long, Customer> idCustomerMap = Maps.newHashMap();
        List<Customer> customers = customerService.listByIds(shopId, customerIds);
        if (Langs.isNotEmpty(customers)) {
            for (Customer customer : customers) {
                idCustomerMap.put(customer.getId(), customer);
            }
        }
        return idCustomerMap;
    }

    private Map<Long, List<MemberCardDiscount>> getMemberCardDiscountMap(Long shopId, Collection<Long> accountIds) {
        if (Langs.isEmpty(accountIds)) {
            return Collections.emptyMap();
        }

        List<MemberCard> memberCardList = memberCardService.getUnExpiredMemberCardListByAccountIds(shopId, accountIds);
        if (Langs.isEmpty(memberCardList)) {
            return Collections.emptyMap();
        }
        // 按accountId分组
        Map<Long, List<MemberCardDiscount>> memberCardDiscountMap = Maps.newHashMap();

        for (MemberCard memberCard : memberCardList) {
            MemberCardDiscount memberCardDiscount = new MemberCardDiscount();
            memberCardDiscount.setId(memberCard.getId());
            memberCardDiscount.setTypeName(memberCard.getCardTypeName());
            memberCardDiscount.setCardNumber(memberCard.getCardNumber());
            memberCardDiscount.setBalance(memberCard.getBalance());
            memberCardDiscount.setDepositAmount(memberCard.getDepositAmount());
            memberCardDiscount.setCardPoints(memberCard.getCardPoints());
            putListMap(memberCard.getAccountId(), memberCardDiscount, memberCardDiscountMap);
        }
        return memberCardDiscountMap;
    }

    private Map<Long, Long> getSumComboCouponNum(Long shopId, Collection<Long> accountIds) {
        if (Langs.isEmpty(accountIds)) {
            return Collections.emptyMap();
        }
        Map<Long, Long> sumComboCouponNumMap = Maps.newHashMap();
        Map<Long, Long> unExpireComboNumMap = accountComboService.getUnExpireComboNum(shopId, accountIds);
        if (!org.springframework.util.CollectionUtils.isEmpty(unExpireComboNumMap)) {
            sumComboCouponNumMap = unExpireComboNumMap;
        }
        Map<Long, Long> unExpireCouponNumMap = accountCouponService.getUnExpireCouponNum(shopId, accountIds);
        if (!org.springframework.util.CollectionUtils.isEmpty(unExpireCouponNumMap)) {
            for (Long accountId : accountIds) {
                Long comboCouponNum = sumComboCouponNumMap.get(accountId);
                comboCouponNum = comboCouponNum == null ? 0 : comboCouponNum;
                Long couponNum = unExpireCouponNumMap.get(accountId);
                couponNum = couponNum == null ? 0 : couponNum;
                comboCouponNum += couponNum;
                sumComboCouponNumMap.put(accountId, comboCouponNum);
            }
        }
        return sumComboCouponNumMap;
    }

    private Map<Long, List<ComboCouponDiscount>> getComboCouponDiscount(Long shopId, Collection<Long> accountIds) {
        if (Langs.isEmpty(accountIds)) {
            return Collections.emptyMap();
        }

        // 现金券、通用券
        Map<Long, List<ComboCouponDiscount>> couponDiscountMap = getLastExpireCouponByType(shopId, accountIds);
        // 计次卡
        Map<Long, ComboCouponDiscount> coumboDiscountMap = getLastExpireCombo(shopId, accountIds);

        if (org.springframework.util.CollectionUtils.isEmpty(couponDiscountMap)) {
            couponDiscountMap = Maps.newHashMap();
        }

        if (!org.springframework.util.CollectionUtils.isEmpty(coumboDiscountMap)) {
            for (Map.Entry<Long, ComboCouponDiscount> entry : coumboDiscountMap.entrySet()) {
                Long accountId = entry.getKey();
                ComboCouponDiscount couponDiscount = entry.getValue();
                putListMap(accountId, couponDiscount, couponDiscountMap);
            }
        }
        return couponDiscountMap;
    }


    /**
     * 批量查询账户下过期时间最近每种类型一张优惠券
     *
     * @param shopId
     * @param accountIds
     * @return
     */
    private Map<Long, List<ComboCouponDiscount>> getLastExpireCouponByType(Long shopId, Collection<Long> accountIds) {
        if (Langs.isEmpty(accountIds)) {
            return Collections.emptyMap();
        }
        List<AccountCoupon> accountCouponList = accountCouponService.getUnExpireCouponList(shopId, accountIds);
        if (Langs.isEmpty(accountCouponList)) {
            return Collections.emptyMap();
        }
        // 现金券
        Map<Long, List<AccountCoupon>> cashCouponMap = Maps.newHashMap();
        // 通用券
        Map<Long, List<AccountCoupon>> commonCouponMap = Maps.newHashMap();

        for (AccountCoupon accountCoupon : accountCouponList) {
            Long accountId = accountCoupon.getAccountId();
            Integer couponType = accountCoupon.getCouponType();
            if (couponType != null) {
                if (couponType == 1) {
                    putListMap(accountId, accountCoupon, cashCouponMap);
                } else if (couponType == 2) {
                    putListMap(accountId, accountCoupon, commonCouponMap);
                }
            }
        }

        Map<Long, List<ComboCouponDiscount>> couponDiscountMap = Maps.newHashMap();
        accountCouponToCouponDiscount(cashCouponMap, couponDiscountMap);
        accountCouponToCouponDiscount(commonCouponMap, couponDiscountMap);
        return couponDiscountMap;
    }


    private void accountCouponToCouponDiscount(Map<Long, List<AccountCoupon>> couponMap, Map<Long, List<ComboCouponDiscount>> couponDiscountMap) {
        for (Map.Entry<Long, List<AccountCoupon>> entry : couponMap.entrySet()) {
            Long accountId = entry.getKey();
            List<AccountCoupon> accountCoupons = entry.getValue();
            if (Langs.isNotEmpty(accountCoupons)) {
                Collections.sort(accountCoupons, new Comparator<AccountCoupon>() {
                    @Override
                    public int compare(AccountCoupon o1, AccountCoupon o2) {
                        Date expireDate1 = o1.getExpireDate();
                        Date expireDate2 = o2.getExpireDate();
                        if (expireDate1 == null || expireDate2 == null) {
                            return 0;
                        }
                        return o1.getExpireDate().compareTo(o2.getExpireDate());
                    }
                });
                AccountCoupon accountCoupon = accountCoupons.get(0);
                ComboCouponDiscount couponDiscount = new ComboCouponDiscount();
                couponDiscount.setId(accountCoupon.getId());
                couponDiscount.setName(accountCoupon.getCouponName());
                couponDiscount.setExpireDateStr(DateFormatUtils.toYMD(accountCoupon.getExpireDate()));
                couponDiscount.setTypeName(accountCoupon.getCouponTypeName());
                String key = accountCoupon.getCouponName() + accountCoupon.getFlowId();
                int num = 0;
                for (AccountCoupon coupon : accountCoupons) {
                    String tempKey = coupon.getCouponName() + coupon.getFlowId();
                    if (key.equals(tempKey)) {
                        num++;
                    }
                }
                couponDiscount.setNum(num);
                putListMap(accountId, couponDiscount, couponDiscountMap);
            }
        }
    }


    private <K, V> void putListMap(K key, V value, Map<K, List<V>> map) {
        List<V> valueList = map.get(key);
        if (valueList == null) {
            valueList = new ArrayList<>();
            map.put(key, valueList);
        }
        valueList.add(value);
    }

    /**
     * 批量查询账户下过期时间最近的一张计次卡
     *
     * @param shopId
     * @param accountIds
     * @return
     */
    private Map<Long, ComboCouponDiscount> getLastExpireCombo(Long shopId, Collection<Long> accountIds) {
        if (Langs.isEmpty(accountIds)) {
            return Collections.emptyMap();
        }
        List<AccountCombo> accountComboList = accountComboService.getUnExpireComboList(shopId, accountIds);
        if (Langs.isEmpty(accountComboList)) {
            return Collections.emptyMap();
        }
        ImmutableListMultimap<Long, AccountCombo> multimap = Multimaps.index(accountComboList, new Function<AccountCombo, Long>() {
            @Override
            public Long apply(AccountCombo accountCombo) {
                return accountCombo.getAccountId();
            }
        });
        Map<Long, ComboCouponDiscount> lastExpireComboMap = Maps.newHashMap();
        List<Long> accountComboIds = Lists.newArrayList();
        for (Long accountId : multimap.keySet()) {
            ImmutableList<AccountCombo> accountCombos = multimap.get(accountId);
            if (Langs.isNotEmpty(accountCombos)) {
                List<AccountCombo> accountComboList1 = Lists.newArrayList(accountCombos);
                Collections.sort(accountComboList1, new Comparator<AccountCombo>() {
                    @Override
                    public int compare(AccountCombo o1, AccountCombo o2) {
                        Date expireDate1 = o1.getExpireDate();
                        Date expireDate2 = o2.getExpireDate();
                        if (expireDate1 == null || expireDate2 == null) {
                            return 0;
                        }
                        return expireDate1.compareTo(expireDate2);
                    }
                });
                AccountCombo accountCombo = accountCombos.get(0);
                ComboCouponDiscount comboDiscount = new ComboCouponDiscount();
                comboDiscount.setId(accountCombo.getId());
                comboDiscount.setTypeName("计次卡");
                comboDiscount.setName(accountCombo.getComboName());
                comboDiscount.setExpireDateStr(DateFormatUtils.toYMD(accountCombo.getExpireDate()));
                lastExpireComboMap.put(accountId, comboDiscount);
                accountComboIds.add(accountCombo.getId());
            }
        }

        if (org.springframework.util.CollectionUtils.isEmpty(lastExpireComboMap)) {
            return lastExpireComboMap;
        }

        List<AccountComboServiceRel> accountComboServiceRels = accountComboServiceRelService.listByComboIds(shopId, accountComboIds);
        Map<Long, Integer> leftServiceCountMap = Maps.newHashMap();
        if (Langs.isNotEmpty(accountComboServiceRels)) {
            for (AccountComboServiceRel accountComboServiceRel : accountComboServiceRels) {
                Integer leftServiceCount = accountComboServiceRel.getLeftServiceCount();
                leftServiceCount = leftServiceCount == null ? 0 : leftServiceCount;
                Long comboId = accountComboServiceRel.getComboId();
                Integer tempLeftServiceCount = leftServiceCountMap.get(comboId);
                tempLeftServiceCount = tempLeftServiceCount == null ? 0 : tempLeftServiceCount;
                tempLeftServiceCount += leftServiceCount;
                leftServiceCountMap.put(comboId, tempLeftServiceCount);
            }
        }

        for (ComboCouponDiscount comboCouponDiscount : lastExpireComboMap.values()) {
            Long id = comboCouponDiscount.getId();
            Integer leftServiceCount = leftServiceCountMap.get(id);
            if (leftServiceCount != null) {
                comboCouponDiscount.setNum(leftServiceCount);
            }
        }
        return lastExpireComboMap;
    }


    @Override
    public List<CustomerDiscountInfoForApp> getCustomerDiscountInfoForApp(Long shopId, Long customerCarId) {
        List<CustomerDiscountInfoForApp> customerDiscountInfoList = new ArrayList<>();
        Map<Long, Long> accountIdCustomerIdRel = getAccountIdAndCustomerIdByCarId(shopId, customerCarId);
        if (org.springframework.util.CollectionUtils.isEmpty(accountIdCustomerIdRel)) {
            return customerDiscountInfoList;
        }

        Set<Long> accountIds = accountIdCustomerIdRel.keySet();
        Collection<Long> customerIds = accountIdCustomerIdRel.values();

        Map<Long, Customer> customerMap = getCustomerMap(shopId, customerIds);
        Map<Long, List<MemberCardDiscount>> memberCardDiscountMap = getMemberCardDiscountMap(shopId, accountIds);
        Map<Long, List<ComboDiscount>> comboDiscountMap = getComboDiscount(shopId, accountIds);
        Map<Long, List<CouponDiscount>> couponDiscountMap = getCouponDiscount(shopId, accountIds);

        for (Map.Entry<Long, Long> entry : accountIdCustomerIdRel.entrySet()) {
            Long accountId = entry.getKey();
            Long customerId = entry.getValue();
            Customer customer = customerMap.get(customerId);
            if (customer != null) {
                CustomerDiscountInfoForApp customerDiscountInfo = new CustomerDiscountInfoForApp();
                customerDiscountInfo.setCustomerId(customerId);
                customerDiscountInfo.setCustomerName(customer.getCustomerName());
                customerDiscountInfo.setMobile(customer.getMobile());
                customerDiscountInfo.setMemberCardDiscountList(memberCardDiscountMap.get(accountId));
                customerDiscountInfo.setComboDiscountList(comboDiscountMap.get(accountId));
                customerDiscountInfo.setCouponDiscountList(couponDiscountMap.get(accountId));
                customerDiscountInfoList.add(customerDiscountInfo);
            }
        }
        return customerDiscountInfoList;
    }

    /**
     * 根据车辆id查询归属和关联账户
     *
     * @param shopId
     * @param customerCarId
     * @return key 账户id，value 客户id
     */
    @Override
    public Map<Long, Long> getAccountIdAndCustomerIdByCarId(Long shopId, Long customerCarId) {
        Assert.isTrue(shopId != null && shopId > 0);
        Assert.isTrue(customerCarId != null && customerCarId > 0);

        Map<Long, Long> accountIdCustomerIdRel = Maps.newLinkedHashMap();
        AccountInfo accountInfo = accountInfoService.getAccountInfoByCarIdAndShopId(shopId, customerCarId);
        if (accountInfo != null) {
            accountIdCustomerIdRel.put(accountInfo.getId(), accountInfo.getCustomerId());
        }
        List<CustomerCarRel> customerCarRels = customerCarRelService.listByCarId(shopId, customerCarId);
        if (Langs.isNotEmpty(customerCarRels)) {
            for (CustomerCarRel customerCarRel : customerCarRels) {
                accountIdCustomerIdRel.put(customerCarRel.getAccountId(), customerCarRel.getCustomerId());
            }
        }
        return accountIdCustomerIdRel;
    }

    private Map<Long, List<ComboDiscount>> getComboDiscount(Long shopId, Collection<Long> accountIds) {
        Map<Long, List<ComboDiscount>> comboDiscountMap = Maps.newHashMap();
        if (Langs.isEmpty(accountIds)) {
            return comboDiscountMap;
        }
        List<AccountCombo> accountComboList = accountComboService.getUnExpireComboList(shopId, accountIds);
        if (Langs.isEmpty(accountComboList)) {
            return comboDiscountMap;
        }
        List<Long> accountComboIds = Lists.newArrayList();
        List<Long> comboInfoIds = Lists.newArrayList();
        for (AccountCombo accountCombo : accountComboList) {
            accountComboIds.add(accountCombo.getId());
            comboInfoIds.add(accountCombo.getComboInfoId());
        }

        // 查询剩余服务次数
        ImmutableListMultimap<Long, AccountComboServiceRel> multimap = null;
        List<AccountComboServiceRel> accountComboServiceRels = accountComboServiceRelService.listByComboIds(shopId, accountComboIds);
        if (Langs.isNotEmpty(accountComboServiceRels)) {
            multimap = Multimaps.index(accountComboServiceRels, new Function<AccountComboServiceRel, Long>() {
                @Override
                public Long apply(AccountComboServiceRel accountComboServiceRel) {
                    return accountComboServiceRel.getComboId();
                }
            });
        }

        List<ComboInfo> comboInfos = comboInfoService.listByIds(comboInfoIds);
        Map<Long, String> comboRemarkMap = Maps.newHashMap();
        if (Langs.isNotEmpty(comboInfos)) {
            for (ComboInfo comboInfo : comboInfos) {
                comboRemarkMap.put(comboInfo.getId(), comboInfo.getRemark());
            }
        }

        for (AccountCombo accountCombo : accountComboList) {
            Long accountId = accountCombo.getAccountId();
            Long accountComboId = accountCombo.getId();
            ComboDiscount comboDiscount = new ComboDiscount();
            comboDiscount.setAccountComboId(accountComboId);
            comboDiscount.setComboName(accountCombo.getComboName());
            comboDiscount.setEffectiveDate(accountCombo.getEffectiveDate());
            comboDiscount.setExpireDate(accountCombo.getExpireDate());
            List<ComboServiceVo> comboServiceVoList = Lists.newArrayList();
            if (multimap != null) {
                ImmutableList<AccountComboServiceRel> immutableList = multimap.get(accountComboId);
                if (Langs.isNotEmpty(immutableList)) {
                    for (AccountComboServiceRel accountComboServiceRel : immutableList) {
                        ComboServiceVo comboServiceVo = new ComboServiceVo();
                        comboServiceVo.setName(accountComboServiceRel.getServiceName());
                        comboServiceVo.setTotalCount(accountComboServiceRel.getTotalServiceCount());
                        comboServiceVo.setUsedCount(accountComboServiceRel.getUsedServiceCount());
                        comboServiceVo.setLeftCount(accountComboServiceRel.getLeftServiceCount());
                        comboServiceVoList.add(comboServiceVo);
                    }
                }
            }
            comboDiscount.setComboServiceList(comboServiceVoList);
            String remark = comboRemarkMap.get(accountCombo.getComboInfoId());
            remark = remark == null ? "" : remark;
            comboDiscount.setDescription(remark);
            putListMap(accountId, comboDiscount, comboDiscountMap);
        }
        return comboDiscountMap;
    }

    private Map<Long, List<CouponDiscount>> getCouponDiscount(Long shopId, Collection<Long> accountIds) {
        Map<Long, List<CouponDiscount>> couponDiscountMap = Maps.newHashMap();
        if (Langs.isEmpty(accountIds)) {
            return couponDiscountMap;
        }
        List<AccountCoupon> accountCouponList = accountCouponService.getUnExpireCouponList(shopId, accountIds);
        if (Langs.isEmpty(accountCouponList)) {
            return couponDiscountMap;
        }
        List<Long> couponInfoIds = Lists.transform(accountCouponList, new Function<AccountCoupon, Long>() {
            @Override
            public Long apply(AccountCoupon accountCoupon) {
                return accountCoupon.getCouponInfoId();
            }
        });
        List<CouponInfo> couponInfoList = couponInfoService.findByIds(shopId, couponInfoIds);
        ImmutableMap<Long, CouponInfo> couponInfoMap = null;
        if (Langs.isNotEmpty(couponInfoList)) {
            couponInfoMap = Maps.uniqueIndex(couponInfoList, new Function<CouponInfo, Long>() {
                @Override
                public Long apply(CouponInfo couponInfo) {
                    return couponInfo.getId();
                }
            });
        }

        for (AccountCoupon accountCoupon : accountCouponList) {
            Long accountId = accountCoupon.getAccountId();
            CouponDiscount couponDiscount = new CouponDiscount();
            couponDiscount.setAccountCouponId(accountCoupon.getId());
            couponDiscount.setCouponType(accountCoupon.getCouponType());
            couponDiscount.setCouponName(accountCoupon.getCouponName());
            couponDiscount.setTypeName(accountCoupon.getCouponTypeName());
            couponDiscount.setEffectiveDate(accountCoupon.getEffectiveDate());
            couponDiscount.setExpireDate(accountCoupon.getExpireDate());
            if (couponInfoMap != null) {
                CouponInfo couponInfo = couponInfoMap.get(accountCoupon.getCouponInfoId());
                if (couponInfo != null) {
                    couponDiscount.setUseRange(couponInfo.getUseRange());
                    couponDiscount.setUseRangeDescript(couponInfo.getUseRangeDescript());
                    couponDiscount.setDescription(couponInfo.getRemark());
                }
            }
            couponDiscount.setFlowId(accountCoupon.getFlowId());
            putListMap(accountId, couponDiscount, couponDiscountMap);
        }
        return couponDiscountMap;
    }
}
