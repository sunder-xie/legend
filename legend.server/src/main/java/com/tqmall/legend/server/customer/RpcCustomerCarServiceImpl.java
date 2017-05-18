package com.tqmall.legend.server.customer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.Constants;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.BizTemplate;
import com.tqmall.common.util.BdUtil;
import com.tqmall.common.util.DateUtil;
import com.tqmall.common.util.HttpUtil;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.common.entity.Result;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.legend.api.entity.ApiCarVo;
import com.tqmall.legend.biz.account.AccountCouponService;
import com.tqmall.legend.biz.account.AccountInfoService;
import com.tqmall.legend.biz.account.CouponInfoService;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.customer.AppointService;
import com.tqmall.legend.biz.customer.CustomerCarFileService;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.legend.biz.customer.CustomerService;
import com.tqmall.legend.biz.marketing.ng.NoteConfigureService;
import com.tqmall.legend.biz.order.StatisticsOrderService;
import com.tqmall.legend.biz.precheck.PrechecksService;
import com.tqmall.legend.biz.shop.SerialNumberService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.support.elasticsearch.ELResult;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.common.LegendError;
import com.tqmall.legend.dao.customer.CustomerCarDao;
import com.tqmall.legend.dao.prechecks.PrechecksDao;
import com.tqmall.legend.dao.tqcheck.TqCheckDetailDao;
import com.tqmall.legend.dao.tqcheck.TqCheckLogDao;
import com.tqmall.legend.entity.account.AccountCoupon;
import com.tqmall.legend.entity.account.AccountInfo;
import com.tqmall.legend.entity.account.CouponInfo;
import com.tqmall.legend.entity.account.vo.AccountCouponVo;
import com.tqmall.legend.entity.account.vo.CouponVo;
import com.tqmall.legend.entity.customer.Appoint;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.customer.CustomerCarBo;
import com.tqmall.legend.entity.customer.CustomerCarFile;
import com.tqmall.legend.entity.precheck.PrecheckDetailsVO;
import com.tqmall.legend.entity.precheck.Prechecks;
import com.tqmall.legend.entity.shop.NoteShopConfig;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.entity.tqcheck.TqCheckLog;
import com.tqmall.legend.object.enums.appoint.AppointStatusEnum;
import com.tqmall.legend.enums.coupon.AccountCouponSourceEnum;
import com.tqmall.legend.facade.customer.CustomerCarFacade;
import com.tqmall.legend.facade.customer.vo.CarDetailVo;
import com.tqmall.legend.facade.customer.vo.CustomerCarVo;
import com.tqmall.legend.object.param.customer.CustomerCarParam;
import com.tqmall.legend.object.param.customer.ReceiveCouponParam;
import com.tqmall.legend.object.result.customer.*;
import com.tqmall.legend.service.customer.RpcCustomerCarService;
import com.tqmall.search.common.data.PageableRequest;
import com.tqmall.search.dubbo.client.legend.customercar.param.LegendCustomerCarRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 16/1/5.
 */
@Service ("rpcCustomerCarService")
public class RpcCustomerCarServiceImpl implements RpcCustomerCarService {

    public static final Logger log = LoggerFactory.getLogger(RpcCustomerCarServiceImpl.class);
    @Autowired
    private CustomerCarService customerCarService;

    @Autowired
    private CustomerCarFileService customerCarFileService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private AppointService appointService;

    @Autowired
    private PrechecksService prechecksService;

    @Autowired
    private PrechecksDao prechecksDao;

    @Autowired
    private CustomerCarDao customerCarDao;

    @Autowired
    private NoteConfigureService noteConfigureService;

    @Autowired
    private TqCheckLogDao tqCheckLogDao;

    @Autowired
    private TqCheckDetailDao tqCheckDetailDao;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AccountInfoService accountInfoService;

    @Autowired
    private CouponInfoService couponInfoService;

    @Autowired
    private AccountCouponService accountCouponService;

    @Value("${i.search.url}")
    private String iSearchUrl;

    @Autowired
    private SerialNumberService serialNumberService;

    @Autowired
    private CustomerCarFacade customerCarFacade;
    @Autowired
    private StatisticsOrderService statisticsOrderService;

    /**
     * 创建客户接口
     * TODO 日志打印方式需要规范，目前暂时先用原来的
     *
     * @param param
     *
     * @return
     */
    @Override
    public Result<Long> addCustomerCar(CustomerCarParam param) {
        Result returnResult;
        try {
            log.info("【dubbo】RpcCustomerCarServiceImpl.addCustomerCar：创建客户,传参对象{},来源{}", param, param.getSource());
            //数据校验
            Result result = check(param);
            if (!result.isSuccess()) {
                log.info("【dubbo】RpcCustomerCarServiceImpl.addCustomerCar：数据有误，返回值{}", LogUtils.objectToString(result));
                return result;
            } else {
                //进行客户车辆添加操作
                CustomerCar customerCar = new CustomerCar();
                BeanUtils.copyProperties(param,customerCar);
                UserInfo userInfo = new UserInfo();
                userInfo.setShopId(param.getShopId());
                CustomerCar temp = customerCarService.addOrUpdate(userInfo, customerCar);
                Long id = temp.getId();
                returnResult = Result.wrapSuccessfulResult(id);
            }
            log.info("【dubbo】RpcCustomerCarServiceImpl.addCustomerCar，返回数据{}", LogUtils.objectToString(returnResult));
            return returnResult;
        } catch (BizException e) {
            log.error("【dubbo】RpcCustomerCarServiceImpl.addCustomerCar，出现业务异常{}", e);
        } catch (Exception e) {
            log.error("【dubbo】RpcCustomerCarServiceImpl.addCustomerCar，出现异常{}", e);
        }
        returnResult = Result.wrapErrorResult("", "系统异常，请稍后再试");
        log.info("【dubbo】RpcCustomerCarServiceImpl.addCustomerCar，出现异常,返回{}", returnResult);
        return returnResult;
    }

