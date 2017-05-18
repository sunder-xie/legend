package com.tqmall.legend.biz.attendance.impl;

import com.tqmall.legend.biz.attendance.AppAttendanceService;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.bo.attendance.SignTime;
import com.tqmall.legend.biz.component.converter.DataSignTimeConverter;
import com.tqmall.legend.biz.shop.ShopConfigureService;
import com.tqmall.legend.dao.attendance.AppAttendanceDao;
import com.tqmall.legend.entity.attendance.AppAttendance;
import com.tqmall.legend.entity.shop.ShopConfigureTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by dingbao on 15/9/22.
 */
@Slf4j
@Service
public class AppAttendanceServiceImpl extends BaseServiceImpl implements AppAttendanceService {

    @Autowired
    private AppAttendanceDao appAttendanceDao;
    @Autowired
    ShopConfigureService shopConfigureService;

    @Override
    public void add(AppAttendance appAttendance) {
        appAttendanceDao.insert(appAttendance);

    }

    @Override
    public void update(AppAttendance appAttendance) {
        appAttendanceDao.updateById(appAttendance);
    }

    @Override
    public List<AppAttendance> select(Map<String, Object> params) {
        return appAttendanceDao.select(params);
    }

    @Override
    public Page<AppAttendance> getPage(Map<String, Object> params, Pageable pageable) {
        return super.getPage(appAttendanceDao, pageable, params);
    }

    @Override
    public void deleteById(Long id) {
        appAttendanceDao.deleteById(id);
    }

    @Override
    public List<AppAttendance> selectByMonth(Map<String, Object> params) {
        return appAttendanceDao.selectByMonth(params);
    }

    @Override
    public SignTime getSigTime(Long shopId) {
        //add by twg
        return shopConfigureService.getShopConfigure(shopId, ShopConfigureTypeEnum.COMMUTETIME.getCode(),new DataSignTimeConverter<SignTime>());
    }
}
