package com.sprout.oa.leave.listener;

import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LeaveTaskCompleteListener implements TaskListener {

    private static Logger logger = LoggerFactory.getLogger(LeaveTaskCompleteListener.class);
    @Override
    public void notify(DelegateTask delegateTask) {
        delegateTask.getEventName();
        logger.debug("执行任务监听..." + delegateTask);
    }
}
