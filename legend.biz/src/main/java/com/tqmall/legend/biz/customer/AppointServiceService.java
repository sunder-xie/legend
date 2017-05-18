package com.tqmall.legend.biz.customer;

import com.tqmall.legend.entity.customer.AppointServiceVo;

import java.util.List;
import java.util.Map;

/**
 * 预约的服务 service
 */
public interface AppointServiceService {

    public AppointServiceVo selectById(Long id);

    public List<AppointServiceVo> selectByIds(Long[] ids);

    public List<AppointServiceVo> select(Map<String, Object> searchParams);


    /**
     * get appoint's service list
     *
     * @param appointId 预约单ID
     * @param shopId    门店ID
     * @return List<AppointServiceVo>
     */
    List<AppointServiceVo> getAppointServiceList(Long appointId, Long shopId);

    /**
     * 批量插入
     * @param appointServicesList
     * @return
     */
    public Integer batchInsert(List<AppointServiceVo> appointServicesList);

    List<AppointServiceVo> selectByServiceIds(int suitNum, Long... serviceIds);
}

