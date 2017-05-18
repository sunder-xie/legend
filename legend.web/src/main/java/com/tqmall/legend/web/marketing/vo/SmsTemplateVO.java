package com.tqmall.legend.web.marketing.vo;

import lombok.Data;

import java.util.List;

/**
 * Created by tanghao on 16/8/12.
 */
@Data
public class SmsTemplateVO {
    private List<Long> carIdList;
    private String smsTemplate;
}
