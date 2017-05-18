package com.tqmall.legend.biz.marketing.impl;


import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.marketing.AppActServiceRelService;
import com.tqmall.legend.dao.marketing.AppActServiceRelDao;
import com.tqmall.legend.entity.marketing.AppActServiceRel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AppActServiceRelServiceImpl extends BaseServiceImpl implements AppActServiceRelService {

    @Autowired
    private AppActServiceRelDao appActServiceRelDao;

    public List<AppActServiceRel> select(Map<String, Object> searchParams) {
        return appActServiceRelDao.select(searchParams);
    }

    public Page<AppActServiceRel> getPage(Pageable pageable, Map<String, Object> searchParams) {
        return super.getPage(appActServiceRelDao, pageable, searchParams);
    }

    public AppActServiceRel getById(Long id) {
        return super.getById(appActServiceRelDao, id);
    }

}

