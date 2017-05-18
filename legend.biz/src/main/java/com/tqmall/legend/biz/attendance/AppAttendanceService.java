package com.tqmall.legend.biz.attendance;

import com.tqmall.legend.biz.bo.attendance.SignTime;
import com.tqmall.legend.entity.attendance.AppAttendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by dingbao on 15/9/22.
 */
public interface AppAttendanceService {

    /**
     * 添加打卡记录
     * @param appAttendance
     */
    public void add(AppAttendance appAttendance);

    /**
     * 添加打卡记录
     * @param appAttendance
     */
    public void update(AppAttendance appAttendance);

    /**
     * 查询打卡记录
     * @param params
     * @return
     */
    public List<AppAttendance> select(Map<String,Object> params);

    /**
     * 分页搜索问题
     *
     * @param params
     * @param pageable
     * @return
     */
    public Page<AppAttendance> getPage(Map<String, Object> params, Pageable pageable);

    public void deleteById(Long id);

    /**
     * 按照月查询员工的打卡记录
     * @param parameters
     * @return
     */
    public List<AppAttendance> selectByMonth(Map<String, Object> parameters);

    /**
     *查询上下班打卡时间
     * @param shopId
     * @return
     */
    SignTime getSigTime(Long shopId);
}
