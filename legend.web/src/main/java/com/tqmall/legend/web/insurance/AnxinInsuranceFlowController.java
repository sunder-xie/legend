package com.tqmall.legend.web.insurance;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.DateUtil;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.common.exception.BusinessProcessFailException;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.error.LegendInsuranceErrorCode;
import com.tqmall.insurance.constants.CredentialTypeEnum;
import com.tqmall.insurance.domain.param.insurance.IdentityInfoParam;
import com.tqmall.insurance.domain.param.insurance.InsuranceCalculateParam;
import com.tqmall.insurance.domain.param.insurance.InsuranceFormParam;
import com.tqmall.insurance.domain.param.insurance.InsuranceItemParam;
import com.tqmall.insurance.domain.result.*;
import com.tqmall.legend.annotation.HttpRequestLog;
import com.tqmall.legend.cache.JedisClient;
import com.tqmall.legend.facade.insurance.AnxinInsuranceVirtualFlowFacade;
import com.tqmall.legend.facade.insurance.InsuranceFormFacade;
import com.tqmall.legend.facade.insurance.vo.InsuranceCategoryVo;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 安心保险投保流程
 * Created by lixiao on 16/9/14.
 */
@Slf4j
@Controller
@RequestMapping("insurance/anxin/flow")
public class AnxinInsuranceFlowController extends BaseController {
    @Autowired
    private InsuranceFormFacade insuranceFormFacade;
    @Autowired
    private JedisClient jedisClient;
    @Autowired
    private AnxinInsuranceVirtualFlowFacade anxinInsuranceVirtualFlowFacade;


    /**
     * 投保流程 1-3步  从输入基本信息 输入车型 到保费计算
     *
     * @param
     * @param
     * @return
     */
    @HttpRequestLog
    @RequestMapping(value = "insurance-flow", method = RequestMethod.GET)
    public String insuranceFlow(Model model) {
        model.addAttribute("listInsuranceCategory", insuranceFormFacade.getInsuranceCategory());
        return "yqx/page/ax_insurance/create/insureBaseInfo";
    }

    /**
     * 根据carNo和ownerName查询车辆基本信息
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "search-vehicleInfo", method = RequestMethod.GET)
    public String searchVehicleInfo(@RequestParam("carNo") String carNo, @RequestParam("ownerName") String ownerName, Model model) {
        VehicleInformationDTO vehicleInformation = insuranceFormFacade.getVehicleInformation(carNo, ownerName);
        List<InsuranceCategoryVo> insuranceList = insuranceFormFacade.getInsuranceCategory();
        if (vehicleInformation == null) {
            model.addAttribute("insuranceBasic", null);
            model.addAttribute("listInsuranceCategory", insuranceList);
            return "yqx/page/ax_insurance/create/second-three-mix";
        }

        InsuranceBasicDTO insurance = new InsuranceBasicDTO();
        insurance.setVehicleOwnerName(vehicleInformation.getOwnerName());
        insurance.setVehicleSn(vehicleInformation.getCarNo());
        insurance.setCarEngineSn(vehicleInformation.getEngineNo());
        insurance.setCarFrameSn(vehicleInformation.getVin());
        insurance.setCarConfigType(vehicleInformation.getModelName());
        insurance.setCarEgisterDate(DateUtil.convertStringToDateYMD(vehicleInformation.getRegisterDate()));
        List<InsuranceFormDTO> insuranceFormDTOList = Lists.newArrayList();
        if (vehicleInformation.getForceExpireDate() != null && !("").equals(vehicleInformation.getForceExpireDate())) {
            InsuranceFormDTO insuranceFormDTO = new InsuranceFormDTO();
            insuranceFormDTO.setInsuranceType(1);
            Date endTime = DateUtil.convertStringToDateYMD(vehicleInformation.getForceExpireDate());
            insuranceFormDTO.setGmtCreate(endTime);
            insuranceFormDTOList.add(insuranceFormDTO);
        }
        if (vehicleInformation.getBusinessExpireDate() != null && !("").equals(vehicleInformation.getBusinessExpireDate())) {
            InsuranceFormDTO insuranceFormDTO = new InsuranceFormDTO();
            insuranceFormDTO.setInsuranceType(2);
            Date endTime = DateUtil.convertStringToDateYMD(vehicleInformation.getBusinessExpireDate());
            insuranceFormDTO.setGmtCreate(endTime);
            insuranceFormDTOList.add(insuranceFormDTO);
        }
        insurance.setInsuranceFormDTOList(insuranceFormDTOList);
        model.addAttribute("insuranceBasic", insurance);
        model.addAttribute("listInsuranceCategory", insuranceList);
        return "yqx/page/ax_insurance/create/second-three-mix";
    }


    /**
     * 跳转到选择服务包
     *
     * @param
     * @param
     * @return
     */
    @RequestMapping(value = "select-package", method = RequestMethod.GET)
    public String selectPackage() {
        return "yqx/page/ax_insurance/create/service-pack";
    }

