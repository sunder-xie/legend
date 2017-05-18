package com.tqmall.legend.facade.shop.impl;

import com.tqmall.legend.common.Result;
import com.tqmall.legend.facade.shop.ShopManagerBalanceFacade;
import com.tqmall.mace.service.balance.RpcUserBalanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by feilong.li on 16/11/1.
 */
@Service
@Slf4j
public class ShopManagerBalanceFacadeImpl implements ShopManagerBalanceFacade{

    @Autowired
    private RpcUserBalanceService rpcUserBalanceService;

    @Override
    public Result<String> checkUserBalance(Long managerId, Long shopId) {
        com.tqmall.core.common.entity.Result<String> result = null;
        log.info("[检查用户提现中金额和可提现余额] 调用mace dubbo接口检查提现金额;[参数]managerId:{}, shopId:{}", managerId, shopId);
        try {
            result = rpcUserBalanceService.checkUserWithdrawAmount(managerId, shopId);
        } catch (Exception e) {
            log.error("[检查用户提现中金额和可提现余额] 调用mace dubbo接口检查提现金额异常,e:", e);
            return Result.wrapErrorResult("", "检查账户提现金额失败,系统错误。");
        }
        if (null == result) {
            log.info("[检查用户提现中金额和可提现余额] 调用mace dubbo接口检查提现金额失败,[参数]managerId:{}, shopId:{}; [结果]result==null", managerId, shopId);
            return Result.wrapErrorResult("", "检查账户提现金额失败,系统错误。");
        }
        if (result.isSuccess()) {
            return Result.wrapSuccessfulResult(result.getData());
        } else {
            return Result.wrapErrorResult(result.getCode(), result.getMessage());
        }
    }
}
