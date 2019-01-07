package com.xiaoshabao.blog.component;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;

import com.xiaoshabao.blog.component.shiro.ShiroUtil;
import com.xiaoshabao.blog.component.shiro.authc.AccountSubject;
import com.xiaoshabao.blog.dto.AccountProfile;
@Service("contextHolderShiroImpl")
public class ContextHolderShiroImpl implements ContextHolder{
	
	@Override
	public AccountProfile getProfile() {
		AccountSubject subject=ShiroUtil.getSubject();
		if(subject==null){
			return null;
		}
		return subject.getProfile();
	}

	@Override
	public void putProfile(AccountProfile profile) {
		SecurityUtils.getSubject().getSession(true).setAttribute("profile", profile);		
	}

	@Override
	public boolean isAuthenticated() {
		return SecurityUtils.getSubject().isAuthenticated();
	}

}
