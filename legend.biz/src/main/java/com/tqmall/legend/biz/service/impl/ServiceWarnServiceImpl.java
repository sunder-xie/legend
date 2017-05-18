package com.tqmall.legend.biz.service.impl;

import com.google.common.collect.Lists;
import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.service.ServiceWarnService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.dao.service.ServiceWarnDao;
import com.tqmall.legend.dao.shop.ShopConfigureDao;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.order.ServiceStatusEnum;
import com.tqmall.legend.entity.service.ServiceWarn;
import com.tqmall.legend.entity.shop.ShopConfigure;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**   
 * @Description: 服务提醒Service
 * @date 2015-11-02
 * @version V1.0   
 *
 */
@Service
public class ServiceWarnServiceImpl extends BaseServiceImpl implements ServiceWarnService{
    private final Logger logger = LoggerFactory.getLogger(ServiceWarnServiceImpl.class);
    @Autowired
    private ServiceWarnDao serviceWarnDao;
    @Autowired
    private ShopConfigureDao shopConfigureDao;

    @Override
    public List<CustomerCar> customerCarsNotExit(Map map) {
        return serviceWarnDao.customerCarsNotExit(map);
    }

    @Override
    public List<Long> carIds(Map map,Integer serviceType, Date current_time) {
        //查询未处理的保养提醒信息
        map.put("serviceStatus", ServiceStatusEnum.NOT_PROCESS.getCode());
        map.put("serviceType", serviceType);
        List<ServiceWarn> serviceWarns = serviceWarnDao.select(map);
        List<Long> carIdList = Lists.newArrayList();
        for (ServiceWarn warn : serviceWarns) {
            Date yesterday = DateUtil.getDateBy(current_time, -1);
            //查询今日已到期的提醒信息,更新为过期
            if(warn.getWarnEndTime().compareTo(yesterday) ==0 ){
                warn.setServiceStatus(ServiceStatusEnum.EXPIRED.getCode());
                serviceWarnDao.updateById(warn);
                logger.info("通过服务类型serviceType= {},当前时间{}更新服务id={}未处理保养提醒信息为[通知已过期]成功",serviceType,current_time,warn.getId());
            }
            carIdList.add(warn.getCarId());
        }
        return carIdList;
    }

    @Override
    public void saveAndUpdateServiceWarns(Integer serviceType, Map<String, Object> params, CustomerCar customerCar) {
        try {
            List<ShopConfigure> shopConfigures = shopConfigureDao.select(params);
            if(!CollectionUtils.isEmpty(shopConfigures) && null != customerCar && customerCar.getKeepupTime() !=null){
                ShopConfigure shopConfigure = shopConfigures.get(0);
                int value = Integer.parseInt(shopConfigure.getConfValue());
                Date current_time = DateUtil.convertStringToDateYMD(DateUtil.convertDateToYMD(new Date()));
                //保养到期时间
                Date warn_end_time = customerCar.getKeepupTime();
                if(logger.isDebugEnabled()){
                    logger.debug("保养设置提醒时间段为{}",value);
                }
                Date warn_start_time  = DateUtil.getDateBy(warn_end_time,-value);

                params.put("carId",customerCar.getId());
                params.put("serviceType",serviceType);
                params.put("serviceStatus", ServiceStatusEnum.NOT_PROCESS.getCode());
                List<ServiceWarn> serviceWarns = serviceWarnDao.select(params);
                int space = DateUtil.daysBetween(current_time,warn_end_time);

                if(warn_end_time.compareTo(current_time)>=0 && space <= value){
                    //新增或者更新提醒通知
                    if(!CollectionUtils.isEmpty(serviceWarns)){
                        ServiceWarn serviceWarn = serviceWarns.get(0);
                        serviceWarn.setServiceStatus(ServiceStatusEnum.NOT_PROCESS.getCode());
                        serviceWarn.setWarnStartTime(warn_start_time);
                        serviceWarn.setWarnEndTime(warn_end_time);
                        serviceWarn.setCarLicense(customerCar.getLicense());
                        serviceWarnDao.updateById(serviceWarn);
                        logger.info("通过服务类型serviceType= {},当前时间{}更新服务id={}未处理保养提醒信息成功",serviceType,current_time,serviceWarn.getId());
                    }else {
                        ServiceWarn serviceWarn = new ServiceWarn();
                        serviceWarn.setShopId(customerCar.getShopId());
                        serviceWarn.setCarId(customerCar.getId());
                        serviceWarn.setWarnEndTime(warn_end_time);
                        serviceWarn.setWarnStartTime(warn_start_time);
                        serviceWarn.setCarLicense(customerCar.getLicense());
                        serviceWarn.setServiceType(serviceType);
                        serviceWarnDao.insert(serviceWarn);
                        logger.info("通过客户信息来添加保养提醒服务成功");
                    }

                }else{
                    if(!CollectionUtils.isEmpty(serviceWarns)){
                        ServiceWarn serviceWarn = serviceWarns.get(0);
                        serviceWarn.setServiceStatus(ServiceStatusEnum.CANCEL.getCode());
                        serviceWarn.setCarLicense(customerCar.getLicense());
                        serviceWarnDao.updateById(serviceWarn);
                        logger.info("通过服务类型serviceType= {},当前时间{}更新服务id={}未处理保养提醒信息为[通知已废弃]成功",serviceType,current_time,serviceWarn.getId());
                    }
                }
            }
        }catch(ParseException e){
            logger.error("比较两个时间段的天数异常,信息：{}", e);
        }catch (Exception e){
            logger.error("保存或者更新服务提醒信息异常,信息：{}",e);
        }

    }

