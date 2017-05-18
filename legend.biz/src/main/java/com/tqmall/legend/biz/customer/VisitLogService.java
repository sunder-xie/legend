package com.tqmall.legend.biz.customer;

import java.util.Date;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.customer.VisitLog;
import com.tqmall.legend.entity.precheck.PrecheckDetailsVO;

/**
 * @Author 王佳超<jiachao.wang@tqmall.com>
 * @Create 2015年7月8日下午8:40:58
 */
public interface VisitLogService {
	public Result insert(VisitLog visitLog);
	public Page<VisitLog> getList(Pageable pageable, Map<String, Object> params);
	public Result changeRemindTime(int customerCarId, byte visitType, Date nextVisitTime);
}
