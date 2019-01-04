package com.xiaoshabao.blog.component.freemarker;

import freemarker.template.TemplateModelException;

/**
 * 验证参数函数
 * 带输入不带返回值的函数（抛出异常）
 * @param <T> 输入参数值
 */
public interface VerifyFunction<T> {

	/**
	 * 执行
	 * @param t
	 * @throws TemplateModelException 参数验证不通过时，可以通过此异常返回信息
	 */
	void accept(T t) throws TemplateModelException;
}
