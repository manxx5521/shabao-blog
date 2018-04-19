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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.xiaoshabao.base.controller.BaseController;
import com.xiaoshabao.blog.dto.Channel;
import com.xiaoshabao.blog.dto.Post;
import com.xiaoshabao.blog.lang.Consts;
import com.xiaoshabao.blog.service.ChannelService;
import com.xiaoshabao.blog.service.PostService;

/**
 * Channel 主页
 */
@Controller
public class ChannelController extends BaseController {
	@Autowired
	private ChannelService channelService;
	@Autowired
	private PostService postService;
	
	@RequestMapping("/channel/{id}")
	public String channel(ModelMap model,@PathVariable Integer id, 
			@RequestParam(defaultValue=Consts.order.NEWEST) String order,
			@RequestParam(defaultValue="1") Integer pn,
			@RequestParam(defaultValue=Consts.skin.DEFAULT) String skin) {

		Channel channel = channelService.getById(id);
		
		model.put("channel", channel);
		model.put("order", order);
		model.put("pn", pn);
		return skin+Views.ROUTE_POST_INDEX;
	}

	@RequestMapping("/view/{id}")
	public String view(ModelMap model, @PathVariable Long id,
			@RequestParam(defaultValue=Consts.skin.DEFAULT) String skin) {
		Post view = postService.get(id);

		Assert.notNull(view, "该文章已被删除");

		postService.identityViews(id);
		model.put("view", view);
		return skin+Views.ROUTE_POST_VIEW;
	}
}
