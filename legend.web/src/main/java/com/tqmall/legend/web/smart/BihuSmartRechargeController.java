package com.tqmall.legend.web.smart;

import com.tqmall.common.util.WebUtils;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.finance.model.param.pay.OfferListFormParam;
import com.tqmall.insurance.domain.result.smart.SmartRechargeRecordDTO;
import com.tqmall.insurance.domain.result.smart.SmartRechargeRuleDTO;
import com.tqmall.insurance.domain.result.smart.SmartShopOrderDTO;
import com.tqmall.legend.facade.smart.BihuSmartRechargeFacade;
import com.tqmall.legend.facade.smart.BihuSmartViewFacade;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by zwb on 16/12/19.
 * 壁虎智能投保 充值模块
 */

@Slf4j
@Controller
@RequestMapping("smart/bihu/recharge")
public class BihuSmartRechargeController extends BaseController {
//    //支付宝回调
//    private final static String ALIPAY_URL = "/smart/bihu/recharge/bihu-verify/alipay";
//    @Autowired
//    private BihuSmartRechargeFacade bihuSmartRechargeFacade;
    @Autowired
    private BihuSmartViewFacade bihuSmartViewFacade;
//
//    /**
//     * 跳转到充值首页
//     *
//     * @param
//     * @param
//     * @return
//     */
//    @RequestMapping(value = "index", method = RequestMethod.GET)
//    public String rechargeIndex(Model model) {
//        Integer minimumRechargeFee = bihuSmartRechargeFacade.getMinimumRechargeFee();
//        model.addAttribute("minimumFee", minimumRechargeFee);
//        List<SmartRechargeRuleDTO> rechargeRule = bihuSmartRechargeFacade.getRechargeRule(SmartRechargeRuleDTO.NORM_TYPE);
//        if(rechargeRule != null && rechargeRule.size() == 1){
//            model.addAttribute("rechargeRuleTimes", rechargeRule.get(0).getEffectiveNum());
//        }
//        model.addAttribute("minimumFee", minimumRechargeFee);
//        return "yqx/page/smart/recharge-scheme";
//    }
//
//
//    /**
//     * 获取门店充值规则
//     */
//    @RequestMapping(value = "getRechargeRuleList", method = RequestMethod.GET)
//    @ResponseBody
//    public Result getRechargeRule() {
//        List<SmartRechargeRuleDTO> speedRule = bihuSmartRechargeFacade.getRechargeRule(SmartRechargeRuleDTO.QUICK_TYPE);
//        List<SmartRechargeRuleDTO> favorableRule = bihuSmartRechargeFacade.getRechargeRule(SmartRechargeRuleDTO.FAVORABLE_TYPE);
//        speedRule.addAll(favorableRule);
//        Collections.sort(favorableRule, new Comparator<SmartRechargeRuleDTO>() {
//            @Override
//            public int compare(SmartRechargeRuleDTO ruleOne, SmartRechargeRuleDTO ruleTwo) {
//                //首先比较规则类型1.资费规则 2.快速充值 3.优惠充值
//                int flag = ruleOne.getRechargeType().compareTo(ruleTwo.getRechargeType());
//                if(flag==0){
//                    //其次暗中充值金额排序
//                    return ruleTwo.getRechargeFeeNumber().compareTo(ruleOne.getRechargeFeeNumber());
//                }else{
//                    return flag;
//                }
//            }
//        });
//        return Result.wrapSuccessfulResult(speedRule);
//    }
//
//    /**
//     * 获取当前门店充值信息
//     */
    @RequestMapping(value = "getShopInfo", method = RequestMethod.GET)
    @ResponseBody
    public Result getShopInfo() {
        String uid = UserUtils.getUserGlobalIdForSession(request);
        SmartShopOrderDTO shopInfo = bihuSmartViewFacade.getShopInfo(Integer.valueOf(uid));
        return Result.wrapSuccessfulResult(shopInfo);
    }


//
//
//    /**
//     * 发起快速充值
//     */
//    @RequestMapping(value = "speedRecharge", method = RequestMethod.POST)
//    @ResponseBody
//    public Result speedRecharge(@RequestParam("rechargeRuleId") Integer rechargeRuleId) {
//        String uid = UserUtils.getUserGlobalIdForSession(request);
//        com.tqmall.core.common.entity.Result<SmartRechargeRecordDTO> smartRechargeResult = bihuSmartRechargeFacade.speedRecharge(Integer.valueOf(uid), rechargeRuleId);
//        return transformResult(smartRechargeResult);
//    }
//
//    /**
//     * 发起自定义充值
//     */
//    @RequestMapping(value = "feeRecharge", method = RequestMethod.POST)
//    @ResponseBody
//    public Result feeRecharge(@RequestParam("rechargeFee") BigDecimal rechargeFee) {
//        String uid = UserUtils.getUserGlobalIdForSession(request);
//        com.tqmall.core.common.entity.Result<SmartRechargeRecordDTO> smartRechargeResult = bihuSmartRechargeFacade.feeRecharge(Integer.valueOf(uid), rechargeFee);
//        return transformResult(smartRechargeResult);
//    }
//
//
//
//    /**
//     * 支付宝支付
//     */
//    @RequestMapping(value = "alipay", method = RequestMethod.GET)
//    public String aliPay(Model model,
//                                @RequestParam(value = "payFee") BigDecimal payFee,
//                                @RequestParam(value = "rechargeNumber") String rechargeNumber){
//        String uid = UserUtils.getUserGlobalIdForSession(request);
//        OfferListFormParam param = new OfferListFormParam();
//        param.setBody("智能查询充值");
//        param.setTotalFee(payFee);
//        param.setUid(Integer.parseInt(uid));
//        param.setSn(rechargeNumber);
//        param.setWebReturnUrl(WebUtils.getHostUrl(request) + ALIPAY_URL);
//        String htmlInfo = bihuSmartRechargeFacade.alipay(param);
//        if (htmlInfo == null || htmlInfo == "") {
//            return "common/error";
//        }
//        model.addAttribute("alipayForm", htmlInfo);
//        return "yqx/page/smart/recharge-alipay";
//    }
//
//    /**
//     * 支付宝回调
//     *
//     * @return
//     */
//    @RequestMapping(value = "bihu-verify/alipay", method = RequestMethod.GET)
//    public String aliPayBack(Model model) {
//        Map<String, String[]> paramMap = request.getParameterMap();
//        String outTradeNo = paramMap.get("out_trade_no")[0];
//        String[] outTradeArray = outTradeNo.split("-");
//        String rechargeNumber = outTradeArray[0];
//        if ("TRADE_SUCCESS".equals(paramMap.get("trade_status")[0])
//                || "TRADE_FINISHED".equals(paramMap.get("trade_status")[0])) {
//            try {
//                bihuSmartRechargeFacade.updateOrder(rechargeNumber, true);
//                bihuSmartRechargeFacade.verifyAliPay(paramMap);
//                return afterPayDo(model, rechargeNumber);
//            } catch (Exception e) {
//                log.error("[DUBBO]查询订单状态失败,错误原因:", e);
//            }
//        } else {
//            bihuSmartRechargeFacade.updateOrder(rechargeNumber, false);
//        }
//        return "yqx/page/smart/recharge-fail";
//    }
//
//    /**
//     * 根据rechargeNumber跳转到不同的展示页面
//     *
//     * @param
//     * @param
//     * @return
//     */
//    @RequestMapping(value = "judge-go", method = RequestMethod.GET)
//    public String judgeGo(Model model, @RequestParam(value = "rechargeNumber") String rechargeNumber) {
//        return afterPayDo(model,rechargeNumber);
//    }
//
//    private String afterPayDo(Model model, String rechargeNumber) {
//        SmartRechargeRecordDTO recordByRechargeNumber = bihuSmartRechargeFacade.getRecordByRechargeNumber(rechargeNumber);
//        if (recordByRechargeNumber == null) {
//            return "common/error";
//        }
//        Integer billStatus = recordByRechargeNumber.getBillStatus();
////        Integer billStatus = 1;
//
//        model.addAttribute("recordResult", recordByRechargeNumber);
//
//        if (billStatus.compareTo(SmartRechargeRecordDTO.NOT_PAY_BILL_STATUS) == 0 ) {
//            return "yqx/page/smart/recharge-nopay";
//        }
//
//        if (billStatus.compareTo(SmartRechargeRecordDTO.HAVE_PAY_BILL_STATUS) == 0 ) {
//            return "yqx/page/smart/recharge-wait";
//        }
//
//        if( billStatus.compareTo(SmartRechargeRecordDTO.PAY_FAIL_BILL_STATUS) == 0){
//            return "yqx/page/smart/recharge-fail";
//        }
//        String uid = UserUtils.getUserGlobalIdForSession(request);
//        SmartShopOrderDTO shopInfo = bihuSmartViewFacade.getShopInfo(Integer.valueOf(uid));
//        if (shopInfo == null) {
//            return "common/error";
//        }
//        model.addAttribute("shopAllNum", shopInfo.getRemainNum());
//        return "yqx/page/smart/recharge-success";
//    }
//
//    /**
//     * 在等待页面多次回调是否成功
//     *
//     * @param
//     * @param
//     * @return
//     */
//    @RequestMapping(value = "confirmPay", method = RequestMethod.POST)
//    @ResponseBody
//    public Result confirmPay(@RequestParam("rechargeNumber") String rechargeNumber) {
//        SmartRechargeRecordDTO recordByRechargeNumber = bihuSmartRechargeFacade.getRecordByRechargeNumber(rechargeNumber);
////        recordByRechargeNumber.setBillStatus(3);
//        return Result.wrapSuccessfulResult(recordByRechargeNumber);
//    }
//
//    //result转换
//    private Result transformResult(com.tqmall.core.common.entity.Result resultTemp) {
//        Result needResult = new Result();
//        needResult.setCode(resultTemp.getCode());
//        needResult.setData(resultTemp.getData());
//        needResult.setErrorMsg(resultTemp.getMessage());
//        needResult.setSuccess(resultTemp.isSuccess());
//        return needResult;
//    }
}
