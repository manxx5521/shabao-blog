package com.xiaoshabao.blog.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.xiaoshabao.base.controller.BaseController;
import com.xiaoshabao.blog.component.ContextHolder;
import com.xiaoshabao.blog.component.MailHelper;
import com.xiaoshabao.blog.dto.AccountProfile;
import com.xiaoshabao.blog.dto.Data;
import com.xiaoshabao.blog.dto.User;
import com.xiaoshabao.blog.lang.Consts;
import com.xiaoshabao.blog.service.UserService;
import com.xiaoshabao.blog.service.VerifyService;

/**
 */
@Controller
public class RegisterController extends BaseController {
	@Autowired
	private ContextHolder contentHolder;
	@Autowired
	private UserService userService;
	@Autowired
	private VerifyService verifyService;
	@Autowired
	private MailHelper mailHelper;
	
	@GetMapping("/register")
	public String view(@RequestParam(defaultValue=Consts.skin.DEFAULT) String skin) {
		AccountProfile profile = contentHolder.getProfile();
		if (profile != null) {
			return "redirect:/home";
		}
		return skin+Views.REGISTER;
	}
	
	@PostMapping("/register")
	public String register(User post, ModelMap model,@RequestParam(defaultValue=Consts.skin.DEFAULT) String skin) {
		Data data;
		String ret = skin+Views.REGISTER;

		try {
			post.setAvatar(Consts.AVATAR);
			User user = userService.register(post);

			sendEmail(user);

			data = Data.success("恭喜您! 注册成功, 已经给您的邮箱发了验证码, 赶紧去完成邮箱绑定吧。", Data.NOOP);
			data.addLink("login", "先去登陆尝尝鲜");

			ret = skin+Views.REGISTER_RESULT;
			
		} catch (Exception e) {
            model.addAttribute("post", post);
			data = Data.failure(e.getMessage());
		}
		model.put("data", data);
		return ret;
	}

	private void sendEmail(User user) {
		String code = verifyService.generateCode(user.getId(), Consts.VERIFY_BIND, user.getEmail());
		Map<String, Object> data = new HashMap<>();
		data.put("userId", user.getId());
		data.put("code", code);
		data.put("type", Consts.VERIFY_BIND);

		mailHelper.sendEmail(Consts.EMAIL_TEMPLATE_BIND, user.getEmail(), "邮箱绑定验证", data);
	}

}