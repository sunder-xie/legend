package com.tqmall.legend.entity.marketing;

import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Data
public class MarketingSms extends BaseEntity {
    public static final int SEND_SUCCESS = 1;
    public static final int SEND_FAIL = 2;

    private Long shopId;
    private String mobiles;
    private String licenses;
    private String customerName;
    private Long receiverNum;
    private String operator;
    private Long smsNum;
    private String content;
    private Integer status;
    private Date sendTime;
    private Long smsLogId;

    private String customerCarIds;
    private String sendTimeStr;
    private String statusName;
    //提醒处理用
    private String noteInfoIds;// 提醒id

    public String getSendTimeStr(){
        return DateUtil.convertDate1(sendTime);
    }

    public String getStatusName(){
        return SmsSendStatusEnum.getMesByCode(status);
    }


}

