package com.tqmall.legend.biz.customer.impl;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.athena.client.car.CarCategoryService;
import com.tqmall.athena.client.maintain.MaintainService;
import com.tqmall.athena.domain.param.maintain.CarMaintainPO;
import com.tqmall.athena.domain.result.carcategory.CarCategoryDTO;
import com.tqmall.athena.domain.result.carcategory.CarListDTO;
import com.tqmall.athena.domain.result.maintain.AdviseMaintainDTO;
import com.tqmall.common.Constants;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.DateUtil;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.cube.shop.RpcCustomerInfoService;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.legend.biz.account.AccountInfoService;
import com.tqmall.legend.biz.account.bo.AccountAndCarBO;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.customer.CustomerCarFileService;
import com.tqmall.legend.biz.customer.CustomerCarRelService;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.legend.biz.customer.CustomerService;
import com.tqmall.legend.biz.customer.CustomerSourceService;
import com.tqmall.legend.biz.customer.CustomerUserRelService;
import com.tqmall.legend.biz.marketing.gather.GatherCouponFlowDetailService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.common.LegendError;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.dao.customer.CustomerCarDao;
import com.tqmall.legend.dao.customer.CustomerContactDao;
import com.tqmall.legend.dao.customer.CustomerDao;
import com.tqmall.legend.entity.customer.AppointAppVo;
import com.tqmall.legend.entity.customer.CarConcision;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.customer.CustomerCarBo;
import com.tqmall.legend.entity.customer.CustomerCarByModel;
import com.tqmall.legend.entity.customer.CustomerCarComm;
import com.tqmall.legend.entity.customer.CustomerCarFile;
import com.tqmall.legend.entity.customer.CustomerContact;
import com.tqmall.legend.entity.customer.CustomerPerfectOfCarWashEntity;
import com.tqmall.legend.entity.customer.CustomerSource;
import com.tqmall.legend.entity.customer.CustomerUserRel;
import com.tqmall.legend.entity.insurance.InsuranceBill;
import com.tqmall.legend.entity.marketing.gather.GatherCouponFlowDetail;
import com.tqmall.legend.entity.order.OrderInfo;
import com.tqmall.legend.facade.customer.CustomerCarFacade;
import com.tqmall.legend.facade.shop.ShopFunFacade;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by litan on 14-11-9.
 */
@Service
@Slf4j
public class CustomerCarServiceImpl extends BaseServiceImpl implements CustomerCarService {
    Logger logger = LoggerFactory.getLogger(CustomerCarServiceImpl.class);

    @Autowired
    CustomerService customerService;
    @Autowired
    private CustomerCarDao customerCarDao;
    @Autowired
    private CustomerDao customerDao;
    @Autowired
    private CustomerContactDao customerContactDao;
    @Autowired
    private CustomerSourceService customerSourceService;
    @Autowired
    private CarCategoryService carCategoryService;
    @Autowired
    private CustomerCarFileService customerCarFileService;
    @Autowired
    private MaintainService maintainService;
    @Autowired
    private AccountInfoService accountInfoService;
    @Autowired
    private CustomerCarFacade customerCarFacade;
    @Autowired
    private CustomerUserRelService customerUserRelService;
    @Autowired
    private RpcCustomerInfoService rpcCustomerInfoService;
    @Autowired
    private ShopFunFacade shopFunFacade;
    @Autowired
    private GatherCouponFlowDetailService gatherCouponFlowDetailService;
    @Autowired
    private CustomerCarRelService customerCarRelService;

    /**
     * 更新车辆信息,底层方法
     */
    private CustomerCar _updateCustomerCar(CustomerCar cc, UserInfo userInfo) {
        /**
         * 入参格式校验
         */
        if (userInfo == null) {
            throw new BizException("用户登录信息不能为空.");
        }
        if (cc == null || cc.getId() == null) {
            throw new BizException("车辆信息或车辆id不能为空.");
        }
        CustomerCar occ = selectById(cc.getId());
        if (occ == null) {
            throw new BizException("车辆ID为" + cc.getId() + "的车辆不存在");
        }


        /**
         * 1.更新客户信息
         */
        Customer customer = this._updateCustomerByCarInfo(cc, userInfo);

        /**
         * 2.预初始化操作
         */
        //2.1 通用属性预初始化
        cc = this.setCarField(cc, userInfo);
        //2.2 更新属性初始化
        cc.setModifier(userInfo.getUserId());
        cc.setGmtModified(DateUtil.currentDate());
        Long shopId = userInfo.getShopId();


        /**
         * 更新车辆保养时间时会产生保养新记录
         */
        if (customerCarDao.updateById(cc) > Constants.FAIL_FLAG) {
            Map param = Maps.newConcurrentMap();
            processCarFile(cc.getCarPictureUrl(), param, shopId, cc);
        }

        /**
         * 3.更新联系人信息
         */
        this.addOrUpdateCustomerContact(cc, userInfo);

        CustomerCar customerCar = selectById(cc.getId());
        // 关联车主姓名和车主电话
        customerCar.setCustomerName(customer.getCustomerName());
        customerCar.setMobile(customer.getMobile());
        return customerCar;
    }

    @Override
    public List<CustomerCarByModel> queryCustomerCarByModel(Map<String, Object> paramMap) {
        return customerCarDao.queryCustomerCarByModel(paramMap);
    }

    private CustomerContact addOrUpdateCustomerContact(CustomerCar customerCar, UserInfo userInfo) {
        CustomerContact customerContact = null;
        Long customerContactId = null;
        if (StringUtils.isNotEmpty(customerCar.getContact())) {
            
            Map contactMap = new HashMap();
            contactMap.put("shopId", userInfo.getShopId());
            contactMap.put("contact", customerCar.getContact());
            contactMap.put("customerId", customerCar.getCustomerId());
            contactMap.put("customerCarId", customerCar.getCarId());
            List<CustomerContact> customerContactList = customerContactDao.select(contactMap);

            /**
             * I.联系人不存在,则新增
             * II.联系人存在,则更新
             */
            if (CollectionUtils.isEmpty(customerContactList)) {
                customerContact = this.setCusContactField(customerCar, userInfo, new CustomerContact());
                customerContact.setGmtCreate(DateUtil.currentDate());
                customerContact.setCreator(userInfo.getUserId());
                customerContactDao.insert(customerContact);
                customerContactId = customerContact.getId();
            } else {
                customerContact = customerContactList.get(0);
                customerContactId = customerContact.getId();
                customerContact.setGmtModified(DateUtil.currentDate());
                customerContact = this.setCusContactField(customerCar, userInfo, customerContact);
                customerContactDao.updateById(customerContact);
            }
        }
        return customerContactDao.selectById(customerContactId);

    }

    @Override
    public List<CustomerCarComm> queryCustomerCar(Map<String, Object> paramMap) {
        return customerCarDao.queryCustomerCar(paramMap);
    }

