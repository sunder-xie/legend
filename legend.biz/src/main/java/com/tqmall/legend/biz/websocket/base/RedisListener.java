package com.tqmall.legend.biz.websocket.base;

import com.google.gson.Gson;
import com.tqmall.legend.biz.websocket.magic.BoardProcessWebSocket;
import com.tqmall.legend.biz.websocket.login.LoginWebSocket;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.enums.websocket.ChannelsEnum;
import com.tqmall.magic.object.result.board.BoardProcessNoticeDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPubSub;

import java.io.IOException;
import java.util.List;

/**
 * Created by zsy on 16/9/8.
 *
 * 业务场景: redis通知发布订阅
 *
 */
@Component
@Slf4j
public class RedisListener extends JedisPubSub {
    // 取得订阅的消息后的处理
    public void onMessage(String channel, String message) {
        try {
            int codeId = ChannelsEnum.getCodeByName(channel);
            switch (codeId){
                case 1:
                    boardProcessSendMessage(channel, message);
                    break;
                case 2:
                    loginSendMessage(channel, message);
                    break;
            }
        } catch (Exception e) {
            log.error("[redis订阅] websocket推送收到订阅消息异常", e);
        }
    }

    /**
     * 工序看板redis订阅推送消息
     * @param channel
     * @param message
     * @throws IOException
     */
    private void boardProcessSendMessage(String channel, String message) throws IOException {
        log.info("[工序看板redis订阅] 收到消息,通道:{}", channel);
        BoardProcessNoticeDTO boardProcessNoticeDTO = new Gson().fromJson(message, BoardProcessNoticeDTO.class);
        Long lineId = boardProcessNoticeDTO.getLineId();
        Integer count = BoardProcessWebSocket.sendMessage(lineId, message);
        log.info("[工序看板redis订阅] websocket推送收到订阅消息 推送数量:{}", count);
    }

    /**
     * 安全登录redis订阅推送消息
     * @param channel
     * @param message
     * @throws IOException
     */
    private void loginSendMessage(String channel, String message) throws IOException {
        log.info("[安全登录redis订阅] 收到消息,通道:{}", channel);
        Result<List<String>> result = new Gson().fromJson(message, Result.class);
        List<String> list = result.getData();
        String uuid = list.get(0);
        Integer count = LoginWebSocket.sendMessage(uuid, message);
        log.info("[安全登录redis订阅] websocket推送收到订阅消息 推送数量:{}",count);
    }

    // 初始化订阅时候的处理
    public void onSubscribe(String channel, int subscribedChannels) {
        log.info("[redis初始化订阅通道]:{}:{}", channel, subscribedChannels);
    }

    // 取消订阅时候的处理
    public void onUnsubscribe(String channel, int subscribedChannels) {
    }

    // 初始化按表达式的方式订阅时候的处理
    public void onPSubscribe(String pattern, int subscribedChannels) {
    }

    // 取消按表达式的方式订阅时候的处理
    public void onPUnsubscribe(String pattern, int subscribedChannels) {
    }

    // 取得按表达式的方式订阅的消息后的处理
    public void onPMessage(String pattern, String channel, String message) {
    }
}