package com.tqmall.legend.biz.privilege.impl;

import com.google.common.collect.Maps;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.privilege.RolesFuncRelService;
import com.tqmall.legend.dao.privilege.RolesFuncRelDao;
import com.tqmall.legend.entity.privilege.RolesFuncRel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by QXD on 2014/10/30.
 */
@Service
public class RolesFuncRelImpl extends BaseServiceImpl implements RolesFuncRelService {
    @Autowired
    private RolesFuncRelDao rolesFuncRelDao;

    /**
     * 根据用户角色(rolesID)查询角色功能ID
     *
     * @param rolesId 角色id
     */
    @Override
    public List<Long> selectFuncIdsByRolesId(Long shopId, Long rolesId) {
        List<Long> funcIds = rolesFuncRelDao.selectFuncIdsByRolesId(shopId, rolesId);
        return funcIds;
    }

    @Override
    public Integer delete(Long shopId, Long rolesId) {
        if (shopId == null || rolesId == null) {
            return 0;
        }
        Map<String, Object> deleteMap = Maps.newHashMap();
        deleteMap.put("shopId", shopId);
        deleteMap.put("rolesId", rolesId);
        return rolesFuncRelDao.delete(deleteMap);
    }

    @Override
    public Integer batchInsert(List<RolesFuncRel> rolesFuncRelList) {
        return super.batchInsert(rolesFuncRelDao, rolesFuncRelList, 300);
    }
}
