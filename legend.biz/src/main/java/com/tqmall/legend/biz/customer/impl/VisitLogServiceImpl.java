package com.tqmall.legend.biz.customer.impl;

import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.legend.biz.customer.VisitLogService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.dao.customer.VisitLogDao;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.customer.VisitLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Author 王佳超<jiachao.wang@tqmall.com>
 * @Create 2015年7月8日下午8:42:23
 */
@Service
public class VisitLogServiceImpl extends BaseServiceImpl implements VisitLogService {

    private static Logger logger = LoggerFactory.getLogger(VisitLogServiceImpl.class);
    @Autowired
    VisitLogDao visitLogDao;

    @Autowired
    CustomerCarService customerCarService;

    @Override
    public Result insert(VisitLog visitLog) {
        int result = visitLogDao.insert(visitLog);
        if (result > 0) {
            return changeRemindTime(visitLog.getCustomerCarId(), visitLog.getVisitType(),
                    visitLog.getNextVisitTime());
        }
        return Result.wrapErrorResult("", "添加失败");
    }

    /**
     * 设置提醒时间
     * 
     * @param customerCarId
     * @param visitType
     * @param nextVisitTime
     * @return
     */
    @Override
    public Result changeRemindTime(int customerCarId, byte visitType, Date nextVisitTime) {
        CustomerCar customerCar = new CustomerCar();
        customerCar.setId((long) customerCarId);
        // 判断是1.保险到期||2.年检到期||3.保养到期
        if (nextVisitTime == null) {
            nextVisitTime = new Date();
        }
        if (visitType == 1) {
            customerCar.setRemindInsuranceTime(nextVisitTime);
        } else if (visitType == 2) {
            customerCar.setRemindAuditingTime(nextVisitTime);
        } else if (visitType == 3) {
            customerCar.setRemindKeepupTime(nextVisitTime);
        }
        customerCarService.update(customerCar);
        return Result.wrapSuccessfulResult("设置成功");
    }

    @Override
    public Page<VisitLog> getList(Pageable pageable, Map<String, Object> params) {
        if (pageable.getSort() != null) {
            Iterator<Sort.Order> iterator = pageable.getSort().iterator();
            ArrayList<String> sorts = new ArrayList<String>();
            while (iterator.hasNext()) {
                Sort.Order order = iterator.next();
                sorts.add(order.getProperty() + " " + order.getDirection().name());
            }
            params.put("sorts", sorts);
        }
        PageRequest pageRequest = new PageRequest((pageable.getPageNumber() < 1 ? 1
                : pageable.getPageNumber()) - 1, pageable.getPageSize() < 1 ? 1
                : pageable.getPageSize(), pageable.getSort());
        params.put("offset", pageRequest.getOffset());
        params.put("limit", pageRequest.getPageSize());
        int totalSize = visitLogDao.selectCount(params);
        List<VisitLog> retList = visitLogDao.select(params);
        DefaultPage<VisitLog> page = new DefaultPage<>(retList, pageRequest, totalSize);
        return page;
    }
}
