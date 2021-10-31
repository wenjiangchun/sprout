package com.sprout.oa.leave.web.controller;

import com.sprout.core.spring.SpringContextUtils;
import com.sprout.flowable.service.ProcessInstanceService;
import com.sprout.oa.leave.entity.Leave;
import com.sprout.oa.leave.entity.LeaveTaskLog;
import com.sprout.oa.leave.flow.LeaveFlowVariable;
import com.sprout.oa.leave.service.LeaveService;
import com.sprout.oa.leave.service.LeaveTaskLogService;
import com.sprout.oa.leave.util.LeaveState;
import com.sprout.oa.util.UserHelper;
import com.sprout.shiro.ShiroUser;
import com.sprout.shiro.util.ShiroUtils;
import com.sprout.system.entity.Group;
import com.sprout.system.entity.User;
import com.sprout.system.service.DictService;
import com.sprout.system.service.GroupService;
import com.sprout.system.service.UserService;
import com.sprout.web.base.BaseCrudController;
import com.sprout.web.util.RestResult;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping(value = "/oa/leave")
public class LeaveController extends BaseCrudController<Leave, Long> {

    private LeaveService leaveService;

    private LeaveTaskLogService leaveTaskLogService;

    public LeaveController(LeaveService leaveService,LeaveTaskLogService leaveTaskLogService) {
        super("oa", "leave", "请假", leaveService);
        this.leaveService = leaveService;
        this.leaveTaskLogService = leaveTaskLogService;
    }

    @Override
    protected void setPageQueryVariables(Map<String, Object> queryVariables, HttpServletRequest request) {

    }

    @Override
    protected void setModel(Model model, HttpServletRequest request) {
        super.setModel(model, request);
    }

    @GetMapping("/applyLeave")
    public String applyLeave(Model model) {
        ShiroUser currentUser = ShiroUtils.getCurrentUser();
        model.addAttribute("applier", ShiroUtils.getCurrentUser());
        model.addAttribute("applyTypeList", this.leaveService.getLeaveTypeList(SpringContextUtils.getBean(DictService.class)));
        assert currentUser != null;
        //获取用户级别信息
        int userLevel = UserHelper.getUserLevel(Long.valueOf(currentUser.userId));
        //判断用户是哪个级别 普通员工还是经理董事长级别
        if (userLevel == 1) { //普通员工
            model.addAttribute("userLevel", 1);
            model.addAttribute("userList", UserHelper.getDeptManagerList(Long.valueOf(currentUser.userId), true));
        } else { //部门经理及以上
            model.addAttribute("userLevel", 2);
            model.addAttribute("userList", UserHelper.getChairmanManagerList(Long.valueOf(currentUser.userId)));
        }
        User user = SpringContextUtils.getBean(UserService.class).findById(Long.valueOf(currentUser.userId));
        model.addAttribute("holiday", UserHelper.getTotalHoliday(user, new Date()));
        return "/oa/leave/applyLeave";
    }


    /**
     * 发起请假流程申请
     * @param leave 请假信息
     * @param variable 流程变量
     * @return RestResult
     */
    @PostMapping(value = "saveAndStartWorkflow")
    @ResponseBody
    public RestResult saveAndStartWorkflow(Leave leave, LeaveFlowVariable variable) {
        leave.setState(LeaveState.UNDO);
        variable.getFlowVariables().put(ProcessInstanceService.INITIATOR, leave.getApplier().getId());
        try {
            //判断是否存在未完成的请假信息
            List<Leave> existLeaveList = this.leaveService.getUnFinishedLeaveList(leave.getApplier().getId());
            if (!existLeaveList.isEmpty()) {
                StringBuilder errorMessage = new StringBuilder("存在进行中的请假信息，请先完成之前请假信息:");
                for (Leave lv : existLeaveList) {
                    errorMessage.append("【请假单ID=").append(lv.getId()).append(lv.getLeaveType().getName()).append("】");
                }
                return RestResult.createErrorResult(errorMessage.toString());
            } else {
                //判断请假时间是否冲突 如果冲突则本次请假不能申请
                boolean checkApplyTime = this.leaveService.checkApplyTime(leave);
                if (checkApplyTime) {
                    ProcessInstance processInstance = this.leaveService.startWorkflow(leave, variable.getFlowVariables());
                    processInstance.getStartUserId();
                    return RestResult.createSuccessResult("申请已发起,流程已成功启动");
                } else {
                    return RestResult.createErrorResult("请假日期和已有记录冲突！");
                }
            }
        } catch (Exception ex) {
            logger.error("请假申请失败", ex);
            return RestResult.createErrorResult("申请失败,请重试", ex.getMessage());
        }
    }

    @GetMapping("/getMyLeave")
    public String getMyLeave(Model model) {
        ShiroUser currentUser = ShiroUtils.getCurrentUser();
        List<Leave> leaveList = new ArrayList<>();
        if (Objects.nonNull(currentUser)) {
            leaveList = this.leaveService.findByProperty("applier", currentUser.getUser());
        }
        model.addAttribute("leaveList", leaveList);
        return "/oa/leave/myLeaveList";
    }

