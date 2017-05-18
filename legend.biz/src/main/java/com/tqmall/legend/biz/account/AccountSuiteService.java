package com.tqmall.legend.biz.account;


import com.tqmall.legend.entity.account.AccountSuite;
import com.tqmall.legend.entity.account.AccountTradeFlow;

import java.util.Collection;
import java.util.List;

public interface AccountSuiteService {

    boolean insert(AccountSuite accountSuite);

    void batchInsert(List<AccountSuite> accountSuiteList);

    void delete(Long shopId);

    AccountSuite findByFlowId(Long shopId, Long id);

    AccountSuite recordReverseSuiteForRecharge(Long shopId, Long userId, Long flowId, AccountTradeFlow reverseFlow);

    /**
     * 批量查询
     * @return
     * @param shopId
     * @param flowIds
     */
    List<AccountSuite> listByFlowIds(Long shopId, Collection<Long> flowIds);
}
