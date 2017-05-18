package com.tqmall.legend.entity.customer;

import com.tqmall.common.util.DateUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

import java.text.SimpleDateFormat;
import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Data
public class CustomerFeedback extends BaseEntity {

    private Long orderId;
    private String carLicense;
    private Long customerId;
    private String customerName;
    private Long customerCarId;
    private Long carBrandId;
    private String carBrandName;
    private Long carSeriesId;
    private String carSeriesName;
    private String carAlias;
    private String mobile;
    private Integer receptionStar;
    private Integer repairStar;
    private Integer sendcarStar;
    private Integer totalStar;
    private String customerFeedback;
    private Long visitorId;
    private String visitorName;
    private String visitMethod;
    private Date visitTime;
    private Date finishTime;
    private Long shopId;
    private String refer;
    private String ver;
    private Date nextVisitTime;// 下次回访时间
    private Long noteInfoId;
    private Integer noteType;

    private String finishTimeFormat;
    private String nextVisitTimeStr;//下次回访时间

    public String getFinishTimeFormat() {
        if(finishTimeFormat != null){
            return finishTimeFormat ;
        }
        SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        if (finishTime != null) {
            return df.format(finishTime);
        } else {
            return null;
        }
    }

    private String visitTimeFormat;

    public String getVisitTimeFormat() {
        if(visitTimeFormat != null){
            return visitTimeFormat ;
        }
        SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        if (visitTime != null) {
            return df.format(visitTime);
        } else {
            return null;
        }
    }

    public String getNextVisitTimeStr(){
        return DateUtil.convertDateToYMD(nextVisitTime);
    }
}

