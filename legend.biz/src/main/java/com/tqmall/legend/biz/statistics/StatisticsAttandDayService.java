package com.tqmall.legend.biz.statistics;

import com.tqmall.legend.bi.entity.AttendanceStatis;
import com.tqmall.legend.bi.entity.CommonPair;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by cloudgu on 15/6/10.
 */
public interface StatisticsAttandDayService {


    public List<AttendanceStatis> getDateList(Map<String, Object> param);

    public List<AttendanceStatis> getAttandInfoListByDays(Map<String, Object> param);

    public long getAttendInfoCount(Map<String,Object> param);

    CommonPair<Date, Date> getMinMaxSignDate(Map<String,Object> param);


}
