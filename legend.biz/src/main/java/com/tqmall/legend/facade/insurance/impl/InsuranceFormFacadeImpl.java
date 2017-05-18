package com.tqmall.legend.facade.insurance.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.google.common.base.Function;
import com.google.common.collect.*;
import com.tqmall.common.Constants;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.BdUtil;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.finance.model.result.AccountAmountDTO;
import com.tqmall.finance.service.credit.FcAccountAmountService;
import com.tqmall.finance.service.insurance.FcInsuranceService;
import com.tqmall.insurance.domain.param.insurance.*;
import com.tqmall.insurance.domain.param.insurance.cashcoupon.SearchFormForCashCouponParam;
import com.tqmall.insurance.domain.result.*;
import com.tqmall.insurance.domain.result.common.PageEntityDTO;
import com.tqmall.insurance.service.insurance.*;
import com.tqmall.legend.biz.insurance.InsuranceCompanyService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.facade.insurance.InsuranceFormFacade;
import com.tqmall.legend.facade.insurance.vo.*;
import com.tqmall.mace.param.anxin.RpcAxUpdatePhotosParam;
import com.tqmall.mace.service.anxin.RpcAxService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zwb on 16/8/18.
 */
@Slf4j
@Service
public class InsuranceFormFacadeImpl implements InsuranceFormFacade {
    @Autowired
    InsuranceCompanyService insuranceCompanyService;
    @Autowired
    private RpcInsuranceRegionService rpcInsuranceRegionService;
    @Autowired
    private FcAccountAmountService fcAccountAmountService;
    @Autowired
    private RpcInsuranceShopConfigService rpcInsuranceShopConfigService;
    @Autowired
    private RpcVehicleInformationQueryService rpcVehicleInformationQueryService;

    /**
     * 险种
     */
    @Autowired
    private RpcInsuranceCategoryService rpcInsuranceCategoryService;
    /**
     * 车型
     */
    @Autowired
    private RpcCarModelQueryService rpcCarModelQueryService;
    @Autowired
    private RpcInsuranceBasicService rpcInsuranceBasicService;
    @Autowired
    private RpcInsuranceFormService rpcInsuranceFormService;
    @Autowired
    private RpcAxService rpcAxService;

    @Override
    public List<InsuranceRegionVo> getRegionByCode(String regionParentCode) {
        log.info("调用接口获取省市信息. 入参:{}", regionParentCode);
        com.tqmall.core.common.entity.Result<List<InsuranceRegionDTO>> result = rpcInsuranceRegionService.selectRegionByCode(regionParentCode);
        if (result == null || !result.isSuccess()) {
            log.error("调用接口获取省市信息,错误原因:{}", result.getMessage());
            throw new BizException("调用接口获取省市信息失败");
        }
        List<InsuranceRegionVo> list = Lists.newArrayList();
        if (result.getData() != null) {
            list = BdUtil.bo2do4List(result.getData(), InsuranceRegionVo.class);
        }
        return list;
    }

    @Override
    public List<InsuranceRegionVo> getRegionByCodeAndIsOpen(String regionParentCode) {
        log.info("调用接口获取省市是否开放保险. 入参:{}", regionParentCode);
        com.tqmall.core.common.entity.Result<List<InsuranceRegionDTO>> result = rpcInsuranceRegionService.selectRegionByCodeNndIsOpen(regionParentCode);
        if (result == null || !result.isSuccess()) {
            log.error("调用接口获取省市信息失败.错误原因:{}", result.getMessage());
            throw new BizException("调用接口获取省市信息失败");
        }
        List<InsuranceRegionVo> list = Lists.newArrayList();
        if (result.getData() != null) {
            list = BdUtil.bo2do4List(result.getData(), InsuranceRegionVo.class);
        }
        return list;
    }


