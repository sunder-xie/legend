package com.tqmall.legend.facade.insurance.impl;

import com.google.common.collect.Lists;
import com.tqmall.common.util.BdUtil;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.common.entity.Result;
import com.tqmall.insurance.domain.param.insurance.AgentCheckAccountParam;
import com.tqmall.insurance.domain.result.InsuranceCashAccountDTO;
import com.tqmall.insurance.domain.result.InsuranceFormAccountDTO;
import com.tqmall.insurance.domain.result.InsuranceServicePackageAccountDTO;
import com.tqmall.insurance.domain.result.InsuranceToServiceAccountDTO;
import com.tqmall.insurance.domain.result.ServiceToInsuranceAccountDTO;
import com.tqmall.insurance.domain.result.common.PageEntityDTO;
import com.tqmall.insurance.service.insurance.RpcInsuranceToServiceAccountService;
import com.tqmall.insurance.service.insurance.RpcServiceToInsuranceAccountService;
import com.tqmall.legend.facade.insurance.AnxinInsuranceBalanceFacade;
import com.tqmall.legend.facade.insurance.vo.InsuranceServicePackageBalanceStatisVo;
import com.tqmall.legend.facade.insurance.vo.InsuranceServicePackageBalanceVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by twg on 16/9/18.
 */
@Slf4j
@Service
public class AnxinInsuranceBalanceFacadeImpl implements AnxinInsuranceBalanceFacade {
    @Autowired
    private RpcInsuranceToServiceAccountService rpcInsuranceToServiceAccountService;
    @Autowired
    private RpcServiceToInsuranceAccountService rpcServiceToInsuranceAccountService;

    @Override
    public InsuranceServicePackageBalanceStatisVo getInsuranceServicePackage(String shopId) {
        Integer shopid = Integer.valueOf(shopId);
        InsuranceServicePackageBalanceStatisVo insuranceServicePackageVo = new InsuranceServicePackageBalanceStatisVo();

        try{
            Result<InsuranceCashAccountDTO> insuranceCashAccountResult = rpcInsuranceToServiceAccountService.checkCashAccount(shopid);
            if(insuranceCashAccountResult == null || !insuranceCashAccountResult.isSuccess()){
                log.error("调用获取买保险送服务的现金对账接口失败. 门店id = {}", shopid);
            }else{
                setInsurancCashInfo(insuranceServicePackageVo, insuranceCashAccountResult);
            }

            Result<InsuranceServicePackageAccountDTO> insuranceServicePackageAccountResult = rpcInsuranceToServiceAccountService.checkServicePackageAccount(shopid);
            if(insuranceServicePackageAccountResult == null || !insuranceServicePackageAccountResult.isSuccess()){
                log.error("调用获取买保险送服务的服务包对账接口失败. 门店id = {}", shopid);
            }else {
                setInsuranceServicePackageInfo(insuranceServicePackageVo, insuranceServicePackageAccountResult);
            }

            Result<InsuranceFormAccountDTO> insuranceFormAccountResult = rpcInsuranceToServiceAccountService.checkFormAccount(shopid);
            if(insuranceFormAccountResult == null || !insuranceFormAccountResult.isSuccess()){
                log.error("调用获取买保险送服务的保单对账接口失败. 门店id = {}", shopid);
            }else {
                setInsuranceOrderInfo(insuranceServicePackageVo, insuranceFormAccountResult);
            }
        }catch (Exception e){
            log.error("调用获取买保险送服务接口发生异常，异常信息：",e);
        }

        return insuranceServicePackageVo;
    }

