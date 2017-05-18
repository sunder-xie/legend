package com.tqmall.legend.biz.websocket.base;

import com.tqmall.core.utils.JedisPoolUtils;
import com.tqmall.legend.enums.websocket.ChannelsEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zsy on 16/9/8.
 *
 * 业务场景: 订阅redis通道
 */
@Component
@Slf4j
public class RedisSubInit implements ApplicationContextAware, InitializingBean {

    @Autowired
    private JedisPoolUtils jedisPoolUtils;
    @Autowired
    private RedisListener redisListener;

    private ApplicationContext applicationContext;

    private void init() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Jedis jedis = jedisPoolUtils.getMasterJedis();
                try {
                    String[] channels = ChannelsEnum.getChannelsStr();
                    jedis.subscribe(redisListener, channels);
                } catch (Exception e) {
                    log.error("redis通道订阅失败:{}", e);
                } finally {
                    JedisPoolUtils.returnMasterRes(jedis);
                }
            }
        });
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(thread);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.init();
    }
}