    @Override
    public Result deleteByFormId(Integer formId) {
        if (null == formId || formId.compareTo(0) <= 0) {
            return Result.wrapErrorResult("", "参数不能为空");
        }
        log.info("调用删除保单接口. 入参:{}", formId);
        com.tqmall.core.common.entity.Result<Boolean> result = rpcInsuranceFormService.delFormById(formId);
        if ( !result.isSuccess()) {
            log.error("调用删除保单接口.错误原因:{}", result.getMessage());
            return Result.wrapErrorResult("0001", result.getMessage());
        }
        return Result.wrapSuccessfulResult(true);
    }


    @Override
    public List<InsuranceCarModelVo> getCarModel(InsuranceCarModelParam insuranceCarModelParam) {
        log.info("调用接口获取车辆信息. 入参:{}", LogUtils.objectToString(insuranceCarModelParam));
        com.tqmall.core.common.entity.Result<PageEntityDTO<CarModelDTO>> resultCarModel = rpcCarModelQueryService.carModelQuery(insuranceCarModelParam);
        List<InsuranceCarModelVo> listCarVO = Lists.newArrayList();
        if (resultCarModel == null || !resultCarModel.isSuccess()) {
            log.error("调用接口获取车辆信息失败,错误原因:{}", resultCarModel.getMessage());
            throw new BizException("调用接口获取车辆信息失败");
        }
        if (resultCarModel.getData() != null && !CollectionUtils.isEmpty(resultCarModel.getData().getRecordList())) {
            List<CarModelDTO> listCarDTO = resultCarModel.getData().getRecordList();
            for (CarModelDTO carModelDTO : listCarDTO) {
                InsuranceCarModelVo insuranceCarModelVo = BdUtil.bo2do(carModelDTO, InsuranceCarModelVo.class);
                if (carModelDTO.getVehicleList() != null) {
                    List<InsuranceVehicleVo> vehicleVoList = BdUtil.bo2do4List(carModelDTO.getVehicleList(), InsuranceVehicleVo.class);
                    insuranceCarModelVo.setVehicleList(vehicleVoList);
                }
                if (carModelDTO.getFamilyList() != null) {
                    List<InsuranceFamilyVo> FamilyList = BdUtil.bo2do4List(carModelDTO.getFamilyList(), InsuranceFamilyVo.class);
                    insuranceCarModelVo.setFamilyList(FamilyList);
                }
                if (carModelDTO.getBrandList() != null) {
                    List<InsuranceBrandVo> brandList = BdUtil.bo2do4List(carModelDTO.getBrandList(), InsuranceBrandVo.class);
                    insuranceCarModelVo.setBrandList(brandList);
                }

                listCarVO.add(insuranceCarModelVo);
            }
        }
        return listCarVO;
    }

    @Override
    public Map<Integer, String> getInsuredCertType() {
        log.info("【DUBBO】调用获取证件类型接口");
        com.tqmall.core.common.entity.Result certTypeResult = rpcInsuranceBasicService.showInsuredCertType();
        if (certTypeResult == null || !certTypeResult.isSuccess()) {
            log.error("调用接口获取证件类型失败,错误原因:{}", certTypeResult.getMessage());
            throw new BizException("调用接口获取证件类型失败");
        }
        Map<Integer, String> needMap = new HashMap<>();
        if (certTypeResult.getData() != null) {
            needMap = (Map<Integer, String>) certTypeResult.getData();
        }
        return needMap;
    }


    @Override
    public List<InsuranceCategoryVo> getInsuranceCategory() {
        log.info("【DUBBO】调用接口获取险种信息接口");
        com.tqmall.core.common.entity.Result<List<InsuranceCategoryDTO>> result = rpcInsuranceCategoryService.showInsuranceCategory();
        List<InsuranceCategoryVo> list = Lists.newArrayList();
        if (result == null || !result.isSuccess()) {
            log.error("调用接口获取险种信息失败,错误原因:{}", result.getMessage());
            throw new BizException("调用接口获取险种信息失败");
        }
        if (!CollectionUtils.isEmpty(result.getData())) {
            List<InsuranceCategoryDTO> insuranceCategoryDTOList = result.getData();
            for (InsuranceCategoryDTO insuranceCategoryDTO : insuranceCategoryDTOList) {
                InsuranceCategoryVo insuranceCategoryVo = new InsuranceCategoryVo();
                BeanUtils.copyProperties(insuranceCategoryDTO, insuranceCategoryVo);
                insuranceCategoryVo.setInsuranceItemCoverageVoList(BdUtil.bo2do4List(insuranceCategoryDTO.getInsuranceItemCoverageDTOList(), InsuranceItemCoverageVo.class));
                list.add(insuranceCategoryVo);
            }
        }
        return list;
    }

