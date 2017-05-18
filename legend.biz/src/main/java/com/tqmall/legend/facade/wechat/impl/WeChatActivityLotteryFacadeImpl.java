package com.tqmall.legend.facade.wechat.impl;

import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.ObjectUtils;
import com.tqmall.core.common.entity.Result;
import com.tqmall.dandelion.wechat.client.dto.wechat.lottery.*;
import com.tqmall.dandelion.wechat.client.param.wechat.LotteryUserStatisticParam;
import com.tqmall.dandelion.wechat.client.wechat.lottery.DubboWeChatActivityLotteryService;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.facade.wechat.WeChatActivityLotteryFacade;
import com.tqmall.legend.facade.wechat.bo.ActivityLotteryBO;
import com.tqmall.legend.facade.wechat.bo.ActivityLotteryPrizeBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 微信公众号门店抽奖管理
 *
 * @author kang.zhao@tqmall.com
 * @version 1.0 2016/10/18
 */
@Service
@Slf4j
public class WeChatActivityLotteryFacadeImpl implements WeChatActivityLotteryFacade{
    @Autowired
    private DubboWeChatActivityLotteryService dubboWeChatActivityLotteryService;
    @Autowired
    private ShopService shopService;
    /**
     * @see com.tqmall.legend.facade.wechat.WeChatActivityLotteryFacade#getLotteryByShopId(Long)
     */
    @Override
    public ActivityLotteryBO getLotteryByShopId(Long shopId) {
        return getLotteryFromWeChatByShopId(shopId);
    }

    /**
     * 调用微信公众号dubbo接口获取抽奖活动数据
     *
     * @param shopId 门店ID
     *
     * @return 门店设置的抽奖活动数据
     */
    private ActivityLotteryBO getLotteryFromWeChatByShopId(Long shopId) {
        Result<ActivityLotteryDTO> result;

        try {
            log.info("调用微信公众号服务,method={dubboWeChatActivityLotteryService.getLotteryByShopId}, shopId={" + shopId + "}");

            result = dubboWeChatActivityLotteryService.getOpeningLotteryByShopId(shopId);

        } catch (Exception e) {
            log.error("调用微信公众号服务异常, method={dubboWeChatActivityLotteryService.getLotteryByShopId}, shopId={" + shopId + "}", e);
            throw new BizException(LegendErrorCode.SYSTEM_ERROR.getErrorMessage());
        }

        if (result == null) {
            log.error("调用微信公众号服务返回异常, method={dubboWeChatActivityLotteryService.getLotteryByShopId}, shopId={" + shopId + "},result is null");
            throw new BizException(LegendErrorCode.SYSTEM_ERROR.getErrorMessage());
        }

        if (!result.isSuccess()) {
            log.error("调用微信公众号服务返回异常, method={dubboWeChatActivityLotteryService.getLotteryByShopId}, shopId={" + shopId + "},errorCode=" + result.getCode() + ",message={" + result.getMessage() + "}");
            throw new BizException(LegendErrorCode.SYSTEM_ERROR.getErrorMessage());
        }

        ActivityLotteryDTO activityLotteryDTO = result.getData();

        if (null == activityLotteryDTO) {
            return null;
        }

        return convertActivityLotteryDTO2BO(activityLotteryDTO);
    }

    /**
     * @see com.tqmall.legend.facade.wechat.WeChatActivityLotteryFacade#saveLotteryActivity(com.tqmall.legend.facade.wechat.bo.ActivityLotteryBO)
     */
    @Override
    public ActivityLotteryBO saveLotteryActivity(ActivityLotteryBO activityLotteryBO) {
        return saveLotteryActivityToWeChat(activityLotteryBO);
    }

