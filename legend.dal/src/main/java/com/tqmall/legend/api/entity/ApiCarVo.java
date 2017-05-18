package com.tqmall.legend.api.entity;

import com.tqmall.common.util.DateUtil;
import lombok.Data;

import java.util.Date;

/**
 * Created by mokala on 11/13/15.
 * 车辆信息Vo
 */
@Data
public class ApiCarVo {
    /**
     * 用户id
     */
    private Long userId = Long.valueOf("0");
    /**
     * 车辆id
     */
    private Long carId = Long.valueOf("0");
    /**
     * 客户id
     */
    private Long customerId = Long.valueOf("0");

    /**
     * 车牌信息
     */
    private String license = "";

    /**
     * 车型信息,已经组装好,格式为:品牌 车型 年款 批量
     */
    private String carInfo = "";

    /**
     * 车主信息
     */
    private String customerName = "";
    /**
     * 车主电话
     */
    private String mobile = "" ;
    /**
     * 行驶里程
     */
    private String mileage;

    /**
     * 下次保养里程
     */
    private String nextUpkeepMileage;

    /**
     * 下次保养时间
     */
    private Date nextUpkeepTime;
    /**
     * 保险时间
     */
    private Date insuranceTime;

    /**
     * 下次保养时间
     */
    private String nextUpkeepTimeStr;
    /**
     * 保险时间
     */
    private String insuranceTimeStr;

    /**
     * 车辆图片路径
     */
    private String imgUrl;
    /**
     * 数据来源：0：web, 1 : android, 2 : IOS
     */
    private String refer = "1";
    /**
     * 版本
     */
    private String ver;


    public String getInsuranceTimeStr(){
        if(insuranceTimeStr != null){
             return insuranceTimeStr;
        }
        return DateUtil.convertDateToYMD(insuranceTime);
    }
    public String getNextUpkeepTimeStr(){
        if(nextUpkeepTimeStr != null){
            return  nextUpkeepTimeStr;
        }
        return DateUtil.convertDateToYMD(nextUpkeepTime);
    }

}
