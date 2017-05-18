package com.tqmall.legend.biz.marketing.activity.impl;

import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.marketing.activity.CzActCateRelService;
import com.tqmall.legend.biz.marketing.activity.CzActivityService;
import com.tqmall.legend.dao.marketing.activity.CzActCateRelDao;
import com.tqmall.legend.entity.marketing.activity.CzActCateRel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by jason on 15/11/10.
 */
@Service
public class CzActCateRelServiceImpl extends BaseServiceImpl implements CzActCateRelService {

    @Autowired
    private CzActCateRelDao czActCateRelDao;

    public List<CzActCateRel> getAll() {
        return super.getAll(czActCateRelDao);
    }

    public Page<CzActCateRel> getPage(Pageable pageable, Map<String, Object> searchParams) {
        return super.getPage(czActCateRelDao, pageable, searchParams);
    }

    public CzActCateRel getById(Long id) {
        return super.getById(czActCateRelDao, id);
    }

    public boolean save(CzActCateRel czActCateRel) {
        return super.save(czActCateRelDao, czActCateRel);
    }

    public boolean deleteById(Long id) {
        return super.deleteById(czActCateRelDao, id);
    }

    public int deleteByIds(Long[] ids) {
        return super.deleteByIds(czActCateRelDao, ids);
    }
}
