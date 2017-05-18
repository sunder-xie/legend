package com.tqmall.common.util;

import com.tqmall.common.exception.BizException;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 杨毅
 *         <p/>
 *         2014年6月27日
 */
public class DateUtil {

    static Logger logger = LoggerFactory.getLogger(DateUtil.class);

    public static final String DATE_FORMAT_YMDHM = "yyyy-MM-dd HH:mm";
    public static final String DATE_FORMAT_YMD = "yyyy-MM-dd";

    public static Date long2Date(long millis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        return c.getTime();
    }

    /**
     * 把时间类型转化为规定格式的字符串
     * "yyyy-MM-dd hh:MM:ss"
     *
     * @param date 时间
     * @return 格式化以后的时间字符串
     */
    public static String convertDateToYMDHMS(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return f.format(date);
    }

    /**
     * 把时间类型转化为规定格式的字符串
     * "yyyy-MM-dd hh:MM"
     *
     * @param date 时间
     * @return 格式化以后的时间字符串
     */
    public static String convertDateToYMDHHmm(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return f.format(date);
    }

    /**
     * 把时间类型转化为规定格式的字符串
     * "yyyy-MM-dd hh:MM:ss"
     *
     * @param date 时间
     * @return 格式化以后的时间字符串
     */
    public static String convertDateToYMDHM(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat f = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        return f.format(date);
    }

    /**
     * 把时间类型转化为规定格式的字符串
     * "MM月dd日 HH:mm"
     *
     * @param date 时间
     * @return 格式化以后的时间字符串
     */
    public static String convertDateToMDHM(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat f = new SimpleDateFormat("MM月dd日 HH:mm");
        return f.format(date);
    }

