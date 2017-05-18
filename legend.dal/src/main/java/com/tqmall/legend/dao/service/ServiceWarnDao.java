package com.tqmall.legend.dao.service;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.service.ServiceWarn;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface ServiceWarnDao extends BaseDao<ServiceWarn> {
    /**
     * 查询不在此范围内的车辆信息
     */
    List<CustomerCar> customerCarsNotExit(Map map);
}
