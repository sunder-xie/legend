package com.tqmall.legend.biz.account.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.legend.biz.account.AccountSuiteService;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.dao.account.AccountSuiteDao;
import com.tqmall.legend.entity.account.AccountSuite;
import com.tqmall.legend.entity.account.AccountTradeFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class AccountSuiteServiceImpl extends BaseServiceImpl implements AccountSuiteService {
    @Autowired
    private AccountSuiteDao accountSuiteDao;

    @Override
    public boolean insert(AccountSuite accountSuite) {
        return accountSuiteDao.insert(accountSuite) > 0 ? true : false;
    }

    @Override
    public void batchInsert(List<AccountSuite> accountSuiteList) {
        accountSuiteDao.batchInsert(accountSuiteList);
    }

    @Override
    public void delete(Long shopId) {
        Map param = Maps.newHashMap();
        if(shopId != null){
            param.put("shopId",shopId);
        }
        accountSuiteDao.delete(param);
    }

    @Override
    public AccountSuite findByFlowId(Long shopId, Long flowId) {
        Map param = Maps.newHashMap();
        param.put("shopId", shopId);
        param.put("flowId", flowId);
        List<AccountSuite> accountSuiteList = accountSuiteDao.select(param);
        if (!CollectionUtils.isEmpty(accountSuiteList)) {
            return accountSuiteList.get(0);
        }
        return null;
    }

    @Override
    public AccountSuite recordReverseSuiteForRecharge(Long shopId, Long userId, Long flowId, AccountTradeFlow reverseFlow) {
        AccountSuite suite = this.findByFlowId(shopId, flowId);
        if (suite != null) {
            AccountSuite reverseSuite = new AccountSuite();
            reverseSuite.setShopId(shopId);
            reverseSuite.setModifier(userId);
            reverseSuite.setCreator(userId);
            reverseSuite.setCouponSource(AccountSuite.SourceEnum.CHARGE_REVERESE.getCode());
            reverseSuite.setAccountId(suite.getAccountId());
            reverseSuite.setCouponSuiteId(suite.getCouponSuiteId());
            reverseSuite.setCouponSuiteName(suite.getCouponSuiteName());
            reverseSuite.setAmount(suite.getAmount().negate());
            reverseSuite.setFlowSn(reverseFlow.getFlowSn());
            reverseSuite.setFlowId(reverseFlow.getId());
            accountSuiteDao.insert(reverseSuite);
        }
        return null;
    }

    @Override
    public List<AccountSuite> listByFlowIds(Long shopId, Collection<Long> flowIds) {
        if (flowIds.isEmpty()) {
            return Lists.newArrayList();
        }
        return accountSuiteDao.selectByFlowIds(shopId, flowIds);

    }
}
