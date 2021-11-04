package com.sprout.datasource.data.ds.web.controller;

import com.sprout.datasource.data.ds.entity.DataSourceMeta;
import com.sprout.datasource.data.ds.service.DataSourceMetaService;
import com.sprout.datasource.data.ds.util.DataSourceMetaType;
import com.sprout.web.base.BaseCrudController;
import com.sprout.web.util.RestResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@Controller
@RequestMapping(value = "/data/dataSourceMeta")
public class DataSourceMetaController extends BaseCrudController<DataSourceMeta, Long> {

	private DataSourceMetaService dataSourceMetaService;

	public DataSourceMetaController(DataSourceMetaService dataSourceMetaService) {
		super("data", "dataSourceMeta", "数据源信息", dataSourceMetaService);
		this.dataSourceMetaService = dataSourceMetaService;
	}

	@Override
	protected void setPageQueryVariables(Map<String, Object> queryVariables, HttpServletRequest request) {
	}

	@Override
	protected void setModel(Model model, HttpServletRequest request) {
		super.setModel(model, request);
		model.addAttribute("driverList", DataSourceMetaType.values());
	}

	@GetMapping(value = "/list")
	public String showSourceMetaList(Model model) {
		model.addAttribute("sourceMetaList", this.dataSourceMetaService.findAll());
		return "/data/dataSourceMeta/list";
	}

	@PostMapping(value = "/testConnect")
	@ResponseBody
	public RestResult testConnect(DataSourceMeta dataSourceMeta) {
		try {
			boolean connected = dataSourceMetaService.testConnection(dataSourceMeta);
			if (connected) {
				return RestResult.createSuccessResult("连接正常");
			} else {
				return RestResult.createErrorResult("连接失败");
			}
		} catch (Exception e) {
			logger.error("", e);
			return RestResult.createErrorResult("连接失败, 错误原因：" + e);
		}
	}

}