    @Override
    public PageEntityDTO<InsuranceServicePackageBalanceVo> getInsuranceServicePackageList(AgentCheckAccountParam agentCheckAccountParam) {
        PageEntityDTO<InsuranceServicePackageBalanceVo> pageEntity = getInsuranceServicePackageBalanceVoPageEntityDTO();
        try {
            Result<PageEntityDTO<InsuranceToServiceAccountDTO>> result = rpcInsuranceToServiceAccountService.selectPackageDetailForInsureService(agentCheckAccountParam);
            if(result == null || !result.isSuccess() || result.getData() == null || CollectionUtils.isEmpty(result.getData().getRecordList())){
                log.error("调用获取买保险送服务的列表查询接口失败. agentCheckAccountParam : {}", LogUtils.objectToString(agentCheckAccountParam));
            }else {
                pageEntity.setPageNum(result.getData().getPageNum());
                pageEntity.setRecordList(transFormList(result.getData().getRecordList()));
                pageEntity.setTotalNum(result.getData().getTotalNum());
            }
        }catch (Exception e){
            log.error("调用获取买保险送服务的列表查询接口异常，异常信息：", e);
        }
        return pageEntity;
    }

    @Override
    public PageEntityDTO<InsuranceServicePackageBalanceVo> getServicePackageInsuranceList(AgentCheckAccountParam agentCheckAccountParam) {
        PageEntityDTO<InsuranceServicePackageBalanceVo> pageEntity = getInsuranceServicePackageBalanceVoPageEntityDTO();
        try {
            Result<PageEntityDTO<ServiceToInsuranceAccountDTO>> result = rpcServiceToInsuranceAccountService.selectPackageDetailForServiceInsure(agentCheckAccountParam);
            if(result == null || !result.isSuccess() || result.getData() == null || CollectionUtils.isEmpty(result.getData().getRecordList())){
                log.error("调用获取买服务送保险的列表查询接口失败. agentCheckAccountParam : {}", LogUtils.objectToString(agentCheckAccountParam));
            }else {
                pageEntity.setPageNum(result.getData().getPageNum());
                pageEntity.setRecordList(transFormList(result.getData().getRecordList()));
                pageEntity.setTotalNum(result.getData().getTotalNum());
            }
        }catch (Exception e){
            log.error("调用获取买服务送保险的列表查询接口异常，异常信息：", e);
        }

        return pageEntity;
    }

    @Override
    public InsuranceServicePackageBalanceStatisVo getServicePackageInsurance(String shopId) {
        Integer shopid = Integer.valueOf(shopId);
        InsuranceServicePackageBalanceStatisVo insuranceServicePackageVo = new InsuranceServicePackageBalanceStatisVo();

        try {
            Result<InsuranceCashAccountDTO> insuranceCashAccountResult = rpcServiceToInsuranceAccountService.checkCashAccount(shopid);
            if(insuranceCashAccountResult == null || !insuranceCashAccountResult.isSuccess()){
                log.error("调用获取买服务送保险的现金对账接口失败. 门店id = {}", shopid);
            }else{
                setInsurancCashInfo(insuranceServicePackageVo, insuranceCashAccountResult);
            }
            Result<InsuranceServicePackageAccountDTO> insuranceServicePackageAccountResult = rpcServiceToInsuranceAccountService.checkServicePackageAccount(shopid);
            if(insuranceServicePackageAccountResult == null || !insuranceServicePackageAccountResult.isSuccess()){
                log.error("调用获取买服务送保险的服务包对账接口失败. 门店id = {}", shopid);
            }else {
                setInsuranceServicePackageInfo(insuranceServicePackageVo, insuranceServicePackageAccountResult);
            }

            Result<InsuranceFormAccountDTO> insuranceFormAccountResult = rpcServiceToInsuranceAccountService.checkFormAccount(shopid);
            if(insuranceFormAccountResult == null || !insuranceFormAccountResult.isSuccess()){
                log.error("调用获取买服务送保险的保单对账接口失败. 门店id = {}", shopid);
            }else {
                setInsuranceOrderInfo(insuranceServicePackageVo, insuranceFormAccountResult);
            }
        }catch (Exception e){
            log.error("调用获取买服务送保险接口发生异常，异常信息：",e);
        }

        return insuranceServicePackageVo;
    }