    private Customer _updateCustomerByCarInfo(CustomerCar cc, UserInfo userInfo) {
        if (cc == null || cc.getCustomerId() == null) {
            throw new BizException("车辆信息或客户id不能为空.");
        }
        if (this.customerDao.selectById(cc.getCustomerId()) == null) {
            throw new BizException("id为" + cc.getCustomerId() + "的客户信息不存在.");
        }

        CustomerCar occ = selectById(cc.getId());
        if (occ == null) {
            throw new BizException("id为" + cc.getId() + "的车辆不存在.");
        }
        //不对电话号码做帐户逻辑的处理,直接更新
        Customer customer = this.setCustomerField(cc, userInfo);
        this.customerDao.updateById(customer);
        Result result = accountInfoService.generateAccountInfo(customer);
        if (result.isSuccess()) {
            log.info("通过shopId={},customerId={},添加账户信息成功", userInfo.getShopId(), customer.getId());
        }
//        customer = this.customerDao.selectById(customer.getId());
        return customer;
    }

    @Override
    @Transactional
    public Result add(CustomerCar customerCar, UserInfo userInfo) {
        insertCustomerSourceInfo(customerCar, userInfo);

        Customer customer = generateCustomerAndAccount(customerCar, userInfo);

        /**
         * 设置车辆信息
         */
        setCarField(customerCar, userInfo);
        customerCar.setCustomerId(customer.getId());
        customerCar.setCreator(userInfo.getUserId());
        customerCar.setPrecheckCount(Constants.DB_DEFAULT_INT);
        customerCar.setMaintainCount(Constants.DB_DEFAULT_INT);
        customerCar.setRepairCount(Constants.DB_DEFAULT_INT);
        log.info("[新增客户车辆]customerCar:{}",customerCar);
        customerCarDao.insert(customerCar);

        //新增客户归属
        allotCustomerCar(customerCar, userInfo, customer);

        if (!StringUtils.isBlank(customerCar.getContact())) {
            CustomerContact customerContact = new CustomerContact();
            customerContact.setCreator(userInfo.getUserId());
            customerContact = setCusContactField(customerCar, userInfo, customerContact);
            customerContactDao.insert(customerContact);
        }

        return Result.wrapSuccessfulResult(customerCar);
        
       
    }

    /**
     * 分配客户归属
     * @param customerCar
     * @param userInfo
     * @param customer
     */
    private void allotCustomerCar(CustomerCar customerCar, UserInfo userInfo, Customer customer) {
        Long userId = userInfo.getUserId();
        Long shopId = userInfo.getShopId();
        boolean isYBD = shopFunFacade.isYBD(shopId);
        if (userId == null || Long.compare(userId, 0l) == 0 || !isYBD) {
            return;
        }
        //查询是否是老客户拉新带来的客户
        String mobile = customer.getMobile();
        if (StringUtils.isNotBlank(mobile)) {
            GatherCouponFlowDetail gatherCouponFlowDetail = gatherCouponFlowDetailService.getLatelyFlow(shopId, mobile, null);
            if (gatherCouponFlowDetail != null) {
                Long referUserId = gatherCouponFlowDetail.getReferUserId();
                if (Long.compare(referUserId, 0l) == 1) {
                    userId = referUserId;
                }
            }
        }
        Long customerCarId = customerCar.getId();
        CustomerUserRel customerUserRel = new CustomerUserRel();
        customerUserRel.setShopId(shopId);
        customerUserRel.setUserId(userId);
        customerUserRel.setCustomerCarId(customerCarId);
        customerUserRel.setCustomerId(customer.getId());
        customerUserRel.setCreator(userId);
        customerUserRelService.insert(customerUserRel);
        //同步cube
        com.tqmall.core.common.entity.Result<Boolean> allotCustomerCarsResult = rpcCustomerInfoService.allotCustomerCarIfPossible(shopId, userId, customerCarId);
        if (!allotCustomerCarsResult.isSuccess()) {
            log.error("新建车辆：调用cube同步客户归属接口异常，shopId：{}，userId：{}，车辆id：{}", shopId, userId, customerCarId);
            throw new BizException("同步客户信息失败，请稍后再试");
        }
    }

    //新建客户,同时新建帐户
    private Customer generateCustomerAndAccount(CustomerCar customerCar, UserInfo userInfo) {
        if (StringUtils.isBlank(customerCar.getMobile())) {
            return InsertCustomerAndAccountInfo(customerCar, userInfo);
        }
        Map param = Maps.newHashMap();
        param.put("shopId", userInfo.getShopId());
        param.put("mobile", customerCar.getMobile());
        List<Customer> customers = customerService.select(param);
        if (!CollectionUtils.isEmpty(customers)) {
            log.info("根据手机号：{}，新建客户时已存在客户信息", customerCar.getMobile());
            Customer customer = setCustomerField(customerCar, userInfo);
            customer.setId(customers.get(0).getId());
            customerService.update(customer);
            return customer;
        }
        return InsertCustomerAndAccountInfo(customerCar, userInfo);
    }

    private Customer InsertCustomerAndAccountInfo(CustomerCar customerCar, UserInfo userInfo) {
        Customer customer = setCustomerField(customerCar, userInfo);
        customer.setCustomerName(customerCar.getCustomerName());
        customer.setShopId(userInfo.getShopId());
        customer.setCreator(userInfo.getUserId());
        customer.setSource(customerCar.getCustomerSource());
        customerService.add(customer);
        Result result = accountInfoService.generateAccountInfo(customer);
        if (result.isSuccess()) {
            log.info("通过shopId={},customerId={},添加账户信息成功", userInfo.getShopId(), customer.getId());
        }
        return customer;
    }

    private void insertCustomerSourceInfo(CustomerCar customerCar, UserInfo userInfo) {
        if (StringUtils.isNotBlank(customerCar.getCustomerSource())) {
            if (null == customerSourceService.getCustomerSource(customerCar.getCustomerSource(), userInfo.getShopId())) {
                CustomerSource customerSource = new CustomerSource();
                customerSource.setShopId(userInfo.getShopId());
                customerSource.setSource(customerCar.getCustomerSource());
                customerSource.setCreator(userInfo.getUserId());
                customerSource.setModifier(userInfo.getUserId());
                customerSource.setGmtModified(new Date());
                customerSource.setGmtCreate(new Date());
                customerSourceService.addCustomerSource(customerSource);
            }
        }
    }

    private Result carCategory(Integer pid) {
        com.tqmall.core.common.entity.Result<List<CarListDTO>> result = this.carCategoryService.categoryCarInfo(pid);

        if (result != null && result.isSuccess() && result.getData() != null) {
            List<CarListDTO> carCatDTOs = result.getData();
            if (Integer.valueOf(0).equals(pid)) {
                carCatDTOs.add(_newOther());
            }

            /**
             * 在新增客户弹框中,车型选择必须为二级,为兼容,对其他车型增加其他二级分类
             */
            if (Integer.valueOf(-1).equals(pid)) {
                carCatDTOs = new LinkedList<>();
                CarListDTO carCategoryDTO1 = new CarListDTO();
                carCategoryDTO1.setId(-2);
                carCategoryDTO1.setPid(-1);
                carCategoryDTO1.setBrand("未知");
                carCategoryDTO1.setName("其他");
                carCategoryDTO1.setImportInfo("");
                carCategoryDTO1.setFirstWord("Q");
                carCategoryDTO1.setLevel(0);
                carCategoryDTO1.setCarType("未知");
                carCatDTOs.add(carCategoryDTO1);
            }
            return Result.wrapSuccessfulResult(carCatDTOs);

        } else {
            log.error("从数据团队获取车型信息失败.{}", LogUtils.funToString(pid, result));
            return Result.wrapErrorResult("9999", "获取车型失败");
        }
    }

