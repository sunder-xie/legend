package com.tqmall.legend.biz.bo.attendance;

import com.tqmall.common.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * Created by 123 on 2016/3/21 0021.
 */
@Data
@AllArgsConstructor
public class SignTime {

    private Date signInTime;
    private Date signOffTime;
    private Date noonBreakStartTime;
    private Date noonBreakEndTime;

    public SignTime() {
    }

    public SignTime(String timein, String timeoff) {
        signInTime = DateUtil.convertStringToDate(timein, "HH:mm:ss");
        signOffTime = DateUtil.convertStringToDate(timeoff, "HH:mm:ss");
    }

    public SignTime(String timein, String timeoff, String noonBreakStart, String noonBreakEnd) {
        signInTime = DateUtil.convertStringToDate(timein, "HH:mm:ss");
        signOffTime = DateUtil.convertStringToDate(timeoff, "HH:mm:ss");
        noonBreakStartTime = DateUtil.convertStringToDate(noonBreakStart, "HH:mm:ss");
        noonBreakEndTime = DateUtil.convertStringToDate(noonBreakEnd, "HH:mm:ss");
    }


}
