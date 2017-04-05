package org.bes.interceptor;


import org.bes.common.SessionUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (SessionUtils.getLoginInfo() == null) {
			//response.setContentType("text/html; charset=UTF-8");
			//response.getWriter().write("{\"status\": -1, \"message\": \"访问未授权, 请登录!\"}");


			response.sendRedirect(request.getContextPath() + "/");
			return false;
		}

		return super.preHandle(request, response, handler);
	}
}
