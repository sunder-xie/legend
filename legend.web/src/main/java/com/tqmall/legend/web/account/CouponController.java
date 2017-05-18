package com.tqmall.legend.web.account;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.error.LegendAccountErrorCode;
import com.tqmall.legend.annotation.HttpRequestLog;
import com.tqmall.legend.biz.account.AccountCouponService;
import com.tqmall.legend.biz.account.AccountFlowService;
import com.tqmall.legend.biz.account.AccountInfoService;
import com.tqmall.legend.biz.account.AccountSuiteService;
import com.tqmall.legend.biz.account.AccountTradeFlowService;
import com.tqmall.legend.biz.account.CouponInfoService;
import com.tqmall.legend.biz.account.CouponSuiteService;
import com.tqmall.legend.biz.account.MemberCardService;
import com.tqmall.legend.biz.account.bo.CouponCreateBo;
import com.tqmall.legend.biz.account.bo.CouponRechargeReverseBo;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.legend.biz.customer.CustomerService;
import com.tqmall.legend.biz.settlement.PaymentService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.entity.account.AccountConsumeTypeEnum;
import com.tqmall.legend.entity.account.AccountCoupon;
import com.tqmall.legend.entity.account.AccountInfo;
import com.tqmall.legend.entity.account.AccountSuite;
import com.tqmall.legend.entity.account.AccountTradTypeEnum;
import com.tqmall.legend.entity.account.AccountTradeFlow;
import com.tqmall.legend.entity.account.CouponInfo;
import com.tqmall.legend.entity.account.CouponSuite;
import com.tqmall.legend.entity.account.MemberCard;
import com.tqmall.legend.entity.account.vo.AccountCouponVo;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.settlement.Payment;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.web.account.converter.CashCouponCreateConverter;
import com.tqmall.legend.web.account.converter.UniversalCouponCreateConverter;
import com.tqmall.legend.web.account.vo.CashCouponCreateParam;
import com.tqmall.legend.web.account.vo.RechargeGrantPrintVo;
import com.tqmall.legend.web.account.vo.UniversalCouponCreateParam;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.common.Result;
import com.tqmall.legend.web.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by wanghui on 6/2/16.
 */
@RequestMapping("account/coupon")
@Controller
@Slf4j
public class CouponController extends BaseController {
    @Autowired
    private CouponInfoService couponInfoService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private AccountSuiteService accountSuiteService;
    @Autowired
    private AccountCouponService accountCouponService;
    @Autowired
    private CouponSuiteService couponSuiteService;
    @Autowired
    private AccountTradeFlowService accountTradeFlowService;
    @Autowired
    private AccountInfoService accountInfoService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private AccountFlowService accountFlowService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerCarService customerCarService;

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String createPage(Model model,@RequestParam(value = "id", required = false)Long id) {
        if(id != null && id != 0l) {
            Long shopId = UserUtils.getShopIdForSession(request);
            CouponInfo couponInfo = couponInfoService.selectById(id, shopId);
            model.addAttribute("couponInfo", couponInfo);
        }
        model.addAttribute("moduleUrl", "customer");
        model.addAttribute("subModule","account-setting");
        return "yqx/page/account/coupon/create";
    }

    @RequestMapping(value = "create/universal", method = RequestMethod.POST)
    @ResponseBody
    public com.tqmall.core.common.entity.Result<String> createUniversalInfo(@RequestBody final UniversalCouponCreateParam param) {
        return new ApiTemplate<String>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                checkUniversalCouponParam(param);
            }

