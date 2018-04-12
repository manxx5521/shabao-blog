package com.xiaoshabao.blog.component.shiro.tags;

import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;

/**
 * shiro指令
 */
public class ShiroTags extends SimpleHash {

	private static final long serialVersionUID = 2578819184945561599L;

	public ShiroTags() {
//		this.super();
    	put("hasPermission", new HasPermissionTag());
    	/*
        put("authenticated", new AuthenticatedTag());
        put("guest", new GuestTag());
        put("hasAnyRoles", new HasAnyRolesTag());
        
        put("hasRole", new HasRoleTag());
        put("lacksPermission", new LacksPermissionTag());
        put("lacksRole", new LacksRoleTag());
        put("notAuthenticated", new NotAuthenticatedTag());
        put("principal", new PrincipalTag());
        put("user", new UserTag());*/
    }

}
