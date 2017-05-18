package com.tqmall.legend.web.account;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.common.util.DateUtil;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.error.LegendAccountErrorCode;
import com.tqmall.legend.annotation.HttpRequestLog;
import com.tqmall.legend.biz.account.*;
import com.tqmall.legend.biz.account.bo.CardCreateBo;
import com.tqmall.legend.biz.account.bo.CardGrantReverseBo;
import com.tqmall.legend.biz.account.bo.CardUpgradeBo;
import com.tqmall.legend.biz.account.bo.RechargeSummaryVo;
import com.tqmall.legend.biz.account.vo.BackCardVo;
import com.tqmall.legend.biz.account.vo.MemberCardVo;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.legend.biz.customer.CustomerService;
import com.tqmall.legend.biz.discount.utils.DiscountUtil;
import com.tqmall.legend.biz.settlement.PaymentService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.entity.account.*;
import com.tqmall.legend.entity.account.vo.MemberCardChargeVo;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.facade.account.MemberFacadeService;
import com.tqmall.legend.facade.account.vo.MemberGrantVo;
import com.tqmall.legend.web.account.converter.CardCreateConverter;
import com.tqmall.legend.web.account.converter.CardInfoEditVoConverter;
import com.tqmall.legend.web.account.converter.CardInfoListConverter;
import com.tqmall.legend.web.account.converter.MemberCardConverter;
import com.tqmall.legend.web.account.vo.*;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.wheel.lang.Langs;
import com.tqmall.wheel.lang.Objects;
import com.tqmall.zenith.errorcode.support.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by wanghui on 6/2/16.
 */
@RequestMapping("account/member")
@Controller
@Slf4j
public class MemberController extends BaseController {

    @Autowired
    private MemberFacadeService memberFacadeService;
    @Autowired
    private MemberCardInfoService memberCardInfoService;
    @Autowired
    private MemberCardService memberCardService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private AccountInfoService accountInfoService;
    @Autowired
    private AccountTradeFlowService accountTradeFlowService;
    @Autowired
    private CustomerCarService customerCarService;
    @Autowired
    private AccountCardFlowDetailService accountCardFlowDetailService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private AccountFlowService accountFlowService;
    @Autowired
    private CustomerService customerService;

    /**
     * 跳转到会员卡充值页面
     * @param model
     * @param accountId
     * @param request
     * @return
     */
    @RequestMapping(value = "charge", method = RequestMethod.GET)
    public String charge(Model model, @RequestParam("accountId") Long accountId, HttpServletRequest request) {
        model.addAttribute("moduleUrl", "customer");
        model.addAttribute("subModule", "account-index");

        Long shopId = UserUtils.getShopIdForSession(request);
        List<MemberCard> memberCardList = memberCardService.getUnExpiredMemberCardListByAccountId(shopId, accountId);
        model.addAttribute("memberCardList", MemberCardConverter.convertVoList(memberCardList));
        return "yqx/page/account/member/charge";
    }

