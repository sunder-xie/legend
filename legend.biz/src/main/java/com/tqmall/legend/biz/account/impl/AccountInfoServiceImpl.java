package com.tqmall.legend.biz.account.impl;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.bi.entity.CommonPair;
import com.tqmall.legend.biz.account.*;
import com.tqmall.legend.biz.account.bo.AccountAndCarBO;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.customer.CustomerCarRelService;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.legend.biz.customer.CustomerService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.dao.account.AccountInfoDao;
import com.tqmall.legend.dao.customer.CustomerCarDao;
import com.tqmall.legend.dao.customer.CustomerDao;
import com.tqmall.legend.entity.account.AccountCombo;
import com.tqmall.legend.entity.account.AccountInfo;
import com.tqmall.legend.entity.account.MemberCard;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.customer.CustomerCarRel;
import com.tqmall.wheel.lang.Langs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * @version V1.0
 * @Description: 账户信息Service
 * @date 2016-06-01
 */
@Slf4j
@Service
public class AccountInfoServiceImpl extends BaseServiceImpl implements AccountInfoService {
    @Autowired
    private AccountInfoDao accountInfoDao;
    @Autowired
    private CustomerCarDao customerCarDao;
    @Autowired
    private CustomerDao customerDao;
    @Autowired
    private AccountCouponService accountCouponService;
    @Autowired
    private MemberCardService memberCardService;
    @Autowired
    private MemberCardInfoService memberCardInfoService;
    @Autowired
    private AccountComboService accountComboService;
    @Autowired
    private CustomerCarService customerCarService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerCarRelService customerCarRelService;


    @Override
    public Result generateAccountInfo(Long shopId, Long customerId) {
        if (this.isExist(shopId, customerId)) {
            return Result.wrapErrorResult("", "该客户已存在账户");
        }
        AccountInfo account = new AccountInfo();
        account.setShopId(shopId);
        account.setCustomerId(customerId);
        if (accountInfoDao.insert(account) > 0) {
            return Result.wrapSuccessfulResult("创建账户成功");
        }
        return Result.wrapErrorResult("", "创建账户失败");
    }

    @Override
    public Result generateAccountInfo(Customer customer) {
        if (this.isExist(customer.getShopId(), customer.getId())) {
            return Result.wrapErrorResult("", "该客户已存在账户");
        }
        AccountInfo account = new AccountInfo();
        account.setShopId(customer.getShopId());
        account.setCustomerId(customer.getId());
        account.setCreator(customer.getModifier());
        if (accountInfoDao.insert(account) > 0) {
            return Result.wrapSuccessfulResult("创建账户成功");
        }
        return Result.wrapErrorResult("", "创建账户失败");
    }

    @Override
    public boolean isExist(Long shopId, Long customerId) {
        AccountInfo accountInfo = accountInfoDao.getAccountInfoByCustomerIdAndShopId(shopId, customerId);
        return accountInfo != null ? true : false;
    }

    @Override
    public AccountInfo getAccountInfoByCarIdAndShopId(Long shopId, Long carId) {
        CustomerCar car = customerCarDao.selectById(carId);
        if(car != null && car.getCustomerId() != null){
            AccountInfo accountInfo = accountInfoDao.getAccountInfoByCustomerIdAndShopId(shopId, car.getCustomerId());
            if (getAccountInfo(accountInfo)) {
                return accountInfo;
            }
        }
        return null;
    }

    private boolean getAccountInfo(AccountInfo accountInfo) {
        if(accountInfo != null){
            Customer customer = customerDao.selectById(accountInfo.getCustomerId());
            accountInfo.setCustomer(customer);
            if(customer != null && StringUtils.isNotBlank(customer.getCustomerName())){
                accountInfo.setCustomerName(customer.getCustomerName());
            }else {
                accountInfo.setCustomerName("");
            }
            if(customer != null && StringUtils.isNotBlank(customer.getMobile())){
                accountInfo.setMobile(customer.getMobile());
            }else {
                accountInfo.setMobile("");
            }
            return true;
        }
        return false;
    }

    @Override
    public AccountInfo getAccountInfoById(Long accountId) {
        AccountInfo accountInfo = accountInfoDao.selectById(accountId);
        if (getAccountInfo(accountInfo)) {
            return accountInfo;
        }
        return null;
    }

    @Override
    public AccountInfo getAccountInfoByCustomerIdAndShopId(Long shopId, Long customerId) {
        AccountInfo accountInfo = accountInfoDao.getAccountInfoByCustomerIdAndShopId(shopId, customerId);
        if(accountInfo == null){
            return null;
        }
        return getAccountInfoDetail(shopId, accountInfo);
    }

