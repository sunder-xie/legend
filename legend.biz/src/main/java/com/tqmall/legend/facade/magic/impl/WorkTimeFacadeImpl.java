package com.tqmall.legend.facade.magic.impl;

import com.tqmall.common.Constants;
import com.tqmall.common.util.DateUtil;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.core.utils.JedisPoolUtils;
import com.tqmall.legend.biz.bo.attendance.SignTime;
import com.tqmall.legend.biz.component.converter.DataSignTimeConverter;
import com.tqmall.legend.biz.shop.ShopConfigureService;
import com.tqmall.legend.entity.shop.ShopConfigureTypeEnum;
import com.tqmall.legend.facade.magic.WorkTimeFacade;
import com.tqmall.legend.facade.magic.vo.WorkTimeVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;


/**
 * 业务场景：初始化门店的营业时间
 * Created by shulin on 16/7/27.
 */
@Service
@Slf4j
public class WorkTimeFacadeImpl implements WorkTimeFacade {
    @Autowired
    private ShopConfigureService shopConfigureService;

    @Override
    public WorkTimeVo initWorkTime(Long shopId) {
        WorkTimeVo workTimeVo = WorkTimeVo.getInstance();
        Jedis jedis;
        jedis = JedisPoolUtils.getMasterJedis();
        String bufferOpenTime = null;
        String bufferCloseTime = null;
        String bufferNoonBreakStartTime = null;
        String bufferNoonBreakEndTime = null;
        if (jedis.exists(Constants.OPEN_TIME_KEY + shopId)) {
            bufferOpenTime = jedis.get(Constants.OPEN_TIME_KEY + shopId);
        }
        if (jedis.exists(Constants.CLOSE_TIME_KEY + shopId)) {
            bufferCloseTime = jedis.get(Constants.CLOSE_TIME_KEY + shopId);
        }
        if (jedis.exists(Constants.NOONBREAK_START_TIME_KEY + shopId)) {
            bufferNoonBreakStartTime = jedis.get(Constants.NOONBREAK_START_TIME_KEY + shopId);
        }
        if (jedis.exists(Constants.NOONBREAK_END_TIME_KEY + shopId)) {
            bufferNoonBreakEndTime = jedis.get(Constants.NOONBREAK_END_TIME_KEY + shopId);
        }
        if (bufferOpenTime == null || bufferCloseTime == null || bufferNoonBreakEndTime == null || bufferNoonBreakStartTime == null) {
            SignTime signTime = shopConfigureService.getShopConfigure(shopId, ShopConfigureTypeEnum.COMMUTETIME.getCode(), new DataSignTimeConverter<SignTime>());
            log.info("[初始化门店营业时间] 调用接口返回设定时间结果={}", JSONUtil.object2Json(signTime));
            String openTime = DateUtil.convertDateToHHmm(signTime.getSignInTime());
            String closeTime = DateUtil.convertDateToHHmm(signTime.getSignOffTime());
            String noonBreakStartTime = DateUtil.convertDateToHHmm(signTime.getNoonBreakStartTime());
            String noonBreakEndTime = DateUtil.convertDateToHHmm(signTime.getNoonBreakEndTime());
            if (openTime != null) {
                workTimeVo.setOpenTime(openTime);
            } else {
                openTime = workTimeVo.getOpenTime();
            }
            if (closeTime != null) {
                workTimeVo.setCloseTime(closeTime);
            } else {
                closeTime = workTimeVo.getCloseTime();
            }
            if (noonBreakStartTime != null) {
                workTimeVo.setNoonBreakStartTime(noonBreakStartTime);
            } else {
                noonBreakStartTime = workTimeVo.getNoonBreakStartTime();
            }
            if (noonBreakEndTime != null) {
                workTimeVo.setNoonBreakEndTime(noonBreakEndTime);
            } else {
                noonBreakEndTime = workTimeVo.getNoonBreakEndTime();
            }

            jedis.setex(Constants.OPEN_TIME_KEY + shopId, 24 * 3600, openTime);
            jedis.setex(Constants.CLOSE_TIME_KEY + shopId, 24 * 3600, closeTime);
            jedis.setex(Constants.NOONBREAK_START_TIME_KEY + shopId, 24 * 3600, noonBreakStartTime);
            jedis.setex(Constants.NOONBREAK_END_TIME_KEY + shopId, 24 * 3600, noonBreakEndTime);
            log.info("[初始化门店营业时间] 实例化workTimeVo={}", workTimeVo.toString());
            return workTimeVo;
        } else {
            log.info("[初始化门店营业时间] 从Redis中取到得 bufferOpenTime={} ,bufferCloseTime={},bufferNoonBreakStartTime={},bufferNoonBreakEndTime={}", bufferOpenTime, bufferCloseTime,bufferNoonBreakStartTime,bufferNoonBreakEndTime);
            workTimeVo.setOpenTime(bufferOpenTime);
            workTimeVo.setCloseTime(bufferCloseTime);
            workTimeVo.setNoonBreakStartTime(bufferNoonBreakStartTime);
            workTimeVo.setNoonBreakEndTime(bufferNoonBreakEndTime);
            return workTimeVo;
        }
    }
}