    private ActivityLotteryBO saveLotteryActivityToWeChat(ActivityLotteryBO activityLotteryBO) {
        ActivityLotteryDTO activityLotteryDTO = convertActivityLotteryBO2DTO(activityLotteryBO);

        Result<ActivityLotteryDTO> result;

        try {
            log.info("调用微信公众号服务,method={dubboWeChatActivityLotteryService.saveLotteryActivity}, param={" + ObjectUtils.objectToJSON(activityLotteryDTO) + "}");

            result = dubboWeChatActivityLotteryService.saveLotteryActivity(activityLotteryDTO);

        } catch (Exception e) {
            log.error("调用微信公众号服务异常, method={dubboWeChatActivityLotteryService.saveLotteryActivity}, param={" + ObjectUtils.objectToJSON(activityLotteryDTO) + "}", e);
            throw new BizException(LegendErrorCode.SYSTEM_ERROR.getErrorMessage());
        }

        if (result == null) {
            log.error("调用微信公众号服务返回异常, method={dubboWeChatActivityLotteryService.saveLotteryActivity}, param={" + ObjectUtils.objectToJSON(activityLotteryDTO) + "},result is null");
            throw new BizException(LegendErrorCode.SYSTEM_ERROR.getErrorMessage());
        }

        if (!result.isSuccess()) {
            log.error("调用微信公众号服务返回异常, method={dubboWeChatActivityLotteryService.saveLotteryActivity}, param={" + ObjectUtils.objectToJSON(activityLotteryDTO) + "},errorCode=" + result.getCode() + ",message={" + result.getMessage() + "}");
            throw new BizException(result.getMessage());
        }

        activityLotteryDTO = result.getData();

        if (null == activityLotteryDTO) {
            return null;
        }

        return convertActivityLotteryDTO2BO(activityLotteryDTO);
    }

    private ActivityLotteryBO convertActivityLotteryDTO2BO(ActivityLotteryDTO activityLotteryDTO){
        ActivityLotteryBO activityLotteryBO = new ActivityLotteryBO();

        BeanUtils.copyProperties(activityLotteryDTO, activityLotteryBO);

        if(activityLotteryDTO.getActivityLotteryPrizeDTOList() != null){
            List<ActivityLotteryPrizeBO> activityLotteryPrizeBOList = new ArrayList<>();

            for(ActivityLotteryPrizeDTO activityLotteryPrizeDTO : activityLotteryDTO.getActivityLotteryPrizeDTOList()){
                ActivityLotteryPrizeBO activityLotteryPrizeBO = new ActivityLotteryPrizeBO();

                BeanUtils.copyProperties(activityLotteryPrizeDTO, activityLotteryPrizeBO);

                activityLotteryPrizeBOList.add(activityLotteryPrizeBO);
            }

            activityLotteryBO.setActivityLotteryPrizeBOList(activityLotteryPrizeBOList);
        }

        return activityLotteryBO;
    }

    private ActivityLotteryDTO convertActivityLotteryBO2DTO(ActivityLotteryBO activityLotteryBO){
        ActivityLotteryDTO activityLotteryDTO = new ActivityLotteryDTO();

        BeanUtils.copyProperties(activityLotteryBO, activityLotteryDTO, "activityLotteryPrizeDTOList");

        if(activityLotteryBO.getActivityLotteryPrizeBOList() != null){
            List<ActivityLotteryPrizeDTO> activityLotteryPrizeDTOList = new ArrayList<>();

            for(ActivityLotteryPrizeBO activityLotteryPrizeBO : activityLotteryBO.getActivityLotteryPrizeBOList()){
                ActivityLotteryPrizeDTO activityLotteryPrizeDTO = new ActivityLotteryPrizeDTO();

                BeanUtils.copyProperties(activityLotteryPrizeBO, activityLotteryPrizeDTO);

                activityLotteryPrizeDTOList.add(activityLotteryPrizeDTO);
            }

            activityLotteryDTO.setActivityLotteryPrizeDTOList(activityLotteryPrizeDTOList);
        }

        return activityLotteryDTO;
    }

    /**
     * @see com.tqmall.legend.facade.wechat.WeChatActivityLotteryFacade#closeLotteryActivity(Long)
     */
    @Override
    public void closeLotteryActivity(Long shopId) {
        closeWechatLotteryActivity(shopId);
    }

    /**
     * 调用微信公众号dubbo接口关闭抽奖活动
     *
     * @param shopId 门店ID
     *
     */
    private Boolean closeWechatLotteryActivity(Long shopId) {
        Result<Boolean> result;

        try {
            log.info("调用微信公众号服务,method={dubboWeChatActivityLotteryService.closeOldLotteryActivity}, shopId={" + shopId + "}");

            result = dubboWeChatActivityLotteryService.closeOldLotteryActivity(shopId);

        } catch (Exception e) {
            log.error("调用微信公众号服务异常, method={dubboWeChatActivityLotteryService.closeOldLotteryActivity}, shopId={" + shopId + "}", e);
            throw new BizException(LegendErrorCode.SYSTEM_ERROR.getErrorMessage());
        }

        if (result == null) {
            log.error("调用微信公众号服务返回异常, method={dubboWeChatActivityLotteryService.closeOldLotteryActivity}, shopId={" + shopId + "},result is null");
            throw new BizException(LegendErrorCode.SYSTEM_ERROR.getErrorMessage());
        }

        if (!result.isSuccess()) {
            log.error("调用微信公众号服务返回异常, method={dubboWeChatActivityLotteryService.closeOldLotteryActivity}, shopId={" + shopId + "},errorCode=" + result.getCode() + ",message={" + result.getMessage() + "}");
            throw new BizException(LegendErrorCode.SYSTEM_ERROR.getErrorMessage());
        }

        return result.getData();
    }

