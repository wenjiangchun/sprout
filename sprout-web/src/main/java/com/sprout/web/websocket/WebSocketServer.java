package com.sprout.web.websocket;

import com.sprout.common.util.SproutJsonUtils;
import com.sprout.common.util.SproutStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * websocket通信服务器
 *
 * <p>客户端使用Example:</p>
 * <pre><code>
 * if ('WebSocket' in window) {
 *         websocket = new WebSocket("ws://localhost:8081/websocket/zhangsan");
 *         websocket.onopen = function () {
 *             console.log("连接成功");
 *         };
 *
 *         websocket.onclose = function () {
 *             console.log("退出连接");
 *         };
 *
 *         websocket.onmessage = function (message) {
 *             console.log("收到消息" + message.data);
 *             websocket.send("发送信息至服务器")
 *         };
 *
 *         websocket.onerror = function () {
 *             console.log("连接出错");
 *         };
 *
 *         window.onbeforeunload = function () {
 *             websocket.close();
 *         };
 *     } else {
 *         console.warn('当前浏览器不支持websocket')
 *     }
 * </code></pre>
 */
@Component
@ServerEndpoint("/websocket/{name}")
public class WebSocketServer {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    /**
     * 与某个客户端的连接对话，需要通过它来给客户端发送消息
     */
    private Session session;

    /**
     * 标识当前连接客户端的用户名
     */
    private String name;

    /**
     * 用于存所有的连接服务的客户端，这个对象存储是安全的
     */
    private static Map<String, WebSocketServer> connectedMap = new ConcurrentHashMap<>();


    @OnOpen
    public void OnOpen(Session session, @PathParam(value = "name") String name) {
        this.session = session;
        session.setMaxIdleTimeout(0);
        this.name = name;
        connectedMap.put(name, this);
        logger.info("[WebSocketServer] 连接成功，name={}, 当前连接人数为：={}", name, connectedMap.size());
    }


    @OnClose
    public void OnClose() {
        connectedMap.remove(this.name);
        logger.info("[WebSocketServer] 退出成功，name={},当前连接人数为：={}", name, connectedMap.size());
    }

    @OnMessage
    public void OnMessage(String message) {
        logger.info("[WebSocketServer] 收到消息：{}", message);
        //判断是否需要指定发送，具体规则自定义
        /*if (message.indexOf("TOUSER") == 0) {
            String name = message.substring(message.indexOf("TOUSER") + 6, message.indexOf(";"));
            sendMessageToName(name, message.substring(message.indexOf(";") + 1, message.length()));
        } else {
            sendMessageToAll(message);
        }*/
        //TODO 暂未实现
    }

    /**
     * 对当前所有已连接客户端发送信息
     *
     * @param message 信息内容
     */
    public void sendMessageToAll(String message) {
        for (String name : connectedMap.keySet()) {
            try {
                connectedMap.get(name).session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                logger.error("[WebSocketServer] sendMessageToAll 信息发送失败, message={}, e={}", message, e);
            }
        }
    }

    /**
     * 群发对指定集合内名称的连接发送信息
     *
     * @param message 信息内容
     * @param names   连接名称集合
     */
    public void sendMessageToNames(String message, Set<String> names) {
        for (String name : names) {
            try {
                if (connectedMap.containsKey(name)) {
                    connectedMap.get(name).session.getBasicRemote().sendText(message);
                }
            } catch (Exception e) {
                logger.error("[WebSocketServer] sendMessageToNames 信息发送失败, message={}, names={}, e={}", message, SproutStringUtils.join(names, ","), e);
            }
        }
    }

    /**
     * 对指定名称的连接发送信息
     *
     * @param message 信息内容
     * @param name    连接名称
     */
    public void sendMessageToName(String message, String name) {
        try {
            connectedMap.get(name).session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            logger.error("[WebSocketServer] sendMessageToName 信息发送失败, name={}, message={}, e={}", name, message, e);
        }
    }

    /**
     * 对指定名称的连接发送信息
     *
     * @param webSocketMessage 信息对象
     * @param name    连接名称
     */
    public <T extends Serializable> void sendMessageToName(WebSocketMessage<T> webSocketMessage, String name) {
        try {
            //connectedMap.get(name).session.getBasicRemote().sendObject(webSocketMessage);
            connectedMap.get(name).session.getBasicRemote().sendText(SproutJsonUtils.writeToString(webSocketMessage));
        } catch (Exception e) {
            //logger.error("[WebSocketServer] sendMessageToName 信息发送失败, name={}, message={}, e={}", name, webSocketMessage, e);
        }
    }

    public static Map<String, WebSocketServer> getConnectedMap() {
        return connectedMap;
    }
}
