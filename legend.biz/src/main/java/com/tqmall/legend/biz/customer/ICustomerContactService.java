package com.tqmall.legend.biz.customer;

import com.tqmall.legend.entity.customer.CustomerContact;

import java.util.List;
import java.util.Map;

/**
 * customerContact service interface
 *
 * Created by dongc on 15/7/22.
 */
public interface ICustomerContactService {


    /**
     * query CustomerContact
     *
     * @param shopId
     *          门店ID
     * @param contactName
     *          姓名
     * @param customerId
     *          编号
     * @param customerCarId
     *          客户车编号
     * @return
     */
    List<CustomerContact> queryCustomerContact(Long shopId, String contactName,
                                               Long customerId, Long customerCarId);

    /**
     *
     * update CustomerContact
     *
     * @param customerContact
     *
     * @return
     */
    int update(CustomerContact customerContact);

    /**
     * save CustomerContact
     *
     * @param customerContact
     * @return
     */
    int save(CustomerContact customerContact);

    List<CustomerContact> select(Map<String, Object> param);

}