    @HttpRequestLog
    @RequestMapping("reverseRecharge")
    @ResponseBody
    public Result reverseRecharge(@RequestParam("id") Long flowId) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        Long userId = userInfo.getUserId();
        try {
            memberFacadeService.reverseRecharge(shopId, userId, flowId);
            return Result.wrapSuccessfulResult("撤销成功");
        } catch (BizException e) {
            return LegendAccountErrorCode.COMMON_ERROR.newResult(e.getMessage());
        }
    }

    /**
     * 会员卡充值
     */
    @HttpRequestLog
    @RequestMapping(value = "reCharge", method = RequestMethod.POST)
    @ResponseBody
    public com.tqmall.core.common.entity.Result<AccountTradeFlow> reCharge(@RequestBody final MemberCardChargeVo memberCardChargeVo) {
        return new ApiTemplate<AccountTradeFlow>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(memberCardChargeVo);
                Assert.notNull(memberCardChargeVo.getCardId(), "会员卡Id不能为空");
                BigDecimal amount = memberCardChargeVo.getAmount();
                Assert.notNull(amount, "会员卡充值金额不能为空");
                Assert.isTrue(amount.compareTo(BigDecimal.ZERO) >= 0, "会员卡充值金额不能小于零");
                BigDecimal payAmount = memberCardChargeVo.getPayAmount();
                Assert.notNull(payAmount, "会员卡收款金额不能为空");
                Assert.isTrue(payAmount.compareTo(BigDecimal.ZERO) >= 0, "会员卡收款金额不能小于零");
                Assert.notNull(memberCardChargeVo.getPaymentId(), "会员卡支付方式不能为空");
            }

            @Override
            protected AccountTradeFlow process() throws BizException {
                UserInfo userInfo = UserUtils.getUserInfo(request);
                Long shopId = UserUtils.getShopIdForSession(request);
                Long userId = userInfo.getUserId();
                AccountTradeFlow flow = memberCardService.recharge(shopId, userId, memberCardChargeVo);
                if (Objects.isNull(flow)) {
                    throw new BizException("会员卡充值失败.");
                }
                return flow;
            }
        }.execute();
    }

    @RequestMapping(value = "grant", method = RequestMethod.GET)
    public String grantPage(Model model, @RequestParam(value = "accountId", required = false) Long accountId) {
        Long shopId = UserUtils.getShopIdForSession(request);

        model.addAttribute("moduleUrl", "customer");
        model.addAttribute("subModule", "member-grant");
        if (Langs.isNotNull(accountId)) {
            List<MemberCard> memberCards = memberCardService.getMemberCardWithCardInfoListByAccountId(accountId);
            model.addAttribute("memberCardList", memberCards);
            AccountInfo accountInfo = accountInfoService.getAccountInfoById(accountId);
            if (null != accountInfo && null != accountInfo.getCustomerId()) {
                Map param = Maps.newHashMap();
                param.put("customerId",accountInfo.getCustomerId());
                param.put("shopId",shopId);
                List<CustomerCar> customerCars = customerCarService.select(param);
                if (Langs.isNotEmpty(customerCars)) {
                    model.addAttribute("carId", customerCars.get(0).getId());
                    model.addAttribute("license", customerCars.get(0).getLicense());
                }
            } else {
                log.error("未查到该车牌对应的accountInfo对象,accountId:{},shopId:{}", accountId, shopId);
            }
        }
        model.addAttribute("paymentList", paymentService.getPaymentsByShopId(shopId));
        return "yqx/page/account/member/grant";
    }

    @RequestMapping(value = "lastCard", method = RequestMethod.GET)
    @ResponseBody
    public Result<MemberCard> getLastGrantMemberCard(){
        Long shopId = UserUtils.getShopIdForSession(request);
        MemberCard memberCard = memberCardService.getMaxIdMemberCard(shopId);
        return Result.wrapSuccessfulResult(memberCard);
    }

    /**
     * 跳转退卡页面
     * @param model
     * @param accountId
     * @return
     */
    @HttpRequestLog
    @RequestMapping(value = "back", method = RequestMethod.GET)
    public String backPage(Model model,
                           @RequestParam("accountId") Long accountId,
                           HttpServletRequest request) {
        if (accountId == null || accountId < 0) {
            return "common/error";
        }
        model.addAttribute("moduleUrl", "customer");
        model.addAttribute("subModule", "account-index");

        Long shopId = UserUtils.getShopIdForSession(request);
        // 查询所有未过期的会员卡
        List<MemberCard> memberCardList = memberCardService.getUnExpiredMemberCardListByAccountId(shopId, accountId);
        List<MemberCardVo> memberCardVoList = Lists.newArrayList();
        if (Langs.isNotEmpty(memberCardList)) {
            for (MemberCard memberCard : memberCardList) {
                MemberCardVo memberCardVo = MemberCardConverter.convertVo(memberCard);
                if (memberCardVo != null) {
                    // 查询累计收款金额
                    BigDecimal totalPayAmount = accountTradeFlowService.getCardTotalPayAmount(shopId, memberCardVo.getMemberCardId());
                    memberCardVo.setTotalPayAmount(totalPayAmount);

                    memberCardVoList.add(memberCardVo);
                }
            }
        }
        model.addAttribute("memberCardList", memberCardVoList);

        return "yqx/page/account/member/back";
    }

    /**
     * 退卡操作
     *
     * @return
     */
    @RequestMapping(value = "back", method = RequestMethod.POST)
    @ResponseBody
    public Result backCard(BackCardVo backCardVo, HttpServletRequest request) {
        if (backCardVo == null) {
            return LegendAccountErrorCode.NUllPOINTER_ERROR.newResult("退卡操作参数不能为空");
        }
        if (backCardVo.getCardId() == null || backCardVo.getCardId() <= 0) {
            return LegendAccountErrorCode.PARAMS_ERROR.newResult("会员卡id不能为空");
        }
        if (backCardVo.getPaymentId() == null && backCardVo.getPaymentId() <= 0) {
            return LegendAccountErrorCode.PARAMS_ERROR.newResult("收款方式不能为空");
        }
        if (StringUtil.isStringEmpty(backCardVo.getPaymentName())) {
            return LegendAccountErrorCode.PARAMS_ERROR.newResult("收款方式名称不能为空");
        }
        if (backCardVo.getPayAmount() == null) {
            return LegendAccountErrorCode.PARAMS_ERROR.newResult("收款金额不能为空");
        }
        if (backCardVo.getPayAmount().compareTo(BigDecimal.ZERO) < 0) {
            return LegendAccountErrorCode.PARAMS_ERROR.newResult("收款金额不能小于0");
        }

        UserInfo userInfo = UserUtils.getUserInfo(request);
        backCardVo.setShopId(userInfo.getShopId());
        backCardVo.setUserId(userInfo.getUserId());
        try {
            if (this.memberCardService.backCard(backCardVo)) {
                return Result.wrapSuccessfulResult("退卡成功.");
            } else {
                return LegendAccountErrorCode.COMMON_ERROR.newResult("退卡失败");
            }
        } catch (IllegalStateException e) {
            return LegendAccountErrorCode.COMMON_ERROR.newResult(e.getMessage());
        }
    }


    @RequestMapping(value = "grant", method = RequestMethod.POST)
    @ResponseBody
    public Result grant(MemberGrantVo grantVo) {
        UserInfo userInfo = UserUtils.getUserInfo(request);

        if (grantVo == null) {
            return LegendAccountErrorCode.PARAMS_ERROR.newResult("非法参数");
        }

        try {
            AccountTradeFlow flow = memberFacadeService.grantMemberCard(grantVo, userInfo.getShopId(), userInfo.getUserId(), userInfo.getName());

            return Result.wrapSuccessfulResult(flow);
        } catch (Exception e) {
            e.printStackTrace();
            return LegendAccountErrorCode.COMMON_ERROR.newResult("办理会员卡失败.");
        }

    }

    @RequestMapping("number/exists")
    @ResponseBody
    public Result checkCardNumberExists(@RequestParam("cardNumber") String cardNumber) {
        if (StringUtils.isBlank(cardNumber)) {
            return LegendAccountErrorCode.PARAMS_ERROR.newResult("查询会员卡号不能为空");
        }
        Long shopId = UserUtils.getShopIdForSession(request);
        if (!this.memberCardService.isExistCardNumber(shopId, cardNumber, null)) {
            return Result.wrapSuccessfulResult(true);
        } else {
            return LegendAccountErrorCode.COMMON_ERROR.newResult("会员卡号已经存在");
        }
    }


    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String createPage(Model model) {
        model.addAttribute("moduleUrl", "customer");
        model.addAttribute("subModule", "account-setting");
        return "yqx/page/account/member/create";
    }

    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public String editPage(Model model, @RequestParam("id") Long id) {
        Long shopId = UserUtils.getShopIdForSession(request);
        MemberCardInfo memberCardInfo = memberCardInfoService.findById(shopId, id);
        CardInfoEditVo cardInfoEditVo = new CardInfoEditVoConverter().convert(memberCardInfo);
        model.addAttribute("memberCardInfo", cardInfoEditVo);
        model.addAttribute("moduleUrl", "customer");
        model.addAttribute("subModule", "account-setting");
        return "yqx/page/account/member/create";
    }


    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public com.tqmall.core.common.entity.Result<String> create(@RequestBody final CardCreateParam param) {
        return new ApiTemplate<String>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                checkParam(param);
            }

            @Override
            protected String process() throws BizException {
                UserInfo userInfo = UserUtils.getUserInfo(request);
                CardCreateBo cardCreateBo = new CardCreateConverter().convert(param);
                cardCreateBo.setShopId(userInfo.getShopId());
                cardCreateBo.setOperatorId(userInfo.getUserId());
                cardCreateBo.setOperatorName(userInfo.getName());
                memberCardInfoService.create(cardCreateBo);
                return "创建成功";
            }
        }.execute();

    }

    private void checkParam(@RequestBody CardCreateParam param) {
        Assert.notNull(param);
        Assert.hasLength(param.getName(), "会员卡名称不能为空");
        Assert.notNull(param.getDiscountType(), "请选择折扣类型");
        switch (param.getDiscountType().intValue()) {
            case 0:
                break;
            case 1:
                assertDiscountValue(param.getOrderDiscount(), "整单折扣值必须位于0~10之间");
                break;
            case 2:
                int serviceDiscountType = param.getServiceDiscountType().intValue();
                if (serviceDiscountType == 1) {
                    assertDiscountValue(param.getServiceDiscount(), "全部服务折扣值必须位于0~10之间");
                }
                if (serviceDiscountType == 2) {
                    Assert.notEmpty(param.getServiceRels(), "请选择关联服务类型");
                    for (Map.Entry<Long, BigDecimal> serviceCatRel : param.getServiceRels().entrySet()) {
                        assertDiscountValue(serviceCatRel.getValue(), "服务折扣值必须位于0~10之间");
                    }
                }
                break;
            case 3:
                int goodsDiscountType = param.getGoodsDiscountType().intValue();
                if (goodsDiscountType == 1) {
                    assertDiscountValue(param.getGoodsDiscount(), "全部配件折扣值必须位于0~10之间");
                }
                if (goodsDiscountType == 2) {
                    Assert.notEmpty(param.getGoodsCats(), "请选择关联配件类型");
                    for (GoodsCatParam goodsCatParam : param.getGoodsCats()) {
                        assertDiscountValue(goodsCatParam.getDiscount(), "配件折扣值必须位于0~10之间");
                    }
                }
                break;
            case 4:
                int goodsDiscountType2 = param.getGoodsDiscountType().intValue();
                if (goodsDiscountType2 == 1) {
                    assertDiscountValue(param.getGoodsDiscount(),"全部配件折扣值必须位于0~10之间");
                }
                if (goodsDiscountType2 == 2) {
                    Assert.notEmpty(param.getGoodsCats(), "请选择关联配件类型");
                    for (GoodsCatParam goodsCatParam : param.getGoodsCats()) {
                        assertDiscountValue(goodsCatParam.getDiscount(), "配件折扣值必须位于0~10之间");
                    }
                }
                int serviceDiscountType2 = param.getServiceDiscountType().intValue();
                if (serviceDiscountType2 == 1) {
                    assertDiscountValue(param.getServiceDiscount(),"全部服务折扣值必须位于0~10之间");
                }
                if (serviceDiscountType2 == 2) {
                    Assert.notEmpty(param.getServiceRels(), "请选择关联服务类型");
                    for (Map.Entry<Long, BigDecimal> serviceCatRel : param.getServiceRels().entrySet()) {
                        assertDiscountValue(serviceCatRel.getValue(),"服务折扣值必须位于0~10之间");
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("暂时不支持此类型");
        }

        /**
         * 有效期校验
         */
        if (param.getEffectivePeriodDays() == null
                || param.getEffectivePeriodDays().compareTo(0) < 0) {
            throw new IllegalArgumentException("有效期必须为大于等于0的整数.");
        }
        /**
         * 会员卡余额校验
         */
        if (param.getInitBalance() == null
                || param.getInitBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("会员卡余额必须为大于0");
        }
        /**
         * 会员卡售价校验
         */
        if (param.getSalePrice() == null
                || param.getSalePrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("会员卡售价必须为大于0");
        }
    }

    private void assertDiscountValue(BigDecimal discount, String message) {
        if (!DiscountUtil.checkDiscountValue(discount)) {
            throw new IllegalArgumentException(message);
        }
    }


    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @ResponseBody
    public com.tqmall.core.common.entity.Result<String> edit(@RequestBody final CardCreateParam param) {
        return new ApiTemplate<String>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                checkParam(param);
            }

            @Override
            protected String process() throws BizException {
                final UserInfo userInfo = UserUtils.getUserInfo(request);
                CardCreateBo updateBo = new CardCreateConverter().convert(param);
                updateBo.setShopId(userInfo.getShopId());
                updateBo.setOperatorId(userInfo.getUserId());
                updateBo.setOperatorName(userInfo.getName());
                updateBo.setId(param.getId());
                memberCardInfoService.update(updateBo);
                return "修改成功";
            }
        }.execute();

    }


    @RequestMapping("get_all_card_info")
    @ResponseBody
    public Result<List<CardInfoVo>> getAllMemberCardInfo(@RequestParam(value = "type", required = false) Integer type) {
        Long shopId = UserUtils.getShopIdForSession(request);
        List<MemberCardInfo> cardInfoList = memberCardInfoService.findAllByShopId(shopId, type);
        List<CardInfoVo> cardInfoVoList = new CardInfoListConverter().convert(cardInfoList);
        return Result.wrapSuccessfulResult(cardInfoVoList);
    }

    /**
     * 启用会员卡类型
     *
     * @param id 会员卡类型id
     * @return
     */
    @RequestMapping("enable")
    @ResponseBody
    public Result enable(@RequestParam("id") Long id) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        memberCardInfoService.enableCardInfoById(id, userInfo);
        return Result.wrapSuccessfulResult("启用成功!");
    }

    /**
     * 禁用会员卡类型
     *
     * @param id 会员卡类型id
     * @return
     */
    @RequestMapping("disable")
    @ResponseBody
    public Result disable(@RequestParam("id") Long id) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        memberCardInfoService.disableCardInfoById(id, userInfo);
        return Result.wrapSuccessfulResult("停用成功!");
    }

    /**
     * 删除会员卡类型
     *
     * @param id 会员卡类型id
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public com.tqmall.core.common.entity.Result<String> delete(@RequestParam("id") final Long id) {
        return new ApiTemplate<String>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(id, "ID不能为空");
            }

            @Override
            protected String process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                MemberCardInfo memberCardInfo = memberCardInfoService.findById(shopId, id);
                if (memberCardInfo.getId() == null) {
                    throw new BizException("查询不到此ID结果");
                }
                memberCardInfoService.deleteMemberCardInfoById(shopId, id);
                return "删除成功";
            }
        }.execute();
    }


    @HttpRequestLog
    @RequestMapping("grantList")
    @ResponseBody
    public Result search(@PageableDefault(page = 1, value = 10, sort = "gmt_create", direction = Sort.Direction.DESC) Pageable pageable) {

        Long shopId = UserUtils.getShopIdForSession(request);
        Map searchMap = Maps.newHashMap();
        List<MemberCard> resultList = Lists.newArrayList();
        searchMap.put("shopId", shopId);
        searchMap.put("tradeType", AccountTradTypeEnum.MEMBER_CARD.getCode());
        searchMap.put("consumeType", AccountCardFlowDetail.ConsumeTypeEnum.HANDLE_CARD.getCode());
//        Page<AccountTradeFlow> accountFlowPage = accountTradeFlowService.getMemberCardAccountTradFlowsByPage(pageable,searchMap);

        PageRequest pageRequest =
                new PageRequest((pageable.getPageNumber() < 1 ? 1 : pageable.getPageNumber()) - 1,
                        pageable.getPageSize() < 1 ? 1 : pageable.getPageSize(), pageable.getSort());
        Page<AccountTradeFlow> accountFlowPage = accountTradeFlowService.getAccountTradFlowsByPage(pageable, searchMap);

        List<AccountTradeFlow> accountTradFlow = accountFlowPage.getContent();

        if (CollectionUtils.isEmpty(accountTradFlow)) {
            DefaultPage<MemberCard> page = new DefaultPage<MemberCard>(resultList, pageRequest, accountFlowPage.getTotalElements());
            return Result.wrapSuccessfulResult(page);
        }

        List<Long> accountIds = Lists.transform(accountTradFlow, new Function<AccountTradeFlow, Long>() {
            @Override
            public Long apply(AccountTradeFlow input) {
                return input.getAccountId();
            }
        });

        List<Long> flowIds = Lists.transform(accountTradFlow, new Function<AccountTradeFlow, Long>() {
            @Override
            public Long apply(AccountTradeFlow input) {
                return input.getId();
            }
        });

        List<AccountCardFlowDetail> flowDetails = accountCardFlowDetailService.getCardFlowDetailByFlowIds(shopId, flowIds);

        List<Long> flowDetailList = Lists.transform(flowDetails, new Function<AccountCardFlowDetail, Long>() {
            @Override
            public Long apply(AccountCardFlowDetail accountCardFlowDetail) {
                return accountCardFlowDetail.getCardId();
            }
        });


        List<MemberCard> memberCards = memberCardService.getMemberCardWithDeletedByIds(shopId, flowDetailList);
        Map<Long, MemberCard> memberCardMap = Maps.uniqueIndex(memberCards, new Function<MemberCard, Long>() {
            @Override
            public Long apply(MemberCard memberCard) {
                return memberCard.getId();
            }
        });
        List<Long> infoIds = Lists.transform(memberCards, new Function<MemberCard, Long>() {
            @Override
            public Long apply(MemberCard input) {
                return input.getCardTypeId();
            }
        });

        if (CollectionUtils.isEmpty(memberCards)) {
            DefaultPage<MemberCard> page = new DefaultPage<MemberCard>(resultList, pageRequest, accountFlowPage.getTotalElements());
            return Result.wrapSuccessfulResult(page);
        }
        Map<Long, MemberCardInfo> infoMap = null;
        if (!CollectionUtils.isEmpty(infoIds)) {
            List<MemberCardInfo> memberCardInfoList = memberCardInfoService.selectInfoByIds(shopId, infoIds);
            if (CollectionUtils.isEmpty(memberCardInfoList)) {
                log.error("未找到会员卡办理流水对应的会员卡信息,请确认下列account_id的会员卡是否存在:{}", LogUtils.objectToString(accountIds));
                return LegendAccountErrorCode.PARAMS_ERROR.newResult("未找到办卡流水记录关联的会员卡");
            }
            infoMap = Maps.uniqueIndex(memberCardInfoList, new Function<MemberCardInfo, Long>() {
                @Override
                public Long apply(MemberCardInfo input) {
                    return input.getId();
                }
            });
        }

        Map<Long, AccountTradeFlow> accountTradeFlowMap = null;
        if (!CollectionUtils.isEmpty(accountIds)) {
            List<AccountInfo> accountInfoList = accountInfoService.getInfoByIds(accountIds);


            accountTradeFlowMap = Maps.uniqueIndex(accountTradFlow, new Function<AccountTradeFlow, Long>() {
                @Override
                public Long apply(AccountTradeFlow accountTradeFlow) {
                    return accountTradeFlow.getId();
                }
            });


            if (CollectionUtils.isEmpty(accountInfoList)) {
                log.error("未找到会员卡对应的账户信息,请确认下列account_id的账户是否存在:{}", LogUtils.objectToString(accountIds));
                return LegendAccountErrorCode.PARAMS_ERROR.newResult("未找到会员卡对应的账户信息");
            }
            for (AccountCardFlowDetail detailFlow : flowDetails) {
                MemberCard memberCardVo = new MemberCard();
                AccountTradeFlow tradeFlow = accountTradeFlowMap.get(detailFlow.getAccountTradeFlowId());
                if (null != tradeFlow) {
                    memberCardVo.setAccountId(tradeFlow.getAccountId());
                    memberCardVo.setMobile(tradeFlow.getMobile());
                    memberCardVo.setCustomerName(tradeFlow.getCustomerName());
                    memberCardVo.setFlowId(tradeFlow.getId());
                    memberCardVo.setShopId(shopId);
                    memberCardVo.setPayAmount(tradeFlow.getPayAmount());
                    memberCardVo.setGmtCreate(tradeFlow.getGmtCreate());
                    memberCardVo.setIsReversed(tradeFlow.getIsReversed());
                }
                MemberCard memberCard = memberCardMap.get(detailFlow.getCardId());
                if (null != memberCard) {
                    memberCardVo.setCardNumber(memberCard.getCardNumber());
                    memberCardVo.setPublisherName(memberCard.getPublisherName());
                    memberCardVo.setReceiverName(memberCard.getReceiverName());
                    MemberCardInfo memberCardInfo = infoMap.get(memberCard.getCardTypeId());
                    if (null != memberCardInfo) {
                        memberCardVo.setCardTypeName(memberCardInfo.getTypeName());
                    }
                }
                resultList.add(memberCardVo);
            }

        }

        Collections.reverse(resultList);

        DefaultPage<MemberCard> page = new DefaultPage<MemberCard>(resultList, pageRequest, accountFlowPage.getTotalElements());
        return Result.wrapSuccessfulResult(page);
    }

    @RequestMapping("rechargeFlow/list")
    @ResponseBody
    public Result listRechargeFlowForShop(@PageableDefault(page = 1, value = 10, sort = "gmt_create", direction = Sort.Direction.DESC) Pageable pageable,
                                          @RequestParam(required = false) String startTime,
                                          @RequestParam(required = false) String endTime,
                                          @RequestParam(required = false) String mobile) {
        Date date1 = DateUtil.convertStringToDateYMD(startTime);
        Date date2 = DateUtil.convertStringToDateYMD(endTime);
        date2 = DateUtil.getEndTime(date2);
        Long shopId = UserUtils.getShopIdForSession(request);
        try {
            Page<AccountTradeFlow> pageFlow = accountTradeFlowService.getPageFlowForCardRecharge(pageable, shopId, date1, date2,mobile);
            return Result.wrapSuccessfulResult(pageFlow);
        } catch (Exception e) {
            log.error("获取门店会员卡充值记录失败");
        }
        return LegendAccountErrorCode.COMMON_ERROR.newResult("获取数据失败");
    }

    @RequestMapping("rechargeSummary")
    @ResponseBody
    public Result getRechargeSummary(@RequestParam(required = false) String startTime,
                                     @RequestParam(required = false) String endTime) {
        Date date1 = DateUtil.convertStringToDateYMD(startTime);
        Date date2 = DateUtil.convertStringToDateYMD(endTime);
        date2 = DateUtil.getEndTime(date2);
        Long shopId = UserUtils.getShopIdForSession(request);
        BigDecimal summaryAmount = accountCardFlowDetailService.getRechargeSummaryAmount(shopId, date1, date2);
        Integer summaryCount = accountCardFlowDetailService.getRechargeCustomerCount(shopId, date1, date2);
        RechargeSummaryVo rechargeSummaryVo = new RechargeSummaryVo();
        rechargeSummaryVo.setSummaryAmount(summaryAmount);
        rechargeSummaryVo.setSummaryCount(summaryCount);
        return Result.wrapSuccessfulResult(rechargeSummaryVo);
    }

    @RequestMapping("grantPrint")
    public String printGrant(Model model, @RequestParam("id") Long flowId, @RequestParam("cardId") Long cardId) {
        Long shopId = UserUtils.getShopIdForSession(request);
        AccountTradeFlow tradeFlow = accountTradeFlowService.findById(shopId, flowId);
        Long accountId = tradeFlow.getAccountId();
        AccountInfo accountInfo = accountInfoService.getAccountInfoAllById(shopId, accountId);
        Shop shop = shopService.selectById(shopId);
        List<CustomerCar> cars = accountInfo.getCarLicences();
        StringBuilder licenses = new StringBuilder();
        if (cars != null) {
            for (CustomerCar item : cars) {
                licenses.append(item.getLicense()).append(";");
            }
            licenses.substring(0, licenses.length() - 1);
        }

        MemberCard memberCard = memberCardService.findById(cardId);
        if (memberCard == null) {
            return "common/error";
        }
        String expireDateStr = memberCard.getExpireDateStr();
        Long cardTypeId = memberCard.getCardTypeId();
        MemberCardInfo memberCardInfo = memberCardInfoService.findById(shopId, cardTypeId);
        if (memberCardInfo == null) {
            return "common/error";
        }
        String cardTypeName = memberCardInfo.getTypeName();
        Long effectivePeriodDays = memberCardInfo.getEffectivePeriodDays();
        String discountDescription = memberCardInfo.getDiscountDescription();

        String s1 = "可用余额:" + memberCard.getBalance();
        String s2 = "会员卡折扣" + discountDescription;
        String s3 = "有效期:" + effectivePeriodDays + "天";
        String s4 = "过期时间:" + expireDateStr;
        String remark = s1 + "  " + s2 + "  " + s3 + "  " + s4;
        RechargeGrantPrintVo vo = new RechargeGrantPrintVo();
        vo.setFlowSn(tradeFlow.getFlowSn());
        vo.setDateStr(tradeFlow.getGmtCreateStr());
        vo.setCardNumber(memberCard.getCardNumber());
        vo.setCustomerName(tradeFlow.getCustomerName());
        vo.setCarLicenses(licenses.toString());
        vo.setPhone(tradeFlow.getMobile());
        vo.setPayAmount(tradeFlow.getPayAmount());
        vo.setShopName(shop.getName());
        vo.setShopAddress(shop.getAddress());
        vo.setShopTele(shop.getTel());
        //        vo.setTips();
        vo.setName(cardTypeName);
        //        vo.setNumber();
        vo.setAmount(tradeFlow.getAmount());
        vo.setPayableAmount(tradeFlow.getAmount());
        vo.setRemark(remark);
        model.addAttribute("printObj", vo);
        return "yqx/page/account/member/grant-print";
    }

    @RequestMapping("rechargePrint")
    public String printRecharge(Model model, @RequestParam("id") Long flowId) {
        Long shopId = UserUtils.getShopIdForSession(request);
        AccountTradeFlow tradeFlow = accountTradeFlowService.findById(shopId, flowId);
        AccountCardFlowDetail detail = accountCardFlowDetailService.findByFlowId(shopId, flowId);
        Long cardId = detail.getCardId();
        MemberCard memberCard = null;
        if (cardId != null) {
            memberCard = memberCardService.findById(cardId);
        }
        Long accountId = tradeFlow.getAccountId();
        AccountInfo accountInfo = accountInfoService.getAccountInfoAllById(shopId, accountId);
        Shop shop = shopService.selectById(shopId);
        List<CustomerCar> cars = accountInfo.getCarLicences();
        String licenseStr = "";
        StringBuilder licenses = new StringBuilder();
        if (cars != null && cars.size() > 0) {
            for (CustomerCar item : cars) {
                licenses.append(item.getLicense()).append(";");
            }
        }
        if (licenses.length() >= 1) {
            licenseStr = licenses.substring(0, licenses.length() - 1);
        }
        String remark = "会员卡余额: " + tradeFlow.getCardBalance() + "元";
        RechargeGrantPrintVo vo = new RechargeGrantPrintVo();
        vo.setFlowSn(tradeFlow.getFlowSn());
        vo.setDateStr(tradeFlow.getGmtCreateStr());
        if (memberCard != null) {
            vo.setCardNumber(memberCard.getCardNumber());
        }
        vo.setCustomerName(tradeFlow.getCustomerName());
        vo.setCarLicenses(licenseStr);
        vo.setPhone(tradeFlow.getMobile());
        vo.setPayAmount(tradeFlow.getPayAmount());
        vo.setShopName(shop.getName());
        vo.setShopAddress(shop.getAddress());
        vo.setShopTele(shop.getTel());
        //        vo.setTips();
        vo.setName("余额充值");
        //        vo.setNumber();
        vo.setAmount(detail.getChangeAmount());
        vo.setPayableAmount(tradeFlow.getAmount());
        vo.setRemark(remark);
        model.addAttribute("printObj", vo);
        return "yqx/page/account/member/recharge-print";
    }

    /**
     * 会员卡办卡撤销操作
     * @param flowId 流水id
     * @return
     * @see <a href="http://confluence.weichedao.com/pages/viewpage.action?pageId=16693956">杨柳的需求</a>
     *
     */
    @HttpRequestLog
    @RequestMapping("reverse_grant")
    @ResponseBody
    public Result reverseGrant(@RequestParam("id") Long flowId) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long userId = userInfo.getUserId();
        Long shopId = userInfo.getShopId();
        String userName = userInfo.getName();

        AccountTradeFlow flow = accountTradeFlowService.findById(shopId, flowId);
        //1.是否已撤销
        if (flow == null) {
            return LegendAccountErrorCode.COMMON_ERROR.newResult("流水查询异常");
        }
        if (flow.getIsReversed() == 1) {
            return LegendAccountErrorCode.COMMON_ERROR.newResult("流水已撤销");
        }
        AccountCardFlowDetail cardFlowDetail = accountCardFlowDetailService.findByFlowId(shopId, flowId);
        if (Langs.isNull(cardFlowDetail)) {
            return LegendAccountErrorCode.COMMON_ERROR.newResult("查询会员卡流水明细异常");
        }

        if(!(flow.getTradeType() == AccountTradTypeEnum.MEMBER_CARD.getCode()
                && cardFlowDetail.getConsumeType() == AccountCardFlowDetail.ConsumeTypeEnum.HANDLE_CARD.getCode())) {
            return LegendAccountErrorCode.COMMON_ERROR.newResult("交易类型异常,非会员办卡交易");
        }

        // 要撤销的发放记录的会员卡id
        Long cardId = cardFlowDetail.getCardId();

        MemberCard memberCard = memberCardService.findById(cardId);
        if (Langs.isNull(memberCard)) {
            return LegendAccountErrorCode.COMMON_ERROR.newResult("会员卡已退卡,无法撤销");
        }

        // 查询会员卡升级链集合
        List<MemberCard> cardList = memberCardService.getUpgradedCardsContainDeletedById(shopId, cardId);
        if (CollectionUtils.isEmpty(cardList)) {
            return LegendAccountErrorCode.COMMON_ERROR.newResult("会员卡不存在");
        }

        List<Long> cardIds = Lists.transform(cardList, new Function<MemberCard, Long>() {
            @Override
            public Long apply(MemberCard memberCard) {
                return memberCard.getId();
            }
        });

        if (cardIds.size() > 1) {
            if(!memberCard.getId().equals(cardId)) {
                String cardTypeName = memberCardInfoService.getTypeNameById(memberCard.getShopId(), memberCard.getCardTypeId());
                return LegendAccountErrorCode.COMMON_ERROR.newResult("会员卡已升级为"+cardTypeName+",无法撤销");
            }
        }

        // 查询会员卡累计消费次数
        int cardConsumeNum = accountCardFlowDetailService.getCardConsumeNum(shopId, cardIds);
        if (cardConsumeNum > 0) {
            return LegendAccountErrorCode.COMMON_ERROR.newResult("会员卡已消费,无法撤销");
        }

        memberCardService.deleteMemberCard(cardId);

        //4.写流水
        CardGrantReverseBo cardGrantReverseBo = new CardGrantReverseBo();
        cardGrantReverseBo.setShopId(shopId);
        cardGrantReverseBo.setUserId(userId);
        cardGrantReverseBo.setUserName(userName);
        cardGrantReverseBo.setFlow(flow);
        accountFlowService.recordReverseFlowForCardGrant(cardGrantReverseBo);

        return Result.wrapSuccessfulResult("撤销成功");
    }


    @RequestMapping(value = "upgrade/get-info", method = RequestMethod.GET)
    @ResponseBody
    public com.tqmall.legend.web.common.Result upgradeCardInfo(@RequestParam(value = "cardId") Long cardId) {
        Long shopId = UserUtils.getShopIdForSession(request);

        /**
         * 获取会员卡实体信息
         */
        MemberCard memberCard = memberCardService.findById(cardId);
        if (Langs.isNull(memberCard)) {
            return com.tqmall.legend.web.common.Result.wrapErrorResult("", "获取会员卡信息失败,会员卡不存在.");
        }
        /**
         * 获取会员卡类型信息
         */
        MemberCardInfo memberCardInfo = this.memberCardInfoService.findById(shopId, memberCard.getCardTypeId());
        if (Langs.isNull(memberCardInfo)) {
            return com.tqmall.legend.web.common.Result.wrapErrorResult("", "获取会员卡类型信息失败.");
        }

        /**
         * 获取客户信息
         */
        AccountInfo accountInfo = this.accountInfoService.getAccountInfoById(shopId, memberCard.getAccountId());
        if (Langs.isNull(accountInfo)) {
            return com.tqmall.legend.web.common.Result.wrapErrorResult("", "获取账户信息异常，账户不存在.");
        }
        Customer customer = this.customerService.selectById(accountInfo.getCustomerId(), shopId);
        if (Langs.isNull(customer)) {
            return com.tqmall.legend.web.common.Result.wrapErrorResult("", "获取账户关联的客户信息异常.");
        }

        CardUpgradeAccountInfo cardUpgradeAccountInfo = new CardUpgradeAccountInfo();
        cardUpgradeAccountInfo.setCardId(cardId);
        //会员卡号
        cardUpgradeAccountInfo.setCardNumber(memberCard.getCardNumber());
        //卡内余额
        cardUpgradeAccountInfo.setCardBalance(memberCard.getBalance());
        //会员卡过期时间
        cardUpgradeAccountInfo.setExpireTime(memberCard.getExpireDate());
        //会员卡类型名
        cardUpgradeAccountInfo.setCardType(memberCardInfo.getTypeName());
        //客户姓名
        cardUpgradeAccountInfo.setCustomerName(customer.getCustomerName());
        //联系电话
        cardUpgradeAccountInfo.setMobile(customer.getMobile());
        //累计充值金额
        cardUpgradeAccountInfo.setTotalChargeAmount(this.accountTradeFlowService.getTotalChargeAmountByCardId(shopId, memberCard.getId()));
        /**
         * 获取可用的会员卡类型
         */
        List<MemberCardInfo> memberCardInfos = memberCardInfoService.findAllByShopId(shopId, MemberCardInfo.ENABLED);
        List<MemberCard> memberCards = memberCardService.getUnExpiredMemberCardListByAccountId(shopId,accountInfo.getId());
        final Set typeIdSet = Sets.newHashSet(Lists.transform(memberCards, new Function<MemberCard, Long>() {
            @Override
            public Long apply(MemberCard o) {
                return o.getCardTypeId();
            }
        }));
        Collection<MemberCardInfo> filteredCardInfoList =  Collections2.filter(memberCardInfos, new Predicate<MemberCardInfo>() {
            @Override
            public boolean apply(MemberCardInfo memberCardInfo) {
                if (typeIdSet.contains(memberCardInfo.getId())) {
                    return false;
                }
                return true;
            }
        });

        List<CardUpgradeCardInfo> cardUpgradeCardInfos = Lists.newArrayList();

        for (MemberCardInfo cardInfo : filteredCardInfoList) {
            if (!cardInfo.getId().equals(memberCard.getCardTypeId())) {
                CardUpgradeCardInfo cardUpgradeCardInfo = new CardUpgradeCardInfo();
                cardUpgradeCardInfo.setCardInfoId(cardInfo.getId());
                cardUpgradeCardInfo.setCardInfoName(cardInfo.getTypeName());
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DAY_OF_YEAR, cardInfo.getEffectivePeriodDays().intValue());
                cardUpgradeCardInfo.setExpireTime(c.getTime());
                cardUpgradeCardInfos.add(cardUpgradeCardInfo);
            }
        }
        cardUpgradeAccountInfo.setCardInfoList(cardUpgradeCardInfos);
        return com.tqmall.legend.web.common.Result.wrapSuccessfulResult(cardUpgradeAccountInfo);
    }

    /**
     * 会员卡升级
     *
     * @param cardId      会员卡id
     * @param cardTypeId     预升级的会员卡id
     * @param expireTimeType 会员卡升级过期时间（0.使用原会员卡过期时间;1.使用新的计算方式）
     * @return 会员卡升级是否成功
     */
    @HttpRequestLog
    @RequestMapping(value = "upgrade", method = RequestMethod.POST)
    @ResponseBody
    public com.tqmall.legend.web.common.Result upgradeCard(@RequestParam(value = "cardId") Long cardId,
                                                           @RequestParam(value = "cardTypeId") Long cardTypeId,
                                                           @RequestParam(value = "expireTimeType", defaultValue = "0") Integer expireTimeType) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        /**
         * 获取会员卡信息
         */
        MemberCard memberCard = memberCardService.findById(cardId);
        if (Langs.isNull(memberCard)) {
            return com.tqmall.legend.web.common.Result.wrapErrorResult("", "此账户不存在会员卡信息,无法升级");
        }
        List<MemberCard> memberCards = memberCardService.getUnExpiredMemberCardListByAccountId(memberCard.getShopId(),memberCard.getAccountId());
        if (memberCardService.checkDuplicate(memberCards,cardTypeId)) {
            return com.tqmall.legend.web.common.Result.wrapErrorResult("", "不能升级为已有类型的会员卡.");
        }
        if (expireTimeType != 0 && expireTimeType != 1) {
            return com.tqmall.legend.web.common.Result.wrapErrorResult("", "会员卡升级过期时间选择异常.");
        }

        MemberCardInfo memberCardInfo = this.memberCardInfoService.findById(userInfo.getShopId(), cardTypeId);
        if (Langs.isNull(memberCardInfo) || memberCardInfo.getCardInfoStatus() != MemberCardInfo.ENABLED) {
            return com.tqmall.legend.web.common.Result.wrapErrorResult("", "升级会员卡失败,待会员卡类型不存在或已禁用.");
        }

        CardUpgradeBo cardUpgradeBo = new CardUpgradeBo();
        cardUpgradeBo.setShopId(userInfo.getShopId());
        cardUpgradeBo.setOperatorId(userInfo.getUserId());
        cardUpgradeBo.setCardId(cardId);
        cardUpgradeBo.setAccountId(memberCard.getAccountId());
        cardUpgradeBo.setNewCardTypeId(cardTypeId);
        if (expireTimeType == 0) {
            cardUpgradeBo.setExpireTime(memberCard.getExpireDate());
        } else if (expireTimeType == 1) {
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DAY_OF_YEAR, memberCardInfo.getEffectivePeriodDays().intValue());
            cardUpgradeBo.setExpireTime(c.getTime());
        }
        if (this.memberCardService.upgradeCard(cardUpgradeBo)) {
            return com.tqmall.legend.web.common.Result.wrapSuccessfulResult("升级会员卡成功.");
        } else {
            return com.tqmall.legend.web.common.Result.wrapErrorResult("", "升级会员卡失败.");
        }

    }

    /**
     * 获取最近的会员卡号
     * @return
     */
    @RequestMapping(value = "get-current-cardnumber")
    @ResponseBody
    public Result<String> getCurrentCardNumber(){
        Long shopId = UserUtils.getShopIdForSession(request);
        MemberCard memberCard = memberCardService.getMaxIdMemberCard(shopId);
        return Result.wrapSuccessfulResult(memberCard.getCardNumber());
    }
}
