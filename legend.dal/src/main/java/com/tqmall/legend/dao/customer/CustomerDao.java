package com.tqmall.legend.dao.customer;

import com.tqmall.legend.bi.entity.CommonPair;
import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.customer.Customer;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@MyBatisRepository
public interface CustomerDao extends BaseDao<Customer> {
    public List<Customer> getCustomerByCarId(Map<String, Object> parameters);
    public List<Customer> getCustomerByNameMobile(Map<String, Object> params);

    /**
     * 根据shopId删除，重新初始化使用
     * @param shopId
     * @return
     */
    public Integer deleteByShopId(Long shopId);

    /**
     * 根据客户IDs和mobile查询customer信息
     * @param params
     * @return
     */
    public List<Customer> selectByIdsAndMobile(Map<String, Object> params);

    List<Customer> searchCompanyList(Map<String,Object> searchMap);

    List<Customer> getCustomerByGroupMobile(Map param);

    List<Customer> selectByMobiles(@Param("mobiles") Collection<String> mobiles, @Param("shopId") Long shopId);

    List<CommonPair<String,Long>> getPhoneCustomerIdPairList(Long shopId);

    List<Customer> selectByIdss(@Param("shopId") Long shopId, @Param("ids") Set<Long> ids);
}

