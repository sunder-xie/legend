package com.tqmall.legend.biz.privilege;

import com.tqmall.legend.entity.privilege.FuncF;

import java.util.List;
import java.util.Map;

/**
 * Created by QXD on 2014/11/4.
 */
public interface FuncService {
    /**
     * 根据角色id获取所有的功能
     *
     * @param shopId   当前的shopId
     * @param parentId 角色的父id
     * @param rolesId  角色id
     */
    List<FuncF> getFuncFsForRoles(Long shopId, Long rolesId, Long parentId, Integer shopLevel);

    /**
     * 根据角色id获取权限
     *
     * @param roleId    岗位id
     * @param shopId    门店id
     * @param shopLevel 门店等级
     * @return
     */
    List<FuncF> getFuncFsForUser(Long roleId, Long shopId, Integer shopLevel);

    /**
     * 获取所有的权限功能
     * @param shopLevel
     * @return
     */
    List<FuncF> getFuncLs(Integer shopLevel);
}
