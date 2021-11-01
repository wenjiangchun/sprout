package com.sprout.oa.leave.service;

import com.sprout.common.util.SproutDateUtils;
import com.sprout.core.service.AbstractBaseService;
import com.sprout.core.spring.SpringContextUtils;
import com.sprout.flowable.service.ProcessInstanceService;
import com.sprout.oa.leave.dao.LeaveDao;
import com.sprout.oa.leave.dao.LeaveTaskLogDao;
import com.sprout.oa.leave.entity.Leave;
import com.sprout.oa.leave.entity.LeaveTaskLog;
import com.sprout.oa.leave.flow.LeaveFlowVariable;
import com.sprout.oa.leave.util.DaySection;
import com.sprout.oa.leave.util.LeaveState;
import com.sprout.oa.notice.NoticeMessage;
import com.sprout.oa.notice.NoticeType;
import com.sprout.system.entity.Dict;
import com.sprout.system.entity.Group;
import com.sprout.system.entity.User;
import com.sprout.system.service.DictService;
import com.sprout.system.service.UserService;
import com.sprout.system.utils.Status;
import com.sprout.web.websocket.WebSocketServer;
import com.sprout.work.entity.Holiday;
import com.sprout.work.service.HolidayService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class LeaveService extends AbstractBaseService<Leave, Long> {

    private static final String DICT_LEAVE_TYPE = "LEAVE_TYPE";

    private static final String PROCESS_KEY = "leaveProcess";

    private LeaveDao leaveDao;

    private LeaveTaskLogDao leaveTaskLogDao;

    private ProcessInstanceService processInstanceService;

    private HolidayService holidayService;

    public LeaveService(LeaveDao leaveDao, ProcessInstanceService processInstanceService, LeaveTaskLogDao leaveTaskLogDao, HolidayService holidayService) {
        super(leaveDao);
        this.leaveDao = leaveDao;
        this.processInstanceService = processInstanceService;
        this.leaveTaskLogDao = leaveTaskLogDao;
        this.holidayService = holidayService;
    }

    @Transactional(readOnly = true)
    public List<Dict> getLeaveTypeList(DictService dictService) {
        return dictService.findChildsByRootCode(DICT_LEAVE_TYPE);
    }

    public ProcessInstance startWorkflow(Leave leave, Map<String, Object> variables) throws Exception {
        this.save(leave);
        String businessKey = leave.getId().toString();
        ProcessInstance processInstance = processInstanceService.startProcessInstanceByKey(PROCESS_KEY, businessKey, variables);
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
        //SpringContextUtils.getBean(WebSocketServer.class).sendMessageToName(new NoticeMessage<>(leaveTaskLog, NoticeType.TODO), variables.get("firstApprovalId").toString());
        return processInstance;
    }

    /**
     * 获取指定用户待办请假信息
     * @param userId 用户ID
     * @return 返回正在执行流程中的用户待办请假列表
     */
    @Transactional(readOnly = true)
    public List<Leave> getTodoList(Long userId) {
        List<Task> todoTaskList = this.processInstanceService.getTodoList(userId.toString(), PROCESS_KEY);
        List<Leave> leaveList = new ArrayList<>();
        todoTaskList.forEach(task -> {
            ProcessInstance processInstance = this.processInstanceService.getProcessInstanceById(task.getProcessInstanceId());
            String businessKey = processInstance.getBusinessKey();
            Leave leave = this.findById(Long.valueOf(businessKey));
            leave.setProcessInstance(processInstance);
            leave.setCurrentTask(task);
            leaveList.add(leave);
        });
        return leaveList;
    }

    @Transactional(readOnly = true)
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
    public void handleLeave(LeaveFlowVariable flowVariable, String taskId) {
        Map<String, Object> runtimeVariables = this.processInstanceService.getRuntimeVariablesByTaskId(taskId);
        for (String name : flowVariable.getFlowVariables().keySet()) {
            if (Objects.nonNull(flowVariable.getFlowVariables().get(name))) {
                runtimeVariables.put(name, flowVariable.getFlowVariables().get(name));
            }
        }
        this.processInstanceService.complete(taskId, runtimeVariables);
    }

    /**
     * 获取用户3个月的请假记录
     * @param userId 用户ID
     * @return 请假结果列表
     */
    public List<Leave> getRecentLeave(Long userId) {
        Date endDay = new Date();
        Date startDay = SproutDateUtils.addMonths(endDay, -3);
        return this.leaveDao.findLeaveListByApplierId(userId, startDay, endDay);
    }


    /**
     * 计算请假提交审核级别
     * @param leaveId 请假ID
     * @return [1代表部门经理审批即可，2代表需要提交主管副经理，3代表需要提交总经理]
     */
    public int getApprovalLevel(Long leaveId) {
        Leave leave = this.findById(leaveId);
        //计算本次请假天数 排除节假日
        float days = getLeaveDays(leave);
        if (days > 3) {
            return 3;
        } else {
            //判断当月累计请假量
            int year = SproutDateUtils.getYear(leave.getPlanStartTime());
            int month = SproutDateUtils.getMonth(leave.getPlanStartTime());
            float leavedDays = SpringContextUtils.getBean(LeaveStatisticService.class).getTotalDays(leave.getApplier().getId(), year, month);
            if (leavedDays > 3 || (leavedDays + days)  > 3) {
                return 3;
            } else {
                if (days <=1) {
                    return 1;
                } else {
                    return 2;
                }
            }
        }
    }

    /**
     * 计算请假日期之间天数 半天用0.5代替并排除节假日
     * @param leave 请假信息
     * @return 请假天数
     */
    public float getLeaveDays(Leave leave) {
        Date startTime = leave.getPlanStartTime();
        Date endTime = leave.getPlanEndTime();
        //计算两个日期时间间隔 排除节假日
        List<Holiday> holidayList = this.holidayService.getHolidayList(SproutDateUtils.format(startTime, SproutDateUtils.DAY_PATTERN), SproutDateUtils.format(endTime, SproutDateUtils.DAY_PATTERN));
        float days = 0;
        if (SproutDateUtils.isSameDay(startTime, endTime)) { //起止时间是同一天
            if (holidayList.isEmpty()) { //请假当天为节假日 直接返回0
                if (leave.getPlanStartFlag() == DaySection.ALL.getSection() || leave.getPlanEndFlag() == DaySection.ALL.getSection()) {
                    days = 1;
                } else if (leave.getPlanStartFlag() == DaySection.AM.getSection() && leave.getPlanEndFlag() == DaySection.PM.getSection()) {
                    days = 1;
                } else {
                    days = 0.5f;
                }
            }
        } else { //起止时间不为同一天
            //判断开始日期是否位于节假日
            boolean flag = true;
            while(flag) {
                if (startTime.after(endTime)) {
                    break;
                }
                if (!this.holidayService.existHoliday(SproutDateUtils.format(startTime, SproutDateUtils.DAY_PATTERN), holidayList)) {
                    if (leave.getPlanStartFlag() == DaySection.ALL.getSection()) {
                        days += 1;
                    } else {
                        days += 0.5f;
                    }
                }
                startTime = SproutDateUtils.addDays(startTime, 1);
                if (SproutDateUtils.isSameDay(startTime, endTime)) {
                    if (!this.holidayService.existHoliday(SproutDateUtils.format(endTime, SproutDateUtils.DAY_PATTERN), holidayList)) {
                        if (leave.getPlanEndFlag() == DaySection.ALL.getSection()) {
                            days += 1;
                        } else {
                            days += 0.5f;
                        }
                    }
                    flag = false;
                }
            }
        }
        return days;
    }

    /**
     * 查询用户未开始或进行中的请假信息，排除已完成和已放弃请假信息
     * @param userId 用户信息
     * @return 包含未开始或进行中的请假信息
     */
    @Transactional(readOnly = true)
    public List<Leave> getUnFinishedLeaveList(Long userId) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("userId", userId);
        queryParams.put("undoState", LeaveState.UNDO);
        queryParams.put("doingState", LeaveState.DOING);
        return this.leaveDao.findByJql("from Leave lv where lv.state=:undoState or lv.state=:doingState and lv.applier.id=:userId", queryParams);
    }

    /**
     * 判断请假申请时间是否和已有请假记录实际请假时间有冲突
     * @param leave 请假申请
     * @return 请假申请时间和已有记录有冲突返回false 否则返回true
     */
    @Transactional(readOnly = true)
    public boolean checkApplyTime(Leave leave) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("userId", leave.getApplier().getId());
        //TODO 对比日期是否重复 后续未加入半天判断
        queryParams.put("startTime", leave.getPlanStartTime());
        queryParams.put("endTime", leave.getPlanEndTime());
        queryParams.put("leaveState", LeaveState.DONE);
        List<Leave> leaveList = this.leaveDao.findByJql("from Leave lv where ((lv.realStartTime <=:startTime and lv.realEndTime >=:endTime) or (lv.realStartTime <=:endTime and lv.realEndTime >=:endTime)) and lv.applier.id=:userId and state=:leaveState", queryParams);
        return leaveList.isEmpty();
    }
}
