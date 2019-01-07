package com.xiaoshabao.blog.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import com.xiaoshabao.base.controller.BaseController;
import com.xiaoshabao.blog.component.ContextHolder;
import com.xiaoshabao.blog.dto.AccountProfile;
import com.xiaoshabao.blog.dto.Comment;
import com.xiaoshabao.blog.dto.Data;
import com.xiaoshabao.blog.event.NotifyEvent;
import com.xiaoshabao.blog.lang.Consts;
import com.xiaoshabao.blog.service.CommentService;

/**
 * @author langhsu
 *
 */
@Controller
@RequestMapping("/comment")
public class CommentController extends BaseController {
	
	@Autowired
	private ContextHolder contentHolder;
	@Autowired
	private CommentService commentService;
	@Autowired
	private ApplicationContext applicationContext;

	@RequestMapping("/list/{toId}")
	public @ResponseBody Page<Comment> view(@PathVariable Long toId
			,@RequestParam(defaultValue="10") Integer pageSize,
			@RequestParam(defaultValue="1") Integer pn) {
		Pageable pageable = PageRequest.of(pn-1, pageSize);
		Page<Comment> page = commentService.paging(pageable, toId);
		return page;
	}
	
	@RequestMapping("/submit")
	public @ResponseBody
	Data post(Long toId, String text
			,@RequestParam(defaultValue="0") long pid) {
		
		Data data = Data.failure("操作失败");
		if (!contentHolder.isAuthenticated()) {
			data = Data.failure("请先登录在进行操作");
			
			return data;
		}
		if (toId > 0 && StringUtils.isNotEmpty(text)) {
			AccountProfile up = contentHolder.getProfile();
			
			Comment c = new Comment();
			c.setToId(toId);
			c.setContent(HtmlUtils.htmlEscape(text));
			c.setAuthorId(up.getId());
			
			c.setPid(pid);
			
			commentService.post(c);

            if(toId != up.getId()) {
			    sendNotify(up.getId(), toId, pid);
            }
			
			data = Data.success("发表成功!", Data.NOOP);
		}
		return data;
	}

	@RequestMapping("/delete")
	public @ResponseBody Data delete(Long id) {
		Data data = Data.failure("操作失败");
		if (id != null) {
			AccountProfile up = contentHolder.getProfile();
			try {
				commentService.delete(id, up.getId());
				data = Data.success("操作成功", Data.NOOP);
			} catch (Exception e) {
				data = Data.failure(e.getMessage());
			}
		}
		return data;
	}

	/**
	 * 发送通知
	 * @param userId
	 * @param postId
	 */
	private void sendNotify(long userId, long postId, long pid) {
		NotifyEvent event = new NotifyEvent("NotifyEvent");
		event.setFromUserId(userId);

		if (pid > 0) {
			event.setEvent(Consts.NOTIFY_EVENT_COMMENT_REPLY);
		} else {
			event.setEvent(Consts.NOTIFY_EVENT_COMMENT);
		}
		// 此处不知道文章作者, 让通知事件系统补全
		event.setPostId(postId);
		applicationContext.publishEvent(event);
	}
}