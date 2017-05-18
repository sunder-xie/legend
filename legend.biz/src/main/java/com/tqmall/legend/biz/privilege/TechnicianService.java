package com.tqmall.legend.biz.privilege;

import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.entity.privilege.Technician;

/**
 * Created by lilige on 16/1/4.
 */
public interface TechnicianService {
    /**
     * 保存技师认证资料
     * @param technician
     * @param shopManager
     * @return
     */
    Result<Boolean> saveTechnician(Technician technician,ShopManager shopManager);

    /**
     * 根据shopId managerId获取技师认证资料
     * @param shopId
     * @param managerId
     * @return
     */
    Result<Technician> getTechnician(Long shopId,Long managerId);

    public Integer update(Technician technician);

    public Integer insert(Technician technician);

    boolean hasTechnician(Long shopId,Long managerId);

}
