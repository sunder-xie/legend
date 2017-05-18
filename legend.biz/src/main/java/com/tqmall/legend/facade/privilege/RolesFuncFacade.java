package com.tqmall.legend.facade.privilege;

import com.tqmall.common.exception.BizException;
import com.tqmall.legend.facade.privilege.bo.RolesFuncBo;

/**
 * Created by zsy on 17/1/5.
 * PC岗位权限关系
 */
public interface RolesFuncFacade {

    /**
     * 更新角色岗位权限
     *
     * @param rolesFuncBo
     * @return
     */
    boolean updateRolesFuncRel(RolesFuncBo rolesFuncBo);

    /**
     * 初始化老板岗位的权限关系
     *
     * @param shopId
     * @param rolesId
     * @throws BizException
     */
    void initAdminRolesFuncRel(Long shopId, Long rolesId) throws BizException;
}
