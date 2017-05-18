package com.tqmall.legend.biz.course;

import com.tqmall.legend.entity.course.CourseCat;

import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 15-5-7.
 */
public interface CourseCatService {

    /**
     * 按条件查询数据
     * @param searchMap 查询条件
     * @return
     */
    public List<CourseCat> select(Map<String, Object> searchMap);

    /**
     * 根据id查询
     * @param id
     * @return   文章分类对象
     */
    public CourseCat selectById(Long id);

}
