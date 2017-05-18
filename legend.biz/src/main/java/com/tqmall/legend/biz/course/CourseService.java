package com.tqmall.legend.biz.course;

import com.tqmall.legend.entity.course.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 15-5-7.
 */
public interface CourseService {

    public int updateById(Course course);

    /**
     * 按类别获取课程分页
     * @param pageable
     * @param searchParams
     * @return
     */
    public Page<Course> getPageByCat(Pageable pageable, Map<String, Object> searchParams);

    /**
     * 按时间获取课程分页
     * @param pageable
     * @param searchParams
     * @return
     */
    public Page<Course> getPageByDate(Pageable pageable, Map<String, Object> searchParams);

    /**
     * 根据id查询数据
     * @param id
     * @return
     */
    public Course selectById(Long id);

    /**
     * 条件查询
     * @param searchParams
     * @return
     */
    public List<Course> select(Map<String, Object> searchParams);

}