    @Override
    public List<InsuranceCategoryVo> getInsuranceCategoryHasChoose(InsuranceBasicDTO insuranceBasic) {
        List<InsuranceCategoryVo> categoryVoList = getInsuranceCategory();
        if (insuranceBasic == null) {
            return categoryVoList;
        }
        List<InsuranceFormDTO> insuranceFormList = insuranceBasic.getInsuranceFormDTOList();
        if (insuranceFormList == null) {
            return categoryVoList;
        }
        //商业险form  若商业险只选
        InsuranceFormDTO insuranceForm = null;
        //交强险
        InsuranceFormDTO insuranceFormJQX = null;

        for (int i = 0; i < insuranceFormList.size(); i++) {
            if (insuranceFormList.get(i).getInsuranceType().compareTo(2) == 0) {
                insuranceForm = insuranceFormList.get(i);
            }
            if (insuranceFormList.get(i).getInsuranceType().compareTo(1) == 0) {
                insuranceFormJQX = insuranceFormList.get(i);
            }
        }
        if (insuranceForm == null && insuranceFormJQX == null) {
            return categoryVoList;
        }
        List<InsuranceCategoryVo> needList = getCategoryListUpdated(categoryVoList, insuranceForm, insuranceFormJQX);

        return needList;
    }

    private List<InsuranceCategoryVo> getCategoryListUpdated(List<InsuranceCategoryVo> categoryVoList, InsuranceFormDTO insuranceForm, InsuranceFormDTO insuranceFormJQX) {
        //此处不用Maps.uniqueIndex方法是为有序返回
        Map<String, InsuranceCategoryVo> insuranceCategoryMap = Maps.newLinkedHashMap();
        for (InsuranceCategoryVo insuranceCategory : categoryVoList) {
            insuranceCategoryMap.put(insuranceCategory.getInsuranceCategoryCode(), insuranceCategory);
        }
        List<InsuranceItemDTO> itemList = Lists.newArrayList();
        if (insuranceForm != null) {
            itemList = insuranceForm.getItemDTOList();
        }
        if (insuranceFormJQX != null) {
            itemList.add(0, insuranceFormJQX.getItemDTOList().get(0));
        }
        List<InsuranceCategoryVo> needList = Lists.newArrayList();
        for (InsuranceItemDTO insuranceItem : itemList) {
            //获取需要赋值的险种对象
            String insuranceCategoryCode = insuranceItem.getInsuranceCategoryCode();
            InsuranceCategoryVo insuranceCategoryVo = insuranceCategoryMap.get(insuranceCategoryCode);
            if (insuranceCategoryVo == null) {
                continue;
            }
            //如果勾选了不计免赔
            if (insuranceItem.getIsDeductible().compareTo(0) == 0) {
                insuranceCategoryVo.setIsDeductible(true);
            }
            insuranceCategoryVo.setIsCheck(true);
            String coverageValueDisplay = insuranceItem.getItemCoverageValueDisplay();
            //如果选择具体险别种类
            if (coverageValueDisplay != null) {
                List<InsuranceItemCoverageVo> itemCoverageList = insuranceCategoryVo.getInsuranceItemCoverageVoList();
                for (InsuranceItemCoverageVo itemCoverage : itemCoverageList) {
                    if (itemCoverage.getInsuranceItemCoverageDisplay().equals(coverageValueDisplay)) {
                        itemCoverage.setIsCheck(true);
                        itemCoverage.setInsuranceCategoryId(insuranceItem.getInsuranceItemCoverageId());
                        itemCoverage.setInsuranceCategoryCode(insuranceItem.getInsuranceCategoryCode());
                        itemCoverage.setInsuranceItemCoverageDisplay(coverageValueDisplay);
                        itemCoverage.setInsuranceItemCoverageValue(insuranceItem.getInsuranceItemCoverageValue());
                    }
                }
            }
        }
        for (Map.Entry<String, InsuranceCategoryVo> m : insuranceCategoryMap.entrySet()) {
            needList.add(m.getValue());
        }
        return needList;
    }


