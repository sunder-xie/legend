package com.tqmall.legend.biz.customer.impl;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.*;
import com.tqmall.common.UserInfo;
import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.bi.entity.CommonPair;
import com.tqmall.legend.biz.account.AccountInfoService;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.legend.biz.customer.CustomerService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.dao.account.AccountInfoDao;
import com.tqmall.legend.dao.customer.CustomerCDao;
import com.tqmall.legend.dao.customer.CustomerCarDao;
import com.tqmall.legend.dao.customer.CustomerDao;
import com.tqmall.legend.entity.account.AccountInfo;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.entity.customer.CustomerC;
import com.tqmall.legend.entity.customer.CustomerCar;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * Created by litan on 14-11-7.
 */
@Slf4j
@Service
public class CustomerServiceImpl extends BaseServiceImpl implements CustomerService {

    Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);
    @Autowired
    private CustomerCarDao customerCarDao;
    @Autowired
    private CustomerDao customerDao;
    @Autowired
    private CustomerCarService customerCarService;
    @Autowired
    private CustomerCDao customerCDao;
    @Autowired
    private AccountInfoDao accountInfoDao;
    @Autowired
    private AccountInfoService accountInfoService;

    /**
     * 添加客户信息
     *
     * @param customer
     * @return
     */
    @Override
    public Integer add(Customer customer) {
        return customerDao.insert(customer);
    }

    /**
     * 添加客户信息 7
     *
     * @param customer
     * @return
     */
    @Override
    public Integer update(Customer customer) {
        return customerDao.updateById(customer);
    }

    @Override
    public List<Customer> getCustomerByCarId(Map<String, Object> parameters) {

        List<Customer> list = null;

        try {
            list = customerDao.getCustomerByCarId(parameters);
        } catch (Exception e) {
            log.error("DB error", e);
            return null;
        }
        if (!CollectionUtils.isEmpty(list)) {
            return list;
        }
        return null;
    }


    /**
     * 搜索客户列表
     *
     * @param pageable
     * @param searchParams
     * @return
     */
    @Override
    public Page<CustomerCar> getPageCancelCustomer(Pageable pageable,
                                                   Map<String, Object> searchParams) {

        Integer totalSize = customerCarDao.getCustomerCarCount(searchParams);

        PageRequest pageRequest = new PageRequest((pageable.getPageNumber() < 1 ? 1
                : pageable.getPageNumber()) - 1, pageable.getPageSize() < 1 ? 1
                : pageable.getPageSize(), pageable.getSort());
        searchParams.put("limit", pageRequest.getPageSize());
        searchParams.put("offset", pageRequest.getOffset());
        List<CustomerCar> data = customerCarDao.getCustomerCarList(searchParams);
        DefaultPage<CustomerCar> page = new DefaultPage<CustomerCar>(data, pageRequest, totalSize);
        return page;
    }

    @Override
    public List<Customer> getAllCustomers(Long shopId) {

        Map<String, Object> params = new HashMap<>();
        params.put("shopId", shopId);
        return customerDao.select(params);
    }

    @Override
    public Customer selectById(Long id) {
        return customerDao.selectById(id);
    }

    @Override
    public List<CustomerCar> getCustomerCarList(Map<String, Object> params) {
        return customerCarDao.getCustomerCarList(params);
    }

    @Override
    public List<CustomerCar> searchCarList(Map<String, Object> params) {
        List<CustomerCar> customerCarList = customerCarDao.select(params);
        if (!CollectionUtils.isEmpty(customerCarList)) {
            List<Long> ids = new ArrayList<>();
            for (CustomerCar customerCar : customerCarList) {
                Long customerId = customerCar.getCustomerId();
                if (Long.compare(customerId, 0) > 0) {
                    ids.add(customerCar.getCustomerId());
                }
            }
            if (!CollectionUtils.isEmpty(ids)) {
                Long[] customerIds = ids.toArray(new Long[ids.size()]);
                List<Customer> customerList = customerDao.selectByIds(customerIds);
                Map<Long, Customer> customerMap = new HashMap<>();
                for (Customer customer : customerList) {
                    customerMap.put(customer.getId(), customer);
                }
                for (CustomerCar customerCar : customerCarList) {
                    Long customerId = customerCar.getCustomerId();
                    if (customerMap.containsKey(customerId)) {
                        Customer customer = customerMap.get(customerId);
                        customerCar.setCustomerName(customer.getCustomerName());
                        customerCar.setMobile(customer.getMobile());
                    }
                }
            }
        }
        return customerCarList;
    }

    @Override
    public List<Customer> selectByIds(Long[] ids) {
        return customerDao.selectByIds(ids);
    }

    @Override
    public List<Customer> select(Map<String, Object> searchParams) {
        return customerDao.select(searchParams);
    }

    @Override
    public Integer selectCount(Map<String, Object> searchParams) {
        Integer count = customerDao.selectCount(searchParams);
        return count;
    }


    @Override
    public List<Customer> getCustomerByNameMobile(Map<String, Object> searchParams) {
        return customerDao.getCustomerByNameMobile(searchParams);
    }

    @Override
    public List<Customer> getCustomerByMobile(String mobile, Long shopId) {
        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("shopId", shopId);
        searchParams.put("mobile", mobile);
        return this.select(searchParams);
    }

    //两个用户的手机号相同创建一个帐户
    @Override
    public List<Customer> initCustomer(List<Customer> customerList, Long shopId, String uuid) {
        if (!CollectionUtils.isEmpty(customerList)) {
            logger.info("【初始化客户】：批量添加{}条客户信息customer", customerList.size());
            super.batchInsert(customerDao, customerList, 300);

            Map<String, Object> param = new HashMap<>();
            param.put("shopId", shopId);
            param.put("importFlag", uuid);
            customerList = customerDao.select(param);
            List<AccountInfo> accountList = new ArrayList<>();
            for (Customer customer : customerList) {
                AccountInfo accountInfo = new AccountInfo();
                accountInfo.setCreator(customer.getCreator());
                accountInfo.setShopId(customer.getShopId());
                accountInfo.setCustomerId(customer.getId());
                accountInfo.setMobile(customer.getMobile());
                accountList.add(accountInfo);
            }
            if (!CollectionUtils.isEmpty(accountList)) {
                Integer accountSize = super.batchInsert(accountInfoDao, accountList, 300);
                logger.info("【初始化账户】：批量添加{}条账户信息accountInfo", accountSize);
            }
        }
        return customerList;
    }

    /**
     * 初始化客户车辆
     *
     * @param excelList
     * @param shopId
     * @param user
     * @return
     */
    @Override
    //    @Transactional
    public Integer insertCustomerAndCar(List<List<Object>> excelList, Long shopId, UserInfo user, Integer existSize) {
        Integer excelListSize = excelList.size();
        List<Customer> customerList = new ArrayList<>();
        List<CustomerCar> customerCarList = new ArrayList<>();
        //获取数据库门店全部的 电话-客户ID
        Map<String, Long> mobileMap = getMobileMap(shopId);
        //存放excel 的mobile - haodeuserID
        Map<String, Long> excelMap = new HashMap<>();
        //导入批次号
        String uuid = UUID.randomUUID().toString();
        for (int i = 0; i < excelListSize; i++) {
            CustomerCar customerCar = new CustomerCar();
            String license = StringUtils.trim((String) excelList.get(i).get(0));
            if (!StringUtils.isBlank(license)) {
                customerCar.setLicense(license.toUpperCase());
            }
            if (!StringUtils.isBlank((String) excelList.get(i).get(13))) {
                customerCar.setVin((String) excelList.get(i).get(13));
            }
            if (!StringUtils.isBlank((String) excelList.get(i).get(14))) {
                customerCar.setEngineNo((String) excelList.get(i).get(14));
            }
            if (!StringUtils.isBlank((String) excelList.get(i).get(15))) {
                customerCar.setCarBrand((String) excelList.get(i).get(15));
            }
            if (!StringUtils.isBlank((String) excelList.get(i).get(16))) {
                customerCar.setCarSeries((String) excelList.get(i).get(16));
            }
            if (!StringUtils.isBlank((String) excelList.get(i).get(17))) {
                customerCar.setByName((String) excelList.get(i).get(17));
            }
            if (!StringUtils.isBlank((String) excelList.get(i).get(18))) {
                customerCar.setCarCompany((String) excelList.get(i).get(18));
            }
            if (!StringUtils.isBlank((String) excelList.get(i).get(19))) {
                customerCar.setInsuranceTime(DateUtil.convertStringToDateYMD(StringUtil
                        .formatTime((String) excelList.get(i).get(19))));
            }
            if (!StringUtils.isBlank((String) excelList.get(i).get(20))) {
                customerCar.setBuyTime(DateUtil.convertStringToDateYMD(StringUtil
                        .formatTime((String) excelList.get(i).get(20))));
            }
            if (!StringUtils.isBlank((String) excelList.get(i).get(21))) {
                customerCar.setAuditingTime(DateUtil.convertStringToDateYMD(StringUtil
                        .formatTime((String) excelList.get(i).get(21))));
            }
            if (!StringUtils.isBlank((String) excelList.get(i).get(22))) {
                customerCar.setInsuranceCompany((String) excelList.get(i).get(22));
            }
            if (!StringUtils.isBlank((String) excelList.get(i).get(23))) {
                customerCar.setColor((String) excelList.get(i).get(23));
            }
            if (!StringUtils.isBlank((String) excelList.get(i).get(24))) {
                customerCar.setLicenseType((String) excelList.get(i).get(24));
            }
            if (!StringUtils.isBlank((String) excelList.get(i).get(25))) {
                customerCar.setMileage(Long.valueOf((String) excelList.get(i).get(25)));
            }
            customerCar.setShopId(shopId);
            customerCar.setCreator(user.getUserId());
            customerCar.setModifier(user.getUserId());
            // 设置客户信息
            //取mobile对比数据库
            String mobile = ((String) excelList.get(i).get(2)).trim();
            if (StringUtils.isNotBlank(mobile) && mobileMap.containsKey(mobile)) {
                //数据库已有客户 --- 客户无需新增
                customerCar.setCustomerId(mobileMap.get(mobile));
            } else if (StringUtils.isNotBlank(mobile) && excelMap.containsKey(mobile)) {
                //excel表的mobile已经存在 -- 同一个haodeuserid --客户无需新增
                Long existHaodeUserId = excelMap.get(mobile);
                customerCar.setHaodeUserId(existHaodeUserId);
            } else {
                //需要添加客户
                Long haodeUserId = Long.valueOf(existSize + i + 1);
                Customer customer = new Customer();
                if (!StringUtils.isBlank((String) excelList.get(i).get(1))) {
                    customer.setCustomerName((String) excelList.get(i).get(1));
                } else {
                    customer.setCustomerName("");
                }
                if (!StringUtils.isBlank((String) excelList.get(i).get(2))) {
                    customer.setMobile(((String) excelList.get(i).get(2)).trim());
                    excelMap.put(mobile,haodeUserId);
                } else {
                    customer.setMobile("");
                }
                if (!StringUtils.isBlank((String) excelList.get(i).get(3))) {
                    customer.setDrivingLicense((String) excelList.get(i).get(3));
                }
                if (!StringUtils.isBlank((String) excelList.get(i).get(4))) {
                    customer.setCustomerAddr((String) excelList.get(i).get(4));
                }
                if (!StringUtils.isBlank((String) excelList.get(i).get(5))) {
                    customer.setBirthday(DateUtil.convertStringToDateYMD(StringUtil
                            .formatTime((String) excelList.get(i).get(5))));
                }
                if (!StringUtils.isBlank((String) excelList.get(i).get(6))) {
                    customer.setCompany((String) excelList.get(i).get(6));
                }
                if (!StringUtils.isBlank((String) excelList.get(i).get(7))) {
                    customer.setIdentityCard((String) excelList.get(i).get(7));
                }
                if (!StringUtils.isBlank((String) excelList.get(i).get(8))) {
                    customer.setLicenseCheckDate(DateUtil.convertStringToDateYMD(StringUtil
                            .formatTime((String) excelList.get(i).get(8))));
                }
                if (!StringUtils.isBlank((String) excelList.get(i).get(9))) {
                    customer.setLicenseGetDate(DateUtil.convertStringToDateYMD(StringUtil
                            .formatTime((String) excelList.get(i).get(9))));
                }
                if (!StringUtils.isBlank((String) excelList.get(i).get(10))) {
                    customer.setContact((String) excelList.get(i).get(10));
                } else {
                    customer.setContact(customer.getCustomerName());
                }
                if (!StringUtils.isBlank((String) excelList.get(i).get(11))) {
                    customer.setContactMobile(((String) excelList.get(i).get(11)).trim());
                } else {
                    customer.setContactMobile(customer.getMobile());
                }
                if (!StringUtils.isBlank((String) excelList.get(i).get(12))) {
                    customer.setRemark((String) excelList.get(i).get(12));
                }
                if (!StringUtils.isBlank((String) excelList.get(i).get(26))) {
                    customer.setTel((String) excelList.get(i).get(26));
                }
                customer.setShopId(shopId);
                customer.setCreator(user.getUserId());
                customer.setModifier(user.getUserId());
                customer.setHaodeUserId(haodeUserId);
                customerCar.setHaodeUserId(haodeUserId);
                //设置导入批次号
                customer.setImportFlag(uuid);
                customerList.add(customer);
            }
            customerCarList.add(customerCar);
        }
        //先添加客户
        customerList = initCustomer(customerList, shopId, uuid);
        //设置车辆的customerID
        Map<Long, Long> customerMap = new HashMap();
        for (Customer customer : customerList) {
            customerMap.put(customer.getHaodeUserId(), customer.getId());
        }
        for (CustomerCar customerCar : customerCarList) {
            if (null == customerCar.getCustomerId()) {
                customerCar.setCustomerId(customerMap.get(customerCar.getHaodeUserId()));
            }
        }
        Integer totalSize = customerCarService.initCustomerCar(customerCarList);
        return totalSize;
    }

    private Map<String, Long> getMobileMap(Long shopId) {
        Map<String, Object> searchParam = new HashMap<>();
        searchParam.put("shopId", shopId);
        List<Customer> customerList = customerDao.select(searchParam);
        Map<String, Long> cusMap = new HashMap<>();
        if (CollectionUtils.isEmpty(customerList)) {
            return cusMap;
        }
        for (Customer customer : customerList) {
            if (StringUtils.isNotBlank(customer.getMobile())){
                cusMap.put(customer.getMobile(), customer.getId());
            }
        }
        return cusMap;
    }

    /**
     * 根据shopId删除，重新初始化使用
     *
     * @param shopId
     * @return
     */
    @Override
    @Transactional
    public Integer deleteByShopId(Long shopId) {
        Integer deleteCount = customerDao.deleteByShopId(shopId);
        return deleteCount;
    }

    @Override
    public List<CustomerC> selectCustomerCList(Map<String, Object> searchParams) {
        return customerCDao.select(searchParams);
    }

    @Override
    public Optional<Customer> getCustomer(@NotNull Long customerId) {
        Customer customer = null;
        try {
            customer = this.selectById(customerId);
        } catch (Exception e) {
            log.error("[DB]获取客户信息异常，客户ID:{} 异常信息:{}", customerId, e.toString());
            return Optional.absent();
        }

        if (customer == null) {
            return Optional.absent();
        }

        // 客户姓名
        String customerName = customer.getCustomerName();
        customerName = (customerName == null) ? StringUtils.EMPTY : customerName;
        // 客户电话
        String mobile = customer.getMobile();
        mobile = (mobile == null) ? StringUtils.EMPTY : mobile;

        /***
         * 信息补录
         *
         * 1. IF '联系人姓名'不存在 THEN '客户姓名' 填充'联系人姓名'
         * 2. IF '联系人电话'不存在 THEN '客户电话' 填充'联系人姓名'
         */
        if (StringUtil.isStringEmpty(customer.getContact())) {
            customer.setContact(customerName);
        }
        if (StringUtil.isStringEmpty(customer.getContactMobile())) {
            customer.setContactMobile(mobile);
        }

        return Optional.fromNullable(customer);
    }

    @Override
    public List<Customer> searchCompanyList(Map<String, Object> searchMap) {
        return customerDao.searchCompanyList(searchMap);
    }

    @Override
    public Optional<Customer> getCustomer(@NotNull Long customerCarId, @NotNull Long shopId) {
        Map<String, Object> searchParams = new HashMap<String, Object>(2);
        searchParams.put("shopId", shopId);
        searchParams.put("id", customerCarId);

        List<CustomerCar> customerCarList = null;
        try {
            customerCarList = customerCarDao.select(searchParams);
        } catch (Exception e) {
            log.error("[DB]查询legend_customer获取customer异常,参数:{} 异常信息:{}", shopId + "," + customerCarId, e);
            return Optional.absent();
        }

        if (CollectionUtils.isEmpty(customerCarList)) {
            return Optional.absent();
        }
        Long customerId = customerCarList.get(0).getCustomerId();
        Customer customer = customerDao.selectById(customerId);
        return Optional.fromNullable(customer);
    }

    @Override
    public Result deleteById(Long customerId) {
        try {
            customerDao.deleteById(customerId);
            return Result.wrapSuccessfulResult(true);
        } catch (Exception e) {
            log.error("删除客户失败 customerId:{}", customerId, e);
            return Result.wrapErrorResult("", "删除客户失败");
        }
    }

    @Override
    public List<Customer> getCustomerByGroupMobile(Map param) {
        return customerDao.getCustomerByGroupMobile(param);
    }

    @Override
    public List<Customer> getCustomerByMobiles(Set<String> mobiles, Long shopId) {
        return customerDao.selectByMobiles(mobiles, shopId);
    }

    @Override
    public Multimap<String, Long> getPhoneCustomerIdMap(Long shopId) {
        Multimap<String, Long> result = HashMultimap.create();
        List<CommonPair<String, Long>> phoneCustomerIdPairList = customerDao.getPhoneCustomerIdPairList(shopId);
        if (!CollectionUtils.isEmpty(phoneCustomerIdPairList)) {
            for (CommonPair<String, Long> item : phoneCustomerIdPairList) {
                result.put(item.getDataF(), item.getDataS());
            }
        }
        return result;
    }

    @Override
    public boolean isExistMobile(Long shopId, String mobile) {
        Map param = Maps.newHashMap();
        param.put("mobile", mobile);
        param.put("shopId", shopId);
        List<Customer> customerList = customerDao.select(param);
        if (CollectionUtils.isEmpty(customerList)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public Customer addCustomerFromWeChat(Long shopId, String mobile) {
        Customer customer = new Customer();
        customer.setShopId(shopId);
        customer.setMobile(mobile);
        customer.setSource("微信");
        customer.setCustomerName("");
        customerDao.insert(customer);
        return customer;
    }

    @Override
    public List<String> getLicenseByMobile(Long shopId, String mobile) {
        Map param = Maps.newHashMap();
        param.put("mobile", mobile);
        param.put("shopId", shopId);
        List<String> licenseList = Lists.newArrayList();
        List<Customer> customerList = customerDao.select(param);
        if (!CollectionUtils.isEmpty(customerList)) {
            List<Long> customerIds = Lists.transform(customerList, new Function<Customer, Long>() {
                @Override
                public Long apply(Customer customer) {
                    return customer.getId();
                }
            });
            if (!CollectionUtils.isEmpty(customerIds)) {
                Map carQueryParam = Maps.newHashMap();
                carQueryParam.put("customerIds", customerIds);
                List<CustomerCar> customerCars = customerCarDao.selectByCustomerId(carQueryParam);
                for (CustomerCar car : customerCars) {
                    licenseList.add(car.getLicense());
                }
            }
        }
        return licenseList;
    }

    @Override
    public Customer selectById(Long customerId, Long shopId) {
        Map param = Maps.newHashMap();
        param.put("shopId", shopId);
        param.put("id", customerId);
        List<Customer> customerList = customerDao.select(param);
        if (CollectionUtils.isEmpty(customerList)) {
            return null;
        }
        return customerList.get(0);
    }

    @Override
    public List<Customer> listByIds(Long shopId, Collection<Long> customerIds) {
        Assert.notNull(shopId, "shopId can not be null");
        if (CollectionUtils.isEmpty(customerIds)) {
            return  Lists.newArrayList();
        }
        Set<Long> customerIdSet = Sets.newHashSet(customerIds);
        return customerDao.selectByIdss(shopId, customerIdSet);
    }

    @Override
    public ImmutableMap<Long, Customer> getCustomerId2CustomerMap(Long shopId, Collection<Long> customerIds) {
        List<Customer> customers = listByIds(shopId, customerIds);
        return Maps.uniqueIndex(customers, new Function<Customer, Long>() {
            @Override
            public Long apply(Customer input) {
                return input.getId();
            }
        });
    }

    @Override
    public void batchSave(List<Customer> customers) {
        super.batchInsert(customerDao,customers,1000);
    }

    @Override
    @Transactional
    public void insertCustomerAndAccountInfo(Customer customer) {
        customerDao.insert(customer);
        accountInfoService.generateAccountInfo(customer);
        log.info("车辆解绑时，新建客户信息和账户信息");
    }
}