    @Override
    public AccountInfo getAccountInfoAllById(Long shopId, Long accountId) {
        AccountInfo accountInfo = getAccountInfoById(shopId, accountId);
        if (accountInfo == null) {
            return null;
        }
        return getAccountInfoDetail(shopId,accountInfo);
    }

    @Override
    public AccountInfo getAccountInfoById(Long shopId, Long accountId) {
        Map param = Maps.newHashMap();
        param.put("shopId",shopId);
        param.put("id",accountId);
        List<AccountInfo> accountInfoList = accountInfoDao.select(param);
        if (!CollectionUtils.isEmpty(accountInfoList)) {
            return accountInfoList.get(0);
        }
        return null;
    }

    private AccountInfo getAccountInfoDetail(Long shopId, AccountInfo accountInfo) {
        Long customerId = accountInfo.getCustomerId();
        Customer customer = customerDao.selectById(customerId);
        if(customer != null){
            accountInfo.setCustomer(customer);
            accountInfo.setCustomerCreateTime(DateUtil.convertDateToYMD(customer.getGmtCreate()));
            accountInfo.setCustomerName(customer.getCustomerName());
            accountInfo.setMobile(customer.getMobile());
        }
        Map param = Maps.newHashMap();
        param.put("shopId",shopId);
        param.put("customerId",customerId);
        List<CustomerCar> customerCars = customerCarDao.select(param);//拥有车辆
        List<CustomerCar> customerCarList = getCustomerCarRels(shopId, accountInfo);//关联车辆
        List<CustomerCar> cars = getCustomerCarHasCustomerName(shopId, customerCars, customerCarList);
        accountInfo.setCarLicences(cars);
        accountInfo.setCustomerId(customerId);
        //获取账户下，有效的会员卡信息
        List<MemberCard> memberCardList = memberCardService.getUnExpiredMemberCardListByAccountId(shopId, accountInfo.getId());
        accountInfo.setMemberCards(memberCardList);
        return accountInfo;
    }

    private List<CustomerCar> getCustomerCarHasCustomerName(Long shopId, List<CustomerCar> customerCars, List<CustomerCar> customerCarList) {
        List<CustomerCar> cars = Lists.newLinkedList(customerCars);
        cars.addAll(customerCarList);
        List<Long> customerIds = Lists.transform(cars, new Function<CustomerCar, Long>() {
            @Override
            public Long apply(CustomerCar input) {
                return input.getCustomerId();
            }
        });
        ImmutableMap<Long,Customer> customerMap = customerService.getCustomerId2CustomerMap(shopId, customerIds);

        for (CustomerCar car : cars) {
            if (customerMap.containsKey(car.getCustomerId())){
                Customer c = customerMap.get(car.getCustomerId());
                car.setMobile(c.getMobile());
                car.setCustomerName(c.getCustomerName());
            }
        }
        return cars;
    }

    private List<CustomerCar> getCustomerCarRels(Long shopId, AccountInfo accountInfo) {
        List<CustomerCarRel> customerCarRels = customerCarRelService.findCustomerCarRelByAccountId(shopId, accountInfo.getId());
        List<Long> carIds = Lists.transform(customerCarRels, new Function<CustomerCarRel, Long>() {
            @Override
            public Long apply(CustomerCarRel input) {
                return input.getCustomerCarId();
            }
        });
        if (CollectionUtils.isEmpty(carIds)){
            return Lists.newArrayList();
        }
        List<CustomerCar> customerCarList = customerCarService.findCustomerCarByIds(shopId, carIds);
        return customerCarList;
    }


    @Override
    public Result isDeleteAccountInfo(Long shopId, Long customerId) {
        AccountInfo accountInfo = accountInfoDao.getAccountInfoByCustomerIdAndShopId(shopId, customerId);
        if(accountInfo == null){
            return Result.wrapSuccessfulResult("该客户不存在账户，可以删除");
        }
        if(accountInfo.getBalance().compareTo(BigDecimal.ZERO)!=0 ||
                accountInfo.getExpenseCount().intValue() != 0){
            return Result.wrapErrorResult("", "该客户账户已有消费，不能删除");
        }
        if(accountCouponService.isExist(shopId, accountInfo.getId())){
            return Result.wrapErrorResult("","该客户账户已存在多种券，不能删除");
        }
        List<AccountCombo> accountComboList = accountComboService.listCombo(shopId,accountInfo.getId());
        if(!CollectionUtils.isEmpty(accountComboList)){
            return Result.wrapErrorResult("","该客户账户已存在计次卡，不能删除");
        }
        if(memberCardService.isExist(shopId, accountInfo.getId())){
            return Result.wrapErrorResult("","该客户账户已存在会员卡，不能删除");
        }

        return Result.wrapSuccessfulResult("该客户账户还未存在消费记录和卡券，可以删除");
    }

