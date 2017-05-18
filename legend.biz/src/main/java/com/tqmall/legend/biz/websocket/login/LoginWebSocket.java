package com.tqmall.legend.biz.websocket.login;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ServerEndpoint("/websocket/login/{uuid}")
public class LoginWebSocket {
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    public static Map<String, List<Session>> clients = new ConcurrentHashMap<>();

    /**
     * 发送消息给前端
     *
     * @param uuid
     * @param session
     * @throws IOException
     * @throws InterruptedException
     */
    @OnMessage
    public void onMessage(String uuid, Session session)
            throws IOException, InterruptedException {

    }

    /**
     * 给前端发消息
     * 情形：①可登陆②二维码过期
     *
     * @throws java.io.IOException
     */
    public static Integer sendMessage(String uuid, String message) throws IOException {
        List<Session> sessions = get(uuid);
        if (!CollectionUtils.isEmpty(sessions)) {
            for (Session session : sessions) {
                session.getBasicRemote().sendText(message);
            }
            return sessions.size();
        }
        return 0;
    }

    /**
     * 建立连接
     */
    @OnOpen
    public void onOpen(@PathParam("uuid") String uuid, Session session) {
        put(uuid, session);
    }

    /**
     * 关闭连接
     */
    @OnClose
    public void onClose(@PathParam("uuid") String uuid, Session session) {
        List<Session> sessions = get(uuid);   //从set中删除
        if (!CollectionUtils.isEmpty(sessions)) {
            sessions.remove(session);
        }
    }

    /**
     * 发生错误时调用
     *
     * @param error
     */
    @OnError
    public void onError(Throwable error) {
        log.error("【安全登录】websocket连接发生错误", error);
    }

    public void put(String uuid, Session session) {
        List<Session> sessions = get(uuid);
        if (sessions != null) {
            sessions.add(session);
        } else {
            sessions = new ArrayList<>();
            sessions.add(session);
        }
        clients.put(uuid, sessions);
    }

    public static List<Session> get(String uuid) {
        return clients.get(uuid);
    }
}
