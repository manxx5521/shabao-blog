/**
 *
 */
package com.xiaoshabao.blog.component.freemarker.directive;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.xiaoshabao.blog.component.freemarker.DirectiveFunction;
import com.xiaoshabao.blog.component.freemarker.TemplateDirective;

import freemarker.template.TemplateException;

/**
 * 资源路径处理
 * - 当 ${resource.domain} = true 时，自己在资源地址前面追加host
 */
@Component
public class ResourceDirective extends TemplateDirective {
	
	@Override
	protected void execute(DirectiveFunction function) throws TemplateException, IOException {
		String src = function.getString("src", "#");
		String base = function.getString("base");
		if (StringUtils.isBlank(base)) {
            base = function.getContextPath();
        }
		
		StringBuffer buf = new StringBuffer();
        buf.append(base);
        buf.append(src);
        function.renderString(buf.toString());
	}
	

}
