package com.tqmall.legend.service.shop;

import com.tqmall.legend.object.result.pvg.PvgUserOrgRespDTO;
import com.tqmall.legend.object.result.pvg.ShopManagerRespDTO;
import com.tqmall.legend.object.result.shop.ShopManagerDTO;
import com.tqmall.zenith.errorcode.support.Result;

import java.util.List;
import java.util.Set;

/**
 * Created by xiangDong.qu on 16/4/13.
 */
public interface RpcShopManagerService {
    /**
     * 获取店铺的员工
     *
     * @param shopId
     *
     * @return
     */
    public Result<List<ShopManagerDTO>> getShopManager(Long shopId);

    /**
     * 检查当前用户是否存在
     *
     * @param userIdSet 用户id集合
     * @param shopId    店铺id
     *
     * @return Set<Long> 存在的用户id集合
     */
    public Result<Set<Long>> checkExist(Long shopId, Set<Long> userIdSet);

    /**
     * 获取店铺员工角色列表
     *
     * @param shopId 店铺Id
     *
     * @return
     */
    public com.tqmall.core.common.entity.Result<List<PvgUserOrgRespDTO>> getShopManagerRoleList(Long shopId);


    /**
     * 获取店铺员工信息
     *
     * @param shopId    店铺信息
     * @param managerId 员工Id
     *
     * @return
     */
    public com.tqmall.core.common.entity.Result<ShopManagerRespDTO> getUserInfo(Long shopId, Long managerId);
}
