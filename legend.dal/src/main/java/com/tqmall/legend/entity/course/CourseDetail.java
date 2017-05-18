package com.tqmall.legend.entity.course;

import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.text.SimpleDateFormat;
import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Data
public class CourseDetail extends BaseEntity {

    private String title;
    private String pic;
    private String content;
    private Long courseId;
    private Date tradeTime;
    private String address;
    private Long limitCount;
    private Long joinCount;
    private Long sort;
    private Integer status;

    private String tradeTimeStr;
    private String dateStr;
    private boolean outDate;
    private Integer checkDate;
    private CourseShopRel courseShopRel;//课次对应的报名情况

    public String getTradeTimeStr(){
        if(tradeTimeStr!=null){
            return tradeTimeStr;
        }else if(tradeTime!=null){
            return DateUtil.convertDateToYMDHMS(tradeTime);
        }
        return null;
    }

    public String getDateStr(){
        if(dateStr!=null){
            return dateStr;
        }else if(tradeTime!=null){
            StringBuffer str = new StringBuffer();
            SimpleDateFormat df = new SimpleDateFormat("MM月dd日");
            str.append(DateUtil.getWeekOfDate(tradeTime)+"("+df.format(tradeTime)+")");
            return str.toString();
        }
        return null;
    }

}
