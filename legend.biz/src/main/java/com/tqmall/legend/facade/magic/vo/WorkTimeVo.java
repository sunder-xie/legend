package com.tqmall.legend.facade.magic.vo;

import com.tqmall.common.util.DateUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by shulin on 16/7/12.
 */
@Setter
@Getter
public class WorkTimeVo {
    private static WorkTimeVo workTimeVo = new WorkTimeVo();
    private String openTime = "8:00";  //默认上班时间
    private String closeTime = "18:00";  //默认下班时间
    private String noonBreakStartTime = "12:00";  //午休开始时间
    private String noonBreakEndTime = "13:00";   //午休结束时间

    private Date openDate;
    private Date closeDate;
    private Date noonBreakStartDate;
    private Date noonBreakEndDate;

    private WorkTimeVo(){
    }

    public static WorkTimeVo getInstance(){
        return workTimeVo;
    }
    public Date getOpenDate() {
        return DateUtil.convertStringToTodayYMDHMS(openTime);
    }

    public Date getCloseDate() {
        return DateUtil.convertStringToTodayYMDHMS(closeTime);
    }

    public Date getNoonBreakStartDate() {
        return DateUtil.convertStringToTodayYMDHMS(noonBreakStartTime);
    }

    public Date getNoonBreakEndDate() {
        return DateUtil.convertStringToTodayYMDHMS(noonBreakEndTime);
    }

}
