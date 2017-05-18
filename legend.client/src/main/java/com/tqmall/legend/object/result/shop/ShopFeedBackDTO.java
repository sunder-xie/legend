package com.tqmall.legend.object.result.shop;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by feilong.li on 16/9/5.
 */
@Data
public class ShopFeedBackDTO implements Serializable{

    private static final long serialVersionUID = -1489096937159094668L;

    private Long shopId ;   //   门店id
    private Long orderId;   //工单id
    private Long customerCarId ;    //客户车辆id
    private String mobile;//   手机号
    private String contactName;//   联系人
    private String contactMobile;//   联系电话
    private String carLicense;//   车牌
    private Date noteTime;  //提醒时间
    private Integer noteFlag ;//   提醒标记 0 未提醒 1 已提醒 2 失效
    private Integer orderTag;   //工单类型
    private String orderStatus;    //工单状态
    private String carInfo; //车型信息

}
