package com.xiaoshabao.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.xiaoshabao.base.controller.BaseController;
import com.xiaoshabao.blog.lang.Consts;

/**
 *主页
 */
@Controller
public class IndexController extends BaseController{
	
	@GetMapping(value= {"/", "/index"})
	public String root(ModelMap model
			,@RequestParam(defaultValue=Consts.order.NEWEST) String order,
			@RequestParam(defaultValue="1") Integer pn,
			@RequestParam(defaultValue=Consts.skin.DEFAULT) String skin) {
		model.put("order", order);
		model.put("pn", pn);
//		model.put("profile", "11");//临时 include.ftlh 32行 实际LOGIN_TOKEN: '${profile.id}'
		return skin+Views.INDEX;
	}
	
	

}
