package com.tqmall.legend.object.param.order;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by xin on 16/9/3.
 */
@Data
public class CustomerFeedbackDTO implements Serializable {
    private String ver; //APP版本号
    private String refer;//数据来源 1.Android 2.ios
    private Long userId;  //用户id
    private Long shopId;  //门店id
    private Long orderId;  //工单id

    private Integer receptionStar;  //接待评价
    private Integer repairStar;    //维修评价
    private Integer sendcarStar;  //交车评价
    private Integer totalStar;    //综合评价
    private String customerFeedback;  //客户意见
    private String visitMethod;  //回访方式
}
