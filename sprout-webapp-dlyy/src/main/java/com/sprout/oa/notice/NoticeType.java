package com.sprout.oa.notice;

public enum NoticeType {

    //代办事项
    TODO(0),

    //普通通知
    NOTICE(1);

    private final int type;

    NoticeType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