            @Override
            protected String process() throws BizException {
                UserInfo userInfo = UserUtils.getUserInfo(request);
                CouponCreateBo couponCreateBo = new UniversalCouponCreateConverter().convert(param);
                couponCreateBo.setShopId(userInfo.getShopId());
                couponCreateBo.setUserId(userInfo.getUserId());
                couponCreateBo.setUserName(userInfo.getName());
                couponInfoService.create(couponCreateBo);
                return "创建成功";
            }
        }.execute();
    }

    private void checkUniversalCouponParam(UniversalCouponCreateParam param) {
        Assert.notNull(param,"参数不能为空");
        Assert.hasLength(param.getCouponName(),"优惠券名称输入错误");
        //                Assert.notNull(param.getDiscountAmount(), "抵扣金额输入错误");
        Assert.notNull(param.getEffectivePeriodDays(),"请添加有效期");
    }

    @RequestMapping(value = "create/cash", method = RequestMethod.POST)
    @ResponseBody
    public com.tqmall.core.common.entity.Result<String> createCashInfo(@RequestBody final CashCouponCreateParam param) {
        return new ApiTemplate<String>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                checkCashCouponParam(param);

            }

            @Override
            protected String process() throws BizException {
                UserInfo userInfo = UserUtils.getUserInfo(request);
                CouponCreateBo couponCreateBo = new CashCouponCreateConverter().convert(param);
                couponCreateBo.setShopId(userInfo.getShopId());
                couponCreateBo.setUserId(userInfo.getUserId());
                couponCreateBo.setUserName(userInfo.getName());
                couponInfoService.create(couponCreateBo);
                return "创建成功";
            }
        }.execute();
    }

    private void checkCashCouponParam(CashCouponCreateParam param) {
        Assert.notNull(param,"参数不能为空");
        Assert.hasLength(param.getCouponName(),"优惠券名称输入错误");
        Assert.notNull(param.getDiscountAmount(), "抵扣金额输入错误");
        Assert.notNull(param.getLimitAmount(), "限制金额输入错误");
        Assert.notNull(param.getUseRange(), "限制金额输入错误");
        if (param.getUseRange() == 2) {
            Assert.notEmpty(param.getServiceIds(),"请添加服务");
        }
        if (!param.isPeriodCustomized()) {
            Assert.notNull(param.getEffectivePeriodDays(), "请添加有效期");
        }
        if (param.isPeriodCustomized()) {
            Assert.hasLength(param.getEffectiveDate(), "生效日期输入错误");
            Assert.hasLength(param.getExpiredDate(), "失效日期输入错误");
        }
    }

    @RequestMapping("update/cash")
    @ResponseBody
    public com.tqmall.core.common.entity.Result<String> updateCashInfo(@RequestBody final CashCouponCreateParam param){
        return new ApiTemplate<String>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                checkCashCouponParam(param);
                Assert.notNull(param.getId());
            }

            @Override
            protected String process() throws BizException {
                UserInfo userInfo = UserUtils.getUserInfo(request);
                CouponCreateBo couponUpdateBo = new CashCouponCreateConverter().convert(param);
                couponUpdateBo.setShopId(userInfo.getShopId());
                couponUpdateBo.setUserId(userInfo.getUserId());
                couponUpdateBo.setUserName(userInfo.getName());
                couponInfoService.update(couponUpdateBo);
                return "修改成功";
            }
        }.execute();

    }

    @RequestMapping("update/universal")
    @ResponseBody
    public com.tqmall.core.common.entity.Result<String> updateUniversalInfo(@RequestBody final UniversalCouponCreateParam param){
        return new ApiTemplate<String>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                checkUniversalCouponParam(param);
                Assert.notNull(param.getId());
            }

            @Override
            protected String process() throws BizException {
                UserInfo userInfo = UserUtils.getUserInfo(request);
                CouponCreateBo couponUpdateBo = new UniversalCouponCreateConverter().convert(param);
                couponUpdateBo.setShopId(userInfo.getShopId());
                couponUpdateBo.setUserId(userInfo.getUserId());
                couponUpdateBo.setUserName(userInfo.getName());
                couponInfoService.update(couponUpdateBo);
                return "修改成功";
            }
        }.execute();

    }

    @RequestMapping("search")
    @ResponseBody
    public Result list(@RequestParam(value = "serviceName", required = false)String serviceName,
                       @RequestParam(value = "couponType", required = false)Integer couponType){
        Map<String, Object> param = ServletUtils.getParametersMapStartWith(request);
        if(serviceName != null) {
            param.put("nameLike", serviceName);
            param.put("couponStatus",1);
        }
        if(couponType!=null){
            param.put("couponType",couponType);
            param.put("couponStatus",1);
        }
        param.put("shopId", UserUtils.getShopIdForSession(request));
        List<CouponInfo> couponInfos = couponInfoService.selectWithCount(param);
        return Result.wrapSuccessfulResult(couponInfos);
    }

    @RequestMapping("get")
    @ResponseBody
    public Result get(@RequestParam(value = "accountId", required = false)Long accountId){
        Long shopId = UserUtils.getShopIdForSession(request);
        List<AccountCoupon> accountCoupon = accountCouponService.getAccountCouponListByGroup(shopId, accountId);
        return Result.wrapSuccessfulResult(accountCoupon);
    }

    @RequestMapping("status/update")
    @ResponseBody
    public Result updateStatus(@RequestParam("id")Long id){
        Long shopId = UserUtils.getShopIdForSession(request);
        if(id == null){
            return Result.wrapErrorResult("","ID不能为空");
        }
        CouponInfo couponInfo = couponInfoService.selectById(id, shopId);
        Integer couponStatus = couponInfo.getCouponStatus();
        if(couponStatus == null){
            return Result.wrapErrorResult("","查询不到此ID结果");
        }
        String result;
        if(couponStatus.intValue() == 1){
            couponInfo.setCouponStatus(2);
            result = "停用成功";
        } else {
            couponInfo.setCouponStatus(1);
            result = "启用成功";
        }
        couponInfoService.updateBase(couponInfo);
        return Result.wrapSuccessfulResult(result);
    }

    @RequestMapping("delete")
    @ResponseBody
    public com.tqmall.core.common.entity.Result<String> delete(@RequestParam("id") final Long id){
        return new ApiTemplate<String>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(id, "ID不能为空");
            }

            @Override
            protected String process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                CouponInfo couponInfo = couponInfoService.selectById(id, shopId);
                if(couponInfo.getId() == null){
                    throw new BizException("查询不到此ID结果");
                }
                couponInfoService.delete(shopId,id);
                return "删除成功";
            }
        }.execute();
    }

    @HttpRequestLog
    @RequestMapping("flow/list")
    @ResponseBody
    public Result flowList(@PageableDefault(page = 1, value = 10, sort = "id",
            direction = Sort.Direction.DESC) Pageable pageable) {
        Map<String, Object> param = ServletUtils.getParametersMapStartWith(request);
        param.put("shopId", UserUtils.getShopIdForSession(request));
        param.put("tradeType", AccountTradTypeEnum.COUPON.getCode());

        List<Integer> consumeTypes = Lists.newArrayList();
        consumeTypes.add(AccountConsumeTypeEnum.CHARGE.getCode());
        consumeTypes.add(AccountConsumeTypeEnum.INIT.getCode());
        param.put("consumeTypes", consumeTypes);
        Page<AccountTradeFlow> flow = accountTradeFlowService.getAccountTradFlowsByPage(pageable, param);
        return Result.wrapSuccessfulResult(flow);
    }

    @RequestMapping(value="grant",method = RequestMethod.GET)
    public String grantPage(Model model,@RequestParam("id")Long id){
        Long shopId = UserUtils.getShopIdForSession(request);
        AccountInfo accountInfo = accountInfoService.getAccountInfoById(shopId, id);
        if (accountInfo == null) {
            return "common/error";
        }
        List<Payment> payments = paymentService.getPaymentsByShopId(shopId);
        model.addAttribute("payment", payments);
        Map<String, Object> param = new HashMap<>();
        param.put("shopId", UserUtils.getShopIdForSession(request));
        param.put("suiteStatus",1);
        List<CouponSuite> couponSuites = couponSuiteService.select(param);
        model.addAttribute("couponSuites",couponSuites);
        model.addAttribute("id",id);
        model.addAttribute("moduleUrl","customer");
        model.addAttribute("subModule", "account-index");
        Long customerId = accountInfo.getCustomerId();
        Optional<Customer> customerOptional = customerService.getCustomer(customerId);
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            model.addAttribute("customer",customer);
        }
        List<CustomerCar> customerCars = customerCarService.listByCustomerId(shopId, customerId);
        model.addAttribute("customerCars",customerCars);

        return "yqx/page/account/coupon/grant";
    }

    @RequestMapping("grant/insert")
    @ResponseBody
    public Result grant(@RequestBody AccountCouponVo accountCouponVo){
        if(accountCouponVo.getCouponSuiteId() == null && CollectionUtils.isEmpty(accountCouponVo.getCouponVos())){
            return Result.wrapErrorResult("","优惠券不能为空");
        }
        if(accountCouponVo.getAccountId() == null){
            return Result.wrapErrorResult("","账户ID不能为空");
        }
        if(accountCouponVo.getCouponSuiteId() != null && accountCouponVo.getPayAmount() == null){
            return Result.wrapErrorResult("","金额不能为空");
        }
        UserInfo userInfo = UserUtils.getUserInfo(request);
        accountCouponVo.setShopId(userInfo.getShopId());
        Long userId = userInfo.getUserId();
        accountCouponVo.setCreator(userId);
        AccountTradeFlow flow = accountCouponService.grant(accountCouponVo);
        return Result.wrapSuccessfulResult(flow);
    }

    @RequestMapping("grantPrint")
    public String printGrant(Model model, @RequestParam("id") Long flowId) {
        Long shopId = UserUtils.getShopIdForSession(request);
        AccountTradeFlow tradeFlow = accountTradeFlowService.findById(shopId, flowId);
        if (tradeFlow == null) {
            return "common/error";
        }
        Long accountId = tradeFlow.getAccountId();
        AccountInfo accountInfo = accountInfoService.getAccountInfoAllById(shopId, accountId);
        Shop shop = shopService.selectById(shopId);
        List<CustomerCar> cars = accountInfo.getCarLicences();
        StringBuilder licenses = new StringBuilder();
        if (cars != null && cars.size()>0) {
            for (CustomerCar item: cars) {
                licenses.append(item.getLicense()).append(";");
            }
        }
        if (licenses.length() >= 1) {
            licenses.substring(0,licenses.length()-1);
        }

        AccountSuite suite =  accountSuiteService.findByFlowId(shopId, flowId);
        String suiteName = "自定义";
        if (suite != null) {
            suiteName = suite.getCouponSuiteName();
        }
        List<AccountCoupon> couponList = accountCouponService.findByFlowId(shopId,flowId);
        Map<Long, AccountCoupon> couponMap = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(couponList)) {
            for (AccountCoupon item : couponList) {
                Long couponInfoId = item.getCouponInfoId();
                couponMap.put(couponInfoId, item);
            }
        }
        Map<Long,Integer> countMap = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(couponList)) {
            for (AccountCoupon item : couponList) {
                Long couponInfoId = item.getCouponInfoId();
                if (!countMap.containsKey(couponInfoId)) {
                    countMap.put(couponInfoId,1);
                } else {
                    int i = countMap.get(couponInfoId) + 1;
                    countMap.put(couponInfoId, i);
                }
            }
        }

        StringBuilder remark = new StringBuilder("内含: ");
        Set<Map.Entry<Long, AccountCoupon>> entrySet = couponMap.entrySet();
        for (Map.Entry<Long, AccountCoupon> item : entrySet) {
            Integer count = countMap.get(item.getKey());
            AccountCoupon accountCoupon = item.getValue();
            remark.append(accountCoupon.getCouponName())
                    .append(count)
                    .append("张")
                    .append(" 过期时间")
                    .append(accountCoupon.getExpireDateStr())
                    .append(";");
        }


        RechargeGrantPrintVo vo = new RechargeGrantPrintVo();
        vo.setFlowSn(tradeFlow.getFlowSn());
        vo.setDateStr(tradeFlow.getGmtCreateStr());
