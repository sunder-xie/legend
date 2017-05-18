package com.tqmall.legend.facade.smart.impl;

import com.tqmall.common.util.BdUtil;
import com.tqmall.common.util.DateUtil;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.core.common.entity.Result;
import com.tqmall.insurance.domain.param.smart.SmartSearchParam;
import com.tqmall.insurance.domain.result.InsuranceBasicDTO;
import com.tqmall.insurance.domain.result.InsuranceFormDTO;
import com.tqmall.insurance.domain.result.smart.SmartThirdInsuredInfoDTO;
import com.tqmall.insurance.service.smart.RpcSmartSearchBiHuService;
import com.tqmall.legend.facade.insurance.InsuranceFormFacade;
import com.tqmall.legend.facade.insurance.vo.InsuranceCategoryVo;
import com.tqmall.legend.facade.insurance.vo.InsuranceItemCoverageVo;
import com.tqmall.legend.facade.smart.BihuSmartSearchFacade;
import com.tqmall.legend.facade.smart.annotation.SmartBiHuAnnotation;
import com.tqmall.legend.facade.smart.result.SmartInsureCategoryDO;
import com.tqmall.legend.facade.smart.result.SmartThirdInsuredInfoBO;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zwb on 16/12/21.
 */
@Service
@Slf4j
public class BihuSmartSearchFacadeImpl implements BihuSmartSearchFacade {

    @Resource
    private RpcSmartSearchBiHuService rpcSmartSearchBiHuService;
    @Autowired
    private InsuranceFormFacade insuranceFormFacade;


    @Override
    public Result<SmartInsureCategoryDO> bihuSearch(SmartSearchParam param) {
        log.info("调用壁虎车险平台接口,param=" + JSONUtil.object2Json(param));
        Assert.notNull(param, "参数不能为空");
        Result<SmartThirdInsuredInfoDTO> biHuResult = rpcSmartSearchBiHuService.smartSearchByLicenseNoAndCityCode(param);
        log.info("壁虎返回的结果:" + JSONUtil.object2Json(biHuResult.getData()));
        if (!biHuResult.isSuccess()) {
            log.error("调用获取壁虎车险信息接口失败,错误原因:{}", biHuResult.getMessage());
            return Result.wrapErrorResult(biHuResult.getCode(), biHuResult.getMessage());
        }
        List<InsuranceCategoryVo> categoryVoList = insuranceFormFacade.getInsuranceCategory();
        SmartThirdInsuredInfoDTO smartThirdInsuredInfoDTO = biHuResult.getData();
        SmartThirdInsuredInfoBO smartThirdInsuredInfoBO = BdUtil.do2bo(smartThirdInsuredInfoDTO, SmartThirdInsuredInfoBO.class);
        for (InsuranceCategoryVo categoryVo : categoryVoList) {
            if (categoryVo.getInsuranceCategoryType() == 1) {
                Date forceDate = smartThirdInsuredInfoBO.getNextForceStartDate();
                DateTime defaultTime = DateTime.parse("1970-01-01").withTime(12, 0, 0, 0);
                if (forceDate != null && forceDate.getTime() != defaultTime.toDate().getTime()) {
                    categoryVo.setIsCheck(Boolean.TRUE);
                }
            }
            doTransfer(categoryVo, smartThirdInsuredInfoBO);
        }

        SmartInsureCategoryDO smartInsureCategoryDO = new SmartInsureCategoryDO();
        //设置车主的基本信息
        setBasicInsuranceDTO(smartInsureCategoryDO, smartThirdInsuredInfoDTO);
        smartInsureCategoryDO.setInsuranceCategoryVoList(categoryVoList);
        Result result = new Result();
        result.setData(smartInsureCategoryDO);
        result.setSuccess(biHuResult.isSuccess());
        result.setMessage(biHuResult.getMessage());
        return result;
    }

