package com.tqmall.legend.service.privilege;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.object.param.privilege.RpcFuncParam;

/**
 * Created by zsy on 16/6/16.
 * 权限
 */
public interface RpcFuncService {
    /**
     * 权限校验
     * @param rpcFuncParam
     * @return
     */
    Result<Boolean> checkFunc(RpcFuncParam rpcFuncParam);
}
