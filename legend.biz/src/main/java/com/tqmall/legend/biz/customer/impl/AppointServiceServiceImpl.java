package com.tqmall.legend.biz.customer.impl;

import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.customer.AppointServiceService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tqmall.legend.dao.customer.AppointServiceDao;
import com.tqmall.legend.entity.customer.AppointServiceVo;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;


/**
 * Created by jason on 15/7/19.
 */
@Service
public class AppointServiceServiceImpl extends BaseServiceImpl implements AppointServiceService {

    public static final Logger LOGGER = LoggerFactory.getLogger(AppointServiceServiceImpl.class);

    @Autowired
    private AppointServiceDao appointServiceDao;

    public List<AppointServiceVo> getAllAppointService() {
        return super.getAll(appointServiceDao);
    }

    public Page<AppointServiceVo> getPageAppointService(Pageable pageable, Map<String, Object> searchParams) {
        return super.getPage(appointServiceDao, pageable, searchParams);
    }

    public AppointServiceVo getAppointServiceById(Long id) {
        return super.getById(appointServiceDao, id);
    }

    public boolean saveAppointService(AppointServiceVo appointService) {
        return super.save(appointServiceDao, appointService);
    }

    public boolean deleteAppointServiceById(Long id) {
        return super.deleteById(appointServiceDao, id);
    }

    public int deleteAppointServiceByIds(Long[] ids) {
        return super.deleteByIds(appointServiceDao, ids);
    }

    @Override
    public AppointServiceVo selectById(Long id) {
        return appointServiceDao.selectById(id);
    }

    @Override
    public List<AppointServiceVo> selectByIds(Long[] ids) {
        return appointServiceDao.selectByIds(ids);
    }

    @Override
    public List<AppointServiceVo> select(Map<String, Object> searchParams) {
        return appointServiceDao.select(searchParams);
    }

    @Override
    public List<AppointServiceVo> getAppointServiceList(@NotNull Long appointId, Long shopId) {
        Map<String, Object> paramMap = new HashMap<String, Object>(2);
        paramMap.put("appointId", appointId);
        paramMap.put("shopId", shopId);
        List<AppointServiceVo> appointServiceVoList = null;
        try {
            appointServiceVoList = appointServiceDao.select(paramMap);
        } catch (Exception e) {
            LOGGER.error("[DB]query table legend_appoint_service failure,appointId:{} shopId:{} exception:{}", appointId, shopId, e);
            appointServiceVoList = new ArrayList<AppointServiceVo>();
        }
        if (CollectionUtils.isEmpty(appointServiceVoList)) {
            appointServiceVoList = new ArrayList<AppointServiceVo>();
        }

        return appointServiceVoList;
    }

    @Override
    public Integer batchInsert(List<AppointServiceVo> appointServicesList) {
        if(!CollectionUtils.isEmpty(appointServicesList)){
           return appointServiceDao.batchInsert(appointServicesList);
        }
        return null;
    }

    @Override
    public List<AppointServiceVo> selectByServiceIds(int suitNum, Long... serviceIds) {
        if (serviceIds == null || serviceIds.length < 1
                || suitNum < 0 || suitNum > 2) {
            return new ArrayList<>();
        }
        Map<String, Object> param = new HashMap<>();
        if (suitNum == 2) {
            param.put("parentServiceIds", serviceIds);
        } else {
            param.put("serviceIds", serviceIds);
        }
        List<AppointServiceVo> appointServiceVoList = appointServiceDao.select(param);
        return appointServiceVoList;
    }
}

