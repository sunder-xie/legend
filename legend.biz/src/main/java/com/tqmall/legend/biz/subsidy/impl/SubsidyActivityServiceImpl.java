package com.tqmall.legend.biz.subsidy.impl;

import com.tqmall.common.Constants;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.legend.biz.balance.FinanceAccountService;
import com.tqmall.legend.biz.balance.UserBalanceService;
import com.tqmall.legend.biz.subsidy.SubsidyActivityService;
import com.tqmall.legend.dao.balance.UserBalanceDao;
import com.tqmall.legend.dao.balance.UserBalanceLogDao;
import com.tqmall.legend.dao.subsidy.SubsidyActivityDao;
import com.tqmall.legend.entity.balance.FinanceAccount;
import com.tqmall.legend.entity.balance.UserBalance;
import com.tqmall.legend.entity.balance.UserBalanceLog;
import com.tqmall.legend.entity.subsidy.SubsidyActivity;
import com.tqmall.legend.pojo.balance.BalanceHandleStatusEnum;
import com.tqmall.legend.pojo.balance.UserAccountBalanceInfo;
import com.tqmall.legend.pojo.subsidy.UserBalanceSetBankInfo;
import com.tqmall.zenith.errorcode.support.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dingbao on 16/2/24.
 */
@Service
@Slf4j
public class SubsidyActivityServiceImpl implements SubsidyActivityService {

    @Autowired
    private SubsidyActivityDao subsidyActivityDao;

    @Autowired
    private UserBalanceService userBalanceService;

    @Autowired
    private UserBalanceLogDao userBalanceLogDao;

    @Autowired
    private UserBalanceDao userBalanceDao;

    @Autowired
    private FinanceAccountService financeAccountService;

    @Override
    public List<SubsidyActivity> getSubsidyActivities(Long actId) {
        Map<String, Object> params = new HashMap<>();
        params.put("actId", actId);
        params.put("currentTime", new Date());
        params.put("actStatus", 1);
        return subsidyActivityDao.select(params);
    }

    @Override
    public Result getUserAccountBalance(Long userId, Long shopId) {
        log.info("[我的账户]获取用户账户已提现、待到账金额、待审核金额,userId={},shopId={}", userId, shopId);
        UserAccountBalanceInfo userAccountBalanceInfo = new UserAccountBalanceInfo();
        userAccountBalanceInfo.setWithdrewAmount(BigDecimal.ZERO);
        userAccountBalanceInfo.setAuditingAmount(BigDecimal.ZERO);
        userAccountBalanceInfo.setWithdrawingAmount(BigDecimal.ZERO);
        userAccountBalanceInfo.setWithdrawingAmountFoTq(BigDecimal.ZERO);
        //已提现成功金额获取(已到账)
        BigDecimal withdrewAmount = getAmount(userId, shopId, BalanceHandleStatusEnum.TXCG.getHandleStatusType(), 1);
        userAccountBalanceInfo.setWithdrewAmount(userAccountBalanceInfo.getWithdrewAmount().add(withdrewAmount));
        //待到账(余额)金额获取
        List<UserBalance> userBalanceList = userBalanceService.getUserBalanceOnly(userId, shopId);
        BigDecimal userBalanceAmount = BigDecimal.ZERO;
        if (CollectionUtils.isEmpty(userBalanceList)) {
            log.error("[我的账户]找不到用户(待到账)余额记录,userId={},shopId={}", userId, shopId);
        } else if (userBalanceList.size() > 1) {
            log.error("[我的账户]用户信息错误，余额记录有多条,userBalanceList={}", userBalanceList);
            return LegendErrorCode.APP_USER_BALANCE_ERROR.newResult();
        } else {
            UserBalance userBalance = userBalanceList.get(0);
            userBalanceAmount = userBalance.getBalance();
        }
        userAccountBalanceInfo.setWithdrawingAmount(userAccountBalanceInfo.getWithdrawingAmount().add(userBalanceAmount));
        //待审核金额获取(申领中)
        BigDecimal auditingAmount = getAmount(userId, shopId, BalanceHandleStatusEnum.HBDSH.getHandleStatusType(), 0);
        userAccountBalanceInfo.setAuditingAmount(userAccountBalanceInfo.getAuditingAmount().add(auditingAmount));
        //用户提现中（淘汽待审核）余额
        Map<String, Object> param = new HashMap<>();
        param.put("actionType", 1);
        param.put("handleStatus", 3);
        param.put("userId", userId);
        param.put("shopId", shopId);
        param.put("isDeleted","N");
        List<UserBalanceLog> userBalanceLogList = userBalanceLogDao.select(param);
        if(!CollectionUtils.isEmpty(userBalanceLogList)){
            BigDecimal temp = userAccountBalanceInfo.getWithdrawingAmountFoTq();
            for(UserBalanceLog userBalanceLog : userBalanceLogList){
                if(null != userBalanceLog.getAmount()){
                    temp = temp.add(userBalanceLog.getAmount());
                }
            }
            userAccountBalanceInfo.setWithdrawingAmountFoTq(temp);
        }
        return Result.wrapSuccessfulResult(userAccountBalanceInfo);
    }

