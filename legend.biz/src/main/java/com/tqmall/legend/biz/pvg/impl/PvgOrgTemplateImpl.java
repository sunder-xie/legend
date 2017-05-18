package com.tqmall.legend.biz.pvg.impl;

import com.google.common.base.Optional;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.pvg.PvgOrgTemplateService;
import com.tqmall.legend.dao.pvg.PvgOrgTemplateDao;
import com.tqmall.legend.entity.pvg.PvgOrgTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 15-09-21.
 */
@Service
public class PvgOrgTemplateImpl extends BaseServiceImpl implements PvgOrgTemplateService {

    public static final Logger LOGGER = LoggerFactory.getLogger(PvgOrgTemplateImpl.class);

    @Autowired
    private PvgOrgTemplateDao pvgOrgTemplateDao;

    @Override
    public List<PvgOrgTemplate> select(Map<String, Object> searchParams) {
        List<PvgOrgTemplate> pvgOrgTemplateList = pvgOrgTemplateDao.select(searchParams);
        return pvgOrgTemplateList;
    }

    @Override
    public PvgOrgTemplate selectById(Long id) {
        PvgOrgTemplate pvgOrgTemplate = pvgOrgTemplateDao.selectById(id);
        return pvgOrgTemplate;
    }

    @Override
    public Optional<List<PvgOrgTemplate>> getPostList(Long templateType) {

        Map<String, Object> parameters = new HashMap<String, Object>(2);
        parameters.put("type", templateType);
        parameters.put("isDeleted", "N");
        List<PvgOrgTemplate> pvgOrgTemplateList = null;
        try {
            pvgOrgTemplateList = pvgOrgTemplateDao.select(parameters);
        } catch (Exception e) {
            LOGGER.error("查询PvgOrgTemplate岗位数据异常，参数 模板类型:{}", templateType);
            return Optional.absent();
        }

        return Optional.fromNullable(pvgOrgTemplateList);
    }

}
