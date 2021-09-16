package com.sprout.data.ds.web.controller;

import com.sprout.common.util.SproutStringUtils;
import com.sprout.data.ds.entity.DataSourceMeta;
import com.sprout.data.ds.provider.SproutDataSource;
import com.sprout.data.ds.service.DataSourceMetaService;
import com.sprout.web.base.BaseController;
import com.sprout.web.tree.TreeNode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping(value = "/data/dataSourceNavigate")
public class DataSourceNavigateController extends BaseController {

	private DataSourceMetaService dataSourceMetaService;

	public DataSourceNavigateController(DataSourceMetaService dataSourceMetaService) {
		this.dataSourceMetaService = dataSourceMetaService;
	}

	@GetMapping("view")
	public String view(Model model) {
		/*Map<String, SproutDataSource> ds = dataSourceMetaService.getSourceMetaMap();
		model.addAttribute("ds", ds);*/

		List<DataSourceMeta> dataSourceMetaList = dataSourceMetaService.findAll();
		model.addAttribute("dataSourceMetaList", dataSourceMetaList);
		return "data/dataSourceNavigate/view";
	}

	@GetMapping("showDB")
	@ResponseBody
	public List<TreeNode> showDB(@RequestParam Long metaId) {
		//SproutDataSource sproutDataSource = dataSourceMetaService.getSourceMetaMap().get(dataSourceMetaName);
		List<TreeNode> treeNodeList = new ArrayList<>();
		Long startId = 0L;
		try {
			//首先查询scheme
			DataSourceMeta dataSourceMeta = dataSourceMetaService.findById(metaId);
			SproutDataSource sproutDataSource = dataSourceMetaService.getSourceMetaMap().get(dataSourceMeta.getName());
			//查询是否包含schema
			String schema = dataSourceMeta.getSchema();
			if (SproutStringUtils.isNoneBlank(schema)) {
				//首先查询schema
				TreeNode treeNodeRoot = new TreeNode(startId, schema, null, "schema", false);
				treeNodeList.add(treeNodeRoot);
				List<Object> rs = sproutDataSource.query("show tables", null);
				for (Object r : rs) {
					List<String> nr = (List<String>) r;
					startId ++;
					TreeNode treeNode = new TreeNode(startId, nr.get(0), treeNodeRoot.getId(), "table", false);
					treeNodeList.add(treeNode);
				}
			} else {
				//查询schema 然后循环查询schema下面的表
				List<Object> rs = sproutDataSource.query("show databases", null);
				for (Object r : rs) {
					List<String> nr = (List<String>) r;
					startId ++;
					TreeNode schemaNode = new TreeNode(startId, nr.get(0), null, "schema", false);
					treeNodeList.add(schemaNode);
					//查询对应表
					List<Object> tableResult = sproutDataSource.query("use " + nr.get(0) + "; show databases", null);
					for (Object tr : tableResult) {
						List<String> trList = (List<String>) tr;
						startId ++;
						TreeNode tableNode = new TreeNode(startId, nr.get(0), schemaNode.getId(), "table", false);
						treeNodeList.add(tableNode);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return treeNodeList;
	}

	@GetMapping("showTableDesc")
	@ResponseBody
	public List<Object> showTableDesc(@RequestParam String tableName, @RequestParam String schemaName, @RequestParam Long metaId) {
         DataSourceMeta dataSourceMeta = dataSourceMetaService.findById(metaId);
         SproutDataSource sproutDataSource = dataSourceMetaService.getSourceMetaMap().get(dataSourceMeta.getName());
		try {
			return sproutDataSource.query("DESCRIBE " + schemaName + "." + tableName, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

}