    @Override
    public void saveAndUpdateByTimer(List<ShopConfigure> shopConfigures, Integer serviceType, Date current_time, List<CustomerCar> customerCarList,Map map) {
        List<ServiceWarn> serviceWarnList = Lists.newArrayList();
        for (ShopConfigure shopConfigure : shopConfigures) {
            Long shopId = shopConfigure.getShopId();
            if(StringUtils.isNotBlank(shopConfigure.getConfValue()) &&
                    StringUtil.isNumeric(shopConfigure.getConfValue())){
                int value = Integer.parseInt(shopConfigure.getConfValue());

                for (CustomerCar car : customerCarList) {
                    if(shopId.compareTo(car.getShopId())==0 && car != null && car.getKeepupTime() != null){
                        Date warn_end_time = car.getKeepupTime();
                        Date warn_start_time  = DateUtil.getDateBy(warn_end_time, -value);
                        try {
                            int space = DateUtil.daysBetween(current_time,warn_end_time);
                            //在保养提醒时间段内的车辆
                            if(warn_end_time.compareTo(current_time)>=0 && space <= value){
                                ServiceWarn serviceWarn = new ServiceWarn();
                                serviceWarn.setShopId(car.getShopId());
                                serviceWarn.setCarId(car.getId());
                                serviceWarn.setWarnEndTime(warn_end_time);
                                serviceWarn.setWarnStartTime(warn_start_time);
                                serviceWarn.setCarLicense(car.getLicense());
                                serviceWarn.setServiceType(serviceType);
                                serviceWarnList.add(serviceWarn);
                            }
                        } catch (ParseException e) {
                            logger.error("比较两个时间段的天数异常,信息：{}",e);
                        }
                    }
                }
            }
        }


        if(!CollectionUtils.isEmpty(serviceWarnList)){
            map.clear();
            //查询未处理的保养提醒信息
            map.put("serviceStatus", ServiceStatusEnum.NOT_PROCESS.getCode());
            map.put("serviceType", serviceType);
            List<ServiceWarn> serviceWarns = serviceWarnDao.select(map);
            if(!CollectionUtils.isEmpty(serviceWarns)){
                for (ServiceWarn serviceWarn : serviceWarnList) {
                    for (ServiceWarn warn : serviceWarns) {
                        if(serviceWarn.getShopId().compareTo(warn.getShopId())==0 &&
                                serviceWarn.getCarId().compareTo(warn.getCarId()) ==0 &&
                                serviceWarn.getServiceType().compareTo(warn.getServiceType()) == 0
                                ){
                            serviceWarnList.remove(serviceWarn);
                            break;
                        }
                    }
                }
            }
            serviceWarnDao.batchInsert(serviceWarnList);
            logger.info("保养提醒服务定时批量添加成功");
        }
    }
}