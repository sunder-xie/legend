package com.tqmall.legend.facade.account.impl;

import com.google.common.collect.Sets;
import com.tqmall.common.util.NumUtil;
import com.tqmall.core.utils.JedisPoolUtils;
import com.tqmall.legend.biz.account.AccountCouponService;
import com.tqmall.common.exception.BizException;
import com.tqmall.legend.facade.account.CouponFacadeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

/**
 * Created by wanghui on 6/12/16.
 */
@Component
@Slf4j
public class CouponFacadeServiceImpl implements CouponFacadeService {

    private static String COUPON_REDIS_KEY_PREFIX = "coupon_sn_legend_";
    private static int BATCH_SIZE = 1000;
    private static char[] SN_CHAR = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private Set<String> cachedSnSet;

    public CouponFacadeServiceImpl(){
        this.cachedSnSet = Sets.newHashSet();
    }
    @Autowired
    private AccountCouponService accountCouponService;

    @Override
    public String genCouponSN() {
        String couponSn = this._getCouponSn();
        if (couponSn == null) {
            throw new BizException("生成券码异常");
        }
        this.cachedSnSet.add(couponSn);
        return couponSn;
    }

    @Override
    public boolean removeCachedSn(String sn) {
        return this.cachedSnSet.remove(sn);
    }


    private String _getCouponSn() {
        String redisKey = this._getRedisKey();
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtils.getMasterJedis();
            Long count = jedis.scard(redisKey);
            if (count == 0) {
                this.batchGenToRedis();
            }

            return jedis.spop(redisKey);
        } catch (Exception e) {
            log.error("操作redis异常.", e);
        } finally {
            if (jedis != null) {
                JedisPoolUtils.returnMasterRes(jedis);
            }
        }
        return null;
    }

    public synchronized void batchGenToRedis() {
        String redisKey = this._getRedisKey();
        String snPrefix = this.getPrefix();
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtils.getMasterJedis();
            Set<String> snSet = Sets.newHashSet();
            int i = 0;
            for(;i<BATCH_SIZE;){
                String sn = snPrefix + RandomStringUtils.random(4, SN_CHAR);
                if(!jedis.sismember(redisKey, sn) && !this.cachedSnSet.contains(sn)) {
                    snSet.add(sn);
                    i++;
                }
            }

            List<String> snList = accountCouponService.selectExistsSn(snSet.toArray(new String[snSet.size()]));
            snSet.removeAll(snList);

            jedis.sadd(redisKey, snSet.toArray(new String[snList.size()]));
            jedis.expire(redisKey, 60*60*24*31);
        } catch (Exception e) {
            log.error("操作redis异常.", e);
        } finally {
            if (jedis != null) {
                JedisPoolUtils.returnMasterRes(jedis);
            }
        }
    }

    private String _getRedisKey() {
        return COUPON_REDIS_KEY_PREFIX + getPrefix();
    }

    /**
     * 获取券码前缀
     * 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26
     * A B C D E F G H I J  K  L  M  N  O  P  Q  R  S  T  U  V  W  X  Y  Z
     * 年+月
     * 2016-07-07:QG
     * 2016-10-01:Q
     *
     * @return
     */
    private String getPrefix() {
        Calendar calendar = Calendar.getInstance();
        return String.valueOf(NumUtil.toUpperAlphalbetChar(calendar.get(Calendar.YEAR) % 2000)) + Integer.toHexString(calendar.get(Calendar.MONTH) + 1);
    }

}