    /**
     * 跳转到确认保费页面
     *
     * @return
     */
    @RequestMapping(value = "confirm-info", method = RequestMethod.GET)
    public String confirmInfo() {
        return "yqx/page/ax_insurance/create/confirmInfo";
    }

    /**
     * 投保提交后跳转到结果页面
     *
     * @param
     * @param
     * @return
     */
    @RequestMapping(value = "insurance-result", method = RequestMethod.GET)
    public String uploadImg(String orderSn, Model model) {
        if(StringUtils.isBlank(orderSn)){
            return "common/error";
        }
        model.addAttribute("orderSn", orderSn);
        return "yqx/page/ax_insurance/create/whetherSuc";
    }

    /**
     * 计算保费
     *
     * @param
     * @param insuranceCalculateParam
     * @return
     */
    @RequestMapping(value = "FeeSave", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    @ResponseBody
    public Result insuranceFeeSave(@RequestBody InsuranceCalculateParam insuranceCalculateParam) {
        log.info("调用保费计算开始{}", JSON.toJSONString(insuranceCalculateParam));
        UserInfo userInfo = UserUtils.getUserInfo(request);
        String agentName = anxinInsuranceVirtualFlowFacade.selectShopNameById(userInfo.getShopId());
        insuranceCalculateParam.setAgentName(agentName);
        insuranceCalculateParam.setSeatNumber(insuranceCalculateParam.getSeatNumber().trim());
        String ucShopId = UserUtils.getUserGlobalIdForSession(request);
//        insuranceCalculateParam.setCooperationMode(cooperationMode);
        if (StringUtils.isBlank(ucShopId)) {
            log.error("计算保费获取门店id失败：shopId为空");
            return Result.wrapErrorResult("", "系统错误");
        }
        insuranceCalculateParam.setCreator(userInfo.getUserId().intValue());
        insuranceCalculateParam.setAgentId(Integer.valueOf(ucShopId));
        insuranceCalculateParam.setInsuranceCompanyId(1);
        com.tqmall.legend.common.Result<InsuranceCalculateDTO> needResult = null;

        if (insuranceCalculateParam == null || CollectionUtils.isEmpty(insuranceCalculateParam.getInsuranceFormDTOList())) {
            return Result.wrapErrorResult("", "数据有误,保费计算失败");
        }
        try {
            Result result = checkParam(insuranceCalculateParam);
            if (!result.isSuccess()) {
                return result;
            }
            needResult = insuranceFormFacade.insuranceFeeCalculate(insuranceCalculateParam);

        } catch (Exception e) {
//            log.error("订单失败。request:{}", JSON.toJSONString(request), e);
            log.error("订单失败. request:{}", LogUtils.objectToString(insuranceCalculateParam));
            throw new BizException("订单失败");
        }
        log.info("调用保费计算结束{}", JSON.toJSONString(needResult.getData()));
        if (!needResult.isSuccess()) {
            jedisClient.delete(insuranceCalculateParam.getInsuranceToken());
        }

        return transformResult(needResult);
    }

    /**
     * 重新计算保费
     *
     * @param
     * @param
     * @return
     */
    @RequestMapping(value = "repeatCalculate", method = RequestMethod.POST)
    @ResponseBody
    public Result repeatCalculate(Integer basicId) {
        log.info("调用重新计算保费开始{}", basicId);
        com.tqmall.legend.common.Result<InsuranceCalculateDTO> needResult = null;
        try {
            needResult = insuranceFormFacade.repeatFeeCalculate(basicId);
        } catch (Exception e) {
            log.error("调用重新计算保费是失败。basicId:" + basicId, e);
            throw new BusinessProcessFailException("订单失败");
        }
        log.info("调用重新计算保费结束{}", JSON.toJSONString(needResult.getData()));
        return transformResult(needResult);
    }


    /**
     * 点击返回修改返回保单详情
     *
     * @param
     * @param
     * @return
     */
    @RequestMapping(value = "back-update", method = RequestMethod.GET)
    public String backUpdate(Model model, @RequestParam(value = "id", required = false) Integer id) {
        InsuranceBasicDTO insurance = new InsuranceBasicDTO();
        List<InsuranceCategoryVo> insuranceList;
        if (id == null) {
            insuranceList = insuranceFormFacade.getInsuranceCategory();
        } else {
            String userGlobalId = UserUtils.getUserGlobalIdForSession(request);
            insurance = insuranceFormFacade.backUpdateInfo(id, Integer.valueOf(userGlobalId));
            insuranceList = insuranceFormFacade.getInsuranceCategoryHasChoose(insurance);
            List<InsuranceFormDTO> insuranceFormList = insurance.getInsuranceFormDTOList();
            if (insuranceFormList != null) {
                //商业险form  若商业险只选
                InsuranceFormDTO insuranceForm = null;
                //交强险form
                InsuranceFormDTO insuranceFormJQX = null;
                for (int i = 0; i < insuranceFormList.size(); i++) {
                    Integer type = insuranceFormList.get(i).getInsuranceType();
                    if (type.compareTo(2) == 0) {
                        insuranceForm = insuranceFormList.get(i);
                    }
                    if (type.compareTo(1) == 0) {
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
        model.addAttribute("insuranceBasic", insurance);
        model.addAttribute("listInsuranceCategory", insuranceList);
        return "yqx/page/ax_insurance/create/insureBaseInfo";
    }

    private void setTime(InsuranceFormDTO insuranceFormJQX, Date packageStartTime) {
        if (packageStartTime != null) {
            Map map = DateUtil.insuranceDate(packageStartTime);
            insuranceFormJQX.setPackageStartTime((Date) map.get("startTime"));
            insuranceFormJQX.setPackageEndTime((Date) map.get("endTime"));
        }
    }


    /**
     * 奖励  金模式保费提交
     *
     * @param insuranceCalculateParam
     * @return
     */
    @RequestMapping(value = "FeeSubmit", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    @ResponseBody
    public Result insuranceFeeUpdate(@RequestBody InsuranceCalculateParam insuranceCalculateParam) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        log.info("奖励金保费提交阅读协议,门店ID:{},操作人:{},被保人:{},被保人手机号:{}", userInfo.getUserGlobalId(), userInfo.getName(), insuranceCalculateParam.getApplicantName(), insuranceCalculateParam.getApplicantPhone());
        insuranceCalculateParam.setCooperationMode(1);
        Result result = checkInsuranceCalculateParamCommit(insuranceCalculateParam);
        if (!result.isSuccess()) {
            return result;
        }
        if (insuranceCalculateParam.getInsuranceFormToken() != null) {
            //防止多次提交 token需要页面传过来
            boolean lockAcquire = jedisClient.acquireLock(insuranceCalculateParam.getInsuranceFormToken(), 60 * 10);
            if (!lockAcquire) {
                return Result.wrapErrorResult(LegendInsuranceErrorCode.ORDER_REPEAT_SUBMIT_ERROR.getCode(), LegendInsuranceErrorCode.ORDER_REPEAT_SUBMIT_ERROR.getErrorMessage());
            }
        }
        com.tqmall.legend.common.Result<InsuranceBasicDTO> insuranceBasic = insuranceFormFacade.updateInsuranceFee(insuranceCalculateParam);
        log.info("调用保费提交结束{}", JSON.toJSONString(insuranceCalculateParam));
        if (insuranceBasic.isSuccess()) {
            log.info("门店:{}，车主:{},买了保险", userInfo.getName(), insuranceCalculateParam.getVehicleOwnerName());
        } else {
            jedisClient.delete(insuranceCalculateParam.getInsuranceFormToken());
        }
        return transformResult(insuranceBasic);
    }

    /**
     * 买保险送服务包模式保费提交·
     *
     * @param insuranceCalculateParam
     * @return
     */
    @RequestMapping(value = "PackageFeeSubmit", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    @ResponseBody
    public Result packageFeeSubmit(@RequestBody InsuranceCalculateParam insuranceCalculateParam) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        log.info("送服务包保费提交阅读协议,门店ID:{},操作人:{},被保人:{},被保人手机号:{}", userInfo.getUserGlobalId(), userInfo.getName(), insuranceCalculateParam.getApplicantName(), insuranceCalculateParam.getApplicantPhone());
        insuranceCalculateParam.setCooperationMode(2);
        Result result = checkInsuranceCalculateParamCommit(insuranceCalculateParam);
        if (!result.isSuccess()) {
            return result;
        }
        if (insuranceCalculateParam.getInsuranceFormToken() != null) {
            //防止多次提交 token需要页面传过来
            boolean lockAcquire = jedisClient.acquireLock(insuranceCalculateParam.getInsuranceFormToken(), 60 * 10);
            if (!lockAcquire) {
                return Result.wrapErrorResult(LegendInsuranceErrorCode.ORDER_REPEAT_SUBMIT_ERROR.getCode(), LegendInsuranceErrorCode.ORDER_REPEAT_SUBMIT_ERROR.getErrorMessage());
            }
        }
        com.tqmall.legend.common.Result<InsuranceBasicDTO> insuranceBasic = insuranceFormFacade.updateInsuranceFeePackage(insuranceCalculateParam);
        log.info("调用保费提交结束{}", JSON.toJSONString(insuranceCalculateParam));
        if (insuranceBasic.isSuccess()) {
            log.info("门店:{}，车主:{},买了保险", userInfo.getName(), insuranceCalculateParam.getVehicleOwnerName());
        } else {
            jedisClient.delete(insuranceCalculateParam.getInsuranceFormToken());
        }
        return transformResult(insuranceBasic);
    }

    @RequestMapping("upload-info")
    @ResponseBody
    public Result uploadInfo(IdentityInfoParam identityInfoParam) {
        boolean success = insuranceFormFacade.uploadIdentityInfo(identityInfoParam);
        if (!success) {
            return Result.wrapErrorResult("", "验证码发送失败");
        }
        return Result.wrapSuccessfulResult(true);
    }

    @RequestMapping("pay-info")
    @ResponseBody
    public Result uploadInfo(String orderSn) {
        Integer ucshopId = getUserGlobalShopId();
        InsuranceFeeDTO insuranceFeeDTO = insuranceFormFacade.selectPayInfoByOrderSn(ucshopId, orderSn);
        return Result.wrapSuccessfulResult(insuranceFeeDTO);
    }


    /**
     * 手机上传照片
     *
     * @param orderSn
     * @return
     */
    @RequestMapping(value = "mobile/upload-img", method = RequestMethod.GET)
    public String mobileUpload(String orderSn, Model model) {
        model.addAttribute("orderSn", orderSn);
        return "yqx/page/ax_insurance/mobile/upload-img";
    }

    /**
     * 确认上传成功
     *
     * @param orderSn
     * @return
     */
    @RequestMapping("mobile/submit-upload")
    @ResponseBody
    public Result submitUpload(@RequestParam("orderSn") String orderSn,
                               @RequestParam("urls") String urls) {
        boolean flag =jedisClient.set(orderSn,1800, urls);
        return Result.wrapSuccessfulResult(flag);
    }

    /**
     * 轮询是否上传完毕
     *
     * @param orderSn
     * @return
     */
    @RequestMapping("loop-confirm")
    @ResponseBody
    public Result loopConfirm(@RequestParam("orderSn") String orderSn) {
        String urls = jedisClient.get(orderSn, String.class);
        if (StringUtils.isBlank(urls)) {
            return Result.wrapSuccessfulResult(false);
        }
        return Result.wrapSuccessfulResult(true);
    }

    /**
     * 确认上传
     *
     * @param orderSn
     * @return
     */
    @RequestMapping("upload-confirm")
    @ResponseBody
    public Result uploadConfirm(@RequestParam("orderSn") String orderSn) {
        String urls = jedisClient.get(orderSn, String.class);
        UserInfo userInfo = UserUtils.getUserInfo(request);
        boolean success = insuranceFormFacade.uploadImgToAnxin(orderSn, urls, userInfo);
        return Result.wrapSuccessfulResult(success);
    }

    //result转换
    private Result transformResult(com.tqmall.legend.common.Result resultTemp) {
        Result needResult = new Result();
        needResult.setCode(resultTemp.getCode());
        needResult.setData(resultTemp.getData());
        needResult.setErrorMsg(resultTemp.getErrorMsg());
        needResult.setSuccess(resultTemp.isSuccess());
        return needResult;
    }


    private Result checkParam(InsuranceCalculateParam insuranceCalculateParam)

    {
        /**
         * 投保模式不能为空
         */
        if (insuranceCalculateParam.getCooperationMode() == null) {
            return Result.wrapErrorResult(LegendInsuranceErrorCode.INSURE_MODE_NOT_NULL.getCode(), LegendInsuranceErrorCode.INSURE_MODE_NOT_NULL.getErrorMessage());
        }
        /**
         * token验证*
         */
        if (insuranceCalculateParam.getInsuranceToken() == null) {
            return Result.wrapErrorResult(LegendInsuranceErrorCode.TOKEN_NOT_NULL.getCode(), LegendInsuranceErrorCode.TOKEN_NOT_NULL.getErrorMessage());
        }
        /**
         * 投保所在省*
         */
        if (insuranceCalculateParam.getInsuredProvince() == null) {
            return Result.wrapErrorResult(LegendInsuranceErrorCode.PROVINCE_NOT_NULL.getCode(), LegendInsuranceErrorCode.PROVINCE_NOT_NULL.getErrorMessage());

        }
        /**
         * 投保所在城市*
         */
        if (insuranceCalculateParam.getInsuredCity() == null) {
            return Result.wrapErrorResult(LegendInsuranceErrorCode.CITY_NOT_NULL.getCode(), LegendInsuranceErrorCode.CITY_NOT_NULL.getErrorMessage());
        }
        /**
         * 投保所在省CODE*
         */
        if (insuranceCalculateParam.getInsuredProvinceCode() == null) {
            return Result.wrapErrorResult(LegendInsuranceErrorCode.CITY_CODE_NOT_NULL.getCode(), LegendInsuranceErrorCode.CITY_CODE_NOT_NULL.getErrorMessage());
        }
        /**
         * 投保所在城市CODE*
         */
        if (insuranceCalculateParam.getInsuredCityCode() == null) {
            return Result.wrapErrorResult(LegendInsuranceErrorCode.PROVINCE_CODE_NOT_NULL.getCode(), LegendInsuranceErrorCode.PROVINCE_CODE_NOT_NULL.getErrorMessage());
        }

        /**是否取得车牌 1:有 0:没有**/
        if (insuranceCalculateParam.getHasLicense() == null) {
            return Result.wrapErrorResult(LegendInsuranceErrorCode.IS_GET_VEHICLE.getCode(), LegendInsuranceErrorCode.IS_GET_VEHICLE.getErrorMessage());
        } else if (insuranceCalculateParam.getHasLicense() == 0) {
            /**
             * 新车购置发票号*
             */
            if (("北京市").equals(insuranceCalculateParam.getInsuredProvince()) && insuranceCalculateParam.getNewCarBillSn() == null) {
                return Result.wrapErrorResult(LegendInsuranceErrorCode.NEW_CAR_INVOICE.getCode(), LegendInsuranceErrorCode.NEW_CAR_INVOICE.getErrorMessage());
            }
            /**
             * 发票开具日期*
             */
            if (("北京市").equals(insuranceCalculateParam.getInsuredProvince()) && insuranceCalculateParam.getNewCarBillTime() == null) {
                return Result.wrapErrorResult(LegendInsuranceErrorCode.INVOICE_DATE_NOT_NULL.getCode(), LegendInsuranceErrorCode.INVOICE_DATE_NOT_NULL.getErrorMessage());

            }
        }

        /**
         * 车架号*
         */
        if (insuranceCalculateParam.getCarFrameSn() == null) {
            return Result.wrapErrorResult(LegendInsuranceErrorCode.CARFRAME_NOT_NULL.getCode(), LegendInsuranceErrorCode.CARFRAME_NOT_NULL.getErrorMessage());
        }

        /**
         * 车辆登记日期*
         */
        if (insuranceCalculateParam.getCarEgisterDate() == null) {
            return Result.wrapErrorResult(LegendInsuranceErrorCode.CAR_REGISTER_DATE.getCode(), LegendInsuranceErrorCode.CAR_REGISTER_DATE.getErrorMessage());
        }
        /**
         * 车主名称*
         */
        if (insuranceCalculateParam.getVehicleOwnerName() == null) {
            return Result.wrapErrorResult(LegendInsuranceErrorCode.CAR_OWNER_NAME_NOT_NULL.getCode(), LegendInsuranceErrorCode.CAR_OWNER_NAME_NOT_NULL.getErrorMessage());

        }
        /**
         * 车主证件类型*
         */
        if (insuranceCalculateParam.getVehicleOwnerCertType() == null) {
            return Result.wrapErrorResult(LegendInsuranceErrorCode.OWNER_CERTIFICATE_TYPE_NOT_NULL.getCode(), LegendInsuranceErrorCode.OWNER_CERTIFICATE_TYPE_NOT_NULL.getErrorMessage());
        }
        /**
         * 车主证件编码*
         */
        if (insuranceCalculateParam.getVehicleOwnerCertCode() == null) {
            return Result.wrapErrorResult(LegendInsuranceErrorCode.OWNER_CERTIFICATE_CODE_NOT_NULL.getCode(), LegendInsuranceErrorCode.OWNER_CERTIFICATE_CODE_NOT_NULL.getErrorMessage());
        }
        //身份证号18位
        if (CredentialTypeEnum.IDCARD.getCode().toString().equals(insuranceCalculateParam.getVehicleOwnerCertType())
                && insuranceCalculateParam.getVehicleOwnerCertCode().length() != 18) {
            return Result.wrapErrorResult(LegendInsuranceErrorCode.CREDENTAIL_TYPE_IDCARD_LENGHT.getCode(), LegendInsuranceErrorCode.CREDENTAIL_TYPE_IDCARD_LENGHT.getErrorMessage());
        }
        /**
         * 车主手机号*
         */
        if (insuranceCalculateParam.getVehicleOwnerPhone() == null) {
            return Result.wrapErrorResult(LegendInsuranceErrorCode.OWNER_PHONE_NUMBER_NOT_NULL.getCode(), LegendInsuranceErrorCode.OWNER_PHONE_NUMBER_NOT_NULL.getErrorMessage());

        }

        List<InsuranceFormParam> insuranceFormDTOList = insuranceCalculateParam.getInsuranceFormDTOList();

        for (InsuranceFormParam insuranceFormParam : insuranceFormDTOList) {
            if (!CollectionUtils.isEmpty(insuranceFormDTOList)) {
                List<InsuranceItemParam> itemDTOList = insuranceFormParam.getItemDTOList();
                if (!CollectionUtils.isEmpty(itemDTOList)) {
                    for (InsuranceItemParam insuranceItemParam : itemDTOList) {
                        if (null == insuranceFormParam.getPackageStartTime()) {
                            return Result.wrapErrorResult(LegendInsuranceErrorCode.INSURANCE_EFFECTIVE_DATE.getCode(), LegendInsuranceErrorCode.INSURANCE_EFFECTIVE_DATE.getErrorMessage());
                        }
                        if (null == insuranceItemParam.getInsuranceItemCoverageId()) {
                            log.error("险别项目主键不能为空");
                            return Result.wrapErrorResult(LegendInsuranceErrorCode.ITEM_MAJOR_KEY.getCode(), LegendInsuranceErrorCode.ITEM_MAJOR_KEY.getErrorMessage());

                        }
                    }
                }
            }
        }

        return Result.wrapSuccessfulResult(true);
    }


    private Result checkInsuranceCalculateParamCommit(InsuranceCalculateParam insuranceCalculateParam) {
        /**
         * 投保模式不能为空
         */
        if (insuranceCalculateParam.getCooperationMode() == null) {
            return Result.wrapErrorResult(LegendInsuranceErrorCode.INSURE_MODE_NOT_NULL.getCode(), LegendInsuranceErrorCode.INSURE_MODE_NOT_NULL.getErrorMessage());
        }
        /**
         * 门店ID*
         */
        if (insuranceCalculateParam.getAgentId() == null) {
            return Result.wrapErrorResult(LegendInsuranceErrorCode.SHOP_ID_NOT_NULL.getCode(), LegendInsuranceErrorCode.SHOP_ID_NOT_NULL.getErrorMessage());
        }
        /**
         * 保险公司ID*
         */
        if (insuranceCalculateParam.getInsuranceCompanyId() == null) {
            return Result.wrapErrorResult(LegendInsuranceErrorCode.INSURANCE_COMPANY_ID_NOT_NULL.getCode(), LegendInsuranceErrorCode.INSURANCE_COMPANY_ID_NOT_NULL.getErrorMessage());
        }

        /**
         * 卖保险代理人名称*
         */
        if (insuranceCalculateParam.getAgentName() == null) {
            return Result.wrapErrorResult(LegendInsuranceErrorCode.AGENT_NAME_NOT_NULL.getCode(), LegendInsuranceErrorCode.AGENT_NAME_NOT_NULL.getErrorMessage());
        }
        /**
         * 车主名称*
         */
        if (insuranceCalculateParam.getVehicleOwnerName() == null) {
            return Result.wrapErrorResult(LegendInsuranceErrorCode.CAR_OWNER_NAME_NOT_NULL.getCode(), LegendInsuranceErrorCode.CAR_OWNER_NAME_NOT_NULL.getErrorMessage());
        }
        /**
         * 车主证件类型*
         */
        if (insuranceCalculateParam.getVehicleOwnerCertType() == null) {
            return Result.wrapErrorResult(LegendInsuranceErrorCode.OWNER_CERTIFICATE_TYPE_NOT_NULL.getCode(), LegendInsuranceErrorCode.OWNER_CERTIFICATE_TYPE_NOT_NULL.getErrorMessage());
        }
        /**
         * 车主证件编码*
         */
        if (insuranceCalculateParam.getVehicleOwnerCertCode() == null) {
            return Result.wrapErrorResult(LegendInsuranceErrorCode.OWNER_CERTIFICATE_CODE_NOT_NULL.getCode(), LegendInsuranceErrorCode.OWNER_CERTIFICATE_CODE_NOT_NULL.getErrorMessage());
        }
        //身份证号18位
        if (CredentialTypeEnum.IDCARD.getCode().toString().equals(insuranceCalculateParam.getVehicleOwnerCertType())
                && insuranceCalculateParam.getVehicleOwnerCertCode().length() != 18) {
            return Result.wrapErrorResult(LegendInsuranceErrorCode.CREDENTAIL_TYPE_IDCARD_LENGHT.getCode(), LegendInsuranceErrorCode.CREDENTAIL_TYPE_IDCARD_LENGHT.getErrorMessage());
        }
        /**
         * 车主手机号*
         */
        if (insuranceCalculateParam.getVehicleOwnerPhone() == null) {
            return Result.wrapErrorResult(LegendInsuranceErrorCode.OWNER_PHONE_NUMBER_NOT_NULL.getCode(), LegendInsuranceErrorCode.OWNER_PHONE_NUMBER_NOT_NULL.getErrorMessage());
        }
        /**
         * 投保人名称*
         */
        if (insuranceCalculateParam.getApplicantName() == null) {
            return Result.wrapErrorResult(LegendInsuranceErrorCode.INSURE_PEOPLE_NAME_NOT_NULL.getCode(), LegendInsuranceErrorCode.INSURE_PEOPLE_NAME_NOT_NULL.getErrorMessage());
        }
        /**
         * 投保人证件类型*
         */
        if (insuranceCalculateParam.getTapplicantCertType() == null) {
            return Result.wrapErrorResult(LegendInsuranceErrorCode.INSURE_PEOPLE_CERTIFICATE_TYPE_NOT_NULL.getCode(), LegendInsuranceErrorCode.INSURE_PEOPLE_CERTIFICATE_TYPE_NOT_NULL.getErrorMessage());
        }
        /**
         * 投保人证件编码*
         */
        if (insuranceCalculateParam.getApplicantCertCode() == null) {
            return Result.wrapErrorResult(LegendInsuranceErrorCode.INSURE_PEOPLE_CERTIFICATE_CODE_NOT_NULL.getCode(), LegendInsuranceErrorCode.INSURE_PEOPLE_CERTIFICATE_CODE_NOT_NULL.getErrorMessage());
        }
        //身份证号18位
        if (CredentialTypeEnum.IDCARD.getCode().toString().equals(insuranceCalculateParam.getTapplicantCertType())
                && insuranceCalculateParam.getApplicantCertCode().length() != 18) {
            return Result.wrapErrorResult(LegendInsuranceErrorCode.CREDENTAIL_TYPE_IDCARD_LENGHT.getCode(), LegendInsuranceErrorCode.CREDENTAIL_TYPE_IDCARD_LENGHT.getErrorMessage());
        }
        /**
         * 投保人地址*
         */
//        if (insuranceCalculateParam.getApplicantAddr() == null) {
//            return Result.wrapErrorResult("90000015", "投保人地址不能为空!");
//        }
        /**
         * 投保人手机号*
         */
        if (insuranceCalculateParam.getApplicantPhone() == null) {
            return Result.wrapErrorResult(LegendInsuranceErrorCode.INSURE_PEOPLE_PHONE_NUMBER_NOT_NULL.getCode(), LegendInsuranceErrorCode.INSURE_PEOPLE_PHONE_NUMBER_NOT_NULL.getErrorMessage());
        }
        /**
         * 被保人名称*
         */
        if (insuranceCalculateParam.getInsuredName() == null) {
            return Result.wrapErrorResult(LegendInsuranceErrorCode.BEI_INSURE_PEOPLE_NAME_NOT_NULL.getCode(), LegendInsuranceErrorCode.BEI_INSURE_PEOPLE_NAME_NOT_NULL.getErrorMessage());
        }
        /**
         * 被保人证件类型*
         */
        if (insuranceCalculateParam.getInsuredCertType() == null) {
            return Result.wrapErrorResult(LegendInsuranceErrorCode.BEI_INSURE_PEOPLE_CERTIFICATE_TYPE_NOT_NULL.getCode(), LegendInsuranceErrorCode.BEI_INSURE_PEOPLE_CERTIFICATE_TYPE_NOT_NULL.getErrorMessage());
        }

        /**
         * 被保人证件编码*
         */
        if (insuranceCalculateParam.getInsuredCertCode() == null) {
            return Result.wrapErrorResult(LegendInsuranceErrorCode.BEI_INSURE_PEOPLE_CERTIFICATE_CODE_NOT_NULL.getCode(), LegendInsuranceErrorCode.BEI_INSURE_PEOPLE_CERTIFICATE_CODE_NOT_NULL.getErrorMessage());
        }
        //身份证号18位
        if (CredentialTypeEnum.IDCARD.getCode().equals(Integer.parseInt(insuranceCalculateParam.getInsuredCertType()))
                && insuranceCalculateParam.getInsuredCertCode().length() != 18) {
            return Result.wrapErrorResult(LegendInsuranceErrorCode.CREDENTAIL_TYPE_IDCARD_LENGHT.getCode(), LegendInsuranceErrorCode.CREDENTAIL_TYPE_IDCARD_LENGHT.getErrorMessage());
        }
        /**
         * 被保人地址*
         */
//        if (insuranceCalculateParam.getInsuredAddr() == null) {
//            return Result.wrapErrorResult("90000020", "被保人地址不能为空!");
//        }
        /**
         * 被保人手机号*
         */
        if (insuranceCalculateParam.getInsuredPhone() == null) {
            return Result.wrapErrorResult(LegendInsuranceErrorCode.BEI_PHONE_NUMBER_NOT_NULL.getCode(), LegendInsuranceErrorCode.BEI_PHONE_NUMBER_NOT_NULL.getErrorMessage());
        }

        /**
         * 投保所在省*
         */
        if (insuranceCalculateParam.getInsuredProvince() == null) {
            return Result.wrapErrorResult(LegendInsuranceErrorCode.PROVINCE_NOT_NULL.getCode(), LegendInsuranceErrorCode.PROVINCE_NOT_NULL.getErrorMessage());
        }
        /**
         * 投保所在城市*
         */
        if (insuranceCalculateParam.getInsuredCity() == null) {
            return Result.wrapErrorResult(LegendInsuranceErrorCode.CITY_NOT_NULL.getCode(), LegendInsuranceErrorCode.CITY_NOT_NULL.getErrorMessage());
        }

        return Result.wrapSuccessfulResult(true);
    }

    private Integer getUserGlobalShopId() {
        String ucShopId = UserUtils.getUserGlobalIdForSession(request);
        if (StringUtils.isBlank(ucShopId)) {
            throw new BizException("门店信息不存在");
        }
        return Integer.parseInt(ucShopId);

    }
}
