package com.tqmall.legend.dao.customer;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.customer.Appoint;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface AppointDao extends BaseDao<Appoint> {
    /**
     * create by jason 2015-09-18
     * 获得从app端过来的带确认的预约单
     *
     */
    public List<Appoint> selectAppAppoint(Map<String, Object> parameters);

    /**
     * 获取最近的预约单
     */
    public List<Appoint> queryLastAppoint(Map param);

}