    @Override
    public String getLotteryPreviewUrl(Long shopId) {
        return getLotteryPreviewUrlFromWeChat(shopId);
    }

    @Override
    public LotteryStatisticDTO qryLotteryStatisticList(Long shopId) {
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        //.查询砍价活动内服务的预约统计数据
        com.tqmall.core.common.entity.Result<LotteryStatisticDTO> result = dubboWeChatActivityLotteryService.qryLotteryStatisticList(userGlobalId);
        log.info("[consumer-ddlwechat-dubbo]查询抽奖活动的统计信息,userGlobalId:{},actId:{},success:{}", userGlobalId,result.isSuccess());
        LotteryStatisticDTO lotteryStatisticDTO = result.getData();
        return lotteryStatisticDTO;
    }

    @Override
    public Page<LotteryUserPageDTO> qryLotteryUsersByPrizeId(Long shopId,Long lotteryPrizeId, int offset, int limit) {
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        Integer pageNum = offset/limit;//从0开始
        PageRequest pageRequest = new PageRequest(pageNum, limit);
        Page<LotteryUserPageDTO> page = new DefaultPage(new ArrayList(), pageRequest, 0);//默认没有数据的page
        LotteryUserStatisticParam param=new LotteryUserStatisticParam();
        param.setUcShopId(userGlobalId);
        param.setLotteryPrizeId(lotteryPrizeId);
        param.setOffset(offset);
        param.setLimit(limit);
        com.tqmall.core.common.entity.Result<LotteryUserPageDTO> result = dubboWeChatActivityLotteryService.qryLotteryUsersByPrizeId(param);
        log.info("[consumer-ddlwechat-dubbo]查询抽奖活动的用户统计信息,serviceId:{},userGlobalId:{},actId:{},success:{}",lotteryPrizeId,result.isSuccess());
        LotteryUserPageDTO lotteryUserPageDTO = result.getData();
        if(lotteryUserPageDTO==null||lotteryUserPageDTO.getTotal()==null||lotteryUserPageDTO.getInfo()==null){
            return page;
        }
        int count = lotteryUserPageDTO.getTotal();
        List<ActivityLotteryRecordDTO> activityLotteryRecordDTOs = lotteryUserPageDTO.getInfo();
        page = new DefaultPage(activityLotteryRecordDTOs,pageRequest,count);
        return page;
    }

    private String getLotteryPreviewUrlFromWeChat(Long shopId){
        Result<String> result;

        try {
            log.info("调用微信公众号服务,method={dubboWeChatActivityLotteryService.getLotteryPreviewUrl}");
            Long userGlobalId = shopService.getUserGlobalId(shopId);
            result = dubboWeChatActivityLotteryService.getLotteryPreviewUrl(userGlobalId);

        } catch (Exception e) {
            log.error("调用微信公众号服务异常, method={dubboWeChatActivityLotteryService.getLotteryPreviewUrl}", e);
            throw new BizException(LegendErrorCode.SYSTEM_ERROR.getErrorMessage());
        }

        if (result == null) {
            log.error("调用微信公众号服务返回异常, method={dubboWeChatActivityLotteryService.getLotteryPreviewUrl},result is null");
            throw new BizException(LegendErrorCode.SYSTEM_ERROR.getErrorMessage());
        }

        if (!result.isSuccess()) {
            log.error("调用微信公众号服务返回异常, method={dubboWeChatActivityLotteryService.getLotteryPreviewUrl},errorCode=" + result.getCode() + ",message={" + result.getMessage() + "}");
            throw new BizException(LegendErrorCode.SYSTEM_ERROR.getErrorMessage());
        }

        return result.getData();
    }
}
