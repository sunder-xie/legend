package com.tqmall.legend.web.fileImport.process;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.tqmall.legend.biz.component.CacheComponent;
import com.tqmall.legend.biz.component.CacheKeyConstant;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.legend.biz.customer.CustomerService;
import com.tqmall.legend.common.fileImport.ImportFailedMessages;
import com.tqmall.legend.common.fileImport.CommonFileImportContext;
import com.tqmall.legend.common.fileImport.FileImportProcess;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.insurance.InsuranceCompany;
import com.tqmall.legend.web.fileImport.vo.CustomerCarImportContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by twg on 16/12/10.
 */
@Slf4j
@Component
public class CustomerCarValidationProcess implements FileImportProcess {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerCarService customerCarService;
    @Autowired
    private CacheComponent cacheComponent;

    @Override
    public void process(CommonFileImportContext fileImportContext) {
        if (log.isDebugEnabled()) {
            log.debug("This is CustomerCarValidationProcess");
        }
        CustomerCarImportContext customerCarImportContext = (CustomerCarImportContext) fileImportContext;
        Long shopId = customerCarImportContext.getShopId();
        Long userId = customerCarImportContext.getUserId();
        List<CustomerCar> customerCars = customerCarImportContext.getExcelContents();
        List<String> faildMessages = customerCarImportContext.getFaildMessage();
        Map<Integer, List<String>> rowFaildMessages = customerCarImportContext.getRowFaildMessages();

        /*已存在的车辆信息*/
        List<String> licenses = getLicenses(shopId, customerCars);
        /*已存在的客户信息*/
        Map<String, Customer> customerMap = getCustomerMap(shopId, customerCars);
        Map<String, Long> haodeCustomerIdMap = Maps.newHashMap();
        int customerCarSize = customerCarService.countByShopId(shopId); //匹配haode_user_id

        List<InsuranceCompany> insuranceCompanies = (List<InsuranceCompany>) cacheComponent.getCache(CacheKeyConstant.INSURANCE_COMPANY);
        Map<String, InsuranceCompany> insuranceCompanyMap = Maps.uniqueIndex(insuranceCompanies, new Function<InsuranceCompany, String>() {
            @Override
            public String apply(InsuranceCompany input) {
                return input.getName();
            }
        });

        //导入批次号
        String uuid = UUID.randomUUID().toString();
        List<CustomerCar> customerCarList = Lists.newArrayList();
        List<Customer> customerList = Lists.newArrayList();
        Map<String, CustomerCar> customerCarParamMap = Maps.newHashMap();
        for (int i = 0; i < customerCars.size(); i++) {
            CustomerCar customerCar = customerCars.get(i);
            int rowNumber = customerCar.getRowNumber() + 1;
            String license = customerCar.getLicense().toUpperCase();
            String moblie = customerCar.getMobile();
            String contact = customerCar.getContact();
            customerCar.setShopId(shopId);
            customerCar.setCreator(userId);
            customerCar.setLicense(license);
            if (customerCarParamMap.containsKey(license) || licenses.contains(license)) {
                String faildMessage = String.format(ImportFailedMessages.DEFAULT_REPEAT_MESSAGE, rowNumber, license, "车牌号");
                String failedMs = String.format(ImportFailedMessages.FAILED_REPEAT_MESSAGE, license, "车牌号");
                faildMessages.add(faildMessage);
                setRowFalidMessage(rowFaildMessages, rowNumber, failedMs);
                continue;
            }

            if (StringUtils.isNotBlank(customerCar.getInsuranceCompany()) && insuranceCompanyMap.containsKey(customerCar.getInsuranceCompany())) {
                InsuranceCompany insuranceCompany = insuranceCompanyMap.get(customerCar.getInsuranceCompany());
                customerCar.setInsuranceId(insuranceCompany.getId());
            }
            if (StringUtils.isNotBlank(moblie) && customerMap.containsKey(moblie)) {
                customerCar.setCustomerId(customerMap.get(moblie).getId());
            } else if (StringUtils.isNotBlank(moblie) && haodeCustomerIdMap.containsKey(moblie)) {
                customerCar.setHaodeUserId(haodeCustomerIdMap.get(moblie));
            } else {
                Long haodeUserId = customerCarSize + i + 1L;
                Customer customer = new Customer();
                BeanUtils.copyProperties(customerCar, customer);

                if (StringUtils.isBlank(customerCar.getCustomerName())) {
                    customer.setCustomerName("");
                }
                if (StringUtils.isNotBlank(moblie)) {
                    haodeCustomerIdMap.put(moblie, haodeUserId);
                } else {
                    customer.setMobile("");
                }
                if (StringUtils.isBlank(contact)) {
                    customer.setContact(customerCar.getCustomerName());
                }
                if (StringUtils.isBlank(customerCar.getContactMobile())) {
                    customer.setContactMobile(moblie);
                }
                customer.setImportFlag(uuid);
                customer.setHaodeUserId(haodeUserId);
                customerCar.setHaodeUserId(haodeUserId);
                customerList.add(customer);
            }

            customerCarList.add(customerCar);

            customerCarParamMap.put(license, customerCar);
        }
        customerCarImportContext.setUuid(uuid);
        customerCarImportContext.setCustomers(customerList);
        customerCarImportContext.setExcelContents(customerCarList);
    }

    private void setRowFalidMessage(Map<Integer, List<String>> rowFaildMessages, int rowNumber, String faildMessage) {
        if (rowFaildMessages.containsKey(rowNumber)) {
            List<String> faild = rowFaildMessages.get(rowNumber);
            faild.add(faildMessage);
        } else {
            List<String> failds = Lists.newArrayList();
            failds.add(faildMessage);
            rowFaildMessages.put(rowNumber, failds);
        }
    }

    private List<String> getLicenses(Long shopId, List<CustomerCar> customerCars) {
        Set<String> carLicense = Sets.newHashSet();
        for (CustomerCar customerCar : customerCars) {
            carLicense.add(customerCar.getLicense().toUpperCase());
        }
        if (CollectionUtils.isEmpty(carLicense)) {
            return Lists.newArrayList();
        }
        List<CustomerCar> customerCarList = customerCarService.findCustomerCarsByLicense(shopId, carLicense.toArray(new String[] { }));
        return Lists.transform(customerCarList, new Function<CustomerCar, String>() {
            @Override
            public String apply(CustomerCar input) {
                return input.getLicense();
            }
        });
    }

    private Map<String, Customer> getCustomerMap(Long shopId, List<CustomerCar> customerCars) {
        Set<String> mobiles = Sets.newHashSet();
        for (CustomerCar customerCar : customerCars) {
            if (org.apache.commons.lang3.StringUtils.isNotBlank(customerCar.getMobile())) {
                mobiles.add(customerCar.getMobile());
            }
        }
        if (CollectionUtils.isEmpty(mobiles)) {
            return Maps.newHashMap();
        }
        List<Customer> customers = customerService.getCustomerByMobiles(mobiles, shopId);
        Map<String, Customer> customerMap = Maps.newHashMap();
        for (Customer customer : customers) {
            customerMap.put(customer.getMobile(), customer);
        }
        return customerMap;
    }
}
