package com.xiaoshabao.blog.component.freemarker;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.xiaoshabao.blog.component.freemarker.directive.ContentsDirective;
import com.xiaoshabao.blog.component.freemarker.directive.ResourceDirective;
import com.xiaoshabao.blog.component.freemarker.directive.TimeAgoMethod;
import com.xiaoshabao.blog.component.shiro.tags.ShiroTags;

import freemarker.template.Configuration;
import freemarker.template.TemplateModelException;

/**
 * freemarker配置类
 */
@Component
public class FreemarkerConfig {
    @Autowired
    private Configuration configuration;
    
    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void setSharedVariable() throws TemplateModelException {
//        configuration.setSharedVariable("author_contents", applicationContext.getBean(AuthorContentsDirective.class));
//        configuration.setSharedVariable("channel", applicationContext.getBean(ChannelDirective.class));
        configuration.setSharedVariable("contents", applicationContext.getBean(ContentsDirective.class));
//        configuration.setSharedVariable("num", applicationContext.getBean(NumberDirective.class));
        configuration.setSharedVariable("resource", applicationContext.getBean(ResourceDirective.class));
//        configuration.setSharedVariable("authc", applicationContext.getBean(AuthcDirective.class));
//        configuration.setSharedVariable("banner", applicationContext.getBean(BannerDirective.class));
//
        configuration.setSharedVariable("timeAgo", new TimeAgoMethod());
        configuration.setSharedVariable("shiro", new ShiroTags());
    }
}
