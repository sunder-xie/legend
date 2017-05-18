package com.tqmall.legend.facade.privilege.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.google.common.collect.Maps;
import com.tqmall.legend.biz.sys.WhiteAddressService;
import com.tqmall.legend.entity.sys.WhiteAddress;
import com.tqmall.legend.facade.privilege.WhiteAddressFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 17/1/5.
 */
@Service
public class WhiteAddressFacadeImpl implements WhiteAddressFacade {
    @Autowired
    private WhiteAddressService whiteAddressService;

    @Override
    public Map<Long, WhiteAddress> getWhiteAddressMapByUserIds(List<Long> userIdList) {
        Map<Long, WhiteAddress> whiteAddressMap = Maps.newHashMap();
        if (CollectionUtils.isEmpty(userIdList)) {
            return whiteAddressMap;
        }
        List<WhiteAddress> whiteAddressList = whiteAddressService.selectByUserIds(userIdList);
        for (WhiteAddress whiteAddress : whiteAddressList) {
            Long userId = whiteAddress.getShopManagerId();
            whiteAddressMap.put(userId, whiteAddress);
        }
        return whiteAddressMap;
    }
}
