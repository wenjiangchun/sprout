package com.sprout.work.web.controller;

import com.sprout.common.util.SproutDateUtils;
import com.sprout.system.entity.User;
import com.sprout.work.entity.DairySendConfig;
import com.sprout.work.entity.WorkDairy;
import com.sprout.work.service.DairySendConfigService;
import com.sprout.work.service.WorkDairyService;
import com.sprout.web.base.BaseCrudController;
import com.sprout.work.util.WorkDayUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
}
