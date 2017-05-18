package com.tqmall.legend.web.insurance;

import com.google.common.collect.Lists;
import com.tqmall.common.UserInfo;
import com.tqmall.common.util.DateUtil;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.insurance.domain.param.insurance.InsuranceCarModelParam;
import com.tqmall.insurance.domain.result.ServicePackageDTO;
import com.tqmall.insurance.service.insurance.RpcInsuranceLicenseService;
import com.tqmall.insurance.service.insurance.RpcPackageService;
import com.tqmall.legend.common.LegendError;
import com.tqmall.legend.entity.base.BaseEnumBo;
import com.tqmall.legend.facade.insurance.AnxinInsuranceVirtualFlowFacade;
import com.tqmall.legend.facade.insurance.InsuranceFormFacade;
import com.tqmall.legend.facade.insurance.vo.InsuranceCarModelVo;
import com.tqmall.legend.facade.insurance.vo.InsuranceCategoryVo;
import com.tqmall.legend.facade.insurance.vo.InsuranceMapVo;
import com.tqmall.legend.facade.insurance.vo.InsuranceRegionVo;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 安心保险API，存放通用接口方法
 * Created by lixiao on 16/9/17.
 */
@Slf4j
@Controller
@RequestMapping("insurance/anxin/api")
public class AnxinInsuranceApiController extends BaseController {
    @Autowired
    InsuranceFormFacade insuranceFormFacade;
    @Autowired
    private AnxinInsuranceVirtualFlowFacade anxinInsuranceVirtualFlowFacade;
    @Autowired
    private RpcInsuranceLicenseService rpcInsuranceLicenseService;
    @Autowired
    private RpcPackageService rpcPackageService;


    //投保获取省市信息
    @RequestMapping(value = "getAddressProvinceCity", method = RequestMethod.GET)
    @ResponseBody

    public Result<List<InsuranceRegionVo>> getAddressProvinceCity(String regionParentCode) {
        //默认传000000
        if (regionParentCode == null || regionParentCode.equals("")) {
            return Result.wrapErrorResult(LegendError.PARAM_ERROR);
        }

        List<InsuranceRegionVo> listRegion = insuranceFormFacade.getRegionByCodeAndIsOpen(regionParentCode);
        return Result.wrapSuccessfulResult(listRegion);
    }

    // 收货地址获取省市信息
    @RequestMapping(value = "getInsureProvinceCity", method = RequestMethod.GET)
    @ResponseBody
    public Result<List<InsuranceRegionVo>> getInsureProvinceCity(String regionParentCode) {
        //默认传000000
        if (regionParentCode == null || regionParentCode.equals("")) {
            return Result.wrapErrorResult(LegendError.PARAM_ERROR);
        }

        List<InsuranceRegionVo> listRegion = insuranceFormFacade.getRegionByCode(regionParentCode);
        return Result.wrapSuccessfulResult(listRegion);
    }

    //获取车辆信息
    @RequestMapping(value = "getCarModel", method = RequestMethod.GET)
    @ResponseBody
    public Result<List<InsuranceCarModelVo>> getCarModel(InsuranceCarModelParam insuranceCarModelParam) {
        //默认传000000
        if (insuranceCarModelParam.getVehicleName() == null || insuranceCarModelParam.getVehicleName().equals("")) {
            return Result.wrapErrorResult(LegendError.PARAM_ERROR);
        }
        List<InsuranceCarModelVo> insuranceCar = insuranceFormFacade.getCarModel(insuranceCarModelParam);
        return Result.wrapSuccessfulResult(insuranceCar);
    }

    //获取证件类型 （身份证，军人证，护照）
    @RequestMapping(value = "getInsuredCertType", method = RequestMethod.GET)
    @ResponseBody
    public Result<Map<Integer, String>> getInsuredCertType() {
        Map<Integer, String> map = insuranceFormFacade.getInsuredCertType();
        return Result.wrapSuccessfulResult(map);
    }

    //获取险别具体信息 （主险,副险）
    @RequestMapping(value = "getCategoryInfo", method = RequestMethod.GET)
    @ResponseBody
    public List<InsuranceCategoryVo> getCategoryInfo() {
        return insuranceFormFacade.getInsuranceCategory();
    }


    /**
     * 获取投保单状态
     *
     * @return
     */
    @RequestMapping(value = "getInsureStatus", method = RequestMethod.GET)
    @ResponseBody
    public Result<List<InsuranceMapVo>> getInsureStatus() {
        List<InsuranceMapVo> list = Lists.newArrayList();
        Map<Integer, String> map = insuranceFormFacade.getInsureStatus();
        for (Integer key : map.keySet()) {
            InsuranceMapVo insuranceMapVo = new InsuranceMapVo();
            insuranceMapVo.setMapKey(key + "");
            insuranceMapVo.setMapValue(map.get(key));
            list.add(insuranceMapVo);
        }
        return Result.wrapSuccessfulResult(list);
    }

    /**
     * 获取保单状态
     *
     * @return
     */
    @RequestMapping(value = "getQuitStatus", method = RequestMethod.GET)
    @ResponseBody
    public Result<List<InsuranceMapVo>> getQuitStatus() {
        List<InsuranceMapVo> list = Lists.newArrayList();
        Map<Integer, String> map = insuranceFormFacade.getQuitStatus();
        for (Integer key : map.keySet()) {
            InsuranceMapVo insuranceMapVo = new InsuranceMapVo();
            insuranceMapVo.setMapKey(key + "");
            insuranceMapVo.setMapValue(map.get(key));
            list.add(insuranceMapVo);
        }
        return Result.wrapSuccessfulResult(list);
    }