//        vo.setCardNumber(accountInfo.getMemberCard().getCardNumber());
        vo.setCustomerName(tradeFlow.getCustomerName());
        vo.setCarLicenses(licenses.toString());
        vo.setPhone(tradeFlow.getMobile());
        vo.setPayAmount(tradeFlow.getPayAmount());
        vo.setShopName(shop.getName());
        vo.setShopAddress(shop.getAddress());
        vo.setShopTele(shop.getTel());
        //        vo.setTips();
        vo.setName(suiteName);
        //        vo.setNumber();
        vo.setAmount(tradeFlow.getAmount());
        vo.setPayableAmount(tradeFlow.getAmount());
        vo.setRemark(remark.toString());
        model.addAttribute("printObj",vo);
        return "yqx/page/account/coupon/grant-print";
    }

    @HttpRequestLog
    @RequestMapping("reverse_recharge")
    @ResponseBody
    public Result reverseRecharge(@RequestParam("id") Long flowId) {
        //1.检查条件
            //1.流水是否已撤销
        if(flowId == null){
            return Result.wrapErrorResult("","流水查询异常");
        }
        Long shopId = UserUtils.getShopIdForSession(request);
        AccountTradeFlow accountTradeFlow = accountTradeFlowService.findById(shopId, flowId);
        if(accountTradeFlow == null){
            return Result.wrapErrorResult("","流水查询异常");
        }
        if(accountTradeFlow.getIsReversed() == 1){
            return Result.wrapErrorResult("","已撤销");
        }
            //2.优惠券是否已使用
        Map param = new HashMap();
        param.put("shopId",shopId);
        param.put("flowId",accountTradeFlow.getId());
        List<Long> ids = new ArrayList<>();
        List<AccountCoupon> accountCoupons = accountCouponService.select(param);
        if(CollectionUtils.isEmpty(accountCoupons)){
            return Result.wrapErrorResult("","查询不到关联优惠券");
        }
        for (AccountCoupon accountCoupon : accountCoupons) {
            if(accountCoupon.getUsedStatus() == 1){
                return Result.wrapErrorResult("","优惠券已使用");
            }
            ids.add(accountCoupon.getId());
        }
        //2.执行撤销
        accountCouponService.deleteByIds(ids);
        AccountSuite suite = accountSuiteService.findByFlowId(shopId, flowId);
        if (suite != null) {
            couponSuiteService.updateUsedCount(shopId,suite.getCouponSuiteId());
        }
        //3.写流水
        CouponRechargeReverseBo couponRechargeReverseBo = new CouponRechargeReverseBo();
        UserInfo userInfo = UserUtils.getUserInfo(request);
        couponRechargeReverseBo.setShopId(userInfo.getShopId());
        couponRechargeReverseBo.setUserId(userInfo.getUserId());
        couponRechargeReverseBo.setUserName(userInfo.getName());
        couponRechargeReverseBo.setFlow(accountTradeFlow);
        accountFlowService.recordReverseFlowForCouponRecharge(couponRechargeReverseBo);
        return Result.wrapSuccessfulResult("撤销成功");
    }
}
