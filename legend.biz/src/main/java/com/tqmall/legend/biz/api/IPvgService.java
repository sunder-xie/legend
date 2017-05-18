package com.tqmall.legend.biz.api;

import com.tqmall.common.exception.BizException;
import com.tqmall.legend.api.entity.ShopManagerResp;

/**
 * 角色service
 * <p/>
 * Created by dongc on 15/9/21.
 */
public interface IPvgService {

    /**
     * get user info
     *
     * @param managerid 管理员ID
     * @param shopid    店铺ID
     * @return ShopManagerResp
     * @See com.tqmall.legend.api.entity.ShopManagerResp
     */
    ShopManagerResp getUserInfo(Integer managerid, Long shopid) throws BizException;

    /**
     * get role list by shopId
     *
     * @param shopid 店铺ID
     * @return
     */
    Object getRoleList(Long shopid);

}
