package com.tqmall.legend.biz.service;


import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.shop.ShopConfigure;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ServiceWarnService{

    /**
     * 查询不在此范围内的车辆信息
     */
    List<CustomerCar> customerCarsNotExit(Map map);

    /**
     * 定时任务执行时的服务提醒相关操作
     * @param shopConfigures 服务提醒设置
     * @param serviceType 服务提醒类型
     * @param current_time 系统当前时间
     * @param customerCarList 客户车辆列表
     */
    public void saveAndUpdateByTimer(List<ShopConfigure> shopConfigures,Integer serviceType, Date current_time, List<CustomerCar> customerCarList,Map map);

    /**
     * 添加客户信息时,保存或者更新服务提醒
     * @param serviceType 服务类型
     * @param params 参数
     * @param customerCar 客户车辆信息
     */
    public void saveAndUpdateServiceWarns(Integer serviceType,Map<String, Object> params, CustomerCar customerCar);

    /**
     * 查询今日已到期的提醒信息,更新为过期
     */
    public List<Long> carIds(Map map,Integer serviceType, Date current_time);

}
