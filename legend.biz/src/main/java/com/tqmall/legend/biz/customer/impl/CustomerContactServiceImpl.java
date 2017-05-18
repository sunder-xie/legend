package com.tqmall.legend.biz.customer.impl;

import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.customer.ICustomerContactService;
import com.tqmall.legend.dao.customer.CustomerContactDao;
import com.tqmall.legend.entity.customer.CustomerContact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * customerContact Service
 *
 * Created by dongc on 15/7/22.
 */
@Service
public class CustomerContactServiceImpl extends BaseServiceImpl implements ICustomerContactService {

    @Autowired
    private CustomerContactDao customerContactDao;

    @Override
    public List<CustomerContact> queryCustomerContact(Long shopId, String contactName,
                                                      Long customerId, Long customerCarId) {
        Map contactParamMap = new HashMap(4);
        contactParamMap.put("shopId", shopId);
        contactParamMap.put("contact", contactName);
        contactParamMap.put("customerId", customerId);
        contactParamMap.put("customerCarId", customerCarId);
        return customerContactDao.select(contactParamMap);
    }

    @Override
    public int update(CustomerContact customerContact) {
        return customerContactDao.updateById(customerContact);
    }

    @Override
    public int save(CustomerContact customerContact) {
        return customerContactDao.insert(customerContact);
    }

    @Override
    public List<CustomerContact> select(Map<String, Object> param) {
        return customerContactDao.select(param);
    }
}
