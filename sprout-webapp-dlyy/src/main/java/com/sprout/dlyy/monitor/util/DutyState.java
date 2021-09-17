package com.sprout.dlyy.monitor.util;

/**
 * 打卡状态
 */
public enum DutyState {

    COME_LATE("迟到"),

    LEAVE_EARLY("早退"),

    ABSENCE("缺勤"),

    NORMAL("正常");

    private String state;

    DutyState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
