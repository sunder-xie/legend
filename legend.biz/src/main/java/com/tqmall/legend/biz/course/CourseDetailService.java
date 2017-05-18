package com.tqmall.legend.biz.course;

import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.course.CourseDetail;

import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 15-5-7.
 */
public interface CourseDetailService {
    public List<CourseDetail> select(Map<String,Object> searchParams);

    public CourseDetail selectById(Long id);

    public Result updateById(CourseDetail courseDetail);
}
