package com.xiaoshabao.blog.component.shiro;



import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;

import com.xiaoshabao.blog.component.shiro.authc.AccountSubject;

public class ShiroUtil {
	
	/**
	 * 获取登录信息
	 * 
	 * @return
	 */
	public static AccountSubject getSubject(){
	    return (AccountSubject) SecurityUtils.getSubject();
	}

	public static AuthenticationToken createToken(String username, String password) {
		return new UsernamePasswordToken(username,DigestUtils.md5Hex(password));
	}
}
