package com.sprout.oa.leave.listener;

import com.sprout.common.util.SproutDateUtils;
import com.sprout.common.util.SproutJsonUtils;
import com.sprout.core.spring.SpringContextUtils;
import com.sprout.dlyy.message.MessageSender;
import com.sprout.flowable.service.ProcessInstanceService;
import com.sprout.oa.leave.entity.Leave;
import com.sprout.oa.leave.entity.LeaveStatistic;
import com.sprout.oa.leave.entity.LeaveTaskLog;
import com.sprout.oa.leave.service.LeaveService;
import com.sprout.oa.leave.service.LeaveStatisticService;
import com.sprout.oa.leave.service.LeaveTaskLogService;
import com.sprout.oa.leave.util.LeaveState;
import com.sprout.system.entity.User;
import com.sprout.system.service.UserService;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.engine.delegate.event.impl.FlowableEntityWithVariablesEventImpl;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

@Component
public class LeaveTaskCompleteListener implements FlowableEventListener {

    private static Logger logger = LoggerFactory.getLogger(LeaveTaskCompleteListener.class);

    private ProcessInstanceService processInstanceService;

    private LeaveTaskLogService leaveTaskLogService;

    private UserService userService;

    private LeaveService leaveService;

    private LeaveStatisticService leaveStatisticService;

    public LeaveTaskCompleteListener(ProcessInstanceService processInstanceService, LeaveTaskLogService leaveTaskLogService, UserService userService, LeaveService leaveService, LeaveStatisticService leaveStatisticService) {
        this.processInstanceService = processInstanceService;
        this.leaveTaskLogService = leaveTaskLogService;
        this.userService = userService;
        this.leaveService = leaveService;
        this.leaveStatisticService = leaveStatisticService;
    }

    @Transactional
    @Override
    public void onEvent(FlowableEvent event) {
        if (event.getType().name().equals("TASK_COMPLETED")) {
            FlowableEntityWithVariablesEventImpl ent = (FlowableEntityWithVariablesEventImpl) event;
            TaskEntityImpl taskEntity = (TaskEntityImpl) ent.getEntity();
            Map<String, Object> variables = ent.getVariables();
            String assignee = taskEntity.getAssignee();
            String businessKey = this.processInstanceService.getProcessInstanceById(ent.getProcessInstanceId()).getBusinessKey();
            User handler = this.userService.findById(Long.valueOf(assignee));
            LeaveTaskLog leaveTaskLog = new LeaveTaskLog();
            leaveTaskLog.setHandler(handler);
            Date handleTime = new Date();
            leaveTaskLog.setHandleTime(handleTime);
            Leave leave = this.leaveService.findById(Long.valueOf(businessKey));
            leaveTaskLog.setLeave(leave);
            leaveTaskLog.setTaskName(taskEntity.getName());
            String result = "";
            switch (taskEntity.getTaskDefinitionKey()) {
                case "firstApproval":
                    //??????firstApprovalState???????????????
                    if (variables.containsKey("firstApprovalState")) {
                        if (variables.get("firstApprovalState").toString().equals("0")) {
                            result += "??????????????????";
                        } else if (variables.get("firstApprovalState").toString().equals("2")){
                            result += "????????????????????????????????????????????? ";
                        } else {
                            result += "?????????????????????????????????";
                        }
                    }
                    if (variables.containsKey("firstApprovalContent")) {
                        result += " ???????????????" + variables.get("firstApprovalContent") + "???";
                    }
                    //TODO ??????Leave??????????????????
                    leave.setState(LeaveState.DOING);
                    try {
                        this.leaveService.save(leave);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "secondApproval":
                    if (variables.containsKey("secondApprovalState")) {
                        if (variables.get("secondApprovalState").toString().equals("0")) {
                            result += "??????????????????";
                        } else if (variables.get("secondApprovalState").toString().equals("2")){
                            result += "???????????????????????????????????? ";
                        } else {
                            result += "?????????????????????????????????";
                        }
                    }
                    if (variables.containsKey("secondApprovalContent")) {
                        result += " ???????????????" + variables.get("secondApprovalContent") + "???";
                    }
                    break;
                case "thirdApproval":
                    if (variables.containsKey("thirdApprovalState")) {
                        if (variables.get("thirdApprovalState").toString().equals("0")) {
                            result += "??????????????????";
                        } else {
                            result += "?????????????????????????????????";
                        }
                    }
                    if (variables.containsKey("thirdApprovalContent")) {
                        result += "???????????????" + variables.get("thirdApprovalContent") + "???";
                    }
                    break;
                case "fourthApproval":
                    if (variables.containsKey("fourthApprovalState")) {
                        if (variables.get("fourthApprovalState").toString().equals("0")) {
                            result += "??????????????????";
                        } else {
                            result += "?????????????????????????????????";
                        }
                    }
                    if (variables.containsKey("fourthApprovalContent")) {
                        result += "???????????????" + variables.get("fourthApprovalContent") + "???";
                    }
                    break;
                case "updateLeave":
                    if (variables.containsKey("updateState")) {
                        try {
                        if (variables.get("updateState").toString().equals("0")) {
                            result += "????????????";
                            leave.setState(LeaveState.CANCEL);
                        } else {
                            //??????????????????
                            leave.setPlanStartTime(SproutDateUtils.parseDate(variables.get("leave.planStartTime").toString(), "yyyy-MM-dd"));
                            leave.setPlanEndTime(SproutDateUtils.parseDate(variables.get("leave.planEndTime").toString(), "yyyy-MM-dd"));
                            leave.setPlanStartFlag(Integer.parseInt(variables.get("leave.planStartFlag").toString()));
                            leave.setPlanEndFlag(Integer.parseInt(variables.get("leave.planEndFlag").toString()));
                            leave.setContent(variables.get("leave.content").toString());
                            result += "????????????????????????????????????";
                        }
                            this.leaveService.save(leave);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
                case "reportLeave":
                    result += "????????????,????????????";
                    leave.setState(LeaveState.DONE);
                    //?????????????????????
                    try {
                        leave.setRealEndTime(SproutDateUtils.parseDate(variables.get("leave.realEndTime").toString(), "yyyy-MM-dd"));
                        leave.setRealStartTime(SproutDateUtils.parseDate(variables.get("leave.realStartTime").toString(), "yyyy-MM-dd"));
                        leave.setRealStartFlag(Integer.parseInt(variables.get("leave.realStartFlag").toString()));
                        leave.setRealEndFlag(Integer.parseInt(variables.get("leave.realEndFlag").toString()));
                        leave.setBackTime(handleTime);
                        this.leaveService.save(leave);
                        //??????????????????????????????
                        leaveStatisticService.saveLeaveStatistic(leave);
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
                //SpringContextUtils.getBean(MessageSender.class).sendMessage(SproutJsonUtils.writeToString(leaveTaskLog));
            } catch (Exception e) {
                logger.error("????????????????????????", e);
            }

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
