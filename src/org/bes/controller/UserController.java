package org.bes.controller;

import org.apache.commons.lang3.StringUtils;
import org.bes.common.JsonUtils;
import org.bes.common.Page;
import org.bes.common.SessionUtils;
import org.bes.common.UserType;
import org.bes.model.entity.User;
import org.bes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/page")
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping("/system/user")
	public String index() {
		return "/page/system/user/list";
	}

	@RequestMapping("/system/user/{id}")
	@ResponseBody
	public String user(@PathVariable String id) {
		User user = this.userService.get(id);
		Map<String, Object> resultMap = new LinkedHashMap<>();
		resultMap.put("status", 0);
		resultMap.put("data", user);
		return JsonUtils.fromObject(resultMap);
	}

	@RequestMapping("/system/user/list")
	@ResponseBody
	public String list(User user, Page page) {
		if (page == null) {
			page = new Page(1, 10);
		}

		User loginUser = SessionUtils.getLoginInfo().getUser();
		if (StringUtils.isBlank(user.getDeptId())) {
			if (UserType.ADMIN != UserType.getType(loginUser.getType())) {
				user.setDeptId(loginUser.getDeptId());
			}
		}

		Map<String, Object> resultMap = new LinkedHashMap<>();
		int dataCount = this.userService.getCount(user);
		page.setDataCount(dataCount);
		List<User> dataList = new ArrayList<>();
		if (dataCount > 0) {
			dataList = this.userService.getList(user,
					(page.getCurrentPage() - 1) * page.getPageSize(), page.getPageSize());
		}
		resultMap.put("status", 0);
		resultMap.put("data", dataList);
		resultMap.put("page", page);
		return JsonUtils.fromObject(resultMap);
	}

	@RequestMapping("/system/user/save")
	@ResponseBody
	public String save(User user) {
		if (StringUtils.isBlank(user.getId())) {
			User _user = this.userService.getByUserName(user.getUserName());
			if (_user != null) {
				return "{\"status\": 1, \"message\": \"用户名(" + user.getUserName() + ")已存在!\"}";
			}
		}
		this.userService.save(user);
		return "{\"status\": 0}";
	}

	@RequestMapping("/system/user/delete")
	@ResponseBody
	public String delete(String id) {
		this.userService.delete(id);
		return "{\"status\": 0}";
	}

	@RequestMapping("/system/user/changePwd")
	@ResponseBody
	public String changePwd(String oldPassword, String password) {
		User user = SessionUtils.getLoginInfo().getUser();
		user = this.userService.get(user.getId());
		if (!oldPassword.equals(user.getPassword())) {
			return "{\"status\": 1, \"message\": \"原密码错误!\"}";
		} else {
			this.userService.updatePassword(user.getId(), password);
			return "{\"status\": 0}";
		}
	}

}
