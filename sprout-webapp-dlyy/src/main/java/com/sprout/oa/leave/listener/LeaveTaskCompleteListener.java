package com.sprout.oa.leave.listener;

import com.sprout.common.util.SproutDateUtils;
import com.sprout.flowable.service.ProcessInstanceService;
import com.sprout.oa.leave.entity.Leave;
import com.sprout.oa.leave.entity.LeaveTaskLog;
import com.sprout.oa.leave.service.LeaveService;
import com.sprout.oa.leave.service.LeaveTaskLogService;
import com.sprout.system.entity.User;
import com.sprout.system.service.UserService;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

@Component
public class LeaveTaskCompleteListener implements TaskListener {

    private static Logger logger = LoggerFactory.getLogger(LeaveTaskCompleteListener.class);

    private ProcessInstanceService processInstanceService;

    private LeaveTaskLogService leaveTaskLogService;

    private UserService userService;

    private LeaveService leaveService;

    public LeaveTaskCompleteListener(ProcessInstanceService processInstanceService, LeaveTaskLogService leaveTaskLogService, UserService userService, LeaveService leaveService) {
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
        //设置处理结果
        switch (taskKey) {
            case "firstApprovalLeave":
                 //根据firstApprovalState状态来判断
                if (variables.containsKey("firstApprovalState")) {
                    if (variables.get("firstApprovalState").toString().equals("0")) {
                        result += "审核不通过";
                    } else {
                        result += "审核通过，提交部门经理审核";
                    }
                }
                if (variables.containsKey("firstApprovalContent")) {
                    result += "审核内容【" + variables.get("firstApprovalContent") + "】";
                }
                //TODO 设置Leave状态为办理中
                leave.setState(1);
                try {
                    this.leaveService.save(leave);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "secondApprovalLeave":
                if (variables.containsKey("secondApprovalState")) {
                    if (variables.get("secondApprovalState").toString().equals("0")) {
                        result += "审核不通过";
                    } else {
                        result += "审核通过";
                    }
                }
                if (variables.containsKey("secondApprovalContent")) {
                    result += "审核内容【" + variables.get("secondApprovalContent") + "】";
                }
                break;
            case "updateLeave":
                if (variables.containsKey("replayState")) {
                    if (variables.get("replayState").toString().equals("0")) {
                        result += "放弃请假";
                    } else {
                        try {
                            Map<String, Object> plainLeave = (Map) variables.get("leave");
                            leave.setPlanEndTime(SproutDateUtils.parseDate(plainLeave.get("planEndTime").toString(), "yyyy-MM-dd"));
                            leave.setPlanStartTime(SproutDateUtils.parseDate(plainLeave.get("planStartTime").toString(), "yyyy-MM-dd"));
                            leave.setBackTime(handleTime);
                            this.leaveService.save(leave);
                            result += "修改请假单，重新提交主管审核";
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case "backLeave":
                result += "完成销假,流程已结束";
                //TODO 设置Leave状态为已结束
                leave.setState(2);
                //获取修改的信息
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
                //TODO 暂未处理
        }
        leaveTaskLog.setResult(result);
        try {
            this.leaveTaskLogService.save(leaveTaskLog);
            logger.debug("保存流程记录【{}】", leaveTaskLog);
        } catch (Exception e) {
            logger.error("保存流程记录失败", e);
        }

    }
}
