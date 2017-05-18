package com.tqmall.legend.biz.balance.impl;

import com.google.common.collect.Maps;
import com.tqmall.legend.biz.balance.FinanceAccountService;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.dao.balance.FinanceAccountDao;
import com.tqmall.legend.entity.balance.FinanceAccount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiangDong.qu on 15/12/17.
 */
@Service
@Slf4j
public class FinanceAccountServiceImpl extends BaseServiceImpl implements FinanceAccountService {

    @Autowired
    private FinanceAccountDao financeAccountDao;

    @Override
    public List<FinanceAccount> getUserFinanceAccount(Map<String, Object> param) {
        return financeAccountDao.select(param);
    }

    @Override
    public FinanceAccount getLastFinanceAccount(Long userId, Long shopId, Integer accountType) {
        Map<String, Object> param = new HashMap<>();
        param.put("shopId", shopId);
        param.put("userId", userId);
        param.put("accountType", accountType);
        param.put("isDefault", "1");
        param.put("sorts", new String[]{"gmt_modified desc"});
        param.put("offset", 0);
        param.put("limit", 1);
        List<FinanceAccount> financeAccountList = financeAccountDao.select(param);
        if (!CollectionUtils.isEmpty(financeAccountList)) {
            return financeAccountList.get(0);
        }
        return null;
    }

    @Override
    public List<FinanceAccount> getDefaultFinanceAccount(Long userId, Long shopId, Integer accountType) {
        Map<String, Object> param = new HashMap<>();
        param.put("shopId", shopId);
        param.put("userId", userId);
        param.put("accountType", accountType);
        param.put("isDefault", "1");
        List<FinanceAccount> financeAccountList = financeAccountDao.select(param);
        if (CollectionUtils.isEmpty(financeAccountList)) {
            return null;
        }
        return financeAccountList;
    }

    @Override
    public FinanceAccount insertFinanceAccount(FinanceAccount financeAccount) {
        try {
            if (financeAccountDao.insert(financeAccount) > 0) {
                return financeAccount;
            }
        } catch (Exception e) {
            log.error("[用户账户] DB error. e={}", e);
        }
        return null;
    }

    @Override
    public int upDateById(FinanceAccount financeAccount) {
        return financeAccountDao.updateById(financeAccount);
    }

    @Override
    public int upDateByShopIdAndUserIdAndAccountType(FinanceAccount financeAccount) {
        return financeAccountDao.updateByShopIdAndUserIdAndAccountType(financeAccount);
    }

    @Override
    public FinanceAccount getSettleFinanceAccount(Long shopId) {
        Map<String, Object> searchMap = new HashMap<>();
        searchMap.put("userId", 0);
        searchMap.put("shopId", shopId);
        List<FinanceAccount> financeAccountList = financeAccountDao.select(searchMap);
        if (CollectionUtils.isEmpty(financeAccountList)) {
            return null;
        }
        FinanceAccount financeAccount = financeAccountList.get(0);
        return financeAccount;
    }

    @Override
    public List<FinanceAccount> selectFinanceAccount(List<Long> shopIdList) {

        List<FinanceAccount> financeAccountList = financeAccountDao.selectShopFinanceAccount(0L,shopIdList);
        return financeAccountList;
    }

    @Override
    public Long saveOrUpdate(FinanceAccount financeAccount) {
        if(financeAccount.getId() == null){
            financeAccountDao.insert(financeAccount);
            return financeAccount.getId();
        }
        financeAccountDao.updateById(financeAccount);
        return financeAccount.getId();
    }
}
