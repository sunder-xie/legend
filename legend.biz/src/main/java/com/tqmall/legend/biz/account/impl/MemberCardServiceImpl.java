package com.tqmall.legend.biz.account.impl;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.BizTemplate;
import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.biz.account.*;
import com.tqmall.legend.biz.account.bo.CardUpgradeBo;
import com.tqmall.legend.biz.account.vo.BackCardVo;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.customer.CustomerService;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.dao.account.AccountCardFlowDetailDao;
import com.tqmall.legend.dao.account.AccountTradeFlowDao;
import com.tqmall.legend.dao.account.MemberCardDao;
import com.tqmall.legend.entity.account.*;
import com.tqmall.legend.entity.account.vo.MemberCardChargeVo;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.facade.account.vo.MemberCardInfosVo;
import com.tqmall.legend.facade.account.vo.MemberUpgradeVo;
import com.tqmall.legend.log.MarketCardCouponLog;
import com.tqmall.wheel.lang.Langs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by wanghui on 6/2/16.
 */
@Service
@Slf4j
public class MemberCardServiceImpl extends BaseServiceImpl implements MemberCardService {
    @Autowired
    private MemberCardDao memberCardDao;
    @Autowired
    private MemberCardInfoService memberCardInfoService;
    @Autowired
    private ShopManagerService shopManagerService;
    @Autowired
    private AccountCardFlowDetailDao accountCardFlowDetailDao;
    @Autowired
    private AccountTradeFlowDao accountTradeFlowDao;
    @Autowired
    private AccountFlowService accountFlowService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private AccountInfoService accountInfoService;
    @Autowired
    private AccountTradeFlowService accountTradeFlowService;
    @Autowired
    private AccountCardFlowDetailService accountCardFlowDetailService;

    public boolean saveMemberCard(MemberCard memberCard) {
        // 记录log
        log.info(MarketCardCouponLog.grantCardLog(memberCard.getShopId()));

        return super.save(memberCardDao, memberCard);
    }

    @Override
    public boolean isExist(Long shop, Long accountId) {
        return memberCardDao.countByAccountId(shop, accountId) > 0;
    }