    @Override
    public Result<InsuranceCalculateDTO> insuranceFeeCalculate(InsuranceCalculateParam insuranceCalculateParam) {
        log.info("【DUBBO】调用保费计算接口,入参:{}", LogUtils.objectToString(insuranceCalculateParam));
        com.tqmall.core.common.entity.Result<InsuranceCalculateDTO> result = rpcInsuranceFormService.insuranceFeeCalculate(insuranceCalculateParam);
        if (!result.isSuccess()) {
            log.error("调用保费计算接口失败，原因{}", result.getMessage());
            return Result.wrapErrorResult("", result.getMessage());
        }
        return Result.wrapSuccessfulResult(result.getData());
    }


    @Override
    public Result<InsuranceCalculateDTO> repeatFeeCalculate(Integer basicId) {
        if (null == basicId || basicId.compareTo(0) <= 0) {
            log.error("重新计算参数错误. basicId:{}", basicId);
            return Result.wrapErrorResult("", "重新计算参数错误");
        }
        log.info("【DUBBO】调用重新计算保费接口,参数:{}", basicId);
        com.tqmall.core.common.entity.Result<InsuranceCalculateDTO> result = rpcInsuranceFormService.repeatFeeCalculate(basicId);
        if (!result.isSuccess()) {
            log.error("调用重新计算接口参数错误，错误原因{}", result.getMessage());
            return Result.wrapErrorResult("", result.getMessage());
        }
        return Result.wrapSuccessfulResult(result.getData());
    }


    @Override
    public Result<InsuranceBasicDTO> updateInsuranceFee(InsuranceCalculateParam insuranceCalculateParam) {
        log.info("【DUBBO】调用奖励金保费提交接口，入参:{}", LogUtils.objectToString(insuranceCalculateParam));
        com.tqmall.core.common.entity.Result<InsuranceBasicDTO> result = rpcInsuranceFormService.updateInsuranceFee(insuranceCalculateParam);
        if (!result.isSuccess() || result.getData() == null) {
            log.error("调用奖励金保费提交接口失败,原因{}", result.getMessage());
            return Result.wrapErrorResult("", result.getMessage());
        }
        return Result.wrapSuccessfulResult(result.getData());
    }

    @Override
    public Result<InsuranceBasicDTO> updateInsuranceFeePackage(InsuranceCalculateParam insuranceCalculateParam) {
        com.tqmall.core.common.entity.Result<InsuranceBasicDTO> result = rpcInsuranceFormService.updateInsurancePackage(insuranceCalculateParam);
        if (result == null || !result.isSuccess() || result.getData() == null) {
            log.error("调用服务包保费提交接口失败，InsuranceCalculateParam:{},原因{}", LogUtils.objectToString(insuranceCalculateParam), result.getMessage());
            return Result.wrapErrorResult("", result.getMessage());
        }
        return Result.wrapSuccessfulResult(result.getData());
    }

    @Override
    public Result<PageEntityDTO<InsuranceFormVo>> getInsuranceList(InsuranceBasicParam insuranceBasicParam) {
        com.tqmall.core.common.entity.Result<PageEntityDTO<InsuranceFormDTO>> result = rpcInsuranceFormService.selectInsuranceByNameAndVehicleSn(insuranceBasicParam);
        if (result == null || !result.isSuccess()) {
            log.error("调用获取保单投保单信息接口失败. InsuranceBasicParam:{},原因{}", LogUtils.objectToString(insuranceBasicParam), result.getMessage());
            return Result.wrapErrorResult("", "调用获取保单投保单信息接口失败," + result.getMessage());
        }
        PageEntityDTO<InsuranceFormVo> pageEntity = new <InsuranceFormVo>PageEntityDTO();
        List<InsuranceFormVo> list = Lists.newArrayList();
        if (result.getData() != null && !CollectionUtils.isEmpty(result.getData().getRecordList())) {
            pageEntity.setPageNum(result.getData().getPageNum());
            List<InsuranceFormVo> lists = transFormList(result.getData().getRecordList());
            pageEntity.setRecordList(lists);
            pageEntity.setTotalNum(result.getData().getTotalNum());
        } else {
            pageEntity.setPageNum(1);
            pageEntity.setRecordList(list);
            pageEntity.setTotalNum(0);
        }
        return Result.wrapSuccessfulResult(pageEntity);
    }


