package com.tqmall.legend.web.insurance;

import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.DateUtil;
import com.tqmall.common.util.WebUtils;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.error.LegendInsuranceErrorCode;
import com.tqmall.finance.model.param.pay.OfferListFormParam;
import com.tqmall.finance.model.param.pay.OfferListLianFormParam;
import com.tqmall.finance.model.result.user.BankCardDTO;
import com.tqmall.finance.model.result.user.UserBankCardDTO;
import com.tqmall.insurance.constants.CredentialTypeEnum;
import com.tqmall.insurance.constants.RpcServicePackageConstants;
import com.tqmall.insurance.domain.param.insurance.InsuranceCalculateParam;
import com.tqmall.insurance.domain.param.insurance.InsuranceFormParam;
import com.tqmall.insurance.domain.param.insurance.InsuranceItemParam;
import com.tqmall.insurance.domain.result.InsuranceServicePackageFeeDTO;
import com.tqmall.insurance.domain.result.InsuranceVirtualBasicDTO;
import com.tqmall.insurance.domain.result.InsuranceVirtualFormDTO;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.facade.insurance.AnxinInsuranceVirtualFlowFacade;
import com.tqmall.legend.facade.insurance.InsuranceFormFacade;
import com.tqmall.legend.facade.insurance.bo.PayFinanceBo;
import com.tqmall.legend.facade.insurance.vo.InsuranceCategoryVo;
import com.tqmall.legend.facade.onlinepay.LianlianPayFacade;
import com.tqmall.legend.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 安心保险虚拟投保流程
 * Created by sven on 16/9/14.
 */
@Slf4j
@Controller
@RequestMapping("insurance/anxin/virtual/flow")
public class AnxinInsuranceVirtualFlowController extends BaseController {
    //支付保回调
    private final static String ALIPAY_URL = "/insurance/anxin/virtual/flow/virtual-verify/alipay";
    //连连支付回调
    private final static String UNIONPAY_URL = "/insurance/anxin/virtual/flow/virtual-verify/unionpay?mode=3";
    @Resource
    private AnxinInsuranceVirtualFlowFacade anxinInsuranceVirtualFlowFacade;
    @Resource
    private InsuranceFormFacade insuranceFormFacade;
    @Resource
    private LianlianPayFacade lianlianPayFacade;

    /**
     * 活动介绍页
     *
     * @return
     */
    @RequestMapping(value = "activity-description", method = RequestMethod.GET)
    public String activityDescription() {
        return "yqx/page/ax_insurance/virtual/activity-description";
    }

    /**
     * 投保信息保存
     *
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public Result virtualInsuranceSave(@RequestBody InsuranceCalculateParam param) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        //校验数据完整性
        checkSave(param);
        Integer ucShopId = getUserGlobalShopId(userInfo.getUserGlobalId());
        String shopName = anxinInsuranceVirtualFlowFacade.selectShopNameById(userInfo.getShopId());
        param.setAgentId(ucShopId);
        param.setInsuranceCompanyId(1);
        param.setCreator(userInfo.getUserId().intValue());
        param.setAgentType(1);
        param.setAgentName(shopName);
        Integer id = anxinInsuranceVirtualFlowFacade.saveVirtualInfo(param);
        return Result.wrapSuccessfulResult(id);
    }

    /**
     * 跳转到确认虚拟投保方案页面
     *
     * @return
     */
    @RequestMapping(value = "virtual-plan", method = RequestMethod.GET)
    public String virtualPlan(@RequestParam("id") Integer id, Model model) {
        String ucId = UserUtils.getUserGlobalIdForSession(request);
        Integer ucShopId = getUserGlobalShopId(ucId);
        InsuranceVirtualBasicDTO virtualBasic = anxinInsuranceVirtualFlowFacade.selectVirtualDetail(id, ucShopId);
        model.addAttribute("virtualBasic", virtualBasic);
        return "yqx/page/ax_insurance/virtual/virtual-plan";
    }

