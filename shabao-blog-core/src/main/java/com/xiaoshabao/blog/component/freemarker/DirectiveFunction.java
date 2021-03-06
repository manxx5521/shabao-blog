package com.xiaoshabao.blog.component.freemarker;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import freemarker.core.Environment;
import freemarker.template.SimpleScalar;
import freemarker.template.SimpleSequence;
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
	private Map/*<String, TemplateModel>*/ params;
	/** 返回参数 */
	private TemplateModel[] loopVars;
	/** 输出实体 */
	private TemplateDirectiveBody body;
	/**当前指令域的变量集*/
	private Environment.Namespace namespace;
	
	private int rsize=0;
	
	private boolean isRender=true;
	
	private boolean hasParams=false;

	public DirectiveFunction(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) {
		super();
		this.env = env;
		this.params = params;
		this.loopVars = loopVars;
		this.body = body;
		this.namespace = env.getCurrentNamespace();
	}
	/**
	 * 获得参数的 TemplateModel
	 * @param key
	 * @return 没有时返回null
	 */
	public TemplateModel getParamModel(String key) {
		Object obj = params.get(key);
		if (obj == null && !(obj instanceof TemplateModel)) {
			return null;
		}
		return (TemplateModel) obj;
	}
	/**
	 * 返回变量字符串
	 * @param key
	 * @return 不存在时返回null
	 */
	public String getString(String key) {
		return TemplateModelUtils.converString(getParamModel(key));
	}
	/**
	 * 返回变量字符串
	 * @param key
	 * @return 不存在时返回null
	 */
	public String getString(String key,String defaultValue) {
		String value=getString(key);
		return value==null?defaultValue:value;
	}
	/**
	 * 返回变量数字变量
	 * @param key
	 * @return 不存在时返回null
	 */
	public Integer getInteger(String key) throws TemplateModelException {
        return TemplateModelUtils.converInteger(getParamModel(key));
    }
	/**
	 * 返回变量数字变量
	 * @param key
	 * @return
	 */
	public Integer getInteger(String key,Integer defaultValue) throws TemplateModelException {
		Integer value=getInteger(key);
		return value==null?defaultValue:value;
    }
	/**
	 * 验证变量
	 * <p>
	 * String name=function.verifyString("name",str->{<br>
     *		if(str.length()==0) {<br>
     *			throw new TemplateModelException("变量名称不存在");<br>
     *		}<br>
     *	});<br>
	 * 
	 * </p>
	 * @param key 变量key名称
	 * @param function 操作函数
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
	 * <p>
	 * 变量使用时需要声明返回变量result1，result1 
	 *  <@role userid='123456' roleid='admin'; result1,result2 >
	 * </p>
	 * @param obj
	 * @param index
	 */
	public DirectiveFunction addResult(Object obj) {
		this.addResult(obj,rsize);
		rsize++;
		hasParams=true;
		return this;
	}
	
	/**
	 * 添加返回变量
	 * <p>
	 * 变量使用时需要声明返回变量result1，result1 
	 *  <@role userid='123456' roleid='admin'; result1,result2 >
	 * </p>
	 * @param obj
	 * @param index
	 */
	public DirectiveFunction addResult(Object obj,int index) {
		if(obj instanceof String) {
			loopVars[index]=new SimpleScalar(obj.toString());
		}else if(obj instanceof Boolean) {
			loopVars[index]=(Boolean)obj?TemplateBooleanModel.TRUE:TemplateBooleanModel.FALSE;
		}else if(obj instanceof Collection&&((Collection)obj).size()>0){
			loopVars[index]=new SimpleSequence((Collection)obj,FreemarkerConsts.DEFAULT_OBJECT_WRAPPER);
		}
		return this;
	}
	/**
	 * 将一个变量返回当前指令域
	 * @param key
	 * @param value
	 * @return
	 * @throws TemplateModelException
	 */
	public DirectiveFunction put(String key, Object value) throws TemplateModelException {
        namespace.put(key, wrap(value));
        hasParams=true;
        return this;
    }
	/**
     * 包装对象
     * @param object
     * @return
     * @throws TemplateModelException
     */
    public TemplateModel wrap(Object object) throws TemplateModelException {
        return env.getObjectWrapper().wrap(object);
    }
	/**
	 * 显示指令里的内容
	 * @throws IOException 
	 * @throws TemplateException 
	 * @throws Exception
	 */
	public void render() throws TemplateException, IOException {
		if(isRender&&hasParams) {
			body.render(env.getOut());//输出变量
		}
	}
	/**
	 * 向指令位置写入字符串
	 * @param text
	 * @throws IOException
	 */
	public void renderString(String text) throws IOException {
        env.getOut().write(text);
    }

	/**
	 * 设置是否 render显示指令里的内容
	 * @param isRender
	 */
	public void setRender(boolean isRender) {
		this.isRender = isRender;
	}
	
	public String getContextPath() {
        String ret = null;
        try {
            ret =  TemplateModelUtils.converString(env.getVariable("base"));
        } catch (TemplateModelException e) {
        }
        return ret;
    }

}