    @Override
    public boolean isExistCardNumber(Long shopId, String cardNumber, Long memberCardId) {
        List<MemberCard> memberCards = memberCardDao.isExistCardNumber(shopId, cardNumber);
        if (CollectionUtils.isEmpty(memberCards)) {
            return false;
        } else if (memberCards.size() > 1) {
            return true;
        } else if (memberCards.get(0).getId().equals(memberCardId)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public Result queryAccountMemberCardType(Long shopId, Long accountId) {
        List<MemberCardInfosVo> memberCardInfosVos = Lists.newArrayList();

        List<MemberCard> memberCards = memberCardDao.getMemberCardListByAccountId(accountId);
        if (Langs.isEmpty(memberCards)) {
            return Result.wrapSuccessfulResult(memberCardInfosVos);
        }
        List<Long> cardIds = Lists.newArrayList();
        List<Long> cardTypeIds = Lists.newArrayList();
        for (MemberCard memberCard : memberCards) {
            cardIds.add(memberCard.getId());
            cardTypeIds.add(memberCard.getCardTypeId());
        }
        List<MemberCardInfo> memberCardInfos = memberCardInfoService.selectInfoByIds(shopId, cardTypeIds);
        memberCardInfoService.attachDiscount(memberCardInfos);
        ImmutableMap<Long, MemberCardInfo> memberCardInfoMap = Maps.uniqueIndex(memberCardInfos, new Function<MemberCardInfo, Long>() {
            @Override
            public Long apply(MemberCardInfo memberCardInfo) {
                return memberCardInfo.getId();
            }
        });
        for (MemberCard card : memberCards) {
            MemberCardInfosVo vo = new MemberCardInfosVo();
            vo.setExpired(card.isExpired());
            MemberCardInfo memberCardInfo = memberCardInfoMap.get(card.getCardTypeId());
            if (memberCardInfo != null) {
                setProperties(vo, card, memberCardInfo);
            }
            memberCardInfosVos.add(vo);
        }

        return Result.wrapSuccessfulResult(memberCardInfosVos);
    }

    @Override
    public MemberCard consume(Long shopId, Long userId, MemberCard memberCard, BigDecimal payAmount) {
        subBalance(memberCard, payAmount);
        addExpenseAmount(payAmount, memberCard);
        memberCard.setModifier(userId);
        update(memberCard);
        return memberCard;
    }

    private void validateExpire(MemberCard memberCard) {
        if (memberCard.isExpired()) {
            throw new BizException("会员卡已过期");
        }
    }

    private void subBalance(MemberCard memberCard, BigDecimal payAmount) {
        BigDecimal changeAmount = payAmount.negate();
        changeBalance(memberCard, changeAmount);
    }

    @Override
    public MemberCard revertRecharge(Long shopId, Long userId, Long cardId, BigDecimal amount) {
        MemberCard memberCard = this.findById(cardId);
        if (memberCard == null) {
            throw new BizException("会员卡已退卡，无法撤销");
        }
        validateExpire(memberCard);
        subBalance(memberCard, amount);
        memberCard.changeDepositAmount(amount.negate());
        memberCard.subDepositCount();
        memberCard.setModifier(userId);
        update(memberCard);
        return memberCard;
    }

    @Override
    public MemberCard revertConsume(Long shopId, Long userId, Long cardId, BigDecimal payAmount) {
        Assert.notNull(cardId,"cardId不能为空.");
        MemberCard memberCard = findByIdContainDeleted(shopId, cardId);
        if (memberCard == null) {
            throw new BizException("cardId=" + cardId + "的账户无会员卡");
        }
        subBalance(memberCard, payAmount);
        addExpenseAmount(payAmount,memberCard);
        memberCard.setModifier(userId);
        update(memberCard);
        return memberCard;
    }

    private MemberCard changeBalance(MemberCard memberCard, BigDecimal changeAmount) {

        BigDecimal oldBalance = memberCard.getBalance();
        BigDecimal newBalance = oldBalance.add(changeAmount);
        memberCard.setBalance(newBalance);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new BizException("会员卡余额不足，无法撤销。");
        }
        return memberCard;
    }

    private void setProperties(MemberCardInfosVo vo, MemberCard memberCard, MemberCardInfo memberCardInfo) {

        attachCardThings(vo, memberCard);
        attachInfoThins(vo, memberCardInfo);
        //设置使用次数
        Integer count = getUsedCount(memberCard.getShopId(), memberCard.getId());
        vo.setUsedNum(count);
        //设置操作人
        setPublisherName(vo, memberCard);
    }

    private void attachCardThings(MemberCardInfosVo vo, MemberCard memberCard) {
        vo.setShopId(memberCard.getShopId());
        vo.setBuyTime(DateUtil.convertDateToYMD(memberCard.getGmtCreate()));
        vo.setCardNum(memberCard.getCardNumber());
        vo.setOutTimeDate(memberCard.getExpireDateStr());
    }

    private Integer getUsedCount(Long shopId, Long memberCardId) {
        Map paramMap = Maps.newHashMap();
        paramMap.put("shopId", shopId);
        paramMap.put("cardId", memberCardId);
        paramMap.put("consumeType", AccountConsumeTypeEnum.CONSUME.getCode());
        Integer consumeCount = accountCardFlowDetailDao.selectCount(paramMap);
        paramMap.put("consumeType", AccountConsumeTypeEnum.CONSUME_REVERT.getCode());
        Integer revertCount = accountCardFlowDetailDao.selectCount(paramMap);
        return consumeCount - revertCount;
    }

    private void attachInfoThins(MemberCardInfosVo vo, MemberCardInfo memberCardInfo) {
        vo.setCompatibleWithCoupon(memberCardInfo.getCompatibleWithCoupon());
        vo.setCardInfoExplain(memberCardInfo.getCardInfoExplain());
        vo.setOrderDiscount(memberCardInfo.getOrderDiscount());
        vo.setEffectivePeriodDays(memberCardInfo.getEffectivePeriodDays());
        vo.setGeneralUse(memberCardInfo.getGeneralUse());
        vo.setGoodDiscount(memberCardInfo.getGoodDiscount());
        vo.setGoodDiscountType(memberCardInfo.getGoodDiscountType());
        vo.setInitBalance(memberCardInfo.getInitBalance());
        vo.setSalePrice(memberCardInfo.getSalePrice());
        vo.setTypeName(memberCardInfo.getTypeName());
        vo.setServiceDiscount(memberCardInfo.getServiceDiscount());
        vo.setServiceDiscountType(memberCardInfo.getServiceDiscountType());
        vo.setDiscountType(memberCardInfo.getDiscountType());
        vo.setDiscountDescript(memberCardInfo.getDiscountDescription());
    }

    private void setPublisherName(MemberCardInfosVo vo, MemberCard memberCard) {
        List<ShopManager> shopManagers = shopManagerService.selectByShopId(memberCard.getShopId());
        for (ShopManager shopManager : shopManagers) {
            if (shopManager.getId().equals(memberCard.getPublisher())) {
                vo.setPublisherName(shopManager.getName());
            }
        }
    }


    @Override
    public void delete(Long shopId) {
        Map param = Maps.newHashMap();
        if (shopId != null) {
            param.put("shopId", shopId);
        }
        memberCardDao.delete(param);
    }

    @Override
    public void addExpenseCount(MemberCard card) {
        changeExpenseCount(card, 1);
        update(card);
    }

    @Override
    public void subExpenseCount(MemberCard card) {
        changeExpenseCount(card, -1);
        update(card);
    }

    private void addExpenseAmount(BigDecimal amount, MemberCard card) {
        changeExpenseAmount(amount, card);
    }

    private void changeExpenseCount(MemberCard memberCard, Integer count) {

        Integer oldExpenseCount = memberCard.getExpenseCount();
        Integer newExpenseCount = oldExpenseCount + count;
        if (Integer.valueOf(0).compareTo(newExpenseCount) > 0) {
            newExpenseCount = 0;
            if (log.isWarnEnabled()) {
                log.warn("memberId:{},消费次数回滚异常(expenseCount小于<0)", memberCard.getId());
            }
        }
        memberCard.setExpenseCount(newExpenseCount);
    }

    private void changeExpenseAmount(BigDecimal amount, MemberCard card) {
        BigDecimal oldExpenseAmount = card.getExpenseAmount();
        BigDecimal newExpenseAmount = oldExpenseAmount.add(amount);
        card.setExpenseAmount(newExpenseAmount);
    }

    @Override
    public Page<MemberCard> getMemberCardPages(Pageable pageable, Map searchMap) {
        return this.getPage(memberCardDao, pageable, searchMap);
    }

    @Override
    public List<MemberCard> findByAccountIds(List<Long> accountIds) {
        return attachMemberCardInfo(memberCardDao.findByAccountIds(accountIds));
    }

    @Override
    public MemberCard getMemberCardById(Long shopId, Long cardId) {
        Map param = Maps.newHashMap();
        param.put("shopId", shopId);
        param.put("id", cardId);
        List<MemberCard> cards = memberCardDao.select(param);
        if (cards.isEmpty()) {
            return null;
        }
        return cards.get(0);
    }

    @Override
    public MemberCard getMemberCardByNumber(Long shopId, String cardNumber) {
        Map param = Maps.newHashMap();
        param.put("shopId", shopId);
        param.put("cardNumber", cardNumber);
        List<MemberCard> memberCardList = memberCardDao.select(param);
        if (!CollectionUtils.isEmpty(memberCardList)) {
            return memberCardList.get(0);
        }
        return null;
    }

    @Override
    public boolean update(MemberCard memberCard) {
        memberCard.setGmtModified(new Date());
        return memberCardDao.updateById(memberCard) > 0 ? true : false;
    }

    @Override
    public boolean backCard(BackCardVo backCardVo) {
        Long shopId = backCardVo.getShopId();
        Long cardId = backCardVo.getCardId();
        MemberCard memberCard = this.selectByIdAndShopId(this.memberCardDao, cardId, shopId);
        Assert.notNull(memberCard, "根据店铺id和会员id查询会员信息为空.");
        accountFlowService.recordFlowForCardBack(backCardVo, memberCard);

        return this.memberCardDao.deleteById(cardId) > 0;
    }

    @Override
    public boolean insert(MemberCard memberCard) {
        return memberCardDao.insert(memberCard) > 0 ? true : false;
    }

    @Override
    public MemberCard findById(Long id) {
        return memberCardDao.selectById(id);
    }

    @Override
    public AccountTradeFlow findFlowByFlowId(Long shopId, Long flowId) {
        return accountTradeFlowDao.selectByIdAndShopId(shopId, flowId);
    }

    @Override
    public AccountCardFlowDetail findCardFlowByFlowId(Long shopId, Long flowId) {
        Map param = Maps.newHashMap();
        param.put("shopId", shopId);
        param.put("accountTradeFlowId", flowId);
        List list = accountCardFlowDetailDao.select(param);
        if (Langs.isNotEmpty(list)) {
            return (AccountCardFlowDetail) list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public MemberCard findByCardNumber(Long shopId, String cardNumber) {
        Map param = Maps.newHashMap();
        param.put("shopId", shopId);
        param.put("cardNumber", cardNumber);
        List<MemberCard> memberCards = memberCardDao.select(param);
        if (memberCards.size() == 1) {
            return memberCards.get(0);
        } else if (memberCards.size() > 1) {
            log.error("发现一个会员卡号对应多个会员卡的脏数据!cardNumber:{}", cardNumber);
            return null;
        } else {
            return null;
        }
    }

    @Override
    public void deleteMemberCard(Long cardId) {
        memberCardDao.deleteById(cardId);
    }

    @Override
    public List<MemberCard> findAccountIdsWithDel(List<Long> accountIds) {
        return memberCardDao.findAccountIdsWithDel(accountIds);
    }

    @Override
    public Integer batchInsert(List<MemberCard> memberCardList) {
        return super.batchInsert(memberCardDao, memberCardList, 1000);
    }

    @Override
    public List<Long> selectAccountIdsByShopId(Long shopId) {
        Map param = Maps.newHashMap();
        param.put("shopId", shopId);
        Integer size = memberCardDao.selectCount(param);
        List<Long> accountIds = new ArrayList<>();
        for (int i = 0; i < size; i += 2000) {
            List<MemberCard> memberCards = memberCardDao.select(param);
            if (!CollectionUtils.isEmpty(memberCards)) {
                for (MemberCard item : memberCards) {
                    accountIds.add(item.getAccountId());
                }
            }
        }
        return accountIds;
    }

    @Override
    public List<Long> selectGrantedAccountIds(List<Long> accountIds, Long shopId) {
        return memberCardDao.selectGrantedAccountIds(accountIds, shopId);
    }

    @Override
    public List<String> selectExistedCardNumbers(Long shopId, List<String> cardNumbers) {
        return memberCardDao.selectExistedCardNumbers(shopId, cardNumbers);
    }

    @Override
    public boolean isMemberCardExpire(Date date) {
        if (-1 == date.compareTo(new Date())) {
            return false;
        }
        return true;
    }

    @Override
    public List<MemberCard> getMemberCards(Map param) {
        return memberCardDao.select(param);
    }

    @Override
    public List<Long> getAccountIdListByMemberCardInfoId(Long id, Long shopId, String cardNum) {
        Map param = Maps.newHashMap();
        param.put("shopId", shopId);
        param.put("cardInfoId", id);
        param.put("cardNumber", cardNum);
        List<MemberCard> memberCards = memberCardDao.select(param);
        List<Long> accountIds = null;
        accountIds = Lists.transform(memberCards, new Function<MemberCard, Long>() {
            @Override
            public Long apply(MemberCard memberCard) {
                return memberCard.getAccountId();
            }
        });
        return accountIds;
    }

    @Override
    public List<MemberCard> listByAccountIds(Long shopId, Collection<Long> accountIds) {
        if (CollectionUtils.isEmpty(accountIds)) {
            return Lists.newArrayList();
        }
        return memberCardDao.selectByAccountIds(shopId, accountIds);
    }

    @Override
    public AccountTradeFlow recharge(final Long shopId, final Long operatorId, final MemberCardChargeVo memberCardChargeVo) {
        return new BizTemplate<AccountTradeFlow>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Preconditions.checkNotNull(shopId, "店铺id不能为空.");
                Preconditions.checkNotNull(memberCardChargeVo, "充值实体不能为空.");
                Preconditions.checkArgument(memberCardChargeVo.getCardId() != null && memberCardChargeVo.getCardId() > 0, "会员卡Id不能为空");
                BigDecimal amount = memberCardChargeVo.getAmount();
                Preconditions.checkNotNull(amount, "充值金额不能为空.");
                Preconditions.checkArgument(amount.compareTo(BigDecimal.ZERO) >= 0, "会员卡充值金额不能小于零");
                BigDecimal payAmount = memberCardChargeVo.getPayAmount();
                Preconditions.checkNotNull(payAmount, "支付金额不能为空.");
                Preconditions.checkArgument(payAmount.compareTo(BigDecimal.ZERO) >= 0, "会员卡收款金额不能小于零");
                Preconditions.checkNotNull(memberCardChargeVo.getPaymentId(), "支付方式不能为空.");
            }

            @Override
            protected AccountTradeFlow process() throws BizException {
                MemberCard memberCard = memberCardDao.selectById(memberCardChargeVo.getCardId());
                if (memberCard == null) {
                    throw new BizException("会员卡不存在，memberCardId=" + memberCardChargeVo.getCardId());
                }
                AccountInfo accountInfo = accountInfoService.getAccountInfoById(memberCard.getAccountId());
                if (accountInfo == null) {
                    throw new BizException("会员不存在，accountId=" + memberCard.getAccountId());
                }
                if (null != memberCard.getExpireDate() && isMemberCardExpire(memberCard.getExpireDate())) {
                    memberCard.addDepositCount();
                    memberCard.changeBalance(memberCardChargeVo.getAmount());
                    memberCard.changeDepositAmount(memberCardChargeVo.getAmount());
                    if (update(memberCard)) {
                        return accountFlowService.recordFlowForCardCharge(shopId, operatorId, memberCardChargeVo, accountInfo, memberCard);
                    }
                } else {
                    throw new BizException("会员已过期，充值失败");
                }
                return null;
            }
        }.execute();
    }

    @Override
    public boolean upgradeCard(final CardUpgradeBo cardUpgradeBo) {
        return new BizTemplate<Boolean>() {
            protected MemberCard memberCard;
            protected AccountInfo accountInfo;
            protected Customer customer;
            protected MemberCardInfo oldCardInfo;
            protected MemberCardInfo newCardInfo;

            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(cardUpgradeBo.getShopId(), "店铺id不能为空。");
                Assert.notNull(cardUpgradeBo.getOperatorId(), "操作人id不能为空。");
                Assert.notNull(cardUpgradeBo.getAccountId(), "待升级账户id不能为空。");
                Assert.notNull(cardUpgradeBo.getNewCardTypeId(), "待升级的会员卡类型不能为空。");
                Assert.notNull(cardUpgradeBo.getExpireTime(), "会员卡过期时间不能为空。");

                memberCard = findById(cardUpgradeBo.getCardId());
                if (Langs.isNull(memberCard)) {
                    throw new BizException("根据账户获取会员信息失败,会员信息不存在.");
                }
                if (this.memberCard.getCardTypeId().equals(cardUpgradeBo.getNewCardTypeId())) {
                    throw new BizException("升级后的会员卡类型不能与原会员卡类型一样.");
                }
                oldCardInfo = memberCardInfoService.findById(cardUpgradeBo.getShopId(), memberCard.getCardTypeId());
                if (Langs.isNull(oldCardInfo)) {
                    throw new BizException("待升级的会员卡类型获取异常.");
                }

                accountInfo = accountInfoService.getAccountInfoById(cardUpgradeBo.getShopId(), cardUpgradeBo.getAccountId());
                if (Langs.isNull(accountInfo)) {
                    throw new BizException("获取会员账户信息失败。");
                }
                customer = customerService.selectById(accountInfo.getCustomerId(), cardUpgradeBo.getShopId());
                if (Langs.isNull(customer)) {
                    throw new BizException("获取客户信息异常。");
                }

                newCardInfo = memberCardInfoService.findById(cardUpgradeBo.getShopId(), cardUpgradeBo.getNewCardTypeId());
                if (Langs.isNull(newCardInfo)) {
                    throw new BizException("升级的会员卡类型获取异常.");
                }
            }

            @Override
            protected Boolean process() throws BizException {
                MemberCard newCardMember = assmeblyNewCard();

                /**
                 * 1.删除原会员卡信息
                 */
                deleteMemberCard(memberCard.getId());
                /**
                 * 2.持久化新会员卡信息
                 */
                saveMemberCard(newCardMember);
                /**
                 * 3.记录会员卡升级流水
                 */_logFlow(newCardMember);

                return Boolean.TRUE;
            }

            private MemberCard assmeblyNewCard() {
                //新会员卡信息组装
                MemberCard newCardMember = new MemberCard();
                newCardMember.setCreator(cardUpgradeBo.getOperatorId());
                newCardMember.setModifier(cardUpgradeBo.getOperatorId());
                newCardMember.setShopId(this.memberCard.getShopId());
                newCardMember.setCardNumber(this.memberCard.getCardNumber());
                newCardMember.setBalance(this.memberCard.getBalance());
                newCardMember.setExpenseAmount(this.memberCard.getExpenseAmount());
                newCardMember.setExpenseCount(this.memberCard.getExpenseCount());
                /**
                 * 服务顾问
                 */
                newCardMember.setReceiver(this.memberCard.getReceiver());
                newCardMember.setReceiverName(this.memberCard.getReceiverName());
                newCardMember.setDepositAmount(this.memberCard.getDepositAmount());
                newCardMember.setDepositCount(this.memberCard.getDepositCount());
                /**
                 * 发卡人
                 */
                newCardMember.setPublisher(this.memberCard.getPublisher());
                newCardMember.setPublisherName(this.memberCard.getPublisherName());

                newCardMember.setCardTypeId(cardUpgradeBo.getNewCardTypeId());
                newCardMember.setExpireDate(cardUpgradeBo.getExpireTime());
                newCardMember.setAccountId(cardUpgradeBo.getAccountId());
                /**
                 * 关联旧会员卡id
                 */
                newCardMember.setRawCardId(this.memberCard.getId());
                return newCardMember;
            }

            private void _logFlow(MemberCard newCardMember) {
                //3.1记录会员卡升级主流水
                MemberUpgradeVo memberUpgradeVo = new MemberUpgradeVo();
                memberUpgradeVo.setCustomerName(customer.getCustomerName());
                memberUpgradeVo.setMobile(customer.getMobile());
                memberUpgradeVo.setAccountId(cardUpgradeBo.getAccountId());
                memberUpgradeVo.setCardBalance(newCardMember.getBalance());
                memberUpgradeVo.setReceiver(newCardMember.getReceiver());
                memberUpgradeVo.setReceiverName(newCardMember.getReceiverName());
                memberUpgradeVo.setOldCardTypeName(oldCardInfo.getTypeName());
                memberUpgradeVo.setNewCardTypeName(newCardInfo.getTypeName());

                AccountTradeFlow accountTradeFlow = accountTradeFlowService.recordFlowForUpgradeMemberCard(cardUpgradeBo.getShopId(), cardUpgradeBo.getOperatorId(), memberUpgradeVo);
                //3.2记录会员卡升级流水明细
                accountCardFlowDetailService.recordDetailForUpgrade(cardUpgradeBo.getShopId(), newCardMember.getBalance(), newCardMember.getId(), this.accountInfo, accountTradeFlow.getId(), cardUpgradeBo.getOperatorId());
            }
        }.execute();
    }

