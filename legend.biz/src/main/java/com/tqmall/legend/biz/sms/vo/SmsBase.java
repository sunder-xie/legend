package com.tqmall.legend.biz.sms.vo;

import com.tqmall.common.Constants;
import lombok.Data;

import java.util.Map;

/**
 * Created by sven on 16-8-18.
 */
@Data
public class SmsBase {
    private String action;  // 短信模板key

    private String mobile;  // 接收短信的手机号，可批量，英文逗号隔开，如数量巨大请咨询iServer

    private Map<String, Object> data;    // Json String，KV格式，用于替换短信模板中的可变量

    private String source;  // 发送源，请注明调用短信服务的系统名称，如『iServer』

    public SmsBase() {

    }

    public SmsBase(String mobile, String action, Map<String, Object> data) {
        this.mobile = mobile;
        this.action = action;
        this.data = data;
        this.source = Constants.CUST_SOURCE;
    }
}
