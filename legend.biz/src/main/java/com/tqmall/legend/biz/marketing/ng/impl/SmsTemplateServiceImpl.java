package com.tqmall.legend.biz.marketing.ng.impl;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tqmall.legend.biz.marketing.ng.SmsTemplateService;
import com.tqmall.legend.dao.marketing.ng.SmsTemplateDao;
import com.tqmall.legend.entity.marketing.ng.SmsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wjc on 2/29/16.
 */
@Service
@Slf4j
public class SmsTemplateServiceImpl implements SmsTemplateService {
    @Autowired
    private SmsTemplateDao smsTemplateDao;

    /**
     * 根据shopId获取短信模板列表
     * @param shopId
     * @return
     */
    @Override
    public List<SmsTemplate> getTemplateList(Long shopId) {
        Map<String,Object> params = new HashMap<>();
        params.put("shopId", shopId);
        List<SmsTemplate> tempList = smsTemplateDao.select(params);
        log.info("获取店铺短信模板,shopId:{},result:{}",shopId,tempList);
        params.put("shopId",-1);
        List<SmsTemplate> defaultList = smsTemplateDao.select(params);
        log.info("获取默认短信模板,result:{}",defaultList);
        /**
         * 获取当前店铺已经设置的模板
         */
        List<Integer> typeList = new ArrayList<>();
        for(SmsTemplate temp : tempList){
            typeList.add(temp.getTemplateType());
        }
        /**
         * 未设置的模板取默认模板
         */
        for(SmsTemplate temp : defaultList){
            if(!typeList.contains(temp.getTemplateType())){
                temp.setId(null);
                tempList.add(temp);
            }
        }
        Collections.sort(tempList);
        log.info("组装完整模板,result:{}",tempList);
        return tempList;
    }

    @Override
    public SmsTemplate getTemplate(Long shopId, Integer type) {
        List<SmsTemplate> templateList = getTemplateList(shopId);
        ImmutableMap<Integer, SmsTemplate> type2contentMap = Maps.uniqueIndex(templateList,
                                 new Function<SmsTemplate, Integer>() {
                                     @Override
                                     public Integer apply(SmsTemplate input) {
                                         return input.getTemplateType();
                                     }
                                 });
        SmsTemplate smsTemplate = type2contentMap.get(type);
        if (smsTemplate == null) {
            smsTemplate = type2contentMap.get(SmsTemplate.DEFAULT_TYPE);
        }
        return smsTemplate;
    }

    /**
     *  短信模板插入
     * @param templateList
     */
    @Override
    public void insert(List<SmsTemplate> templateList,Long shopId) {
        for(SmsTemplate temp : templateList){
            temp.setId(null);
            temp.setShopId(shopId);
        }
        Map<String,Object> params = new HashMap<>();
        params.put("shopId",shopId);
        smsTemplateDao.delete(params);
        smsTemplateDao.batchInsert(templateList);
    }

}
