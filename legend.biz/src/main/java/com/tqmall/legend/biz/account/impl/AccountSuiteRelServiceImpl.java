package com.tqmall.legend.biz.account.impl;

import com.tqmall.legend.biz.account.AccountInfoService;
import com.tqmall.legend.biz.account.AccountSuiteRelService;
import com.tqmall.legend.biz.account.AccountTradeFlowService;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.dao.account.AccountInfoDao;
import com.tqmall.legend.dao.account.AccountSuiteDao;
import com.tqmall.legend.entity.account.AccountInfo;
import com.tqmall.legend.entity.account.AccountSuite;
import com.tqmall.legend.entity.account.AccountTradeFlow;
import com.tqmall.legend.entity.privilege.ShopManager;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AccountSuiteRelServiceImpl extends BaseServiceImpl implements AccountSuiteRelService {
    @Autowired
    private AccountSuiteDao accountSuiteDao;
    @Autowired
    private AccountInfoService accountInfoService;
    @Autowired
    private ShopManagerService shopManagerService;
    @Autowired
    private AccountTradeFlowService accountTradeFlowService;

    @Override
    public Page<AccountSuite> getPage(Pageable pageable, Map<String, Object> param) {
        Page<AccountSuite> page = this.getPage(accountSuiteDao, pageable, param);
        List<AccountSuite> content = page.getContent();
        List<Long> ids = new LinkedList<>();
        List<Long> flowIds = new LinkedList<>();
        /**
         * 查询组装账户信息
         */
        for (AccountSuite accountSuite : content) {
            ids.add(accountSuite.getAccountId());
            flowIds.add(accountSuite.getFlowId());
        }
        List<AccountInfo> accountInfos = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(ids)) {
            accountInfos = accountInfoService.getInfoByIds(ids);
        }
        Map<Long, AccountInfo> laMap = new HashMap<>();
        for (AccountInfo accountInfo : accountInfos) {
            laMap.put(accountInfo.getId(), accountInfo);
        }
        /**
         * 查询组装用户信息
         */
        List<ShopManager> shopManagers = shopManagerService.select(param);
        Map<Long,String> managerMap = new HashMap<>();
        for (ShopManager shopManager : shopManagers) {
            managerMap.put(shopManager.getId(),shopManager.getName());
        }
        /**
         * 设置账户信息与处理人信息
         */
        for (AccountSuite accountSuite : content) {
            AccountInfo accountInfo = laMap.get(accountSuite.getAccountId());
            if(accountInfo != null) {
                accountSuite.setCustomerName(accountInfo.getCustomerName());
                accountSuite.setMobile(accountInfo.getMobile());
            }
            String name = managerMap.get(accountSuite.getCreator());
            accountSuite.setOperatorName(name);
        }
        /**
         * 设置撤销信息
         */
        List<AccountTradeFlow> accountTradeFlows = new ArrayList<>();
        if (!CollectionUtils.isEmpty(flowIds)) {
            Map<String, Object> params = new HashMap<>();
            params.put("ids", flowIds);
            accountTradeFlows = accountTradeFlowService.select(params);

        }
        Map<Long, AccountTradeFlow> flowMap = new HashMap<>();
        for (AccountTradeFlow accountTradeFlow : accountTradeFlows) {
            flowMap.put(accountTradeFlow.getId(),accountTradeFlow);
        }
        for (AccountSuite accountSuite : content) {
            AccountTradeFlow accountTradeFlow = flowMap.get(accountSuite.getFlowId());
            if(accountTradeFlow != null && accountTradeFlow.getIsReversed() == 1){
                accountSuite.setCouponSource(AccountSuite.SourceEnum.CHARGE_REVERESE.getCode());
            }
        }
        return page;
    }
}