    /**
     * 创建或更新客户车辆
     * @param param
     * @return
     */
    @Override
    public Result<CustomerCarDTO> addOrUpdateCustomerCar(CustomerCarParam param) {
        log.info("【dubbo】创建更新客户,传参对象{}", JSONUtil.object2Json(param));
        Result returnResult;
        //数据校验
        //判断来源是否为空
        String source = param.getSource();
        if (StringUtils.isBlank(source)) {
            log.info("【dubbo】创建更新客户：数据有误，系统来源为空");
            return Result.wrapErrorResult(LegendErrorCode.DUBBO_CUSTOMER_CAR_NULL_EX.newResult("").getCode(), "系统来源为空");
        }
        String license = param.getLicense();
        //判断车牌是否为空
        if (StringUtils.isBlank(license)) {
            log.info("【dubbo】创建更新客户：数据有误，车牌号为空");
            return Result.wrapErrorResult(LegendErrorCode.DUBBO_CUSTOMER_CAR_NULL_EX.newResult("").getCode(), "车牌号为空");
        }
        //vin码判重
        String vin = param.getVin();
        if(StringUtils.isNotBlank(vin)){
            vin = vin.toUpperCase();// vin码大写
            param.setVin(vin);
            // 判断vin码是否存在
            Long shopId = param.getShopId();
            // 查询车辆
            CustomerCar customerCar = customerCarFacade.getCustomerCarByLicense(license, shopId);
            Long carId = null;
            if(customerCar != null){
                carId = customerCar.getId();
            }
            Boolean bool = customerCarFacade.checkVinIsExist(shopId, vin, carId);
            if(bool){
                return Result.wrapErrorResult(LegendErrorCode.DUBBO_CUSTOMER_CAR_NULL_EX.newResult("").getCode(), "vin码已存在，请勿重复添加！");
            }
        }
        if (StringUtils.isNotBlank(param.getUpkeepMileage()) && param.getMileage() != null) {
            Long mileage = param.getMileage();
            Long upkeepMileage = Long.valueOf(param.getUpkeepMileage());
            if (upkeepMileage.compareTo(mileage) <= 0) {
                return Result.wrapErrorResult(LegendErrorCode.CAR_CHECK_EX.getCode(), "下次保养里程不能小于或等于行驶里程");
            }
        }
        try {
            //进行客户车辆添加/更新操作
            CustomerCar customerCar = new CustomerCar();
            BeanUtils.copyProperties(param,customerCar);
            UserInfo userInfo = new UserInfo();
            userInfo.setShopId(param.getShopId());
            Integer userId = param.getCreator();
            if(userId != null){
                userInfo.setUserId(userId.longValue());
            }
            CustomerCar temp = customerCarService.addOrUpdate(userInfo, customerCar);
            CustomerCarDTO customerCarDTO = new CustomerCarDTO();
            BeanUtils.copyProperties(temp,customerCarDTO);
            returnResult = Result.wrapSuccessfulResult(customerCarDTO);
            log.info("【dubbo】创建更新客户，返回数据{}", LogUtils.objectToString(returnResult));
            return returnResult;
        } catch (BizException e) {
            String msg = e.getMessage();
            log.error("【dubbo】创建更新客户，出现业务异常{}", msg);
            returnResult = Result.wrapErrorResult(LegendErrorCode.SYSTEM_ERROR.getCode(), msg);
            return returnResult;
        } catch (Exception e) {
            log.error("【dubbo】创建更新客户，出现异常", e);
            returnResult = Result.wrapErrorResult(LegendErrorCode.SYSTEM_ERROR.getCode(), "系统异常，请稍后再试");
            return returnResult;
        }
    }

    private Result check(CustomerCarParam param) {
        //判断来源是否为空
        String source = param.getSource();
        if (StringUtils.isBlank(source)) {
            return Result.wrapErrorResult(LegendErrorCode.DUBBO_CUSTOMER_CAR_NULL_EX.newResult("").getCode(), "系统来源为空");
        }
        String license = param.getLicense();
        //判断车牌是否为空
        if (StringUtils.isBlank(license)) {
            return Result.wrapErrorResult(LegendErrorCode.DUBBO_CUSTOMER_CAR_NULL_EX.newResult("").getCode(), "车牌号为空");
        }
        Long shopId = param.getShopId();
        Shop shop = shopService.selectById(shopId);
        if (shop == null) {
            return Result.wrapErrorResult(LegendErrorCode.DUBBO_CUSTOMER_CAR_NULL_EX.newResult("").getCode(), "门店不存在");
        }
        return Result.wrapSuccessfulResult(true);
    }

