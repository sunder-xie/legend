package com.tqmall.legend.web.utils.Redis;

import com.tqmall.core.utils.JedisPoolUtils;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

/**
 * Created by twg on 15/11/5.
 */
@Slf4j
public class RedisLock {
    //默认超时时间
    private long DEFAULT_TIME_OUT = 3000;
    //锁的超时时间，过期删除
    private int EXPIRE = 1000;
    private String DEFAULT_KEY = "DEFAULT_REDIS_KEY";
    private Jedis jedis = JedisPoolUtils.getMasterJedis();
    private boolean locked = false;

    public RedisLock(){
    }

    public RedisLock(String key,long time_out,int expire){
        DEFAULT_KEY = key;
        DEFAULT_TIME_OUT = time_out;
        EXPIRE = expire;
    }

    public boolean lock(long timeout,String key) throws InterruptedException {
        timeout*= DEFAULT_TIME_OUT;
        if(key.trim() != null){
            DEFAULT_KEY = key;
        }
        while (timeout>0){
            if(jedis.setnx(DEFAULT_KEY,String.valueOf(EXPIRE))==1){
                jedis.expire(DEFAULT_KEY,EXPIRE);
                locked = true;
                return locked;
            }
            String currentValue = jedis.get(DEFAULT_KEY); //redis里的时间
            if (currentValue != null && Long.parseLong(currentValue) < System.currentTimeMillis()) {
                //判断是否为空，不为空的情况下，如果被其他线程设置了值，则第二个条件判断是过不去的
                String oldValue = jedis.getSet(key, String.valueOf(EXPIRE));
                //获取上一个锁到期时间，并设置现在的锁到期时间，
                //只有一个线程才能获取上一个线上的设置时间，因为jedis.getSet是同步的
                if (oldValue != null && oldValue.equals(currentValue)) {
                    //如果这个时候，多个线程恰好都到了这里，但是只有一个线程的设置值和当前值相同，他才有权利获取锁
                    locked = true;
                    return true;
                }
            }
            timeout -= 100;
            Thread.sleep(100);
        }
        return locked;
    }

    public boolean lock() throws InterruptedException {
        return lock(1l,DEFAULT_KEY);
    }

    public void release(){
        try {
            /*if(locked){
                jedis.del(DEFAULT_KEY);
            }*/
        }catch (Exception e){
            log.error("jedis删除键值异常,信息：{}",e.getMessage());
        }finally {
            JedisPoolUtils.returnMasterRes(jedis);
        }
    }

}