    @Override
    public Result deleteAccountInfo(Long shopId, Long customerId) {
        AccountInfo accountInfo = accountInfoDao.getAccountInfoByCustomerIdAndShopId(shopId, customerId);
        if(accountInfo != null){
            accountInfoDao.deleteById(accountInfo.getId());
            return Result.wrapSuccessfulResult("该客户账户删除成功");
        }
        return Result.wrapErrorResult("","该客户不存在账户") ;
    }

    @Override
    public List<AccountInfo> getInfoByIds(Collection<Long> ids) {
        if (Langs.isEmpty(ids)) {
            return Collections.emptyList();
        }
        List<AccountInfo> accountInfos = this.selectByIds(accountInfoDao, new ArrayList(ids));
        /**
         * 组装客户信息
         */
        List<Long> customerIds = new LinkedList<>();
        for (AccountInfo accountInfo : accountInfos) {
            customerIds.add(accountInfo.getCustomerId());
        }
        if(CollectionUtils.isEmpty(customerIds)){
            return accountInfos;
        }
        Map<String, Object> param = new HashMap<>();
        param.put("customerIdList", customerIds);
        List<Customer> customers = customerDao.select(param);
        ImmutableMap<Long, Customer> customerImmutableMap = Maps.uniqueIndex(customers, new Function<Customer, Long>() {
            @Override
            public Long apply(Customer input) {
                return input.getId();
            }
        });
        for (AccountInfo accountInfo : accountInfos) {
            Customer customer = customerImmutableMap.get(accountInfo.getCustomerId());
            if(customer == null){
                log.error("账户获取客户信息失败,accountId:{},customerId:{}",accountInfo.getId(),accountInfo.getCustomerId());
                continue;
            }
            accountInfo.setCustomer(customer);
            accountInfo.setCustomerName(customer.getCustomerName());
            accountInfo.setMobile(customer.getMobile());
        }
        return accountInfos;
    }

    @Override
    public List<AccountInfo> getInfoByCustomerIds(List<Long> ids) {
        Map<String, Object> param = new HashMap<>();
        param.put("customerIds",ids);
        return accountInfoDao.select(param);
    }

    @Override
    public void batchInsert(List<AccountInfo> accountInfoList) {
        accountInfoDao.batchInsert(accountInfoList);
    }

    @Override
    public Map<Long, Long> getCustomerIdAccountIdMap(Long shopId) {
        Map<Long, Long> result = Maps.newHashMap();
        List<CommonPair<Long,Long>> customerIdAccountIdPairList =
                accountInfoDao.getCustomerIdAccountIdPair(shopId);
        if (!CollectionUtils.isEmpty(customerIdAccountIdPairList)) {
            for (CommonPair<Long,Long> item : customerIdAccountIdPairList) {
                result.put(item.getDataF(),item.getDataS());
            }
        }
        return result;
    }

    @Override
    public synchronized boolean createWeChatAccount(Long shopId, String mobile) {
        if(!customerService.isExistMobile(shopId, mobile)){
            Customer customer = customerService.addCustomerFromWeChat(shopId,mobile);
            com.tqmall.legend.common.Result result = this.generateAccountInfo(customer);
            log.info("微信客户创建云修账户信息:{},mobile:{}",result.getData(),mobile);
            return true;
        }else {
            log.info("手机号已存在,微信客户创建云修账户失败,mobile:{}",mobile);
            return false;
        }
    }

    @Override
    public List<AccountInfo> getAccountInfoByMobile(Long shopId, String mobile) {
        List<Customer> customers = customerService.getCustomerByMobile(mobile, shopId);
        int size = customers.size();
        if (size == 0) {
            log.error("根据手机号查询账户出错,shopID={},mobile={},customer个数{}",shopId,mobile, size);
            throw new BizException("该手机号无对应账户");
        }
        List<Long> customerIdList = Lists.transform(customers, new Function<Customer, Long>() {
            @Override
            public Long apply(Customer input) {
                return input.getId();
            }
        });

        List<AccountInfo> accountInfoList = accountInfoDao.selectByCustomerIds(shopId, customerIdList);
        return accountInfoList;
    }

