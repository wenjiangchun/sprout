package com.sprout.work.web.controller;

import com.sprout.work.entity.DairySendLog;
import com.sprout.work.service.DairySendLogService;
import com.sprout.web.base.BaseCrudController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping(value = "/work/dairySendLog")
public class DairySendLogController extends BaseCrudController<DairySendLog, Long> {

	private DairySendLogService dairySendLogService;

	public DairySendLogController(DairySendLogService dairySendLogService) {
		super("work", "dairySendConfig", "发送记录", dairySendLogService);
		this.dairySendLogService = dairySendLogService;
	}

	@Override
	protected void setPageQueryVariables(Map<String, Object> queryVariables, HttpServletRequest request) {

	}

	@Override
	protected void setModel(Model model, HttpServletRequest request) {
		super.setModel(model, request);
	}

}
