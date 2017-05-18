package com.tqmall.legend.biz.account.impl;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.legend.biz.account.ComboServiceRelService;
import com.tqmall.legend.dao.account.AccountComboServiceRelDao;
import com.tqmall.legend.entity.account.AccountComboServiceRel;
import com.tqmall.wheel.lang.Langs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by majian on 16/6/17.
 */
@Service
@Slf4j
public class ComboSerivceRelServiceImpl implements ComboServiceRelService {
    @Autowired
    private AccountComboServiceRelDao accountComboServiceRelDao;

    @Override
    public AccountComboServiceRel findById(Long shopId, Long comboServiceRelId) {
        Map param = Maps.newHashMap();
        param.put("shopId", shopId);
        param.put("id", comboServiceRelId);
        List<AccountComboServiceRel> comboServiceRelList =
                accountComboServiceRelDao.select(param);
        if (!CollectionUtils.isEmpty(comboServiceRelList)) {
            return comboServiceRelList.get(0);
        }
        return null;
    }

    @Override
    public List<AccountComboServiceRel> findByComboId(Long shopId, Long comboId) {
        Map param = Maps.newHashMap();
        param.put("shopId",shopId);
        param.put("comboId",comboId);
        return accountComboServiceRelDao.select(param);
    }

    @Override
    public List<AccountComboServiceRel> listByComboIds(Long shopId, Collection<Long> comboIds) {
        Assert.notNull(shopId);
        if (CollectionUtils.isEmpty(comboIds)) {
            return Lists.newArrayList();
        }
        return accountComboServiceRelDao.selectByComboIds(shopId, comboIds);
    }

    @Override
    public Optional<AccountComboServiceRel> findByService(Long serviceId, Long comboId , Long shopId) {
        Map param = Maps.newHashMap();
        param.put("shopId",shopId);
        param.put("comboId",comboId);
        param.put("serviceId",serviceId);
        List<AccountComboServiceRel> list = accountComboServiceRelDao.select(param);
        if (CollectionUtils.isEmpty(list)){
            return Optional.fromNullable(null);
        }
        return Optional.fromNullable(list.get(0));

    }

    @Override
    public List<AccountComboServiceRel> listByIds(Collection<Long> ids) {
        if (Langs.isEmpty(ids)) {
            return Collections.emptyList();
        }
        Long[] idArr = new Long[ids.size()];
        return accountComboServiceRelDao.selectByIds(ids.toArray(idArr));
    }
}
