package com.tqmall.legend.biz.jms.core;

/**
 * Created by guozhiqiang on 14-7-4.
 */
public interface EventTemplate {

    void send(String exchangeName, String routingKey, Object eventContent) throws SendRefuseException;

    void send(String exchangeName, String routingKey, Object eventContent, CodecFactory codecFactory) throws SendRefuseException;

    /**
     * 下发消息
     * @param exchangeName
     * @param routingKey
     * @param eventContent
     * @throws SendRefuseException
     */
    void send(String exchangeName, String routingKey, String eventContent) throws SendRefuseException;

}
