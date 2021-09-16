package com.sprout.web.websocket;

import com.sprout.web.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashSet;
import java.util.List;

/**
 * websocket通信发送
 * <pre>
 *     <strong>说明：</strong>也可以使用客户端<code>websocket.send()</code>发送信息,该方法发送的信息将会被{@link WebSocketServer#OnMessage(String)}处理
 * </pre>
 *
 * @see WebSocketServer#OnMessage(String)
 */
@Controller
@RequestMapping("/websocket")
public class WebSocketController {

    @Autowired
    private WebSocketServer webSocketServer;

    /**
     * 发送信息至当前已连接的websocket客户端
     * @param message 信息内容
     * @param destinations 目标对象,如果为null则群发消息
     */
    @PostMapping("/sendMessage")
    @ResponseBody
    public void sendMessage(@RequestParam String message, @RequestParam(required = false) List<String> destinations) {
        if (destinations != null) {
            webSocketServer.sendMessageToNames(message, new HashSet<>(destinations));
        } else {
            webSocketServer.sendMessageToAll(message);
        }
    }
}
