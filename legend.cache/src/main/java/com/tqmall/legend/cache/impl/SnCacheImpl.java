package com.tqmall.legend.cache.impl;

import com.tqmall.legend.cache.JedisClient;
import com.tqmall.legend.cache.SnCache;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Random;

/**
 * Created by lixiao on 16/6/1.
 */

@Slf4j
@Service
public class SnCacheImpl implements SnCache {

    @Resource(name = "jedisClient")
    private JedisClient jedisClient;

    @Override
    public String getNextOrderSnIncrement(String key , Long shopId, String snPrefix) {
        DateTime now = DateTime.now();
        String filed = snPrefix +  String.format("%05d", shopId) + now.toString("yyMMdd")  ;
        try {
            Long increment = jedisClient.incrHash(key, filed, 1);
            return filed  + String.format("%04d", increment % 10000);
        }catch (Exception e){
            log.error("通过 redis 生成 sn 异常，异常信息{}",e);
            return filed + now.toString("HHmmss");
        }

    }






}
