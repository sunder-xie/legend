package com.tqmall.legend.biz.statistics.impl;

import com.tqmall.legend.bi.dao.StatisticsAttandDayDao;
import com.tqmall.legend.bi.entity.AttendanceStatis;
import com.tqmall.legend.bi.entity.CommonPair;
import com.tqmall.legend.biz.statistics.StatisticsAttandDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by 123 on 2016/3/23 0023.
 */
@Service
public class StatisticsAttandDayServiceImpl implements StatisticsAttandDayService{

    @Autowired
    private StatisticsAttandDayDao statisticsAttandDayDao;

    @Override
    public List<AttendanceStatis> getDateList(Map<String, Object> param) {
        return statisticsAttandDayDao.getDateList(param);
    }

    @Override
    public List<AttendanceStatis> getAttandInfoListByDays(Map<String, Object> param) {
        return statisticsAttandDayDao.getAttandInfoListByDays(param);
    }

    @Override
    public long getAttendInfoCount(Map<String, Object> param) {
        return statisticsAttandDayDao.getAttendInfoCount(param);
    }

    @Override
    public CommonPair<Date, Date> getMinMaxSignDate(Map<String, Object> param) {
        return statisticsAttandDayDao.getMinMaxSignDate(param);
    }
}
