package com.tqmall.legend.service.service;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.object.param.service.UserRoleInfoParam;

/**
 * TODO  放错package了，后续迁移
 * Created by twg on 16/1/11.
 * 提供给APP的修改员工和角色信息接口
 */
public interface RpcUserRoleInfoService {

    /**
     * 用户技师认证
     */
    public Result approveUserRoleInfo(UserRoleInfoParam userRoleInfoParam);

}
