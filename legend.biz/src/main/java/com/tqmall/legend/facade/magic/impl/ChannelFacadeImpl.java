package com.tqmall.legend.facade.magic.impl;

import com.tqmall.common.util.JSONUtil;
import com.tqmall.core.common.entity.Result;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.facade.magic.ChannelFacade;
import com.tqmall.legend.facade.magic.vo.ChannelVO;
import com.tqmall.magic.object.param.channel.ChannelParam;
import com.tqmall.magic.object.result.channel.ChannelDTO;
import com.tqmall.magic.object.result.common.PageEntityDTO;
import com.tqmall.magic.service.channel.RpcChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by macx on 16/5/13.
 */
@Service
@Slf4j
public class ChannelFacadeImpl implements ChannelFacade{

    @Autowired
    private RpcChannelService rpcChannelService;

    /**
     * 获取渠道商列表(分页)
     * @param pageable
     * @param shopId
     * @param channelName
     * @return
     */
    @Override
    public Result getChannelPage(Pageable pageable, Long shopId, String channelName) {
        ChannelParam channelParam = new ChannelParam();
        channelParam.setChannelName(channelName);
        channelParam.setShopId(shopId);
        channelParam.setPageNum(pageable.getPageNumber());
        channelParam.setPageSize(pageable.getPageSize());
        try {
            log.info("[共享中心] DUBBO接口rpcChannelService.getChannelList获取分页渠道商列表,参数:shopId={}", shopId);
            Result<PageEntityDTO<ChannelDTO>> channelPageResult = rpcChannelService.getChannelList(channelParam);
            log.info("[共享中心] DUBBO接口rpcChannelService.getChannelList获取分页渠道商列表,返回:success={}",channelPageResult.isSuccess());
            if (channelPageResult.isSuccess()) {
                PageEntityDTO<ChannelDTO> pageEntityDTO = channelPageResult.getData();
                List<ChannelDTO> channelDTOList = pageEntityDTO.getRecordList();
                Integer totalNum = pageEntityDTO.getTotalNum();
                List<ChannelVO> channelVOs = new ArrayList<>();
                for (ChannelDTO channelDTO : channelDTOList) {
                    ChannelVO channelVO = new ChannelVO();
                    BeanUtils.copyProperties(channelDTO,channelVO);
                    channelVOs.add(channelVO);
                }
                PageRequest pageRequest = new PageRequest((pageable.getPageNumber() < 1 ? 1 : pageable.getPageNumber()) - 1,
                        pageable.getPageSize() < 1 ? 1 : pageable.getPageSize(), pageable.getSort());
                DefaultPage<ChannelVO> page = new DefaultPage<>(channelVOs, pageRequest, totalNum);
                return Result.wrapSuccessfulResult(page);
            } else {
                return Result.wrapErrorResult(LegendErrorCode.SHARE_DUBBO_ERROR.getCode(),channelPageResult.getMessage());
            }
        } catch (Exception e) {
            log.error("[共享中心] 获取渠道列表异常 e={}",e);
        }
        return Result.wrapErrorResult(LegendErrorCode.SHARE_CHANNEL_LIST_ERROR.getCode(), LegendErrorCode.SHARE_CHANNEL_LIST_ERROR.getErrorMessage());
    }

    /**
     * 获取渠道商信息
     * @param channelId
     * @param shopId
     * @return
     */
    @Override
    public Result getChannelInfo(Long channelId, Long shopId) {
        try {
            log.info("[共享中心] DUBBO接口rpcChannelService.getChannelInfo获取渠道商信息,参数:channelId={};shopId={}", channelId,shopId);
            Result<ChannelDTO> channelDTOResult = rpcChannelService.getChannelInfo(channelId, shopId);
            log.info("[共享中心] DUBBO接口rpcChannelService.getChannelInfo获取渠道商信息,返回:success={}", channelDTOResult.isSuccess());
            if (channelDTOResult.isSuccess()) {
                ChannelDTO channelDTO = channelDTOResult.getData();
                ChannelVO channelVO = new ChannelVO();
                BeanUtils.copyProperties(channelDTO, channelVO);
                return Result.wrapSuccessfulResult(channelVO);
            } else {
                return Result.wrapErrorResult(LegendErrorCode.SHARE_DUBBO_ERROR.getCode(), channelDTOResult.getMessage());
            }
        } catch (Exception e) {
            log.error("[共享中心] 获取渠道信息异常 e={}", e);
        }
        return Result.wrapErrorResult(LegendErrorCode.SHARE_CHANNEL_INFO_ERROR.getCode(), LegendErrorCode.SHARE_CHANNEL_INFO_ERROR.getErrorMessage());
    }

    /**
     * 保存渠道商信息
     * @param channelVO
     * @param shopId
     * @return
     */
    @Override
    public Result saveChannelInfo(ChannelVO channelVO, Long shopId) {
        try{
            ChannelParam channelParam = new ChannelParam();
            BeanUtils.copyProperties(channelVO, channelParam);
            channelParam.setShopId(shopId);
            log.info("[共享中心] DUBBO接口rpcChannelService.saveChannelInfo保存渠道商信息,参数:shopId={}", shopId);
            Result result = rpcChannelService.saveChannelInfo(channelParam);
            log.info("[共享中心] DUBBO接口rpcChannelService.saveChannelInfo保存渠道商信息,返回:success={}", result.isSuccess());
            if (result.isSuccess()) {
                return Result.wrapSuccessfulResult("保存成功");
            } else {
                return Result.wrapErrorResult(LegendErrorCode.SHARE_DUBBO_ERROR.getCode(), result.getMessage());
            }
        }catch (Exception e){
            log.error("[共享中心] 保存渠道商信息异常 e={}",e);
        }
        return Result.wrapErrorResult(LegendErrorCode.SHARE_CHANNEL_SAVE_ERROR.getCode(), LegendErrorCode.SHARE_CHANNEL_SAVE_ERROR.getErrorMessage());
    }

    /**
     * 删除渠道商
     * @param channelId
     * @param shopId
     * @return
     */
    @Override
    public Result deleteChannelInfo(Long channelId, Long shopId) {
        try{
            log.info("[共享中心] DUBBO接口rpcChannelService.deleteChannelInfo删除渠道商,参数:channelId={};shopId={}", channelId,shopId);
            Result result = rpcChannelService.deleteChannelInfo(channelId, shopId);
            log.info("[共享中心] DUBBO接口rpcChannelService.deleteChannelInfo删除渠道商,返回:success={}", result.isSuccess());
            if (result.isSuccess()) {
                return Result.wrapSuccessfulResult("删除成功");
            } else {
                return Result.wrapErrorResult(LegendErrorCode.SHARE_DUBBO_ERROR.getCode(), result.getMessage());
            }
        }catch (Exception e){
            log.error("[共享中心] 删除渠道商信息异常 e={}", e);
        }
        return Result.wrapErrorResult(LegendErrorCode.SHARE_CHANNEL_DELETE_ERROR.getCode(), LegendErrorCode.SHARE_CHANNEL_DELETE_ERROR.getErrorMessage());
    }

}
