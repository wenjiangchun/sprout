package com.sprout.oa.leave.flow;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class LeaveFlowVariable implements Serializable {

    //绑定流程参数对象
    private Map<String, Object> flowVariables = new HashMap<>();

    public Map<String, Object> getFlowVariables() {
        return flowVariables;
    }

    public void setFlowVariables(Map<String, Object> flowVariables) {
        this.flowVariables = flowVariables;
    }

    private Map<String, Object> leave;

    public Map<String, Object> getLeave() {
        return leave;
    }

    public void setLeave(Map<String, Object> leave) {
        this.leave = leave;
    }
}
