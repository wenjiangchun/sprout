package com.sprout.system.web.controller;

import java.util.*;

import com.sprout.core.spring.SpringContextUtils;
import com.sprout.system.entity.Group;
import com.sprout.system.entity.Resource;
import com.sprout.system.service.ResourceService;
import com.sprout.system.utils.ResourceType;
import com.sprout.web.base.BaseCrudController;
import com.sprout.web.tree.TreeNode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/system/resource")
public class ResourceController extends BaseCrudController<Resource, Long> {
	
	private ResourceService resourceService;

	public ResourceController(ResourceService resourceService) {
		super("system", "resource", "资源信息", resourceService);
		this.resourceService = resourceService;
	}

	@Override
	protected void setModel(Model model, HttpServletRequest request) {
		model.addAttribute("resourceTypes", ResourceType.values());
	}

	@Override
	protected void setPageQueryVariables(Map<String, Object> queryVariables, HttpServletRequest request) {
		if (queryVariables != null && queryVariables.get("resourceType") != null) {
			String value = (String) queryVariables.get("resourceType");
			queryVariables.put("resourceType",ResourceType.valueOf(value));
		}
		if (queryVariables.containsKey("parent.id") && queryVariables.get("parent.id") != null){
			Group g = new Group();
			g.setId(Long.valueOf(queryVariables.get("parent.id").toString()));
			queryVariables.put("parent.id", Long.valueOf(queryVariables.get("parent.id").toString()));
		} else {
			queryVariables.put("parent_isNull", null); //默认查询顶级字典列表
		}
	}

	@Override
	@GetMapping(value = "add")
	public String add(Model model, HttpServletRequest request) {
		setModel(model, request);
		List<Resource> resources = this.resourceService.findMenuResources();
		model.addAttribute("resources",resources);
		RequestMappingHandlerMapping requestMappingBean = SpringContextUtils.getBean(RequestMappingHandlerMapping.class);
		Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingBean.getHandlerMethods();
		Set<String> result = new LinkedHashSet<>();
		for (RequestMappingInfo requestMappingInfo : handlerMethods.keySet()) {
			PatternsRequestCondition pc = requestMappingInfo.getPatternsCondition();
			Set<String> pSet = pc.getPatterns();
			result.addAll(pSet);
		}
		model.addAttribute("urlList",result);
		String parentId = request.getParameter("parentId");
		if (parentId != null) {
			Resource parent = this.resourceService.findById(Long.parseLong(parentId));
			model.addAttribute("parent", parent);
			model.addAttribute("parentId", parentId);
			model.addAttribute("num", parent.getChilds().size() + 1);
		} else {
			List<Resource> roots = this.resourceService.findByProperty("parent", null);
			model.addAttribute("num", roots.size() + 1);
		}
		return "system/resource/add";
	}
	

	@PostMapping(value = "getResourcesTree")
	@ResponseBody
	public List<TreeNode> getResources() {
		List<Resource> resourceList = this.resourceService.findAllBySn(true);
		List<TreeNode> treeNodeList = new ArrayList<TreeNode>();
		TreeNode root = new TreeNode();
		root.setName("系统资源树");
		root.setId(0L);
		root.setParentId(null);
		treeNodeList.add(root);
		for (Resource resource : resourceList) {
			TreeNode treeNode = new TreeNode();
			treeNode.setId(resource.getId());
			treeNode.setName(resource.getName());
			treeNode.setParentId(resource.getParent() != null ? resource.getParent().getId() : root.getId());
			treeNodeList.add(treeNode);
		}
		return treeNodeList;
	}

	@Override
	@GetMapping(value = "edit/{id}")
	public String edit(@PathVariable Long id, Model model, HttpServletRequest request) {
		model.addAttribute("resourceTypes",ResourceType.values());
		List<Resource> menuResources = new ArrayList<Resource>();
		Resource resource = this.resourceService.findById(id);
		if (resource.getParent() != null) {
			menuResources = this.resourceService.findMenuResources();
		}
		model.addAttribute("resource",resource);
		model.addAttribute("menuResources",menuResources);
		RequestMappingHandlerMapping requestMappingBean = SpringContextUtils.getBean(RequestMappingHandlerMapping.class);
		Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingBean.getHandlerMethods();
		Set<String> result = new LinkedHashSet<>();
		for (RequestMappingInfo requestMappingInfo : handlerMethods.keySet()) {
			PatternsRequestCondition pc = requestMappingInfo.getPatternsCondition();
			Set<String> pSet = pc.getPatterns();
			result.addAll(pSet);
		}
		model.addAttribute("urlList",result);
		return "system/resource/editResource";
	}
	
	@PostMapping(value = "update")
	public String update(Resource resource) throws Exception {
		if (resource.getParent() != null && resource.getParent().getId() == null) {
			resource.setParent(null);
		}
		this.resourceService.saveOrUpdate(resource);
		return "redirect:/system/resource/view/";
	}

}
