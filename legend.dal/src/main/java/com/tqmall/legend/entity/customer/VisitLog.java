package com.tqmall.legend.entity.customer;

import com.tqmall.legend.entity.base.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 回访记录
 * @Author 王佳超<jiachao.wang@tqmall.com>
 * @Create 2015年7月8日下午7:55:36
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class VisitLog extends BaseEntity {
    private byte satisfactionStar;
    private byte purchaseIntentionStar;
    private Long shopId;
    private Date nextVisitTime;
    private String nextVisitTimeFormat;
    private Date visitTime;
    private String visitTimeFormat;
    private String visitMethod;
    private String content;
    private String visitorName;
    private int visitorId;
    private int customerCarId;
    private int customerId;
    private byte visitType;
    private Date expiredTime;
    private String expiredTimeFormat;
    
    public String getNextVisitTimeFormat(){
    	if(nextVisitTimeFormat != null){
            return nextVisitTimeFormat ;
        }
        SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        if (nextVisitTime != null) {
            return df.format(nextVisitTime);
        } else {
            return null;
        }
    }
    public String getVisitTimeFormat(){
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
    public String getExpiredTimeFormat(){
    	if(expiredTimeFormat != null){
            return expiredTimeFormat ;
        }
        SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        if (expiredTime != null) {
            return df.format(expiredTime);
        } else {
            return null;
        }
    }
}
