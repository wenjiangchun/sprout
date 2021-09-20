package com.sprout.work.web.controller;

import com.alibaba.excel.EasyExcel;
import com.sprout.core.spring.SpringContextUtils;
import com.sprout.shiro.ShiroUser;
import com.sprout.shiro.util.ShiroUtils;
import com.sprout.system.entity.User;
import com.sprout.system.service.UserService;
import com.sprout.web.base.BaseCrudController;
import com.sprout.web.util.RestResult;
import com.sprout.work.entity.DairySendConfig;
import com.sprout.work.entity.WorkDairy;
import com.sprout.work.service.DairySendConfigService;
import com.sprout.work.service.WorkDairyService;
import com.sprout.work.util.WorkDairyReadListener;
import com.sprout.work.util.WorkDairyWrapper;
import com.sprout.work.util.WorkDayUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping(value = "/work/workDairy")
public class WorkDairyController extends BaseCrudController<WorkDairy, Long> {

    private WorkDairyService workDairyService;

    private DairySendConfigService dairySendConfigService;

    public WorkDairyController(WorkDairyService workDairyService, DairySendConfigService dairySendConfigService) {
        super("work", "workDairy", "工作日志", workDairyService);
        this.workDairyService = workDairyService;
        this.dairySendConfigService = dairySendConfigService;
    }

    @Override
    protected void setPageQueryVariables(Map<String, Object> queryVariables, HttpServletRequest request) {

    }

    @Override
    protected void setModel(Model model, HttpServletRequest request) {
        super.setModel(model, request);
    }



    @PostMapping("getWorkDairyByWorkDay")
    @ResponseBody
    public WorkDairy getWorkDairyByWorkDay(@RequestParam Date workDay) {
        Long userId = 2L;
        WorkDairy workDairy = this.workDairyService.findWorkDairy(userId, workDay);
        User worker = new User();
        worker.setId(userId);
        // 查询员工日志配置信息
        List<DairySendConfig> configList = this.dairySendConfigService.findByProperty("worker", worker);
        if (Objects.isNull(workDairy)) {
            //TODO 计算当前周
            workDairy = new WorkDairy();
            workDairy.setWorkDay(workDay);
            workDairy.setWeekDay(WorkDayUtils.getWeekDayByDate(workDay).getWeekDayName());
            if (configList.isEmpty()) {
                workDairy.setWeekNum(1);
            } else {
                workDairy.setWeekNum(WorkDayUtils.getWeekNum(configList.get(0).getDairyStartDay(), workDay));
            }
        }
        return workDairy;
    }

    @PostMapping("/uploadWorkDairy")
    @ResponseBody
    public RestResult uploadWorkDairy(MultipartHttpServletRequest request) {
        ShiroUser currentUser = ShiroUtils.getCurrentUser();
        if (Objects.nonNull(currentUser)) {
            User worker = new User();
            worker.setId(Long.valueOf(currentUser.userId));
            List<MultipartFile> fileList = request.getFiles("fileBlob");
            try {
                for (MultipartFile multipartFile : fileList) {
                    EasyExcel.read(multipartFile.getInputStream(), WorkDairyWrapper.class, new WorkDairyReadListener(this.workDairyService, dairySendConfigService.findOneByProperty("worker", worker), worker)).sheet().doRead();
                }
                return RestResult.createSuccessResult("导入成功");
            } catch (Exception ex) {
                return RestResult.createErrorResult("导入失败:" + ex.getMessage());
            }
        } else {
            return RestResult.createErrorResult("导入失败:用户未登录");
        }
    }

    @GetMapping("/sendEmail")
    @ResponseBody
    public RestResult sendEmail() {
        try {
            ShiroUser currentUser = ShiroUtils.getCurrentUser();
            if (Objects.nonNull(currentUser)) {
                String userId = currentUser.getUserId();
                User worker = new User();
                worker.setId(Long.valueOf(userId));
                workDairyService.sendEmail(worker);
                return RestResult.createSuccessResult("邮件发送成功!");
            } else {
                return RestResult.createErrorResult("邮件发送失败: 当前用户不存在，请登录系统!");
            }
        } catch (Exception ex) {
            return RestResult.createErrorResult("邮件发送失败:" + ex.getMessage());
        }

    }

    @PostMapping("/generateWorkDairy")
    @ResponseBody
    public RestResult generateWorkDairy() {
        try {
            Long userId = Long.valueOf(ShiroUtils.getCurrentUser().getUserId());
            User user = new User();
            user.setId(userId);
            int count = this.workDairyService.generateWorkDairy(userId, this.dairySendConfigService.findOneByProperty("worker", user));
            return RestResult.createSuccessResult(count, "创建成功");
        } catch (Exception ex) {
            return RestResult.createErrorResult("创建失败:" + ex.getMessage());
        }
    }
}
