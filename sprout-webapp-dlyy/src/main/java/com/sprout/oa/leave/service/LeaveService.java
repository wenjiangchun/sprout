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
        //??????leaveTaskLog
        LeaveTaskLog leaveTaskLog = new LeaveTaskLog();
        leaveTaskLog.setLeave(leave);
        leaveTaskLog.setTaskName("????????????");
        leaveTaskLog.setResult("???????????????");
        leaveTaskLog.setHandler(leave.getApplier());
        leaveTaskLog.setHandleTime(processInstance.getStartTime());
        leaveTaskLogDao.save(leaveTaskLog);
        //SpringContextUtils.getBean(WebSocketServer.class).sendMessageToName(new NoticeMessage<>(leaveTaskLog, NoticeType.TODO), variables.get("firstApprovalId").toString());
        return processInstance;
    }

    /**
     * ????????????????????????????????????
     * @param userId ??????ID
     * @return ??????????????????????????????????????????????????????
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
     * ????????????3?????????????????????
     * @param userId ??????ID
     * @return ??????????????????
     */
    public List<Leave> getRecentLeave(Long userId) {
        Date endDay = new Date();
        Date startDay = SproutDateUtils.addMonths(endDay, -3);
        return this.leaveDao.findLeaveListByApplierId(userId, startDay, endDay);
    }


    /**
     * ??????????????????????????????
     * @param leaveId ??????ID
     * @return [1?????????????????????????????????2????????????????????????????????????3???????????????????????????]
     */
    public int getApprovalLevel(Long leaveId) {
        Leave leave = this.findById(leaveId);
        //???????????????????????? ???????????????
        float days = getLeaveDays(leave);
        if (days >= 3) {
            return 3;
        } else {
            //???????????????????????????
            int year = SproutDateUtils.getYear(leave.getPlanStartTime());
            int month = SproutDateUtils.getMonth(leave.getPlanStartTime());
            float leavedDays = SpringContextUtils.getBean(LeaveStatisticService.class).getTotalDays(leave.getApplier().getId(), year, month);
            if (leavedDays >= 3 || (leavedDays + days)  >= 3) {
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
     * ?????????????????????????????? ?????????0.5????????????????????????
     * @param leave ????????????
     * @return ????????????
     */
    public float getLeaveDays(Leave leave) {
        Date startTime = leave.getPlanStartTime();
        Date endTime = leave.getPlanEndTime();
        //?????????????????????????????? ???????????????
        List<Holiday> holidayList = this.holidayService.getHolidayList(SproutDateUtils.format(startTime, SproutDateUtils.DAY_PATTERN), SproutDateUtils.format(endTime, SproutDateUtils.DAY_PATTERN));
        float days = 0;
        if (SproutDateUtils.isSameDay(startTime, endTime)) { //????????????????????????
            if (holidayList.isEmpty()) { //???????????????????????? ????????????0
                if (leave.getPlanStartFlag() == DaySection.ALL.getSection() || leave.getPlanEndFlag() == DaySection.ALL.getSection()) {
                    days = 1;
                } else if (leave.getPlanStartFlag() == DaySection.AM.getSection() && leave.getPlanEndFlag() == DaySection.PM.getSection()) {
                    days = 1;
                } else {
                    days = 0.5f;
                }
            }
        } else { //???????????????????????????
            //???????????????????????????????????????
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
     * ??????????????????????????????????????????????????????????????????????????????????????????
     * @param userId ????????????
     * @return ??????????????????????????????????????????
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
     * ??????????????????????????????????????????????????????????????????????????????
     * @param leave ????????????
     * @return ????????????????????????????????????????????????false ????????????true
     */
    @Transactional(readOnly = true)
    public boolean checkApplyTime(Leave leave) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("userId", leave.getApplier().getId());
        //TODO ???????????????????????? ???????????????????????????
        queryParams.put("startTime", leave.getPlanStartTime());
        queryParams.put("endTime", leave.getPlanEndTime());
        queryParams.put("leaveState", LeaveState.DONE);
        List<Leave> leaveList = this.leaveDao.findByJql("from Leave lv where ((lv.realStartTime <=:startTime and lv.realEndTime >=:endTime) or (lv.realStartTime <=:endTime and lv.realEndTime >=:endTime)) and lv.applier.id=:userId and state=:leaveState", queryParams);
        return leaveList.isEmpty();
    }
}
