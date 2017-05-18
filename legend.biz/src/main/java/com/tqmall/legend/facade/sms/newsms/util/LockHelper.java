package com.tqmall.legend.facade.sms.newsms.util;

import com.tqmall.core.utils.JedisPoolUtils;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

/**
 * Created by majian on 16/11/24.
 */
@Slf4j
public class LockHelper {
    public static boolean getLock(String lockKey) {
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtils.getMasterJedis();
            int i = 0;
            for (i = 0; i < 10; i++) {
                boolean isSuccess = getLockOnce(jedis, lockKey);
                if (isSuccess) {
                    break;
                }
                i++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.error("等待获取短信锁出错",e);
                }
            }
            if (i >=10) {
                log.info("等待短信锁超时");
                return false;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            JedisPoolUtils.returnMasterRes(jedis);
        }
        return true;

    }

    private static boolean getLockOnce(Jedis jedis, String lockKey) {
        long lock = jedis.setnx(lockKey, "1");
        if (lock == 1) {
            jedis.expire(lockKey, 200);
            return true;
        }
        return false;
    }

    public static void releaseLock(String lockKey) {
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtils.getMasterJedis();
            jedis.del(lockKey);
        } catch (Exception e) {
            log.error("短信发送-释放短信锁失败", e);
        } finally {
            JedisPoolUtils.returnMasterRes(jedis);
        }
    }
}
