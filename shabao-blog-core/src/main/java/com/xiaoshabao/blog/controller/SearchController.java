package com.xiaoshabao.blog.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.xiaoshabao.base.controller.BaseController;
import com.xiaoshabao.blog.dto.Post;
import com.xiaoshabao.blog.lang.Consts;
import com.xiaoshabao.blog.service.PostService;

/**
 * 文章搜索
 */
@Controller
public class SearchController extends BaseController {
	@Autowired
	private PostService postService;

	@RequestMapping("/search")
	public String search(ModelMap model,String kw, 
			@RequestParam(defaultValue="10") Integer pageSize,
			@RequestParam(defaultValue="1") Integer pn,
			@RequestParam(defaultValue=Consts.skin.DEFAULT) String skin) {
		Pageable pageable = PageRequest.of(pn-1, pageSize);
		try {
			if (StringUtils.isNotEmpty(kw)) {
				Page<Post> page = postService.search(pageable, kw);
				model.put("page", page);
			}else{
				return skin+Views.INDEX;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.put("kw", kw);
		return skin+Views.BROWSE_SEARCH;
	}
	
}
