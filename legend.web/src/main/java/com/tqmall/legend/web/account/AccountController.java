package com.tqmall.legend.web.account;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.tqmall.common.Constants;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.common.util.DateUtil;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.annotation.HttpRequestLog;
import com.tqmall.legend.biz.account.*;
import com.tqmall.legend.biz.account.bo.AccountAndCarBO;
import com.tqmall.legend.biz.account.param.ComboExportArgs;
import com.tqmall.legend.biz.account.param.MemberExportArgs;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.customer.CustomerCarRelService;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.legend.biz.customer.CustomerService;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.settlement.PaymentService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.account.*;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.customer.CustomerCarRel;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.facade.account.AccountFacadeService;
import com.tqmall.legend.facade.account.vo.CustomerDiscountInfo;
import com.tqmall.legend.log.ExportLog;
import com.tqmall.legend.web.account.converter.AccountSearchConverter;
import com.tqmall.legend.web.account.param.ComboExportConditions;
import com.tqmall.legend.web.account.param.MemberExportConditions;
import com.tqmall.legend.web.account.vo.*;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.search.common.data.FieldsSort;
import com.tqmall.search.common.data.PageableRequest;
import com.tqmall.search.dubbo.client.legend.account.param.LegendAccountRequest;
import com.tqmall.search.dubbo.client.legend.account.result.LegendAccountDTO;
import com.tqmall.search.dubbo.client.legend.account.service.LegendAccountService;
import com.tqmall.wheel.component.excel.export.ExcelExportor;
import com.tqmall.wheel.exception.WheelException;
import com.tqmall.wheel.helper.BeanMapper;
import com.tqmall.wheel.helper.ExcelHelper;
import com.tqmall.wheel.lang.Langs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by wanghui on 6/2/16.
 */
@RequestMapping("account")
@Controller
@Slf4j
public class AccountController extends BaseController {

    @Autowired
    private LegendAccountService legendAccountService;
    @Autowired
    private AccountInfoService accountInfoService;
    @Autowired
    private AccountTradeFlowService accountTradeFlowService;
    @Autowired
    private MemberCardService memberCardService;
    @Autowired
    private MemberCardInfoService memberCardInfoService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerCarService customerCarService;
    @Autowired
    private AccountCouponService accountCouponService;
    @Autowired
    private AccountComboService accountComboService;
    @Autowired
    private ComboInfoService comboInfoService;
    @Autowired
    private ComboInfoServiceRelService comboInfoServiceRelService;
    @Autowired
    private AccountExportService accountExportService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopManagerService shopManagerService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private CustomerCarRelService customerCarRelService;
    @Autowired
    private AccountFacadeService accountFacadeService;


    @HttpRequestLog
    @RequestMapping
    public String index(Model model, HttpServletRequest request) {
        model.addAttribute("subModule", "account-index");
        model.addAttribute("moduleUrl", "customer");
        return "yqx/page/account/index";
    }

