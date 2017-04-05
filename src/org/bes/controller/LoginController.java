package org.bes.controller;

import org.bes.common.SessionUtils;
import org.bes.model.dto.LoginInfo;
import org.bes.model.entity.Dept;
import org.bes.model.entity.User;
import org.bes.service.DeptService;
import org.bes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

	@Autowired
	private UserService userService;
	@Autowired
	private DeptService deptService;

	@PostMapping("/ext/login")
	@ResponseBody
	public String login(HttpServletRequest request,
	                    @RequestParam("userName") String userName, @RequestParam("password") String password) {
		User user = this.userService.getByUserName(userName);
		if (user == null) {
			return "{\"status\": -1, \"message\": \"用户名不存在!\"}";
		}
		if (!user.getPassword().equals(password)) {
			return "{\"status\": -2, \"message\": \"用户名或密码错误!\"}";
		}

		Dept dept = this.deptService.get(user.getDeptId());

		LoginInfo loginInfo = new LoginInfo();
		loginInfo.setUser(user);
		loginInfo.setDept(dept);

		request.getSession().setAttribute("loginInfo", loginInfo);
		loginInfo.setAdmin(SessionUtils.isAdmin());
		loginInfo.setFinancial(SessionUtils.isFinancial());

		return "{\"status\": 0}";
	}

	@RequestMapping("/ext/logout")
	public String logout(HttpServletRequest request) {
		request.getSession().removeAttribute("loginUser");
		return "redirect:/";
	}
	
}
