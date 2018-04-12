package com.xiaoshabao.blog.component.shiro.tags;

import com.xiaoshabao.blog.component.shiro.tags.function.DirectiveFunction;
import com.xiaoshabao.blog.component.shiro.tags.function.VerifyFunction;
import com.xiaoshabao.blog.lang.Consts;

import freemarker.template.TemplateModelException;

/**
 * <p>Equivalent to {@link org.apache.shiro.web.tags.PermissionTag}</p>
 */
public abstract class PermissionTag extends SecureTag {

    @Override
	protected void execute(DirectiveFunction function) throws TemplateModelException {
    	VerifyFunction<String> f=(str)->{
    		if(str.length()==0) {
    			throw new TemplateModelException("变量名称不存在");
    		}
    	};
    	String name=function.verifyString("name",f);
    	
    	boolean show = showTagBody(name);
        if (!show) {
        	function.setRender(false);
        }
		
	}

	/**
     * 判断是否拥有指定权限
     * - 超级管理员例外, 拥有所有
     * @param p 权限代码
     * @return true/false
     */
    protected boolean isPermitted(String p) {
        return getSubject() != null && (getSubject().hasRole(Consts.ROLE_ADMIN) || getSubject().isPermitted(p));
    }

    protected abstract boolean showTagBody(String p);
}
