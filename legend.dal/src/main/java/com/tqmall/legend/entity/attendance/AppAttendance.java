package com.tqmall.legend.entity.attendance;

import com.tqmall.common.util.DateUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.ToString;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = false)
@Data
@ToString
public class AppAttendance extends BaseEntity {

    private Long shopId;
    private Long userId;
    private Date workTime;
    private String status;
    private String longitude;
    private String latitude;
    private String leftTime;
    private String rightTime;
    private String statusStr;
    private String dayStr;
    private String ver;
    private String refer;
    private Integer locationIsValid;//0：无效 1：有效
    private String userName;
    private Integer signStatus;//签到状态 1：正常  2：迟到  3：早退

    public String  getLeftTime(){
        if(workTime != null){
            return DateUtil.convertDateToTime(workTime);
        }
        return null;
    }

    public String getRightTime(){
        if(workTime != null){
            return DateUtil.getWeeksOfDate(workTime);
        }
        return null;
    }

    public String getStatusStr(){
        if(status != null) {
            if (status.equals("1")) {
                return "上班打卡";
            }
            if (status.equals("2")) {
                return "下班打卡";
            }
        }
        return null;
    }
}

