package com.tqmall.legend.biz.jms.crm;

import com.google.gson.Gson;
import com.tqmall.legend.biz.jms.core.DefaultEventController;
import com.tqmall.legend.biz.jms.core.EventControlConfig;
import com.tqmall.legend.biz.jms.core.SendRefuseException;
import com.tqmall.legend.entity.shop.crm.CrmSynCustomerVo;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * Created by jason on 2015-08-04
 * CRM 消息队列推送
 */
public class MessagePlatformCrm {

    Logger logger = LoggerFactory.getLogger(MessagePlatformCrm.class);
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


    /**
     * create by jason 2015-08-04
     * 推送门店信息到CRM
     */
    private boolean sendMessage(String routingKey, String exchange, CrmSynCustomerVo crmSynCustomerVo) {
        String msg = null;
        try {
            msg = new Gson().toJson(crmSynCustomerVo);
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

    public void pushMsgToCrm(CrmSynCustomerVo crmSynCustomerVo) {
        sendMessage(routingKey, defaultExchange, crmSynCustomerVo);
        logger.info("发送信息到CRM MQ成功!{}", crmSynCustomerVo);

    }


}
