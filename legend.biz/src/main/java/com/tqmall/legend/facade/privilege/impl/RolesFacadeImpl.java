package com.tqmall.legend.facade.privilege.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.google.common.collect.Maps;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.BdUtil;
import com.tqmall.legend.biz.privilege.RolesService;
import com.tqmall.legend.entity.privilege.Roles;
import com.tqmall.legend.entity.privilege.RolesL;
import com.tqmall.legend.facade.privilege.RolesFacade;
import com.tqmall.legend.facade.privilege.bo.RolesBo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 17/1/5.
 */
@Service
public class RolesFacadeImpl implements RolesFacade {
    @Autowired
    private RolesService rolesService;

    @Override
    public Map<Long, RolesL> getRolesLMapByShopId(Long shopId) {
        List<RolesL> rolesLList = rolesService.selectRolesByShopId(shopId);
        Map<Long, RolesL> rolesLMap = Maps.newHashMap();
        for (RolesL rolesL : rolesLList) {
            rolesLMap.put(rolesL.getId(), rolesL);
        }
        return rolesLMap;
    }

    @Override
    public Roles addRoles(RolesBo rolesBo) {
        Roles roles = new Roles();
        BdUtil.do2bo(rolesBo, roles);
        roles.setCreator(rolesBo.getUserId());
        rolesService.save(roles);
        return roles;
    }

    @Override
    public boolean delRoles(Long rolesId, Long shopId, Long userId) {
        Roles role = rolesService.selectById(rolesId);
        if (role == null) {
            throw new BizException("岗位不存在");
        }
        Integer pvgRoleId = role.getPvgRoleId();
        Map rolesMap = Maps.newHashMap();
        rolesMap.put("shopId", shopId);
        rolesMap.put("pvgRoleId", pvgRoleId);
        List<Roles> rolesList = rolesService.select(rolesMap);
        if (CollectionUtils.isEmpty(rolesList) || rolesList.size() == 1) {
            throw new BizException("系统里必须有店长、接待、仓库、技师、财务角色，不能删除");
        }
        rolesService.deleteById(rolesId);
        return true;
    }

    @Override
    public boolean editRoles(RolesBo rolesBo) {
        Long roleId = rolesBo.getId();
        Roles role = rolesService.selectById(roleId);
        if (role == null) {
            throw new BizException("岗位不存在");
        }
        role.setName(rolesBo.getName());
        role.setModifier(rolesBo.getUserId());
        rolesService.update(role);
        return true;
    }

    @Override
    public boolean isExist(RolesBo rolesBo) {
        Map<String, Object> searchMap = BdUtil.beanToMap(rolesBo);
        Integer selectCount = rolesService.selectCount(searchMap);
        if (selectCount > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isExistChild(Long rolesId, Long shopId){
        Map rolesMap = Maps.newHashMap();
        rolesMap.put("shopId", shopId);
        rolesMap.put("parentId", rolesId);
        Integer selectCount = rolesService.selectCount(rolesMap);
        if (selectCount > 0) {
            return true;
        }
        return false;
    }

    /**
     * 除了本身之外，校验平级是否存在岗位
     *
     * @param rolesId   必须传
     * @param shopId    门店id,必须传
     * @param rolesName 必须传
     * @return
     */
    @Override
    public boolean isExistIgnoreOwn(Long rolesId, Long shopId, String rolesName) {
        Roles role = rolesService.selectById(rolesId);
        if (role == null) {
            throw new BizException("岗位不存在");
        }
        Long parentId = role.getParentId();
        Map rolesMap = Maps.newHashMap();
        rolesMap.put("shopId", shopId);
        rolesMap.put("parentId", parentId);
        rolesMap.put("name", rolesName);
        List<Roles> rolesList = rolesService.select(rolesMap);
        if (CollectionUtils.isEmpty(rolesList)) {
            return false;
        }
        if (rolesList.size() > 1) {
            return true;
        }
        Roles roles = rolesList.get(0);
        if (rolesId.equals(roles.getId())) {
            return false;
        }
        return true;
    }
}
