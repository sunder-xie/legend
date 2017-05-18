package com.tqmall.legend.biz.jms.core;



/**
 * Created by guozhiqiang on 14-7-4.
 */
public interface EventController {

    /**
     * 控制器启动方法
     */
    void start();

    /**
     * 获取发送模版
     */
    EventTemplate getEopEventTemplate();

    /**
     * 绑定消费程序到对应的exchange和queue
     */
    EventController add(String queueName, String exchangeName, EventProcessor eventProcessor);

}