    //将壁虎返回的数据渲染到InsuranceCategoryVo对象中
    private void doTransfer(InsuranceCategoryVo categoryVo, SmartThirdInsuredInfoBO smartThirdInsuredInfoBO) {
        Class clazz = smartThirdInsuredInfoBO.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            SmartBiHuAnnotation smartBiHuAnnotation = field.getAnnotation(SmartBiHuAnnotation.class);
            //注解存在,且是相对于的字段
            if (smartBiHuAnnotation == null || !categoryVo.getInsuranceCategoryCode().equals(smartBiHuAnnotation.name())) {
                continue;
            }
            try {
                String fieldName = field.getName();
                String subFieldName = fieldName.substring(0, fieldName.length() - 3);
                char firstChar = subFieldName.charAt(0);
                subFieldName = subFieldName.replace(firstChar, Character.toUpperCase(firstChar));
                String mianFieldName = "mian" + subFieldName;
                PropertyDescriptor feeProperty = new PropertyDescriptor(fieldName, clazz);
                Method method = feeProperty.getReadMethod();
                Object result = method.invoke(smartThirdInsuredInfoBO);
                if (smartBiHuAnnotation.isDeductible()) {
                    BigDecimal resultFee = new BigDecimal(result.toString());
                    PropertyDescriptor mianProperty = new PropertyDescriptor(mianFieldName, clazz);
                    //设置是否计免赔
                    Method mianMethod = mianProperty.getReadMethod();
                    categoryVo.setIsDeductible((Boolean) mianMethod.invoke(smartThirdInsuredInfoBO));
                    if (resultFee.compareTo(BigDecimal.ZERO) != 0) {
                        categoryVo.setIsCheck(Boolean.TRUE);
                        for (InsuranceItemCoverageVo itemCoverageVo : categoryVo.getInsuranceItemCoverageVoList()) {
                            if (resultFee.compareTo(new BigDecimal(itemCoverageVo.getInsuranceItemCoverageValue())) == 0) {
                                itemCoverageVo.setIsCheck(Boolean.TRUE);
                                break;
                            }
                        }
                    }
                } else {
                    //这里对玻璃险单独处理
                    if (result != null && (int) result != 0) {
                        categoryVo.setIsCheck(Boolean.TRUE);
                        for (InsuranceItemCoverageVo itemCoverageVo : categoryVo.getInsuranceItemCoverageVoList()) {
                            if ((int) result == 1 && "2".equals(itemCoverageVo.getInsuranceItemCoverageValue())) {
                                itemCoverageVo.setIsCheck(Boolean.TRUE);
                                break;
                            }
                            if ((int) result == 2 && "1".equals(itemCoverageVo.getInsuranceItemCoverageValue())) {
                                itemCoverageVo.setIsCheck(Boolean.TRUE);
                                break;
                            }
                        }
                    }
                }
            } catch (IntrospectionException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    //设置车主的基本信息
    private void setBasicInsuranceDTO(SmartInsureCategoryDO smartInsureCategoryDO, SmartThirdInsuredInfoDTO smartThirdInsuredInfoDTO) {
        InsuranceBasicDTO insuranceBasicDTO = new InsuranceBasicDTO();
        insuranceBasicDTO.setSeatNumber(smartThirdInsuredInfoDTO.getSeatCount().toString());
        insuranceBasicDTO.setCarEngineSn(smartThirdInsuredInfoDTO.getEngineNo());
        insuranceBasicDTO.setCarFrameSn(smartThirdInsuredInfoDTO.getCarVin());
        insuranceBasicDTO.setCarEgisterDate(smartThirdInsuredInfoDTO.getRegisterDate());
        insuranceBasicDTO.setVehicleOwnerName(smartThirdInsuredInfoDTO.getLicenseOwner());
        DateTime defaultTime = DateTime.parse("1970-01-01").withTime(12, 0, 0, 0);
        DateTime now = DateTime.now().plusDays(1).withTime(0, 0, 0, 0);
        List<InsuranceFormDTO> list = new ArrayList<>();
        //设置交强险和商业险的起保时间
        InsuranceFormDTO businessForm = new InsuranceFormDTO();
        businessForm.setInsuranceType(2);
        Map<String, Date> map = DateUtil.insuranceDate(smartThirdInsuredInfoDTO.getNextBusinessStartDate());
        businessForm.setPackageStartTime(map.get("startTime"));
        businessForm.setPackageEndTime(map.get("endTime"));
        list.add(businessForm);
        InsuranceFormDTO forceForm = new InsuranceFormDTO();
        forceForm.setInsuranceType(1);
        Map<String, Date> forceMap = DateUtil.insuranceDate(smartThirdInsuredInfoDTO.getNextForceStartDate());
        forceForm.setPackageStartTime(forceMap.get("startTime"));
        forceForm.setPackageEndTime(forceMap.get("endTime"));
        list.add(forceForm);
        insuranceBasicDTO.setInsuranceFormDTOList(list);
        if (smartThirdInsuredInfoDTO.getIdType() == 1) {
            insuranceBasicDTO.setVehicleOwnerCertType("120001");
            insuranceBasicDTO.setVehicleOwnerCertTypeName("居民身份证");
            insuranceBasicDTO.setVehicleOwnerCertCode(smartThirdInsuredInfoDTO.getCredentislasNum());
        } else if (smartThirdInsuredInfoDTO.getIdType() == 3) {
            insuranceBasicDTO.setVehicleOwnerCertType("120002");
            insuranceBasicDTO.setVehicleOwnerCertTypeName("护照");
            insuranceBasicDTO.setVehicleOwnerCertCode(smartThirdInsuredInfoDTO.getCredentislasNum());
        } else if (smartThirdInsuredInfoDTO.getIdType() == 4) {
            insuranceBasicDTO.setVehicleOwnerCertType("120003");
            insuranceBasicDTO.setVehicleOwnerCertTypeName("军人证");
            insuranceBasicDTO.setVehicleOwnerCertCode(smartThirdInsuredInfoDTO.getCredentislasNum());
        }
        insuranceBasicDTO.setVehicleSn(smartThirdInsuredInfoDTO.getLicenseNo());
        smartInsureCategoryDO.setInsuranceBasicDTO(insuranceBasicDTO);
    }

}
