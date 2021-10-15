package com.sprout.oa.leave.service;

import com.sprout.core.service.AbstractBaseService;
import com.sprout.flowable.service.ProcessInstanceService;
import com.sprout.oa.leave.dao.LeaveDao;
import com.sprout.oa.leave.dao.LeaveTaskLogDao;
import com.sprout.oa.leave.entity.Leave;
import com.sprout.oa.leave.entity.LeaveTaskLog;
import com.sprout.system.entity.Dict;
import com.sprout.system.entity.User;
import com.sprout.system.service.DictService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class LeaveService extends AbstractBaseService<Leave, Long> {

    private static final String DICT_LEAVE_TYPE = "LEAVE_TYPE";

    private static final String PROCESS_KEY = "leaveProcess1";

    private LeaveDao leaveDao;

    private LeaveTaskLogDao leaveTaskLogDao;

    private ProcessInstanceService processInstanceService;

    public LeaveService(LeaveDao leaveDao, ProcessInstanceService processInstanceService, LeaveTaskLogDao leaveTaskLogDao) {
        super(leaveDao);
        this.leaveDao = leaveDao;
        this.processInstanceService = processInstanceService;
        this.leaveTaskLogDao = leaveTaskLogDao;
    }

    @Transactional(readOnly = true)
    public List<Dict> getLeaveTypeList(DictService dictService) {
        return dictService.findChildsByRootCode(DICT_LEAVE_TYPE);
    }

    public ProcessInstance startWorkflow(Leave leave, Map<String, Object> variables) throws Exception {
        this.save(leave);
        String businessKey = leave.getId().toString();
        ProcessInstance processInstance = processInstanceService.startProcessInstanceByKey("leaveProcess1", businessKey, variables);
        processInstance.getProcessVariables();
        String processInstanceId = processInstance.getId();
        leave.setProcessInstanceId(processInstanceId);
        leave.setApplyTime(processInstance.getStartTime());
        this.save(leave);
        logger.debug("start process of {key={}, bkey={}, pid={}, variables={}}", "leave", businessKey, processInstanceId, variables);
        //保存leaveTaskLog
        LeaveTaskLog leaveTaskLog = new LeaveTaskLog();
        leaveTaskLog.setLeave(leave);
        leaveTaskLog.setTaskName("提交申请");
        leaveTaskLog.setResult("已发起申请");
        leaveTaskLog.setHandler(leave.getApplier());
        leaveTaskLog.setHandleTime(processInstance.getStartTime());
        leaveTaskLogDao.save(leaveTaskLog);
        return processInstance;
    }

    /**
     * 获取指定用户待办请假信息
     * @param user 用户信息
     * @return 返回正在执行流程中的用户待办请假列表
     */
    public List<Leave> getTodoList(User user) {
        List<Task> todoTaskList = this.processInstanceService.getTodoList(user.getId().toString(), PROCESS_KEY);
        List<Leave> leaveList = new ArrayList<>();
        todoTaskList.forEach(task -> {
            ProcessInstance processInstance = this.processInstanceService.getProcessInstanceById(task.getProcessInstanceId());
            String businessKey = processInstance.getBusinessKey();
            Leave leave = this.leaveDao.getById(Long.valueOf(businessKey));
            leave.setProcessInstance(processInstance);
            leave.setCurrentTask(task);
            leaveList.add(leave);
        });
        return leaveList;
    }

    public Leave getLeaveByTaskId(String taskId) {
        Task task = this.processInstanceService.getTaskById(taskId);
        ProcessInstance processInstance = this.processInstanceService.getProcessInstanceById(task.getProcessInstanceId());
        Map<String, Object> runtimeVariables = this.processInstanceService.getRuntimeVariablesByTaskId(taskId);
        String businessKey = processInstance.getBusinessKey();
        Leave leave = this.leaveDao.getById(Long.valueOf(businessKey));
        leave.setProcessInstance(processInstance);
        leave.setCurrentTask(task);
        leave.setRuntimeVariables(runtimeVariables);
        return leave;
    }

    @Transactional
    public void handleLeave(Map<String, Object> flowVariable, String taskId) {
        Map<String, Object> runtimeVariables = this.processInstanceService.getRuntimeVariablesByTaskId(taskId);
        for (String name : flowVariable.keySet()) {
            if (Objects.nonNull(flowVariable.get(name))) {
                runtimeVariables.put(name, flowVariable.get(name));
            }
        }
        this.processInstanceService.complete(taskId, runtimeVariables);
    }

    public String getInitiatorName() {
        return ProcessInstanceService.INITIATOR;
    }
}