    private void setInsuranceOrderInfo(InsuranceServicePackageBalanceStatisVo insuranceServicePackageVo, Result<InsuranceFormAccountDTO> insuranceFormAccountResult) {
       if (insuranceFormAccountResult.isSuccess() && insuranceFormAccountResult.getData() != null) {
            InsuranceFormAccountDTO insuranceFormAccountDTO = insuranceFormAccountResult.getData();
            insuranceServicePackageVo.setOrderDsxCount(insuranceFormAccountDTO.getDsxCount());
            insuranceServicePackageVo.setOrderMonthCount(insuranceFormAccountDTO.getMonthCount());
            insuranceServicePackageVo.setOrderTotalCount(insuranceFormAccountDTO.getTotalCount());
            insuranceServicePackageVo.setOrderYsxCount(insuranceFormAccountDTO.getYsxCount());
        }
    }

    private void setInsuranceServicePackageInfo(InsuranceServicePackageBalanceStatisVo insuranceServicePackageVo, Result<InsuranceServicePackageAccountDTO> insuranceServicePackageAccountResult) {
        if (insuranceServicePackageAccountResult.isSuccess() && insuranceServicePackageAccountResult.getData() != null) {
            InsuranceServicePackageAccountDTO insuranceServicePackageAccountDTO = insuranceServicePackageAccountResult.getData();
            insuranceServicePackageVo.setPackageDfhCount(insuranceServicePackageAccountDTO.getDfhCount());
            insuranceServicePackageVo.setPackageDsxCount(insuranceServicePackageAccountDTO.getDsxCount());
            insuranceServicePackageVo.setPackageMonthCount(insuranceServicePackageAccountDTO.getMonthCount());
            insuranceServicePackageVo.setPackagePszCount(insuranceServicePackageAccountDTO.getPszCount());
            insuranceServicePackageVo.setPackageTotalCount(insuranceServicePackageAccountDTO.getTotalCount());
            insuranceServicePackageVo.setPackageYqsCount(insuranceServicePackageAccountDTO.getYqsCount());
        }
    }

    private void setInsurancCashInfo(InsuranceServicePackageBalanceStatisVo insuranceServicePackageVo, Result<InsuranceCashAccountDTO> insuranceCashAccountResult) {
        if (insuranceCashAccountResult.isSuccess() && insuranceCashAccountResult.getData() != null) {
            InsuranceCashAccountDTO insuranceCashAccountDTO = insuranceCashAccountResult.getData();
            insuranceServicePackageVo.setCashMonthAmount(insuranceCashAccountDTO.getMonthAmount());
            insuranceServicePackageVo.setCashTotalAmount(insuranceCashAccountDTO.getTotalAmount());
            insuranceServicePackageVo.setCashMonthWithdrawAmount(insuranceCashAccountDTO.getMonthWithdrawAmount());
        }
    }

    private List<InsuranceServicePackageBalanceVo> transFormList(List<?> listFormDTO) {
        List<InsuranceServicePackageBalanceVo> listFormVO = Lists.newArrayList();
        if (CollectionUtils.isEmpty(listFormDTO)) {
            return listFormVO;
        }
        for (Object insuranceFormDTO : listFormDTO) {
            InsuranceServicePackageBalanceVo insuranceFormVo = BdUtil.bo2do(insuranceFormDTO, InsuranceServicePackageBalanceVo.class);
            listFormVO.add(insuranceFormVo);
        }
        return listFormVO;
    }

    private PageEntityDTO<InsuranceServicePackageBalanceVo> getInsuranceServicePackageBalanceVoPageEntityDTO() {
        PageEntityDTO<InsuranceServicePackageBalanceVo> pageEntity = new <InsuranceServicePackageBalanceVo>PageEntityDTO();
        List<InsuranceServicePackageBalanceVo> list = Lists.newArrayList();
        pageEntity.setPageNum(1);
        pageEntity.setRecordList(list);
        pageEntity.setTotalNum(0);
        return pageEntity;
    }
}