    /**
     * 账户详情页
     *
     * @param model
     * @return
     */
    @HttpRequestLog
    @RequestMapping(value = "detail", method = RequestMethod.GET)
    public String detail(Model model,@RequestParam(value = "customerId", required = false) Long customerId,
                         @RequestParam(value = "accountId", required = false) Long accountId) {
        Long shopId = UserUtils.getShopIdForSession(request);
        model.addAttribute("moduleUrl","customer");
        model.addAttribute("subModule","account-index");
        if(customerId != null){
            AccountInfo accountInfo = accountInfoService.getAccountInfoByCustomerIdAndShopId(shopId, customerId);
            Integer accountCouponNum = accountCouponService.getAccountCouponCountOfUnUsed(shopId, accountInfo.getId());
            Integer accountComboNum = accountComboService.selectCount(shopId, accountInfo.getId());
            int count = memberCardService.getCountByAccountId(shopId,accountInfo.getId());

            Map<String, List<CustomerCar>> multimap = getStringCustomerCarMultimap(accountInfo);
            model.addAttribute("cars", multimap);
            model.addAttribute("accountInfo", accountInfo);
            model.addAttribute("accountComboNum", accountComboNum);
            model.addAttribute("accountCouponNum", accountCouponNum);
            model.addAttribute("memberCardNum", count);
        }else if(accountId != null){
            AccountInfo accountInfo = accountInfoService.getAccountInfoById(accountId);
            AccountInfo account = accountInfoService.getAccountInfoByCustomerIdAndShopId(shopId, accountInfo.getCustomerId());
            Integer accountCouponNum = accountCouponService.getAccountCouponCountOfUnUsed(shopId, accountId);
            Integer accountComboNum = accountComboService.selectCount(shopId, accountId);
            int count = memberCardService.getCountByAccountId(shopId,accountInfo.getId());
            Map<String, List<CustomerCar>> multimap = getStringCustomerCarMultimap(account);
            model.addAttribute("cars", multimap);
            model.addAttribute("accountInfo", account);//
            model.addAttribute("accountComboNum", accountComboNum);//计次卡数
            model.addAttribute("accountCouponNum", accountCouponNum);//优惠券数
            model.addAttribute("memberCardNum", count);//会员卡数
        }
        return "yqx/page/account/detail";
    }


    /**
     * 账户详情页编辑
     *
     * @param model
     * @return
     */
    @HttpRequestLog
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public String edit(Model model,@RequestParam("accountId") Long accountId) {
        Long shopId = UserUtils.getShopIdForSession(request);
        model.addAttribute("moduleUrl","customer");
        model.addAttribute("subModule","account-index");
        if(accountId != null){
            model.addAttribute("accountInfo", accountInfoService.getAccountInfoAllById(shopId, accountId));
        }
        return "yqx/page/account/edit";
    }

    /**
     * 账户详情编辑提交
     *
     * @return
     */
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @ResponseBody
    public com.tqmall.core.common.entity.Result<String> edit(@RequestBody final CustomerAccountVO customerAccountVO) {
        return new ApiTemplate<String>() {
            Long shopId = UserUtils.getShopIdForSession(request);
            Customer customer = customerAccountVO.getCustomer();
            List<MemberCard> memberCards = customerAccountVO.getMemberCards();
            @Override
            protected void checkParams() throws IllegalArgumentException {
                if (customer.getId() == null){
                    throw new IllegalArgumentException("客户id不能为空");
                }
                if (StringUtils.isBlank(customer.getCustomerName())){
                    throw new IllegalArgumentException("姓名不能为空");
                }
                if (StringUtils.isBlank(customer.getMobile())){
                    throw new IllegalArgumentException("电话不能为空");
                }
                if (!StringUtil.isMobileNO(customer.getMobile())) {
                    throw new IllegalArgumentException("手机号码格式不正确");
                }
                if (!customer.getMobile().equals(customer.getOldMobile()) &&
                        !CollectionUtils.isEmpty(customerService.getCustomerByMobile(customer.getMobile(),shopId))){
                    throw new IllegalArgumentException("电话已存在");
                }

                Map<String,MemberCard> memberCardMap = Maps.newHashMap();
                for (MemberCard memberCard : memberCards) {
                    if (StringUtils.isBlank(memberCard.getCardNumber())){
                        throw new IllegalArgumentException("会员卡号不能为空");
                    }
                    if (memberCardService.isExistCardNumber(shopId, memberCard.getCardNumber(),memberCard.getId())){
                        throw new IllegalArgumentException("会员卡号"+memberCard.getCardNumber()+"已存在");
                    }
                    if (memberCardMap.containsKey(memberCard.getCardNumber())){
                        throw new IllegalArgumentException("会员卡号"+memberCard.getCardNumber()+"重复");
                    }
                    memberCardMap.put(memberCard.getCardNumber(),memberCard);
                }
            }

            @Override
            protected String process() throws BizException {
                customerService.update(customer);
                for (MemberCard memberCard : memberCards) {
                    memberCardService.update(memberCard);
                }
                return "客户资料编辑成功";
            }
        }.execute();

    }

