package com.tqmall.legend.dao.attendance;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.attendance.AppAttendance;

import java.util.List;
import java.util.Map;

/**
 * Created by dingbao on 15/9/21.
 */
@MyBatisRepository
public interface AppAttendanceDao extends BaseDao<AppAttendance> {

    /**
     * 按照月查询员工的打卡记录
     * @param parameters
     * @return
     */
    public List<AppAttendance> selectByMonth(Map<String, Object> parameters);


}
