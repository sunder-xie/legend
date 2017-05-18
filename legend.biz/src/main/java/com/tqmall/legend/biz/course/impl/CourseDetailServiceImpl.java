package com.tqmall.legend.biz.course.impl;

import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.course.CourseDetailService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.dao.course.CourseDetailDao;
import com.tqmall.legend.entity.course.CourseDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 15-5-7.
 */
@Service
public class CourseDetailServiceImpl extends BaseServiceImpl implements CourseDetailService {

    @Autowired
    private CourseDetailDao courseDetailDao;

    @Override
    public List<CourseDetail> select(Map<String,Object> searchParams) {
        return courseDetailDao.select(searchParams);
    }

    @Override
    public CourseDetail selectById(Long id) {
        return courseDetailDao.selectById(id);
    }

    @Override
    @Transactional
    public Result updateById(CourseDetail courseDetail) {
        courseDetailDao.updateById(courseDetail);
        return Result.wrapSuccessfulResult(courseDetail.getId());
    }
}
