package com.tqmall.legend.biz.course.impl;

import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.course.CourseCatService;
import com.tqmall.legend.dao.course.CourseCatDao;
import com.tqmall.legend.dao.course.CourseDao;
import com.tqmall.legend.entity.course.CourseCat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * Created by zsy on 15-5-7.
 */
@Service
public class CourseCatServiceImpl extends BaseServiceImpl implements CourseCatService {
    @Autowired
    CourseCatDao courseCatDao;

    @Autowired
    CourseDao courseDao;

    @Override
    public List<CourseCat> select(Map<String, Object> searchMap) {
        return super.select(courseCatDao,searchMap);
    }

    @Override
    public CourseCat selectById(Long id) {
        return courseCatDao.selectById(id);
    }

}
