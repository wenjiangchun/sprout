package com.sprout.work.web.controller;

import com.sprout.core.spring.SpringContextUtils;
import com.sprout.system.entity.User;
import com.sprout.system.service.UserService;
import com.sprout.work.entity.DairySendConfig;
import com.sprout.work.service.DairySendConfigService;
import com.sprout.web.base.BaseCrudController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping(value = "/work/dairySendConfig")
public class DairySendConfigController extends BaseCrudController<DairySendConfig, Long> {

	private DairySendConfigService dairySendConfigService;

	public DairySendConfigController(DairySendConfigService dairySendConfigService) {
		super("work", "dairySendConfig", "发送配置", dairySendConfigService);
		this.dairySendConfigService = dairySendConfigService;
	}

	@Override
	protected void setPageQueryVariables(Map<String, Object> queryVariables, HttpServletRequest request) {

	}

	@Override
	protected void setModel(Model model, HttpServletRequest request) {
		super.setModel(model, request);
		Long userId = 2L;
		User worker = SpringContextUtils.getBean(UserService.class).findById(userId);
		model.addAttribute("worker", worker);
	}

}
