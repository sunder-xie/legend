package com.tqmall.legend.biz.privilege.impl;

import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.privilege.TechnicianService;
import com.tqmall.legend.biz.privilege.UserRoleInfoService;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.entity.privilege.Technician;
import com.tqmall.legend.object.param.service.UserRoleInfoParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by twg on 16/1/11.
 */
@Slf4j
@Service
public class UserRoleServiceImpl implements UserRoleInfoService {
    @Autowired
    private ShopManagerService shopManagerService;
    @Autowired
    private TechnicianService technicianService;


    @Override
    @Transactional
    public Result approve(UserRoleInfoParam userRoleInfoParam, ShopManager shopManager, boolean flag) {
        try {
            Long managerId = userRoleInfoParam.getId();
            Long shopId = shopManager.getShopId();
            BeanUtils.copyProperties(userRoleInfoParam, shopManager);
            shopManager.setGmtModified(new Date());
            shopManager.setModifier(managerId);
            shopManagerService.update(shopManager);

            if (flag) {
                log.info("[技师认证] 用户认证信息. userRoleInfoParam={}", userRoleInfoParam);
                com.tqmall.legend.common.Result<Technician> technician = technicianService.getTechnician(shopId, managerId);
                if (technician != null && technician.isSuccess() && technician.getData() != null) {
                    Technician tech = technician.getData();
                    Technician technicianTemp = new Technician();
                    technicianTemp.setId(tech.getId());
                    technicianTemp.setSeniority(userRoleInfoParam.getSeniority());
                    technicianTemp.setAdeptRepair(userRoleInfoParam.getAdeptRepair());
                    technicianTemp.setTechnicianLevel(userRoleInfoParam.getTechLevel());
                    technicianTemp.setGraduateSchool(userRoleInfoParam.getGraduateSchool());
                    technicianTemp.setModifier(managerId);
                    technicianTemp.setGmtModified(new Date());
                    technicianService.update(technicianTemp);
                } else {
                    if (userRoleInfoParam.getSeniority() != null && userRoleInfoParam.getTechLevel() != null && userRoleInfoParam.getTechStatus() != null) {
                        Technician tech = new Technician();
                        tech.setCreator(managerId);
                        tech.setModifier(managerId);
                        tech.setManagerId(managerId);
                        tech.setSeniority(userRoleInfoParam.getSeniority());
                        tech.setAdeptRepair(userRoleInfoParam.getAdeptRepair());
                        tech.setShopId(shopId);
                        tech.setTechnicianLevel(userRoleInfoParam.getTechLevel());
                        tech.setVerifyStatus(userRoleInfoParam.getTechStatus());
                        technicianService.insert(tech);
                    }
                }
            }

        } catch (Exception e) {
            log.error("[技师认证] 操作用户信息和技师认证信息异常，异常信息：{}", e);
            throw new RuntimeException("用户信息和技师认证信息失败");
        }
        return Result.wrapSuccessfulResult("用户信息和技师认证成功");
    }
}
