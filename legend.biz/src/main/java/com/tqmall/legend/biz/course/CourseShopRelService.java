package com.tqmall.legend.biz.course;

import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.course.CourseShopRel;

import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 15-5-7.
 */
public interface CourseShopRelService {

    /**
     * 根据id查询数据
     * @param id
     * @return
     */
    public CourseShopRel selectById(Long id);

    /**
     * 条件查询
     * @param searchParams
     * @return
     */
    public List<CourseShopRel> select(Map<String, Object> searchParams);


    /**
     * 添加
     * @param courseShopRel
     * @return
     */
    public Result insert(CourseShopRel courseShopRel);

    /**
     * 更新
     * @param courseShopRel
     * @return
     */
    public Result update(CourseShopRel courseShopRel);
}
