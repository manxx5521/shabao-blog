package com.xiaoshabao.blog.component;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ServletContextAware;

import com.xiaoshabao.base.component.SysConfig;
import com.xiaoshabao.blog.lang.Consts;
import com.xiaoshabao.blog.service.ChannelService;

/**
 * 加载容器相关内容
 */
@Configuration
public class ServletInitializer implements ServletContextAware,ApplicationRunner{
	
	private ServletContext servletContext;
	@Autowired
	private SysConfig sysConfig;
	@Autowired
	private ChannelService channelService;
	
	private final static String[] KEYS= {"site_metas","site_domain","site_name","site_keywords","site_description"};

	@Override
	public void run(ApplicationArguments args) throws Exception {
		servletContext.setAttribute("base", servletContext.getContextPath());
		
		for(String key:KEYS) {
			servletContext.setAttribute(key,sysConfig.getString(key));
		}
		servletContext.setAttribute("channels", channelService.findAll(Consts.STATUS_NORMAL));
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext=servletContext;
	}

	


}
