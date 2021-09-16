package com.sprout.shiro.web.controller;


import com.sprout.common.util.ValidateCodeUtils;
import com.sprout.shiro.ValidateCodeAuthenticationFilter;
import com.sprout.web.base.BaseController;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Controller
public class LoginController extends BaseController {

	@GetMapping("/login")
	public String login() {
		if (!SecurityUtils.getSubject().isAuthenticated()) {
			return "login";
		}
		return "redirect:/";
	}

	@PostMapping("/login")
	public String fail(@RequestParam(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM) String userName, @RequestParam String password, Model model, HttpServletRequest request) {
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(new UsernamePasswordToken(userName, password));
			return "redirect:/";
			/*SavedRequest savedRequest = WebUtils.getSavedRequest(request);
			if (savedRequest != null && HazeStringUtils.isNotBlank(savedRequest.getRequestUrl())) {
				//return "redirect:" + savedRequest.getRequestUrl();
				return "redirect:/";
			} else {
				return "redirect:/";
			}*/
		} catch (Exception e) {
			String errorMessage = e.getMessage();
			if (e instanceof IncorrectCredentialsException) {
				errorMessage = "密码错误!";
			}
			model.addAttribute("error", errorMessage);
			return "login";
		}
	}

	@GetMapping("/validateCode")
	public ResponseEntity<byte[]> validateCode(HttpSession session) throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		String validateCode = ValidateCodeUtils.getCode(200, 60, 5, outputStream).toLowerCase();
		session.setAttribute(ValidateCodeAuthenticationFilter.DEFAULT_VALIDATE_CODE_PARAM,validateCode);
		byte[] bs = outputStream.toByteArray();
		outputStream.close();
		return new ResponseEntity<byte[]>(bs,headers, HttpStatus.OK);
	}

	@GetMapping("/logout")
	public String logout() {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return "redirect:/login";
	}
}
