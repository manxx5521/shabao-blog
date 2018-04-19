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
import com.xiaoshabao.blog.lang.Consts;
import com.xiaoshabao.blog.service.PostService;

import freemarker.template.TemplateException;

/**
 * 文章内容查询
 * 示例：
 * 	请求：http://127.0.0.1:8080/index?ord=newest&pn=2
 *  使用：@contents group=x pn=pn ord=ord
 */
@Component
public class ContentsDirective extends TemplateDirective {
	@Autowired
    private PostService postService;
	
	@Override
	protected void execute(DirectiveFunction function) throws TemplateException, IOException {
		Integer pn = function.getInteger("pn", 1);
        Integer channelId = function.getInteger("channelId", 0);
        String order = function.getString("order", Consts.order.NEWEST);
        Pageable pageable = PageRequest.of(pn - 1, 15);
        Page<Post> result = postService.paging(pageable, channelId, order);
        
        function.put(RESULTS, result);
	}
	
}
