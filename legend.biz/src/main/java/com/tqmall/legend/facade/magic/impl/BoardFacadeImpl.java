package com.tqmall.legend.facade.magic.impl;

import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;
import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.biz.websocket.base.RedisPublish;
import com.tqmall.legend.enums.websocket.ChannelsEnum;
import com.tqmall.legend.facade.magic.BoardFacade;
import com.tqmall.magic.object.result.board.BoardProcessNoticeDTO;
import com.tqmall.magic.service.board.RpcBoardProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 业务场景:看板
 * 创建人: macx
 * 创建时间: 16/7/27.
 * 最后修改时间: 16/7/27.
 * 修改记录:
 */
@Service
@Slf4j
public class BoardFacadeImpl implements BoardFacade{

    @Autowired
    private RpcBoardProcessService rpcBoardProcessService;
    @Autowired
    private RedisPublish redisPublish;


    @Override
    public void sendMessage(Long shopId, List<Long> orderIds) {
        try {
            Result<List<BoardProcessNoticeDTO>> messageResult = rpcBoardProcessService.settlementNoticeInfo(shopId, Lists.newArrayList(orderIds));
            if (messageResult.isSuccess()){
                List<BoardProcessNoticeDTO> boardProcessNoticeDTOs = messageResult.getData();
                if (!CollectionUtils.isEmpty(boardProcessNoticeDTOs)) {
                    for (BoardProcessNoticeDTO boardProcessNoticeDTO : boardProcessNoticeDTOs) {
                        boolean success = redisPublish.shareNotice(ChannelsEnum.BP_BOARD_PROCESS_SHARE_NOTICE.name(),new GsonBuilder().serializeNulls().create().toJson(boardProcessNoticeDTO));
                        if (success) {
                            log.info("[看板消息] redis发布看板消息成功,lineId={}",boardProcessNoticeDTO.getLineId());
                        }else{
                            log.info("[看板消息] redis发布看板消息失败,lineId={}",boardProcessNoticeDTO.getLineId());
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("[看板消息] redis发布看板消息异常 e={}", e);
        }
    }

    @Override
    public void sendMessage(Long shopId, String workOrderSn, Long workOrderId) {
        try {
            Result result = rpcBoardProcessService.queryNoticeInfo(shopId, workOrderId, workOrderSn);
            if (result.isSuccess()) {
                BoardProcessNoticeDTO boardProcessNoticeDTO = (BoardProcessNoticeDTO) result.getData();
                boolean success = redisPublish.shareNotice(ChannelsEnum.BP_BOARD_PROCESS_SHARE_NOTICE.name(),new GsonBuilder().serializeNulls().create().toJson(boardProcessNoticeDTO));
                if (success) {
                    log.info("[看板消息] redis发布看板消息成功,lineId={}",boardProcessNoticeDTO.getLineId());
                }else{
                    log.info("[看板消息] redis发布看板消息失败,lineId={}",boardProcessNoticeDTO.getLineId());
                }
            }
        } catch (Exception e) {
            log.error("[看板消息] redis发布看板消息异常,e={}",e);
        }
    }

    @Override
    public void sendMessage(Long shopId, Long workOrderId) {
        try {
            Result<BoardProcessNoticeDTO> messageResult = rpcBoardProcessService.updatePaintNoticeInfo(shopId, workOrderId);
            if (messageResult.isSuccess()){
                BoardProcessNoticeDTO boardProcessNoticeDTO = messageResult.getData();
                boolean success = redisPublish.shareNotice(ChannelsEnum.BP_BOARD_PROCESS_SHARE_NOTICE.name(),new GsonBuilder().serializeNulls().create().toJson(boardProcessNoticeDTO));
                if (success) {
                    log.info("[看板消息] redis发布看板消息成功,lineId={}",boardProcessNoticeDTO.getLineId());
                }else{
                    log.info("[看板消息] redis发布看板消息失败,lineId={}",boardProcessNoticeDTO.getLineId());
                }
            }
        } catch (Exception e) {
            log.error("[看板消息] redis发布看板消息异常 e={}", e);
        }
    }
}