    /**
     * 把时间类型转化为规定格式的字符串
     * "yyyy年MM月dd日"
     *
     * @param date
     * @return
     */
    public static String convertDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
        if (date != null) {
            return df.format(date);
        } else {
            return "";
        }
    }

    /**
     * 把时间类型转化为规定格式的字符串
     * "yyyy.MM.dd"
     *
     * @param date
     * @return
     */
    public static String convertDate1(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd");
        if (date != null) {
            return df.format(date);
        } else {
            return "";
        }
    }

    /**
     * M月d日 HH:ss格式
     *
     * @param date
     * @return
     */
    public static String convertsDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("M月d日 HH:mm");
        if (date != null) {
            return df.format(date);
        } else {
            return "";
        }
    }

    /**
     * 把时间类型转化为规定格式的字符串
     * "yyyy-MM-dd"
     *
     * @param date 时间
     * @return 格式化以后的时间字符串
     */
    public static String convertDateToYMD(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        return f.format(date);
    }

    /**
     * 把时间类型转化为规定格式的字符串
     * "yyyy-MM"
     *
     * @param date 时间
     * @return 格式化以后的时间字符串
     */
    public static String convertDateToYM(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM");
        return f.format(date);
    }

    public static String convertDateToStr(Date date, String formatStr) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat f = new SimpleDateFormat(formatStr);
        return f.format(date);
    }

    public static Date convertStrToDate(String dateStr, String formatStr) {
        SimpleDateFormat f = new SimpleDateFormat(formatStr);
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        try {
            return f.parse(dateStr);
        } catch (ParseException e) {
            logger.error("转化为时间失败", e);
        }
        return null;
    }

    public static String convertDateToYM1(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat f = new SimpleDateFormat("yyyy_MM");
        return f.format(date);
    }


    public static String convertDateToHHmm(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat f = new SimpleDateFormat("HH:mm");
        return f.format(date);
    }

    /**
     * 把时间类型转化为规定格式的字符串
     * "MM-dd hh:MM"
     *
     * @param date 时间
     * @return 格式化以后的时间字符串
     */
    public static String convertDateToMD(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat f = new SimpleDateFormat("MM-dd");
        return f.format(date);
    }

    public static Date convertStringToDate(String dateStr) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        try {
            return f.parse(dateStr);
        } catch (ParseException e) {
            logger.error("转化为时间失败", e);
        }
        return null;
    }

    public static Date convertStringToDate1(String dateStr) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        try {
            return f.parse(dateStr);
        } catch (ParseException e) {
            logger.error("转化为时间失败", e);
        }
        return null;
    }

    public static Date convertStringToHMS(String dateStr) {
        SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss");
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        try {
            return f.parse(dateStr);
        } catch (ParseException e) {
            logger.error("转化为时间失败", e);
        }
        return null;
    }

    public static Date convertStringToHM(String dateStr) {
        SimpleDateFormat f = new SimpleDateFormat("HH:mm");
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        try {
            return f.parse(dateStr);
        } catch (ParseException e) {
            logger.error("转化为时间失败", e);
        }
        return null;
    }


    public static Date convertStringToTodayYMDHMS(String dateStr) {
        SimpleDateFormat f = new SimpleDateFormat("HH:mm");
        Date temp = null;
        try {
            temp = f.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar tempC = Calendar.getInstance();
        tempC.setTime(temp);
        Date now = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        c.set(Calendar.HOUR_OF_DAY, tempC.get(Calendar.HOUR_OF_DAY));
        c.set(Calendar.MINUTE, tempC.get(Calendar.MINUTE));
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    /**
     * 加几年
     * @param d 当前时间
     * @param diff 间隔年
     * @return
     */
    public static Date addYear (Date d , int diff) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(c.YEAR, diff);
        return c.getTime();
    }

    public static Date addMonth(Date d, int diff) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(c.MONTH, diff);
        return c.getTime();
    }

    public static Date addDate(Date d, int diff) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(c.DATE, diff);
        return c.getTime();
    }

    public static String getMonthLastDay(Date d) {

        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(c.MONTH, 1);
        c.add(c.DATE, -1);
        return convertDateToYMD(c.getTime());
    }

    public static Date convertStringToDateYMD(String dateStr) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        try {
            return f.parse(dateStr);
        } catch (ParseException e) {
            logger.error("转化为时间失败", e);
        }
        return null;
    }

    /**
     * String -> date
     *
     * @param dateStr
     * @param format  <br/>
     *                1. yyyy-MM-dd<br/>
     *                2. yyyy-MM-dd HH:mm
     *                ...
     * @return date
     */
    public static Date convertStringToDate(String dateStr, String format) {
        SimpleDateFormat f = new SimpleDateFormat(format);
        if (StringUtils.isEmpty(dateStr)) {
            return null;
        }

        try {
            return f.parse(dateStr);
        } catch (ParseException e) {
            logger.error("转化为时间失败,source:{},format:{}", e);
        }

        return null;
    }

    public static Date convertStringToDateYM(String dateStr) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM");
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        try {
            return f.parse(dateStr);
        } catch (ParseException e) {
            logger.error("转化为时间失败", e);
        }
        return null;
    }


    /**
     * @param dateStr
     * @return
     */
    public static Date convertStringToDateYMDHMS(String dateStr) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss");
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        try {
            return f.parse(dateStr);
        } catch (ParseException e) {
            logger.error("转化为时间失败", e);
        }
        return null;
    }

    public static Date minusDays(Date date, Integer days) {
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, - days);
        return calendar.getTime();
    }


    public static Date getDayStartAndEnd(Date date, int flag) {
        DateTime dateTime = new DateTime(date);
        Date resultDate=new Date();
        String dateString="";
        if(flag==1){
            dateString=dateTime.toString(DateTimeFormat.forPattern("yyyy-MM-dd 23:59:59"));
        }
        if(flag==0){
            dateString= dateTime.toString(DateTimeFormat.forPattern("yyyy-MM-dd 00:00:00"));
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            resultDate=sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resultDate;
    }

    /**
     * 获取Calendar的日期
     * "yyyy-MM-dd"
     *
     * @param cal 时间
     * @return 格式化以后的时间字符串
     */
    public static String getDate(Calendar cal) {
        String v_strDate = "";
        int v_intYear = cal.get(Calendar.YEAR);
        int v_intMonth = cal.get(Calendar.MONTH) + 1;
        int v_intDAY = cal.get(Calendar.DAY_OF_MONTH);

        v_strDate = v_strDate + v_intYear + "-";

        if (v_intMonth < 10) {
            v_strDate = v_strDate + "0" + v_intMonth + "-";
        } else {
            v_strDate = v_strDate + v_intMonth + "-";
        }
        if (v_intDAY < 10) {
            v_strDate = v_strDate + "0" + v_intDAY + "";
        } else {
            v_strDate = v_strDate + v_intDAY + "";
        }

        return v_strDate;
    }

    /**
     * 获取选定日期的目标间隔日期
     *
     * @param date   选定日期
     * @param number 间隔日期
     */
    public static String getDateStr(Date date, int number) {
        if (null == date) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, number);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(calendar.getTime());
    }

    /**
     * 获取选定日期的目标间隔日期
     *
     * @param date   选定日期
     * @param number 间隔日期
     */
    public static Date getDateBy(Date date, int number) {
        if (null == date) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, number);
        return calendar.getTime();
    }

    /**
     * 获取指定日期是星期几
     * 参数为null时表示获取当前日期是星期几
     *
     * @param date
     * @return
     */
    public static String getWeekOfDate(Date date) {
        String[] weekOfDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekOfDays[w];
    }

    /**
     * 将日期本周内转化为星期，本周以外转化为xxxx年xx月xx日
     *
     * @param date
     * @return
     */
    public static String getWeeksOfDate(Date date) {
        if (date != null) {
            String da = DateUtil.getWeekOfDate(new Date());
            try {
                int count = daysBetween(date, new Date());
                if (da.equals("周一")) {
                    if (count == 0) {
                        return DateUtil.getWeekOfDate(date);
                    } else {
                        return DateUtil.convertDate(date);
                    }
                }
                if (da.equals("周二")) {
                    if (count <= 1) {
                        return DateUtil.getWeekOfDate(date);
                    } else {
                        return DateUtil.convertDate(date);
                    }
                }
                if (da.equals("周三")) {
                    if (count <= 2) {
                        return DateUtil.getWeekOfDate(date);
                    } else {
                        return DateUtil.convertDate(date);
                    }
                }
                if (da.equals("周四")) {
                    if (count <= 3) {
                        return DateUtil.getWeekOfDate(date);
                    } else {
                        return DateUtil.convertDate(date);
                    }
                }
                if (da.equals("周五")) {
                    if (count <= 4) {
                        return DateUtil.getWeekOfDate(date);
                    } else {
                        return DateUtil.convertDate(date);
                    }
                }
                if (da.equals("周六")) {
                    if (count <= 5) {
                        return DateUtil.getWeekOfDate(date);
                    } else {
                        return DateUtil.convertDate(date);
                    }
                }
                if (da.equals("周日")) {
                    if (count <= 6) {
                        return DateUtil.getWeekOfDate(date);
                    } else {
                        return DateUtil.convertDate(date);
                    }
                }
            } catch (ParseException p) {
                logger.error("日期解析错误", p);
            }
        }
        return null;

    }


    /**
     * 计算两个日期之间的天数
     *
     * @param smdate
     * @param bdate
     * @return
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 计算两个日期之间的分钟
     *
     * @param smdate
     * @param bdate
     * @return
     * @throws ParseException
     */
    public static int secBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_sec = (time2 - time1) / (1000 * 60);
        return Integer.parseInt(String.valueOf(between_sec));
    }


    /**
     * @param date
     * @param number
     * @return
     */
    public static String getDateStr2(Date date, int number) {
        if (null == date) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, number);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(calendar.getTime());
    }

    /**
     * 指定日期格式化 yyyy-MM
     *
     * @param date
     * @param number
     * @return
     */
    public static String getDateStr3(Date date, int number) {
        if (null == date) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, number);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
        return df.format(calendar.getTime());
    }


    /**
     * 比较两个时间的间隔天数
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    public static int getSpaceByCompareTwoDate(Date startTime, Date endTime) {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.setTime(startTime);
        end.setTime(endTime);
        int betweenYears = end.get(Calendar.YEAR) - start.get(Calendar.YEAR);
        int betweenDays = end.get(Calendar.DAY_OF_YEAR)
                - start.get(Calendar.DAY_OF_YEAR);
        for (int i = 0; i < betweenYears; i++) {
            start.set(Calendar.YEAR, (start.get(Calendar.YEAR) + 1));
            betweenDays += start.getMaximum(Calendar.DAY_OF_YEAR);
        }
        return betweenDays;
    }


    /**
     * 把时间类型转化为规定格式的字符串
     * "yyyy.MM.dd hh:MM:ss"
     *
     * @param date 时间
     * @return 格式化以后的时间字符串
     */
    public static String convertDateYMDHMS(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat f = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        return f.format(date);
    }

    /**
     * 把时间类型转化为规定格式的字符串
     * "HH:mm"
     *
     * @param date
     * @return
     */
    public static String convertDateToTime(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat f = new SimpleDateFormat("HH:mm");
        return f.format(date);
    }

    /**
     * create by jason 2015-07-20
     * 获得明天的开始时间 e.g. 2015-07-10 00 :00:00
     */
    public static Date getTomStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 1);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        Date startTime = todayStart.getTime();
        return startTime;
    }

    /**
     * create by jason 2015-07-20
     * 获得当天的开始时间 e.g. 2015-07-10 00 :00:00
     */
    public static Date getStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        Date startTime = todayStart.getTime();
        return startTime;
    }

    /**
     * create by jason 2015-07-20
     * 获得当天的开始时间 e.g. 2015-07-10 00 :00:00
     */
    public static Date getStartTime(Date startTime) {
        if (startTime == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        startTime = calendar.getTime();
        return startTime;
    }

    /**
     * create by jason 2015-07-20
     * 获得昨天的开始时间 e.g. 2015-07-10 00 :00:00
     */
    public static Date getYstdStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.DATE, todayStart.get(Calendar.DATE) - 1);
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        Date startTime = todayStart.getTime();
        return startTime;
    }

    /**
     * create by jason 2015-07-20
     * 获得前天的开始时间 e.g. 2015-07-10 00 :00:00
     */
    public static Date getBYstdStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.DATE, todayStart.get(Calendar.DATE) - 2);
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        Date startTime = todayStart.getTime();
        return startTime;
    }

    /**
     * create by jason 2015-07-20
     * 获得当天的开始时间 e.g. 2015-07-10 23 :59:59
     */
    public static Date getEndTime(Date endTime) {
        if (endTime == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endTime);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        endTime = calendar.getTime();
        return endTime;
    }


    /**
     * create by jason 2015-11-07
     * 获得昨天的结束时间 e.g. 2015-11-07 59:59:59
     */
    public static Date getYstdEndTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.DATE, todayStart.get(Calendar.DATE) - 1);
        todayStart.set(Calendar.HOUR_OF_DAY, 23);
        todayStart.set(Calendar.MINUTE, 59);
        todayStart.set(Calendar.SECOND, 59);
        todayStart.set(Calendar.MILLISECOND, 999);
        Date startTime = todayStart.getTime();
        return startTime;
    }

    /**
     * create by jason 2015-11-07
     * 获得qiant的结束时间 e.g. 2015-11-07 59:59:59
     */
    public static Date getBYstdEndTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.DATE, todayStart.get(Calendar.DATE) - 2);
        todayStart.set(Calendar.HOUR_OF_DAY, 23);
        todayStart.set(Calendar.MINUTE, 59);
        todayStart.set(Calendar.SECOND, 59);
        todayStart.set(Calendar.MILLISECOND, 999);
        Date startTime = todayStart.getTime();
        return startTime;
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static Date currentDate() {
        return new Date();
    }

    /**
     * 获取制定时间的月初时间，默认当月
     *
     * @param dateTime
     * @return
     */
    public static Date getStartMonth(Date dateTime) {
        Calendar calendar = Calendar.getInstance();
        if (dateTime != null) {
            calendar.setTime(dateTime);
        }
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取制定时间的月末时间，默认当月
     *
     * @param dateTime
     * @return
     */
    public static Date getEndMonth(Date dateTime) {
        Calendar calendar = Calendar.getInstance();
        if (dateTime != null) {
            calendar.setTime(dateTime);
        }
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 59);
        return calendar.getTime();
    }

    /**
     * 获取指定月份的第一天
     * 如现在是2015-12-14，
     * day = 1  ：返回2016-01-01
     * day = -1 ：返回2015-11-01
     * day = 0  ：返回2015-12-01
     *
     * @param day
     * @return
     */
    public static String getFirstMonthDay(Integer day) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, day);//设置月份
        calendar.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        String day_first = df.format(calendar.getTime());
        return day_first;
    }

    /**
     * 获取指定月份的最后一天
     * 如现在是2015-12-14，
     * day = 1  ：返回2016-01-31
     * day = -1 ：返回2015-11-30
     * day = 0  ：返回2015-12-31
     *
     * @param day
     * @return
     */
    public static String getLastMonthDay(Integer day) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, day);//设置月份
        calendar.set(Calendar.DAY_OF_MONTH, 1);//设置为1号
        calendar.add(Calendar.MONTH, 1);//月增加1天
        calendar.add(Calendar.DAY_OF_MONTH, -1);//日期倒数一日,既得到此月份的最后一天
        String day_last = df.format(calendar.getTime());
        return day_last;
    }

    /**
     * 获取上一年
     *
     * @param date   当前日期
     * @param number 间隔
     * @return
     */
    public static Date getLastYear(Date date, int number) {
        if (null == date) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, number);
        return calendar.getTime();
    }

    public static String convertDateToHMS(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss");
        return f.format(date);
    }

    public static Date queryDateToYM(String date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM");
        Date time = null;
        try {
            time = f.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }


    /**
     * 任意一个时间，转成10的整数，全部进一位，比如5:31转成5:40
     *
     * @param date
     * @return
     */
    public static Date formatDate(Date date) {
        Calendar time = Calendar.getInstance();
        time.setTime(date);
        int minute = time.get(Calendar.MINUTE);
        time.set(Calendar.MINUTE, (minute / 10 + 1) * 10);
        time.set(Calendar.SECOND, 0);
        Date result = time.getTime();
        return result;
    }


    /**
     * 计算与date相差interval分钟的时间，比如date=12点半，interval=30分钟，那么返回值为13点
     *
     * @param date
     * @param interval
     * @return
     */
    public static Date timePlus(Date date, Integer interval) {
        Calendar afterTime = Calendar.getInstance();
        afterTime.setTime(date);
        afterTime.add(Calendar.MINUTE, interval);
        Date result = afterTime.getTime();
        return result;
    }


    /**
     * 加上差值并且格式化(任意一个时间，转成10的整数，全部进一位，比如5:31转成5:40)
     *
     * @param date
     * @param interval
     * @return
     */
    public static Date timePlusAndFormat(Date date, Integer interval) {
        Calendar afterTime = Calendar.getInstance();
        afterTime.setTime(date);
        afterTime.add(Calendar.MINUTE, interval);
        int minute = afterTime.get(Calendar.MINUTE);
        afterTime.set(Calendar.MINUTE, (minute / 10 + 1) * 10);
        afterTime.set(Calendar.SECOND, 0);
        Date result = afterTime.getTime();
        return result;
    }


    /**
     * 将这样的一个时间（Thu Jan 01 17:18:00 CST 1970）格式化为今天的17:18:00
     *
     * @param openDate
     * @param intervalDay 间隔天数
     * @return
     */
    public static Date formatDateToToday(Date openDate, Integer intervalDay) {
        Calendar open = Calendar.getInstance();
        open.setTime(openDate);
        Calendar today = Calendar.getInstance();
        today.setTime(new Date());
        open.set(Calendar.YEAR, today.get(Calendar.YEAR));
        open.set(Calendar.MONTH, today.get(Calendar.MONTH));
        open.set(Calendar.DAY_OF_MONTH, today.get(Calendar.DAY_OF_MONTH) + intervalDay);
        return open.getTime();
    }

    /**
     * 获取两个时间点的时间差，单位分
     *
     * @param start
     * @param end
     * @return
     */
    public static Integer calculateTimeInterval(Date start, Date end) {
        Calendar startC = Calendar.getInstance();
        startC.setTime(start);
        Calendar endC = Calendar.getInstance();
        endC.setTime(end);
        long val = endC.getTimeInMillis() - startC.getTimeInMillis();
        long interval = val / 60000;
        return Integer.parseInt(String.valueOf(interval));
    }

    /**
     * 把时间类型转化为规定格式的字符串
     * "yyyy-MM-dd"
     *
     * @param date 时间
     * @return 格式化以后的时间字符串
     */
    public static String convertDateToyMd(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
        return f.format(date);
    }

    /**
     * 根据日期获取str类型查询开始时间
     * yyyy-MM-dd -> yyyy-MM-dd 00:00:00
     */
    public static String convertToBeginStr(String time) {
        if (StringUtils.isEmpty(time)) {
            return null;
        }
        boolean flag = true;
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        f.setLenient(false);
        try {
            f.parse(time);
        } catch (ParseException e) {
            flag = false;
        }
        StringBuilder sb = new StringBuilder(time);
        if (flag) {
            sb.append(" 00:00:00");
        } else {
            throw new BizException("日期格式错误");
        }
        return sb.toString();
    }

    /**
     * 根据日期获取str类型查询结束时间
     * yyyy-MM-dd -> yyyy-MM-dd 23:59:59
     */
    public static String convertToEndStr(String time) {
        if (StringUtils.isEmpty(time)) {
            return null;
        }
        boolean flag = true;
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        f.setLenient(false);
        try {
            f.parse(time);
        } catch (ParseException e) {
            flag = false;
        }
        StringBuilder sb = new StringBuilder(time);
        if (flag) {
            sb.append(" 23:59:59");
        } else {
            throw new BizException("日期格式错误");
        }
        return sb.toString();
    }

    /**
     * 时间差毫秒
     *
     * @return
     */
    public static Long getSubMilliSecond(String dateStr, Date now) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        SimpleDateFormat fd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = fd.parse(dateStr);
            return (date.getTime() - now.getTime());
        } catch (ParseException e) {
            logger.error("计算时间错误", e);
        }
        return null;
    }

    /**
     * 加一天
     *
     * @param currentDate
     * @return
     */
    public static String addOneDay(Date currentDate) {
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        gc.setTime(currentDate);
        gc.add(Calendar.DATE, 1);
        return sf.format(gc.getTime());
    }

    public static Map<String,Date> insuranceDate(Date date) {
        Map<String, Date> map = new HashMap<>();
        DateTime now = DateTime.now().plusDays(1).withTime(0, 0, 0, 0);
        DateTime defaultTime = DateTime.parse("1970-01-01").withTime(12, 0, 0, 0),dateTime,startTime, endTime;
        if (date == null || date.getTime() == defaultTime.toDate().getTime()){
            dateTime=now;
        }else{
            dateTime = new DateTime(date);
        }
        if (now.getMillis() < dateTime.getMillis()) {
            startTime = dateTime;
        } else {
            startTime = now;
        }
        endTime = startTime.plusYears(1).minusSeconds(1);
        map.put("startTime", startTime.toDate());
        map.put("endTime", endTime.toDate());
        return map;
    }
}
