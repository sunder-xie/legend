package com.tqmall.legend.facade.smart;

import com.tqmall.core.common.entity.Result;
import com.tqmall.finance.model.param.pay.OfferListFormParam;
import com.tqmall.insurance.domain.result.smart.SmartRechargeRecordDTO;
import com.tqmall.insurance.domain.result.smart.SmartRechargeRuleDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by zwb on 16/12/19.
 */
public interface BihuSmartRechargeFacade {

    /**
     * 获取最小的充值金额
     * @return
     */
    Integer getMinimumRechargeFee();

    /**
     * 获取充值记录根据 rechargeNumber
     *
     * @param rechargeNumber
     * @return
     */
    SmartRechargeRecordDTO getRecordByRechargeNumber(String rechargeNumber);

    /**
     * 获取快速充值规则
     *
     * @param rechargeType
     * @return
     */
    List<SmartRechargeRuleDTO> getRechargeRule(Integer rechargeType);

    /**
     * 支付宝支付
     *
     * @return
     */
    String alipay(OfferListFormParam param);

    /**
     * 支付宝验证
     */
    void verifyAliPay(Map<String, String[]> param);

    /**
     * 申请发起快速充值
     *
     * @param agentId
     * @param rechargeRuleId
     * @return
     */
    Result<SmartRechargeRecordDTO> speedRecharge(Integer agentId, Integer rechargeRuleId);

    /**
     * 申请发起自定义充值
     *
     * @param agentId
     * @param rechargeFee
     * @return
     */
    Result<SmartRechargeRecordDTO> feeRecharge(Integer agentId, BigDecimal rechargeFee);


    Result<String> updateOrder(String rechargeNumber, Boolean flag);

}