    @Override
    public List<MemberCard> getMemberCardWithDeletedByIds(Long shopId, List<Long> memberCardIds) {
        Assert.notNull(shopId, "门店id不能为空.");
        Assert.notEmpty(memberCardIds, "会员卡id不能为空.");
        return memberCardDao.getMemberCardWithDeletedByIds(shopId, memberCardIds);
    }

    @Override
    public List<MemberCard> getUpgradedCardsContainDeletedById(final Long shopId, final Long cardId) {
        return new BizTemplate<List<MemberCard>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(shopId);
                Assert.notNull(cardId);
            }

            @Override
            protected List<MemberCard> process() throws BizException {
                Deque<MemberCard> cardList = new ArrayDeque();
                MemberCard memberCard = memberCardDao.findByIdContainDeleted(shopId, cardId);
                if (Langs.isNull(memberCard)) {
                    throw new BizException("根据id" + cardId + "获取会员卡信息失败.");
                }
                cardList.addFirst(memberCard);

                //升级之前的路径
                if (Langs.isNotNull(memberCard.getRawCardId()) &&
                        !Long.valueOf(0).equals(memberCard.getRawCardId())) {
                    MemberCard upMemberCard = memberCardDao.findByIdContainDeleted(shopId, memberCard.getRawCardId());
                    cardList.addFirst(upMemberCard);
                    while (Langs.isNotNull(upMemberCard.getRawCardId())
                            && !Long.valueOf(0).equals(upMemberCard.getRawCardId())) {
                        upMemberCard = memberCardDao.findByIdContainDeleted(shopId, upMemberCard.getRawCardId());
                        cardList.addFirst(upMemberCard);
                    }
                }

                //升级之后的路径
                MemberCard downMemberCard = memberCardDao.findByRawIdContainDeleted(shopId, memberCard.getId());

                while (Langs.isNotNull(downMemberCard)) {
                    cardList.addLast(downMemberCard);
                    downMemberCard = memberCardDao.findByRawIdContainDeleted(shopId, downMemberCard.getId());
                }
                return new ArrayList<>(cardList);
            }
        }.execute();
    }

    @Override
    public MemberCard getUpgradedMemberCard(Long shopId, Long cardId) {
        List<MemberCard> cardList = getUpgradedCardsContainDeletedById(shopId, cardId);
        if (Langs.isNotEmpty(cardList)) {
            return cardList.get(cardList.size() - 1);
        } else {
            throw new BizException("获取会员卡升级路径异常");
        }
    }

    @Override
    public boolean grantMemberCardAble(Long shopId, Long accountId,Long typeId) {
        Assert.notNull(shopId ,"门店id不能为空");
        Assert.notNull(accountId ,"账户id不能为空");
        Map param = Maps.newHashMap();
        param.put("shopId",shopId);
        param.put("accountId",accountId);
        List<MemberCard> unExpireList = this.getUnExpiredMemberCardListByAccountId(shopId,accountId);
        if (CollectionUtils.isEmpty(unExpireList)) {
            return true;
        } else if (unExpireList.size() >= 3) {
            return false;
        } else if (checkDuplicate(unExpireList,typeId)){
            return false;
        } else {
            return true;
        }
    }

    public boolean checkDuplicate(List<MemberCard> memberCards , Long typeId){
        if (CollectionUtils.isEmpty(memberCards)) {
            return false;
        }
        Set typeIdSet = Sets.newHashSet(Lists.transform(memberCards, new Function<MemberCard, Long>() {
            @Override
            public Long apply(MemberCard o) {
                return o.getCardTypeId();
            }
        }));
        if (typeIdSet.contains(typeId)) {
            return true;
        }
        return false;

    }

    @Override
    public MemberCard getMaxIdMemberCard(Long shopId) {
        Assert.notNull(shopId,"门店id不能为空.");
        return memberCardDao.findMaxMemberCardId(shopId);
    }

    @Override
    public List<MemberCard> getMemberCardWithCardInfoListByAccountId(Long accountId) {
        Assert.notNull(accountId,"账户id不能为空.");
        List<MemberCard> memberCards = memberCardDao.getMemberCardListByAccountId(accountId);

        return attachMemberCardInfo(memberCards);
    }

    private List<MemberCard> attachMemberCardInfo(List<MemberCard> memberCards){
        if (CollectionUtils.isEmpty(memberCards)) {
            return Lists.newArrayList();
        }
        List<Long> typeIds = Lists.transform(memberCards, new Function<MemberCard, Long>() {
            @Override
            public Long apply(MemberCard memberCard) {
                return memberCard.getCardTypeId();
            }
        });
        List<MemberCardInfo> memberCardInfos = memberCardInfoService.selectInfoByIds(memberCards.get(0).getShopId(),typeIds);
        memberCardInfoService.attachDiscount(memberCardInfos);
        Map<Long,MemberCardInfo> memberCardInfoMap = Maps.newHashMap();
        for (MemberCardInfo memberCardInfo : memberCardInfos) {
            memberCardInfoMap.put(memberCardInfo.getId(),memberCardInfo);
        }
        for (MemberCard memberCard : memberCards) {
            MemberCardInfo memberCardInfo = memberCardInfoMap.get(memberCard.getCardTypeId());
            if (null != memberCardInfo) {
                memberCard.setCardTypeName(memberCardInfo.getTypeName());
                memberCard.setMemberCardInfo(memberCardInfo);
            }
        }
        return memberCards;
    }

    @Override
    public List<MemberCard> getUnExpiredMemberCardListByAccountId(Long shopId,Long accountId) {
        Assert.notNull(accountId,"账户id不能为空.");
        Map param = Maps.newHashMap();
        param.put("shopId",shopId);
        param.put("accountId",accountId);
        return attachMemberCardInfo(getUnExpireMemberCard(this.getMemberCards(param)));
    }

    @Override
    public List<MemberCard> getUnExpiredMemberCardListByAccountIds(Long shopId, Collection<Long> accountIds) {
        if (Langs.isEmpty(accountIds)) {
            return Collections.emptyList();
        }
        return attachMemberCardInfo(getUnExpireMemberCard(listByAccountIds(shopId, accountIds)));
    }

    private List<MemberCard> getUnExpireMemberCard(List<MemberCard> memberCards){
        List<MemberCard> unExpireList = Lists.newArrayList();
        for (MemberCard memberCard : memberCards) {
            if (!memberCard.isExpired()) {
                unExpireList.add(memberCard);
            }
        }
        return unExpireList;
    }

    @Override
    public MemberCard findByIdContainDeleted(Long shopId, Long cardId) {
        Assert.notNull(shopId);
        Assert.notNull(cardId);

        return memberCardDao.findByIdContainDeleted(shopId, cardId);
    }

    @Override
    public int getCountByAccountId(Long shopId, Long accountId) {
        Map param = Maps.newHashMap();
        param.put("shopId",shopId);
        param.put("accountId",accountId);
        return memberCardDao.selectCount(param);
    }
}
