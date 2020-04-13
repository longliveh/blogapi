package com.bobilwm.weibo.controller.live;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@Component
@ServerEndpoint("/ws/chat/{roomid}")
//@ServerEndpoint("/websocket")
public class LiveChatWs {
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    private static final Map<String, Set<Session>> rooms = new ConcurrentHashMap();

    /**
     * 以用户的姓名为key，WebSocket为对象保存起来
     */
    private static final Map<String, LiveChatWs> clients = new ConcurrentHashMap<String, LiveChatWs>();
    /**
     * 会话
     */
    private Session session;

    /**
     * 建立连接
     *
     * @param session
     */
    @OnOpen
    public void connect(@PathParam("roomid") String roomName, Session session) throws Exception {
        // 将session按照房间名来存储，将各个房间的用户隔离
        if (!rooms.containsKey(roomName)) {
            // 创建房间不存在时，创建房间
            Set<Session> room = new HashSet<Session>();
            // 添加用户
            room.add(session);

            rooms.put(roomName, room);
        } else {
            // 房间已存在，直接添加用户到相应的房间
            rooms.get(roomName).add(session);
        }
        System.err.println("session" + session);
        System.out.println("a client has connected!");
    }

    @OnError
    public void onError(Session session, Throwable error) {
        logger.info("服务端发生了错误" + error.getMessage());
        //error.printStackTrace();
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void disConnect(@PathParam("roomid") String roomName, Session session) {
        rooms.get(roomName).remove(session);
        System.out.println("a client has disconnected!");
    }


    /**
     * 收到客户端的消息
     *
     * @param msg     消息
     * @param session 会话
     */
    @OnMessage
    public void receiveMsg(@PathParam("roomid") String roomName, String msg, Session session) throws Exception {
//        // 此处应该有html过滤
//        msg = session.getId() + ":" + msg;
//        System.out.println(msg);
//        // 接收到信息后进行广播
//        broadcast(roomName, msg);
    }

    // 按照房间名进行广播
    public static void broadcast(String roomid, String msg) throws Exception {
        if (rooms.get(roomid) != null) {
            for (Session session : rooms.get(roomid)) {
                session.getBasicRemote().sendText(msg);
            }
            System.out.println("room size:" + rooms.get(roomid).size());
        }
    }


}
