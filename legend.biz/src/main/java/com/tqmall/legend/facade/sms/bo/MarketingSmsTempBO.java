package com.tqmall.legend.facade.sms.bo;

import lombok.Data;

/**
 * Created by majian on 16/8/11.
 */
@Data
public class MarketingSmsTempBO {
    private Integer number;//所需短信条数
    private String smsUID;
    private Integer mobileNumber;//客户数

    public MarketingSmsTempBO() {}
    public MarketingSmsTempBO(Integer number, String smsUID, Integer mobileNumber) {
        this.smsUID = smsUID;
        this.number = number;
        this.mobileNumber = mobileNumber;
    }


}