    /**
     * 目前车型库不能包含所有车型,淘汽云修增加id为-1的其他分类
     */
    private CarListDTO _newOther() {
        CarListDTO carCategoryDTO = new CarListDTO();
        carCategoryDTO.setId(-1);
        carCategoryDTO.setName("其他");
        carCategoryDTO.setBrand("未知");
        carCategoryDTO.setImportInfo("");
        carCategoryDTO.setFirstWord("Q");
        carCategoryDTO.setLevel(0);
        carCategoryDTO.setCarType("未知");
        return carCategoryDTO;
    }

    @Override
    @Transactional
    public Result update(CustomerCar customerCar, UserInfo userInfo) {
        //设置客户属性
        Customer customer = setCustomerField(customerCar, userInfo);
        //手机号码填写了才调用bind方法变更了手机号
        Customer oldCustomer = customerDao.selectById(customerCar.getCustomerId());
        String mobile = customer.getMobile();
        String oldMobile = oldCustomer == null ? null : oldCustomer.getMobile();
        if (StringUtils.isNotBlank(mobile) && !mobile.equals(oldMobile)) {//只要填了手机号,手机号都不为空
            List<Customer> list = customerService.getCustomerByMobile(mobile, userInfo.getShopId());
            //如果存在已有车主
            //执行更新成奥做
            if (!CollectionUtils.isEmpty(list)) {
                Long newCustomerId = list.get(0).getId();
                customer.setId(newCustomerId);
                customerDao.updateById(customer);
            } else {
                //如果不在车主
                //插入新客户
                if (customerCar.getCustomerId() == null || customerCar.getCustomerId() == 0) {
                    customerDao.insert(customer);//解绑
                } else {
                    customerDao.updateById(customer);//洗车单
                }
                Result result = accountInfoService.generateAccountInfo(customer);
                if (!result.isSuccess()) {
                    log.info("根据参数：绑定车辆信息失败，返回信息：{}", result.getErrorMsg());
                }
            }
        } else {
            if (customerCar.getCustomerId() == null || customerCar.getCustomerId() == 0) {
                //插入新客户
                customerDao.insert(customer);
            } else {
                //更新客户
                customerDao.updateById(customer);
            }
        }
        //修改客户信息
        //设置车辆属性
        customerCar = setCarField(customerCar, userInfo);
        customerCar.setCustomerId(customer.getId());
        //修改联系人
        this.addOrUpdateCustomerContact(customerCar, userInfo);
        log.info("[修改客户车辆信息]customerCar:{}",customerCar);
        if (customerCarDao.updateById(customerCar) > Constants.FAIL_FLAG) {
            return Result.wrapSuccessfulResult(customerCar);
        }

        return Result.wrapErrorResult("", "更新客户信息失败");
    }


    @Override
    @Transactional
    public Result changeCustomer(String mobile, UserInfo userInfo , Long carId) {
        Long shopId = userInfo.getShopId();
        //校验手机号是否重复
        CustomerCar car = customerCarDao.selectById(carId);
        if (null == car) {
            log.error("[变更电话错误]:未找到对应的车辆 carId:{},shopId:{}", carId,shopId);
            return Result.wrapErrorResult("", "未找到对应的车辆");
        }
        String oldMobile = car.getMobile();
        Long oldCustomer = car.getCustomerId();
        if (mobile.equals(oldMobile)) {
            log.error("[变更电话错误]:电话号码没有发生变动,无需更改 mobile:{}", mobile);
            return Result.wrapErrorResult("", "电话号码没有发生变动,无需更改");
        }
        List<Customer> list = customerService.getCustomerByMobile(mobile, shopId);
        Long newCustomerId ;
        car.setModifier(userInfo.getUserId());
        if (!CollectionUtils.isEmpty(list)) {
            //车辆绑定到另一个客户下
            newCustomerId = list.get(0).getId();
            car.setCustomerId(newCustomerId);
        } else {
            //新增一个客户,并把车辆绑定到客户下
            Customer customer = new Customer();
            customer.setMobile(mobile);
            customer.setShopId(shopId);
            customer.setCreator(userInfo.getUserId());
            customerDao.insert(customer);
            accountInfoService.generateAccountInfo(customer);
            newCustomerId = customer.getId();
            car.setCustomerId(newCustomerId);
        }
        customerCarDao.updateById(car);
        removeCustomerCarRel(carId, shopId);
        log.info("[更新车主]从旧车主id:{},到新车主id{},操作人:{}",oldCustomer,newCustomerId,userInfo.getUserId());
        return Result.wrapSuccessfulResult(car);
    }

    private void removeCustomerCarRel(Long carId, Long shopId) {
        //删除原有车辆的关联关系
        AccountAndCarBO accountAndCarBO = new AccountAndCarBO();
        accountAndCarBO.setShopId(shopId);
        accountAndCarBO.setCustomerCarId(carId);
        customerCarRelService.delete(accountAndCarBO);
    }

    private CustomerCar setCarField(CustomerCar customerCar, UserInfo userInfo) {
        customerCar.setLicense(customerCar.getLicense() == null ? null : customerCar.getLicense().replace(" ", "").toUpperCase());
        customerCar.setShopId(userInfo.getShopId());
        customerCar.setModifier(userInfo.getUserId());
        if (customerCar.getVin() != null) {
            customerCar.setVin(customerCar.getVin().toUpperCase());
        }
        if (customerCar.getId() == null) {
            // 新增客户时，发动号不是必须的
            customerCar.setEngineNo((customerCar.getEngineNo() == null) ? "" : customerCar.getEngineNo().toUpperCase());
        }
        customerCar.setBuyTime(DateUtil.convertStringToDateYMD(customerCar.getBuyTimeStr()));
        customerCar.setAuditingTime(DateUtil.convertStringToDateYMD(customerCar.getAuditingTimeStr()));
        customerCar.setReceiveLicenseTime(DateUtil.convertStringToDateYMD(customerCar.getReceiveLicenseTimeStr()));
        customerCar.setInsuranceTime(DateUtil.convertStringToDateYMD(customerCar.getInsuranceTimeStr()));
        customerCar.setInsuranceId(customerCar.getInsuranceId());
        customerCar.setInsuranceCompany(customerCar.getInsuranceCompany());
        // 设置出厂年月
        customerCar.setProductionDate(DateUtil.convertStringToDateYM(customerCar.getProductionDateStr()));
        customerCar.setKeepupTime(DateUtil.convertStringToDateYMD(customerCar.getKeepupTimeStr()));
        // 设置车辆级别
        customerCar.setCarLevel(getCarLevel(customerCar.getCarModelId()));
        if (customerCar.getCarModelId() != null) {
            Result result = carCategory(customerCar.getCarModelId().intValue());
            if (result != null && result.isSuccess() && result.getData() != null) {
                List<CarListDTO> carModelVos = (List<CarListDTO>) result.getData();
                if (!CollectionUtils.isEmpty(carModelVos)) {
                    customerCar.setCarType(carModelVos.get(0).getCarType());
                }
            }
        }
        return customerCar;
    }

