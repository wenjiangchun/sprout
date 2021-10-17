package com.sprout.web.websocket;

import java.io.Serializable;

public interface WebSocketMessage<T extends Serializable> {

    T getPayLoad();

    int getMessageType();
}
