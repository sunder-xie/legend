package com.tqmall.legend.biz.pvg.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.legend.api.entity.PvgUserOrgResp;
import com.tqmall.legend.api.entity.RoleInfoResp;
import com.tqmall.legend.biz.pvg.IPvgUserOrgService;
import com.tqmall.legend.dao.pvg.PvgUserOrgDao;
import com.tqmall.legend.entity.pvg.PvgRole;
import com.tqmall.legend.entity.pvg.PvgUserOrg;
import com.tqmall.legend.facade.privilege.PvgRoleFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 用户岗位关系表Service Impl
 * <p/>
 * Created by dongc on 15/9/21.
 */
@Service
public class PvgUserOrgServiceImpl implements IPvgUserOrgService {

    public static final Logger LOGGER = LoggerFactory.getLogger(PvgUserOrgServiceImpl.class);

    @Autowired
    PvgUserOrgDao pvgUserOrgDao;
    @Autowired
    private PvgRoleFacade pvgRoleFacade;

    @Override
    public Optional<List<RoleInfoResp>> getReferRoleList(Integer managerId, Long shopId) {
        List<RoleInfoResp> roleInfoRespList = Lists.newArrayList();
        List<PvgRole> pvgRoleList = pvgRoleFacade.getPvgRoleListByShopIdAndUserId(shopId, managerId.longValue());
        if(CollectionUtils.isEmpty(pvgRoleList)){
            return Optional.fromNullable(roleInfoRespList);
        }
        for(PvgRole pvgRole : pvgRoleList){
            RoleInfoResp roleInfoResp = new RoleInfoResp();
            roleInfoResp.setRoleId(pvgRole.getId());
            roleInfoResp.setRoleName(pvgRole.getName());
            roleInfoRespList.add(roleInfoResp);
        }
        return Optional.fromNullable(roleInfoRespList);
    }

    @Override
    public Optional<List<PvgUserOrgResp>> getReferRoleList(Long shopId) {
        List<PvgUserOrgResp> pvgUserOrgRespList = Lists.newArrayList();
        Map<String, Object> searchMap = Maps.newHashMap();
        searchMap.put("shopId", shopId);
        List<PvgUserOrg> pvgUserOrgList = pvgUserOrgDao.select(searchMap);
        if (CollectionUtils.isEmpty(pvgUserOrgList)) {
            return Optional.fromNullable(pvgUserOrgRespList);
        }
        Map<Long, PvgRole> pvgRoleMap = pvgRoleFacade.getPvgRoleMap();
        for (PvgUserOrg pvgUserOrg : pvgUserOrgList) {
            PvgUserOrgResp pvgUserOrgResp = new PvgUserOrgResp();
            Long pvgRoleId = pvgUserOrg.getPvgRoleId();
            pvgUserOrgResp.setRoleId(pvgRoleId);
            if (pvgRoleMap.containsKey(pvgRoleId)) {
                PvgRole pvgRole = pvgRoleMap.get(pvgRoleId);
                pvgUserOrgResp.setRoleName(pvgRole.getName());
            }
            pvgUserOrgResp.setUserId(pvgUserOrg.getUserId());
            pvgUserOrgResp.setOrgId(pvgUserOrg.getOrgId());
            pvgUserOrgResp.setShopId(shopId);
            pvgUserOrgRespList.add(pvgUserOrgResp);
        }
        return Optional.fromNullable(pvgUserOrgRespList);
    }

    @Override
    public int insert(PvgUserOrg pvgUserOrg) {
        int flag = 0;
        try {
            flag = pvgUserOrgDao.insert(pvgUserOrg);
        } catch (Exception e) {
            LOGGER.error("插入用户岗位角色关系失败,pvgUserOrg:{}",pvgUserOrg);
        }

        return flag;
    }

    @Override
    public List<PvgUserOrg> select(Map searchMap) {
        return pvgUserOrgDao.select(searchMap);
    }

    @Override
    public int delete(Long id) {
        return pvgUserOrgDao.deleteById(id);
    }

    @Override
    public Integer update(PvgUserOrg pvgUserOrg) {
        return pvgUserOrgDao.updateById(pvgUserOrg);
    }

    @Override
    public List<PvgUserOrg> findByShopIdAndUserId(Long shopId, Long userId) {
        Map<String, Object> pvgUserOrgMap = Maps.newHashMap();
        pvgUserOrgMap.put("shopId", shopId);
        pvgUserOrgMap.put("userId", userId);
        return pvgUserOrgDao.select(pvgUserOrgMap);
    }
}
