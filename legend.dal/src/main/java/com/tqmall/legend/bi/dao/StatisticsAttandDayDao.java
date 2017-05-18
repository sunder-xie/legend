package com.tqmall.legend.bi.dao;

import com.tqmall.legend.bi.entity.AttendanceStatis;
import com.tqmall.legend.bi.entity.CommonPair;
import com.tqmall.legend.dao.common.MyBatisRepository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by 123 on 2016/3/23 0023.
 */
@MyBatisRepository
public interface StatisticsAttandDayDao {

   public List<AttendanceStatis> getDateList(Map<String, Object> param);

   public List<AttendanceStatis> getAttandInfoListByDays(Map<String, Object> param);

   public long getAttendInfoCount(Map<String, Object> param);

   CommonPair<Date, Date> getMinMaxSignDate(Map<String, Object> param);

}
