/**
 *
 */
package com.xiaoshabao.blog.component.freemarker.directive;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.xiaoshabao.blog.component.freemarker.DirectiveFunction;
import com.xiaoshabao.blog.component.freemarker.TemplateDirective;
import com.xiaoshabao.blog.dto.Post;
import com.xiaoshabao.blog.service.PostService;

import freemarker.template.TemplateException;

/**
 * 根据作者取文章列表
 */
@Component
public class AuthorContentsDirective extends TemplateDirective {
	@Autowired
	private PostService postService;

	@Override
	protected void execute(DirectiveFunction function)
			throws TemplateException, IOException {
		int pn = function.getInteger("pn", 1);
		long uid = function.getInteger("uid", 0);

		Pageable pageable = PageRequest.of(pn - 1, 10);
		Page<Post> result = postService.pagingByAuthorId(pageable, uid);

		function.put(RESULTS, result);
	}

}
