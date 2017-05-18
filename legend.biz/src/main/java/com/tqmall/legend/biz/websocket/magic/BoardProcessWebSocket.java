package com.tqmall.legend.biz.websocket.magic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by litan on 16/7/11.
 */

//该注解用来指定一个URI，客户端可以通过这个URI来连接到WebSocket。类似Servlet的注解mapping。无需在web.xml中配置。
@ServerEndpoint("/websocket/{lineId}")
@Slf4j
public class BoardProcessWebSocket {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    public static Map<Long, List<Session>> clients = new ConcurrentHashMap<>();

    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(@PathParam("lineId") Long lineId, Session session) {
        put(lineId, session);     //加入set中
        addOnlineCount();           //在线数加1
        log.info("有新连接加入lineId={}！当前在线人数为:{}",lineId,getOnlineCount());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam("lineId") Long lineId,Session session) {
        List<Session> sessions = get(lineId);   //从set中删除
        if (!CollectionUtils.isEmpty(sessions)) {
            sessions.remove(session);
        }
        subOnlineCount();           //在线数减1
        log.info("有一连接关闭lineId={}！当前在线人数为:{}",lineId,getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param count   客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public static void onMessage(String count, Session session) {
        //群发消息
        /*for(BoardProcessWebSocket item: myWebSockets){
            try {
                item.sendMessage(map.get("message"));
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }*/
    }

    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.info("websocket连接发生错误e={}",error);
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     *
     * @param message
     * @throws java.io.IOException
     */
    public static Integer sendMessage(Long lineId, String message) throws IOException {
        List<Session> sessions = get(lineId);
        if (!CollectionUtils.isEmpty(sessions)) {
            for (Session session : sessions) {
                session.getBasicRemote().sendText(message);
            }
            return sessions.size();
        }
        return 0;
        //this.session.getAsyncRemote().sendText(message);
    }

    public void put(Long lineId, Session session) {
        List<Session> sessions = get(lineId);
        if (sessions != null) {
            sessions.add(session);
        } else {
            sessions = new ArrayList<>();
            sessions.add(session);
        }
        clients.put(lineId, sessions);
    }

    public static List<Session> get(Long lineId) {
        return clients.get(lineId);
    }

    /**
     * 判断是否有连接
     *
     * @return
     */
    public static boolean hasConnection(Long lineId) {
        return clients.containsKey(lineId);
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        BoardProcessWebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        BoardProcessWebSocket.onlineCount--;
    }

}
