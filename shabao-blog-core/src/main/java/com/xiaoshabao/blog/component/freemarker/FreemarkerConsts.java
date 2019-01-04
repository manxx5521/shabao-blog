package com.xiaoshabao.blog.component.freemarker;

import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.ObjectWrapper;
import freemarker.template.Version;

public interface FreemarkerConsts {
	
	/**
	 * 版本这里时freemarker,用来限制语法等
	 */
	Version VERSION=new Version("2.3.28");
	/**
	 * 对象包装器
	 * 关系：
	 * 类型	   FreeMarker接口	    FreeMarker实现
	      字符串    TemplateScalarModel	SimpleScalar
	      数值	   TemplateNumberModel	SimpleNumber
              日期	   TemplateDateModel	SimpleDate
              布尔	   TemplateBooleanModel	TemplateBooleanModel.TRUE
              哈希	   TemplateHashModel	SimpleHash
              序列	   TemplateSequenceModel	SimpleSequence
              集合	   TemplateCollectionModel	SimpleCollection
              节点	   TemplateNodeModel	NodeModel
	 */
	ObjectWrapper DEFAULT_OBJECT_WRAPPER=new DefaultObjectWrapperBuilder(VERSION).build();
	
	
	
	

}