    @HttpRequestLog
    @RequestMapping(value = "flow", method = RequestMethod.GET)
    public String flowPage(Model model,
                           @RequestParam(value = "startTime", required = false) String startTime,
                           @RequestParam(value = "endTime", required = false) String endTime) {
        model.addAttribute("moduleUrl", "customer");
        model.addAttribute("subModule", "account-index");

        // 营业报表跳转过来
        if (StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTime)) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date startDate = df.parse(startTime);
                Date endDate = df.parse(endTime);
                if (startDate.after(new Date()) || startDate.after(endDate)) {
                    return "yqx/page/account/flow";
                }
            } catch (ParseException e) {
                log.error("营业报表跳转客户管理-充值记录,日期格式错误,参数:startTime={}, endTime={}, 异常信息:", startTime, endTime, e);
                return "yqx/page/account/flow";
            }
            model.addAttribute("startTime", startTime);
            model.addAttribute("endTime", endTime);
        }

        return "yqx/page/account/flow";
    }

    /**
     * 车辆绑定账户
     */
    @HttpRequestLog
    @RequestMapping(value = "bundleCar", method = RequestMethod.POST)
    @ResponseBody
    public Result bundleCar(@RequestBody AccountAndCarVO accountAndCarVO,BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            return Result.wrapErrorResult("",fieldError.getDefaultMessage());
        }
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = UserUtils.getShopIdForSession(request);
        AccountAndCarBO accountAndCarBO = new AccountAndCarBO();
        BeanUtils.copyProperties(accountAndCarVO,accountAndCarBO);
        accountAndCarBO.setShopId(shopId);
        accountAndCarBO.setUserId(userInfo.getUserId());
        if (accountInfoService.bundlingCarToAccount(accountAndCarBO)){
            return Result.wrapSuccessfulResult("车辆绑定成功");
        }
        return Result.wrapErrorResult("","请勿重复绑定车辆");
    }

    /**
     * 车辆解绑账户
     */
    @HttpRequestLog
    @RequestMapping(value = "unBundleCarById", method = RequestMethod.POST)
    @ResponseBody
    public Result unBundleCarById(@RequestParam("carId") Long carId,@RequestParam("accountId")Long accountId) {
        if (carId == null) {
            return Result.wrapErrorResult("", "车牌号不能为空");
        }
        if (accountId == null) {
            return Result.wrapErrorResult("", "账户不能为空");
        }
        Long userId = UserUtils.getUserIdForSession(request);
        Long shopId = UserUtils.getShopIdForSession(request);
        AccountAndCarBO accountAndCarBO = new AccountAndCarBO();
        accountAndCarBO.setShopId(shopId);
        accountAndCarBO.setUserId(userId);
        accountAndCarBO.setAccountId(accountId);
        accountAndCarBO.setCustomerCarId(carId);
        if (accountInfoService.unBundlingCarFromAccount(accountAndCarBO)){
            return Result.wrapSuccessfulResult("车辆解绑成功");
        }
        return Result.wrapErrorResult("", "车辆解绑失败") ;
    }

    /**
     * 账户使用记录列表
     */
    @RequestMapping(value = "accountUsedList", method = RequestMethod.GET)
    @ResponseBody
    public Result accountUsedList(@RequestParam("accountId")Long accountId,@PageableDefault(page = 1, value = 10,sort = "gmt_create", direction = Sort.Direction.DESC) Pageable pageable) {
        Long shopId = UserUtils.getShopIdForSession(request);
        if(accountId==null||0==accountId){
            return Result.wrapErrorResult("","accountId不能为空");
        }
        Map paramMap = Maps.newHashMap();
        paramMap.put("shopId",shopId);
        paramMap.put("accountId",accountId);
        Page<AccountTradeFlow> accountTradeFlowPage = accountTradeFlowService.getAccountTradFlowsByPage(pageable,paramMap);
        return Result.wrapSuccessfulResult(accountTradeFlowPage);
    }

    /**
     * 查询会员卡对应的特权
     */
    @RequestMapping(value = "cardInfoQuery", method = RequestMethod.GET)
    @ResponseBody
    public Result cardInfoQuery(@RequestParam(value="accountId",required = true)Long accountId){
        Long shopId = UserUtils.getShopIdForSession(request);
        return memberCardService.queryAccountMemberCardType(shopId,accountId);
    }

    @RequestMapping("search")
    @ResponseBody
    public Result search(@RequestParam(value = "cardNumber", required = false)String cardNumber,
                         @RequestParam(value = "customerName", required = false)String customerName,
                         @RequestParam(value = "license", required = false)String license,
                         @RequestParam(value = "mobile", required = false)String mobile,
                         @RequestParam(value = "cardTypeId", required = false)Long cardTypeId,
                         @PageableDefault(page = 1, value = 10, sort = "gmt_create", direction = Sort.Direction.DESC) Pageable pageable) {

        Long shopId = UserUtils.getShopIdForSession(request);
        LegendAccountRequest param = new LegendAccountRequest();
        param.setShopId(shopId);
        param.setCardNumber(cardNumber);
        param.setCustomerName(customerName);
        param.setLicense(license);
        param.setMobile(mobile);
        param.setCardTypeId(cardTypeId);

        try {
            PageableRequest pageableRequest = new PageableRequest(pageable.getPageNumber()-1,
                    pageable.getPageSize(),new FieldsSort(pageable.getSort()));
            com.tqmall.search.common.result.Result<?> result = legendAccountService.queryAccountList(param, pageableRequest);
            if (result.isSuccess()) {
                if(result.getData() instanceof  Page) {
                    Page<LegendAccountDTO> searchPage = (Page<LegendAccountDTO>) result.getData();
                    List<AccountSearchVo> accountSearchVoList = AccountSearchConverter.convertList(searchPage.getContent());
                    DefaultPage<AccountSearchVo> page = new DefaultPage<>(accountSearchVoList, pageableRequest, searchPage.getTotalElements());
                    return Result.wrapSuccessfulResult(page);
                } else {
                    return Result.wrapErrorResult("", "调用搜索异常.");
                }
            } else {
                return Result.wrapErrorResult(result.getCode(), result.getMessage());
            }
        } catch (Exception e) {
            log.error("客户查询调用搜索异常，错误信息:{}", e);
            return Result.wrapErrorResult("", "调用搜索异常");
        }
    }



    /**
     * 根据车辆信息查账户信息
     */
    @RequestMapping(value = "queryAccountByLicense", method = RequestMethod.GET)
    @ResponseBody
    public Result queryCarInfo(@RequestParam(value = "carId", required = false)Long carId,@RequestParam(value = "accountId", required = false)Long accountId){
        Long shopId = UserUtils.getShopIdForSession(request);
        AccountInfoVo accountInfoVo = new AccountInfoVo();
        AccountInfo accountInfo = null;
        if(carId!=null&&carId!=0){
            accountInfo = accountInfoService.getAccountInfoByCarIdAndShopId(shopId,carId);
        }else if(accountId!=null&&accountId!=0){
            accountInfo=accountInfoService.getAccountInfoById(accountId);
        }
        if(null==accountInfo){
            return Result.wrapErrorResult("","该车牌未绑定账户");
        }
        accountInfoVo.setAccountInfo(accountInfo);
        Map searchMap = Maps.newHashMap();
        searchMap.put("id",accountInfo.getCustomerId());
        searchMap.put("shopId",shopId);
        List<Customer> customerList = customerService.select(searchMap);
        if(!CollectionUtils.isEmpty(customerList)){
            Customer customer = customerList.get(0);
            /**
             * 客户名和手机号都为空时为空账户
             */
            if (null == customer || (StringUtils.isEmpty(customer.getCustomerName()) && StringUtils.isEmpty(customer.getMobile()))) {
                return Result.wrapErrorResult("","该账户为空账户");
            }
            accountInfoVo.setCustomer(customerList.get(0));
            Map carMap = Maps.newHashMap();
            carMap.put("shopId",shopId);
            carMap.put("customerId",customerList.get(0).getId());
//            List<CustomerCar> customerCars = customerCarService.select(carMap);
            /**
             * 获取拥有关系车牌
             */
            List<CustomerCar> customerCars = customerCarService.select(carMap);

            /**
             * 获取绑定关系车牌
             */
            List<CustomerCarRel> customerCarRels = customerCarRelService.selectByCustomerId(shopId,customerList.get(0).getId());
            List<Long> carIds = Lists.transform(customerCarRels, new Function<CustomerCarRel, Long>() {
                @Override
                public Long apply(CustomerCarRel customerCarRel) {
                    return customerCarRel.getCustomerCarId();
                }
            });
            List<CustomerCar> bindCarList = customerCarService.selectByIds(carIds);
            if (!CollectionUtils.isEmpty(bindCarList)) {
                customerCars.addAll(bindCarList);
            }

            if(!CollectionUtils.isEmpty(customerCars)){
                accountInfoVo.setCustomerCarList(customerCars);
            }
        }else {
            log.error("未找到客户信息");
            return Result.wrapErrorResult("","未找到客户信息");
        }
        if (accountInfoVo.getAccountInfo() != null) {
            try {
                List<MemberCard> memberCards = this.memberCardService.getMemberCardWithCardInfoListByAccountId(accountInfoVo.getAccountInfo().getId());
                accountInfoVo.setMemberCardList(getUnExpireMemberCard(memberCards));
//                if (memberCards.size()>3) {
//                    log.error("一个账户绑定了多于3张的会员卡!accountId:{}",accountInfoVo.getAccountInfo().getId());
//                    return Result.wrapErrorResult("9995","一个账户最多只能绑定3张会员卡");
//                }
            }catch (Exception e){
                log.error("数据异常!accountId:{}",accountInfoVo.getAccountInfo().getId());
                return Result.wrapErrorResult("","数据异常!");
            }
        }
        return Result.wrapSuccessfulResult(accountInfoVo);
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

    /**
     * 查询客户优惠信息
     *
     * @param carId
     * @param request
     * @return
     */
    @RequestMapping(value = "discount_info", method = RequestMethod.GET)
    @ResponseBody
    public Result getDiscount(@RequestParam("carId") Long carId, HttpServletRequest request) {
        Long shopId = UserUtils.getShopIdForSession(request);
        List<CustomerDiscountInfo> customerDiscountInfoList = accountFacadeService.getCustomerDiscountInfo(shopId, carId);
        return Result.wrapSuccessfulResult(customerDiscountInfoList);
    }

    /**
     * 卡券汇总
     * @param model
     * @return
     */
    @RequestMapping(value = "summary", method = RequestMethod.GET)
    public String cardSummaryPage(Model model, HttpServletRequest request) {
        model.addAttribute("moduleUrl", "customer");
        model.addAttribute("subModule", "account-index");
        return "yqx/page/account/summary";
    }


    @RequestMapping("card_info_pair/list")
    @ResponseBody
    public com.tqmall.core.common.entity.Result listCardInfoPair() {
        return new ApiTemplate<List<PairVO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            @Override
            protected List<PairVO> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                List<MemberCardInfo> infoList = memberCardInfoService.list(shopId);
                List<PairVO> pairVOList = Lists.newArrayList();
                for (MemberCardInfo info : infoList) {
                    PairVO pairVO = new PairVO(info.getId(), info.getTypeName());
                    pairVOList.add(pairVO);
                }
                return pairVOList;
            }
        }.execute();
    }

    @RequestMapping("combo_info_pair/list")
    @ResponseBody
    public com.tqmall.core.common.entity.Result listComboPair() {
        return new ApiTemplate<List<PairVO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            @Override
            protected List<PairVO> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                List<ComboInfo> infoList = comboInfoService.list(shopId);
                List<PairVO> pairVOList = Lists.newArrayList();
                for (ComboInfo info : infoList) {
                    PairVO pairVO = new PairVO(info.getId(), info.getComboName());
                    pairVOList.add(pairVO);
                }
                return pairVOList;
            }
        }.execute();
    }

    @RequestMapping("combo_service_pair/list")
    @ResponseBody
    public com.tqmall.core.common.entity.Result listComboServicePair(@RequestParam(required = false) final Long comboId) {
        return new ApiTemplate<List<PairVO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            @Override
            protected List<PairVO> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                List<ComboInfoServiceRel> comboInfoServiceList = comboInfoServiceRelService.findByComboInfoId(shopId, comboId);
                Set<PairVO> pairVOSet = Sets.newHashSet();
                for (ComboInfoServiceRel infoService : comboInfoServiceList) {
                    PairVO pairVO = new PairVO(infoService.getServiceId(), infoService.getServiceName());
                    pairVOSet.add(pairVO);
                }
                return Lists.newArrayList(pairVOSet);
            }
        }.execute();
    }

    /**
     *
     * @param pageable sort property can be :expireDate remainNumber createDate(default)
     * @param conditions
     * @return
     */
    @HttpRequestLog
    @RequestMapping("combo/page")
    @ResponseBody
    public com.tqmall.core.common.entity.Result pageCombo(
            @PageableDefault(page = 1,sort = "createDate", direction = Sort.Direction.DESC) final Pageable pageable,
            final ComboExportConditions conditions) {
        return new ApiTemplate<Page<ComboExportVO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            @Override
            protected Page<ComboExportVO> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                ComboExportArgs param = new ComboExportArgs();
                BeanUtils.copyProperties(conditions, param);
                param.setShopId(shopId);
                Page<ComboExportEntity> rawPage = accountExportService.pageCombo(pageable, param);
                List<ComboExportVO> content = BeanMapper.mapListIfPossible(rawPage.getContent(), ComboExportVO.class);

                return new PageImpl<>(content, pageable, rawPage.getTotalElements());
            }
        }.execute();
    }

    /**
     *
     * @param pageable sort property can be :expireDate balance createDate(default)
     * @param conditions
     * @return
     */
    @HttpRequestLog
    @RequestMapping("member/page")
    @ResponseBody
    public com.tqmall.core.common.entity.Result pageMember(
            @PageableDefault(page = 1,sort = "createDate", direction = Sort.Direction.DESC) final Pageable pageable,
            final MemberExportConditions conditions) {
        return new ApiTemplate<Page<MemberExportVO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            @Override
            protected Page<MemberExportVO> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                MemberExportArgs param = new MemberExportArgs();
                BeanUtils.copyProperties(conditions, param);
                param.setShopId(shopId);
                Page<MemberExportEntity> rawPage = accountExportService.pageMemeber(pageable, param);
                List<MemberExportVO> content = BeanMapper.mapListIfPossible(rawPage.getContent(), MemberExportVO.class);
                return new PageImpl<>(content, pageable, rawPage.getTotalElements());
            }
        }.execute();
    }

    @RequestMapping("member/summary")
    @ResponseBody
    public com.tqmall.core.common.entity.Result summaryMember(final MemberExportConditions conditions) {
        return new ApiTemplate<MemberExportSummaryVO>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            @Override
            protected MemberExportSummaryVO process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                MemberExportArgs args = new MemberExportArgs();
                args.setShopId(shopId);
                BeanUtils.copyProperties(conditions, args);
                MemberExportSummary summary = accountExportService.summarizeMember(args);
                MemberExportSummaryVO summaryVO = new MemberExportSummaryVO();
                BeanUtils.copyProperties(summary, summaryVO);
                return summaryVO;
            }
        }.execute();
    }

    @RequestMapping("combo/summary")
    @ResponseBody
    public com.tqmall.core.common.entity.Result summaryCombo(final ComboExportConditions conditions) {
        return new ApiTemplate<ComboExportSummaryVO>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            @Override
            protected ComboExportSummaryVO process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                ComboExportArgs args = new ComboExportArgs();
                args.setShopId(shopId);
                BeanUtils.copyProperties(conditions, args);
                ComboExportSummary summary = accountExportService.summarizeCombo(args);
                ComboExportSummaryVO summaryVO = new ComboExportSummaryVO();
                BeanUtils.copyProperties(summary, summaryVO);
                return summaryVO;
            }
        }.execute();
    }

    @RequestMapping("combo/export")
    public void exportCombo(
            @PageableDefault(page = 1,sort = "createDate", direction = Sort.Direction.DESC) final Pageable pageable,
            ComboExportConditions conditions, HttpServletResponse response)
            throws IOException, WheelException {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        String fileName = "计次卡查询结果导出-" + DateUtil.convertDateToStr(new Date(), "yyyyMMdd");
        ExcelExportor exportor = null;
        ComboExportArgs args = new ComboExportArgs();
        args.setShopId(userInfo.getShopId());
        BeanUtils.copyProperties(conditions, args);

        try {
            long sTime = System.currentTimeMillis();
            exportor = ExcelHelper.createDownloadExportor(response, fileName);
            writeComboSummary(exportor, args);
            Shop shop = shopService.selectById(userInfo.getShopId());
            String headline = shop.getName() + "——计次卡查询结果导出";
            exportor.writeTitle(null, headline, ComboExportVO.class);
            Pageable pageRequest = new PageRequest(1, Constants.EXCEL_EXPORT_LIMIT, pageable.getSort());
            int exportSize = 0;
            while (true) {
                Page<ComboExportEntity> rawPage = accountExportService.pageCombo(pageRequest, args);
                List<ComboExportEntity> rawItems = rawPage.getContent();
                if (CollectionUtils.isEmpty(rawItems)) {
                    break;
                }
                List<ComboExportVO> exportItems = BeanMapper.mapListIfPossible(rawItems, ComboExportVO.class);
                exportor.write(exportItems);
                exportSize += exportItems.size();
                pageRequest = pageRequest.next();
            }
            logExport(userInfo, "计次卡查询结果导出", sTime, exportSize);
        } finally {
            ExcelHelper.closeQuiet(exportor);
        }

    }

    private void writeComboSummary(ExcelExportor exportor, ComboExportArgs args) throws WheelException {
        ComboExportSummary summary = accountExportService.summarizeCombo(args);
        StringBuilder summarySB = new StringBuilder("查询结果:");
        summarySB.append(" 有效计次卡总数").append(summary.getComboNumberSummary()).append("张")
                .append(" 服务项目合计").append(summary.getServiceNumberSummary()).append("项")
                .append(" 服务项目剩次数合计").append(summary.getComboServiceRemainSummary()).append("次");
        Row row = exportor.createRow();
        Cell summaryCell = row.createCell(0, CellType.STRING);
        summaryCell.setCellValue(summarySB.toString());
        exportor.mergeCell(0,row.getRowNum(), row.getRowNum(), row.getFirstCellNum(), 7);
        exportor.createRow();//增加空行
    }

    @RequestMapping("member/export")
    public void exportMember(
            @PageableDefault(page = 1,sort = "createDate", direction = Sort.Direction.DESC) final Pageable pageable,
            MemberExportConditions conditions,
            HttpServletResponse response) throws IOException, WheelException {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        String fileName = "会员卡查询结果导出-" + DateUtil.convertDateToStr(new Date(), "yyyyMMdd");
        ExcelExportor exportor = null;
        MemberExportArgs args = new MemberExportArgs();
        args.setShopId(userInfo.getShopId());
        BeanUtils.copyProperties(conditions, args);

        try {
            long sTime = System.currentTimeMillis();
            exportor = ExcelHelper.createDownloadExportor(response, fileName);
            writeMemberSummary(exportor, args);
            Shop shop = shopService.selectById(userInfo.getShopId());
            String headline = shop.getName() + "——会员卡查询结果导出";
            exportor.writeTitle(null, headline, MemberExportVO.class);
            Pageable pageRequest = new PageRequest(1, Constants.EXCEL_EXPORT_LIMIT, pageable.getSort());
            int exportSize = 0;
            while (true) {
                Page<MemberExportEntity> rawPage = accountExportService.pageMemeber(pageRequest, args);
                List<MemberExportEntity> rawItems = rawPage.getContent();
                if (CollectionUtils.isEmpty(rawItems)) {
                    break;
                }
                List<MemberExportVO> exportItems = BeanMapper.mapListIfPossible(rawItems, MemberExportVO.class);
                exportor.write(exportItems);
                exportSize += exportItems.size();
                pageRequest = pageRequest.next();
            }
            logExport(userInfo, "会员卡查询结果导出", sTime, exportSize);
        } finally {
            ExcelHelper.closeQuiet(exportor);
        }
    }

    private void writeMemberSummary(ExcelExportor exportor, MemberExportArgs args) throws WheelException {
        MemberExportSummary summary = accountExportService.summarizeMember(args);
        StringBuilder summarySB = new StringBuilder("查询结果:");
        summarySB.append(" 有效会员卡总数").append(summary.getNumberSummary()).append("张")
                .append(" 余额合计").append(summary.getBalanceSummary()).append("元")
                .append(" 累计充值金额合计").append(summary.getDepositSummary()).append("元");
        Row row = exportor.createRow();
        Cell summaryCell = row.createCell(0, CellType.STRING);
        summaryCell.setCellValue(summarySB.toString());
        exportor.mergeCell(0, row.getRowNum(), row.getRowNum(), row.getFirstCellNum(), 8);
        exportor.createRow();//增加空行
    }

    private void logExport(UserInfo userInfo, String fileName, long startTime,
                           int size) {
        long elapsedTime = System.currentTimeMillis() - startTime;
        String exportLog = ExportLog.getExportLog(fileName, userInfo, size, elapsedTime);
        log.info(exportLog);
    }

    private Map<String, List<CustomerCar>> getStringCustomerCarMultimap(AccountInfo accountInfo) {
        List<CustomerCar> customerCars = accountInfo.getCarLicences();
        Map<String,List<CustomerCar>> multimap = Maps.newLinkedHashMap();

        for (CustomerCar customerCar : customerCars) {
            String key = String.format("%s %s", customerCar.getCustomerName(), customerCar.getMobile());

            if (multimap.containsKey(key)){
                multimap.get(key).add(customerCar);
            }else {
                List<CustomerCar> cars = Lists.newArrayList();
                cars.add(customerCar);
                multimap.put(key, cars);
            }
        }
        return multimap;
    }

    /**
     * 卡券办理页面
     * @param model
     * @param accountId
     * @return
     */
    @HttpRequestLog
    @RequestMapping(value = "grant", method = RequestMethod.GET)
    public String grantPage(Model model, @RequestParam(value = "accountId", required = false) Long accountId) {
        Long shopId = UserUtils.getShopIdForSession(request);

        model.addAttribute("moduleUrl", "customer");
        model.addAttribute("subModule", "grant");
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
        model.addAttribute("reciveList",shopManagerService.selectByShopId(shopId));
        return "yqx/page/account/new-grant";
    }

}
