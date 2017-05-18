package com.tqmall.legend.biz.message;

import com.tqmall.legend.entity.order.OrderInfo;

/**
 * Created by zsy on 16/6/24.
 */
public interface MQPushMessageService {
    /**
     * mq发送消息
     *
     * @param type
     * 2C-APP 推送约定的type 属性值
     * APPOINT_CREATE = "1";// 预约单创建待确认
     * ORDER_CREATE = "2";// 工单创建
     * ORDER_FINISH = "3";// 工单完成
     * ORDER_SETTLE = "4";// 工单结算
     * ORDER_INVALID = "5";// 工单无效
     * APPOINT_CONFIRM = "6";// 预约单确认
     * APPOINT_C_CANCEL = "7";// C端预约单取消
     * APPOINT_B_CANCEL = "8";// B端预约单取消
     * APPOINT_WECHAT_CANCEL = "9";// 微信端预约单取消
     * @param orderInfo 工单消息
     * @return
     */
    boolean pushMsgToApp(String type, OrderInfo orderInfo);


    /**
     * mq发送消息
     *
     * @return
     */
    boolean pushMsgToDdlwechat(OrderInfo orderInfo);
}