    @Override
    public List<String> listLicenseByAccountId(Long shopID, Long accountId) {
        AccountInfo accountInfo = this.getAccountInfoById(shopID, accountId);
        Long customerId = accountInfo.getCustomerId();
        List<CustomerCar> customerCarList = customerCarService.listByCustomerId(shopID, customerId);
        List<String> licenseList = Lists.newArrayList();
        for (CustomerCar car : customerCarList) {
            licenseList.add(car.getLicense());
        }
        return licenseList;
    }

    @Override
    public boolean bundlingCarToAccount(AccountAndCarBO accountAndCarBO) {
        String license = accountAndCarBO.getLicense().trim().toUpperCase(Locale.SIMPLIFIED_CHINESE);
        Long accountId = accountAndCarBO.getAccountId();
        boolean isChange = accountAndCarBO.isChange();
        Long shopId = accountAndCarBO.getShopId();
        CustomerCar customerCar = customerCarService.selectByLicenseAndShopId(license, shopId);
        if (customerCar == null){
            this.bundCustomerCarToAccountInfo(accountAndCarBO);
            return true;
        }
        AccountInfo accountInfo = this.getAccountInfoById(accountId);
        accountAndCarBO.setCustomerCarId(customerCar.getId());
        if (!isChange && !accountInfo.getCustomerId().equals(customerCar.getCustomerId())){
            //有车主存在，不更换车主时
            CustomerCarRel customerCarRel = customerCarRelService.findByCustomerIdAndCarId(accountAndCarBO);
            if (customerCarRel == null){
                customerCarRel = new CustomerCarRel();
                customerCarRel.setShopId(shopId);
                customerCarRel.setAccountId(accountInfo.getId());
                customerCarRel.setCustomerCarId(customerCar.getId());
                customerCarRel.setCreator(accountAndCarBO.getUserId());
                customerCarRel.setCustomerId(accountInfo.getCustomerId());
                customerCarRelService.save(customerCarRel);
                return true;
            }
        }else if (!accountInfo.getCustomerId().equals(customerCar.getCustomerId())){
            //有车主存在，更换车主时，建立拥有关系，并删除关联关系。
            accountAndCarBO.setAccountId(null);
            customerCarRelService.delete(accountAndCarBO);
            customerCar.setCustomerId(accountInfo.getCustomerId());
            customerCarService.update(customerCar);
            return true;
        }
        log.info("门店id：{}，此车辆：{}，已绑定成功，请勿重复操作。",shopId,license);
        return false;
    }

    @Override
    @Transactional
    public void bundCustomerCarToAccountInfo(AccountAndCarBO accountAndCarBO) {
        AccountInfo accountInfo = this.getAccountInfoById(accountAndCarBO.getAccountId());
        Customer customer = accountInfo.getCustomer();
        CustomerCar customerCar = new CustomerCar();
        customerCar.setCreator(accountAndCarBO.getUserId());
        customerCar.setLicense(accountAndCarBO.getLicense());
        customerCar.setShopId(customer.getShopId());
        customerCar.setCustomerId(customer.getId());
        customerCarService.save(customerCar);
    }

    @Override
    public boolean unBundlingCarFromAccount(AccountAndCarBO accountAndCarBO) {
        AccountInfo accountInfo = this.getAccountInfoById(accountAndCarBO.getAccountId());
        accountAndCarBO.setCustomerId(accountInfo.getCustomerId());
        CustomerCar customerCar = customerCarService.selectById(accountAndCarBO.getCustomerCarId());
        //拥有关系
        if (customerCar.getCustomerId().equals(accountInfo.getCustomerId())){
            Customer customer = new Customer();
            customer.setShopId(accountAndCarBO.getShopId());
            customer.setCreator(accountAndCarBO.getUserId());
            customer.setModifier(accountAndCarBO.getUserId());
            this.customerService.insertCustomerAndAccountInfo(customer);
            customerCar.setCustomerId(customer.getId());
            customerCarService.update(customerCar);
            return true;
        }
        //关联关系
        customerCarRelService.delete(accountAndCarBO);
        return true;
    }

    @Override
    public List<AccountInfo> getBindAccountInfosByCarId(Long shopId, Long carId) {
        List<CustomerCarRel> customerCarRels = customerCarRelService.listByCarId(shopId, carId);
        List<Long> accountIds = Lists.transform(customerCarRels, new Function<CustomerCarRel, Long>() {
            @Override
            public Long apply(CustomerCarRel input) {
                return input.getAccountId();
            }
        });
        if (CollectionUtils.isEmpty(accountIds)){
            return Lists.newArrayList();
        }
        return this.getInfoByIds(accountIds);
    }
}