package com.tqmall.legend.web.timer.shopnote;

import com.google.common.collect.Maps;
import com.tqmall.common.AbstractTimer;
import com.tqmall.common.util.DateUtil;
import com.tqmall.core.utils.JedisPoolUtils;
import com.tqmall.legend.biz.service.shopnote.InsuranceNoteService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.entity.shop.Shop;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 保险提醒
 * <p/>
 * Created by yuchengdu on 16/7/29.
 */
@Component
public class InsuranceTimer extends AbstractTimer {

    @Resource
    private ShopService shopService;

    @Resource
    private InsuranceNoteService insuranceNoteService;


    private static final String DEFAULT_KEY = "SHOP_NOTE_INSURANCE";

    public InsuranceTimer() {
        super(InsuranceTimer.class, "InsuranceJob", 5000, 3);
    }

    @Override
    public void execute() {
        Jedis jedis = JedisPoolUtils.getMasterJedis();
        final long sTime = System.currentTimeMillis();
        try {
            Long result = jedis.setnx(DEFAULT_KEY, String.valueOf(sTime));
            if (result == 1) {//多机部署只有一台设备会在运行Job
                jedis.expire(DEFAULT_KEY, 300);
                final Date currentTime = DateUtil.getStartTime();
                List<Shop> shops = findAllShopList();
                if (CollectionUtils.isEmpty(shops)) {
                    return;
                }
                for (final Shop shop : shops) {
                    //按照店铺为维度放入线程执行任务队列
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            insuranceNoteService.processInsuranceNoteInfo(shop.getId(), currentTime);
                        }
                    });
                }
            }
        } catch (Exception e) {
            logger.error("[提醒中心]保险提醒定时任务异常, 异常信息:", e);
        } finally {
            JedisPoolUtils.returnMasterRes(jedis);
        }
    }

    /**
     * 获取所有门店
     *
     * @return
     */
    private List<Shop> findAllShopList() {
        Map shopParam = Maps.newHashMap();
        return shopService.select(shopParam);
    }

    /**
     * 保存保险信息到提醒表
     *
     * @param shopId
     * @param currentTime
     */
    private void processInsuranceNoteInfo(Long shopId, Date currentTime) {

    }



}
