package com.xiaoshabao.blog.component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.xiaoshabao.blog.dto.AccountProfile;
/**
 * 后置拦截，将登录信息和个人信息放到attribute中，方便前获取
 */
@Component
public class TokenInterceptor implements HandlerInterceptor  {
	private static Logger logger = LoggerFactory.getLogger(TokenInterceptor.class);
	@Autowired
	private ContextHolder contentHolder;
	// 后置拦截器
	@Override
	public void postHandle(HttpServletRequest request,HttpServletResponse response, 
			Object handler,ModelAndView modelAndView) throws Exception {
		try {
			String token=request.getParameter("access_token");
			if(StringUtils.isNotEmpty(token)) {
				request.setAttribute("access_token", token);
				
				AccountProfile profile=contentHolder.getProfile();
				request.setAttribute("profile", profile);
			}
		} catch (Exception e) {
			logger.error("传递token时错误");
			e.printStackTrace();
		}
	}
}
