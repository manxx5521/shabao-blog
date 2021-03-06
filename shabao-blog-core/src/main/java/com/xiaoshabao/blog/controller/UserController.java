package com.xiaoshabao.blog.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.xiaoshabao.base.controller.BaseController;
import com.xiaoshabao.blog.component.ContextHolder;
import com.xiaoshabao.blog.dto.AccountProfile;
import com.xiaoshabao.blog.dto.BadgesCount;
import com.xiaoshabao.blog.dto.Comment;
import com.xiaoshabao.blog.dto.Favor;
import com.xiaoshabao.blog.dto.Feeds;
import com.xiaoshabao.blog.dto.Notify;
import com.xiaoshabao.blog.dto.Post;
import com.xiaoshabao.blog.dto.User;
import com.xiaoshabao.blog.lang.Consts;
import com.xiaoshabao.blog.service.CommentService;
import com.xiaoshabao.blog.service.FavorService;
import com.xiaoshabao.blog.service.FeedsService;
import com.xiaoshabao.blog.service.FollowService;
import com.xiaoshabao.blog.service.NotifyService;
import com.xiaoshabao.blog.service.PostService;
import com.xiaoshabao.blog.service.UserService;

/**
 * 访问他人主页
 */
@Controller
public class UserController extends BaseController {
	@Autowired
	private ContextHolder contentHolder;
	
	@Autowired
	protected HttpSession session;
	
	@Autowired
	private UserService userService;
	@Autowired
	private PostService postService;
	@Autowired
	private FeedsService feedsService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private FollowService followService;
	@Autowired
	private FavorService favorService;
	@Autowired
	private NotifyService notifyService;
	
	@RequestMapping("/users/{uid}")
	public String home(@PathVariable Long uid, HttpServletRequest request, ModelMap model,
			@RequestParam(defaultValue=Consts.skin.DEFAULT) String skin) {
		User user = userService.get(uid);
		int pn = ServletRequestUtils.getIntParameter(request, "pn", 1);

		model.put("user", user);
		model.put("pn", pn);
		return skin+Views.USERS_VIEW;
	}

	/**
	 * 用户主页
	 * @param model
	 * @return
	 */
	@GetMapping("/user")
	public String home(ModelMap model
			,@RequestParam(defaultValue="10") Integer pageSize
			,@RequestParam(defaultValue="1") Integer pn
			,@RequestParam(defaultValue=Consts.skin.DEFAULT) String skin) {
		Pageable pageable = PageRequest.of(pn-1, pageSize);
		AccountProfile profile=contentHolder.getProfile();
		Page<Feeds> page = feedsService.findUserFeeds(pageable, profile.getId());

		model.put("page", page);
		initUser(model);

		return skin+Views.USER_FEEDS;
	}

	/**
	 * 我发布的文章
	 * @param model
	 * @return
	 */
	@GetMapping(value="/user", params = "method=posts")
	public String posts(ModelMap model
			,@RequestParam(defaultValue="10") Integer pageSize
			,@RequestParam(defaultValue="1") Integer pn
			,@RequestParam(defaultValue=Consts.skin.DEFAULT) String skin) {
		Pageable pageable = PageRequest.of(pn-1, pageSize);
		AccountProfile up = contentHolder.getProfile();
		Page<Post> page = postService.pagingByAuthorId(pageable, up.getId());

		model.put("page", page);
		initUser(model);

		return skin+Views.USER_POSTS;
	}

	/**
	 * 我发表的评论
	 * @param model
	 * @return
	 */
	@GetMapping(value="/user", params = "method=comments")
	public String comments(ModelMap model
			,@RequestParam(defaultValue="10") Integer pageSize
			,@RequestParam(defaultValue="1") Integer pn
			,@RequestParam(defaultValue=Consts.skin.DEFAULT) String skin) {
		Pageable pageable = PageRequest.of(pn-1, pageSize);
		AccountProfile up = contentHolder.getProfile();
		Page<Comment> page = commentService.paging4Home(pageable, up.getId());

		model.put("page", page);
		initUser(model);

		return skin+Views.USER_COMMENTS;
	}

	/**
	 * 我喜欢过的文章
	 * @param model
	 * @return
	 */
	@GetMapping(value="/user", params = "method=favors")
	public String favors(ModelMap model
			,@RequestParam(defaultValue="10") Integer pageSize
			,@RequestParam(defaultValue="1") Integer pn
			,@RequestParam(defaultValue=Consts.skin.DEFAULT) String skin) {
		Pageable pageable = PageRequest.of(pn-1, pageSize);
		AccountProfile profile = contentHolder.getProfile();
		Page<Favor> page = favorService.pagingByOwnId(pageable, profile.getId());

		model.put("page", page);
		initUser(model);

		return skin+Views.USER_FAVORS;
	}

	/**
	 * 我的关注
	 * @param model
	 * @return
	 */
	@GetMapping(value="/user", params = "method=follows")
	public String follows(ModelMap model
			,@RequestParam(defaultValue="10") Integer pageSize
			,@RequestParam(defaultValue="1") Integer pn
			,@RequestParam(defaultValue=Consts.skin.DEFAULT) String skin) {
		Pageable pageable = PageRequest.of(pn-1, pageSize);
		AccountProfile profile = contentHolder.getProfile();
		Page<User> page = followService.follows(pageable, profile.getId());

		model.put("page", page);
		initUser(model);

		return skin+Views.USER_FOLLOWS;
	}

	/**
	 * 我的粉丝
	 * @param model
	 * @return
	 */
	@GetMapping(value="/user", params = "method=fans")
	public String fans(ModelMap model
			,@RequestParam(defaultValue="10") Integer pageSize
			,@RequestParam(defaultValue="1") Integer pn
			,@RequestParam(defaultValue=Consts.skin.DEFAULT) String skin) {
		Pageable pageable = PageRequest.of(pn-1, pageSize);
		AccountProfile profile = contentHolder.getProfile();
		Page<User> page = followService.fans(pageable, profile.getId());

		model.put("page", page);
		initUser(model);

		return skin+Views.USER_FANS;
	}

	/**
	 * 我的通知
	 * @param model
	 * @return
	 */
	@GetMapping(value="/user", params = "method=notifies")
	public String notifies(ModelMap model
			,@RequestParam(defaultValue="10") Integer pageSize
			,@RequestParam(defaultValue="1") Integer pn
			,@RequestParam(defaultValue=Consts.skin.DEFAULT) String skin) {
		Pageable pageable = PageRequest.of(pn-1, pageSize);
		AccountProfile profile = contentHolder.getProfile();
		Page<Notify> page = notifyService.findByOwnId(pageable, profile.getId());
		// 标记已读
		notifyService.readed4Me(profile.getId());

		model.put("page", page);
		initUser(model);

		return skin+Views.USER_NOTIFIES;
	}

	private void initUser(ModelMap model) {
		AccountProfile profile = contentHolder.getProfile();
		User user = userService.get(profile.getId());

		model.put("user", user);

		pushBadgesCount();
	}

	private void pushBadgesCount() {
		AccountProfile profile = (AccountProfile)session.getAttribute("profile");
		if (profile != null && profile.getBadgesCount() != null) {
			BadgesCount count = new BadgesCount();
			count.setNotifies(notifyService.unread4Me(profile.getId()));
			profile.setBadgesCount(count);
			session.setAttribute("profile", profile);
		}
	}
	
}
