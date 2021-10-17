package com.sprout.system.web.controller;

import java.util.Map;

import com.sprout.system.entity.Config;
import com.sprout.system.service.ConfigService;
import com.sprout.system.utils.ConfigType;
import com.sprout.web.base.BaseCrudController;
import com.sprout.web.util.RestResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/system/config")
public class ConfigController extends BaseCrudController<Config, Long> {

	private ConfigService configService;

	public ConfigController(ConfigService configService) {
		super("system", "config", "配置信息", configService);
		this.configService = configService;
	}

	@Override
	protected void setPageQueryVariables(Map<String, Object> queryVariables, HttpServletRequest request) {
		//queryVariables.put("configType", "S");
	}

	@Override
	protected void setModel(Model model, HttpServletRequest request) {
		super.setModel(model, request);
		model.addAttribute("configTypes", ConfigType.values());
	}

	@PostMapping(value="updateConfigValue")
    @ResponseBody 
	public RestResult updateConfigValue(@RequestParam(value="id")Long id, @RequestParam(value="value") String value) {
        try {
            Config config = this.configService.findById(id);
            config.setValue(value);
            this.configService.updateConfig(config);
            return RestResult.createSuccessResult();
        } catch (Exception e) {
            return RestResult.createErrorResult(e.getMessage());
        }
    }
	
	/**
	 * 判断配置代码是否存在
	 * @param code 配置名称
	 * @return 不存在返回 true / 存在返回 false
	 */
	@PostMapping(value = "notExistCode")
	@ResponseBody
	public Boolean notExistCode(String code) {
		Boolean isExist = this.configService.existCode(code);
		return !isExist;
	}
}
