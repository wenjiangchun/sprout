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
import com.sprout.work.entity.DairySendLog;
import com.sprout.work.entity.WorkDairy;
import com.sprout.work.service.DairySendConfigService;
import com.sprout.work.service.DairySendLogService;
import com.sprout.work.service.WorkDairyService;
import com.sprout.work.util.WorkDairyReadListener;
import com.sprout.work.util.WorkDairyWrapper;
import com.sprout.work.util.WorkDayUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.mail.Message;
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


    @Override
    public String add(Model model, HttpServletRequest request) {
        model.addAttribute("workDairy", getWorkDairyByWorkDay(new Date()));
        return super.add(model, request);
    }

    @Override
    public String edit(Long id, Model model, HttpServletRequest request) {
        model.addAttribute("workDairy", getWorkDairyByWorkDay(new Date()));
        return super.edit(id, model, request);
    }

    @PostMapping("getWorkDairyByWorkDay")
    @ResponseBody
    public WorkDairy getWorkDairyByWorkDay(@RequestParam Date workDay) {
        //查询当前登录人信息
        ShiroUser shiroUser = ShiroUtils.getCurrentUser();
        WorkDairy workDairy = new WorkDairy();
        if (Objects.nonNull(shiroUser)) {
            User worker = SpringContextUtils.getBean(UserService.class).findById(Long.valueOf(shiroUser.getUserId()));
            //查询已经存在的数据
            workDairy = this.workDairyService.findWorkDairy(worker.getId(), workDay);
            //如果存在则直接查询赋值
            if (Objects.nonNull(workDairy)) {
                return workDairy;
            } else {
                //重新生成数据
                workDairy = new WorkDairy();
                workDairy.setWorker(worker);
                workDairy.setWorkDay(workDay);
                workDairy.setWeekDay(WorkDayUtils.getWeekDayByDate(workDay).getWeekDayName());
                //查找对应配置信息 如果不存在则根据数据库中第一条判断，如果第一条不存在则默认设为1
                List<DairySendConfig> dairySendConfigList = this.dairySendConfigService.findByProperty("worker", worker);
                if (!dairySendConfigList.isEmpty()) {
                    workDairy.setWeekNum(WorkDayUtils.getWeekNum(dairySendConfigList.get(0).getDairyStartDay(), workDay) + dairySendConfigList.get(0).getWeekStartNum() - 1);
                } else {
                    //查询数据库中该人员第一条记录
                    List<WorkDairy> firstWorkDairyList = this.workDairyService.findByProperty("worker", worker, Sort.by(Sort.Direction.ASC, "weekDay"));
                    if (!firstWorkDairyList.isEmpty()) {
                        workDairy.setWeekNum(WorkDayUtils.getWeekNum(firstWorkDairyList.get(0).getWorkDay(), workDay) + firstWorkDairyList.get(0).getWeekNum() - 1);
                    }
                }
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
        DairySendLog dairySendLog = new DairySendLog();
        DairySendConfig dairySendConfig = null;
        User worker = new User();
        try {
            ShiroUser currentUser = ShiroUtils.getCurrentUser();
            if (Objects.nonNull(currentUser)) {
                String userId = currentUser.getUserId();
                worker = new User();
                worker.setId(Long.valueOf(userId));
                dairySendConfig = this.dairySendConfigService.findOneByProperty("worker", worker);
                Message message = workDairyService.sendEmail(dairySendConfig);
                dairySendLog.setSendFlag(true);
                dairySendLog.setSubject(message.getSubject());
                dairySendLog.setSendTime(message.getSentDate());
                dairySendLog.setDestination(dairySendConfig.getDestination());
                dairySendLog.setSource(dairySendConfig.getSource());
                dairySendLog.setCopyDestinations(dairySendConfig.getCopyDestinations());
                dairySendLog.setUser(worker);
                dairySendLog.setSendResult("邮件发送成功");
                SpringContextUtils.getBean(DairySendLogService.class).save(dairySendLog);
                return RestResult.createSuccessResult("邮件发送成功!");
            } else {
                return RestResult.createErrorResult("邮件发送失败: 当前用户不存在，请登录系统!");
            }
        } catch (Exception ex) {
            dairySendLog.setSendFlag(false);
            dairySendLog.setSubject("");
            dairySendLog.setSendTime(new Date());
            dairySendLog.setUser(worker);
            if (dairySendConfig != null) {
                dairySendLog.setDestination(dairySendConfig.getDestination());
                dairySendLog.setSource(dairySendConfig.getSource());
                dairySendLog.setCopyDestinations(dairySendConfig.getCopyDestinations());
            }
            dairySendLog.setSendResult("邮件发送失败:" + ex.getMessage());
            try {
                SpringContextUtils.getBean(DairySendLogService.class).save(dairySendLog);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
