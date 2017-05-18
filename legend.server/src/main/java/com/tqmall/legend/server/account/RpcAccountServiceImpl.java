package com.tqmall.legend.server.account;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.biz.account.*;
import com.tqmall.legend.biz.account.bo.ComboPageParam;
import com.tqmall.legend.biz.account.bo.RechargeComboBo;
import com.tqmall.legend.biz.customer.CustomerCarRelService;
import com.tqmall.legend.biz.customer.CustomerService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.entity.account.*;
import com.tqmall.legend.entity.account.vo.MemberCardChargeVo;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.entity.customer.CustomerCarRel;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.enums.coupon.CouponInfoUseRangeEnum;
import com.tqmall.legend.facade.account.AccountFacadeService;
import com.tqmall.legend.facade.account.MemberFacadeService;
import com.tqmall.legend.facade.account.vo.CustomerDiscountInfoForApp;
import com.tqmall.legend.facade.account.vo.MemberCardBo;
import com.tqmall.legend.facade.account.vo.MemberGrantVo;
import com.tqmall.legend.facade.discount.bo.*;
import com.tqmall.legend.facade.wechat.vo.WechatAccountCouponVo;
import com.tqmall.legend.object.param.account.*;
import com.tqmall.legend.object.result.account.*;
import com.tqmall.legend.server.account.converter.*;
import com.tqmall.legend.service.account.RpcAccountService;
import com.tqmall.wheel.helper.BeanMapper;
import com.tqmall.wheel.lang.Langs;
import com.tqmall.wheel.lang.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by wanghui on 6/16/16.
 */
@Service("rpcAccountService")
@Slf4j
public class RpcAccountServiceImpl implements RpcAccountService {

    @Autowired
    private AccountFacadeService accountFacadeService;
    @Autowired
    private MemberFacadeService memberFacadeService;
    @Autowired
    private MemberCardService memberCardService;
    @Autowired
    private MemberCardInfoService memberCardInfoService;
    @Autowired
    private AccountInfoService accountInfoService;
    @Autowired
    private AccountComboService accountComboService;
    @Autowired
    private AccountCouponService accountCouponService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CouponInfoService couponInfoService;
    @Autowired
    private AccountFlowService accountFlowService;
    @Autowired
    private AccountCardFlowDetailService cardFlowDetailService;
    @Autowired
    private CustomerCarRelService customerCarRelService;


