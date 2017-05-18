package com.tqmall.legend.biz.shop;

import com.tqmall.legend.entity.shop.ServiceTemplate;

import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 2015/09/17.
 */
public interface ServiceTemplateService {

    /**
     * 查询
     *
     * @param searchParams
     * @return
     */
    public List<ServiceTemplate> select(Map<String, Object> searchParams);

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public ServiceTemplate selectById(Long id);

    /**
     * 计数查询
     * @param searchMap
     * @return
     */
    Integer selectCount(Map<String, Object> searchMap);
}
