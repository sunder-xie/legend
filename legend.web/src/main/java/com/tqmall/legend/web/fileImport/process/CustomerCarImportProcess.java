package com.tqmall.legend.web.fileImport.process;

import com.google.common.collect.Maps;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.legend.biz.customer.CustomerService;
import com.tqmall.legend.common.fileImport.CommonFileImportContext;
import com.tqmall.legend.common.fileImport.FileImportProcess;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.web.fileImport.vo.CustomerCarImportContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by twg on 16/12/10.
 */
@Slf4j
@Component
public class CustomerCarImportProcess implements FileImportProcess {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerCarService customerCarService;

    @Override
    @Transactional
    public void process(CommonFileImportContext fileImportContext) {
        if (log.isDebugEnabled()) {
            log.debug("This is CustomerCarImportProcess");
        }
        CustomerCarImportContext customerCarImportContext = (CustomerCarImportContext) fileImportContext;
        String uuid = customerCarImportContext.getUuid();
        Long shopId = customerCarImportContext.getShopId();
        int hasMobileNum = customerCarImportContext.getHasMobileNum();
        int notMobileNum = customerCarImportContext.getNotMobileNum();
        List<Customer> customerList = customerCarImportContext.getCustomers();
        List<CustomerCar> customerCarList = customerCarImportContext.getExcelContents();
        /*批量添加客户、账户信息，并获取此批量的客户信息，关联车辆的customerId*/
        setCustomerId(shopId, uuid, customerCarList, customerList);
        if (!CollectionUtils.isEmpty(customerCarList)) {
            customerCarService.batchSave(customerCarList);
            customerCarImportContext.setSuccessNum(customerCarList.size());
            for (CustomerCar customerCar : customerCarList) {
                String mobile = customerCar.getMobile();
                if (StringUtils.isNotBlank(mobile)) {
                    ++hasMobileNum;
                } else {
                    ++notMobileNum;
                }
            }
            customerCarImportContext.setHasMobileNum(hasMobileNum);
            customerCarImportContext.setNotMobileNum(notMobileNum);
        }
    }

    private void setCustomerId(Long shopId, String uuid, List<CustomerCar> customerCarList, List<Customer> customerList) {
        customerList = customerService.initCustomer(customerList, shopId, uuid);

        Map<Long, Customer> customerHaodeUserMap = Maps.newHashMap();
        for (Customer customer : customerList) {
            Long haodeUserId = customer.getHaodeUserId();
            if(haodeUserId != null && haodeUserId != 0){
                customerHaodeUserMap.put(haodeUserId,customer);
            }
        }

        for (CustomerCar customerCar : customerCarList) {
            if (customerCar.getCustomerId() == null && customerHaodeUserMap.containsKey(customerCar.getHaodeUserId())) {
                Customer customer = customerHaodeUserMap.get(customerCar.getHaodeUserId());
                customerCar.setCustomerId(customer.getId());
            }
        }
    }
}
