package com.tqmall.legend.facade.marketing.gather.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by xin on 2016/12/24.
 */
@Getter
@Setter
public class GatherOperateStatVO {
    // 盘活客户
    private DataStatVO panHuoCustomer;
    // 老客户拉新
    private DataStatVO laXinCustomer;
    // 发送短信
    private DataStatVO sms;
    // 电话回访
    private DataStatVO phone;
    // 微信转发
    private DataStatVO weChat;
    // 领券客户
    private DataStatVO receiveCoupon;

    // 老客户到店消费
    private DataStatVO toStoreOldCustomer;
    // 新客户到店消费
    private DataStatVO toStoreNewCustomer;
    // 未到店客户
    private DataStatVO notToStoreCustomer;
    // 总计消费
    private DataStatVO totalConsume;
    // 老客户消费
    private DataStatVO oldCustomerConsume;
    // 新客户消费
    private DataStatVO newCustomerConsume;
}
