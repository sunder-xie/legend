package com.tqmall.legend.biz.activity;

import com.tqmall.common.UserInfo;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.pojo.activity.ActivityServiceTemplateVo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by lixiao on 16/2/25.
 */
public interface ActivityJoinService {

    /**
     * 根据渠道、门店获取套餐列表
     *
     * @param channel
     * @param shopId
     * @return
     */
    public List<ActivityServiceTemplateVo> getServiceListByChannelAndShopId(Long channel, Long shopId);

    /**
     * 根据活动模板ID和服务模板ID获取套餐模板信息
     *
     * @param actTplId
     * @param serviceTplId
     * @param shopId
     * @return
     */
    public ActivityServiceTemplateVo getServiceTpl(Long actTplId, Long serviceTplId, Long shopId);

    /**
     * 报名参加引流活动
     * @param actTplId
     * @param serviceTplId
     * @param servicePrice
     * @param shopReason
     * @param userInfo
     * @return
     */
    public Result joinActivity(Long actTplId, Long serviceTplId,BigDecimal servicePrice, String shopReason, UserInfo userInfo);

    /**
     * 退出引流活动
     * @param actTplId
     * @param serviceTplId
     * @param shopReason
     * @param userInfo
     * @return
     */
    public Result exitActivity(Long actTplId, Long serviceTplId, String shopReason, UserInfo userInfo);
}
