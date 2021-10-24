package com.sprout.oa.leave.listener;

import com.sprout.flowable.service.ProcessInstanceService;
import com.sprout.oa.leave.service.LeaveService;
import com.sprout.oa.leave.service.LeaveTaskLogService;
import com.sprout.system.service.UserService;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LeaveTaskCreateListener implements TaskListener {

    private static Logger logger = LoggerFactory.getLogger(LeaveTaskCreateListener.class);

    private ProcessInstanceService processInstanceService;

    private LeaveTaskLogService leaveTaskLogService;

    private UserService userService;

    private LeaveService leaveService;

    public LeaveTaskCreateListener(ProcessInstanceService processInstanceService, LeaveTaskLogService leaveTaskLogService, UserService userService, LeaveService leaveService) {
        this.processInstanceService = processInstanceService;
        this.leaveTaskLogService = leaveTaskLogService;
        this.userService = userService;
        this.leaveService = leaveService;
    }

    @Override
    public void notify(DelegateTask delegateTask) {
        Map<String, Object> variables = delegateTask.getVariables();
    }
}