    /**
     * 获取虚拟投保单状态
     * 已OK
     *
     * @return
     */
    @RequestMapping(value = "virtual-status", method = RequestMethod.GET)
    @ResponseBody
    public Result<List<BaseEnumBo>> virtualStatus() {
        List<BaseEnumBo> boList = anxinInsuranceVirtualFlowFacade.selectVirtualStauts();
        return Result.wrapSuccessfulResult(boList);
    }

    /**
     * 获取服务包推荐列表
     *
     * @param formId
     * @param isVirtualForm
     * @return
     */
    @RequestMapping(value = "getPackageList", method = RequestMethod.GET)
    @ResponseBody
    public Result<List<ServicePackageDTO>> getPackageList(Integer formId, String isVirtualForm) {
        if (formId == null || formId.compareTo(0) <= 0) {
            return Result.wrapErrorResult(LegendError.PARAM_ERROR);
        }
        Boolean isVirtual = false;
        if (isVirtualForm != null && !isVirtualForm.equals("") && (isVirtualForm.equals("true") || isVirtualForm.equals("false"))) {
            isVirtual = Boolean.parseBoolean(isVirtualForm);
        }
        log.info("调用获取服务包列表接口参数. formId:{}", formId);
        try {
            com.tqmall.core.common.entity.Result<List<ServicePackageDTO>> suitableServicePackageResult = rpcPackageService.getSuitableServicePackage(formId, isVirtual);
            log.info("调用获取服务包列表接口参数. 返回success:{}", suitableServicePackageResult.isSuccess());
            if (!suitableServicePackageResult.isSuccess()) {
                return Result.wrapErrorResult("", "获取服务包列表失败，请稍后再试");
            }
            List<ServicePackageDTO> servicePackageDTOList = getNewServicePackageDTOList(suitableServicePackageResult.getData());
            Result needResult = new Result();
            needResult.setErrorMsg(suitableServicePackageResult.getMessage());
            needResult.setData(servicePackageDTOList);
            needResult.setSuccess(true);
            return needResult;
        } catch (Exception e) {
            log.error("调用获取服务包列表接口参数异常，异常信息", e);
        }
        return Result.wrapErrorResult("", "系统异常，请稍后再试");
    }

    /**
     * result转换
     *
     * @param servicePackageDTOList
     * @return
     */
    private List<ServicePackageDTO> getNewServicePackageDTOList(List<ServicePackageDTO> servicePackageDTOList) {
        List<ServicePackageDTO> resultList = Lists.newArrayList();
        List<ServicePackageDTO> falseList = Lists.newArrayList();
        //推荐排最前面
        for (ServicePackageDTO servicePackageDTO : servicePackageDTOList) {
            Boolean recommend = servicePackageDTO.getRecommend();
            if (recommend) {
                resultList.add(servicePackageDTO);
            } else {
                falseList.add(servicePackageDTO);
            }
        }
        resultList.addAll(falseList);
        return resultList;
    }

    @RequestMapping("check-license")
    @ResponseBody
    public Result checkLicense(@RequestParam(value = "provincesCode") String provincesCode,
                               @RequestParam(value = "cityCode") String cityCode,
                               @RequestParam(value = "licenseNo") String licenseNo) {
        if (StringUtils.isBlank(provincesCode)) {
            return Result.wrapErrorResult("", "省份为空");
        }
        if (StringUtils.isBlank(cityCode)) {
            return Result.wrapErrorResult("", "地区为空");
        }
        if (StringUtils.isBlank(licenseNo)) {
            return Result.wrapSuccessfulResult(true);
        }
        log.info("【安心保险】调用insurance校验车牌的有效性,provincesCode：{}，cityCode：{}，licenseNo：{}", provincesCode, cityCode, licenseNo);
        com.tqmall.core.common.entity.Result<Boolean> booleanResult = rpcInsuranceLicenseService.getInsuranceLicenseDTODetailByCityCode(provincesCode, cityCode, licenseNo);
        if (booleanResult.isSuccess() && booleanResult.getData()) {
            return Result.wrapSuccessfulResult(true);
        }
        return Result.wrapErrorResult("", "请填写和投保省对应一致的车牌!");
    }

    @RequestMapping(value = "log" ,method = RequestMethod.POST)
    @ResponseBody
    public Result printLog(@RequestParam("protocol") Boolean protocol) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        if (protocol) {
            log.info("【服务包协议日志记录】门店ID:{},操作人ID:{},操作时间:{},协议版本:服务包协议-含奖励金", userInfo.getShopId(), userInfo.getUserId(), DateUtil.convertDateToYMDHMS(new Date()));
            return Result.wrapSuccessfulResult(true);
        }
        log.info("【服务包协议日志记录】门店ID:{},操作人ID:{},操作时间:{},协议版本:服务包协议-不含奖励金", userInfo.getShopId(), userInfo.getUserId(),DateUtil.convertDateToYMDHMS(new Date()));
        return Result.wrapSuccessfulResult(true);
    }

}
