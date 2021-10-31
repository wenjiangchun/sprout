package com.sprout.oa.leave.util;

/**
 * 请假状态
 */
public enum LeaveState {

    /**
     * 已申请未审核
     */
    UNDO(0),

    /**
     * 办理中
     */
    DOING(1),

    /**
     * 正常结束
     */
    DONE(2),

    /**
     * 放弃申请
     */
    CANCEL(-1);

    private final int state;

    LeaveState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }
}
