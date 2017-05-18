package com.tqmall.legend.biz.customer;

import com.tqmall.legend.biz.account.bo.AccountAndCarBO;
import com.tqmall.legend.entity.customer.CustomerCarRel;

import java.util.List;

/**
 * Created by xin on 2017/2/28.
 */
public interface CustomerCarRelService {
    /**
     * 根据车辆id查询关联的客户
     *
     * @param shopId
     * @param carId
     * @return
     */
    List<CustomerCarRel> listByCarId(Long shopId, Long carId);

    /**
     * 获取车辆、车主关联关系
     * @param accountAndCarBO
     * @return
     */
    CustomerCarRel findByCustomerIdAndCarId(AccountAndCarBO accountAndCarBO);

    /**
     * 保存车辆、车主关联关系
     * @param customerCarRel
     */
    void save(CustomerCarRel customerCarRel);

    /**
     * 删除车辆、车主关联关系
     * @param accountAndCarBO
     */
    void delete(AccountAndCarBO accountAndCarBO);

    /**
     * 根据客户id获取关联id
     * @param shopId
     * @param customerId
     * @return
     */
    List<CustomerCarRel> selectByCustomerId(Long shopId,Long customerId);

    List<CustomerCarRel> findCustomerCarRelByAccountId(Long shopId,Long accountId);

}