    @Override
    public Result<List<CardMemberDTO>> getCardMemberByCustomerCarId(final Long shopId, final Long customerCarId) {
        return new ApiTemplate<List<CardMemberDTO>>() {
            /**
             * 参数合法性检查 IllegalArgumentException
             */
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.isTrue(shopId != null && shopId > 0, "店铺id不能为空");
                Assert.isTrue(customerCarId != null && customerCarId > 0, "车辆id不能为空");
            }

            /**
             * 主逻辑入口 抛出BizException类型的异常 在execute方法中进行处理
             *
             * @return
             * @throws BizException
             */
            @Override
            protected List<CardMemberDTO> process() throws BizException {
                List<MemberCard> memberCardList = memberFacadeService.getAvailableListByCarId(shopId, customerCarId);
                return _assemblyCardMemberDTOList(memberCardList);
            }
        }.execute();
    }

    @Override
    public Result<List<CardMemberDTO>> getCardMemberByCustomerId(final Long shopId, final Long customerId) {
        return new ApiTemplate<List<CardMemberDTO>>() {
            /**
             * 参数合法性检查 IllegalArgumentException
             */
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.isTrue(shopId != null && shopId > 0, "店铺id不能为空");
                Assert.isTrue(customerId != null && customerId > 0, "客户id不能为空");
            }

            /**
             * 主逻辑入口 抛出BizException类型的异常 在execute方法中进行处理
             *
             * @return
             * @throws BizException
             */
            @Override
            protected List<CardMemberDTO> process() throws BizException {
                List<MemberCard> memberCards = memberFacadeService.getMemberCardByCustomerId(shopId, customerId);
                return _assemblyCardMemberDTOList(memberCards);
            }
        }.execute();
    }

    @Override
    public Result<List<CardMemberDTO>> getCardMemberByCarLicense(final Long shopId, final String carLicense) {
        return new ApiTemplate<List<CardMemberDTO>>() {
            /**
             * 参数合法性检查 IllegalArgumentException
             */
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.isTrue(shopId != null && shopId > 0, "店铺id不能为空");
                Assert.hasText(carLicense, "车牌号不能为空.");
            }

            /**
             * 主逻辑入口 抛出BizException类型的异常 在execute方法中进行处理
             *
             * @return
             * @throws BizException
             */
            @Override
            protected List<CardMemberDTO> process() throws BizException {
                List<MemberCard> memberCardList = memberFacadeService.getMemberCardByCarLicense(shopId, carLicense);
                return _assemblyCardMemberDTOList(memberCardList);
            }
        }.execute();
    }

    /**
     * 根据客户手机号获取关联的会员信息
     *
     * @param shopId
     * @param mobile
     * @return
     */
    @Override
    public Result<List<CardMemberDTO>> getCardMemberByMobile(final Long shopId, final String mobile) {
        return new ApiTemplate<List<CardMemberDTO>>() {
            /**
             * 参数合法性检查 IllegalArgumentException
             */
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.isTrue(shopId != null && shopId > 0, "店铺id不能为空");
                Assert.hasText(mobile, "客户手机号不能为空");
            }

            /**
             * 主逻辑入口 抛出BizException类型的异常 在execute方法中进行处理
             *
             * @return
             * @throws BizException
             */
            @Override
            protected List<CardMemberDTO> process() throws BizException {
                List<MemberCard> memberCardList = memberFacadeService.getMemberCardByMobile(shopId, mobile);
                return _assemblyCardMemberDTOList(memberCardList);
            }
        }.execute();
    }

    @Override
    public Result<CardMemberDTO> getCardMemberByCardNumber(final Long shopId, final String cardNumber) {
        return new ApiTemplate<CardMemberDTO>() {
            /**
             * 参数合法性检查 IllegalArgumentException
             */
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.isTrue(shopId != null && shopId > 0, "店铺id不能为空");
                Assert.hasText(cardNumber, "会员卡号不能为空");
            }

            /**
             * 主逻辑入口 抛出BizException类型的异常 在execute方法中进行处理
             *
             * @return
             * @throws BizException
             */
            @Override
            protected CardMemberDTO process() throws BizException {
                return _assemblyCardMemberDTO(memberCardService.findByCardNumber(shopId, cardNumber));
            }
        }.execute();
    }

    /**
     * 根据车辆id查询该车所有关联的账户下的卡券优惠信息
     *
     * @param shopId
     * @param customerCarId
     * @return
     */
    @Override
    public Result<List<CustomerDiscountInfoDTO>> getCustomerDiscountInfo(final Long shopId, final Long customerCarId) {
        return new ApiTemplate<List<CustomerDiscountInfoDTO>>() {
            /**
             * 参数合法性检查 IllegalArgumentException
             */
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.isTrue(shopId != null && shopId > 0, "店铺id不能为空");
                Assert.isTrue(customerCarId != null && customerCarId > 0, "车辆id不能为空");
            }

            /**
             * 主逻辑入口 抛出BizException类型的异常 在execute方法中进行处理
             *
             * @return
             * @throws BizException
             */
            @Override
            protected List<CustomerDiscountInfoDTO> process() throws BizException {
                List<CustomerDiscountInfoForApp> customerDiscountInfoList = accountFacadeService.getCustomerDiscountInfoForApp(shopId, customerCarId);
                List<CustomerDiscountInfoDTO> customerDiscountInfoDTOList = Lists.newArrayList();
                if (Langs.isNotEmpty(customerDiscountInfoList)) {
                    customerDiscountInfoDTOList = BeanMapper.mapListIfPossible(customerDiscountInfoList, CustomerDiscountInfoDTO.class);
                }
                return customerDiscountInfoDTOList;
            }
        }.execute();
    }

    @Override
    public Result<List<AccountComboDTO>> getAccountComboByCustomerCarId(Long shopId, Long customerCarId) {
        return getAccountComboByCustomerCarId(shopId, customerCarId, true);
    }

    @Override
    public Result<List<AccountComboDTO>> getAccountComboByCustomerCarId(final Long shopId, final Long customerCarId, final boolean avaiable) {
        return new ApiTemplate<List<AccountComboDTO>>() {
            /**
             * 参数合法性检查 IllegalArgumentException
             */
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.isTrue(shopId != null && shopId > 0, "店铺id不能为空");
                Assert.isTrue(customerCarId != null && customerCarId > 0, "车辆id不能为空");
            }

            /**
             * 主逻辑入口 抛出BizException类型的异常 在execute方法中进行处理
             *
             * @return
             * @throws BizException
             */
            @Override
            protected List<AccountComboDTO> process() throws BizException {
                /**
                 * 获取绑定和拥有关系的accountIdList
                 */
                List<Long> accountIds = getBoundAccountIdList(shopId, customerCarId);

                List<AccountCombo> accountComboList = null;
                if (avaiable) {
                    accountComboList = accountComboService.listAvailableComboByAccountIds(shopId, accountIds);
                } else {
                    accountComboList = accountComboService.listAllComboByAccountIds(shopId, accountIds);
                }

                return _convertToAccountComboDTO(accountComboList);
            }
        }.execute();
    }

    /**
     * 获取绑定和拥有关系的accountIdList
     */
    private List<Long> getBoundAccountIdList(Long shopId, Long customerCarId) {
        /**
         * 获取绑定关系的账户信息
         */
        AccountInfo accountInfo = this.accountInfoService.getAccountInfoByCarIdAndShopId(shopId, customerCarId);
        if (accountInfo == null) {
            log.warn("车辆[{}],店铺id[{}]不存在对应的账户信息.", customerCarId, shopId);
            return ListUtils.EMPTY_LIST;
        }
        List<Long> accountIds = Lists.newArrayList(accountInfo.getId());
        List<CustomerCarRel> customerCarRels = customerCarRelService.listByCarId(shopId, customerCarId);
        if (!CollectionUtils.isEmpty(customerCarRels)) {
            for (CustomerCarRel rel : customerCarRels) {
                accountIds.add(rel.getAccountId());
            }
        }
        return accountIds;
    }

    /**
     * 内部bean转换
     *
     * @param accountComboList
     * @return
     */
    private List<AccountComboDTO> _convertToAccountComboDTO(List<AccountCombo> accountComboList) {
        List<AccountComboDTO> accountComboDTOList = Lists.newArrayList();
        for (AccountCombo accountCombo : accountComboList) {
            AccountComboDTO accountComboDTO = new AccountComboDTO();
            accountComboDTO.setAccountComboId(accountCombo.getId());
            accountComboDTO.setComboName(accountCombo.getComboName());
            accountComboDTO.setCreateDate(accountCombo.getGmtCreate());
            accountComboDTO.setExpireDate(accountCombo.getExpireDate());
            accountComboDTO.setComboServiceList(this._convertToAccountComboServiceDTO(accountCombo.getServiceList()));
            accountComboDTOList.add(accountComboDTO);
        }
        return accountComboDTOList;
    }

    /**
     * 内部bean转换
     *
     * @param accountComboServiceRelList
     * @return
     */
    private List<AccountComboServiceDTO> _convertToAccountComboServiceDTO(List<AccountComboServiceRel> accountComboServiceRelList) {
        List<AccountComboServiceDTO> accountComboServiceDTOList = Lists.newArrayList();

        for (AccountComboServiceRel serviceRel : accountComboServiceRelList) {
            if (!serviceRel.getLeftServiceCount().equals(Integer.valueOf(0))) {
                AccountComboServiceDTO accountComboServiceDTO = new AccountComboServiceDTO();
                accountComboServiceDTO.setId(serviceRel.getId());
                accountComboServiceDTO.setComboServiceName(serviceRel.getServiceName());
                accountComboServiceDTO.setTotalServiceCount(serviceRel.getTotalServiceCount());
                accountComboServiceDTO.setUsedServiceCount(serviceRel.getUsedServiceCount());

                accountComboServiceDTOList.add(accountComboServiceDTO);
            }
        }
        return accountComboServiceDTOList;
    }


    @Override
    public Result<List<AccountCouponDTO>> getAccountCouponByCustomerCarId(Long shopId, Long customerCarId) {
        return getAccountCouponByCustomerCarId(shopId, customerCarId, true);
    }


    @Override
    public Result<List<AccountCouponDTO>> getAccountCouponByCustomerCarId(final Long shopId, final Long customerCarId, final boolean avaiable) {
        return new ApiTemplate<List<AccountCouponDTO>>() {
            /**
             * 参数合法性检查 IllegalArgumentException
             */
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.isTrue(shopId != null && shopId > 0, "店铺id不能为空");
                Assert.isTrue(customerCarId != null && customerCarId > 0, "车辆id不能为空");
            }

            /**
             * 主逻辑入口 抛出BizException类型的异常 在execute方法中进行处理
             *
             * @return
             * @throws BizException
             */
            @Override
            protected List<AccountCouponDTO> process() throws BizException {
                /**
                 * 获取绑定和拥有关系的accountIdList
                 */
                List<Long> accountIds = getBoundAccountIdList(shopId, customerCarId);

                List<AccountCoupon> accountCouponList = null;
                if (avaiable) {
                    accountCouponList = accountCouponService.findAvailableAccountCouponByAccountIds(shopId, accountIds);
                } else {
                    accountCouponList = accountCouponService.findAllAccountCouponByAccountIds(shopId, accountIds);
                }

                return _convertToAccountCouponDTO(accountCouponList);
            }
        }.execute();
    }

    private List<AccountCouponDTO> _convertToAccountCouponDTO(List<AccountCoupon> accountCouponList) {
        List<AccountCouponDTO> accountCouponDTOList = Lists.newArrayList();

        for (AccountCoupon accountCoupon : accountCouponList) {
            CouponInfo couponInfo = accountCoupon.getCouponInfo();
            AccountCouponDTO accountCouponDTO = new AccountCouponDTO();
            accountCouponDTO.setAccountCouponId(accountCoupon.getId());
            accountCouponDTO.setAccountCouponSn(accountCoupon.getCouponCode());
            accountCouponDTO.setCouponInfoId(accountCoupon.getCouponInfoId());
            accountCouponDTO.setCouponName(accountCoupon.getCouponName());
            accountCouponDTO.setCouponType(accountCoupon.getCouponType());
            accountCouponDTO.setEffectiveDate(accountCoupon.getEffectiveDate());
            accountCouponDTO.setExpireDate(accountCoupon.getExpireDate());
            accountCouponDTO.setUseRange(couponInfo.getUseRange());
            accountCouponDTO.setDiscountAmount(couponInfo.getDiscountAmount());
            accountCouponDTO.setDiscount(couponInfo.getDiscount());
            accountCouponDTO.setLimitDescription(couponInfo.getRuleStr());
            accountCouponDTO.setCompatibleWithCard(couponInfo.getCompatibleWithCard());
            accountCouponDTO.setCompatibleWithOtherAccount(couponInfo.getCompatibleWithOtherAccount());
            accountCouponDTO.setCompatibleWithOtherCoupon(couponInfo.getCompatibleWithOtherCoupon());
            accountCouponDTO.setSingleUse(couponInfo.getSingleUse());
            accountCouponDTO.setFlowId(accountCoupon.getFlowId());
            accountCouponDTOList.add(accountCouponDTO);

        }
        return accountCouponDTOList;
    }

    @Override
    public Result<DiscountInfoDTO> getDiscountInfoByOrderId(Long shopId, Long orderId, DiscountSelectedParam param) {
        try {
            DiscountInfoBo discountInfo = this.accountFacadeService.getDiscountInfoByOrderId(shopId, orderId, this._convertToDiscountSelectedItem(param));
            return Result.wrapSuccessfulResult(_convertToDiscountInfoDTO(discountInfo));
        } catch (BizException e) {
            return Result.wrapErrorResult("", e.getMessage());
        }
    }

    @Override
    public Result<DiscountInfoDTO> getCarWashDiscountInfo(Long shopId, String carLicense, Long serviceId, BigDecimal amount, DiscountSelectedParam param) {
        try {
            DiscountInfoBo discountInfo = this.accountFacadeService.getCarWashDiscountInfo(shopId, carLicense, serviceId, amount, this._convertToDiscountSelectedItem(param));
            return Result.wrapSuccessfulResult(_convertToDiscountInfoDTO(discountInfo));
        } catch (BizException e) {
            return Result.wrapErrorResult("", e.getMessage());
        }
    }

    /**
     * 查询工单上次结算使用的会员卡
     *
     * @param shopId
     * @param orderId
     * @return
     */
    @Override
    public Result<CardMemberDTO> getUsedForOrderLastSettle(Long shopId, Long orderId) {
        Assert.isTrue(shopId != null && shopId > 0);
        Assert.isTrue(orderId != null && orderId > 0);
        MemberCardBo memberCardBo = memberFacadeService.getUsedForOrderLastSettle(shopId, orderId);
        CardMemberDTO cardMemberDTO = _assemblyCardMemberDTO(memberCardBo);
        if (memberCardBo != null && cardMemberDTO != null) {
            cardMemberDTO.setBelongOther(memberCardBo.isBelongOther());
            cardMemberDTO.setMobile(memberCardBo.getMobile());
            cardMemberDTO.setCustomerName(memberCardBo.getCustomerName());
            return Result.wrapSuccessfulResult(cardMemberDTO);
        }
        return Result.wrapSuccessfulResult(null);
    }

    private DiscountSelectedBo _convertToDiscountSelectedItem(DiscountSelectedParam param) {
        if (param == null) {
            return null;
        } else {
            DiscountSelectedBo discountSelectedItem = new DiscountSelectedBo();
            // 会员卡使用
            discountSelectedItem.setGuestMobile(param.getGuestMobile());
            DiscountSelectedCardParam selectedCard = param.getSelectedCard();
            if (selectedCard != null) {
                SelectedCardBo selectedCardBo = new SelectedCardBo();
                selectedCardBo.setCardId(selectedCard.getCardId());
                selectedCardBo.setCardDiscountAmount(selectedCard.getCardDiscountAmount());
                discountSelectedItem.setSelectedCard(selectedCardBo);
            }

            //计次卡使用
            List<SelectedComboBo> selectedComboList = Lists.newArrayList();
            if (CollectionUtils.isNotEmpty(param.getSelectedComboList())) {
                for (DiscountSelectedComboParam selectedComboParam : param.getSelectedComboList()) {
                    SelectedComboBo selectedCombo = new SelectedComboBo();
                    selectedCombo.setComboServiceId(selectedComboParam.getComboServiceId());
                    selectedCombo.setCount(selectedComboParam.getUseCount());
                    selectedComboList.add(selectedCombo);
                }
            }
            discountSelectedItem.setSelectedComboList(selectedComboList);

            //优惠券使用
            List<SelectedCouponBo> discountCouponVoList = Lists.newArrayList();
            if (CollectionUtils.isNotEmpty(param.getSelectedCouponList())) {
                for (DiscountSelectedCouponParam selectedCouponParam : param.getSelectedCouponList()) {
                    SelectedCouponBo selectedCoupon = new SelectedCouponBo();
                    selectedCoupon.setCouponId(selectedCouponParam.getAccountCouponId());
                    selectedCoupon.setCouponDiscountAmount(selectedCouponParam.getDiscountAmount());
                    discountCouponVoList.add(selectedCoupon);
                }
            }
            discountSelectedItem.setSelectedCouponList(discountCouponVoList);

            //赠送券使用(本期不开放)
            return discountSelectedItem;
        }
    }

    private DiscountInfoDTO _convertToDiscountInfoDTO(DiscountInfoBo discountInfoVo) {
        DiscountInfoDTO discountInfoDTO = new DiscountInfoDTO();
        discountInfoDTO.setTotalDiscountAmount(discountInfoVo.getDiscountedAmount());

        /**
         * 会员卡优惠
         */
        discountInfoDTO.setDiscountCardList(CardDiscountConverter.convertList(discountInfoVo.getSortedCardList()));
        discountInfoDTO.setGuestDiscountCardList(CardDiscountConverter.convertList(discountInfoVo.getSortedGuestCardList()));

        /**
         * 计次卡优惠
         */
        discountInfoDTO.setDiscountComboList(ComboDiscountConverter.convertList(discountInfoVo.getSortedComboList()));
        discountInfoDTO.setGuestDiscountComboList(ComboDiscountConverter.convertList(discountInfoVo.getSortedGuestComboList()));

        /**
         * 优惠券优惠
         */
        discountInfoDTO.setDiscountCouponList(CouponDiscountConverter.convertList(discountInfoVo.getSortedCouponList()));
        discountInfoDTO.setGuestDiscountCouponList(CouponDiscountConverter.convertList(discountInfoVo.getSortedGuestCouponList()));

        return discountInfoDTO;
    }

    private List<CardServiceRelDTO> _convertToCardServiceRel(MemberCardInfo discountCardInfo) {
        this.memberCardInfoService.attachDiscount(discountCardInfo);
        if (discountCardInfo.getCardServiceRels() == null || discountCardInfo.getCardServiceRels().size() == 0) {
            return Lists.newArrayList();
        } else {
            List<CardServiceRelDTO> cardServiceRelDTOs = Lists.newArrayListWithCapacity(discountCardInfo.getCardServiceRels().size());
            for (CardServiceRel cardServiceRel : discountCardInfo.getCardServiceRels()) {
                CardServiceRelDTO cardServiceRelDTO = new CardServiceRelDTO();
                cardServiceRelDTO.setServiceCatId(cardServiceRel.getServiceCatId());
                cardServiceRelDTO.setServiceCatName(cardServiceRel.getServiceCatName());
                cardServiceRelDTO.setDiscount(cardServiceRel.getDiscount());
                cardServiceRelDTOs.add(cardServiceRelDTO);
            }
            return cardServiceRelDTOs;
        }
    }

    @Override
    public Result<Boolean> revertSettlement(Long shopId, Long operator, Long orderId, BigDecimal payAmount) {
        this.accountFacadeService.revertSettlement(shopId, operator, orderId);
        return Result.wrapSuccessfulResult(true);
    }


    /**
     * 会员卡信息组装
     *
     * @param memberCard
     * @return
     */
    private CardMemberDTO _assemblyCardMemberDTO(MemberCard memberCard) {
        if (memberCard != null) {
            MemberCardInfo memberCardInfo = memberCard.getMemberCardInfo();
            if (memberCardInfo == null) {
                memberCardInfo = this.memberCardInfoService.findById(memberCard.getShopId(), memberCard.getCardTypeId());
            }
            Preconditions.checkNotNull(memberCardInfo, "根据会员卡查询到会员卡类型为空.");

            CardMemberDTO cardMemberDTO = new CardMemberDTO();
            cardMemberDTO.setMemberCardId(memberCard.getId());
            cardMemberDTO.setCardNumber(memberCard.getCardNumber());
            cardMemberDTO.setBalance(memberCard.getBalance());
            cardMemberDTO.setCardTypeName(memberCardInfo.getTypeName());
            cardMemberDTO.setExpenseAmount(memberCard.getExpenseAmount());
            cardMemberDTO.setDepositAmount(memberCard.getDepositAmount());
            cardMemberDTO.setCardPoints(memberCard.getCardPoints());
            cardMemberDTO.setDiscountType(memberCardInfo.getDiscountType());
            cardMemberDTO.setExpireDate(memberCard.getExpireDate());
            cardMemberDTO.setCompatibleWithCoupon(memberCardInfo.getCompatibleWithCoupon());
            cardMemberDTO.setGeneralUse(memberCardInfo.getGeneralUse());
            cardMemberDTO.setLimitDescription(memberCardInfo.getLimitDescription());
            cardMemberDTO.setCreateDate(memberCard.getGmtCreate());

            switch (memberCardInfo.getDiscountType()) {
                case 0:
                    break;//无折扣
                case 1:
                    cardMemberDTO.setDiscount(memberCardInfo.getOrderDiscount());
                    break;//全部工单折扣
                case 2: //部分服务折扣
                    _assemblyCardServicesDiscountInfo(memberCardInfo, cardMemberDTO);
                    break;
                case 3: //部分配件折扣
                    _assemblyCardGoodsDiscount(memberCardInfo, cardMemberDTO);
                    break;
                case 4:
                    //服务折扣设置
                    _assemblyCardServicesDiscountInfo(memberCardInfo, cardMemberDTO);
                    //配件折扣设置
                    _assemblyCardGoodsDiscount(memberCardInfo, cardMemberDTO);
                    break;
                default:
                    return cardMemberDTO;
            }
            cardMemberDTO.setDiscountDescription(memberCardInfo.getDiscountDescription());
            return cardMemberDTO;
        } else {
            return null;
        }
    }

    private List<CardMemberDTO> _assemblyCardMemberDTOList(List<MemberCard> memberCardList) {
        List<CardMemberDTO> cardMemberDTOList = Lists.newArrayList();
        if (Langs.isEmpty(memberCardList)) {
            return cardMemberDTOList;
        }
        for (MemberCard memberCard : memberCardList) {
            cardMemberDTOList.add(_assemblyCardMemberDTO(memberCard));
        }
        return cardMemberDTOList;
    }

    private void _assemblyCardGoodsDiscount(MemberCardInfo memberCardInfo, CardMemberDTO cardMemberDTO) {
        cardMemberDTO.setGoodsDiscountType(memberCardInfo.getGoodDiscountType());
        if (cardMemberDTO.getGoodsDiscountType() == 1) {//全部物料折扣
            cardMemberDTO.setGoodsDiscount(memberCardInfo.getGoodDiscount());
        } else if (cardMemberDTO.getGoodsDiscountType() == 2) { //部分物料折扣
            List<CardGoodsRelDTO> cardGoodsRelDTOs = _convertToCardGoodsRelDTO(memberCardInfo);
            cardMemberDTO.setCardGoodsRels(cardGoodsRelDTOs);
        }
    }

    private void _assemblyCardServicesDiscountInfo(MemberCardInfo memberCardInfo, CardMemberDTO cardMemberDTO) {
        cardMemberDTO.setServiceDiscountType(memberCardInfo.getServiceDiscountType());
        if (cardMemberDTO.getServiceDiscountType() == 1) { //全部服务折扣
            cardMemberDTO.setServiceDiscount(memberCardInfo.getServiceDiscount());
        } else if (cardMemberDTO.getServiceDiscountType() == 2) {//部分服务折扣
            cardMemberDTO.setCardServiceRels(_convertToCardServiceRel(memberCardInfo));
        }
    }

    private List<CardGoodsRelDTO> _convertToCardGoodsRelDTO(MemberCardInfo memberCardInfo) {
        this.memberCardInfoService.attachDiscount(memberCardInfo);
        if (memberCardInfo.getCardGoodsRels() == null || memberCardInfo.getCardGoodsRels().size() == 0) {
            return Lists.newArrayList();
        } else {
            List<CardGoodsRelDTO> cardGoodsRelDTOs = Lists.newArrayListWithCapacity(memberCardInfo.getCardGoodsRels().size());
            for (CardGoodsRel cardGoodsRel : memberCardInfo.getCardGoodsRels()) {
                CardGoodsRelDTO cardGoodsRelDTO = new CardGoodsRelDTO();
                cardGoodsRelDTO.setGoodsCatSource(cardGoodsRel.getGoodsCatSource());
                cardGoodsRelDTO.setGoodsCatId(cardGoodsRel.getGoodsCatId());
                cardGoodsRelDTO.setGoodsCatName(cardGoodsRel.getGoodsCatName());
                cardGoodsRelDTO.setDiscount(cardGoodsRel.getDiscount());
                cardGoodsRelDTOs.add(cardGoodsRelDTO);
            }
            return cardGoodsRelDTOs;
        }
    }

    @Override
    public Result<AccountCouponInfoPageDTO> qryAccountCouponList(QryAccountCouponParam qryAccountCouponParam) {
        log.info("[查询账户优惠券列表]入参{}", LogUtils.objectToString(qryAccountCouponParam));
        if (qryAccountCouponParam == null || qryAccountCouponParam.getUserGlobalId() == null
                || qryAccountCouponParam.getMobile() == null) {
            log.error("[查询账户优惠券列表]失败,参数错误,入参{}", LogUtils.objectToString(qryAccountCouponParam));
            return Result.wrapErrorResult("-1", "参数错误");
        }
        Long userGlobalId = qryAccountCouponParam.getUserGlobalId();
        String mobile = qryAccountCouponParam.getMobile();
        Long offset = qryAccountCouponParam.getOffset() == null ? 0l : qryAccountCouponParam.getOffset();
        Long limit = qryAccountCouponParam.getLimit() == null ? 10l : qryAccountCouponParam.getLimit();
        Shop shop = shopService.getShopByUserGlobalId(userGlobalId);
        if (shop == null) {
            log.error("[查询账户优惠券列表]失败,查询不到门店信息,入参{}", LogUtils.objectToString(qryAccountCouponParam));
            return Result.wrapErrorResult("-1", "查询不到门店信息");
        }
        Long shopId = shop.getId();
        AccountCouponInfoPageDTO accountCouponInfoPageDTO = new AccountCouponInfoPageDTO();
        Long accountId = null;
        List<Customer> customerList = customerService.getCustomerByMobile(mobile, shopId);
        if (CollectionUtils.isEmpty(customerList)) {
            return Result.wrapSuccessfulResult(accountCouponInfoPageDTO);
        }
        Customer customer = customerList.get(0);
        AccountInfo accountInfo = accountInfoService.getAccountInfoByCustomerIdAndShopId(shopId, customer.getId());
        if (accountInfo == null) {
            return Result.wrapSuccessfulResult(accountCouponInfoPageDTO);
        }
        accountId = accountInfo.getId();
        if (accountId == null) {
            return Result.wrapSuccessfulResult(accountCouponInfoPageDTO);
        }

        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("accountId", accountId);
        searchParams.put("shopId", shopId);
        searchParams.put("usedStatus", qryAccountCouponParam.getUsedStatus());
        if (qryAccountCouponParam.getExpireStatus() != null) {
            if (1 == qryAccountCouponParam.getExpireStatus()) {
                //1.未过期
                searchParams.put("expireDateGt", new Date());
            } else if (2 == qryAccountCouponParam.getExpireStatus()) {
                //2.已过期
                searchParams.put("expireDateLt", new Date());
            }
        }
        int totalCount = accountCouponService.selectCount(searchParams);
        if (totalCount < 1) {
            return Result.wrapSuccessfulResult(accountCouponInfoPageDTO);
        }
        searchParams.put("offset", offset);
        searchParams.put("limit", limit);
        searchParams.put("sorts", new String[]{" used_status asc", " expire_date asc", " gmt_modified desc"});
        List<WechatAccountCouponVo> wechatAccountCouponList = accountCouponService.qryAccountCoupon(searchParams);
        if (CollectionUtils.isEmpty(wechatAccountCouponList)) {
            return Result.wrapSuccessfulResult(accountCouponInfoPageDTO);
        }
        accountCouponInfoPageDTO.setCount(Long.valueOf(totalCount));
        List<AccountCouponInfoDTO> content = new ArrayList<>();
        for (WechatAccountCouponVo wechatAccountCouponVo : wechatAccountCouponList) {
            AccountCouponInfoDTO accountCouponInfoDTO = new AccountCouponInfoDTO();
            BeanUtils.copyProperties(wechatAccountCouponVo, accountCouponInfoDTO);
            CouponInfo couponInfo = wechatAccountCouponVo.getCouponInfo();
            if (couponInfo != null) {
                CouponInfoDTO couponInfoDTO = new CouponInfoDTO();
                BeanUtils.copyProperties(couponInfo, couponInfoDTO);
                StringBuffer description = new StringBuffer();
                if (couponInfo.getUseRange() != null) {
                    String useRangeName = CouponInfoUseRangeEnum.getNameByValue(couponInfo.getUseRange());
                    if (StringUtils.isNotBlank(useRangeName)) {
                        description.append(useRangeName + ";");
                    }
                }
                if (couponInfo.getAmountLimit() != null && couponInfo.getAmountLimit().compareTo(new BigDecimal("0")) > 0) {
                    description.append("满" + couponInfo.getAmountLimit() + "元使用;");
                }
                if (couponInfo.getSingleUse() != null && couponInfo.getSingleUse() == 1) {
                    description.append("不可同时使用多张优惠券;");
                } else {
                    description.append("可同时使用多张优惠券;");
                }
                if (couponInfo.getCompatibleWithCard() != null && couponInfo.getCompatibleWithCard() == 0) {
                    description.append("不允许与会员卡共同使用;");
                } else {
                    description.append("允许与会员卡共同使用;");
                }
                couponInfoDTO.setDescription(description.toString());
                accountCouponInfoDTO.setCouponInfoDTO(couponInfoDTO);
            }
            content.add(accountCouponInfoDTO);
        }
        accountCouponInfoPageDTO.setContent(content);
        return Result.wrapSuccessfulResult(accountCouponInfoPageDTO);
    }

    @Override
    public Result<AccountComboPageDTO> qryAccountComboList(final QryAccountComboParam qryAccountComboParam) {
        return new ApiTemplate<AccountComboPageDTO>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(qryAccountComboParam, "入参不能为null");
                Assert.notNull(qryAccountComboParam.getUserGlobalId(), "userGlobalId不能为null");
                Assert.notNull(qryAccountComboParam.getMobile(), "mobile不能为null");
            }

            @Override
            protected AccountComboPageDTO process() throws BizException {
                Long userGlobalId = qryAccountComboParam.getUserGlobalId();
                Long shopId = transformUcshopID2ShopID(userGlobalId);
                String mobile = qryAccountComboParam.getMobile();
                List<Long> accoutIds = transformMobile2AccountId(mobile, shopId);

                AccountComboPageDTO accountComboPageDTO = new AccountComboPageDTO();

                ComboPageParam param = new ComboPageParam();
                param.setAccountIds(accoutIds);
                param.setShopId(shopId);
                param.setComboStatus(qryAccountComboParam.getComboStatus());
                param.setExpireStautus(qryAccountComboParam.getExpireStautus());

                //.查询计次卡
                List<AccountCombo> allAccountComboList = accountComboService.listForWechat(param);
                if (org.springframework.util.CollectionUtils.isEmpty(allAccountComboList)) {
                    return accountComboPageDTO;
                }

                int fromIndex = qryAccountComboParam.getOffset().intValue();
                int toIndex = fromIndex + qryAccountComboParam.getLimit().intValue() + 1;
                int size = allAccountComboList.size();
                toIndex = toIndex > size ? size : toIndex;
                List<AccountCombo> accountComboList = allAccountComboList.subList(fromIndex, toIndex);
                List<AccountComboDTO> content = new ArrayList<>();
                for (AccountCombo accountCombo : accountComboList) {
                    AccountComboDTO accountComboDTO = new ComboDtoConvert(accountCombo).invoke();
                    content.add(accountComboDTO);
                }

                //增加适用车牌号
                ImmutableMap<Long, AccountCombo> comboMap = Maps.uniqueIndex(accountComboList, new Function<AccountCombo, Long>() {
                    @Override
                    public Long apply(AccountCombo input) {
                        return input.getId();
                    }
                });
                for (AccountComboDTO comboDTO : content) {
                    AccountCombo combo = comboMap.get(comboDTO.getAccountComboId());
                    List<String> licenses = accountInfoService.listLicenseByAccountId(shopId, combo.getAccountId());
                    comboDTO.setLicenseList(licenses);
                }
                accountComboPageDTO.setContent(content);
                int count = allAccountComboList.size();
                accountComboPageDTO.setCount(count);
                return accountComboPageDTO;
            }
        }.execute();


    }

    @Override
    public Result<Boolean> createCustomerForWexinByUserGlobalId(Long userGlobalId, String mobile) {
        if (null == userGlobalId) {
            return Result.wrapErrorResult("", "门店号不能为空.");
        }
        if (StringUtils.isEmpty(mobile)) {
            return Result.wrapErrorResult("", "手机号码不能为空.");
        }
        Shop shop = shopService.getShopByUserGlobalId(userGlobalId);
        if (shop == null) {
            log.error("[创建来自微信的云修客户]失败,查询不到门店信息,userGlobalId:{}", LogUtils.objectToString(userGlobalId));
            return Result.wrapErrorResult("-1", "查询不到门店信息");
        }
        Long shopId = shop.getId();
        return Result.wrapSuccessfulResult(accountInfoService.createWeChatAccount(shopId, mobile));

    }

    @Override
    public Result<List<CouponInfo4WechatDTO>> findCouponInfoById4Wehcat(Long ucShopId, Long... couponInfoIds) {
        if (ucShopId == null) {
            return Result.wrapErrorResult("", "ucShopId不能为空");
        }
        Shop shop = shopService.getShopByUserGlobalId(ucShopId);
        if (shop == null) {
            return Result.wrapErrorResult("", "查不到ucShopId为" + ucShopId + "的门店");
        }
        Long shopId = shop.getId();
        if (shopId == null) {
            return Result.wrapErrorResult("", "店铺id不能为空");
        }
        if (couponInfoIds == null || couponInfoIds.length == 0) {
            return Result.wrapErrorResult("", "请传入正确的优惠券类型id集合");
        }

        List<CouponInfo> couponInfoList = this.couponInfoService.findByIds(shopId, couponInfoIds);
        couponInfoService.attachUseRange(couponInfoList);
        List<CouponInfo4WechatDTO> couponInfo4WechatDTOs = Lists.newArrayListWithCapacity(couponInfoList.size());
        for (CouponInfo couponInfo : couponInfoList) {
            CouponInfo4WechatDTO couponInfo4WechatDTO = new CouponInfo4WechatDTO();
            couponInfo4WechatDTO.setUniversal(couponInfo.getCouponType() == 2);
            couponInfo4WechatDTO.setCouponName(couponInfo.getCouponName());
            couponInfo4WechatDTO.setId(couponInfo.getId());
            couponInfo4WechatDTO.setRemark(couponInfo.getRemark());
            couponInfo4WechatDTO.setCustomizeTime(couponInfo.getCustomizeTime());
            couponInfo4WechatDTO.setEffectivePeriodDays(couponInfo.getEffectivePeriodDays());
            couponInfo4WechatDTO.setEffectiveDate(couponInfo.getEffectiveDate());
            couponInfo4WechatDTO.setExpireDate(couponInfo.getExpireDate());
            couponInfo4WechatDTO.setCompatibleWithCard(couponInfo.getCompatibleWithCard());
            couponInfo4WechatDTO.setSingleUse(couponInfo.getSingleUse());
            couponInfo4WechatDTO.setAmountLimit(couponInfo.getAmountLimit());
            couponInfo4WechatDTO.setUseRange(couponInfo.getUseRange());
            couponInfo4WechatDTO.setUseRangeDesc(CouponInfoUseRangeEnum.getNameByValue(couponInfo.getUseRange()));
            couponInfo4WechatDTO.setUseRangeDescDetail(couponInfo.getUseRangeDescript());
            couponInfo4WechatDTO.setDiscountAmount(couponInfo.getDiscountAmount());
            couponInfo4WechatDTOs.add(couponInfo4WechatDTO);
        }
        return Result.wrapSuccessfulResult(couponInfo4WechatDTOs);
    }

    @Override
    public Result<Boolean> grantCombo4Wechat(Long ucShopId, Long comboInfoId, String mobile) {
        log.info("[dubbo][客户领取计次卡],入参:userGlobalId:{},comboInfoId:{},mobile:{}", ucShopId, comboInfoId, mobile);
        if (ucShopId == null) {
            return Result.wrapErrorResult("", "uc店铺Id不能为空");
        }
        if (comboInfoId == null) {
            return Result.wrapErrorResult("", "计次卡类型ID不能为空");
        }
        if (StringUtil.isStringEmpty(mobile)) {
            return Result.wrapErrorResult("", "手机号不能为空");
        }

        Shop shop = shopService.getShopByUserGlobalId(ucShopId);
        if (shop == null) {
            log.info("[dubbo][客户领取计次卡],找不到ucShopId对应的门店,入参:userGlobalId:{},comboInfoId:{},mobile:{}", ucShopId, comboInfoId, mobile);
            return Result.wrapErrorResult("", "找不到ucShopId对应的门店");
        }
        Long shopId = shop.getId();

        List<Customer> customerList = customerService.getCustomerByMobile(mobile, shopId);
        if (customerList.size() != 1) {
            log.info("[dubbo][客户领取计次卡],手机号对应{}个客户,入参:userGlobalId:{},comboInfoId:{},mobile:{}", customerList.size(), ucShopId, comboInfoId, mobile);
            return Result.wrapErrorResult("", "手机号对应" + customerList.size() + "个客户");
        }
        Long customerId = customerList.get(0).getId();
        AccountInfo accountInfo = accountInfoService.getAccountInfoByCustomerIdAndShopId(shopId, customerId);
        if (accountInfo == null) {
            log.info("[dubbo][客户领取计次卡],找不到手机号对应的账户信息,入参:userGlobalId:{},comboInfoId:{},mobile:{}", ucShopId, comboInfoId, mobile);
            return Result.wrapErrorResult("", "找不到手机号对应的账户信息");
        }
        RechargeComboBo rechargeComboBo = new RechargeComboBo();
        rechargeComboBo.setAccountId(accountInfo.getId());
        rechargeComboBo.setComboInfoId(comboInfoId);

        try {
            accountComboService.rechargeCombo(rechargeComboBo, shopId, null);
        } catch (BizException e) {
            log.error("微信端办理计次卡出错,错误原因:{}", e);
            return Result.wrapErrorResult("", e.getMessage());
        }

        return Result.wrapSuccessfulResult(Boolean.TRUE);
    }

    @Override
    public Result<List<MemberCardInfoDTO>> listCardInfo4Wechat(final Long ucShopId, final List<Long> cardInfoIds) {
        return new ApiTemplate<List<MemberCardInfoDTO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(ucShopId, "门店ucShopid不能为空");
                if (CollectionUtils.isEmpty(cardInfoIds)) {
                    throw new IllegalArgumentException("会员卡卡类型id数组不能为空");
                }
            }

            @Override
            protected List<MemberCardInfoDTO> process() throws BizException {
                Long shopID = transformUcshopID2ShopID(ucShopId);
                List<MemberCardInfo> cardInfos = memberCardInfoService.selectInfoByIds(shopID, cardInfoIds);
                memberCardInfoService.attachDiscount(cardInfos);
                List<MemberCardInfoDTO> data = new CardInfoListConverter().convert(cardInfos);
                return data;
            }
        }.execute();

    }

    @Override
    public Result<List<MemberCardDTO>> listCard4Wechat(final Long ucShopId, final String mobile) {
        return new ApiTemplate<List<MemberCardDTO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(ucShopId, "门店ucShopid不能为空");
                if (!StringUtil.isMobileNO(mobile)) {
                    throw new IllegalArgumentException("手机号格式错误");
                }
            }

            @Override
            protected List<MemberCardDTO> process() throws BizException {
                Long shopID = transformUcshopID2ShopID(ucShopId);
                List<Long> accountIdList = transformMobile2AccountId(mobile, shopID);
                List<MemberCard> cardList = memberCardService.listByAccountIds(shopID, accountIdList);

                List<MemberCardDTO> data = Lists.newArrayList();
                for (MemberCard card : cardList) {
                    if (card != null) {
                        MemberCardDTO cardDTO = new CardConverter().convert(card);
                        MemberCardUsedSummay usedInfo = cardFlowDetailService.sumUsedInfo(shopID, card.getId());
                        cardDTO.setUsedAmount(usedInfo.getUsedAmount());
                        cardDTO.setUsedCount(usedInfo.getUsedCount());
                        MemberCardInfo cardInfo = memberCardInfoService.findById(shopID, card.getCardTypeId());
                        memberCardInfoService.attachDiscount(cardInfo);
                        MemberCardInfoDTO infoDTO = new CardInfoConverter().convert(cardInfo);
                        attachInfo2DTO(infoDTO, cardDTO);

                        data.add(cardDTO);
                    }
                }
                for (MemberCardDTO cardDTO : data) {
                    List<String> licenseList = accountInfoService.listLicenseByAccountId(shopID, cardDTO.getAccountId());
                    cardDTO.setLicenseList(licenseList);
                }

                return data;
            }
        }.execute();

    }

    private boolean isMultiAccount(List<Long> accountIdList) {
        return accountIdList.size() > 1;
    }

    private void attachInfo2DTO(MemberCardInfoDTO infoDTO, MemberCardDTO cardDTO) {
        cardDTO.setDiscountDescriptionDetail(infoDTO.getDiscountTypeDescriptionDetail());
        cardDTO.setDiscountType(infoDTO.getDiscountType());
        cardDTO.setGoodDiscountType(infoDTO.getGoodDiscountType());
        cardDTO.setServiceDiscountType(infoDTO.getServiceDiscountType());
        cardDTO.setGoodDiscount(infoDTO.getGoodDiscount());
        cardDTO.setServiceDiscount(infoDTO.getServiceDiscount());
        cardDTO.setOrderDiscount(infoDTO.getOrderDiscount());
        cardDTO.setDiscountDescription(infoDTO.getDiscountTypeDescription());
    }


    @Override
    public Result<MemberCardDTO> grantCard4Wechat(final Long ucShopId, final Long cardInfoId, final String mobile, final String cardNumber, final BigDecimal incomeAmount) {
        return new ApiTemplate<MemberCardDTO>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(ucShopId, "ucShopId不能为空");
                Assert.notNull(cardInfoId, "会员卡类型id不能为空");
                if (!StringUtil.isMobileNO(mobile)) {
                    throw new IllegalArgumentException("手机号格式错误");
                }

            }

            @Override
            protected MemberCardDTO process() throws BizException {
                Long shopID = transformUcshopID2ShopID(ucShopId);
                List<Long> accountIdList = transformMobile2AccountId(mobile, shopID);
                Long accountId = accountIdList.get(0);
                MemberGrantVo grantVo = new MemberGrantVo();
                grantVo.setAccountId(accountId);
                grantVo.setCardNumber(cardNumber);
                grantVo.setMemberCardInfoId(cardInfoId);
                //微信办卡支付金额为0
                //grantVo.setAmount(incomeAmount == null? BigDecimal.ZERO: incomeAmount);

                AccountTradeFlow accountTradeFlow = memberFacadeService.grantMemberCard(grantVo, shopID, null, null);
                if (accountTradeFlow != null) {
                    MemberCard memberCard = accountTradeFlow.getMemberCard();
                    if (memberCard != null) {
                        //进行一次充值
                        MemberCardChargeVo memberCardChargeVo = new MemberCardChargeVo();
                        memberCardChargeVo.setAmount(incomeAmount);
                        memberCardChargeVo.setPayAmount(BigDecimal.ZERO);
                        memberCardChargeVo.setPaymentId(0L);
                        memberCardChargeVo.setPaymentName("现金");
                        memberCardChargeVo.setRemark("微信会员卡办卡自动充值.");
                        memberCardChargeVo.setCardId(memberCard.getId());

                        AccountTradeFlow flow = memberCardService.recharge(shopID, null, memberCardChargeVo);
                        if (Objects.isNull(flow)) {
                            throw new BizException("会员卡充值失败.");
                        }

                        MemberCardDTO cardDTO = new CardConverter().convert(memberCard);
                        return cardDTO;
                    }
                }
                throw new BizException("微信会员卡办卡失败");
            }
        }.execute();

    }

    @Override
    public Result<List<MemberCardFlowDTO>> listMemberCardTradeFlow4Wechat(final Long ucShopId, final Long memberCardId, String mobile) {

        return new ApiTemplate<List<MemberCardFlowDTO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(ucShopId, "ucShopId不能为空");
                Assert.notNull(memberCardId, "会员卡id不能为空");
            }

            @Override
            protected List<MemberCardFlowDTO> process() throws BizException {
                Long shopID = transformUcshopID2ShopID(ucShopId);
                List<AccountTradeFlow> cardFlows = accountFlowService.listCardFlowByCardId(shopID, memberCardId);
                Iterator<AccountTradeFlow> iterator = cardFlows.iterator();
                while (iterator.hasNext()) {
                    AccountTradeFlow flow = iterator.next();
                    Integer consumeType = flow.getConsumeType();
                    if (consumeType > 4) {
                        iterator.remove();
                    }
                }
                List<MemberCardFlowDTO> data = new CardFlowListConverter().convert(cardFlows);
                return data;
            }
        }.execute();

    }

    @Override
    public Result<List<ComboFlowDTO>> listComboConsumeFlow4Wechat(final Long ucShopId, final Long comboId, String mobile) {

        return new ApiTemplate<List<ComboFlowDTO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(ucShopId, "ucShopId不能为空");
                Assert.notNull(comboId, "几次卡id不能为空");
            }

            @Override
            protected List<ComboFlowDTO> process() throws BizException {
                Long shopID = transformUcshopID2ShopID(ucShopId);

                List<AccountTradeFlow> comboFlows = accountFlowService.listComboFlowByComboId(shopID, comboId);
                Iterator<AccountTradeFlow> iterator = comboFlows.iterator();
                while (iterator.hasNext()) {
                    AccountTradeFlow flow = iterator.next();
                    Integer consumeType = flow.getConsumeType();
                    if (consumeType < 3 || consumeType > 4) {
                        iterator.remove();
                    }
                }
                List<ComboFlowDTO> data = new ComboFlowListConverter().convert(comboFlows);
                return data;
            }
        }.execute();

    }

    @Override
    public Result<MemberCardDetailDTO> getCardDetail4Wechat(final Long ucShopId, final Long cardId) {

        return new ApiTemplate<MemberCardDetailDTO>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(ucShopId, "ucShopId不能为空");
                Assert.notNull(cardId, "会员卡id不能为空");
            }

            @Override
            protected MemberCardDetailDTO process() throws BizException {
                Long shopId = transformUcshopID2ShopID(ucShopId);
                MemberCard card = memberCardService.getMemberCardById(shopId, cardId);
                if (card == null) {
                    throw new BizException("无法获取carId=" + cardId + "的会员卡");
                }
                Long cardTypeId = card.getCardTypeId();
                MemberCardInfo cardInfo = memberCardInfoService.findById(shopId, cardTypeId);
                if (cardInfo == null) {
                    throw new BizException("无法获取Id=" + cardTypeId + "的会员卡类型信息");
                }

                MemberCardDetailDTO dto = new MemberCardDetailDTO();
                MemberCardUsedSummay cardUsedSummay = cardFlowDetailService.sumUsedInfo(shopId, cardId);
                dto.setCardName(cardInfo.getTypeName());
                dto.setCardNumber(card.getCardNumber());
                dto.setDiscountType(cardInfo.getDiscountType());
                dto.setOrderDiscount(cardInfo.getOrderDiscount());
                dto.setGoodsDiscount(cardInfo.getGoodDiscount());
                dto.setServiceDiscount(cardInfo.getServiceDiscount());
                dto.setExpireDate(card.getExpireDate());
                dto.setUsedAmount(cardUsedSummay.getUsedAmount());
                dto.setUsedCount(cardUsedSummay.getUsedCount());
                dto.setDepositAmount(card.getDepositAmount());
                dto.setDepositCount(card.getDepositCount());
                dto.setBalance(card.getBalance());
                dto.setCardTypeId(card.getCardTypeId());
                return dto;
            }
        }.execute();
    }

    @Override
    public Result<ComboDetailDTO> getComboDetail4Wechat(final Long ucShopId, final Long comboId) {
        return new ApiTemplate<ComboDetailDTO>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(ucShopId, "ucShopId不能为空");
                Assert.notNull(comboId, "计次卡id不能为空");
            }

            @Override
            protected ComboDetailDTO process() throws BizException {

                Long shopId = transformUcshopID2ShopID(ucShopId);
                AccountCombo comboWithService = accountComboService.getComboWithService(shopId, comboId);
                ComboDetailDTO dto = new ComboDetailDTO();
                dto.setName(comboWithService.getComboName());
                dto.setExpireDate(comboWithService.getExpireDate());
                dto.setTypeId(comboWithService.getComboInfoId());
                List<ComboServiceDTO> serviceDTOs = Lists.newArrayList();
                for (AccountComboServiceRel comboService : comboWithService.getServiceList()) {
                    Integer totalCount = comboService.getTotalServiceCount();
                    Integer usedCount = comboService.getUsedServiceCount();
                    ComboServiceDTO serviceDTO = new ComboServiceDTO();
                    serviceDTO.setName(comboService.getServiceName());
                    serviceDTO.setTotalCount(totalCount);
                    serviceDTO.setLeftCount(totalCount - usedCount);
                    serviceDTO.setUsedCount(usedCount);
                    serviceDTOs.add(serviceDTO);
                }
                dto.setServiceList(serviceDTOs);
                return dto;
            }
        }.execute();
    }

    @Override
    public Result<List<WxCouponDTO>> getCouponListWithoutSuite(final Long ucShopId, final String mobile) {
        return new ApiTemplate<List<WxCouponDTO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(ucShopId, "ucShopId不能为空");
                if (!StringUtil.isMobileNO(mobile)) {
                    throw new IllegalArgumentException("手机号格式错误");
                }
            }

            @Override
            protected List<WxCouponDTO> process() throws BizException {
                Long shopID = transformUcshopID2ShopID(ucShopId);
                List<Long> accountIdList = transformMobile2AccountId(mobile, shopID);
                List<AccountCoupon> freeCoupons = Lists.newArrayList();
                for (Long accountId : accountIdList) {
                    List<AccountCoupon> accountfreeCoupons = accountFacadeService.listFreeCoupons(shopID, accountId);
                    freeCoupons.addAll(accountfreeCoupons);
                }
                List<WxCouponDTO> dtos = new CouponListConverter().convert(freeCoupons);
                return dtos;
            }
        }.execute();
    }

    @Override
    public Result<List<CouponSuiteDTO>> getCouponSuiteList(final Long ucShopId, final String mobile) {
        return new ApiTemplate<List<CouponSuiteDTO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(ucShopId, "ucShopId不能为空");
                if (!StringUtil.isMobileNO(mobile)) {
                    throw new IllegalArgumentException("手机号格式错误");
                }
            }

            @Override
            protected List<CouponSuiteDTO> process() throws BizException {
                Long shopID = transformUcshopID2ShopID(ucShopId);
                List<Long> accountIdList = transformMobile2AccountId(mobile, shopID);

                List<AccountSuite> allSuites = Lists.newArrayList();
                for (Long accountId : accountIdList) {
                    List<AccountSuite> accountSuites = accountFacadeService.listBundleCoupons(shopID, accountId);
                    allSuites.addAll(accountSuites);
                }
                List<CouponSuiteDTO> data = new SuiteListConverter().convert(allSuites);
                return data;
            }
        }.execute();
    }

    @Override
    public Result<WxCouponDTO> getCouponDetail(final Long ucShopId, final Long couponId) {
        return new ApiTemplate<WxCouponDTO>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(ucShopId, "店铺id不能为空");
                Assert.notNull(couponId, "优惠券id不能为空");
            }

            @Override
            protected WxCouponDTO process() throws BizException {
                Long shopID = transformUcshopID2ShopID(ucShopId);
                AccountCoupon coupon = accountFacadeService.getCouponDetail(shopID, couponId);
                WxCouponDTO data = new CouponConverter().convert(coupon);
                List<String> licenseList = accountInfoService.listLicenseByAccountId(shopID, coupon.getAccountId());
                data.setLicenseList(licenseList);
                return data;
            }
        }.execute();
    }

    @Override
    public Result<Boolean> hasOwnedCouponByCouponTypeId(final Long ucShopId, final String mobile, final Long couponTypeId) {
        return new ApiTemplate<Boolean>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(ucShopId, "店铺id不能为空.");
                Assert.notNull(couponTypeId, "优惠券类型id不能为空.");
                if (!StringUtil.isMobileNO(mobile)) {
                    throw new IllegalArgumentException("用户手机号格式不正确.");
                }
            }

            @Override
            protected Boolean process() throws BizException {
                Long shopId = transformUcshopID2ShopID(ucShopId);
                List<Long> accountIdList = transformMobile2AccountId(mobile, shopId);
                boolean result = false;
                for (Long accountId : accountIdList) {
                    boolean accountHas = accountCouponService.hasOwnedCouponByType(shopId, accountId, couponTypeId);
                    result = result || accountHas;
                }
                return result;
            }
        }.execute();

    }

    @Override
    public Result<Boolean> isMultiAccount(final Long ucShopId, final String mobile) {
        return new ApiTemplate<Boolean>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(ucShopId, "店铺id不能为空.");
                if (!StringUtil.isMobileNO(mobile)) {
                    throw new IllegalArgumentException("用户手机号格式不正确.");
                }
            }

            @Override
            protected Boolean process() throws BizException {
                Long shopId = transformUcshopID2ShopID(ucShopId);
                List<Long> accountIdList = transformMobile2AccountId(mobile, shopId);
                return isMultiAccount(accountIdList);
            }
        }.execute();
    }


    private Long transformUcshopID2ShopID(Long ucShopId) {

        Shop shop = shopService.getShopByUserGlobalId(ucShopId);
        if (shop == null) {
            throw new BizException("ucShopId转shopId失败");
        }
        return shop.getId();
    }

    private List<Long> transformMobile2AccountId(String mobile, Long shopID) {

        List<AccountInfo> accountInfoList = accountInfoService.getAccountInfoByMobile(shopID, mobile);
        List<Long> accountIdList = Lists.newArrayList();
        for (AccountInfo accountInfo : accountInfoList) {
            accountIdList.add(accountInfo.getId());
        }
        return accountIdList;
    }

    private class ComboDtoConvert {
        private AccountCombo accountCombo;

        public ComboDtoConvert(AccountCombo accountCombo) {
            this.accountCombo = accountCombo;
        }

        public AccountComboDTO invoke() {
            AccountComboDTO accountComboDTO = new AccountComboDTO();
            BeanUtils.copyProperties(accountCombo, accountComboDTO);
            accountComboDTO.setCreateDate(accountCombo.getGmtCreate());
            accountComboDTO.setAccountComboId(accountCombo.getId());

            ComboInfo comboInfo = accountCombo.getComboInfo();
            if (comboInfo != null) {
                accountComboDTO.setSalePrice(comboInfo.getSalePrice());
            }

            if (!org.springframework.util.CollectionUtils.isEmpty(accountCombo.getServiceList())) {
                List<AccountComboServiceRel> accountComboServiceRelList = accountCombo.getServiceList();
                List<AccountComboServiceDTO> accountComboServiceDTOList = new ArrayList<>();
                for (AccountComboServiceRel accountComboServiceRel : accountComboServiceRelList) {
                    AccountComboServiceDTO accountComboServiceDTO = new AccountComboServiceDTO();
                    accountComboServiceDTO.setId(accountComboServiceRel.getId());
                    accountComboServiceDTO.setComboServiceId(accountComboServiceRel.getServiceId());
                    accountComboServiceDTO.setComboServiceName(accountComboServiceRel.getServiceName());
                    accountComboServiceDTO.setTotalServiceCount(accountComboServiceRel.getTotalServiceCount());
                    accountComboServiceDTO.setUsedServiceCount(accountComboServiceRel.getUsedServiceCount());
                    accountComboServiceDTOList.add(accountComboServiceDTO);
                }
                accountComboDTO.setComboServiceList(accountComboServiceDTOList);
            }
            return accountComboDTO;
        }
    }
}

