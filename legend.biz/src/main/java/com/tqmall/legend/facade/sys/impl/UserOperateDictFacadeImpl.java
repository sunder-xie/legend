package com.tqmall.legend.facade.sys.impl;

import com.google.gson.Gson;
import com.tqmall.common.Constants;
import com.tqmall.legend.biz.sys.UserOperateDictService;
import com.tqmall.legend.biz.sys.bo.UserOperateDictSearchBO;
import com.tqmall.legend.cache.JedisClient;
import com.tqmall.legend.entity.sys.UserOperateDict;
import com.tqmall.legend.facade.sys.UserOperateDictFacade;
import com.tqmall.wheel.lang.Langs;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 17/5/9.
 */
@Service
public class UserOperateDictFacadeImpl implements UserOperateDictFacade {
    @Autowired
    private JedisClient jedisClient;
    @Autowired
    private UserOperateDictService userOperateDictService;

    private Gson gson = new Gson();

    @Override
    public UserOperateDict getByRefer(String refer) {
        if (StringUtils.isBlank(refer)) {
            return null;
        }

        Boolean exists = jedisClient.exists(Constants.USER_OPERATE_DICT);
        if (exists != null && exists) {
            Object value = jedisClient.hget(Constants.USER_OPERATE_DICT, refer);
            if (value != null) {
                return gson.fromJson((String) value, UserOperateDict.class);
            }
        } else {
            //查询数据库
            UserOperateDictSearchBO userOperateDictSearchBO = new UserOperateDictSearchBO();
            userOperateDictSearchBO.setOperateApplication(UserOperateDictSearchBO.Application.LEGEND.getCode());
            List<UserOperateDict> userOperateDictList = userOperateDictService.select(userOperateDictSearchBO);
            if (Langs.isEmpty(userOperateDictList)) {
                return null;
            }
            Map<String, String> userOperateDictMap = new HashMap<>(userOperateDictList.size());
            UserOperateDict currentUserOperate = null;
            for (UserOperateDict userOperateDict : userOperateDictList) {
                userOperateDictMap.put(userOperateDict.getRequestUrl(), gson.toJson(userOperateDict));
                if (refer.equals(userOperateDict.getRequestUrl())) {
                    currentUserOperate = userOperateDict;
                }
            }
            jedisClient.hmset(Constants.USER_OPERATE_DICT, userOperateDictMap);
            return currentUserOperate;
        }
        return null;
    }

    @Override
    public void delKey() {
        jedisClient.delete(Constants.USER_OPERATE_DICT);
    }
}