    @Override
    public Result<PageEntityDTO<InsuranceFormVo>> getApplyNoInsuranceList(InsuranceBasicParam insuranceBasicParam) {
        log.info("【DUBBO】调用获取投保单信息接口,参数:{}", LogUtils.objectToString(insuranceBasicParam));
        com.tqmall.core.common.entity.Result<PageEntityDTO<InsuranceFormDTO>> result = rpcInsuranceFormService.selectApplyNoByNameAndVehicleSn(insuranceBasicParam);
        if (result == null || !result.isSuccess()) {
            log.error("【DUBBO】调用获取投保单信息接口失败,错误原因:{}", result.getMessage());
            return Result.wrapErrorResult("", "调用获取投保单信息接口失败," + result.getMessage());
        }
        PageEntityDTO<InsuranceFormVo> pageEntity = new <InsuranceFormVo>PageEntityDTO();
        List<InsuranceFormVo> list = Lists.newArrayList();
        if (result.getData() != null && !CollectionUtils.isEmpty(result.getData().getRecordList())) {
            pageEntity.setPageNum(result.getData().getPageNum());
            List<InsuranceFormVo> lists = transFormList(result.getData().getRecordList());
            setIsUploadImg(lists);
            pageEntity.setRecordList(lists);
            pageEntity.setTotalNum(result.getData().getTotalNum());
        } else {
            pageEntity.setPageNum(1);
            pageEntity.setRecordList(list);
            pageEntity.setTotalNum(0);
        }
        return Result.wrapSuccessfulResult(pageEntity);
    }

    @Override
    public Result<InsuranceFormVo> getInsuranceFormDetail(Integer id, Integer agentId) {
        log.info("调用获取保单详情接口. id:{},agentId:{}", id, agentId);
        com.tqmall.core.common.entity.Result<InsuranceFormDTO> result = rpcInsuranceFormService.getInsuranceFormDetail(id, agentId);
        if (result == null || !result.isSuccess()) {
            log.error("调用获取保单详情接口失败,错误原因{}", result.getMessage());
            return Result.wrapErrorResult("", "调用获取保单详情接口失败,原因" + result.getMessage());
        }
        InsuranceFormVo insuranceFormVo = transForm(result.getData());
        return Result.wrapSuccessfulResult(insuranceFormVo);

    }

    @Override
    public InsuranceBasicDTO backUpdateInfo(Integer basicId, Integer agentId) {
        log.info("【DUBBO】调用安心保险获取返回修改接口,投保单号:{},shopId:{}", basicId, agentId);
        com.tqmall.core.common.entity.Result<InsuranceBasicDTO> result = rpcInsuranceFormService.getInsuranceFormInfoByBasicId(basicId, agentId);
        if (!result.isSuccess()) {
            log.error("【DUBBO】调用安心保险获取返回修改接口,错误原因:{}", LogUtils.objectToString(result));
            throw new BizException("获取详情失败");
        }
        return result.getData();
    }

