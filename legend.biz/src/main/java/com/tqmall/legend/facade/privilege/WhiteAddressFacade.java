package com.tqmall.legend.facade.privilege;

import com.tqmall.legend.entity.sys.WhiteAddress;

import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 17/1/5.
 */
public interface WhiteAddressFacade {

    /**
     * 根据用户ids获取app角色map
     *
     * @param
     * @return
     */
    Map<Long, WhiteAddress> getWhiteAddressMapByUserIds(List<Long> userIdList);
}
