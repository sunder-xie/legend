package com.tqmall.legend.facade.smart.impl;

import com.tqmall.common.Constants;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.common.entity.Result;
import com.tqmall.finance.model.param.pay.OfferListFormParam;
import com.tqmall.finance.service.pay.WebPayService;
import com.tqmall.insurance.domain.result.smart.SmartRechargeRecordDTO;
import com.tqmall.insurance.domain.result.smart.SmartRechargeRuleDTO;
import com.tqmall.insurance.service.smart.RpcSmartRechargeService;
import com.tqmall.legend.facade.smart.BihuSmartRechargeFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by zwb on 16/12/19.
 */
@Service
@Slf4j
public class BihuSmartRechargeFacadeImpl implements BihuSmartRechargeFacade {
    @Resource
    private WebPayService webPayService;
    @Resource
    private RpcSmartRechargeService rpcSmartRechargeService;

    @Override
    public SmartRechargeRecordDTO getRecordByRechargeNumber(String rechargeNumber) {
        Assert.notNull(rechargeNumber, "请传入订单流水号");
        log.info("[DUBBO]调用壁虎车险获取充值记录,充值流水号:{},rechargeNumber:{}", rechargeNumber);
        Result<SmartRechargeRecordDTO> result = rpcSmartRechargeService.getRecordByRechargeNumber(rechargeNumber);
        if (!result.isSuccess()) {
            log.error("[DUBBO]调用壁虎车险获取充值记录失败,错误原因:{}", LogUtils.objectToString(result));
            throw new BizException("获取充值记录失败");
        }
        return result.getData();

    }

    @Override
    public Integer getMinimumRechargeFee() {
        log.info("[DUBBO]调用壁虎车险获取最小充值金额");
        Result<Integer> result= rpcSmartRechargeService.getMinimumRechargeFee();
        if (!result.isSuccess()) {
            log.error("[DUBBO]调用壁虎车险获取最小充值金额失败,错误原因:{}", LogUtils.objectToString(result));
            throw new BizException("获取最小充值金额失败");
        }
        return result.getData();

    }

    @Override
    public List<SmartRechargeRuleDTO> getRechargeRule(Integer rechargeType) {
        Assert.notNull(rechargeType, "请指定需要查询充值规则的类型");
        log.info("[DUBBO]调用获取充值规则接口. 入参:rechargeType={}", rechargeType);
        Result<List<SmartRechargeRuleDTO>> result = rpcSmartRechargeService.getRechargeRuleList(rechargeType);
        if (!result.isSuccess()) {
            log.error("[DUBBO]调用获取充值规则接口失败，原因{}", result.getMessage());
            throw new BizException("调用获取充值规则接口失败");
        }
        return result.getData();
    }

    @Override
    public Result<SmartRechargeRecordDTO> speedRecharge(Integer agentId, Integer rechargeRuleId) {
        Assert.notNull(agentId, "请传门店ID");
        Assert.notNull(rechargeRuleId, "请选择冲充值规则");
        log.info("[DUBBO]调用快速充值接口. 入参:agentId={},rechargeRuleId={}", agentId,rechargeRuleId);
        Result<SmartRechargeRecordDTO> result = rpcSmartRechargeService.speedRecharge(agentId, rechargeRuleId);
        if (!result.isSuccess()) {
            log.error("[DUBBO]调用快速充值接口失败，原因{}", result.getMessage());
            return Result.wrapErrorResult("", result.getMessage());
        }
        return result;
    }

    @Override
    public Result<String> updateOrder(String rechargeNumber, Boolean flag) {
        Assert.notNull(rechargeNumber, "请传入充值订单号");
        Assert.notNull(flag, "请传入充值结果");
        log.info("[DUBBO]调用修改订单状态接口,rechargeNumber:{},flag:{}", rechargeNumber, flag);
        Result<String> stringResult;
        if (flag) {
            stringResult = rpcSmartRechargeService.upBillStatusPaySuccess(rechargeNumber);
        } else {
            stringResult = rpcSmartRechargeService.upBillStatusPayFail(rechargeNumber);
        }
        if (!stringResult.isSuccess()) {
            log.error("[DUBBO]调用修改订单状态接口失败，原因{}", stringResult.getMessage());
            return Result.wrapErrorResult("", stringResult.getMessage());
        }
        return stringResult;
    }

    @Override
    public Result<SmartRechargeRecordDTO> feeRecharge(Integer agentId, BigDecimal rechargeFee) {
        Assert.notNull(agentId, "请传门店ID");
        Assert.notNull(rechargeFee, "请传入充值金额");
        log.info("[DUBBO]调用自定义充值接口. 入参:agentId={},rechargeFee={}", agentId,rechargeFee);
        Result<SmartRechargeRecordDTO> result = rpcSmartRechargeService.feeRecharge(agentId, rechargeFee);
        if (!result.isSuccess()) {
            log.error("[DUBBO]调用自定义充值接口失败，原因{}", result.getMessage());
            return Result.wrapErrorResult("", result.getMessage());
        }
        return result;
    }


    @Override
    public String alipay(OfferListFormParam param) {
        param.setSubject("智能投保充值");
        param.setSource(Constants.CUST_SOURCE);
        log.info("[DUBBO]获取支付宝支付接口,ucShopId:{},订单编号:{}", param.getUid(), param.getSn());
        Result<String> result = webPayService.getAliPayParamCommon(param);
        if (!result.isSuccess()) {
            log.error("[DUBBO]调用支付宝支付接口失败,失败原因:{}", LogUtils.objectToString(result));
            throw new BizException("获取支付信息失败");
        }
        return result.getData();
    }

    @Override
    public void verifyAliPay(Map<String, String[]> param) {
        log.info("[DUBBO]调用支付宝验证接口:{}", LogUtils.objectToString(param));
        try {
            Result<String> result = webPayService.verifyAliReturnCommon(Constants.CUST_SOURCE, param);
            if (!result.isSuccess()) {
                log.error("[DUBBO]调用支付宝验证接口,失败原因:{}", LogUtils.objectToString(result));
            }
        } catch (Exception e) {
            log.error("[DUBBO]调用支付宝验证接口,失败原因:", e);
        }
    }
}
