package com.tqmall.legend.dao.customer;


import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.customer.CustomerUserRel;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 16/12/15.
 */
@MyBatisRepository
public interface CustomerUserRelDao extends BaseDao<CustomerUserRel> {
    /**
     * 批量删除客户
     * @param deleteMap
     * @return
     */
    int batchDelete(Map<String,Object> deleteMap);

    /**
     * 获取已分配的门店员工ids
     * @param shopId
     * @return
     */
    List<Long> selectAllotUserIds(@Param("shopId")Long shopId);
}


