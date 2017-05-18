package com.tqmall.legend.web.insurance;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.insurance.domain.result.InsuranceServicePackageDTO;
import com.tqmall.insurance.service.insurance.RpcInsurancePackageService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.facade.insurance.vo.InsuranceServicePackageVo;
import com.tqmall.legend.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * 安心保险服务包培训资料
 * Created by lixiao on 16/9/17.
 */
@Slf4j
@Controller
@RequestMapping("insurance/anxin/train")
public class AnxinInsuranceTrainController extends BaseController {
    @Autowired
    private RpcInsurancePackageService rpcInsurancePackageService;


    /**
     * 服务包培训资料页
     *
     * @return
     */
    @RequestMapping("training-materials")
    public String trainingMaterialsPage() {
        return "yqx/page/ax_insurance/train/training-materials";
    }


    /**
     * 服务包培训资料list
     *
     * @return
     */
    @RequestMapping("training-materials/list")
    @ResponseBody
    public Result<List<InsuranceServicePackageVo>> trainingMaterialsList() {
        List<InsuranceServicePackageVo> insuranceServicePackageVoList = Lists.newArrayList();
        try {
            com.tqmall.core.common.entity.Result<List<InsuranceServicePackageDTO>> packageList = rpcInsurancePackageService.getServicePackageList();
            log.info("【dubbo:安心保险】获取服务包培训资料详情，返回success:{}", packageList.isSuccess());
            if (packageList.isSuccess()) {
                List<InsuranceServicePackageDTO> packageDTOList = packageList.getData();
                //服务包价位期间分组
                getServicePackagePriceMap(packageDTOList,insuranceServicePackageVoList);
            }
        } catch (Exception e) {
            log.error("【dubbo:安心保险】获取服务包培训资料异常", e);
        }
        return Result.wrapSuccessfulResult(insuranceServicePackageVoList);
    }

    /**
     * 服务包价位区间分组
     *
     * @param packageDTOList
     * @param insuranceServicePackageVoList
     */
    private void getServicePackagePriceMap(List<InsuranceServicePackageDTO> packageDTOList, List<InsuranceServicePackageVo> insuranceServicePackageVoList) {
        if(CollectionUtils.isEmpty(packageDTOList)){
            return;
        }
        Map<String,InsuranceServicePackageVo> pricePackageVoMap = Maps.newHashMap();
        Map<String,List<InsuranceServicePackageDTO>> pricePackageDTOMap = Maps.newHashMap();
        for (InsuranceServicePackageDTO insuranceServicePackageDTO : packageDTOList) {
            String suitableStartPrice = getPriceStr(insuranceServicePackageDTO.getSuitableStartPrice());
            String suitableEndPrice = getPriceStr(insuranceServicePackageDTO.getSuitableEndPrice());
            StringBuffer priceSb = new StringBuffer();
            priceSb.append(suitableStartPrice);
            priceSb.append("≤");
            priceSb.append("商业保费");
            priceSb.append("≤");
            priceSb.append(suitableEndPrice);
            String suitablePriceStr = priceSb.toString();
            if (!pricePackageVoMap.containsKey(suitablePriceStr)) {
                InsuranceServicePackageVo insuranceServicePackageVo = new InsuranceServicePackageVo();
                insuranceServicePackageVo.setSuitablePrice(suitablePriceStr);
                insuranceServicePackageVoList.add(insuranceServicePackageVo);
                pricePackageVoMap.put(suitablePriceStr, insuranceServicePackageVo);
            }
            List<InsuranceServicePackageDTO> insuranceServicePackageDTOList;
            if(pricePackageDTOMap.containsKey(suitablePriceStr)){
                insuranceServicePackageDTOList = pricePackageDTOMap.get(suitablePriceStr);
            }else{
                insuranceServicePackageDTOList = Lists.newArrayList();
            }
            insuranceServicePackageDTOList.add(insuranceServicePackageDTO);
            pricePackageDTOMap.put(suitablePriceStr,insuranceServicePackageDTOList);
        }
        for(InsuranceServicePackageVo insuranceServicePackageVo : insuranceServicePackageVoList){
            String suitablePrice = insuranceServicePackageVo.getSuitablePrice();
            if(pricePackageDTOMap.containsKey(suitablePrice)){
                List<InsuranceServicePackageDTO> insuranceServicePackageDTOList = pricePackageDTOMap.get(suitablePrice);
                insuranceServicePackageVo.setInsuranceServicePackageDTOList(insuranceServicePackageDTOList);
            }
        }
    }

    /**
     * 金额取小数点无用的0
     * @param price
     * @return
     */
    private String getPriceStr(BigDecimal price) {
        if(price == null){
            return "";
        }
        DecimalFormat formatWithoutFraction = new DecimalFormat("###");
        DecimalFormat formatWithFraction = new DecimalFormat("###.###");
        if (new BigDecimal(price.intValue()).compareTo(price) == 0) {
            return formatWithoutFraction.format(price);
        }
        return formatWithFraction.format(price);
    }

    /**
     * 服务包培训资料详情
     *
     * @return
     */
    @RequestMapping("training-materials/detail")
    @ResponseBody
    public Result<InsuranceServicePackageDTO> trainingMaterialsDetail(@RequestParam(value = "id") Integer id) {
        if (id == null || id <= 0) {
            return Result.wrapErrorResult("", "id有误");
        }
        try {
            log.info("【dubbo:安心保险】获取服务包培训资料详情，id:{}", id);
            com.tqmall.core.common.entity.Result<InsuranceServicePackageDTO> packageDTOResult = rpcInsurancePackageService.getServicePackageById(id);
            log.info("【dubbo:安心保险】获取服务包培训资料详情，返回success:{}", packageDTOResult.isSuccess());
            if (packageDTOResult.isSuccess()) {
                InsuranceServicePackageDTO insuranceServicePackageDTO = packageDTOResult.getData();
                return Result.wrapSuccessfulResult(insuranceServicePackageDTO);
            }
        } catch (Exception e) {
            log.error("【dubbo:安心保险】获取服务包培训资料详情，出现异常，id:" + id, e);
        }
        return Result.wrapErrorResult("", "系统异常，请稍后再试");
    }
}
