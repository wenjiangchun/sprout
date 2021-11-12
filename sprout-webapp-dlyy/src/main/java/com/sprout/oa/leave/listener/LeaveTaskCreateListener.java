package com.sprout.oa.leave.listener;

import com.sprout.core.spring.SpringContextUtils;
import com.sprout.flowable.service.ProcessInstanceService;
import com.sprout.oa.leave.service.LeaveService;
import com.sprout.oa.leave.service.LeaveTaskLogService;
import com.sprout.oa.notice.NoticeMessage;
import com.sprout.oa.notice.NoticeType;
import com.sprout.system.service.UserService;
import com.sprout.web.websocket.WebSocketServer;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.common.engine.impl.event.FlowableEntityEventImpl;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LeaveTaskCreateListener implements FlowableEventListener {

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
    public void onEvent(FlowableEvent event) {
        System.out.println(event.getType());
        if (event.getType().name().equals("TASK_CREATED")) {
            FlowableEntityEventImpl ent = (FlowableEntityEventImpl) event;
            TaskEntityImpl taskEntity = (TaskEntityImpl) ent.getEntity();
            logger.debug("Flowable-task创建完成, taskId={}, taskName={}, createTime={}", taskEntity.getTaskDefinitionId(), taskEntity.getName(), taskEntity.getCreateTime());
            String assignee = taskEntity.getAssignee();
            //SpringContextUtils.getBean(WebSocketServer.class).sendMessageToName(new NoticeMessage<>(taskEntity.getName(), NoticeType.TODO), assignee);
        }
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }

    @Override
    public boolean isFireOnTransactionLifecycleEvent() {
        return false;
    }

    @Override
    public String getOnTransaction() {
        return null;
    }
}
