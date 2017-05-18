package com.tqmall.legend.biz.sys.impl;

import com.tqmall.common.util.BdUtil;
import com.tqmall.legend.biz.sys.UserOperateDictService;
import com.tqmall.legend.biz.sys.bo.UserOperateDictSearchBO;
import com.tqmall.legend.dao.sys.UserOperateDictDao;
import com.tqmall.legend.entity.sys.UserOperateDict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 17/5/9.
 */
@Service
public class UserOperateDictServiceImpl implements UserOperateDictService {
    @Autowired
    private UserOperateDictDao userOperateDictDao;

    @Override
    public List<UserOperateDict> select(UserOperateDictSearchBO userOperateDictSearchBO) {
        Map<String, Object> searchMap = BdUtil.beanToMap(userOperateDictSearchBO);
        return userOperateDictDao.select(searchMap);
    }
}