    private Customer setCustomerField(CustomerCar customerCar, UserInfo userInfo) {
        Customer customer = new Customer();
        if (customerCar.getCustomerId() != null && customerCar.getCustomerId() != 0) {
            customer.setId(customerCar.getCustomerId());
        }
        customer.setShopId(userInfo.getShopId());
        customer.setCustomerName(customerCar.getCustomerName());
        customer.setMobile(customerCar.getMobile());
        customer.setTel(customerCar.getTel());
        customer.setModifier(userInfo.getUserId());
        customer.setDrivingLicense(customerCar.getDrivingLicense());
        customer.setCustomerAddr(customerCar.getCustomerAddr());
        customer.setBirthday(DateUtil.convertStringToDateYMD(customerCar.getBirthdayStr()));
        customer.setCompany(customerCar.getCompany());
        customer.setIdentityCard(customerCar.getIdentityCard());
        customer.setContact(customerCar.getContact());
        customer.setContactMobile(customerCar.getContactMobile());
        customer.setSource(customerCar.getCustomerSource());
        customer.setGmtModified(DateUtil.currentDate());
        customer.setRemark(customerCar.getRemark());
        if (StringUtils.isNotBlank(customerCar.getVer())) {
            customer.setVer(customerCar.getVer());
        }
        if (StringUtils.isNotBlank(customerCar.getRefer())) {
            customer.setRefer(customerCar.getRefer());
        }
        return customer;
    }

    private CustomerContact setCusContactField(CustomerCar customerCar, UserInfo userInfo, CustomerContact customerContact) {
        customerContact.setModifier(userInfo.getUserId());
        customerContact.setShopId(userInfo.getShopId());
        customerContact.setContact(customerCar.getContact());
        customerContact.setContactMobile(customerCar.getContactMobile());
        customerContact.setCustomerId(customerCar.getCustomerId());
        customerContact.setCustomerCarId(customerCar.getId());
        customerContact.setVer(customerCar.getVer());
        customerContact.setRefer(customerCar.getRefer());
        return customerContact;
    }

    private CustomerCar _addCustomerCar(CustomerCar cc, UserInfo userInfo) {
        if (cc == null) {
            throw new BizException("客户信息不能为空");
        }
        if (userInfo == null) {
            throw new BizException("用户登录信息不能为空.");
        }

        Long shopId = userInfo.getShopId();
        /**
         * 1.客户来源
         */
        this.insertCustomerSourceInfo(cc, userInfo);
        /**
         * 2.客户信息持久化 新建帐户
         */
        if (StringUtils.isBlank(cc.getCustomerName()) && !StringUtils.isBlank(cc.getContact())) {
            cc.setCustomerName(cc.getContact());
        }
        if (StringUtils.isBlank(cc.getMobile()) && !StringUtils.isBlank(cc.getContactMobile())) {
            cc.setMobile(cc.getContactMobile());
        }
        Customer customer = generateCustomerAndAccount(cc, userInfo);
        /**
         * 3.车辆信息持久化
         */
        cc = this.setCarField(cc, userInfo);
        cc.setCreator(userInfo.getUserId());
        cc.setCustomerId(customer.getId());

        cc.setGmtCreate(DateUtil.currentDate());
        cc.setPrecheckCount(Constants.DB_DEFAULT_INT);
        cc.setMaintainCount(Constants.DB_DEFAULT_INT);
        cc.setRepairCount(Constants.DB_DEFAULT_INT);
        log.info("[新增客户车辆]customerCar:{}",cc);
        customerCarDao.insert(cc) ;

        //新增客户归属
        allotCustomerCar(cc, userInfo, customer);
        Map param = Maps.newConcurrentMap();
        processCarFile(cc.getCarPictureUrl(), param, shopId, cc);
        this.addOrUpdateCustomerContact(cc, userInfo);

        return this.customerCarDao.selectById(cc.getId());
    }

    /**
     * 删除车辆接口逻辑
     * 1.删除车辆
     * 2.删除车辆客户关联关系表的相关信息
     *
     * @param shopId
     * @param userId
     * @param carId
     */
    @Override
    public void deleteCar(Long shopId, Long userId, Long carId) throws Exception {
        AccountAndCarBO bo = new AccountAndCarBO();
        bo.setCustomerCarId(carId);
        bo.setShopId(shopId);
        customerCarRelService.delete(bo);
        customerCarDao.deleteInfo(shopId,userId,carId);
    }

    @Override
    public List<CustomerCar> select(Map<String, Object> map) {
        if (CollectionUtils.isEmpty(((List<String>) map.get("sorts")))) {
            List<String> list = new ArrayList<>();
            list.add("gmt_modified desc");
            map.put("sorts", list);
        }
        return customerCarDao.select(map);
    }

    @Override
    public Integer update(CustomerCar car) {

        /**
         * 设置车辆级别冗余字段
         */
        if (car != null) {
            car.setCarLevel(getCarLevel(car.getCarModelId()));
        }
        return customerCarDao.updateById(car);
    }

    @Override
    public Integer updateByLicenseAndShopId(CustomerCar customerCar) {
        if (customerCar != null) {
            customerCar.setCarLevel(getCarLevel(customerCar.getCarModelId()));
        }
        return customerCarDao.updateByLicenseAndShopId(customerCar);
    }

    @Override
    public CustomerCar selectByLicenseAndShopId(String license, Long shopId) {
        if (license != null && shopId != null) {
            return customerCarDao.selectByLicenseAndShopId(license, shopId);
        } else {
            return null;
        }
    }

    @Override
    public Integer selectCount(Map<String, Object> params) {
        return customerCarDao.selectCount(params);
    }

    @Override
    public Integer initCustomerCar(List<CustomerCar> customerCarItem) {
        Integer totalSize = 0;
        if (!CollectionUtils.isEmpty(customerCarItem)) {
            logger.info("【初始化车辆】：批量添加{}条车辆信息customer_car", customerCarItem.size());
            totalSize = super.batchInsert(customerCarDao, customerCarItem, 300);
        }
        return totalSize;
    }


    @Override
    public List<CustomerCar> selectByCustomerId(Map<String, Object> paramMap) {
        return this.customerCarDao.selectByCustomerId(paramMap);
    }

    @Override
    public Optional<CustomerCar> getCustomerCar(Long id) {
        return Optional.fromNullable(customerCarDao.selectById(id));
    }

    @Override
    public CustomerCar selectById(Long id) {
        if (id == null) {
            return null;
        }
        return customerCarDao.selectById(id);
    }

