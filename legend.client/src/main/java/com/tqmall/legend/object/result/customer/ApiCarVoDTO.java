package com.tqmall.legend.object.result.customer;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by dingbao on 16/9/22.
 */
@Data
public class ApiCarVoDTO implements Serializable {
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

}
