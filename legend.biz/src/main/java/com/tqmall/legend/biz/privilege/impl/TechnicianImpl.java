package com.tqmall.legend.biz.privilege.impl;


import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.privilege.TechnicianService;
import com.tqmall.legend.common.LegendError;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.dao.privilege.TechnicianDao;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.entity.privilege.Technician;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by lilige on 16/1/4.
 */
@Service
@Slf4j
public class TechnicianImpl extends BaseServiceImpl implements TechnicianService {
    @Autowired
    private TechnicianDao technicianDao;
    @Autowired
    private ShopManagerService shopManagerService;


    @Override
    @Transactional
    public Result<Boolean> saveTechnician(Technician technician, ShopManager shopManager) {
        if (null == technician) {
            return Result.wrapErrorResult(LegendError.PARAM_ERROR.getCode(), "技师认证资料为空");
        }
        Long shopId = technician.getShopId();
        Long managerId = technician.getManagerId();
        ShopManager shopManagerDB = shopManagerService.selectByShopIdAndManagerIdDB(shopId, managerId);
        if (null == shopManagerDB) {
            return Result.wrapErrorResult(LegendError.PARAM_ERROR.getCode(), "未找到对应的人员信息");
        }
        shopManagerDB.setEducation(shopManager.getEducation());
        shopManagerDB.setGraduateSchool(shopManager.getGraduateSchool());
        shopManagerDB.setIdentityCard(shopManager.getIdentityCard());
        shopManagerDB.setGender(shopManager.getGender());
        shopManager.setId(shopManagerDB.getId());
        Result result = shopManagerService.upDataShopManager(shopId, shopManagerDB, managerId);
        if (!result.isSuccess()) {
            //TODO write hint
            log.info("[保存技师认证资料]保存失败 shopManager:{}", shopManager);
            return Result.wrapErrorResult(LegendError.COMMON_ERROR.getCode(), "数据库保存失败");
        }
        Technician dbTechnician = technicianDao.selectByManagerIdAndShopId(managerId, shopId);
        Boolean flag;
        technician.setVerifyStatus(2);
        if (null == dbTechnician) {
            //add to db
            technician.setCreator(technician.getManagerId());
            flag = technicianDao.insert(technician) > 0;
        } else {
            //update to db
            technician.setId(dbTechnician.getId());
            flag = technicianDao.updateById(technician) > 0;
        }
        if (flag) {
            return Result.wrapSuccessfulResult(true);
        } else {
            log.info("[保存技师认证资料]保存失败 technician:{}", technician);
            return Result.wrapErrorResult(LegendError.COMMON_ERROR.getCode(), "数据库保存失败");
        }
    }

    @Override
    public Result<Technician> getTechnician(Long shopId, Long managerId) {
        if (null == shopId || null == managerId) {
            log.warn("[获取技师认证资料]shopId managerId为空");
            return Result.wrapErrorResult(LegendError.PARAM_ERROR.getCode(), "获取技师认证参数错误");
        }
        try {
            Technician technician = technicianDao.selectByManagerIdAndShopId(managerId, shopId);
            return Result.wrapSuccessfulResult(technician);
        } catch (Exception e) {
            log.error("[获取技师认证资料]数据库查找错误 shopId:{},managerId:{}", shopId, managerId, e);
            return Result.wrapErrorResult(LegendError.COMMON_ERROR.getCode(), "数据库查找认证技师信息错误");
        }
    }

    @Override
    public Integer update(Technician technician) {
        return technicianDao.updateById(technician);
    }

    @Override
    public Integer insert(Technician technician) {
        return technicianDao.insert(technician);
    }

    @Override
    public boolean hasTechnician(Long shopId, Long managerId) {
        Technician technician = technicianDao.selectByManagerIdAndShopId(managerId, shopId);
        return technician == null ? false : true;
    }
}
