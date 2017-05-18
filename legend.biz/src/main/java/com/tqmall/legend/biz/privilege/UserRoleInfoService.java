package com.tqmall.legend.biz.privilege;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.object.param.service.UserRoleInfoParam;

/**
 * Created by twg on 16/1/11.
 */
public interface UserRoleInfoService {

    public Result approve(UserRoleInfoParam userRoleInfoParam,ShopManager shopManager,boolean flag);

}
