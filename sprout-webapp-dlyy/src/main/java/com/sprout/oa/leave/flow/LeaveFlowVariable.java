package com.sprout.oa.leave.flow;

import com.sprout.common.util.SproutStringUtils;
import com.sprout.oa.leave.entity.Leave;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class LeaveFlowVariable implements Serializable {

    private String startUserId;

    private int firstApprovalState;

    private String firstApprovalContent;

    private int secondApprovalState;

    private String secondApprovalContent;

    private int replayState;

    private String firstApprovalId;

    private String secondApprovalId;

    public String getStartUserId() {
        return startUserId;
    }

    public void setStartUserId(String startUserId) {
        this.startUserId = startUserId;
    }

    public int getFirstApprovalState() {
        return firstApprovalState;
    }

    public void setFirstApprovalState(int firstApprovalState) {
        this.firstApprovalState = firstApprovalState;
    }

    public String getFirstApprovalContent() {
        return firstApprovalContent;
    }

    public void setFirstApprovalContent(String firstApprovalContent) {
        this.firstApprovalContent = firstApprovalContent;
    }

    public int getSecondApprovalState() {
        return secondApprovalState;
    }

    public void setSecondApprovalState(int secondApprovalState) {
        this.secondApprovalState = secondApprovalState;
    }

    public String getSecondApprovalContent() {
        return secondApprovalContent;
    }

    public void setSecondApprovalContent(String secondApprovalContent) {
        this.secondApprovalContent = secondApprovalContent;
    }

    public int getReplayState() {
        return replayState;
    }

    public void setReplayState(int replayState) {
        this.replayState = replayState;
    }

    public String getFirstApprovalId() {
        return firstApprovalId;
    }

    public void setFirstApprovalId(String firstApprovalId) {
        this.firstApprovalId = firstApprovalId;
    }

    public String getSecondApprovalId() {
        return secondApprovalId;
    }

    public void setSecondApprovalId(String secondApprovalId) {
        this.secondApprovalId = secondApprovalId;
    }

    public Map<String, Object> transToMap() {
        Map<String, Object> variable = new HashMap<>();
        if (SproutStringUtils.isNotBlank(startUserId)) {
            variable.put("startUserId", startUserId);
        }
        variable.put("firstApprovalState", firstApprovalState);
        variable.put("firstApprovalContent", firstApprovalContent);
        variable.put("secondApprovalState", secondApprovalState);
        variable.put("secondApprovalContent", secondApprovalContent);
        variable.put("replayState", replayState);
        variable.put("firstApprovalId", firstApprovalId);
        variable.put("secondApprovalId", secondApprovalId);
        variable.put("leave", leave);

        return variable;
    }

    private Map<String, Object> leave;

    public Map<String, Object> getLeave() {
        return leave;
    }

    public void setLeave(Map<String, Object> leave) {
        this.leave = leave;
    }
}
