package com.tqmall.legend.dao.customer;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.customer.Appoint;
import com.tqmall.legend.entity.customer.AppointVo;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface AppointVoDao extends BaseDao<Appoint> {
    List<AppointVo> selectWithStatInfo(Map<String, Object> params);
    public Integer getAppointCount(Map<String, Object> parameters);
    public List<AppointVo> getAppointList(Map<String, Object> parameters);
}
