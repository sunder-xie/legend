package com.tqmall.legend.biz.websocket.base;

import com.tqmall.core.utils.JedisPoolUtils;
import com.tqmall.legend.enums.websocket.ChannelsEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

/**
 * Created by zsy on 16/9/8.
 * <p/>
 * 业务场景: redis发布消息
 */
@Slf4j
@Component
public class RedisPublish {

    @Autowired
    private JedisPoolUtils jedisPoolUtils;

    /**
     * 发布消息
     *
     * @param channel
     * @param message
     * @return
     */
    public boolean shareNotice(String channel, String message) {
        if(StringUtils.isBlank(ChannelsEnum.getChannelByName(channel))){
            return Boolean.FALSE;
        }
        Jedis jedis = null;
        try {
            jedis = jedisPoolUtils.getMasterJedis();
            Long code = jedis.publish(channel, message);
            log.info("[{}]redis发布到订阅服务器数量:{}", ChannelsEnum.getChannelByName(channel), code);
            if (code != null && code.compareTo(0L) > 0) {
                return Boolean.TRUE;
            }
        } catch (Exception e) {
            log.error("[{}]redis发布websocket消息失败", ChannelsEnum.getChannelByName(channel), e);
        } finally {
            JedisPoolUtils.returnMasterRes(jedis);
        }
        return Boolean.FALSE;
    }
}
