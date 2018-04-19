/**
 *
 */
package com.xiaoshabao.blog.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.xiaoshabao.base.controller.BaseController;
import com.xiaoshabao.blog.dto.Post;
import com.xiaoshabao.blog.lang.Consts;
import com.xiaoshabao.blog.service.PostService;

/**
 * 标签
 */
@Controller
public class TagController extends BaseController {
    @Autowired
    private PostService postService;

    @RequestMapping("/tag/{kw}")
    public String tag(@PathVariable String kw, ModelMap model,
    		@RequestParam(defaultValue="10") int pageSize,
    		@RequestParam(defaultValue="1") int pn,
    		@RequestParam(defaultValue=Consts.skin.DEFAULT) String skin) {
    	
        Pageable pageable = PageRequest.of(pn - 1, pageSize);
        try {
            if (StringUtils.isNotEmpty(kw)) {
                Page<Post> page = postService.searchByTag(pageable, kw);
                model.put("page", page);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        model.put("kw", kw);
        return skin+Views.TAGS_TAG;
    }

}