    @Override
    public Result getBalanceAndIsSetBank(Long userId, Long shopId) {
        log.info("[我的账户]获取用户账户余额和是否已保存了银行卡信息,userId={},shopId={}", userId, shopId);
        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("userId", userId);
        searchParams.put("shopId", shopId);
        List<UserBalance> userBalanceList;
        try {
            userBalanceList = userBalanceDao.select(searchParams);
        } catch (Exception e) {
            log.error("[我的账户] 获取用户余额信息失败. userId={},shopId={},e:{}", userId, shopId, e);
            return LegendErrorCode.APP_USER_BALANCE_ERROR.newResult();
        }

        UserBalanceSetBankInfo userBalanceSetBankInfo = new UserBalanceSetBankInfo();
        userBalanceSetBankInfo.setWithdrawNoticeStr(Constants.WITHDRAW_NOTICE);
        if (!CollectionUtils.isEmpty(userBalanceList)) {
            if (userBalanceList.size() > 1) {
                log.error("[我的账户]用户有多条账户余额记录. userId={},shopId={}", userId, shopId);
                return LegendErrorCode.APP_USER_BALANCE_ERROR.newResult();
            }
            UserBalance userBalance = userBalanceList.get(0);
            userBalanceSetBankInfo.setBalance(userBalance.getBalance());
        } else {
            userBalanceSetBankInfo.setBalance(BigDecimal.ZERO);
        }
        List<FinanceAccount> financeAccounts = financeAccountService.getDefaultFinanceAccount(userId, shopId, 1);
        if (CollectionUtils.isEmpty(financeAccounts) || financeAccounts.size() > 1) {
            userBalanceSetBankInfo.setIsSetBank(false);
        } else {
            userBalanceSetBankInfo.setIsSetBank(true);
        }
        return Result.wrapSuccessfulResult(userBalanceSetBankInfo);
    }

    /**
     *
     * */
    public BigDecimal getAmount(Long userId, Long shopId, Integer handleStatus, Integer actionType) {
        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("userId", userId);
        searchParams.put("shopId", shopId);
        searchParams.put("handleStatus", handleStatus);
        searchParams.put("actionType", actionType);
        searchParams.put("isDeleted", "N");
        List<UserBalanceLog> userBalanceLogList = userBalanceLogDao.select(searchParams);
        BigDecimal amount = BigDecimal.ZERO;
        if (!CollectionUtils.isEmpty(userBalanceLogList)) {
            for (UserBalanceLog userBalanceLog : userBalanceLogList) {
                amount = amount.add(userBalanceLog.getAmount());
            }
        }
        return amount;
    }
}
