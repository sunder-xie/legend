package com.tqmall.legend.biz.jms.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ErrorHandler;

/**
 * Created by guozhiqiang on 14-7-4.
 */
public class MessageErrorHandler implements ErrorHandler {

    private static final Logger logger = LoggerFactory.getLogger(MessageErrorHandler.class);

    @Override
    public void handleError(Throwable t) {
        logger.error("RabbitMQ happen a error:" + t.getMessage(), t);
    }

}
