package com.tqmall.legend.biz.jms.yunxiu;

import com.google.gson.Gson;
import com.tqmall.legend.biz.jms.core.DefaultEventController;
import com.tqmall.legend.biz.jms.core.EventControlConfig;
import com.tqmall.legend.biz.jms.core.SendRefuseException;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * Created by jason on 15/11/11.
 * 门店活动,公告推送到车主app
 */
public class MessagePlatformCzApp {

    Logger logger = LoggerFactory.getLogger(MessagePlatformCApp.class);
    @Getter
    @Setter
    private String host;
    @Getter
    @Setter
    private String onOff;
    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private String password;
    @Getter
    @Setter
    private String defaultExchange;
    @Getter
    @Setter
    private String routingKey;

    private DefaultEventController eventController;

    public void init() throws IOException {
        EventControlConfig config = new EventControlConfig(host, 5672, username, password, "tqmall", 3000, 0, 0, null);
        eventController = DefaultEventController.getInstance(config);
    }

    private boolean sendMessage(String routingKey, String exchange, Map<String, Object> msgInfo) {
        String msg = null;
        try {
            msg = new Gson().toJson(msgInfo);
            if (null == eventController) {
                return false;
            }
            eventController.getEopEventTemplate().send(exchange, routingKey, msg);
        } catch (SendRefuseException e) {
            logger.error("send message error.info=" + msg, e);
            return false;
        }
        return true;
    }

    public void pushMsgToCzApp(Map<String, Object> message) {
        sendMessage(routingKey, defaultExchange, message);
        logger.info("push门店活动,公告信息到车主app端成功! 推送信息是:{}", message);

    }
}
