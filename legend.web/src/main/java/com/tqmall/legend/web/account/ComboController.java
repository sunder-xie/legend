package com.tqmall.legend.web.account;

import com.tqmall.common.UserInfo;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.error.LegendAccountErrorCode;
import com.tqmall.legend.annotation.HttpRequestLog;
import com.tqmall.legend.biz.account.AccountComboFlowDetailService;
import com.tqmall.legend.biz.account.AccountComboService;
import com.tqmall.legend.biz.account.AccountFlowService;
import com.tqmall.legend.biz.account.AccountInfoService;
import com.tqmall.legend.biz.account.AccountTradeFlowService;
import com.tqmall.legend.biz.account.ComboInfoService;
import com.tqmall.legend.biz.account.bo.ComboRechargeReverseBo;
import com.tqmall.legend.biz.account.bo.RechargeComboBo;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.common.exception.BizException;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.entity.account.AccountComboFlowDetail;
import com.tqmall.legend.entity.account.AccountInfo;
import com.tqmall.legend.entity.account.AccountTradeFlow;
import com.tqmall.legend.entity.account.vo.RechargeComboFlowBo;
import com.tqmall.legend.entity.account.AccountCombo;
import com.tqmall.legend.entity.account.ComboInfo;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.web.account.vo.RechargeGrantPrintVo;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.zenith.errorcode.support.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by majian on 16/6/2.
 */
@Controller
@Slf4j
@RequestMapping("account/combo")
public class  ComboController extends BaseController {

    @Autowired
    private ComboInfoService comboInfoService;
    @Autowired
    private AccountComboService accountComboService;
    @Autowired
    private AccountComboFlowDetailService accountComboFlowDetailService;
    @Autowired
    private AccountTradeFlowService accountTradeFlowService;
    @Autowired
    private AccountInfoService accountInfoService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private CustomerCarService customerCarService;
    @Autowired
    AccountFlowService accountFlowService;

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String createPage(Model model, @RequestParam(value = "id", required = false) Long id) {
        if (id != null && !id.equals(0l)) {
            Long shopId = UserUtils.getShopIdForSession(request);
            ComboInfo comboInfo = comboInfoService.getComboInfo(id, shopId);
            model.addAttribute("comboInfo",comboInfo);
        }
        model.addAttribute("moduleUrl","customer");
        model.addAttribute("subModule","account-setting");
        return "yqx/page/account/combo/create";
    }

    @RequestMapping(value="grant", method = RequestMethod.GET)
    public String grantPage(Model model,@RequestParam(value="license",required = false)String license) {
        Long shopId = UserUtils.getShopIdForSession(request);
        model.addAttribute("moduleUrl","customer");
        model.addAttribute("subModule", "combo-grant");
        if(StringUtil.isNotStringEmpty(license)){
            CustomerCar car =  customerCarService.selectByLicenseAndShopId(license,shopId);
            model.addAttribute("license", license);
            model.addAttribute("carId",car.getId());
        }
        return "yqx/page/account/combo/grant";
    }

    @RequestMapping("grantPrint")
    public String printGrant(Model model, @RequestParam("id") Long flowId) {
        Long shopId = UserUtils.getShopIdForSession(request);

        AccountTradeFlow tradeFlow = accountTradeFlowService.findById(shopId, flowId);
        Long accountId = tradeFlow.getAccountId();
        AccountInfo accountInfo = accountInfoService.getAccountInfoAllById(shopId, accountId);
        Shop shop = shopService.selectById(shopId);
        List<CustomerCar> cars = accountInfo.getCarLicences();
        String licensesStr = "";
        StringBuilder licenses = new StringBuilder();
        if (cars != null) {
            for (CustomerCar item: cars) {
                licenses.append(item.getLicense()).append(";");
            }
        }
        if (licenses.length() >= 1) {
            licensesStr = licenses.substring(0,licenses.length()-1);
        }
        List<AccountComboFlowDetail> details = accountComboFlowDetailService.findByFlowId(shopId, tradeFlow.getId());
        Long comboId = 0l;
        if (!CollectionUtils.isEmpty(details)) {
            comboId = details.get(0).getComboId();
        }
        String serviceRemarkStr = "";
        StringBuilder serviceRemark = new StringBuilder("服务项目:");
        if (!CollectionUtils.isEmpty(details)) {
            for (AccountComboFlowDetail item : details) {
                serviceRemark.append(item.getServiceName())
                        .append(item.getChangeCount())
                        .append("次,");
            }
            serviceRemarkStr = serviceRemark.substring(0,serviceRemark.length()-2);
        }
        AccountCombo combo = accountComboService.getComboWithService(shopId, comboId);
        String expireDateStr = combo.getExpireDateStr();
        Long comboInfoId = combo.getComboInfoId();
        ComboInfo comboInfo = comboInfoService.findById(shopId, comboInfoId);
        Long effectivePeriodByMonth = comboInfo.getEffectivePeriodDays();
        StringBuilder remark = new StringBuilder();
        remark.append(serviceRemarkStr)
                .append("; ")
                .append("有效期")
                .append(effectivePeriodByMonth)
                .append("天")
                .append("; ")
                .append("过期时间:")
                .append(expireDateStr);
        RechargeGrantPrintVo vo = new RechargeGrantPrintVo();
        vo.setFlowSn(tradeFlow.getFlowSn());
        vo.setDateStr(tradeFlow.getGmtCreateStr());
//        vo.setCardNumber(accountInfo.getMemberCard().getCardNumber());
        vo.setCustomerName(tradeFlow.getCustomerName());
        vo.setCarLicenses(licensesStr);
        vo.setPhone(tradeFlow.getMobile());
        vo.setPayAmount(tradeFlow.getPayAmount());
        vo.setShopName(shop.getName());
        vo.setShopAddress(shop.getAddress());
        vo.setShopTele(shop.getTel());
        //        vo.setTips();
        vo.setName(combo.getComboName());
        //        vo.setNumber();
        vo.setAmount(tradeFlow.getAmount());
        vo.setPayableAmount(tradeFlow.getAmount());
        vo.setRemark(remark.toString());
        model.addAttribute("printObj",vo);
        return "yqx/page/account/combo/grant-print";
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public Result addComboInfo(@RequestBody ComboInfo comboInfo) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        if (comboInfo == null) {
            return LegendAccountErrorCode.PARAMS_ERROR.newResult("数据错误,comboInfo为空");
        }
        try {
            comboInfoService.addComboInfo(comboInfo,userInfo.getShopId(),userInfo.getUserId());
        } catch (BizException e) {
            log.error("新增计次卡类型失败,shopId={}, 错误信息:", userInfo.getShopId(), e);
            return LegendAccountErrorCode.COMMON_ERROR.newResult(e.getMessage());
        }
        return Result.wrapSuccessfulResult("新增成功");

    }