    @RequestMapping(value = "virtual-modify", method = RequestMethod.GET)
    public String virtualModify(@RequestParam("id") Integer id, Model model) {
        String ucId = UserUtils.getUserGlobalIdForSession(request);
        Integer ucShopId = getUserGlobalShopId(ucId);
        InsuranceVirtualBasicDTO virtualBasic = anxinInsuranceVirtualFlowFacade.selectVirtualDetail(id, ucShopId);
        List<InsuranceCategoryVo> categoryVoList = insuranceFormFacade.getInsuranceCategory();
        if (virtualBasic != null) {
            categoryVoList = anxinInsuranceVirtualFlowFacade.getInsuranceCategoryHasChoose(virtualBasic, categoryVoList);

            List<InsuranceVirtualFormDTO> insuranceFormList = virtualBasic.getInsuranceVirtualFormDTOList();
            if (insuranceFormList != null) {
                //商业险form  若商业险只选
                InsuranceVirtualFormDTO insuranceForm = null;
                //交强险form
                InsuranceVirtualFormDTO insuranceFormJQX = null;
                for (int i = 0; i < insuranceFormList.size(); i++) {
                    if (insuranceFormList.get(i).getInsuranceType().compareTo(2) == 0) {
                        insuranceForm = insuranceFormList.get(i);
                    }
                    if (insuranceFormList.get(i).getInsuranceType().compareTo(1) == 0) {
                        insuranceFormJQX = insuranceFormList.get(i);
                    }
                }
                if (insuranceForm != null) {
                    Date packageStartTime = insuranceForm.getPackageStartTime();
                    setTime(insuranceForm, packageStartTime);
                }
                if (insuranceFormJQX != null) {
                    Date packageStartTime = insuranceFormJQX.getPackageStartTime();
                    setTime(insuranceFormJQX, packageStartTime);
                }
            }
        }
        model.addAttribute("listInsuranceCategory", categoryVoList);
        model.addAttribute("insuranceBasic", virtualBasic);
        model.addAttribute("mode", 3);
        return "yqx/page/ax_insurance/create/insureBaseInfo";
    }


    private void setTime(InsuranceVirtualFormDTO insuranceFormJQX, Date packageStartTime) {
        if (packageStartTime != null) {
            Map map = DateUtil.insuranceDate(packageStartTime);
            insuranceFormJQX.setPackageStartTime((Date) map.get("startTime"));
            insuranceFormJQX.setPackageEndTime((Date) map.get("endTime"));
        }
    }

    /**
     * 确认投保方案信息提交
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public Result virtualInsuranceUpdate(@RequestBody InsuranceCalculateParam param) {
        anxinInsuranceVirtualFlowFacade.updateVirtualInfo(param);
        return Result.wrapSuccessfulResult("");
    }

    /**
     * 服务包购买页面
     *
     * @return
     */
    @RequestMapping(value = "virtual-service", method = RequestMethod.GET)
    public String virtualService(@RequestParam("id") Integer id, Model model) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Integer ucShopId = getUserGlobalShopId(userInfo.getUserGlobalId());
        InsuranceVirtualBasicDTO basicDTO = anxinInsuranceVirtualFlowFacade.selectVirtualDetail(id, ucShopId);
        InsuranceVirtualFormDTO insuranceVirtualFormDTO = getFormId(basicDTO);
        if (insuranceVirtualFormDTO == null) {
            log.error("该保单商业险不存在,basicId:{}", id);
            return "common/error";
        }

        model.addAttribute("id", id);
        model.addAttribute("formId", insuranceVirtualFormDTO.getId());
        model.addAttribute("insuredFee", insuranceVirtualFormDTO.getInsuredFee());
        model.addAttribute("mobile", basicDTO.getVehicleOwnerPhone());