    /**
     * create by jason 2015-09-11 根据车牌和mobile查询客户车牌ID
     */
    @Override
    public List<Long> selectByLicensesAndMobile(List<String> licenseList, String mobile) {
        try {
            Map map = new HashMap(1);
            map.put("licenseList", licenseList);
            //根据车牌获得所有车辆信息
            List<CustomerCar> customerCarList = customerCarDao.select(map);
            List<Long> carIdList = new ArrayList();
            List<Long> customerIdList = new ArrayList();
            Map<Long, Long> idMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(customerCarList)) {
                for (CustomerCar customerCar : customerCarList) {
                    Long customerCarId = customerCar.getId();//客户车辆ID
                    Long customerId = customerCar.getCustomerId();//客户ID
                    customerIdList.add(customerCar.getCustomerId());
                    idMap.put(customerId, customerCarId);//客户ID和客户车辆ID key-value对应关系
                }
            }
            if (!CollectionUtils.isEmpty(customerIdList)) {
                //根据customerIds和mobile 获得客户信息
                Map searchMap = new HashMap();
                searchMap.put("ids", customerIdList);
                searchMap.put("mobile", mobile);
                List<Customer> customerList = customerDao.selectByIdsAndMobile(searchMap);

                if (!CollectionUtils.isEmpty(customerList)) {
                    for (Customer customer : customerList) {
                        //客户车辆ID
                        carIdList.add(idMap.get(customer.getId()));
                    }
                }
                return carIdList;

            }
        } catch (Exception e) {
            logger.error("获得客户车辆信息异常!{}", e);
        }
        return null;
    }

    @Override
    public List<CustomerCarBo> selectCustomerHistoryList(Map<String, Object> params) {
        return customerCarDao.selectCustomerHistoryList(params);
    }

    @Override
    public Page<CustomerCarBo> getPage(Pageable pageable, Map<String, Object> searchParams) {
        if (pageable.getSort() != null) {
            Iterator<Sort.Order> iterator = pageable.getSort().iterator();
            ArrayList<String> sorts = new ArrayList<String>();
            while (iterator.hasNext()) {
                Sort.Order order = iterator.next();
                sorts.add(order.getProperty() + " " + order.getDirection().name());
            }
            searchParams.put("sorts", sorts);
        }
        Integer totalSize = customerCarDao.selectCount(searchParams);
        PageRequest pageRequest = new PageRequest((pageable.getPageNumber() < 1 ? 1 : pageable.getPageNumber()) - 1, pageable.getPageSize() < 1 ? 1 : pageable.getPageSize(), pageable.getSort());

        searchParams.put("limit", pageRequest.getPageSize());
        searchParams.put("offset", pageRequest.getOffset());

        List<CustomerCarBo> data = customerCarDao.selectCustomerHistoryList(searchParams);
        DefaultPage<CustomerCarBo> page = new DefaultPage<CustomerCarBo>(data, pageRequest, totalSize);
        return page;
    }

    @Override
    public Result flushCarLevelToDB() {
        Map<Long, String> carLevelMap = new HashMap<>();
        List<CustomerCar> carList = this.customerCarDao.getCarLevelExistsCars();
        String guidePrice;

        for (CustomerCar customerCar : carList) {
            guidePrice = carLevelMap.get(customerCar.getCarModelId());
            if (guidePrice == null) {
                guidePrice = getCarLevel(customerCar.getCarModelId());
                carLevelMap.put(customerCar.getCarModelId(), guidePrice);
            }
            customerCar.setCarLevel(guidePrice);
        }

        Map<String, Object> paramMap = new HashMap<>();
        for (CustomerCar customerCar : carList) {
            paramMap.put("id", customerCar.getId());
            paramMap.put("carLevel", customerCar.getCarLevel());
            this.customerCarDao.updateCarLevelById(paramMap);
        }
        return Result.wrapSuccessfulResult("初始化车辆级别成功.");
    }

    @Override
    public CustomerCar selectIfNoExistInsert(UserInfo userInfo, String carLicense, String ver, String refer) throws BizException {

        if (userInfo == null) {
            throw new BizException("用户登录信息不能为空.");
        }
        if (StringUtils.isEmpty(carLicense)) {
            throw new BizException("非法车牌:" + carLicense);
        }

        /**
         * 车牌号全部为大写
         */
        carLicense = carLicense.toUpperCase();

        CustomerCar customerCar = null;
        customerCar = selectByLicenseAndShopId(carLicense, userInfo.getShopId());
        if (customerCar == null) {
            //TODO 这里需要插入新的车辆信息
            customerCar = new CustomerCar();
            customerCar.setLicense(carLicense);
            customerCar.setCustomerName("");
            customerCar.setMobile("");
            customerCar.setVer(ver);
            customerCar.setRefer(refer);
            Result result = add(customerCar, userInfo);
            if (result != null && result.isSuccess() && result.getData() != null) {
                customerCar = (CustomerCar) result.getData();
            } else {
                logger.error("添加新客户信息失败,{}", LogUtils.funToString(carLicense, result));
            }
        }

        // get customer
        Optional<Customer> customerOptional = customerService.getCustomer(customerCar.getCustomerId());
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            String contact = customer.getContact();
            String contactMobile = customer.getContactMobile();

            String customerName = customer.getCustomerName();
            String mobile = customer.getMobile();
            if (StringUtil.isStringEmpty(contact)) {
                customerCar.setContact(customerName);
            } else {
                customerCar.setContact(contact);
            }

            if (StringUtil.isStringEmpty(contactMobile)) {
                customerCar.setContactMobile(mobile);
            } else {
                customerCar.setContactMobile(contactMobile);
            }

            customerCar.setCustomerName(customer.getCustomerName());
            customerCar.setMobile(customer.getMobile());
            // 单位
            customerCar.setCompany(customer.getCompany());
        } else {
            customerCar.setContact("");
            customerCar.setContactMobile("");
            customerCar.setCustomerName("");
            customerCar.setMobile("");
        }


        return customerCar;
    }

    @Override
    public CustomerCar addOrUpdate(UserInfo userInfo, CustomerCar customerCar) throws BizException {
        if (userInfo == null) {
            throw new BizException("用户登录信息不能为空.");
        }
        if (customerCar == null) {
            throw new BizException("车辆信息不能为空.");
        }
        if (StringUtils.isEmpty(customerCar.getLicense())) {
            throw new BizException("传入车牌不能为空.");
        }

        //如果存在车辆id,则以id为准,否则以车牌为准
        if (customerCar.getId() != null) {
            CustomerCar car = selectById(customerCar.getId());
            customerCar.setCustomerId(car.getCustomerId());
        } else {
            CustomerCar car = selectByLicenseAndShopId(customerCar.getLicense(), userInfo.getShopId());
            if (car != null) {
                customerCar.setId(car.getId());
                customerCar.setCustomerId(car.getCustomerId());
            }
        }


        /**
         * 如果存在车辆id,则更新车辆信息,否则插入车辆信息
         */
        if (customerCar.getId() != null) {
            customerCar = _updateCustomerCar(customerCar, userInfo);
        } else {
            if (customerCar.getLicense() != null) {
                this._addCustomerCar(customerCar, userInfo);
            } else {
                throw new BizException("车辆信息不完整,缺少车牌信息.");
            }
        }
        return customerCar;
    }

    @Override
    public Result addOrUpdate(AppointAppVo appoint) throws BizException {
        Long shopId = appoint.getShopId();//店铺ID
        Long channel = appoint.getChannel();
        String license = appoint.getLicense();//车牌
        String customerName = appoint.getCustomerName();//联系人
        String mobile = appoint.getMobile();
        CustomerCar car = selectByLicenseAndShopId(license, shopId);
        //vin码判重校验
        String vin = appoint.getVin();
        if (org.apache.commons.lang3.StringUtils.isNotBlank(vin)) {
            vin = vin.toUpperCase();// vin码大写
            appoint.setVin(vin);
            // 判断vin码是否存在
            Long carId = null;
            if (car != null) {
                carId = car.getId();
            }
            Boolean bool = customerCarFacade.checkVinIsExist(shopId, vin, carId);
            if (bool) {
                return Result.wrapErrorResult("", "vin码已存在，请勿重复添加！");
            }
        }
        if (null == car) {
            Customer customer = new Customer();
            customer.setCustomerName(customerName);
            customer.setMobile(mobile);
            customer.setShopId(shopId);
            customer.setContact(customerName);
            customer.setContactMobile(mobile);
            //手机号不为空
            if (StringUtils.isNotBlank(mobile)) {
                //判断手机号是否已经有客户
                List<Customer> customerList = customerService.getCustomerByMobile(mobile, shopId);
                if (CollectionUtils.isEmpty(customerList)) {
                    //新的手机号 新建客户和帐户
                    customerDao.insert(customer);
                } else {
                    customer = customerList.get(0);
                    if (StringUtils.isNotEmpty(customer.getCustomerName())) {
                        appoint.setCustomerName(customer.getCustomerName());//客户姓名回写
                    }
                }
            } else {
                customerDao.insert(customer);
            }
            Result result = accountInfoService.generateAccountInfo(customer);
            if (result.isSuccess()) {
                log.info("通过shopId={},customerId={},添加账户信息成功", shopId, customer.getId());
            }
            CustomerCar customerCarNew = new CustomerCar();
            customerCarNew.setLicense(license);
            customerCarNew.setShopId(shopId);
            customerCarNew.setCarBrandId(appoint.getCarBrandId());
            customerCarNew.setCarBrand(appoint.getCarBrandName());
            customerCarNew.setCarSeriesId(appoint.getCarSeriesId());
            customerCarNew.setCarSeries(appoint.getCarSeriesName());
            customerCarNew.setCustomerId(customer.getId());
            customerCarNew.setVin(appoint.getVin());
            customerCarNew.setImportInfo(appoint.getImportInfo());//进出口信息
            customerCarNew.setByName(appoint.getByName());//车辆别名
            log.info("[新增客户车辆]customerCar:{}",customerCarNew);
            customerCarDao.insert(customerCarNew);



            //联系人不为空 insert到联系人历史表中
            if (!StringUtils.isBlank(customerName)) {
                insertCustomerContact(appoint, customer, customerCarNew);
            }
            return Result.wrapSuccessfulResult(customerCarNew);


        } else {
            //customerCar不为空 更新customer的联系人信息
            //客户联系人电话信息是否发生变更
            Customer customer = customerDao.selectById(car.getCustomerId());
            if (null != customer) {
                if (StringUtils.isNotEmpty(customer.getCustomerName())) {
                    appoint.setCustomerName(customer.getCustomerName());//客户姓名回写
                }
                String contactMobile = customer.getContactMobile();
                if (!StringUtils.equals(mobile, contactMobile)) {
                    //联系人电话变更
                    customer.setGmtModified(new Date());
                    if (!"微信端客户".equals(customerName)) {//微信端客户是微信端创建没有客户姓名时默认值
                        customer.setContact(customerName);
                    }
                    customer.setContactMobile(mobile);
                    customerDao.updateById(customer);
                    logger.info("车主app端更新customer成功! customer的customerName:{},mobile:{},id:{}", customer.getCustomerName(), customer.getMobile(), customer.getId());
                    insertCustomerContact(appoint, customer, car);
                }
                //商家过来的预约单会更新车辆信息
                if (channel == 1l) {
                    car.setCarBrandId(appoint.getCarBrandId());
                    car.setCarBrand(appoint.getCarBrandName());
                    car.setCarSeriesId(appoint.getCarSeriesId());
                    car.setCarSeries(appoint.getCarSeriesName());
                    car.setByName(appoint.getByName());//车辆别名
                    car.setVin(appoint.getVin());//车架号
                    car.setImportInfo(appoint.getImportInfo());//进出口信息
                    car.setByName(appoint.getByName());//车辆别名
                    customerCarDao.updateById(car);

                }
                return Result.wrapSuccessfulResult(car);
            } else {
                return Result.wrapErrorResult("", "根据customerCar中的customerId找不到customer信息");
            }

        }
    }

    /**
     * create by jason 2015-09-08
     * create 客户联系人信息
     */
    private void insertCustomerContact(AppointAppVo appoint, Customer customer, CustomerCar customerCar) {
        try {
            Long shopId = appoint.getShopId();//店铺ID
            String customerName = appoint.getCustomerName();//联系人
            String mobile = appoint.getMobile();
            CustomerContact customerContact = new CustomerContact();
            customerContact.setShopId(shopId);
            customerContact.setContact(customerName);
            customerContact.setContactMobile(mobile);
            customerContact.setCustomerId(customer.getId());
            customerContact.setCustomerCarId(customerCar.getId());
            customerContactDao.insert(customerContact);
            logger.info("车主app端新增customerContact成功! customerContact的ID:{},mobile:{},customerName:{}", customerContact.getId(), customerContact.getContactMobile(), customerContact.getContact());
        } catch (Exception e) {
            logger.error("车主app端新增customerContact异常!" + e);
        }
    }

    @Override
    public Result<CustomerPerfectOfCarWashEntity> selectCustomerCar(Long shopId, Long carId) {
        try {
            if (shopId != null && carId != null) {
                Map param = Maps.newConcurrentMap();
                param.put("shopId", shopId);
                param.put("id", carId);
                List<CustomerCar> customerCarList = customerCarDao.select(param);
                if (!CollectionUtils.isEmpty(customerCarList)) {
                    CustomerCar customerCar = customerCarList.get(0);
                    CustomerPerfectOfCarWashEntity entity = new CustomerPerfectOfCarWashEntity();
                    entity.setLicense(customerCar.getLicense());
                    entity.setCarModel(customerCar.getCarModel());
                    entity.setCarBrand(customerCar.getCarBrand());
                    entity.setCarModelId(customerCar.getCarModelId());
                    entity.setCarBrandId(customerCar.getCarBrandId());
                    entity.setCarSeries(customerCar.getCarSeries());
                    entity.setCarSeriesId(customerCar.getCarSeriesId());
                    entity.setCarYearId(customerCar.getCarYearId());
                    entity.setCarYear(customerCar.getCarYear());
                    entity.setCarPowerId(customerCar.getCarPowerId());
                    entity.setCarPower(customerCar.getCarPower());
                    entity.setCarGearBoxId(customerCar.getCarGearBoxId());
                    entity.setCarGearBox(customerCar.getCarGearBox());
                    entity.setCustomerCarId(customerCar.getId());
                    Customer customer = customerDao.selectById(customerCar.getCustomerId());
                    if (customer != null) {
                        entity.setContactMobile(customer.getContactMobile());
                        entity.setContactName(customer.getContact());
                        // 联系人单位
                        entity.setCompany(customer.getCompany());
                    }

                    entity.setImportInfo(customerCar.getImportInfo());
                    entity.setMileage(customerCar.getMileage());
                    entity.setVin(customerCar.getVin());
                    entity.setShopId(shopId);
                    param.remove("id");
                    param.put("relId", customerCar.getId());
                    param.put("relType", 1);
                    param.put("fileType", 5);
                    List<CustomerCarFile> customerCarFiles = customerCarFileService.select(param);
                    if (!CollectionUtils.isEmpty(customerCarFiles)) {
                        CustomerCarFile customerCarFile = customerCarFiles.get(0);
                        entity.setCarLicensePictureUrl(customerCarFile.getFilePath());
                    }
                    return Result.wrapSuccessfulResult(entity);
                } else {
                    logger.error("根据门店Id={},车辆Id={}获取客户信息为空", shopId, carId);
                }
            } else {
                logger.error("门店Id={}或者车辆Id={}为空", shopId, carId);
            }
        } catch (Exception e) {
            logger.error("根据门店Id={}和车辆Id={}获取用户信息异常 :{}", shopId, carId, e);
        }
        return Result.wrapErrorResult(LegendError.COMMON_ERROR);
    }


    private void processCarFile(String carPictureUrl, Map param, Long shopId, CustomerCar customerCar) {
        String suffix = "";
        if (carPictureUrl != null) {
            int idx = carPictureUrl.lastIndexOf(".");
            if (idx != -1 && idx + 1 != carPictureUrl.length()) {
                suffix = carPictureUrl.substring(idx + 1);
            }
        }
        processCarFile(carPictureUrl, suffix, param, shopId, customerCar);
    }

    private void processCarFile(String carPictureUrl, String fileSuffix, Map param, Long shopId, CustomerCar customerCar) {
        if (!StringUtils.isBlank(carPictureUrl)) {
            param.remove("id");
            param.put("relId", customerCar.getId());
            param.put("relType", 1);
            param.put("fileType", 5);
            param.put("filePath", carPictureUrl);
            List<CustomerCarFile> customerCarFiles = customerCarFileService.select(param);
            if (CollectionUtils.isEmpty(customerCarFiles)) {
                CustomerCarFile customerCarFile = new CustomerCarFile();
                customerCarFile.setShopId(shopId);
                customerCarFile.setRelId(customerCar.getId());
                customerCarFile.setRelType(1L);
                customerCarFile.setFileType(5);
                customerCarFile.setFilePath(carPictureUrl);
                customerCarFile.setCreator(customerCar.getModifier());
                customerCarFile.setModifier(customerCar.getModifier());
                if (StringUtils.isNotBlank(fileSuffix) && fileSuffix.length() <= 20) {
                    customerCarFile.setFileSuffix(fileSuffix);
                }
                customerCarFileService.add(customerCarFile);
            }
        }
    }


    @Override
    public Result flushCarTypeToDB() {
        Map<Long, String> carTypeMap = new HashMap<>();
        List<CustomerCar> carList = this.customerCarDao.getCarLevelExistsCars();
        String carType;

        for (CustomerCar customerCar : carList) {
            carType = carTypeMap.get(customerCar.getCarModelId());
            if (carType == null) {
                carType = getCarType(customerCar.getCarModelId());
                carTypeMap.put(customerCar.getId(), carType);
            }
            customerCar.setCarType(carType);
        }

        Map<String, Object> paramMap = new HashMap<>();
        for (CustomerCar customerCar : carList) {
            paramMap.put("id", customerCar.getId());
            paramMap.put("carType", customerCar.getCarType());
            this.customerCarDao.updateCarTypeById(paramMap);
        }
        return Result.wrapSuccessfulResult("初始化车辆类型成功.");
    }


    /**
     * 从athena获取车辆级别信息
     */
    private String getCarLevel(Long carModelId) {
        String guidePrice = "未知";
        if (carModelId == null || carModelId == 0) {
            return guidePrice;
        }
        try {
            com.tqmall.core.common.entity.Result<CarCategoryDTO> result = carCategoryService.getCarCategoryByPrimaryId(carModelId.intValue());
            if (result == null || !result.isSuccess()) {
                log.error("从athena获取车辆级别失败.{}", "carModelId:" + carModelId + "," +
                        (result == null ? "null" : "code:" + result.getCode() + ", error message:" + result.getMessage()));
                return guidePrice;
            }
            if (StringUtils.isBlank(result.getData().getGuidePrice())) {
                return guidePrice;
            }
            return result.getData().getGuidePrice();
        }catch (Exception e){
            logger.error("从athena获取车辆级别信息异常，异常信息",e);
            return guidePrice;
        }
    }

    /**
     * 从athena获取车辆类型信息
     */
    private String getCarType(Long carModelId) {
        String carType = null;
        if (carModelId == null || carModelId == 0) {
        } else {
            com.tqmall.core.common.entity.Result<CarCategoryDTO> result = this.carCategoryService.getCarCategoryByPrimaryId(carModelId.intValue());
            if (result == null || !result.isSuccess()) {
                log.error("从athena获取车辆类型失败.{}", "carModelId:" + carModelId + "," +
                        (result == null ? "null" : "code:" + result.getCode() + ", error message:" + result.getMessage()));
            } else {
                if (StringUtils.isNotBlank(result.getData().getCarType())) {
                    carType = result.getData().getCarType();
                }
            }
        }
        return carType == null ? "未知" : carType;
    }


    @Override
    public void copyCustomerProperty(UserInfo userInfo, OrderInfo order, String carLicense) {
        CustomerCar customerCar = this.selectIfNoExistInsert(userInfo, carLicense, "", "");
        order.setCarLicense(carLicense);
        Long customerCarId = customerCar.getId();
        order.setCustomerCarId(customerCarId);
        order.setCustomerId(customerCar.getCustomerId());
        order.setCarBrand(customerCar.getCarBrand());
        order.setCarBrandId(customerCar.getCarBrandId());
        order.setCarSeries(customerCar.getCarSeries());
        order.setCarSeriesId(customerCar.getCarSeriesId());
        order.setCarModelsId(customerCar.getCarModelId());
        order.setCarModels(customerCar.getCarModel());
        order.setCarCompany(customerCar.getCarCompany());
        order.setImportInfo(customerCar.getImportInfo());
        order.setCarAlias(customerCar.getByName());
        order.setContactName(customerCar.getContact());
        order.setContactMobile(customerCar.getContactMobile());
        order.setCustomerMobile(customerCar.getMobile());
        order.setCustomerName(customerCar.getCustomerName());
        order.setImportInfo(customerCar.getImportInfo());
        order.setCarCompany(customerCar.getCarCompany());
        order.setCarColor(customerCar.getColor());
        // 客户单位
        String company = customerCar.getCompany();
        order.setCompany((company == null) ? " " : company);
        // 行驶里程
        order.setMileage(customerCar.getMileage() + "");
    }


    @Override
    public void copyCustomerProperty(UserInfo userInfo, InsuranceBill bill, String carLicense) {
        CustomerCar customerCar = this.selectIfNoExistInsert(userInfo, carLicense, "", "");
        bill.setCarLicense(carLicense);
        Long customerCarId = customerCar.getId();
        bill.setCustomerCarId(customerCarId);
        bill.setCustomerId(customerCar.getCustomerId());
        bill.setCarBrand(customerCar.getCarBrand());
        bill.setCarBrandId(customerCar.getCarBrandId());
        bill.setCarSeries(customerCar.getCarSeries());
        bill.setCarSeriesId(customerCar.getCarSeriesId());
        bill.setCarModelsId(customerCar.getCarModelId());
        bill.setCarModels(customerCar.getCarModel());
        bill.setImportInfo(customerCar.getImportInfo());
        bill.setCustomerMobile(customerCar.getMobile());
        bill.setCustomerName(customerCar.getCustomerName());
        bill.setImportInfo(customerCar.getImportInfo());
        // 客户单位
        String company = customerCar.getCompany();
        bill.setCompany((company == null) ? " " : company);
        // 行驶里程
        bill.setMileage(customerCar.getMileage() + "");
        bill.setVin(customerCar.getVin());
    }

    // TODO extract interface 抽取增加会员预约次数+1 接口
    @Transactional
    @Override
    public int appointNumberAddOne(String carLicense, Long shopId) {
        // 获取车辆
        CustomerCar customerCar = this.selectByLicenseAndShopId(carLicense, shopId);
        customerCar.setLatestAppoint(new Date());
        customerCar.setAppointCout(customerCar.getAppointCout() + 1);
        customerCar.setShopId(shopId);
        customerCar.setLicense(carLicense);

        return this.updateByLicenseAndShopId(customerCar);
    }

    /**
     * 根据年款、行驶里程获取推荐服务
     *
     * @param carId
     * @return
     */
    @Override
    public Result getRecommendService(Long carId) {
        CustomerCar customerCar = customerCarDao.selectById(carId);
        if (customerCar == null) {
            return Result.wrapErrorResult(LegendErrorCode.CAR_NULL_EX.getCode(), LegendErrorCode.CAR_NULL_EX.getErrorMessage());
        }
        CarMaintainPO carMaintainPO = new CarMaintainPO();
        Long carYearId = customerCar.getCarYearId();
        if (carYearId != null && carYearId > 0) {
            carMaintainPO.setYearId(carYearId.intValue());
        } else {
            return Result.wrapErrorResult(LegendErrorCode.CAR_SERVICE_EMPTY_EX.getCode(), LegendErrorCode.CAR_SERVICE_EMPTY_EX.getErrorMessage());
        }
        Long mileage = customerCar.getMileage();
        if (mileage != null && carYearId > 0) {
            carMaintainPO.setMileage(mileage.intValue());
        } else {
            return Result.wrapErrorResult(LegendErrorCode.CAR_SERVICE_EMPTY_EX.getCode(), LegendErrorCode.CAR_SERVICE_EMPTY_EX.getErrorMessage());
        }
        log.info("【dubbo:】调用数据团队接口，获取推荐保养服务，参数：{}", carMaintainPO);
        com.tqmall.core.common.entity.Result<AdviseMaintainDTO> rpcResult = maintainService.getAdviseMaintain(carMaintainPO);
        if (rpcResult.isSuccess()) {
            return Result.wrapSuccessfulResult(rpcResult.getData());
        }
        return Result.wrapErrorResult(rpcResult.getCode(), rpcResult.getMessage());
    }

    @Override
    public Integer getCustomerHasMobileNum(Long shopId) {
        return customerCarDao.getCustomerHasMobileNum(shopId);
    }

    /**
     * 获取门店全部的记录数,包括isDeleted = Y
     *
     * @param shopId
     * @return
     */
    @Override
    public Integer countByShopId(Long shopId) {
        return customerCarDao.countByShopId(shopId);
    }

    @Override
    public List<CustomerCar> listByCustomerId(Long shopId, Long customerId) {
        Assert.notNull(shopId);
        Assert.notNull(customerId);

        Map param = Maps.newHashMap();
        param.put("shopId", shopId);
        param.put("customerId", customerId);
        return customerCarDao.select(param);
    }

    @Override
    public Page<CustomerCar> getCustomerPageByParam(Pageable pageable, Map<String, Object> param) {

        Integer totalSize = customerCarDao.selectCount(param);

        PageRequest pageRequest = new PageRequest((pageable.getPageNumber() < 1 ? 1 : pageable.getPageNumber()) - 1, pageable.getPageSize() < 1 ? 1 : pageable.getPageSize(), pageable.getSort());
        param.put("limit", pageRequest.getPageSize());
        param.put("offset", pageRequest.getOffset());
        if (pageable.getSort() != null) {
            Iterator<Sort.Order> iterator = pageable.getSort().iterator();
            ArrayList<String> sorts = new ArrayList<String>();
            while (iterator.hasNext()) {
                Sort.Order order = iterator.next();
                sorts.add(order.getProperty() + " " + order.getDirection().name());
            }
            param.put("sorts", sorts);
        }
        List<CustomerCar> data = customerCarDao.select(param);
        List<Long> customerId = Lists.transform(data, new Function<CustomerCar, Long>() {
            @Override
            public Long apply(CustomerCar customerCar) {
                return customerCar.getCustomerId();
            }
        });
        Map carParam = Maps.newHashMap();
        carParam.put("shopId", param.get("shopId"));
        carParam.put("customerIdList", customerId);
        if (!CollectionUtils.isEmpty(customerId)) {
            List<Customer> customers = customerDao.select(carParam);
            Map<Long, Customer> customerMap = Maps.newHashMap();
            //            Multimap<Long,CustomerCar> customerCarMultimap = ArrayListMultimap.create();
            if (!CollectionUtils.isEmpty(customers)) {
                for (Customer customer : customers) {
                    customerMap.put(customer.getId(), customer);
                }
                for (CustomerCar customerCar : data) {
                    Customer customer = customerMap.get(customerCar.getCustomerId());
                    if (null != customer) {
                        customerCar.setCustomerName(customer.getCustomerName());
                        customerCar.setMobile(customer.getMobile());
                    }
                    if (null == customerCar.getCarModel()) {
                        customerCar.setCarModel("");
                    }
                    if (null == customerCar.getCarBrand()) {
                        customerCar.setCarBrand("");
                    }
                    if (null == customerCar.getCarSeries()) {
                        customerCar.setCarSeries("");
                    }

                }
            }
        }

        DefaultPage<CustomerCar> page = new DefaultPage<CustomerCar>(data, pageRequest, totalSize);
        return page;
    }

    @Override
    public List<CustomerCar> findCustomerCarsByLicense(Long shopId, String... license) {
        return customerCarDao.findCustomerCarsByLicense(shopId, license);
    }

    @Override
    public void batchSave(List<CustomerCar> customerCars) {
        super.batchInsert(customerCarDao,customerCars,1000);
    }

    @Override
    public Map<Long, String> mapCustomerId2Licenses(Long shopId, List<Long> customerIds) {
        Assert.notNull(shopId);
        HashMap<Long, String> map = Maps.newHashMap();
        if (CollectionUtils.isEmpty(customerIds)) {
            return map;
        }
        List<CustomerCar> cars = customerCarDao.listByCustomerIds(shopId, customerIds);
        for (CustomerCar car : cars) {
            Long customerId = car.getCustomerId();
            if (map.containsKey(customerId)) {
                String s = map.get(customerId);
                s += "," + car.getLicense();
                map.put(customerId, s);
            }
            map.put(customerId, car.getLicense());
        }
        return map;
    }

    @Override
    public void save(CustomerCar customerCar) {
        customerCarDao.insert(customerCar);
    }

    @Override
    public List<CustomerCar> selectByIds(List<Long> carIds) {
        if (CollectionUtils.isEmpty(carIds)) {
            return Lists.newArrayList();
        }
        return customerCarDao.selectByIds(carIds.toArray(new Long[carIds.size()]));
    }


    @Override
    public List<CustomerCar> findCustomerCarByIds(Long shopId, List<Long> carIds) {
        return customerCarDao.selectByIdss(shopId,carIds);
    }

    @Override
    public boolean hasExistCustomer(Long shopId, String carLicense) {
        CustomerCar customerCar = this.selectByLicenseAndShopId(carLicense, shopId);
        if (customerCar == null){
            return false;
        }
        Customer customer = customerService.selectById(customerCar.getCustomerId(), shopId);
        if (customer == null ||
                (StringUtils.isBlank(customer.getCustomerName())&& StringUtils.isBlank(customer.getMobile()))){
            return false;
        }
        return true;
    }


}
