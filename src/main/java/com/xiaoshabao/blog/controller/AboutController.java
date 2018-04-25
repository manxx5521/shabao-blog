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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.xiaoshabao.base.controller.BaseController;
import com.xiaoshabao.blog.lang.Consts;

/**
 * 关于我们
 */
@Controller
public class AboutController extends BaseController {
	
	@RequestMapping("/about")
	public String about(@RequestParam(defaultValue=Consts.skin.DEFAULT) String skin) {
		return skin+"/about/about";
	}
	
	@RequestMapping("/joinus")
	public String joinus(@RequestParam(defaultValue=Consts.skin.DEFAULT) String skin) {
		return skin+"/about/joinus";
	}
	
	@RequestMapping("/faqs")
	public String faqs(@RequestParam(defaultValue=Consts.skin.DEFAULT) String skin) {
		return skin+"/about/faqs";
	}

}