    @GetMapping("/todoView")
    public String todoView(Model model) {
        //获取当前用户信息
        ShiroUser currentUser = ShiroUtils.getCurrentUser();
        //根据用户查询用户代办事项
        List<Leave> todoList = this.leaveService.getTodoList(Long.valueOf(currentUser.userId));
        model.addAttribute("todoList", todoList);
        return "oa/leave/todoList";
    }

    @GetMapping("/showTask/{taskId}")
    public String showTask(Model model, @PathVariable String taskId) {
        //根据用户查询用户代办事项
        Leave taskLeave = this.leaveService.getLeaveByTaskId(taskId);
        model.addAttribute("taskLeave", taskLeave);
        String taskKey = taskLeave.getCurrentTask().getTaskDefinitionKey();
        //TODO 计算本次请假审核级别
        int approvalLevel = this.leaveService.getApprovalLevel(taskLeave.getId());
        model.addAttribute("approvalLevel", approvalLevel);
        //查询办理记录
        List<LeaveTaskLog> leaveTaskLogList = this.leaveTaskLogService.findByLeaveId(taskLeave.getId());
        model.addAttribute("leaveTaskLogList", leaveTaskLogList);
        model.addAttribute("recentLeaveList", this.leaveService.getRecentLeave(taskLeave.getApplier().getId()));
        return "oa/leave/" + taskKey;
    }

    /**
     * 办理流程
     */
    @PostMapping(value = "handleLeave")
    @ResponseBody
    public RestResult handleLeave(LeaveFlowVariable leaveFlowVariable, String taskId) {
        try {
            this.leaveService.handleLeave(leaveFlowVariable, taskId);
            return RestResult.createSuccessResult("办理成功");
        } catch (Exception ex) {
            ex.printStackTrace();
            return RestResult.createErrorResult("办理失败," + ex.getMessage());
        }

    }

    @GetMapping("/editLeave/{id}")
    public String editLeave(Model model, @PathVariable Long id) {
        Leave leave = this.leaveService.findById(id);
        model.addAttribute("leave", leave);
        model.addAttribute("applyTypeList", this.leaveService.getLeaveTypeList(SpringContextUtils.getBean(DictService.class)));
        return "oa/leave/editLeave";
    }

    /**
     * 编辑请假信息，如果一个流程已发起但未进入办理中，则可以执行该请假编辑
     * @param leave 请假信息
     * @return 编辑结果
     */
    @PostMapping(value = "updateLeave")
    @ResponseBody
    public RestResult updateLeave(Leave leave) {
        try {
            Leave existLeave = this.leaveService.findById(leave.getId());
            existLeave.setContent(leave.getContent());
            existLeave.setPlanEndTime(leave.getPlanEndTime());
            existLeave.setPlanStartTime(leave.getPlanStartTime());
            this.leaveService.save(existLeave);
            return RestResult.createSuccessResult("编辑成功");
        } catch (Exception ex) {
            ex.printStackTrace();
            return RestResult.createErrorResult("编辑失败," + ex.getMessage());
        }
    }

    @GetMapping("/showLeave/{id}")
    public String showLeave(Model model, @PathVariable Long id) {
        //根据用户查询用户代办事项
        Leave leave = this.leaveService.findById(id);
        model.addAttribute("leave", leave);
        //查询办理记录
        List<LeaveTaskLog> leaveTaskLogList = this.leaveTaskLogService.findByLeaveId(id);
        model.addAttribute("leaveTaskLogList", leaveTaskLogList);
        return "oa/leave/showLeave";
    }

    @GetMapping("/getTodoList/{userId}")
    @ResponseBody
    public List<Leave> getTodoList(@PathVariable Long userId) {
        List<Leave> leaveList =  this.leaveService.getTodoList(userId);
        leaveList.forEach(leave -> {
            Task task = leave.getCurrentTask();
            Map<String, Object> runtimeVariables = new HashMap<>();
            runtimeVariables.put("taskName", task.getName());
            runtimeVariables.put("taskTime", task.getCreateTime());
            leave.setRuntimeVariables(runtimeVariables);
            leave.setCurrentTask(null);
            leave.setProcessInstance(null);
        });
        return leaveList;
    }


    @GetMapping("/getDeputyManagerList")
    @ResponseBody
    public List<User> getDeputyManagerList() {
        return UserHelper.getDeputyManagerList(Long.valueOf(Objects.requireNonNull(ShiroUtils.getCurrentUser()).userId));
    }

    @GetMapping("/getGeneralManagerList")
    @ResponseBody
    public List<User> getGeneralManagerList() {
        return UserHelper.getGeneralManagerList(Long.valueOf(Objects.requireNonNull(ShiroUtils.getCurrentUser()).userId));
    }

    @GetMapping("/getChairmanManagerList")
    @ResponseBody
    public List<User> getChairmanManagerList() {
        return UserHelper.getChairmanManagerList(Long.valueOf(Objects.requireNonNull(ShiroUtils.getCurrentUser()).userId));
    }
}