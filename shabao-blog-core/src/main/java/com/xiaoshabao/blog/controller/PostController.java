package com.xiaoshabao.blog.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiaoshabao.base.controller.BaseController;
import com.xiaoshabao.blog.component.ContextHolder;
import com.xiaoshabao.blog.dto.AccountProfile;
import com.xiaoshabao.blog.dto.Data;
import com.xiaoshabao.blog.dto.Post;
import com.xiaoshabao.blog.lang.Consts;
import com.xiaoshabao.blog.service.ChannelService;
import com.xiaoshabao.blog.service.PostService;

/**
 * 文章操作
 */
@Controller
@RequestMapping("/post")
public class PostController extends BaseController {
	@Autowired
	private ContextHolder contentHolder;
	@Autowired
	private PostService postService;
	@Autowired
	private ChannelService channelService;

	/**
	 * 发布文章页
	 * @return
	 */
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String view(ModelMap model
			,@RequestParam(defaultValue=Consts.skin.DEFAULT) String skin) {
		model.put("channels", channelService.findAll(Consts.STATUS_NORMAL));
		model.put("site_editor", "ueditor");
		return skin+Views.ROUTE_POST_PUBLISH;
	}

	/**
	 * 提交发布
	 * @param p
	 * @return
	 */
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public String post(Post p, HttpServletRequest request) {
		Assert.notNull(p, "参数不完整");
		Assert.state(StringUtils.isNotBlank(p.getTitle()), "标题不能为空");
		Assert.state(StringUtils.isNotBlank(p.getContent()), "内容不能为空");
		AccountProfile profile = contentHolder.getProfile();
		p.setAuthorId(profile.getId());

		postService.post(p);
		return Views.REDIRECT_USER_POSTS;
	}

	/**
	 * 删除文章
	 * @param id
	 * @return
	 */
	@RequestMapping("/delete/{id}")
	public @ResponseBody
	Data delete(@PathVariable Long id) {
		Data data = Data.failure("操作失败");
		if (id != null) {
			AccountProfile up = contentHolder.getProfile();
			try {
				postService.delete(id, up.getId());
				data = Data.success("操作成功", Data.NOOP);
			} catch (Exception e) {
				data = Data.failure(e.getMessage());
			}
		}
		return data;
	}

	/**
	 * 修改文章
	 * @param id
	 * @return
	 */
	@RequestMapping("/to_update/{id}")
	public String toUpdate(@PathVariable Long id, ModelMap model
			,@RequestParam(defaultValue=Consts.skin.DEFAULT) String skin) {
		AccountProfile up = contentHolder.getProfile();
		Post ret = postService.get(id);

		Assert.notNull(ret, "该文章已被删除");

		Assert.isTrue(ret.getAuthorId() == up.getId(), "该文章不属于你");

		model.put("groups", channelService.findAll(Consts.STATUS_NORMAL));
		model.put("view", ret);
		model.put("site_editor", "ueditor");
		return skin+Views.ROUTE_POST_UPDATE;
	}

	/**
	 * 更新文章方法
	 * @author LBB
	 * @return
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String subUpdate(Post p, HttpServletRequest request) {
		Assert.notNull(p, "参数不完整");
		Assert.state(StringUtils.isNotBlank(p.getTitle()), "标题不能为空");
		Assert.state(StringUtils.isNotBlank(p.getContent()), "内容不能为空");

		AccountProfile up = contentHolder.getProfile();
		if (p.getAuthorId() == up.getId()) {
			String content = request.getParameter("content");
			p.setContent(content);
			postService.update(p);
		}
		return Views.REDIRECT_USER_POSTS;
	}

}
