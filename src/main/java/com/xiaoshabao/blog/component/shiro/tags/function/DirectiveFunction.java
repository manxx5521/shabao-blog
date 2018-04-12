package com.xiaoshabao.blog.component.shiro.tags.function;

import java.io.IOException;
import java.util.Map;

import freemarker.core.Environment;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateBooleanModel;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * 指令操作函数
 */
@SuppressWarnings("rawtypes")
public class DirectiveFunction {

	private Environment env;
	/** 入参 */
	private Map params;
	/** 返回参数 */
	private TemplateModel[] loopVars;
	/** 输出实体 */
	private TemplateDirectiveBody body;
	
	private int rsize=0;
	
	private boolean isRender=true;

	public DirectiveFunction(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) {
		super();
		this.env = env;
		this.params = params;
		this.loopVars = loopVars;
		this.body = body;
	}
	/**
	 * 返回变量字符串
	 * @param key
	 * @return 不存在时返回null
	 */
	public String getString(String key) {
		Object obj = params.get(key);
		if (obj == null && !(obj instanceof SimpleScalar)) {
			return null;
		}
		return ((SimpleScalar) obj).getAsString();
	}
	/**
	 * 验证变量
	 * @param key
	 * @param function
	 * @return
	 * @throws TemplateModelException 变量错误验证不通时返回异常
	 */
	public String verifyString(String key, VerifyFunction<String> function) throws TemplateModelException {
		Object obj = params.get(key);
		if (obj == null && !(obj instanceof SimpleScalar)) {
			throw new TemplateModelException("变量" + key + "不存在");
		}
		String str = ((SimpleScalar) obj).getAsString();
		function.accept(str);
		return str;

	}
	
	/**
	 * 添加返回变量
	 * @param obj
	 * @param index
	 */
	public void addResult(Object obj) {
		this.addResult(obj,++rsize);
	}
	
	/**
	 * 添加返回变量
	 * @param obj
	 * @param index
	 */
	public void addResult(Object obj,int index) {
		if(obj instanceof String) {
			loopVars[index]=new SimpleScalar(obj.toString());
		}else if(obj instanceof Boolean) {
			loopVars[index]=(Boolean)obj?TemplateBooleanModel.TRUE:TemplateBooleanModel.FALSE;
		}
	}
	
	/**
	 * 显示指令里的内容
	 * @throws IOException 
	 * @throws TemplateException 
	 * @throws Exception
	 */
	public void render() throws TemplateException, IOException {
		if(isRender) {
			body.render(env.getOut());//输出变量
		}
	}

	/**
	 * 设置是否 render显示指令里的内容
	 * @param isRender
	 */
	public void setRender(boolean isRender) {
		this.isRender = isRender;
	}

}
