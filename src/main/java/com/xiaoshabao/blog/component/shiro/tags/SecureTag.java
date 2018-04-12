package com.xiaoshabao.blog.component.shiro.tags;

import freemarker.core.Environment;
import freemarker.template.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.xiaoshabao.blog.component.shiro.tags.function.DirectiveFunction;

import java.io.IOException;
import java.util.Map;

/**
 * <p>Equivalent to {@link org.apache.shiro.web.tags.SecureTag}</p>
 */
@SuppressWarnings("rawtypes")
public abstract class SecureTag implements TemplateDirectiveModel {
	
	//自定义指令入口函数
	@Override
	public final void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		DirectiveFunction function=new DirectiveFunction(env, params, loopVars, body);
		this.execute(function);
		function.render();
	}
	
	protected abstract void execute(DirectiveFunction function) throws TemplateException, IOException ;

    protected final Subject getSubject() {
        return SecurityUtils.getSubject();
    }
    
}
