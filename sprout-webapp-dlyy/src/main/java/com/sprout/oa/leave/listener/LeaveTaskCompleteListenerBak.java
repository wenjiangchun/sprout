package com.sprout.oa.leave.listener;

import com.sprout.common.util.SproutDateUtils;
import com.sprout.common.util.SproutJsonUtils;
import com.sprout.core.spring.SpringContextUtils;
import com.sprout.dlyy.message.MessageSender;
import com.sprout.flowable.service.ProcessInstanceService;
import com.sprout.oa.leave.entity.Leave;
import com.sprout.oa.leave.entity.LeaveTaskLog;
import com.sprout.oa.leave.service.LeaveService;
import com.sprout.oa.leave.service.LeaveTaskLogService;
import com.sprout.oa.leave.util.LeaveState;
import com.sprout.oa.notice.NoticeMessage;
import com.sprout.oa.notice.NoticeType;
import com.sprout.system.entity.User;
import com.sprout.system.service.UserService;
import com.sprout.web.websocket.WebSocketServer;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class LeaveTaskCompleteListenerBak implements TaskListener {

    private static Logger logger = LoggerFactory.getLogger(LeaveTaskCompleteListenerBak.class);

    private ProcessInstanceService processInstanceService;

    private LeaveTaskLogService leaveTaskLogService;

    private UserService userService;

    private LeaveService leaveService;

    public LeaveTaskCompleteListenerBak(ProcessInstanceService processInstanceService, LeaveTaskLogService leaveTaskLogService, UserService userService, LeaveService leaveService) {
        this.processInstanceService = processInstanceService;
        this.leaveTaskLogService = leaveTaskLogService;
        this.userService = userService;
        this.leaveService = leaveService;
    }

    @Override
    public void notify(DelegateTask delegateTask) {
        Map<String, Object> variables = delegateTask.getVariables();
        String taskKey = delegateTask.getTaskDefinitionKey();
        String taskName = delegateTask.getName();
        String handlerId = delegateTask.getAssignee();
        String processInstanceId = delegateTask.getProcessInstanceId();
        String businessKey = this.processInstanceService.getProcessInstanceById(processInstanceId).getBusinessKey();
        User handler = this.userService.findById(Long.valueOf(handlerId));
        LeaveTaskLog leaveTaskLog = new LeaveTaskLog();
        leaveTaskLog.setHandler(handler);
        Date handleTime = new Date();
        leaveTaskLog.setHandleTime(handleTime);
        Leave leave = this.leaveService.findById(Long.valueOf(businessKey));
        leaveTaskLog.setLeave(leave);
        leaveTaskLog.setTaskName(taskName);
        String result = "";
        //??????????????????
        String noticeUser = variables.get("INITIATOR").toString();
        switch (taskKey) {
            case "firstApprovalLeave":
                 //??????firstApprovalState???????????????
                if (variables.containsKey("firstApprovalState")) {
                    if (variables.get("firstApprovalState").toString().equals("0")) {
                        result += "??????????????????";
                    } else {
                        result += "??????????????????????????????????????????";
                        noticeUser = variables.get("secondApprovalId").toString();
                    }
                }
                if (variables.containsKey("firstApprovalContent")) {
                    result += "???????????????" + variables.get("firstApprovalContent") + "???";
                }
                //TODO ??????Leave??????????????????
                leave.setState(LeaveState.DOING);
                try {
                    this.leaveService.save(leave);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "secondApprovalLeave":
                if (variables.containsKey("secondApprovalState")) {
                    if (variables.get("secondApprovalState").toString().equals("0")) {
                        result += "???????????????";
                    } else {
                        result += "????????????";
                    }
                }
                if (variables.containsKey("secondApprovalContent")) {
                    result += "???????????????" + variables.get("secondApprovalContent") + "???";
                }
                break;
            case "updateLeave":
                if (variables.containsKey("replayState")) {
                    if (variables.get("replayState").toString().equals("0")) {
                        result += "????????????";
                    } else {
                        noticeUser = variables.get("firstApprovalId").toString();
                        try {
                            Map<String, Object> plainLeave = (Map) variables.get("leave");
                            leave.setPlanEndTime(SproutDateUtils.parseDate(plainLeave.get("planEndTime").toString(), "yyyy-MM-dd"));
                            leave.setPlanStartTime(SproutDateUtils.parseDate(plainLeave.get("planStartTime").toString(), "yyyy-MM-dd"));
                            leave.setBackTime(handleTime);
                            this.leaveService.save(leave);
                            result += "??????????????????????????????????????????";
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case "backLeave":
                result += "????????????,???????????????";
                //TODO ??????Leave??????????????????
                leave.setState(LeaveState.DONE);
                //?????????????????????
                try {
                    Map<String, Object> plainLeave = (Map) variables.get("leave");
                    leave.setRealEndTime(SproutDateUtils.parseDate(plainLeave.get("realEndTime").toString(), "yyyy-MM-dd"));
                    leave.setRealStartTime(SproutDateUtils.parseDate(plainLeave.get("realStartTime").toString(), "yyyy-MM-dd"));
                    leave.setBackTime(handleTime);
                    leave.setContent(plainLeave.get("content").toString());
                    this.leaveService.save(leave);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                //TODO ????????????
        }
        leaveTaskLog.setResult(result);
        try {
            this.leaveTaskLogService.save(leaveTaskLog);
            logger.debug("?????????????????????{}???", leaveTaskLog);
            SpringContextUtils.getBean(MessageSender.class).sendMessage(SproutJsonUtils.writeToString(leaveTaskLog));
        } catch (Exception e) {
            logger.error("????????????????????????", e);
        }

        if (!handlerId.equals(variables.get("INITIATOR"))) {
            //??????????????????????????????
            SpringContextUtils.getBean(WebSocketServer.class).sendMessageToName(new NoticeMessage<>(leaveTaskLog, NoticeType.NOTICE), noticeUser);
        } else {
            //????????????????????????????????????
            SpringContextUtils.getBean(WebSocketServer.class).sendMessageToName(new NoticeMessage<>(leaveTaskLog, NoticeType.TODO), noticeUser);
        }
    }
}
