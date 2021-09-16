package com.sprout.system.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sprout.system.entity.Resource;
import com.sprout.system.entity.Role;
import com.sprout.system.service.ResourceService;
import com.sprout.system.service.RoleService;
import com.sprout.system.utils.Status;
import com.sprout.web.base.BaseCrudController;
import com.sprout.web.util.WebMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 角色管理Controller
 * @author sofar
 *
 */
@Controller
@RequestMapping(value = "/system/role")
public class RoleController extends BaseCrudController<Role, Long> {

	private RoleService roleService;
	
	private ResourceService resourceService;

	public RoleController(RoleService roleService, ResourceService resourceService) {
		super("system", "role", "角色信息", roleService);
		this.roleService = roleService;
		this.resourceService = resourceService;
	}

	@Override
	protected void setPageQueryVariables(Map<String, Object> queryVariables, HttpServletRequest request) {
		if (queryVariables != null && queryVariables.get("status") != null) {
			String value = (String) queryVariables.get("status");
			queryVariables.put("status", Status.valueOf(value));
		}
	}

	@Override
	protected void setModel(Model model, HttpServletRequest request) {
		model.addAttribute("statuss", Status.values());
	}

	/**
	 * 进入对角色添加资源管理权限页面
	 * @param id 角色Id
	 * @param model
	 * @return
	 */
	@GetMapping(value = "addResources/{id}")
	public String addResources(@PathVariable("id") Long id, Model model) {
		Role role = this.roleService.findById(id);
		List<Resource> menus = this.resourceService.findMenuResources();
		List<Resource> newMenus = new ArrayList<Resource>();
		model.addAttribute("role",role);
		model.addAttribute("menus", menus);
		return "system/role/addResource";
	}
	
	/**
	 * 对角色添加资源管理权限
	 * @param id 角色Id
	 * @param resourceIds 系统资源Id数组
	 * @return 返回列表页面
	 */
	@PostMapping(value = "saveResources")
    @ResponseBody
	public WebMessage saveResources(@RequestParam("roleId") Long id, @RequestParam(value = "resourceIds[]", required = false) Long[] resourceIds) {
        try {
            this.roleService.addResources(id, resourceIds);
            return WebMessage.createSuccessWebMessage();
        } catch (Exception e) {
            return WebMessage.createErrorWebMessage(e.getMessage());
        }
    }
	
	/**
	 * 更新角色信息
	 * @param role 角色信息
	 * @return 返回角色列表页面
	 */
	@PostMapping(value = "update")
    @ResponseBody
	public WebMessage update(Role role) {
		Role r = this.roleService.findById(role.getId());
		r.setName(role.getName());
		r.setEnabled(role.getEnabled());
		try {
			this.roleService.saveOrUpdate(r);
            /*return WebMessage.createLocaleSuccessWebMessage(RequestContextUtils.getLocale(request));*/
            return WebMessage.createSuccessWebMessage();
		} catch (Exception e) {
            return WebMessage.createErrorWebMessage(e.getMessage());
		}
	}
	
	/**
	 * 判断角色英文名是否存在
	 * @param roleName 角色英文名称
	 * @return true/false 不存在返回true,否则返回false
	 */
	@GetMapping(value = "notExistRoleName")
	@ResponseBody
	public Boolean notExistRoleName(String roleName) {
		Boolean isExist = this.roleService.existCode(roleName);
		return !isExist;
	}
}
