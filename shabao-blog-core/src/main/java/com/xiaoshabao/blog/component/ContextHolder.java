package com.xiaoshabao.blog.component;

import com.xiaoshabao.blog.dto.AccountProfile;

public interface ContextHolder {
	
	AccountProfile getProfile();
	
	void putProfile(AccountProfile profile);
	
	/**
	 * 是否通过身份验证
	 * @return
	 */
	boolean isAuthenticated();

}
