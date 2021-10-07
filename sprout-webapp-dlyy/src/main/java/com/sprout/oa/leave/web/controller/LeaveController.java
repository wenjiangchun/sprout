package com.sprout.oa.leave.web.controller;

import com.sprout.core.spring.SpringContextUtils;
import com.sprout.oa.leave.entity.Leave;
import com.sprout.oa.leave.service.LeaveService;
import com.sprout.shiro.ShiroUser;
import com.sprout.shiro.util.ShiroUtils;
import com.sprout.system.entity.Group;
import com.sprout.system.entity.User;
import com.sprout.system.service.DictService;
import com.sprout.system.service.GroupService;
import com.sprout.web.base.BaseCrudController;
import com.sprout.web.util.RestResult;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping(value = "/oa/leave")
public class LeaveController extends BaseCrudController<Leave, Long> {

    private LeaveService leaveService;

    public LeaveController(LeaveService leaveService) {
        super("oa", "leave", "请假", leaveService);
        this.leaveService = leaveService;
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
        model.addAttribute("applier", ShiroUtils.getCurrentUser());
        model.addAttribute("applyTypeList", this.leaveService.getLeaveTypeList(SpringContextUtils.getBean(DictService.class)));
        //TODO 查询该用户机构下人员信息
        Group group = ShiroUtils.getCurrentUser().getGroup();
        if (Objects.nonNull(group)) {
            Set<User> userSet = SpringContextUtils.getBean(GroupService.class).findById(group.getId()).getUsers();
            userSet.removeIf(u ->
                    u.getId().equals(Long.valueOf(ShiroUtils.getCurrentUser().getUserId()))
            );
            model.addAttribute("userList", userSet);
        } else {
            model.addAttribute("userList", new ArrayList<>());
        }
        return "/oa/leave/applyLeave";
    }


    @PostMapping(value = "saveAndStartWorkflow")
    @ResponseBody
    public RestResult saveAndStartWorkflow(Leave leave, Long firstApprovalId) {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("firstApprovalId", firstApprovalId);
        variables.put("startUserId", leave.getApplier().getId());
        try {
            ProcessInstance processInstance = this.leaveService.startWorkflow(leave, variables);
            processInstance.getStartUserId();
            return RestResult.createSuccessResult("申请已发起,流程已成功启动");
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
}