    @Override
    public PageEntityDTO<InsuranceFormVo> getNotEffectiveReward(InsuranceFormParam insuranceFormParam) {
        PageEntityDTO<InsuranceFormVo> pageEntity = new <InsuranceFormVo>PageEntityDTO();
        List<InsuranceFormVo> list = Lists.newArrayList();
        pageEntity.setPageNum(1);
        pageEntity.setRecordList(list);
        pageEntity.setTotalNum(0);

        try {
            log.error("【DUBBO】调用接口获取待生效奖励金. InsuranceFormParam:{}", LogUtils.objectToString(insuranceFormParam));
            com.tqmall.core.common.entity.Result<PageEntityDTO<InsuranceFormDTO>> result = rpcInsuranceFormService.selectRewardFeeByEffectivedStatus(insuranceFormParam);
            if (!result.isSuccess()) {
                log.error("【DUBBO】调用接口获取待生效奖励金失败,错误原因:{}", result.getMessage());
            }
            if (result.getData() != null && !CollectionUtils.isEmpty(result.getData().getRecordList())) {
                pageEntity.setPageNum(result.getData().getPageNum());
                pageEntity.setRecordList(transFormList(result.getData().getRecordList()));
                pageEntity.setTotalNum(result.getData().getTotalNum());
            }
        } catch (Exception e) {
            log.error("调用接口获取待生效奖励金发生异常，异常信息：", e);
        }
        return pageEntity;
    }


    @Override
    public InsuranceAccountAmountVo getSurplusReward(final String source, final Integer shopId) {
        InsuranceAccountAmountVo insuranceAccountAmountVo = new InsuranceAccountAmountVo();
        try {
            log.info("调用账务获取奖励金余额接口. source:{}，门店userGlobalId:{}", source, shopId);
            com.tqmall.core.common.entity.Result<AccountAmountDTO> result = fcAccountAmountService.getAccountInsuranceRewardAmount(source, shopId);
            if (result == null || !result.isSuccess()) {
                log.error("调用账务获取奖励金余额接口失败,错误原因:{}", result.getMessage());
            } else if (result.getData() != null) {
                insuranceAccountAmountVo = BdUtil.bo2do(result.getData(), InsuranceAccountAmountVo.class);
            }
        } catch (Exception e) {
            log.error("根据source = {}，门店userGlobalId = {}，调用账务获取奖励金余额接口发生异常，异常信息：", source, shopId, e);
        }
        return insuranceAccountAmountVo;
    }

    @Override
    public VehicleInformationDTO getVehicleInformation(final String carNo, final String ownerName) {
        log.info("【DUBBO】调用安心保险获取车辆基本信息接口,carNo:{},ownerName:{}", carNo, ownerName);
        VehicleInformationDTO vehicleInformationDTO = null;
        try {
            com.tqmall.core.common.entity.Result<VehicleInformationDTO> result = rpcVehicleInformationQueryService.vehicleInformationQuery(carNo, ownerName);
            if (result == null || !result.isSuccess()) {
                log.error("调用安心保险获取车辆基本信息接口失败. carNo = {}，ownerName={}", carNo, ownerName);
            } else {
                vehicleInformationDTO = result.getData();
            }
        } catch (Exception e) {
            log.error("调用安心保险获取车辆基本信息接口失败，异常信息：", e);
        }
        return vehicleInformationDTO;
    }

    @Override
    public CheckAcountDTO getWaitEffectReward(final Integer agentId) {
        try {
            log.info("【DUBBO】调用Insurance获取待生效奖励金余额接口. agentId:{}", agentId);
            com.tqmall.core.common.entity.Result<CheckAcountDTO> result = rpcInsuranceFormService.selectRemainRewardFeeByEffectivedStatus(agentId);
            if (!result.isSuccess()) {
                log.error("调用Insurance获取待生效奖励金余额接口失败,错误原因:{}", result.getMessage());
            } else if (result.getData() != null) {
                return result.getData();
            }
        } catch (Exception e) {
            log.error("根据门店userGlobalId = {}，调用Insurance获取待生效奖励金余额接口发生异常，异常信息：", agentId, e);
        }
        return new CheckAcountDTO();
    }

