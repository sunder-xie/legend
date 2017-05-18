package com.tqmall.legend.web.insurance.settle;

import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.facade.insurance.AnxinInsuranceDicFacade;
import com.tqmall.legend.facade.insurance.AnxinManaSettleFacade;
import com.tqmall.legend.facade.insurance.InsuranceFormFacade;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.insurance.vo.SettleCheckDetailShowVO;
import com.tqmall.mana.client.beans.settle.SettleShopDTO;
import com.tqmall.mana.client.beans.settle.SettleShopRuleIntroductionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by zxg on 17/1/13.
 * 14:42
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@Slf4j
@Controller
@RequestMapping("insurance/settle/view")
public class InsuranceSettleViewController extends BaseController {

    @Autowired
    private AnxinManaSettleFacade anxinManaSettleFacade;
    @Autowired
    private InsuranceFormFacade insuranceFormFacade;

    @Autowired
    private AnxinInsuranceDicFacade insuranceDicFacade;

    // 对账首页
    @RequestMapping("index")
    public String indexPage(Model model) {
//        Long shopId = UserUtils.getShopIdForSession(request);
        String shopIdStr = UserUtils.getUserGlobalIdForSession(request);
        Integer shopId = Integer.valueOf(shopIdStr);

        SettleShopDTO shopDetail = anxinManaSettleFacade.getSettleShopDetailByShopId(shopId);
        model.addAttribute("shopDetail", shopDetail);
        //  奖励金的业务注释掉
        // 支出--从账户账务获得 支出和返回
//        BigDecimal expendBonusAmount = insuranceFormFacade.getBonusRewardTotalFee(shopId, SettleBonusTypeVO.EXTEND_TYPE);
//        // 返回
//        BigDecimal returnBonusAmount = insuranceFormFacade.getBonusRewardTotalFee(shopId, SettleBonusTypeVO.RETURN_TYPE);
//        if(expendBonusAmount == null || returnBonusAmount == null){
//            model.addAttribute("expendBonusAmount", expendBonusAmount == null ? -1 : expendBonusAmount);
//            model.addAttribute("returnBonusAmount", returnBonusAmount == null  ? -1 : returnBonusAmount);
//            model.addAttribute("leftBonusAmount", -1);
//        }else {
//            // 剩余可用
//            BigDecimal leftBonusAmount = BigDecimal.ZERO;
//
//            BigDecimal settledBonusAmount = shopDetail.getSettledBonusAmount();
//            if (settledBonusAmount != null && settledBonusAmount.compareTo(BigDecimal.ZERO) != 0) {
//
//                // 剩余可用 = 已结算-支出+返还
//                leftBonusAmount = settledBonusAmount.subtract(expendBonusAmount).add(returnBonusAmount);
//            }
//
//            model.addAttribute("expendBonusAmount",  expendBonusAmount);
//            model.addAttribute("returnBonusAmount",  returnBonusAmount);
//
//            // 若小于0.则不显示
//            if(leftBonusAmount.compareTo(BigDecimal.ZERO) < 0){
//                log.info("leftBonusAmount < 0.leftBonusAmount:{}",leftBonusAmount);
//                model.addAttribute("leftBonusAmount", -1);
//            }else {
//                model.addAttribute("leftBonusAmount", leftBonusAmount);
//            }
//        }
        return "yqx/page/ax_insurance/settle/new/settle-index";
    }

    // 对账-现金页面
    @RequestMapping("cash")
    public String cashPage(Model model) {

        model.addAttribute("cooperationModeList", insuranceDicFacade.getCooperationModeList());
        model.addAttribute("settleProjectList", insuranceDicFacade.getSettleProjectList());
        return "yqx/page/ax_insurance/settle/new/settle-cash";
    }

    // 对账- 服务包页面
    @RequestMapping("package")
    public String packagePage(Model model) {
        model.addAttribute("cooperationModeList", insuranceDicFacade.getCooperationModeList());
        model.addAttribute("settleProjectList", insuranceDicFacade.getSettleProjectList());
        model.addAttribute("settle_project_id", SettleCheckDetailShowVO.SETTLE_PACKAGE_PROJECT_ID);

        return "yqx/page/ax_insurance/settle/new/settle-package";
    }

    // 对账 - 奖励金页面
    @RequestMapping("bonus")
    public String bonusPage(Model model) {
//        model.addAttribute("cooperationModeList", insuranceDicFacade.getCooperationModeList());
//        model.addAttribute("settleProjectList", insuranceDicFacade.getSettleProjectList());
//
//        model.addAttribute("extendReasonList",insuranceFormFacade.getBonusReasonList(SettleBonusTypeVO.EXTEND_TYPE));
//        model.addAttribute("returnReasonList",insuranceFormFacade.getBonusReasonList(SettleBonusTypeVO.RETURN_TYPE));
//        return "yqx/page/ax_insurance/settle/new/settle-bonus";
        return null;
    }

    // 对账 - 规则页面
    @RequestMapping("rule")
    public String rulePage(Model model) {
        Integer CASH_RULE_TYPE = 1;
        Integer BONUS_RULE_TYPE = 2;
        Integer PACKAGE_RULE_TYPE = 3;

        List<SettleShopRuleIntroductionDTO> introductionList = anxinManaSettleFacade.getIntroductionList();
        for (SettleShopRuleIntroductionDTO settleShopRuleIntroductionDTO : introductionList) {
            if(settleShopRuleIntroductionDTO == null){
                continue;
            }
            Integer ruleType = settleShopRuleIntroductionDTO.getRuleType();
            if(ruleType == null){
                continue;
            }

            if(ruleType.equals(CASH_RULE_TYPE)){
                model.addAttribute("cashRule", settleShopRuleIntroductionDTO.getRuleIntroduction());
                continue;
            }
            if(ruleType.equals(BONUS_RULE_TYPE)){
                model.addAttribute("bonusRule", settleShopRuleIntroductionDTO.getRuleIntroduction());
                continue;
            }
            if(ruleType.equals(PACKAGE_RULE_TYPE)){
                model.addAttribute("packageRule", settleShopRuleIntroductionDTO.getRuleIntroduction());
                continue;
            }
        }

        return "yqx/page/ax_insurance/settle/new/settle-rule";
    }

}
