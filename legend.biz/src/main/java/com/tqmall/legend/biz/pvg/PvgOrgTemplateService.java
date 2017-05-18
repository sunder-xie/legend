package com.tqmall.legend.biz.pvg;

import com.google.common.base.Optional;
import com.tqmall.legend.entity.pvg.PvgOrgTemplate;

import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 15-09-21.
 */
public interface PvgOrgTemplateService {

    public List<PvgOrgTemplate> select(Map<String, Object> searchParams);

    public PvgOrgTemplate selectById(Long id);

    /**
     * 获取岗位列表
     *
     * @param templateType 模板类型
     * @return
     */
    Optional<List<PvgOrgTemplate>> getPostList(Long templateType);

}
