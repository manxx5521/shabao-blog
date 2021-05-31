package com.xiaoshabao.blog.controller;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.xiaoshabao.base.controller.BaseController;
import com.xiaoshabao.blog.lang.Consts;

/**
 * 登录页
 */
@Controller
public class LoginController extends BaseController {
	@Value("${security.oauth2.client.clientId}")
	private String clientId;
	@Value("${security.oauth2.client.clientSecret}")
	private String clientSecret;
	@Value("${security.oauth2.client.loginCenterUri}")
	private String loginCenterUri;


	/**
	 * 跳转登录页
	 * 
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView view(@RequestParam(defaultValue = Consts.skin.DEFAULT) String skin, ModelMap model,
			HttpServletRequest request,RedirectAttributes attr) {
		String src = clientId+ ":" + clientSecret;
//		model.put("Authorization", Base64.getEncoder().encodeToString(src.getBytes(StandardCharsets.UTF_8)));
		attr.addAttribute("authorization", Base64.getEncoder().encodeToString(src.getBytes(StandardCharsets.UTF_8)));
		attr.addAttribute("uri", "/blog/user");
		return new ModelAndView("redirect:"+loginCenterUri,model);
//		return skin + Views.LOGIN;
	}

}
