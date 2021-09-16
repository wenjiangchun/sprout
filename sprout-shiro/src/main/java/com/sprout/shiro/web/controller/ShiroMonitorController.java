package com.sprout.shiro.web.controller;

import com.sprout.shiro.ShiroUser;
import com.sprout.shiro.util.ShiroUtils;
import com.sprout.web.base.BaseController;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(value = "/shiro")
public class ShiroMonitorController extends BaseController {

	@Autowired
	private SessionDAO sessionDAO;
		
	@GetMapping(value = "/online/view")
	public String list(Model model) {
		List<ShiroUser> onlineList = ShiroUtils.getOnlineUserList();
		model.addAttribute("onlineList", onlineList);
		return "shiro/onlineList";
	}
}