    @RequestMapping("/update")
    @ResponseBody
    public Result updateComboInfo(@RequestBody ComboInfo comboInfo) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        try {
            comboInfoService.updateComboInfo(comboInfo, userInfo.getShopId(),userInfo.getUserId() );
        } catch (BizException e) {
            log.error("修改计次卡失败, shopId={},comboInfo={}, e:{}", userInfo.getShopId(),comboInfo.toString(),e);
            return LegendAccountErrorCode.COMMON_ERROR.newResult(e.getMessage());
        }
        return Result.wrapSuccessfulResult("修改成功");

    }

    @RequestMapping("/delete")
    @ResponseBody
    public Result deleteComboInfo(@RequestParam("id") Long id) {
        if (id == null) {
            return LegendAccountErrorCode.PARAMS_ERROR.newResult( "id不能为null");
        }
        if (comboInfoService.isComboINfoGranted(id)) {
            return LegendAccountErrorCode.PARAMS_ERROR.newResult( "该计次卡已发放,不能删除");
        }
        UserInfo userInfo = UserUtils.getUserInfo(request);
        try {
            comboInfoService.deleteComboInfo(id,userInfo);
        } catch (BizException e) {
            return LegendAccountErrorCode.COMMON_ERROR.newResult(e.getMessage());
        }
        return Result.wrapSuccessfulResult("删除成功");
    }

    @RequestMapping("/enable")
    @ResponseBody
    public Result enableComboInfo(@RequestParam("id") Long id) {
        UserInfo userInfo = UserUtils.getUserInfo(request);

        if (id == null) {
            return LegendAccountErrorCode.PARAMS_ERROR.newResult("id 不能为空");
        }

        try {
            comboInfoService.enableComboInfo(id,userInfo);
        } catch (BizException e) {
            return LegendAccountErrorCode.COMMON_ERROR.newResult(e.getMessage());
        }
        return Result.wrapSuccessfulResult("启用成功");

    }

    @RequestMapping("/disable")
    @ResponseBody
    public Result disableComboInfo(@RequestParam("id") Long id) {
        UserInfo userInfo = UserUtils.getUserInfo(request);

        if (id == null) {
            return LegendAccountErrorCode.PARAMS_ERROR.newResult("id 不能为空");
        }

        try {
            comboInfoService.disableComboInfo(id,userInfo);
        } catch (BizException e) {
            return LegendAccountErrorCode.COMMON_ERROR.newResult(e.getMessage());
        }
        return Result.wrapSuccessfulResult("停用成功");
    }

    @HttpRequestLog
    @RequestMapping("comboInfo/list")
    @ResponseBody
    public Result getComboInfoList() {
        Long shopId = UserUtils.getShopIdForSession(request);

        List<ComboInfo> comboInfoList = comboInfoService.listComboInfo(shopId);
        if (comboInfoList != null) {
            return Result.wrapSuccessfulResult(comboInfoList);
        } else {
            return LegendAccountErrorCode.COMMON_ERROR.newResult("无计次卡类型");
        }
    }

    @RequestMapping("comboInfo/listEnable")
    @ResponseBody
    public Result getEnabledComboInfoList() {
        Long shopId = UserUtils.getShopIdForSession(request);

        List<ComboInfo> comboInfoList = comboInfoService.listEnabledComboInfo(shopId);
        if (comboInfoList != null) {
            return Result.wrapSuccessfulResult(comboInfoList);
        } else {
            return LegendAccountErrorCode.COMMON_ERROR.newResult("无可用计次卡类型");

        }
    }

    @RequestMapping("rechargeflow/list")
    @ResponseBody
    public Result getShopRechargeFlowList(@PageableDefault(page = 1, value = 10, sort = "id",
            direction = Sort.Direction.DESC) Pageable pageable) {
        Long shopId = UserUtils.getShopIdForSession(request);
        Page<RechargeComboFlowBo> accountComboFlowDetails = accountComboFlowDetailService.getPageRechargeFlow(pageable, shopId);
        if (accountComboFlowDetails != null) {
            return Result.wrapSuccessfulResult(accountComboFlowDetails);
        } else {
            return LegendAccountErrorCode.COMMON_ERROR.newResult("无计次卡发放记录");
        }
    }


    @RequestMapping("recharge")
    @ResponseBody
    public Result recharge(@RequestBody RechargeComboBo comboRechargeBo) {
        UserInfo userInfo = UserUtils.getUserInfo(request);

        if (comboRechargeBo.getAccountId() == null) {
            return LegendAccountErrorCode.PARAMS_ERROR.newResult("请输入账户信息");
        }
        BigDecimal payAmount = comboRechargeBo.getPayAmount();
        if (payAmount == null || payAmount.compareTo(BigDecimal.ZERO) < 0) {
            return LegendAccountErrorCode.PARAMS_ERROR.newResult("实收金额不能小于0");
        }
        if (comboRechargeBo.getComboInfoId() == null) {
            return LegendAccountErrorCode.PARAMS_ERROR.newResult("请选择计次卡类型");
        }
        if (comboRechargeBo.getPaymentId() == null) {
            return LegendAccountErrorCode.PARAMS_ERROR.newResult("请选择付款方式");
        }
        if (comboRechargeBo.getRecieverId() == null) {
            return LegendAccountErrorCode.PARAMS_ERROR.newResult("请选择服务顾问");
        }

        try {
            AccountTradeFlow flow = accountComboService.rechargeCombo(comboRechargeBo, userInfo.getShopId(), userInfo.getUserId());
            return Result.wrapSuccessfulResult(flow);
        } catch (BizException e) {
            return LegendAccountErrorCode.COMMON_ERROR.newResult(e.getMessage());
        }
    }

    @RequestMapping("combo/list")
    @ResponseBody
    public Result listComboByAccount(@RequestParam("accountId") Long accountId) {
        if (accountId == null) {
            return LegendAccountErrorCode.PARAMS_ERROR.newResult("请输入账户信息");
        }
        Long shopId = UserUtils.getShopIdForSession(request);
        List<AccountCombo> comboList = accountComboService.listCombo(shopId,accountId);
        if (comboList != null) {
            return Result.wrapSuccessfulResult(comboList);
        } else {
            log.info("该客户无计次卡");
            return LegendAccountErrorCode.COMMON_ERROR.newResult("该客户无计次卡");
        }
    }

    @HttpRequestLog
    @RequestMapping("reverse_recharge")
    @ResponseBody
    public Result reverseRecharge(@RequestParam("id") Long flowId) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        Long userId = userInfo.getUserId();
        String userName = userInfo.getName();
        //检验撤销条件
        AccountTradeFlow flow = accountTradeFlowService.findById(shopId, flowId);
        if (flow == null) {
            return LegendAccountErrorCode.COMMON_ERROR.newResult("流水查询异常");
        }
        if (flow.getIsReversed() == 1) {
            return LegendAccountErrorCode.COMMON_ERROR.newResult("流水已撤销");
        }
        List<AccountComboFlowDetail> detailList = accountComboFlowDetailService.findByFlowId(shopId, flowId);
        if (CollectionUtils.isEmpty(detailList)) {
            return LegendAccountErrorCode.COMMON_ERROR.newResult("无充值内容");
        }
        Long comboId = detailList.get(0).getComboId();
        Boolean comboIsUsed = accountComboService.comboIsUsed(comboId, shopId);
        if (comboIsUsed) {
            return LegendAccountErrorCode.COMMON_ERROR.newResult("该计次卡已使用");
        }
        //撤销主流程
        accountComboService.reverseRecharge(shopId, comboId);
        //写流水
        ComboRechargeReverseBo comboRechargeReverseBo = new ComboRechargeReverseBo();
        comboRechargeReverseBo.setShopId(shopId);
        comboRechargeReverseBo.setUserName(userName);
        comboRechargeReverseBo.setUserId(userId);
        comboRechargeReverseBo.setFlow(flow);
        accountFlowService.recordReverseFlowForComboRecharge(comboRechargeReverseBo);
        return Result.wrapSuccessfulResult("撤销成功");

    }


}
