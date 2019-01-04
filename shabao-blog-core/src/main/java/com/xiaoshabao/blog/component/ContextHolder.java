package com.xiaoshabao.blog.component;

import com.xiaoshabao.blog.dto.AccountProfile;

public interface ContextHolder {
	
	AccountProfile getProfile();
	
	void putProfile(AccountProfile profile);

}
