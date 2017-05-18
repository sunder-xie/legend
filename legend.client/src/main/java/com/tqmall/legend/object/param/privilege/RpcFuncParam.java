package com.tqmall.legend.object.param.privilege;

import com.tqmall.legend.object.param.BaseRpcParam;
import lombok.Data;

/**
 * Created by zsy on 16/6/16.
 */
@Data
public class RpcFuncParam extends BaseRpcParam{
    private Long userId;//用户id
    private Long shopId;//门店id
    private String funcName;//权限名称
}