    private List<InsuranceFormVo> transFormList(List<InsuranceFormDTO> listFormDTO) {
        if (CollectionUtils.isEmpty(listFormDTO)) {
            return Lists.newArrayList();
        }
        List<InsuranceFormVo> listFormVO = Lists.newArrayList();
        for (InsuranceFormDTO insuranceFormDTO : listFormDTO) {
            InsuranceFormVo insuranceFormVo = BdUtil.bo2do(insuranceFormDTO, InsuranceFormVo.class);
            if (insuranceFormDTO.getItemDTOList() != null) {
                List<InsuranceItemVo> itemVoList = BdUtil.bo2do4List(insuranceFormDTO.getItemDTOList(), InsuranceItemVo.class);
                insuranceFormVo.setItemVoList(itemVoList);
            }
            if (insuranceFormDTO.getBasicDTO() != null) {
                InsuranceBasicVo insuranceBasicVo = BdUtil.bo2do(insuranceFormDTO.getBasicDTO(), InsuranceBasicVo.class);
                insuranceFormVo.setBasicVo(insuranceBasicVo);
                insuranceFormVo.setOrderSn(insuranceFormDTO.getBasicDTO().getInsuranceOrderSn());
            }
            listFormVO.add(insuranceFormVo);
        }
        return listFormVO;
    }

    private InsuranceFormVo transForm(InsuranceFormDTO insuranceFormDTO) {
        if (insuranceFormDTO == null) {
            return new InsuranceFormVo();
        }
        InsuranceFormVo insuranceFormVo = BdUtil.bo2do(insuranceFormDTO, InsuranceFormVo.class);
        if (insuranceFormDTO.getItemDTOList() != null) {
            List<InsuranceItemVo> itemVoList = BdUtil.bo2do4List(insuranceFormDTO.getItemDTOList(), InsuranceItemVo.class);
            insuranceFormVo.setItemVoList(itemVoList);
        }
        if (insuranceFormDTO.getBasicDTO() != null) {
            InsuranceBasicVo insuranceBasicVo = BdUtil.bo2do(insuranceFormDTO.getBasicDTO(), InsuranceBasicVo.class);
            insuranceFormVo.setBasicVo(insuranceBasicVo);
        }
        insuranceFormVo.setOrderSn(insuranceFormDTO.getBasicDTO().getInsuranceOrderSn());
        return insuranceFormVo;
    }

    /**
     * 获取投保单状态
     *
     * @return
     */
    @Override
    public Map<Integer, String> getInsureStatus() {
        com.tqmall.core.common.entity.Result insureStatusResult = rpcInsuranceBasicService.showInsureStatus();
        if (!insureStatusResult.isSuccess()) {
            throw new BizException("调用接口获取投保单状态失败");
        }
        Map<Integer, String> map = new HashMap<>();
        if (insureStatusResult.getData() != null) {
            map = (Map<Integer, String>) insureStatusResult.getData();
        }
        return map;
    }

    /**
     * 获取保单状态
     *
     * @return
     */
    @Override
    public Map<Integer, String> getQuitStatus() {
        com.tqmall.core.common.entity.Result quitStatusResult = rpcInsuranceBasicService.showQuitStatus();
        if (!quitStatusResult.isSuccess() || quitStatusResult.getData() == null) {
            throw new BizException("调用接口获取保单状态失败");
        }
        Map<Integer, String> map = new HashMap<>();
        if (quitStatusResult.getData() != null) {
            map = (Map<Integer, String>) quitStatusResult.getData();
        }
        return map;
    }

    @Override
    public BonusAllocationRatioVo selectRatio(Integer ucShopId) {
        BonusAllocationRatioVo ratioVo = new BonusAllocationRatioVo();
        log.info("[DUBBO]调用insurance获取奖励金分配比例接口,门店ucShopId:{}", ucShopId);
        com.tqmall.core.common.entity.Result<InsuranceShopCommissionsRatioDTO> result = rpcInsuranceShopConfigService.getShopCommissionsRatio(ucShopId);
        if (!result.isSuccess()) {
            log.error("[DUBBO]调用insurance获取奖励金分配比例失败,错误原因:{}", LogUtils.objectToString(result));
            return ratioVo;
        }
        BeanUtils.copyProperties(result.getData(), ratioVo);
        return ratioVo;
    }


