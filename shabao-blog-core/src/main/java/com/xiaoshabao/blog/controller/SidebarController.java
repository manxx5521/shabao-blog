/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package com.xiaoshabao.blog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiaoshabao.base.controller.BaseController;
import com.xiaoshabao.blog.component.ContextHolder;
import com.xiaoshabao.blog.dto.AccountProfile;
import com.xiaoshabao.blog.dto.Post;
import com.xiaoshabao.blog.dto.User;
import com.xiaoshabao.blog.entity.TagPO;
import com.xiaoshabao.blog.service.PostService;
import com.xiaoshabao.blog.service.TagService;
import com.xiaoshabao.blog.service.UserService;

/**
 * 侧边栏数据加载
 *
 */
@Controller
@RequestMapping("/api")
public class SidebarController extends BaseController {
	@Autowired
	private ContextHolder contentHolder;
	@Autowired
	private PostService postService;
	@Autowired
	private TagService tagService;

	@Autowired
	private UserService userService;
	/*
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody
	Data login(String username, String password, ModelMap model) {
		Data data = Data.failure("操作失败");

		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			return data;
		}

		AuthenticationToken token = contentHolder.createToken(username, password);
		if (token == null) {
			data.setMessage("用户名或密码错误");
			return data;
		}

		try {
			SecurityUtils.getSubject().login(token);
			data = Data.success("登录成功", contentHolder.getProfile());

		} catch (Exception e) {
			if (e instanceof UnknownAccountException) {
				data.setMessage("用户不存在");
			} else if (e instanceof LockedAccountException) {
				data.setMessage("用户被禁用");
			} else {
				data.setMessage("登录认证失败");
			}
		}
		return data;
	}
	*/
	
	@GetMapping("/hottags")
	public @ResponseBody List<TagPO> hotTags() {
		List<TagPO> rets = tagService.topTags(12);
		return rets;
	}

	@RequestMapping("/latests")
	public @ResponseBody List<Post> latests() {
		AccountProfile up = contentHolder.getProfile();
		long ignoreUserId = 0;
		if (up != null) {
			ignoreUserId = up.getId();
		}
		List<Post> rets = postService.findLatests(6, ignoreUserId);
		return rets;
	}
	
	@RequestMapping("/hots")
	public @ResponseBody List<Post> hots() {
		AccountProfile up = contentHolder.getProfile();
		long ignoreUserId = 0;
		if (up != null) {
			ignoreUserId = up.getId();
		}
		List<Post> rets = postService.findHots(6, ignoreUserId);
		return rets;
	}
	
	/**
	 * 热门用户
	 * @param pn
	 * @return
	 */
	@RequestMapping(value="/hotusers")
	public @ResponseBody List<User> hotusers(Integer pn) {
		List<User> rets = userService.findHotUserByfans();
         return rets;
	}
	
}
