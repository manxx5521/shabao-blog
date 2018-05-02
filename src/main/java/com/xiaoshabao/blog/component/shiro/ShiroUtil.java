package com.xiaoshabao.blog.component.shiro;



import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import com.xiaoshabao.blog.component.shiro.authc.AccountSubject;
import com.xiaoshabao.blog.dto.AccountProfile;

public class ShiroUtil {
	
	/**
	 * 获取登录信息
	 */
	public static AccountSubject getSubject(){
		Subject subject=SecurityUtils.getSubject();
		if(subject==null){
			return null;
		}
	    return (AccountSubject)subject ;
	}
	
	public static AccountProfile getProfile(){
		AccountSubject subject=ShiroUtil.getSubject();
		if(subject==null){
			return null;
		}
		return subject.getProfile();
	}
	
	public static void putProfile(AccountProfile profile) {
		SecurityUtils.getSubject().getSession(true).setAttribute("profile", profile);
	}

	public static AuthenticationToken createToken(String username, String password) {
		return new UsernamePasswordToken(username,DigestUtils.md5Hex(password));
	}
}
