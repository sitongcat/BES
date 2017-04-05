package org.bes.common;


import org.bes.model.dto.LoginInfo;
import org.bes.model.entity.Dept;
import org.bes.model.entity.User;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class SessionUtils {

	public static LoginInfo getLoginInfo() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		Object loginInfo = request.getSession().getAttribute("loginInfo");
		if (loginInfo != null) {
			return (LoginInfo) loginInfo;
		}
		return null;
	}

	public static boolean isAdmin() {
		User user = SessionUtils.getLoginInfo().getUser();
		Dept dept = SessionUtils.getLoginInfo().getDept();
		if (UserType.ADMIN == UserType.getType(user.getType()) && "0".equals(dept.getType())) {
			return true;
		}
		return false;
	}

	public static boolean isFinancial() {
		User user = SessionUtils.getLoginInfo().getUser();
		Dept dept = SessionUtils.getLoginInfo().getDept();
		if (UserType.DEPTADMIN == UserType.getType(user.getType()) && "0".equals(dept.getType())) {
			return true;
		}
		return false;
	}

	public static boolean isManagement() {
		User user = SessionUtils.getLoginInfo().getUser();
		Dept dept = SessionUtils.getLoginInfo().getDept();
		if (UserType.DEPTADMIN == UserType.getType(user.getType()) && "1".equals(dept.getType())) {
			return true;
		}
		return false;
	}

	public static boolean isAdminOrFinancial() {
		User user = SessionUtils.getLoginInfo().getUser();
		Dept dept = SessionUtils.getLoginInfo().getDept();
		if (UserType.ADMIN == UserType.getType(user.getType())
				|| (UserType.DEPTADMIN == UserType.getType(user.getType()) && "0".equals(dept.getType()))) {
			return true;
		}
		return false;
	}

}
