package com.tqmall.legend.facade.privilege.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.legend.biz.pvg.IPvgRoleService;
import com.tqmall.legend.biz.pvg.IPvgUserOrgService;
import com.tqmall.legend.entity.pvg.PvgRole;
import com.tqmall.legend.entity.pvg.PvgUserOrg;
import com.tqmall.legend.facade.privilege.PvgRoleFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 17/1/5.
 */
@Service
public class PvgRoleFacadeImpl implements PvgRoleFacade {
    @Autowired
    private IPvgRoleService pvgRoleService;
    @Autowired
    private IPvgUserOrgService pvgUserOrgService;

    @Override
    public Map<Long, PvgRole> getPvgRoleMap() {
        Map<Long, PvgRole> pvgRoleMap = Maps.newHashMap();
        List<PvgRole> pvgRoleList = pvgRoleService.selectPvgRoleList();
        for(PvgRole pvgRole : pvgRoleList){
            Long id = pvgRole.getId();
            pvgRoleMap.put(id, pvgRole);
        }
        return pvgRoleMap;
    }

    @Override
    public Map<Long, List<String>> getRolesNameListMapByShopIdAndUserIds(Long shopId, List<Long> userIds) {
        Map<Long, List<String>> rolesNameListMap = Maps.newHashMap();
        if (CollectionUtils.isEmpty(userIds)) {
            return rolesNameListMap;
        }
        Map<Long, PvgRole> pvgRoleMap = getPvgRoleMap();
        Map<String, Object> searchMap = Maps.newHashMap();
        searchMap.put("userIds", userIds);
        List<PvgUserOrg> pvgUserOrgList = pvgUserOrgService.select(searchMap);
        for (PvgUserOrg pvgUserOrg : pvgUserOrgList) {
            Long userId = pvgUserOrg.getUserId();
            Long pvgRoleId = pvgUserOrg.getPvgRoleId();
            if (!pvgRoleMap.containsKey(pvgRoleId)) {
                continue;
            }
            PvgRole pvgRole = pvgRoleMap.get(pvgRoleId);
            String name = pvgRole.getName();
            List<String> rolesNameList;
            if (rolesNameListMap.containsKey(userId)) {
                rolesNameList = rolesNameListMap.get(userId);
            } else {
                rolesNameList = Lists.newArrayList();
            }
            rolesNameList.add(name);
            rolesNameListMap.put(userId, rolesNameList);
        }
        return rolesNameListMap;
    }

    @Override
    public Map<Long, List<PvgRole>> getPvgRoleListMapByShopIdAndUserIds(Long shopId, List<Long> userIds) {
        Map<Long, List<PvgRole>> rolesNameListMap = Maps.newHashMap();
        if (CollectionUtils.isEmpty(userIds)) {
            return rolesNameListMap;
        }
        Map<Long, PvgRole> pvgRoleMap = getPvgRoleMap();
        Map<String, Object> searchMap = Maps.newHashMap();
        searchMap.put("userIds", userIds);
        List<PvgUserOrg> pvgUserOrgList = pvgUserOrgService.select(searchMap);
        for (PvgUserOrg pvgUserOrg : pvgUserOrgList) {
            Long userId = pvgUserOrg.getUserId();
            Long pvgRoleId = pvgUserOrg.getPvgRoleId();
            if (!pvgRoleMap.containsKey(pvgRoleId)) {
                continue;
            }
            PvgRole pvgRole = pvgRoleMap.get(pvgRoleId);
            List<PvgRole> pvgRoleList;
            if (rolesNameListMap.containsKey(userId)) {
                pvgRoleList = rolesNameListMap.get(userId);
            } else {
                pvgRoleList = Lists.newArrayList();
            }
            pvgRoleList.add(pvgRole);
            rolesNameListMap.put(userId, pvgRoleList);
        }
        return rolesNameListMap;
    }

    @Override
    public List<String> getRolesNameListByShopIdAndUserId(Long shopId, Long userId) {
        List<Long> userIdList = Lists.newArrayList();
        userIdList.add(userId);
        Map<Long, List<String>> userIdMap =  getRolesNameListMapByShopIdAndUserIds(shopId, userIdList);
        List<String> nameList = Lists.newArrayList();
        if(userIdMap.containsKey(userId)){
            nameList = userIdMap.get(userId);
        }
        return nameList;
    }

    @Override
    public List<PvgRole> getPvgRoleListByShopIdAndUserId(Long shopId, Long userId) {
        List<Long> userIdList = Lists.newArrayList();
        userIdList.add(userId);
        Map<Long, List<PvgRole>> pvgRoleMap =  getPvgRoleListMapByShopIdAndUserIds(shopId, userIdList);
        List<PvgRole> pvgRoleList = Lists.newArrayList();
        if(pvgRoleMap.containsKey(userId)){
            pvgRoleList = pvgRoleMap.get(userId);
        }
        return pvgRoleList;
    }
}
