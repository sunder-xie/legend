package com.tqmall.legend.biz.account.impl;

import com.tqmall.legend.biz.account.AccountComboServiceRelService;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.dao.account.AccountComboServiceRelDao;
import com.tqmall.legend.entity.account.AccountComboServiceRel;
import com.tqmall.wheel.lang.Langs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by twg on 16/12/9.
 */
@Service
public class AccountComboServiceRelServiceImpl extends BaseServiceImpl implements AccountComboServiceRelService {
    @Autowired
    private AccountComboServiceRelDao accountComboServiceRelDao;
    @Override
    public void batchSave(List<AccountComboServiceRel> accountComboServiceRels) {
        super.batchInsert(accountComboServiceRelDao,accountComboServiceRels,1000);
    }

    @Override
    public List<AccountComboServiceRel> listByComboIds(Long shopId, Collection<Long> comboIds) {
        Assert.isTrue(shopId != null && shopId > 0);
        if (Langs.isEmpty(comboIds)) {
            return Collections.emptyList();
        }
        return accountComboServiceRelDao.selectByComboIds(shopId, comboIds);
    }
}