    /**
     * 客户档案-客户预约单列表
     *
     * @param shopId        店铺id
     * @param customerCarId 车辆id
     * @param page
     * @param size
     *
     * @Since app3.0
     */
    @Override
    public com.tqmall.zenith.errorcode.support.Result<CustomerRecordDTO> getCustomerAppointRecord(Long shopId, Long customerCarId, Integer page, Integer size) {
        log.info("[dubbo][客户档案] 获取客户档案预约信息 getCustomerAppointRecord().传参:shopId={},customerCarId={},page={},size={}", shopId, customerCarId, page, size);
        if (null == shopId) {
            log.error("[dubbo][客户档案] 传入参数错误,shopId=null,searchParams=null");
            return LegendErrorCode.APP_PARAM_ERROR.newResult();
        }
        if (null == customerCarId) {
            log.error("[dubbo][客户档案] 传入参数错误,customerCarId=null");
            return LegendErrorCode.APP_PARAM_ERROR.newResult();
        }
        //参数设置， 提醒设置按店铺来
        Map<String, Object> searchParams = appointParamSet(shopId, customerCarId, page, size);
        //[客户档案]总对象
        CustomerRecordDTO customerRecordDTO = new CustomerRecordDTO();

        Map<String, Object> carParams = new HashMap<>();
        carParams.put("shopId", searchParams.get("shopId"));
        carParams.put("id", searchParams.get("customerCarId"));
        List<CustomerCar> customerCarList = customerCarDao.select(carParams);
        if (CollectionUtils.isEmpty(customerCarList)) {
            log.error("[dubbo][客户档案] 传入参数错误,没有找到该车辆信息,shopId={}, customerCarId={}", shopId, customerCarId);
            List<Appoint> appointList = appointService.select(searchParams);
            if (CollectionUtils.isEmpty(appointList)) {
                log.error("[dubbo][客户档案] 门店预约提醒时间内没有找到客户预约单信息,shopId={}, customerCarId={}", shopId, customerCarId);
                return com.tqmall.zenith.errorcode.support.Result.wrapSuccessfulResult(customerRecordDTO);
            }
            Appoint appoint = appointList.get(0);
            customerRecordDTO.setCustomerCarId(appoint.getCustomerCarId());
            customerRecordDTO.setCarInfo(appoint.getCarBrandName() + " " + appoint.getCarSeriesName());
            customerRecordDTO.setLicense(appoint.getLicense());
        } else {
            CustomerCar customerCar = customerCarList.get(0);
            customerRecordDTO.setCustomerCarId(customerCar.getId());
            customerRecordDTO.setLicense(customerCar.getLicense());
            customerRecordDTO.setCarInfo(customerCar.getCarInfo());
        }
        Integer totalNum = appointService.selectCount(searchParams);
        List<Appoint> appointList = appointService.getAppointRecordApp(searchParams);
        List<CustomerAppointDTO> customerAppointDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(appointList)) {
            for (Appoint appoint : appointList) {
                CustomerAppointDTO customerAppointDTO = new CustomerAppointDTO();
                customerAppointDTO.setAppointId(appoint.getId());
                customerAppointDTO.setAppointTime(appoint.getAppointTime());
                customerAppointDTO.setAppointStatus(appoint.getStatus());
                customerAppointDTO.setAppointStatusName(AppointStatusEnum.getNameByStatus(appoint.getStatus().intValue()));
                customerAppointDTOList.add(customerAppointDTO);
            }
        }
        customerRecordDTO.setCustomerAppointDTOList(customerAppointDTOList);
        customerRecordDTO.setTotalNum(totalNum);
        return com.tqmall.zenith.errorcode.support.Result.wrapSuccessfulResult(customerRecordDTO);
    }

    private HashMap appointParamSet(Long shopId, Long customerCarId, Integer page, Integer size) {
        HashMap<String, Object> searchParams = new HashMap<>();
        searchParams.put("shopId", shopId);
        searchParams.put("customerCarId", customerCarId);
        if (null == page || page < 1) {
            page = 1;
        }
        if (null == size || size < 1) {
            size = 10;
        }
        searchParams.put("offset", (page - 1) * size);
        searchParams.put("limit", size);
        searchParams.put("sorts", new String[] { "appoint_time desc" });
        //-1是作废的，不查询
        searchParams.put("statusList", new Integer[] { 0, 1, 2, 3, 4, 5 });
        //下推状态 0 滴滴过来的预约单要有下推操作 1 另外途径过来
        searchParams.put("pushStatus", 1);

        //获取店铺 预约单配置 信息
        int appointInvalidDate = 0;
        int appointValidDate = 0;
        NoteShopConfig appointShopConfig = noteConfigureService.getConfigure(shopId, Constants.NOTE_APPOINT_CONF_TYPE);
        if (null != appointShopConfig) {
            appointValidDate = appointShopConfig.getFirstValue();
            appointInvalidDate = appointShopConfig.getInvalidValue();
        }
        //门店预约提醒时间
        searchParams.put("appointTimeGt", DateUtil.convertDateToYMDHMS(DateUtil.getDateBy(new Date(), -appointInvalidDate)));
        searchParams.put("appointTimeLt", DateUtil.convertDateToYMDHMS(DateUtil.getDateBy(new Date(), appointValidDate)));
        return searchParams;
    }

    /**
     * 客户档案-客户上次车况(预检单)列表
     *
     * @Since app3.0
     */
    @Override
    public com.tqmall.zenith.errorcode.support.Result<CustomerRecordDTO> getCustomerPrechecksRecord(Long shopId, Long customerCarId, Integer page, Integer size) {
        log.info("[dubbo][客户档案] 客户车辆车况信息.getCustomerPrechecksRecord().传参:shopId={}, customerCarId={},page={},size={}", shopId, customerCarId, page, size);
        if (null == shopId) {
            log.error("[dubbo][客户档案] 传入参数错误,shopId=null");
            return LegendErrorCode.APP_PARAM_ERROR.newResult();
        }
        if (null == customerCarId) {
            log.error("[dubbo][客户档案] 传入参数错误,customerCarId=null");
            return LegendErrorCode.APP_PARAM_ERROR.newResult();
        }
        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("shopId", shopId);
        searchParams.put("customerCarId", customerCarId);
        searchParams.put("sorts", new String[] { "gmt_create desc" });
        if (null == page || page < 1) {
            page = 1;
        }
        if (null == size || size < 1) {
            size = 10;
        }
        searchParams.put("offset", (page - 1) * size);
        searchParams.put("limit", size);
        searchParams.put("sorts", new String[] { "gmt_create desc" });
        //[客户档案]总对象
        CustomerRecordDTO customerRecordDTO = new CustomerRecordDTO();
        Map<String, Object> carParams = new HashMap<>();
        carParams.put("shopId", shopId);
        carParams.put("id", customerCarId);
        List<CustomerCar> customerCarList = customerCarDao.select(carParams);
        List<CustomerPrechecksDTO> customerPrechecksDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(customerCarList)) {
            CustomerCar customerCar = customerCarList.get(0);
            customerRecordDTO.setCustomerCarId(customerCar.getId());
            customerRecordDTO.setLicense(customerCar.getLicense());
            customerRecordDTO.setCarInfo(customerCar.getCarInfo());
            log.info("[dubbo][客户档案]获取该车辆[淘汽检测]记录,shopId={},customerCarId={}", shopId, customerCarId);
            Integer tqCheckNum = tqCheckLogDao.selectCount(searchParams);
            log.info("[dubbo][客户档案]获取该车辆[淘汽检测]记录，[大小:{}]", tqCheckNum);
            //车辆淘汽检测记录获取
            List<TqCheckLog> tqCheckLogList = tqCheckLogDao.select(searchParams);
            if (!CollectionUtils.isEmpty(tqCheckLogList)) {
                for(TqCheckLog tqCheckLog : tqCheckLogList) {
                    Map<String,Object> detailParam = new HashMap<>();
                    detailParam.put("shopId", tqCheckLog.getShopId());
                    detailParam.put("checkLogId", tqCheckLog.getId());
                    Integer checkCount = tqCheckDetailDao.selectCount(detailParam);
                    if(checkCount > 0) {
                        CustomerPrechecksDTO customerPrechecksDTO = new CustomerPrechecksDTO();
                        StringBuilder sb = new StringBuilder("淘汽检测");
                        sb.append(checkCount);
                        sb.append("项");
                        customerPrechecksDTO.setId(tqCheckLog.getId());
                        customerPrechecksDTO.setChecksFlag(1);
                        customerPrechecksDTO.setCarLicense(tqCheckLog.getCarLicense());
                        customerPrechecksDTO.setCarInfo(tqCheckLog.getCarInfo());
                        customerPrechecksDTO.setGmtCreateStr(DateUtil.convertDateToYMDHHmm(tqCheckLog.getGmtCreate()));
                        customerPrechecksDTO.setChecksName(sb.toString());
                        customerPrechecksDTOList.add(customerPrechecksDTO);
                    }
                }
            }
            Integer precheckNum = prechecksDao.selectCount(searchParams);
            Integer totalNum = precheckNum + tqCheckNum;
            //预检单记录获取
            log.info("[dubbo][客户档案] 获取客户预检单列表(含检测详情),shopId={},customerCarId={}", shopId, customerCarId);
            List<PrecheckDetailsVO> retList = prechecksService.toAppGetPrecheckDetailsList(searchParams);
            if (!CollectionUtils.isEmpty(retList)) {
                for (PrecheckDetailsVO precheckDetailsVO : retList) {
                    CustomerPrechecksDTO customerPrechecksDTO = new CustomerPrechecksDTO();
                    customerPrechecksDTO.setId(precheckDetailsVO.getPrecheckId());
                    customerPrechecksDTO.setGmtCreateStr(precheckDetailsVO.getGmtCreate());
                    customerPrechecksDTO.setChecksFlag(0);
                    customerPrechecksDTO.setChecksName("车况登记");
                    customerPrechecksDTOList.add(customerPrechecksDTO);
                }
            }
            //预检单列表对象装载
            customerRecordDTO.setCustomerPrechecksDTOList(customerPrechecksDTOList);
            customerRecordDTO.setTotalNum(totalNum);
        }
        return com.tqmall.zenith.errorcode.support.Result.wrapSuccessfulResult(customerRecordDTO);
    }

    @Override
    public Result<CustomerCarDTO> getCustomerCarByCarLicense(String carLicense, Long shopId) {
        CustomerCar customerCar = customerCarDao.selectByLicenseAndShopId(carLicense, shopId);
        CustomerCarDTO customerCarDTO = null;
        if (null != customerCar) {
            customerCarDTO = new CustomerCarDTO();
            try {
                BeanUtils.copyProperties(customerCar, customerCarDTO);
            } catch (BeansException e){
                log.error("根据车牌号和门店id获取车辆信息，内部属性转换错误,carLicense={}, shopId={}, e={}", carLicense, shopId, e);
                return Result.wrapErrorResult("", "内部错误");
            }
        }

        return Result.wrapSuccessfulResult(customerCarDTO);
    }

    @Override
    public Result<ReceiveCouponDTO> receiveCoupon(ReceiveCouponParam param) {
        log.info("[dubbo][门店微信公众号]客户领取优惠券,入参:{}",LogUtils.objectToString(param));
        //参数校验
        if(param==null){
            log.error("[dubbo][门店微信公众号]客户领取优惠券失败,传入参数为空");
            return Result.wrapErrorResult("-1","入参为空");
        }
        Long userGlobalId = param.getUserGlobalId();
        String mobile = param.getMobile();//车主手机号
        Long couponInfoId = param.getCouponInfoId();//优惠券类型id
        Integer couponSource = param.getCouponSource();//领券来源
        if(!StringUtil.isMobileNO(mobile)){
            log.error("[dubbo][门店微信公众号]客户领取优惠券失败,手机号码:{}格式错误",mobile);
            return Result.wrapErrorResult("-1","手机号码格式错误");
        }
        if(userGlobalId==null||couponInfoId==null||couponSource==null){
            log.error("[dubbo][门店微信公众号]客户领取优惠券失败,有必须参数为空");
            return Result.wrapErrorResult("-1","入参有误");
        }
        Shop shop = shopService.getShopByUserGlobalId(param.getUserGlobalId());
        if(shop==null){
            log.error("[dubbo][门店微信公众号]客户领取优惠券失败,根据userGlobalId:{}查不到门店信息",userGlobalId);
            return Result.wrapErrorResult("-1","操作失败,查询不到门店信息");
        }
        Long shopId = shop.getId();
        List<CouponInfo> couponInfoList = couponInfoService.findByIds(shopId,couponInfoId);
        if (CollectionUtils.isEmpty(couponInfoList)) {
            log.error("[dubbo][门店微信公众号]客户领取优惠券失败,couponInfoId:{}优惠券类型信息",couponInfoId);
            return Result.wrapErrorResult("-1","操作失败,查询不到优惠券设置信息");
        }
        Long customerId = null;
        List<Customer> customerList = customerService.getCustomerByMobile(mobile,shopId);
        if(CollectionUtils.isEmpty(customerList)){
            //创建客户
            Customer customer = new Customer();
            customer.setMobile(mobile);
            customer.setShopId(shopId);
            if(param.getCustomerName()==null){
                customer.setCustomerName("");
            } else{
                customer.setCustomerName(param.getCustomerName());
            }
            log.info("[dubbo][门店微信公众号]客户信息不存在,创建客户信息,入参:{}",LogUtils.objectToString(customer));
            customerService.add(customer);
            customerId = customer.getId();
        } else{
            //查出多条客户信息时取最近创建的1条
            Collections.sort(customerList, new Comparator<Customer>() {
                @Override
                public int compare(Customer o1, Customer o2) {
                    return o2.getGmtCreate().compareTo(o1.getGmtCreate());
                }
            });
            customerId = customerList.get(0).getId();
        }
        AccountInfo accountInfo = accountInfoService.getAccountInfoByCustomerIdAndShopId(shopId, customerId);
        if(accountInfo==null){
            log.info("[dubbo][门店微信公众号]账户信息不存在,创建账户信息,shopId:{},customerId{}",shopId,customerId);
            com.tqmall.legend.common.Result result= accountInfoService.generateAccountInfo(shopId,customerId);
            if(!result.isSuccess()){
                log.error("[dubbo][门店微信公众号]账户信息不存在,创建账户信息失败,shopId:{},customerId{},result:{}",shopId,customerId,result);
                return Result.wrapErrorResult("-1"," 创建账户信息失败");
            }
            accountInfo = accountInfoService.getAccountInfoByCustomerIdAndShopId(shopId,customerId);
        }
        Long accountId = accountInfo.getId();
        AccountCouponVo accountCouponVo = new AccountCouponVo();
        accountCouponVo.setShopId(shopId);
        final CouponVo couponVo = new CouponVo();
        couponVo.setId(couponInfoId);
        couponVo.setNum(1);//领取一张
        List<CouponVo> couponVoList = new ArrayList<CouponVo>() {{
            add(couponVo);
        }};
        accountCouponVo.setCouponVos(couponVoList);
        accountCouponVo.setAccountId(accountId);
        AccountCoupon accountCoupon = accountCouponService.grant(accountCouponVo, AccountCouponSourceEnum.SHOP_WECHAT);
        ReceiveCouponDTO receiveCouponDTO = new ReceiveCouponDTO();
        BeanUtils.copyProperties(accountCoupon,receiveCouponDTO);
        return Result.wrapSuccessfulResult(receiveCouponDTO);
    }

    @Override
    public Result<List<String>> getCarLicenseByMobileAndUserGlobalId(Long userGlobalId, String mobile) {
        if(null == userGlobalId){
            return Result.wrapErrorResult("","门店号不能为空.");
        }
        if(StringUtils.isEmpty(mobile)){
            return Result.wrapErrorResult("","手机号码不能为空.");
        }
        Shop shop = shopService.getShopByUserGlobalId(userGlobalId);
        if(shop==null){
            log.error("[创建来自微信的云修客户]失败,查询不到门店信息,userGlobalId:{}", LogUtils.objectToString(userGlobalId));
            return Result.wrapErrorResult("-1","查询不到门店信息");
        }
        Long shopId = shop.getId();
        List<String> list = customerService.getLicenseByMobile(shopId,mobile);
        return Result.wrapSuccessfulResult(list);
    }

    @Override
    public Result<ApiBaseCarVoDTO> getCustomerCar(Long shopId, String license) {
        log.info("APP调用获取车辆信息,参数：shopId={},license={}", shopId, license);
        Map param = Maps.newConcurrentMap();
        param.put("shopId", shopId);
        param.put("license", license);
        List<CustomerCar> customerCarList = customerCarService.select(param);
        ApiBaseCarVoDTO baseCarVo = new ApiBaseCarVoDTO();
        if (!CollectionUtils.isEmpty(customerCarList)) {
            CustomerCar customerCar = customerCarList.get(0);
            if (customerCar.getCustomerId() != null) {
                Customer customer = customerService.selectById(customerCar.getCustomerId());

                baseCarVo.setCarId(customerCar.getId());
                baseCarVo.setCustomerId(customer.getId());
                if (StringUtils.isNotBlank(customer.getCustomerName())) {
                    baseCarVo.setCustomerName(customer.getCustomerName());
                }
                if (StringUtils.isNotBlank(customer.getMobile())) {
                    baseCarVo.setMobile(customer.getMobile());
                }
                baseCarVo.setCarInfo(customerCar.getCarInfo());
                baseCarVo.setCarModelId(customerCar.getCarModelId());
                baseCarVo.setCarSeriesId(customerCar.getCarSeriesId());
                baseCarVo.setLicense(customerCar.getLicense());
                baseCarVo.setMileage(customerCar.getMileage());
                baseCarVo.setCarBrand(customerCar.getCarBrand());
                baseCarVo.setCarModel(customerCar.getCarModel());
                baseCarVo.setCarSeries(customerCar.getCarSeries());
                baseCarVo.setImportInfo(customerCar.getImportInfo());
                baseCarVo.setCarCompany(customerCar.getCarCompany());
                baseCarVo.setCarBrandId(customerCar.getCarBrandId());
                baseCarVo.setCarYear(customerCar.getCarYear());
                baseCarVo.setCarYearId(customerCar.getCarYearId());
                baseCarVo.setCarGearBoxId(customerCar.getCarGearBoxId());
                baseCarVo.setCarGearBox(customerCar.getCarGearBox());
                baseCarVo.setCarPower(customerCar.getCarPower());
                baseCarVo.setCarPowerId(customerCar.getCarPowerId());
                baseCarVo.setVin(customerCar.getVin());
                baseCarVo.setContactName(customer.getContact());
                baseCarVo.setContactMobile(customer.getContactMobile());
                param.put("sorts", new String[]{"gmtModified desc"});
                param.put("offset",0);
                param.put("limit",1);
                getCarImgUrl(customerCar.getId(),param,baseCarVo);
            } else {
                log.error("APP调用获取车辆信息失败,客户id为空,参数：shopId={},license={}", shopId, license);
            }
        } else {
            log.error("APP调用获取车辆信息为空,参数：shopId={},license={}", shopId, license);
        }
        return Result.wrapSuccessfulResult(baseCarVo);
    }

    @Override
    public Result<List<ApiCustomerSearchVoDTO>> customerSearch(final Long shopId, final String key, final String licenseNot,final Integer page,final Integer size) {
        return new BizTemplate<Result<List<ApiCustomerSearchVoDTO>>>(){
            @Override
            protected void checkParams() throws IllegalArgumentException {
                if(page == null){
                    throw new IllegalArgumentException("page 不能为空");
                }
                if(size == null){
                    throw new IllegalArgumentException("size 不能为空");
                }
                if(shopId == null){
                    throw new IllegalArgumentException("shopId 不能为空");
                }
            }

            @Override
            protected Result<List<ApiCustomerSearchVoDTO>> process() throws BizException {
                LegendCustomerCarRequest customerCarRequest = new LegendCustomerCarRequest();
                customerCarRequest.setShopId(shopId.toString());
                if( StringUtils.isNotBlank(key)){
                    customerCarRequest.setSearchKey(key);
                }
                if( StringUtils.isNotBlank(licenseNot)){
                    customerCarRequest.setLicenseNot(licenseNot);
                }

                int pageNum = page - 1;
                PageableRequest pageableRequest = new PageableRequest(pageNum,size);
                DefaultPage defaultPage = customerCarFacade.getCustomerCarsFromSearch(pageableRequest,customerCarRequest);
                List<CustomerCarVo> customerCarVos = defaultPage.getContent();
                List<ApiCustomerSearchVoDTO> apiCustomerSearchVoList = Lists.newArrayList();
                for (CustomerCarVo carVo : customerCarVos) {
                    ApiCustomerSearchVoDTO apiCustomerSearchVo = new ApiCustomerSearchVoDTO();
                    if(carVo.getId() != null){
                        apiCustomerSearchVo.setCarId(Long.parseLong(carVo.getId()));
                    }
                    if(carVo.getRepairCount() != null){
                        apiCustomerSearchVo.setRepairCount(Integer.parseInt(carVo.getRepairCount()));
                    }else {
                        apiCustomerSearchVo.setRepairCount(0);
                    }
                    apiCustomerSearchVo.setCarInfo(carVo.getCarInfo());
                    apiCustomerSearchVo.setMobile(carVo.getMobile());
                    apiCustomerSearchVo.setLicense(carVo.getLicense());
                    apiCustomerSearchVo.setCustomerName(carVo.getCustomerName());
                    apiCustomerSearchVoList.add(apiCustomerSearchVo);
                }
                return Result.wrapSuccessfulResult(apiCustomerSearchVoList);
            }
        }.execute();
    }

    @Override
    public Result<List<CustomerCarBoDTO>> showCustomerHistoryList(Long shopId, String keyWord,Integer page,Integer size) {
        Map<String,Object> params = new HashMap<>();
        params.put("shopId",shopId);
        params.put("keyWord",keyWord);
        Page<CustomerCarBo> carBoDTOPage = null;
        Pageable pageable = new PageRequest(page,size);
        try {
            carBoDTOPage = customerCarService.getPage(pageable,params);
            List<CustomerCarBo> customerCarBos = carBoDTOPage.getContent();
            if(!CollectionUtils.isEmpty(customerCarBos)){
                for(CustomerCarBo customerCarBo : customerCarBos){
                    if(StringUtils.isEmpty(customerCarBo.getLicense())){
                        customerCarBo.setLicense("暂无车牌");
                    }
                }
            }
        }catch (Exception e){
            log.error("查询客户历史信息失败：AppCustomerController中的showCustomerHistoryList",e);
            return Result.wrapErrorResult("","查询客户历史信息失败");
        }
        List<CustomerCarBoDTO> customerCarBoDTOs = BdUtil.bo2do4List(carBoDTOPage.getContent(),CustomerCarBoDTO.class);
        return Result.wrapSuccessfulResult(customerCarBoDTOs);
    }

    @Override
    public Result updateCustomerCar(Long shopId, ApiCarVoDTO apiCarVoDTO) {
        try {
            if (apiCarVoDTO != null && apiCarVoDTO.getCarId() != null) {
                ApiCarVo apiCarVo = BdUtil.bo2do(apiCarVoDTO,ApiCarVo.class);
                prechecksService.toAppCustomerCarUpdate(shopId, apiCarVo);
                log.info("APP调用车辆更新成功");
                return Result.wrapSuccessfulResult("车辆更新成功");
            } else {
                log.error("APP调用门店id={}车辆更新{}失败", shopId, LogUtils.objectToString(apiCarVoDTO));
                return Result.wrapErrorResult("",LegendError.COMMON_ERROR.getMessage());
            }
        } catch (BizException e) {
            log.info("APP调用车辆更新失败, shopId=" + shopId + "e:", e);
            return Result.wrapErrorResult(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("APP调用门店id={}车辆更新{}异常,异常信息：{}", shopId, LogUtils.objectToString(apiCarVoDTO), e);
            return Result.wrapErrorResult("",LegendError.COMMON_ERROR.getMessage());
        }

    }

    @Override
    public Result<ApiCustomerCarVoDTO> getCarInfo(Long shopId, Long carId) {
        log.info("APP调用获取车辆信息,参数：shopId={},carId={}", shopId, carId);
        ApiCustomerCarVoDTO apiCustomerCarVo = null;
        try {
            CarDetailVo  carDetailVo = customerCarFacade.getCarInfo(carId, shopId);
            if(carDetailVo == null){
                log.error("APP调用获取车辆信息失败,查无此车辆信息,参数：shopId={},carId={}", shopId, carId);
                return Result.wrapSuccessfulResult(apiCustomerCarVo);
            }
            CustomerCar car = carDetailVo.getCustomerCar();
            apiCustomerCarVo = new ApiCustomerCarVoDTO();
            Customer customer = carDetailVo.getCustomer();
            if(car != null){
                apiCustomerCarVo.setCarId(car.getId());
                if (StringUtils.isNotBlank(car.getCarInfo())) {
                    apiCustomerCarVo.setCarInfo(car.getCarInfo());
                }
                apiCustomerCarVo.setCustomerId(car.getCustomerId());
                apiCustomerCarVo.setLicense(car.getLicense());
                apiCustomerCarVo.setCarSeriesId(car.getCarSeriesId());
                apiCustomerCarVo.setCarModelId(car.getCarModelId());
                apiCustomerCarVo.setMileage(car.getMileage());
                apiCustomerCarVo.setCarPower(car.getCarPower());
                apiCustomerCarVo.setCarPowerId(car.getCarPowerId());
                apiCustomerCarVo.setCarCompany(car.getCarCompany());
                apiCustomerCarVo.setImportInfo(car.getImportInfo());
                apiCustomerCarVo.setCarBrand(car.getCarBrand());
                apiCustomerCarVo.setCarBrandId(car.getCarBrandId());
                apiCustomerCarVo.setCarModel(car.getCarModel());
                apiCustomerCarVo.setCarSeries(car.getCarSeries());
                apiCustomerCarVo.setCarYear(car.getCarYear());
                apiCustomerCarVo.setCarYearId(car.getCarYearId());
                apiCustomerCarVo.setCarGearBox(car.getCarGearBox());
                apiCustomerCarVo.setCarGearBoxId(car.getCarGearBoxId());
            }
            if(customer != null){
                if (StringUtils.isNotBlank(customer.getMobile())) {
                    apiCustomerCarVo.setMobile(customer.getMobile());
                }
                if (StringUtils.isNotBlank(customer.getCustomerName())) {
                    apiCustomerCarVo.setCustomerName(customer.getCustomerName());
                }
            }

            apiCustomerCarVo.setTotalOrderCount(carDetailVo.getTotalOrderCount());
            apiCustomerCarVo.setValidOrderCount(carDetailVo.getValidOrderCount());
            apiCustomerCarVo.setSuspendPaymentCount(carDetailVo.getSuspendPaymentCount());
            apiCustomerCarVo.setSuspendAmount(carDetailVo.getSuspendAmount());
            apiCustomerCarVo.setRecent6MonthAmount(carDetailVo.getRecent6MonthAmount());
            //兼容APP版本，此字段保留
            apiCustomerCarVo.setLastOrderDate(statisticsOrderService.getLastOrderDate(shopId, carId));//最近一次到店时间
            /**
             * ========================start========获取车辆图片==========start========================
             */
            //来自车辆
            getCarImgUrlList(carId, shopId, apiCustomerCarVo);
            /**
             * ========================end========获取车辆图片==========end========================
             */
        } catch (Exception e) {
            log.error("获取车辆信息失败.shopId->"+shopId+",carId->"+carId, e);
            return Result.wrapErrorResult("", "获取车辆信息失败.");
        }
        return Result.wrapSuccessfulResult(apiCustomerCarVo);
    }

    @Override
    public Result<String> getSerialNumber(Long shopId, Long userId, String serialType) {
        log.info("APP获取预检单编号,入参：shopId={},userId={},serialType={}",shopId,userId,serialType);
        Map params = Maps.newConcurrentMap();
        params.put("userId", userId);
        params.put("shopId", shopId);
        params.put("serialType", serialType);
        String serialNumber = serialNumberService.getSerialNumber(params);
        return Result.wrapSuccessfulResult(serialNumber);
    }

    /**
     * 获取图片路径
     * @param carId 车辆id
     * @param shopId 参数
     * @param apiCustomerCarVo
     */
    private void getCarImgUrlList(Long carId, Long shopId, ApiCustomerCarVoDTO apiCustomerCarVo) {
        Map param = Maps.newConcurrentMap();
        param.put("shopId", shopId);
        param.put("relType", 1);
        param.put("relId", carId);
        List<CustomerCarFile> customerCarFiles = customerCarFileService.select(param);
        if (!CollectionUtils.isEmpty(customerCarFiles)) {
            List<ApiCarImgVoDTO> apiCarImgVoList = Lists.newArrayList();
            for (CustomerCarFile customerCarFile : customerCarFiles) {
                ApiCarImgVoDTO apiCarImgVo = new ApiCarImgVoDTO();
                apiCarImgVo.setId(customerCarFile.getId());
                apiCarImgVo.setPath(customerCarFile.getFilePath());
                apiCarImgVoList.add(apiCarImgVo);
            }
            apiCustomerCarVo.setCarImgList(apiCarImgVoList);
        } else {
            param.put("customerCarId", carId);
            List<Prechecks> prechecksList = prechecksService.getAllCarPreCheckHeads(param);
            List<Long> preIds = Lists.newArrayList();
            if (!CollectionUtils.isEmpty(prechecksList)) {
                for (Prechecks prechecks : prechecksList) {
                    preIds.add(prechecks.getId());
                }
                //来自预检单
                param.remove("relId");
                param.put("relType", 2);
                param.put("fileType", 5);
                param.put("relIds", preIds.toArray(new Long[] { }));
                List<CustomerCarFile> customerCarFileList = customerCarFileService.select(param);
                if (!CollectionUtils.isEmpty(customerCarFileList)) {
                    List<ApiCarImgVoDTO> apiCarImgVoList = Lists.newArrayList();
                    for (CustomerCarFile customerCarFile : customerCarFileList) {
                        ApiCarImgVoDTO apiCarImgVo = new ApiCarImgVoDTO();
                        apiCarImgVo.setId(customerCarFile.getId());
                        apiCarImgVo.setPath(customerCarFile.getFilePath());
                        apiCarImgVoList.add(apiCarImgVo);
                    }
                    apiCustomerCarVo.setCarImgList(apiCarImgVoList);
                }
            }
        }
    }

    private ELResult<CustomerCarVoDTO> _searchByElasticsearch(Map<String, Object> paramMap, Pageable pageable) {
        if (pageable != null) {
            paramMap.put("page", pageable.getPageNumber());
            paramMap.put("size", pageable.getPageSize());
        }
        ELResult<CustomerCarVoDTO> result = null;
        String searchUrl = iSearchUrl + "elasticsearch/cloudRepair/legendCustomerCar/customerCar";

        try {
            List<String> params = new ArrayList<>(paramMap.size());
            Iterator<Map.Entry<String, Object>> ltr = paramMap.entrySet().iterator();
            while (ltr.hasNext()) {
                Map.Entry<String, Object> e = ltr.next();
                String paramName = e.getKey();
                Object paramValue = e.getValue();

                if (StringUtils.isNotBlank(paramName)) {
                    if (paramValue instanceof Date) {
                        paramValue = DateUtil.convertDateToYMDHMS((Date) paramValue);
                    }
                    if ("sorts".equals(paramName)) {
                        Object type = paramMap.get("tabelType");
                        if (type != null) {
                            params.add(String.format("sortBy=%sTime", type.toString()));
                            params.add("sortType=asc");
                        }

                    } else {
                        params.add(String.format("%s=%s", paramName, UriUtils.encodeQueryParam(paramValue.toString(), "utf-8")));
                    }
                }
            }
            String responseText = HttpUtil.sendGet(searchUrl, StringUtils.join(params.toArray(), "&"));
            if (log.isDebugEnabled()) {
                log.debug("调用搜索时的参数：{}", LogUtils.objectToString(params));
            }
            if (StringUtils.isNotEmpty(responseText)) {
                result = JSON.parseObject(responseText, new TypeReference<ELResult<CustomerCarVoDTO>>() {
                });

            }
        } catch (Exception e) {
            log.error("invoke search error.", e);
        }
        return result;
    }

    /**
     * 获取图片路径
     * @param carId 车辆id
     * @param param 参数
     */
    private void getCarImgUrl(Long carId, Map param, ApiBaseCarVoDTO apiBaseCarVo) {
        param.put("relType", 1);
        param.put("relId", carId);
        List<CustomerCarFile> customerCarFiles = customerCarFileService.select(param);
        if (!CollectionUtils.isEmpty(customerCarFiles)) {
            apiBaseCarVo.setImgUrl(customerCarFiles.get(0).getFilePath());
        } else {
            param.put("customerCarId", carId);
            List<Prechecks> prechecksList = prechecksService.getAllCarPreCheckHeads(param);
            List<Long> preIds = Lists.newArrayList();
            if (!CollectionUtils.isEmpty(prechecksList)) {
                for (Prechecks prechecks : prechecksList) {
                    preIds.add(prechecks.getId());
                }
                //来自预检单
                param.remove("relId");
                param.put("relType", 2);
                param.put("fileType", 5);
                param.put("relIds", preIds.toArray(new Long[] { }));
                List<CustomerCarFile> customerCarFileList = customerCarFileService.select(param);
                if (!CollectionUtils.isEmpty(customerCarFileList)) {
                    apiBaseCarVo.setImgUrl(customerCarFileList.get(0).getFilePath());
                }
            }
        }
    }
}
