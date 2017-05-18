package com.tqmall.legend.service.customer;

import com.tqmall.legend.object.result.customer.RoleInfoDTO;
import com.tqmall.zenith.errorcode.support.Result;

import java.util.List;

/**
 * TODO 放错package了，后续迁移
 * Created by xiangDong.qu on 16/4/10.
 */
public interface RpcUserPvgRoleService {
    /**
     * 获取用户角色列表
     *
     * @param shopId 店铺id
     * @param userId 用户id
     *
     * @return
     */
    public Result<List<RoleInfoDTO>> getUserPvgRoleList(Long shopId, Long userId);
}
