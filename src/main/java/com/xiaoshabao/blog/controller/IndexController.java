package com.xiaoshabao.blog.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiaoshabao.base.component.SysConfig;
import com.xiaoshabao.base.component.SysEnum;
import com.xiaoshabao.base.controller.BaseController;

/**
 * @author langhsu
 *
 */
@Controller
public class IndexController extends BaseController{
	@Autowired
	private SysConfig sysConfig;
	
	@RequestMapping(value= {"/", "/index"})
	public String root(ModelMap model, HttpServletRequest request) {
		String order = ServletRequestUtils.getStringParameter(request, "order",/* Consts.order.NEWEST*/"newest");
		int pn = ServletRequestUtils.getIntParameter(request, "pn", 1);
		model.put("order", order);
		model.put("pn", pn);
		return "default"+Views.INDEX;
	}
	
	@ResponseBody
	@GetMapping("/test")
	public String test(){
		return sysConfig.getString(SysEnum.DOMAIN.getName());
	}

}
