package com.tqmall.legend.biz.marketing.ng;


import com.tqmall.legend.entity.marketing.ng.SmsTemplate;

import java.util.List;

/**
 * Created by wjc on 3/2/2016.
 */
public interface SmsTemplateService {
    List<SmsTemplate> getTemplateList(Long shopId);

    /**
     * @param shopId
     * @param type 模板类型
     * @return
     */
    SmsTemplate getTemplate(Long shopId, Integer type);

    void insert(List<SmsTemplate> templateList, Long shopId);
}
