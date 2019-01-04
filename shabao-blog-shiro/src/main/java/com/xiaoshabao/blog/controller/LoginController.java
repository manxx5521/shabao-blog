package com.xiaoshabao.blog.controller;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.xiaoshabao.base.controller.BaseController;
import com.xiaoshabao.blog.component.shiro.ShiroUtil;
import com.xiaoshabao.blog.lang.Consts;

/**
 * 登录页
 */
@Controller
public class LoginController extends BaseController {

    /**
     * 跳转登录页
     * @return
     */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String view(@RequestParam(defaultValue=Consts.skin.DEFAULT) String skin) {
		return skin+Views.LOGIN;
	}

    /**
     * 提交登录
     * @param username
     * @param password
     * @param model
     * @return
     */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(String username, String password,@RequestParam(value = "rememberMe",defaultValue = "0") int rememberMe, ModelMap model,
			@RequestParam(defaultValue=Consts.skin.DEFAULT) String skin) {
		String ret = skin+Views.LOGIN;
		
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return ret;
        }
		
		AuthenticationToken token = ShiroUtil.createToken(username, password);
        if (token == null) {
        	model.put("message", "用户名或密码错误");
            return ret;
        }

        if (rememberMe == 1) {
            ((UsernamePasswordToken) token).setRememberMe(true);
        }

        try {
            SecurityUtils.getSubject().login(token);

            ret = Views.REDIRECT_USER;
        } catch (AuthenticationException e) {
            if (e instanceof UnknownAccountException) {
            	model.put("message", "用户不存在");
            } else if (e instanceof LockedAccountException) {
            	model.put("message", "用户被禁用");
            } else {
            	model.put("message", "用户认证失败");
            }
        }

        return ret;
	}
	
	/**
	 * 退出登录
	 * @return
	 */
	@RequestMapping("/logout")
	public String logout(HttpServletResponse response) {
		SecurityUtils.getSubject().logout();
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		response.setDateHeader("Expires", 0); // Proxies.
		return "redirect:/index";
	}

}
