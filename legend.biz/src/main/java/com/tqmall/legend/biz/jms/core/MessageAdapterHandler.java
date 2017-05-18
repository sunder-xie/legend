package com.tqmall.legend.biz.jms.core;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * MessageListenerAdapter的Pojo
 * <p>消息处理适配器，主要功能：</p>
 * <p>1、将不同的消息类型绑定到对应的处理器并本地缓存，如将queue01+exchange01的消息统一交由A处理器来出来</p>
 * <p>2、执行消息的消费分发，调用相应的处理器来消费属于它的消息</p>
 * Created by guozhiqiang on 14-7-4.
 */
public class MessageAdapterHandler {

    private static final Logger logger = LoggerFactory.getLogger(MessageAdapterHandler.class);

    private EventProcessorWrap epw;

    public String getQueueName() {
        return queueName;
    }

    @Getter
    private String queueName;

    @Getter
    private String exchangeName;

    public synchronized void handleMessage(EventMessage eem) {
        logger.debug("Receive an EventMessage: [" + eem + "]");
        // 先要判断接收到的message是否是空的，在某些异常情况下，会产生空值
        if (eem == null) {
            logger.warn(
                    "Receive an null EventMessage, it may product some errors, and processing message is canceled.");
            return;
        }
        // 解码，并交给对应的EventHandle执行
        if (epw == null) {
            logger.warn("Receive an EopEventMessage, but no processor can do it.");
            return;
        }
        try {
            epw.process(eem.getEventData());
        } catch (IOException e) {
            logger.error("Event content can not be Deserialized, check the provided CodecFactory.", e);
        }
    }

    public synchronized void handleMessage(String msg) {
        logger.debug("Receive an EventMessage: [" + msg + "]");
        // 先要判断接收到的message是否是空的，在某些异常情况下，会产生空值
        if (msg == null) {
            logger.warn(
                    "Receive an null EventMessage, it may product some errors, and processing message is canceled.");
            return;
        }
        try {
            if (epw == null) {
                logger.warn("Receive an EopEventMessage, but no processor can do it.");
                return;
            }
            epw.process(msg);
        } catch (IOException e) {
            logger.error("Event content can not be process, check the message format.", e);
        }
    }

    public void handleMessage(byte[] eem) throws UnsupportedEncodingException {
        logger.error("Receive an byte[] message: [" + new String(eem) + "]");
    }

    protected void add(String queueName, String exchangeName, EventProcessor processor,
                       CodecFactory codecFactory) {
        if (StringUtils.isEmpty(queueName) || StringUtils.isEmpty(exchangeName) || processor == null
                || codecFactory == null) {
            throw new RuntimeException(
                    "queueName and exchangeName can not be empty,and processor or codecFactory can not be null. ");
        }
        if (epw == null) {
            this.exchangeName = exchangeName;
            this.queueName = queueName;
            epw = new EventProcessorWrap(codecFactory, processor);
        } else {
            logger.error("The processor of this queue and exchange exists, and the new one can't be add");
        }
    }

    protected static class EventProcessorWrap {

        private CodecFactory codecFactory;

        private EventProcessor eep;

        protected EventProcessorWrap(CodecFactory codecFactory,
                                     EventProcessor eep) {
            this.codecFactory = codecFactory;
            this.eep = eep;
        }

        public void process(byte[] eventData) throws IOException {
            Object obj = codecFactory.deSerialize(eventData);
            eep.process(obj);
        }

        public void process(String eventData) throws IOException {
            eep.process(eventData);
        }
    }
}
