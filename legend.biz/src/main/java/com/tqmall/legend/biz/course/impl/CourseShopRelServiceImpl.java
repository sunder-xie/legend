package com.tqmall.legend.biz.course.impl;

import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.course.CourseShopRelService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.dao.course.CourseShopRelDao;
import com.tqmall.legend.entity.course.CourseShopRel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 15-5-7.
 */
@Service
public class CourseShopRelServiceImpl extends BaseServiceImpl implements CourseShopRelService {

    @Autowired
    private CourseShopRelDao courseShopRelDao;

    @Override
    public CourseShopRel selectById(Long id) {
        return courseShopRelDao.selectById(id);
    }

    @Override
    public List<CourseShopRel> select(Map<String, Object> searchParams) {
        return courseShopRelDao.select(searchParams);
    }

    @Override
    @Transactional
    public Result insert(CourseShopRel courseShopRel) {
        courseShopRelDao.insert(courseShopRel);
        return Result.wrapSuccessfulResult(courseShopRel.getId());
    }

    @Override
    @Transactional
    public Result update(CourseShopRel courseShopRel) {
        courseShopRelDao.updateById(courseShopRel);
        return Result.wrapSuccessfulResult(courseShopRel.getId());
    }
}
