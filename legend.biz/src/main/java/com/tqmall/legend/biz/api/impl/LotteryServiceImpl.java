package com.tqmall.legend.biz.api.impl;

import com.google.common.base.Optional;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tqmall.core.utils.JedisPoolUtils;
import com.tqmall.legend.biz.api.ILotteryService;
import com.tqmall.legend.biz.api.redis.RedisKey;
import com.tqmall.legend.biz.lottery.IActivityService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.lottery.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

/**
 * Lottery service implement
 * <p/>
 * Created by dongc on 15/10/27.
 */
@Service
public class LotteryServiceImpl implements ILotteryService {

    public static final Logger LOGGER = LoggerFactory.getLogger(LotteryServiceImpl.class);

    @Autowired
    IActivityService activityService;

    @Override
    public Result getCurrentOnlineActivity() {

        Result result = new Result();

        // 当前在线活动RedisKey
        String redisKey = RedisKey.ACTIVITY_ONLINE;

        // 当前活动JSON
        String activityJSON = null;
        Jedis jedisUtil = JedisPoolUtils.getMasterJedis();
        try {
            activityJSON = jedisUtil.get(redisKey);
            // TODO log trac
            // IF activityJSON ==null THEN query DB、reset rediskey
            if (activityJSON == null) {
                // 读取DB
                Optional<List<Activity>> activityOptional = activityService.getCurrentActivity();
                if (activityOptional.isPresent()) {
                    // 活动存在时：设置缓存30s
                    result = Result.wrapSuccessfulResult(activityOptional.get());
                    activityJSON = new Gson().toJson(activityOptional.get());
                    jedisUtil.set(redisKey, activityJSON);
                    // expire :30s
                    jedisUtil.expire(redisKey, 30);
                } else {
                    // 活动不存在时：不设置到缓存
                    result = Result.wrapSuccessfulResult(new ArrayList<>());
                }
            } else {
                // TODO 废除重新转换对象
                // TODO controller设置response.setContentType("application/json; charset=utf-8");
                List<Activity> activityList = new Gson().fromJson(activityJSON, new TypeToken<List<Activity>>() {
                }.getType());
                result = Result.wrapSuccessfulResult(activityList);
            }

        } catch (Exception e) {
            LOGGER.error("从redis获取当前活动 异常，异常信息:{}", e);
            // 读取DB
            Optional<List<Activity>> activityOptional = activityService.getCurrentActivity();
            List<Activity> activityList = null;
            if (activityOptional.isPresent()) {
                activityList = activityOptional.get();
            } else {
                activityList = new ArrayList<>();
            }
            result = Result.wrapSuccessfulResult(activityList);
            LOGGER.info("从DB获取当前活动:{}", activityJSON);
        } finally {
            JedisPoolUtils.returnMasterRes(jedisUtil);
        }

        return result;
    }

    @Override
    public Result getActivity(Long activityId) {
        Result result = new Result();

        // 当前活动redis key
        StringBuffer redisKeySB = new StringBuffer(RedisKey.ACTIVITY);
        redisKeySB.append(activityId);
        String redisKey = redisKeySB.toString();
        // 活动JSON
        String activityJSON = null;
        Jedis jedisUtil = JedisPoolUtils.getMasterJedis();
        try {
            activityJSON = jedisUtil.get(redisKey);
            // TODO log trac
            // IF activityJSON ==null THEN query DB、reset rediskey
            if (activityJSON == null) {
                // 读取DB
                Optional<Activity> activityOptional = activityService.getActivity(activityId);
                if (activityOptional.isPresent()) {
                    // 活动存在时：设置缓存30s
                    result = Result.wrapSuccessfulResult(activityOptional.get());
                    activityJSON = new Gson().toJson(activityOptional.get());
                    jedisUtil.set(redisKey, activityJSON);
                    // expire :30s
                    jedisUtil.expire(redisKey, 30);
                } else {
                    // 活动不存在时：不设置到缓存
                    result = Result.wrapSuccessfulResult(new Object());
                }
            } else {
                // TODO 废除重新转换对象
                // TODO controller设置response.setContentType("application/json; charset=utf-8");
                Activity activity = new Gson().fromJson(activityJSON, Activity.class);
                result = Result.wrapSuccessfulResult(activity);
            }
        } catch (Exception e) {
            LOGGER.error("从redis获取当前活动 异常，异常信息:{}", e);
            // 读取DB
            Optional<Activity> activityOptional = activityService.getActivity(activityId);
            Object activity = null;
            if (activityOptional.isPresent()) {
                activity = activityOptional.get();
            } else {
                activity = new Object();
            }
            result = Result.wrapSuccessfulResult(activity);
            LOGGER.info("从DB获取当前活动:{}", activityJSON);
        } finally {
            JedisPoolUtils.returnMasterRes(jedisUtil);
        }

        return result;
    }
}
