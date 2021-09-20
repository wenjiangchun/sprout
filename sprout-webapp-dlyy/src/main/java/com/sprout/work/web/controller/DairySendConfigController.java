package com.sprout.work.web.controller;

import com.sprout.core.spring.SpringContextUtils;
import com.sprout.shiro.ShiroUser;
import com.sprout.shiro.util.ShiroUtils;
import com.sprout.system.entity.User;
import com.sprout.system.service.UserService;
import com.sprout.web.util.RestResult;
import com.sprout.web.util.WebMessage;
import com.sprout.work.entity.DairySendConfig;
import com.sprout.work.service.DairySendConfigService;
import com.sprout.web.base.BaseCrudController;
import com.sprout.work.util.EmailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
		ShiroUser shiroUser = ShiroUtils.getCurrentUser();
		User worker = SpringContextUtils.getBean(UserService.class).findById(Long.valueOf(shiroUser.userId));
		model.addAttribute("worker", worker);
	}

	@GetMapping("/testSendEmail/{id}")
	@ResponseBody
	public RestResult testSendEmail(@PathVariable Long id) {
		try {
			DairySendConfig dairySendConfig = dairySendConfigService.findById(id);
			EmailSender.sendSimpleMail(dairySendConfig, "测试邮件", "测试内容");
			return RestResult.createSuccessResult("发送成功，请检查邮箱");
		} catch (Exception ex) {
			ex.printStackTrace();
			return RestResult.createErrorResult("发送失败:" + ex.getMessage());
		}

	}

	@GetMapping("checkCurrentUserConfig")
	@ResponseBody
	public RestResult checkCurrentUserConfig() {
		//查询是否已存在当前人配置信息
		ShiroUser currentUser = ShiroUtils.getCurrentUser();
		if (Objects.nonNull(currentUser)) {
			User worker = new User();
			worker.setId(Long.valueOf(currentUser.userId));
			//根据worker查询配置信息
			List<DairySendConfig> dairySendConfigList = this.dairySendConfigService.findByProperty("worker", worker);
			if (dairySendConfigList.isEmpty()) {
				return RestResult.createSuccessResult("");
			} else {
				return RestResult.createErrorResult("已存在用户配置信息");
			}
		} else {
			return RestResult.createErrorResult("用户未登录");
		}
	}

}
