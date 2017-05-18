package com.tqmall.legend.biz.tech;

import com.tqmall.legend.entity.tech.TechProduct;

import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 2015/4/30.
 */
public interface TechProductService {
    /**
     * 根据id查询数据
     *
     * @param id
     * @return
     */
    public TechProduct selectById(Long id);

    /**
     * 条件查询
     *
     * @param searchParams
     * @return
     */
    public List<TechProduct> select(Map<String, Object> searchParams);

    /**
     * 获取首页产品信息
     */
    List<TechProduct> getTechProductForIndex();

    /**
     * 获取技术中心产品信息
     */
    public List<TechProduct> getTechProductForTech();

}