        return "yqx/page/ax_insurance/virtual/virtual-service";
    }


    /**
     * 购买服务包页返回投保页数据填充
     *
     * @param id basicId
     * @return
     */
    @RequestMapping(value = "virtual-service-return", method = RequestMethod.GET)
    @ResponseBody
    public Result virtualReturn(@RequestParam("id") Integer id) {
        if (id == null) {
            return Result.wrapErrorResult("", "该虚拟投保单不存在");
        }
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Integer ucShopId = getUserGlobalShopId(userInfo.getUserGlobalId());
        InsuranceVirtualBasicDTO insuranceVirtualBasic = anxinInsuranceVirtualFlowFacade.selectVirtualDetail(id, ucShopId);
        if (insuranceVirtualBasic == null) {
            return Result.wrapErrorResult("", "该虚拟投保单不存在");
        }
        return Result.wrapSuccessfulResult(insuranceVirtualBasic);
    }

    /**
     * 选择服务包计算价格
     *
     * @return
     */
    @RequestMapping(value = "virtual-fee", method = RequestMethod.GET)
    @ResponseBody
    public Result virtualFee(@RequestParam("totalFee") BigDecimal totalFee) {
        InsuranceServicePackageFeeDTO payment = anxinInsuranceVirtualFlowFacade.selectPayment(totalFee);
        return Result.wrapSuccessfulResult(payment);
    }

    /**
     * 选择服务包之后更新信息
     *
     * @return
     */
    @RequestMapping(value = "virtual-update-service", method = RequestMethod.POST)
    @ResponseBody
    public Result virtualUpdateService(@RequestParam("basicId") Integer basicId,
                                       @RequestParam("packageId") Integer packageId,
                                       @RequestParam("formId") Integer formId,
                                       @RequestParam("prePayFee") BigDecimal prePayFee,
                                       @RequestParam(value = "colourType", required = false) Integer colourType) {
        if (basicId == null || packageId == null || formId == null ||
                prePayFee == null || BigDecimal.ZERO.compareTo(prePayFee) > 0) {
            return Result.wrapErrorResult("", "数据有误,去支付失败");
        }
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Integer ucShopId = getUserGlobalShopId(userInfo.getUserGlobalId());
        anxinInsuranceVirtualFlowFacade.updateVirtualBasicService(formId, packageId, prePayFee, ucShopId, colourType);
        InsuranceVirtualBasicDTO insuranceVirtualBasic = anxinInsuranceVirtualFlowFacade.selectVirtualDetail(basicId, ucShopId);
        if (insuranceVirtualBasic == null || StringUtils.isBlank(insuranceVirtualBasic.getInsuranceOrderSn())) {
            return Result.wrapErrorResult("", "数据有误,去支付失败");
        }
        return Result.wrapSuccessfulResult(insuranceVirtualBasic.getInsuranceOrderSn());
    }

    /**
     * 详情去支付
     *
     * @param
     * @return
     */
    @RequestMapping(value = "virtual-pay-fee", method = RequestMethod.GET)
    @ResponseBody
    public Result<BigDecimal> virtualPayFee(@RequestParam("packageId") Integer packageId) {
        if (packageId == null) {
            return Result.wrapErrorResult("", "服务包ID不存在");
        }
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Integer ucShopId = getUserGlobalShopId(userInfo.getUserGlobalId());
        BigDecimal totalFee = anxinInsuranceVirtualFlowFacade.selectPrePayAmount(ucShopId, packageId);
        return Result.wrapSuccessfulResult(totalFee);
    }

    /**
     * 支付宝支付
     */
    @RequestMapping(value = "alipay", method = RequestMethod.POST)
    @ResponseBody
    public Result virtualAlipay(@RequestParam("payFee") BigDecimal payFee,
                                @RequestParam("insuranceOrderSn") String insuranceOrderSn) {
        String uid = UserUtils.getUserGlobalIdForSession(request);
        OfferListFormParam param = new OfferListFormParam();
        param.setTotalFee(payFee);
        param.setUid(Integer.parseInt(uid));
        param.setSn(insuranceOrderSn);
        param.setWebReturnUrl(WebUtils.getHostUrl(request) + ALIPAY_URL);
        String htmlInfo = anxinInsuranceVirtualFlowFacade.alipay(param);
        if (htmlInfo == null || htmlInfo == "") {
            return Result.wrapErrorResult("", "获取支付信息失败");
        }
        return Result.wrapSuccessfulResult(htmlInfo);
    }

    /**
     * 支付宝回调
     *
     * @return
     */
    @RequestMapping(value = "virtual-verify/alipay", method = RequestMethod.GET)
    public String virtualPayResult(Model model) {
        Map<String, String[]> paramMap = request.getParameterMap();
        if ("TRADE_SUCCESS".equals(paramMap.get("trade_status")[0])) {
            BigDecimal totalFee = new BigDecimal(paramMap.get("total_fee")[0] + "");
            try {
                InsuranceServicePackageFeeDTO payment = anxinInsuranceVirtualFlowFacade.selectPayment(totalFee);
                model.addAttribute("payment", payment);
            } catch (Exception e) {
                log.error("[DUBBO]查询支付金额失败,错误原因:", e);
            }
        }
        model.addAttribute("info", "支付成功");
        anxinInsuranceVirtualFlowFacade.verifyAliPay(paramMap);
        return "yqx/page/ax_insurance/virtual/virtual-pay-result";
    }

    /**
     * 选择银行卡页面
     *
     * @param orderSn
     * @param model
     * @return
     */
    @RequestMapping("card-select")
    public String cardSelect(@RequestParam("orderSn") String orderSn,
                             @RequestParam("totalFee") BigDecimal totalFee, Model model) {
        model.addAttribute("orderSn", orderSn);
        model.addAttribute("totalFee", totalFee);
        return "yqx/page/ax_insurance/virtual/card-select";
    }

    /**
     * 获取已拥有的银行卡列表
     *
     * @return
     */
    @RequestMapping("card-list")
    @ResponseBody
    public Result cardList() {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Integer ucShopId = getUserGlobalShopId(userInfo.getUserGlobalId());
        List<UserBankCardDTO> bankCardList = lianlianPayFacade.selectUserBankCardList(ucShopId);
        return Result.wrapSuccessfulResult(bankCardList);
    }

    @RequestMapping(value = "check-card", method = RequestMethod.GET)
    @ResponseBody
    public Result checkCardNo(@RequestParam("cardNo") String cardNo,
                              @RequestParam("bankCode") String bankCode,
                              @RequestParam("cardType") Integer cardType) {
        //获取银行卡列表信用卡cardType是1,校验接口信用卡cardType0
        boolean flag = lianlianPayFacade.checkCardNo(cardNo, bankCode, cardType);
        if (!flag) {
            return Result.wrapErrorResult("", "银行卡号与所选银行不符");
        }
        return Result.wrapSuccessfulResult(flag);
    }

    /**
     * 获取支持的借记卡列表
     *
     * @return
     */
    @RequestMapping(value = "support-card-list", method = RequestMethod.GET)
    @ResponseBody
    public Result supportCardlist() {
        List<BankCardDTO> bankCardList = lianlianPayFacade.selectSupportDebitCardList();
        return Result.wrapSuccessfulResult(bankCardList);
    }

    /**
     * 获取支持的信用卡列表
     *
     * @return
     */
    @RequestMapping(value = "support-credit-card-list", method = RequestMethod.GET)
    @ResponseBody
    public Result supportCreditCardlist() {
        List<BankCardDTO> bankCardList = lianlianPayFacade.selectSupportCreditCardList();
        return Result.wrapSuccessfulResult(bankCardList);
    }

    /**
     * 连连支付
     *
     * @param payFinanceBo
     * @return
     */
    @RequestMapping(value = "unionpay", method = RequestMethod.POST)
    @ResponseBody
    public Result unionpay(@RequestBody PayFinanceBo payFinanceBo) {
        if (payFinanceBo == null) {
            return Result.wrapErrorResult("", "数据有误,支付失败");
        }
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Integer ucShopId = getUserGlobalShopId(userInfo.getUserGlobalId());
        OfferListLianFormParam param = new OfferListLianFormParam();
        param.setNoAgree(payFinanceBo.getNoAgree());
        param.setAcctName(payFinanceBo.getAcctName());
        param.setIsEncrypt(payFinanceBo.getIsEncrypt());
        param.setIdNo(payFinanceBo.getIdNo());
        param.setCardNo(payFinanceBo.getCardNo());
        param.setGmtCreate(new Date());
        param.setLianPayType(payFinanceBo.getPayMethod());
        param.setTotalFee(payFinanceBo.getTotalFee());
        param.setWebReturnUrl(WebUtils.getHostUrl(request) + UNIONPAY_URL);
        param.setSn(payFinanceBo.getOrderSn());
        param.setUid(ucShopId);
        //验证支付金额与虚拟投保单信息不符
        anxinInsuranceVirtualFlowFacade.checkPayFee(ucShopId, payFinanceBo.getOrderSn(), payFinanceBo.getTotalFee());
        String htmlInfo = anxinInsuranceVirtualFlowFacade.unionpay(param);
        if (htmlInfo == null || htmlInfo == "") {
            return Result.wrapErrorResult("", "获取支付信息失败");
        }
        return Result.wrapSuccessfulResult(htmlInfo);
    }

    /**
     * 连连支付回调
     *
     * @return
     */
    @RequestMapping(value = "virtual-verify/unionpay")
    public String virtualVerifyUnionpay(Model model) {
        Map<String, String[]> paramMap = request.getParameterMap();
        log.info("连连支付回调返回参数:{}", LogUtils.objectToString(paramMap));
        BigDecimal totalFee = new BigDecimal(paramMap.get("money_order")[0] + "");
        if ("SUCCESS".equals(paramMap.get("result_pay")[0])) {
            InsuranceServicePackageFeeDTO payment = new InsuranceServicePackageFeeDTO();
            payment.setFirstPaidAmount(totalFee);
            payment.setDeductionAmount(totalFee);
            BigDecimal basicPrecent = RpcServicePackageConstants.SERVICE_PACKAGE_PRE_PAID_AMOUNT_PERCENTAGE;
            BigDecimal precent = basicPrecent.divide((BigDecimal.ONE.subtract(basicPrecent)), 8, BigDecimal.ROUND_HALF_UP);
            payment.setSecondPaidAmount(totalFee.divide(precent, 2, BigDecimal.ROUND_HALF_UP));
            model.addAttribute("payment", payment);
            model.addAttribute("info", "success");

        } else {    //支付失败
            String orderSn = paramMap.get("no_order")[0];
            int i = orderSn.indexOf("-");
            if (i > 0) {
                orderSn = orderSn.substring(0, i);
            }
            model.addAttribute("prePayFee", totalFee);
            model.addAttribute("orderSn", orderSn);
            model.addAttribute("info", "defeat");
        }
        anxinInsuranceVirtualFlowFacade.verifyUnionpay(paramMap);
        return "yqx/page/ax_insurance/virtual/virtual-pay-result";
    }


    private Integer getUserGlobalShopId(String ucShopId) {
        if (StringUtils.isBlank(ucShopId)) {
            throw new BizException("门店信息不存在");
        }
        return Integer.parseInt(ucShopId);

    }

    private InsuranceVirtualFormDTO getFormId(InsuranceVirtualBasicDTO insuranceVirtualBasic) {
        if (insuranceVirtualBasic == null) {
            throw new BizException("虚拟投保单不存在,操作失败");
        }
        List<InsuranceVirtualFormDTO> insuranceVirtualFormDTOs = insuranceVirtualBasic.getInsuranceVirtualFormDTOList();
        if (org.springframework.util.CollectionUtils.isEmpty(insuranceVirtualFormDTOs)) {
            log.error("该虚拟投保单不存在保险,basicId", insuranceVirtualBasic.getId());
        }
        for (InsuranceVirtualFormDTO insuranceVirtualFormDTO : insuranceVirtualFormDTOs) {
            if (insuranceVirtualFormDTO == null) {
                throw new BizException("数据有误,操作失败");
            }
            if (insuranceVirtualFormDTO.getInsuranceType() == null) {
                log.error("保险类型Id不存在,basicId:{}", insuranceVirtualBasic.getId());
                throw new BizException("保险类型Id不存在,操作失败");
            }
            if (insuranceVirtualFormDTO.getInsuranceType().intValue() == 2) {
                return insuranceVirtualFormDTO;
            }
        }
        return null;
    }

    /**
     * 校验保存资料字段
     *
     * @param param
     * @return
     */
    private void checkSave(InsuranceCalculateParam param) {
        if (param == null) {
            throw new BizException("数据有误");
        }
        if (StringUtils.isBlank(param.getCarEngineSn())) {
            throw new BizException("发动机号不存在");
        }
        if (StringUtils.isBlank(param.getCarFrameSn())) {
            throw new BizException("车架号不存在");
        }
        if (param.getCarEgisterDate() == null) {
            throw new BizException("车辆登记日期不存在");
        }
        if (StringUtils.isBlank(param.getVehicleOwnerName())) {
            throw new BizException("车主姓名不存在");
        }
        if (StringUtils.isBlank(param.getVehicleOwnerCertType())) {
            throw new BizException("车主证件类型不存在");
        }
        if (StringUtils.isBlank(param.getVehicleOwnerCertCode())) {
            throw new BizException("车主证件号码不存在");
        }
        if (CredentialTypeEnum.IDCARD.getCode().toString().equals(param.getVehicleOwnerCertType())
                && param.getVehicleOwnerCertCode().length() != 18) {
            throw new BizException("请正确输入18位的身份证号");
        }
        if (!StringUtil.isMobileNO(param.getVehicleOwnerPhone())) {
            throw new BizException("手机号码不存在或格式有误");
        }
        if (CollectionUtils.isEmpty(param.getInsuranceFormDTOList())) {
            throw new BizException("未选择任何保险");
        }
        List<InsuranceFormParam> insuranceFormDTOList = param.getInsuranceFormDTOList();

        for (InsuranceFormParam insuranceFormParam : insuranceFormDTOList) {
            if (!CollectionUtils.isEmpty(insuranceFormDTOList)) {
                List<InsuranceItemParam> itemDTOList = insuranceFormParam.getItemDTOList();
                if (!CollectionUtils.isEmpty(itemDTOList)) {
                    for (InsuranceItemParam insuranceItemParam : itemDTOList) {
                        if (null == insuranceFormParam.getPackageStartTime()) {
                            throw new BizException(LegendInsuranceErrorCode.INSURANCE_EFFECTIVE_DATE.getErrorMessage());
                        }
                        if (null == insuranceItemParam.getInsuranceItemCoverageId()) {
                            log.error("险别项目主键不能为空");
                            throw new BizException(LegendInsuranceErrorCode.ITEM_MAJOR_KEY.getErrorMessage());

                        }
                    }
                }
            }
        }
    }

}