    @Override
    public boolean uploadIdentityInfo(IdentityInfoParam param) {
        log.info("[DUBBO]调用insurance上传身份信息,参数:{}", LogUtils.objectToString(param));
        com.tqmall.core.common.entity.Result<Boolean> result = rpcInsuranceFormService.uploadIdentityInfoAndGetValidateCode(param);
        if (!result.isSuccess()) {
            log.error("[DUBBO]调用insurance上传身份信息失败,错误原因:{}", LogUtils.objectToString(result));
            return false;
        }
        return result.getData();
    }

    @Override
    public boolean uploadImgToAnxin(String orderSn, String urls, UserInfo userInfo) {
        if (StringUtils.isBlank(urls) || urls.length() < 10) {
            throw new BizException("图片上传失败");
        }
        RpcAxUpdatePhotosParam param = new RpcAxUpdatePhotosParam();
        String[] url = urls.split(";");
        param.setShopId(userInfo.getShopId());
        param.setSource(Constants.CUST_SOURCE);
        param.setCarDrivingLicense(url[0]);
        param.setCarCredentials(url[1]);
        param.setCarPicture(url[2]);
        param.setInsuranceOrderSn(orderSn);
        log.info("【dubbo】调用mace上传证件照:{}", LogUtils.objectToString(param));
        com.tqmall.core.common.entity.Result<Boolean> result = rpcAxService.updatePhotoToAx(param);
        if (!result.isSuccess()) {
            log.info("【dubbo】调用mace上传证件照失败,错误原因:{}", LogUtils.objectToString(result));
            return false;
        }
        if (result.getData() == null) {
            return false;
        }
        return result.getData();

    }

    @Override
    public InsuranceFeeDTO selectPayInfoByOrderSn(Integer ucShopId, String orderSn) {
        if (StringUtils.isBlank(orderSn)) {
            return null;
        }
        log.info("【dubbo】调用insurance获取真实投保支付信息,orderSn:{}", orderSn);
        com.tqmall.core.common.entity.Result<InsuranceFeeDTO> result = rpcInsuranceFormService.getInsuredFeeInfoByOrderSn(ucShopId, orderSn);
        if (!result.isSuccess()) {
            log.info("【dubbo】调用insurance获取真实投保支付信息失败,错误原因:{}", LogUtils.objectToString(result));
            return null;
        }
        return result.getData();
    }

    private void setIsUploadImg(List<InsuranceFormVo> insuranceFormVoList) {
        Multimap<Integer, InsuranceFormVo> multimap = Multimaps.index(insuranceFormVoList, new Function<InsuranceFormVo, Integer>() {
            @Override
            public Integer apply(InsuranceFormVo input) {
                return input.getInsuranceBasicId();
            }
        });
        Set<Integer> basicIdSet = Sets.newHashSet();
        for (InsuranceFormVo insuranceFormVo : insuranceFormVoList) {
            basicIdSet.add(insuranceFormVo.getInsuranceBasicId());
        }
        for (Integer basicId : basicIdSet) {
            boolean isUpload = true;
            for (InsuranceFormVo formVo : multimap.get(basicId)) {
                Integer isInsured = 0; //未脱保
                if (isInsured.equals(formVo.getHasInsuranced())) {
                    isUpload = false;
                }
            }
            //设置是否上传状态
            for (InsuranceFormVo vo : insuranceFormVoList) {
                if (basicId.equals(vo.getInsuranceBasicId())) {
                    vo.setUploadImg(isUpload);
                }
            }
        }
    }


    @Override
    public Result<List<InsuranceCashCouponFormDTO>> getInsuranceFormForCashCoupon(SearchFormForCashCouponParam param) {
        log.info("【DUBBO】调用获取优惠券适用保单接口，入参:{}", LogUtils.objectToString(param));
        com.tqmall.core.common.entity.Result<List<InsuranceCashCouponFormDTO>> result = rpcInsuranceFormService.getCanUseCashCouponForm(param);
        if (!result.isSuccess() || result.getData() == null) {
            log.error("调用获取优惠券适用保单接口失败,原因{}", result.getMessage());
            return  Result.wrapErrorResult("","调用获取现金券适用保单接口失败,原因"+result.getMessage());

        }
        return Result.wrapSuccessfulResult(result.getData());
    }




}
