package com.tqmall.legend.facade.privilege;

import com.tqmall.legend.entity.pvg.PvgRole;

import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 17/1/5.
 */
public interface PvgRoleFacade {

    /**
     * 获取app角色map
     *
     * @param
     * @return
     */
    Map<Long, PvgRole> getPvgRoleMap();

    /**
     * 根据shop_manager的ids获取员工的角色map
     *
     * @param shopId
     * @param userIds
     * @return
     */
    Map<Long, List<String>> getRolesNameListMapByShopIdAndUserIds(Long shopId, List<Long> userIds);

    /**
     * 根据shop_manager的ids获取员工的角色map
     * @param shopId
     * @param userIds
     * @return
     */
    Map<Long, List<PvgRole>> getPvgRoleListMapByShopIdAndUserIds(Long shopId, List<Long> userIds);

    /**
     * 根据门店id和用户id获取员工角色名称list
     * @param shopId
     * @param userId
     * @return
     */
    List<String> getRolesNameListByShopIdAndUserId(Long shopId, Long userId);

    /**
     * 根据门店id和用户id获取员工角色名称list
     *
     * @param shopId
     * @param userId
     * @return
     */
    List<PvgRole> getPvgRoleListByShopIdAndUserId(Long shopId, Long userId);
}
