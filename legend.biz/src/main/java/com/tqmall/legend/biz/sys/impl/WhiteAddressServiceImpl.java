package com.tqmall.legend.biz.sys.impl;

import com.google.common.collect.Maps;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.sys.WhiteAddressService;
import com.tqmall.legend.dao.sys.WhiteAddressDao;
import com.tqmall.legend.entity.sys.WhiteAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 白名单相关代码
 */
@Service
public class WhiteAddressServiceImpl extends BaseServiceImpl implements WhiteAddressService {

    Logger logger = LoggerFactory.getLogger(WhiteAddressServiceImpl.class);

    @Autowired
    private WhiteAddressDao whiteAddressDao;

    /**
     * 获取用户白名单信息
     *
     * @param userId
     * @param shopId
     * @return
     */
    @Override
    public WhiteAddress getWhiteAddressInfo(Long userId, Long shopId) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("shopManagerId", userId);
        parameters.put("shopId", shopId);
        List<WhiteAddress> list = null;
        list = whiteAddressDao.select(parameters);
        if (!CollectionUtils.isEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    /**
     * @param userId
     * @param shopId
     * @param loginTime 当前登陆时间
     * @return
     */
    @Override
    public WhiteAddress getWhiteAddressInfo(Long userId, Long shopId, String loginTime) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("shopManagerId", userId);
        parameters.put("shopId", shopId);
        parameters.put("loginTime", loginTime);
        List<WhiteAddress> list = null;
        list = whiteAddressDao.select(parameters);
        if (!CollectionUtils.isEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 获取用户白名单信息
     *
     * @param ipAddress
     * @return
     */
    @Override
    public WhiteAddress selectByIpAddress(String ipAddress) {

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("ipAddress", ipAddress);
        List<WhiteAddress> list = whiteAddressDao.select(parameters);
        if (!CollectionUtils.isEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public void saveWhiteAddress(WhiteAddress whiteAddress) {
        whiteAddressDao.insert(whiteAddress);
    }

    @Override
    public void updateWhiteAddress(WhiteAddress whiteAddress) {
        whiteAddressDao.updateById(whiteAddress);
    }

    @Override
    public List<WhiteAddress> selectByUserIds(List<Long> userIdList) {
        if(CollectionUtils.isEmpty(userIdList)){
            return null;
        }
        Map<String,Object> searchMap = Maps.newHashMap();
        searchMap.put("shopManagerIds",userIdList);
        List<WhiteAddress> whiteAddressList = whiteAddressDao.select(searchMap);
        return whiteAddressList;
    }
}
