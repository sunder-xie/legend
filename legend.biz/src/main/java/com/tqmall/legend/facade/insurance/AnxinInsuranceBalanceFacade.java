package com.tqmall.legend.facade.insurance;

import com.tqmall.insurance.domain.param.insurance.AgentCheckAccountParam;
import com.tqmall.insurance.domain.result.common.PageEntityDTO;
import com.tqmall.legend.facade.insurance.vo.InsuranceServicePackageBalanceStatisVo;
import com.tqmall.legend.facade.insurance.vo.InsuranceServicePackageBalanceVo;

/**
 * Created by twg on 16/9/18.
 */
public interface AnxinInsuranceBalanceFacade {

    /**
     * 买保险送服务的对账
     * @param shopId
     * @return
     */
    InsuranceServicePackageBalanceStatisVo getInsuranceServicePackage(String shopId);

    /**
     * 买服务送保险的对账
     * @param shopId
     * @return
     */
    InsuranceServicePackageBalanceStatisVo getServicePackageInsurance(String shopId);

    /**
     * 买保险送服务的对账列表
     * @return
     */
    PageEntityDTO<InsuranceServicePackageBalanceVo> getInsuranceServicePackageList(AgentCheckAccountParam agentCheckAccountParam);

    /**
     * 买服务送保险的对账列表
     * @return
     */
    PageEntityDTO<InsuranceServicePackageBalanceVo> getServicePackageInsuranceList(AgentCheckAccountParam agentCheckAccountParam);



}
