package com.tqmall.legend.biz.privilege.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.Constants;
import com.tqmall.legend.biz.privilege.FuncService;
import com.tqmall.legend.biz.privilege.RolesFuncRelService;
import com.tqmall.legend.dao.privilege.FuncDao;
import com.tqmall.legend.entity.privilege.FuncF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by QXD on 2014/11/4.
 */
@Service
public class FuncServiceImpl implements FuncService {
    @Autowired
    private FuncDao funcDao;
    @Autowired
    private RolesFuncRelService rolesFuncRelService;

    /**
     * 查询所有可用功能
     *
     * @param shopLevel
     */
    public List<FuncF> getFuncLs(Integer shopLevel) {
        List<FuncF> funcFList = funcDao.selectAll(shopLevel);
        if (CollectionUtils.isEmpty(funcFList)) {
            funcFList = Lists.newArrayList();
        }
        return funcFList;
    }


    /**
     * 根据rolesID查询所有功能
     *
     * @param rolesId
     */
    private List<FuncF> getFuncLsForRole(Long rolesId, Long shopId, Integer shopLevel) {
        List<FuncF> allFuncF = getFuncLs(shopLevel);
        //查询当前岗位的权限列表
        List<Long> funcIdsList = rolesFuncRelService.selectFuncIdsByRolesId(shopId, rolesId);
        if (CollectionUtils.isEmpty(funcIdsList)) {
            return allFuncF;
        }
        Map<Long, Object> funcIdMap = Maps.newHashMap();
        for (Long funcId : funcIdsList) {
            funcIdMap.put(funcId, null);
        }
        for (FuncF funcF : allFuncF) {
            Long funcId = funcF.getId();
            if (funcIdMap.containsKey(funcId)) {
                funcF.setRolesId(rolesId);
            }
        }
        return allFuncF;
    }

    /**
     * 根据角色id获取所有的功能
     *
     * @param shopId        当前的shopId
     * @param parentRolesId 角色的父id
     * @param shopId        店铺id
     *                      当父id为0时，表示是管理员
     */
    @Override
    public List<FuncF> getFuncFsForRoles(Long shopId, Long rolesId, Long parentRolesId, Integer shopLevel) {
        List<FuncF> oneLevelList = Lists.newArrayList();
        List<FuncF> allFuncF;
        if (parentRolesId == Constants.ZERO_FLAG) {
            allFuncF = getFuncLs(shopLevel);
            for (FuncF funcf : allFuncF) {
                funcf.setRolesId(rolesId);
            }
        } else {
            allFuncF = getFuncLsForRole(rolesId, shopId, shopLevel);
        }
        Map<Long, List<FuncF>> childMap = Maps.newHashMap();
        for (FuncF funcf : allFuncF) {
            Long parentId = funcf.getParentId();
            if (Long.compare(parentId, Constants.ZERO_FLAG) == 0) {
                oneLevelList.add(funcf);
            } else {
                List<FuncF> childList;
                if (childMap.containsKey(parentId)) {
                    childList = childMap.get(parentId);
                } else {
                    childList = Lists.newArrayList();
                }
                childList.add(funcf);
                childMap.put(parentId, childList);
            }
        }
        for (FuncF funcOneLevel : oneLevelList) {
            Long id = funcOneLevel.getId();
            if (childMap.containsKey(id)) {
                List<FuncF> childList = childMap.get(id);
                funcOneLevel.setFuncFList(childList);
            }
        }
        return oneLevelList;
    }

    public List<FuncF> getFuncFsForUser(Long roleId, Long shopId, Integer shopLevel) {
        List<FuncF> funcFList = funcDao.selectAll(shopLevel);
        if (CollectionUtils.isEmpty(funcFList)) {
            return funcFList;
        }
        List<Long> rolesIdList = rolesFuncRelService.selectFuncIdsByRolesId(shopId, roleId);
        Map<Long, Long> rolesIdMap = Maps.newHashMap();
        for (Long rolesId : rolesIdList) {
            rolesIdMap.put(rolesId, null);
        }
        Iterator<FuncF> iterator = funcFList.iterator();
        while (iterator.hasNext()) {
            FuncF funcF = iterator.next();
            Long id = funcF.getId();
            if (!rolesIdMap.containsKey(id)) {
                iterator.remove();
            }
        }
        return funcFList;
    }
}
