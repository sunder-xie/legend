package com.tqmall.legend.dao.customer;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.customer.CustomerFeedback;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface CustomerFeedbackDao extends BaseDao<CustomerFeedback> {
    CustomerFeedback selectByOrderIdAndShopId(@Param("orderId") Long orderId,
        @Param("shopId") Long shopId);


    List<CustomerFeedback> selectLastFeedback(@Param("shopId") Long shopId,
                                              @Param("nextVisitTimeGt") Date nextVisitTimeGt,
                                              @Param("nextVisitTimeLt") Date nextVisitTimeLt);

    List<CustomerFeedback> getNextVisitTimeList(Map map);
}
