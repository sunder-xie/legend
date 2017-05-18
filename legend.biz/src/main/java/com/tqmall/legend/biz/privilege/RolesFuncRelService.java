package com.tqmall.legend.biz.privilege;

import com.tqmall.legend.entity.privilege.RolesFuncRel;

import java.util.List;

/**
 * Created by QXD on 2014/10/30.
 */
public interface RolesFuncRelService {
    /**
     * 根据角色(rolesID)查询角色功能ID
     *
     * @param rolesId 角色id
     * @param shopId  店铺id
     */
    List<Long> selectFuncIdsByRolesId(Long shopId, Long rolesId);

    /**
     * 根据shopId,rolesId删除岗位权限关系
     * @param shopId
     * @param rolesId
     * @return
     */
    Integer delete(Long shopId, Long rolesId);

    /**
     * 批量添加
     * @param rolesFuncRelList
     * @return
     */
    Integer batchInsert(List<RolesFuncRel> rolesFuncRelList);
}
