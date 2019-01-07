package com.xiaoshabao.blog.component.freemarker;

import java.io.IOException;
import java.util.Map;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 自定指令统一操作
 */
@SuppressWarnings("rawtypes")
public abstract class TemplateDirective implements TemplateDirectiveModel {
	
	protected static String RESULT = "result";
    protected static String RESULTS = "results";
	
	//自定义指令入口函数
	@Override
	public final void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		DirectiveFunction function=new DirectiveFunction(env, params, loopVars, body);
		this.execute(function);
		function.render();
	}
	
	protected abstract void execute(DirectiveFunction function) throws TemplateException, IOException ;

   
    
}
