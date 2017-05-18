package com.tqmall.legend.biz.pvg.impl;

import com.tqmall.common.UserInfo;
import com.tqmall.legend.biz.privilege.RolesService;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.pvg.IHRMService;
import com.tqmall.legend.biz.pvg.IPvgUserOrgService;
import com.tqmall.legend.biz.pvg.PvgOrgTemplateService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.privilege.Roles;
import com.tqmall.legend.entity.pvg.PvgOrgTemplate;
import com.tqmall.legend.facade.privilege.RolesFuncFacade;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dongc on 15/9/22.
 */
@Service
public class HRMServiceImpl implements IHRMService {

    public static final Logger LOGGER = LoggerFactory.getLogger(HRMServiceImpl.class);

    @Autowired
    RolesService rolesService;

    @Autowired
    PvgOrgTemplateService pvgOrgTemplateService;

    @Autowired
    IPvgUserOrgService pvgUserOrgService;

    @Autowired
    ShopManagerService shopManagerService;
    @Autowired
    private RolesFuncFacade rolesFuncFacade;

    /**
     * 初始化门店岗位
     *
     * @param type
     * @param userInfo
     * @return
     */
    @Override
    public Result insertOrgTreeFromTemplate(Integer type, UserInfo userInfo) {
        Map orgMap = new HashMap(2);
        orgMap.put("shopId", userInfo.getShopId());
        List<Roles> roles = rolesService.select(orgMap);
        if (CollectionUtils.isEmpty(roles)) {
            return Result.wrapErrorResult("error", "顶级岗位不存在");
        }
        if (roles.size() > 1) {
            return Result.wrapErrorResult("exsit", "已有岗位数据，不需要插入");
        }
        Roles topRole = roles.get(0);
        Map searchMap = new HashMap(1);
        searchMap.put("type", type);
        List<PvgOrgTemplate> pvgOrgTemplateList = pvgOrgTemplateService.select(searchMap);
        if (CollectionUtils.isEmpty(pvgOrgTemplateList)) {
            return Result.wrapErrorResult("error", "岗位模板不存在");
        }

        Map levelMap1 = new HashMap();
        for (PvgOrgTemplate pvgOrgTemplate : pvgOrgTemplateList) {
            if (pvgOrgTemplate.getLevel().equals(1)) {
                long orgId = buildRole(topRole.getId(), pvgOrgTemplate, userInfo);
                levelMap1.put(pvgOrgTemplate.getId(), orgId);
            }
        }

        Map levelMap2 = new HashMap();
        for (PvgOrgTemplate pvgOrgTemplate : pvgOrgTemplateList) {
            if (pvgOrgTemplate.getLevel().equals(2)) {
                long orgKey = pvgOrgTemplate.getParentId();
                if (levelMap1.containsKey(orgKey)) {
                    Long parentId = (Long) levelMap1.get(orgKey);
                    long orgId = buildRole(parentId, pvgOrgTemplate, userInfo);
                    levelMap2.put(pvgOrgTemplate.getId(), orgId);

                }
            }
        }

        Map levelMap3 = new HashMap();
        for (PvgOrgTemplate pvgOrgTemplate : pvgOrgTemplateList) {
            if (pvgOrgTemplate.getLevel().equals(3)) {
                long orgKey = pvgOrgTemplate.getParentId();
                if (levelMap2.containsKey(orgKey)) {
                    Long parentId = (Long) levelMap2.get(orgKey);
                    long orgId = buildRole(parentId, pvgOrgTemplate, userInfo);
                    levelMap3.put(pvgOrgTemplate.getId(), orgId);

                }
            }
        }


        for (PvgOrgTemplate pvgOrgTemplate : pvgOrgTemplateList) {
            if (pvgOrgTemplate.getLevel().equals(4)) {
                long orgKey = pvgOrgTemplate.getParentId();
                if (levelMap3.containsKey(orgKey)) {
                    Long parentId = (Long) levelMap3.get(orgKey);
                    buildRole(parentId, pvgOrgTemplate, userInfo);
                }
            }
        }

        //初始化老板权限
        rolesFuncFacade.initAdminRolesFuncRel(userInfo.getShopId(), topRole.getId());
        return Result.wrapSuccessfulResult(true);
    }

    /**
     * 构建岗位
     *
     * @param parentId
     * @param pvgOrgTemplate
     * @param userInfo
     * @return
     */
    private Long buildRole(Long parentId, PvgOrgTemplate pvgOrgTemplate, UserInfo userInfo) {
        Roles newRole = new Roles();
        newRole.setCreator(userInfo.getUserId());
        newRole.setModifier(userInfo.getUserId());
        newRole.setShopId(userInfo.getShopId());
        newRole.setLevelId(pvgOrgTemplate.getLevel());
        newRole.setName(pvgOrgTemplate.getName());
        newRole.setParentId(parentId);
        newRole.setPvgRoleId(pvgOrgTemplate.getPvgRoleId().intValue());
        rolesService.save(newRole);
        return newRole.getId();
    }
}
