package com.tqmall.legend.facade.privilege;

import com.tqmall.legend.entity.privilege.Roles;
import com.tqmall.legend.entity.privilege.RolesL;
import com.tqmall.legend.facade.privilege.bo.RolesBo;

import java.util.Map;

/**
 * Created by zsy on 17/1/5.
 * 人员岗位
 */
public interface RolesFacade {

    /**
     * 根据门店id获取角色map
     *
     * @param shopId 角色id role_id
     * @return
     */
    Map<Long, RolesL> getRolesLMapByShopId(Long shopId);

    /**
     * 添加岗位
     *
     * @param rolesBo
     * @return
     */
    Roles addRoles(RolesBo rolesBo);

    /**
     * 删除岗位
     *
     * @param rolesId
     * @param shopId
     * @param userId
     * @return
     */
    boolean delRoles(Long rolesId, Long shopId, Long userId);

    /**
     * 修改岗位
     *
     * @param rolesBo
     * @return
     */
    boolean editRoles(RolesBo rolesBo);

    /**
     * 校验是否存在岗位
     *
     * @param rolesBo
     * @return
     */
    boolean isExist(RolesBo rolesBo);

    /**
     * 校验是否存在子岗位
     * @param rolesId
     * @param shopId
     * @return
     */
    boolean isExistChild(Long rolesId, Long shopId);

    /**
     * 除了本身之外，校验平级是否存在岗位
     *
     * @param rolesId   必须传
     * @param shopId    门店id,必须传
     * @param rolesName 必须传
     * @return
     */
    boolean isExistIgnoreOwn(Long rolesId, Long shopId, String rolesName);
}
