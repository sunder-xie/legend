package com.tqmall.legend.biz.balance.impl;

import com.tqmall.common.Constants;
import com.tqmall.legend.biz.balance.FinanceAccountService;
import com.tqmall.legend.biz.balance.UserBalanceLogService;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.dao.balance.UserBalanceDao;
import com.tqmall.legend.dao.balance.UserBalanceLogDao;
import com.tqmall.legend.entity.balance.FinanceAccount;
import com.tqmall.legend.entity.balance.UserBalance;
import com.tqmall.legend.entity.balance.UserBalanceLog;
import com.tqmall.legend.pojo.balance.UserWithdrawInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiangDong.qu on 15/12/17.
 */
@Service
@Slf4j
public class UserBalanceLogServiceImpl extends BaseServiceImpl implements UserBalanceLogService {

    @Autowired
    private UserBalanceDao userBalanceDao;

    @Autowired
    private FinanceAccountService financeAccountService;

    @Autowired
    private UserBalanceLogDao userBalanceLogDao;


    @Override
    public Result getUserBalanceAndAccount(Long userId, Long shopId, Integer accountType) {
        Map<String, Object> userBalanceParam = new HashMap<>();
        userBalanceParam.put("userId", userId);
        userBalanceParam.put("shopId", shopId);
        List<UserBalance> userBalanceList = new ArrayList<>();
        try {
            userBalanceList = userBalanceDao.select(userBalanceParam);
        } catch (Exception e) {
            log.error("[用户余额] 获取用户余额 DB error. e={}", e);
            return Result.wrapErrorResult("", "获取用户余额失败");
        }
        log.info("[用户余额] 数据库获取结果 userBalanceList={}", userBalanceList);

        UserWithdrawInfo userWithdrawInfo = new UserWithdrawInfo();
        userWithdrawInfo.setBalance(BigDecimal.ZERO);
        if (!CollectionUtils.isEmpty(userBalanceList)) {
            if (userBalanceList.size() > 1) {
                log.error("[用户余额] 用户余额信息错误 userBalanceList={}", userBalanceList);
                return Result.wrapErrorResult("", "获取用户余额失败");
            }
            userWithdrawInfo.setBalance(userBalanceList.get(0).getBalance());
        }

        //获取用户的最后使用的账号
        FinanceAccount financeAccount = financeAccountService.getLastFinanceAccount(userId, shopId, accountType);
        if (null != financeAccount) {
            userWithdrawInfo.setAccount(financeAccount.getAccount());
            userWithdrawInfo.setUserName(financeAccount.getAccountUser());
            userWithdrawInfo.setAccountId(financeAccount.getId());
            userWithdrawInfo.setAccountType(financeAccount.getAccountType());
            userWithdrawInfo.setAccountBank(financeAccount.getAccountBank());
        }
        userWithdrawInfo.setLeastAmount(Constants.LEAST_WITHDRAW_AMOUNT);

        return Result.wrapSuccessfulResult(userWithdrawInfo);
    }


    @Override
    public List<UserBalanceLog> getUserBalanceLogs(Map<String, Object> params) {
        return userBalanceLogDao.select(params);
    }
}
