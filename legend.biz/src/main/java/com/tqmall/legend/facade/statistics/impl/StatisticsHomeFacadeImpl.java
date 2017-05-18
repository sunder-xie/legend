package com.tqmall.legend.facade.statistics.impl;

import com.tqmall.common.util.DateUtil;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.common.entity.Result;
import com.tqmall.cube.shop.RpcShopInfoService;
import com.tqmall.dandelion.wechat.client.wechat.data.DataService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.facade.statistics.StatisticsHomeFacade;
import com.tqmall.legend.facade.statistics.vo.StatisticsHomeVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by zsy on 16/8/10.
 */
@Slf4j
@Service
public class StatisticsHomeFacadeImpl implements StatisticsHomeFacade {
    @Autowired
    private RpcShopInfoService rpcShopInfoService;
    @Autowired
    private DataService dataService;
    @Autowired
    private ShopService shopService;

    @Override
    public StatisticsHomeVo getStatisticsHome(Long shopId) {
        StatisticsHomeVo statisticsHomeVo = new StatisticsHomeVo();
        if(shopId != null){
            Shop shop = shopService.selectById(shopId);
            if(shop != null){
                try {
                    Result<Integer> appointCountResult = rpcShopInfoService.getUnConfirmedWeixinAppointCount(shopId);
                    log.info("【dubbo】调用cube获取待处理预约单，shopId：{}，返回值：{}", shopId, LogUtils.objectToString(appointCountResult));
                    if(appointCountResult !=null && appointCountResult.isSuccess()){
                        Integer appointCount = appointCountResult.getData();
                        statisticsHomeVo.setAppointCount(appointCount);
                    }
                } catch (Exception e) {
                    log.error("【dubbo】调用cube获取待处理预约单，shopId：{}，出现异常",shopId,e);
                }
                try {
                    Result<Integer> orderCountResult = rpcShopInfoService.getUnConfirmedOrderCount(shopId);
                    log.info("【dubbo】调用cube获取待结算工单，shopId：{}，返回值：{}", shopId, LogUtils.objectToString(orderCountResult));
                    if(orderCountResult !=null && orderCountResult.isSuccess()){
                        Integer orderCount = orderCountResult.getData();
                        statisticsHomeVo.setOrderCount(orderCount);
                    }
                } catch (Exception e) {
                    log.error("【dubbo】调用cube获取待结算工单，shopId：{}，出现异常",shopId,e);
                }
                String userGlobalId = shop.getUserGlobalId();
                if(StringUtils.isNotBlank(userGlobalId)){
                    try {
                        Result<Long> followerIncreaseNowResult = dataService.queryFollowerIncrease(Long.parseLong(userGlobalId), new Date());
                        log.info("【dubbo】调用dandelion获取微信公众号粉丝数，ucshopId：{}，返回值：{}", userGlobalId, LogUtils.objectToString(followerIncreaseNowResult));
                        if(followerIncreaseNowResult !=null && followerIncreaseNowResult.isSuccess()){
                            Long followerCount = followerIncreaseNowResult.getData();
                            statisticsHomeVo.setFollowerCount(followerCount.intValue());
                        }
                    } catch (Exception e) {
                        log.error("【dubbo】调用dandelion获取微信公众号粉丝数，ucshopId：{}，出现异常", userGlobalId, e);
                    }
                }
            }
        }
        return statisticsHomeVo;
    }
}
