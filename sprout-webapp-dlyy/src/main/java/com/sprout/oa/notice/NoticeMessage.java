package com.sprout.oa.notice;

import com.sprout.web.websocket.WebSocketMessage;

import java.io.Serializable;

public class NoticeMessage<T extends Serializable> implements WebSocketMessage<T> {

    private T t;

    private NoticeType noticeType;

    public NoticeMessage(T t, NoticeType noticeType) {
        this.t = t;
        this.noticeType = noticeType;
    }

    @Override
    public T getPayLoad() {
        return t;
    }

    @Override
    public int getMessageType() {
        return noticeType.getType();
    }
}
