package com.tqmall.legend.facade.wechat;

import com.tqmall.dandelion.wechat.client.dto.wechat.lottery.LotteryStatisticDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.lottery.LotteryUserPageDTO;
import com.tqmall.legend.facade.wechat.bo.ActivityLotteryBO;
import org.springframework.data.domain.Page;

/**
 * 微信公众号门店抽奖管理
 *
 * @author kang.zhao@tqmall.com
 * @version 1.0 2016/10/18
 */
 public interface WeChatActivityLotteryFacade {

    /**
     * 查询门店发布的抽奖活动
     *
     * @param shopId 门店ID
     *
     * @return 门店设置抽奖活动
     *         门店没有设置抽奖活返回null
     */
    public ActivityLotteryBO getLotteryByShopId(Long shopId);

    /**
     * 保存门店设置的抽奖活动
     *
     * @param activityLotteryBO 抽奖活动
     *
     * @return 保存好的抽奖活动实体
     */
    public ActivityLotteryBO saveLotteryActivity(ActivityLotteryBO activityLotteryBO);

    /**
     * 关闭门店的抽奖活动
     *
     * @param shopId 门店ID
     */
    public void closeLotteryActivity(Long shopId);

    /**
     * 调用微信端获取抽奖预览URL
     *
     * @return 抽奖预览URL
     */
    public String getLotteryPreviewUrl(Long shopId);

   /**
    * 查询抽奖情况
    * @param shopId
    * @return
    */
   public LotteryStatisticDTO qryLotteryStatisticList(Long shopId);

    /**
     * 查询莫奖项用户情况
     * @return
     */
    public Page<LotteryUserPageDTO> qryLotteryUsersByPrizeId(Long shopId,Long lotteryPrizeId,int offset, int limit);

}
