package com.tqmall.legend.dao.privilege;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.privilege.Technician;
import org.apache.ibatis.annotations.Param;

/**
 * Created by lilige on 16/1/4.
 */
@MyBatisRepository
public interface TechnicianDao extends BaseDao<Technician> {

    /**
     * 根据managerId shopId查找
     * @param managerId
     * @return
     */
    Technician selectByManagerIdAndShopId(@Param("managerId")Long managerId,@Param("shopId")Long shopId);




}