package com.xiaoshabao.blog.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.xiaoshabao.base.controller.BaseController;
import com.xiaoshabao.blog.component.MailHelper;
import com.xiaoshabao.blog.component.shiro.ShiroUtil;
import com.xiaoshabao.blog.dto.AccountProfile;
import com.xiaoshabao.blog.dto.Data;
import com.xiaoshabao.blog.dto.User;
import com.xiaoshabao.blog.lang.Consts;
import com.xiaoshabao.blog.service.UserService;
import com.xiaoshabao.blog.service.VerifyService;

/**
 */
@Controller
@RequestMapping("/user")
public class ProfileController extends BaseController {
	@Autowired
	private UserService userService;
	@Autowired
	private VerifyService verifyService;
	@Autowired
	private MailHelper mailHelper;

	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public String view(ModelMap model,@RequestParam(defaultValue=Consts.skin.DEFAULT) String skin) {
		AccountProfile profile = ShiroUtil.getSubject().getProfile();
		User view = userService.get(profile.getId());
		model.put("view", view);
		return skin+Views.USER_PROFILE;
	}

	@RequestMapping(value = "/profile", method = RequestMethod.POST)
	public String post(String name, String signature, ModelMap model,@RequestParam(defaultValue=Consts.skin.DEFAULT) String skin) {
		Data data;
		AccountProfile profile = ShiroUtil.getSubject().getProfile();
		
		try {
			User user = new User();
			user.setId(profile.getId());
			user.setName(name);
			user.setSignature(signature);

			SecurityUtils.getSubject().getSession(true).setAttribute("profile", userService.update(user));

			// put 最新信息
			User view = userService.get(profile.getId());
			model.put("view", view);

			data = Data.success("操作成功", Data.NOOP);
		} catch (Exception e) {
			data = Data.failure(e.getMessage());
		}
		model.put("data", data);
		return skin+Views.USER_PROFILE;
	}

	@RequestMapping(value = "/email", method = RequestMethod.GET)
	public String email(@RequestParam(defaultValue=Consts.skin.DEFAULT) String skin) {
		return skin+Views.USER_EMAIL;
	}

	@RequestMapping(value = "/email", method = RequestMethod.POST)
	public String emailPost(String email, ModelMap model,@RequestParam(defaultValue=Consts.skin.DEFAULT) String skin) {
		Data data;
		AccountProfile profile = ShiroUtil.getSubject().getProfile();

		try {
			Assert.notNull(email, "缺少必要的参数");

			// 先执行修改，判断邮箱是否更改，或邮箱是否被人使用
			userService.updateEmail(profile.getId(), email);

			String code = verifyService.generateCode(profile.getId(), Consts.VERIFY_BIND, email);

			Map<String, Object> context = new HashMap<>();
			context.put("userId", profile.getId());
			context.put("code", code);
			context.put("type", Consts.VERIFY_BIND);

			mailHelper.sendEmail(Consts.EMAIL_TEMPLATE_BIND, email, "邮箱绑定验证", context);

			data = Data.success("操作成功，已经发送验证邮件，请前往邮箱验证", Data.NOOP);
		} catch (Exception e) {
			data = Data.failure(e.getMessage());
		}
		model.put("data", data);
		return skin+Views.USER_EMAIL;
	}

}
