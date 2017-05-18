package com.tqmall.legend.dao.shop;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.shop.ServiceGoodsSuite;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisRepository
public interface ServiceGoodsSuiteDao extends BaseDao<ServiceGoodsSuite> {
    public ServiceGoodsSuite selectByServiceId(Long serviceId);

    public ServiceGoodsSuite selectByServiceIdAndShopId(@Param("serviceId") Long serviceId,
        @Param("shopId") Long shopId);

    public List selectByServiceIds(Long[] serviceIds);

    public Integer deleteByServiceId(Long serviceId);

    public Integer deleteByServiceIdAndShopId(@Param("serviceId") Long serviceId,
        @Param("shopId") Long shopId);

}
